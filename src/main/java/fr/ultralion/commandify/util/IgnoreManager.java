package fr.ultralion.commandify.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class IgnoreManager {

    private static final Map<UUID, Set<UUID>> CACHE = new HashMap<>();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static File BASE_DIR;

    public static void init(MinecraftServer server) {
        File dataDir = new File(server.getWorldPath(net.minecraft.world.level.storage.LevelResource.ROOT).toFile(), "data/commandify/ignored");
        if (!dataDir.exists()) dataDir.mkdirs();
        BASE_DIR = dataDir;

        File[] files = BASE_DIR.listFiles((dir, name) -> name.endsWith(".json"));
        if (files != null) {
            for (File f : files) {
                try (Reader reader = new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8)) {
                    UUID playerId = UUID.fromString(f.getName().replace(".json", ""));
                    List<String> list = GSON.fromJson(reader, new TypeToken<List<String>>() {
                    }.getType());
                    if (list != null) {
                        Set<UUID> ignored = new HashSet<>();
                        for (String s : list) ignored.add(UUID.fromString(s));
                        CACHE.put(playerId, ignored);
                    }
                } catch (Exception e) {
                    System.out.println(ConsoleColors.RED + "[Commandify] Failed to load ignore file " + f.getName() + ": " + e.getMessage() + ConsoleColors.RESET);
                }
            }
        }

        System.out.println(ConsoleColors.GREEN + "[Commandify] IgnoreManager initialized (" + CACHE.size() + " files loaded)." + ConsoleColors.RESET);
    }

    public static void ignore(ServerPlayer ignorer, ServerPlayer target) {
        Set<UUID> ignored = CACHE.computeIfAbsent(ignorer.getUUID(), k -> new HashSet<>());
        ignored.add(target.getUUID());
        save(ignorer.getUUID());
    }

    public static void unignore(ServerPlayer ignorer, ServerPlayer target) {
        Set<UUID> ignored = CACHE.get(ignorer.getUUID());
        if (ignored != null) {
            ignored.remove(target.getUUID());
            if (ignored.isEmpty()) {
                CACHE.remove(ignorer.getUUID());
                deleteFile(ignorer.getUUID());
            } else {
                save(ignorer.getUUID());
            }
        }
    }

    public static boolean isIgnoring(ServerPlayer ignorer, ServerPlayer target) {
        Set<UUID> ignored = CACHE.get(ignorer.getUUID());
        return ignored != null && ignored.contains(target.getUUID());
    }

    public static Set<UUID> getIgnoredList(ServerPlayer player) {
        return CACHE.getOrDefault(player.getUUID(), Collections.emptySet());
    }

    private static void save(UUID playerId) {
        if (BASE_DIR == null) return;
        Set<UUID> ignored = CACHE.get(playerId);
        File file = new File(BASE_DIR, playerId.toString() + ".json");

        if (ignored == null || ignored.isEmpty()) {
            if (file.exists()) file.delete();
            return;
        }

        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            List<String> list = new ArrayList<>();
            for (UUID id : ignored) list.add(id.toString());
            GSON.toJson(list, writer);
        } catch (IOException e) {
            System.out.println(ConsoleColors.RED + "[Commandify] Failed to save ignore file for " + playerId + ": " + e.getMessage() + ConsoleColors.RESET);
        }
    }

    private static void deleteFile(UUID playerId) {
        if (BASE_DIR == null) return;
        File file = new File(BASE_DIR, playerId.toString() + ".json");
        if (file.exists()) file.delete();
    }

    public static void clearAll() {
        CACHE.clear();
        if (BASE_DIR == null) return;
        File[] files = BASE_DIR.listFiles();
        if (files != null) {
            for (File f : files) f.delete();
        }
        System.out.println(ConsoleColors.YELLOW + "[Commandify] All ignore data cleared." + ConsoleColors.RESET);
    }

    /**
     * Console colors for better readability
     */
    public static class ConsoleColors {
        public static final String RESET = "\u001B[0m";
        public static final String RED = "\u001B[31m";
        public static final String GREEN = "\u001B[32m";
        public static final String YELLOW = "\u001B[33m";
        public static final String CYAN = "\u001B[36m";
    }
}
