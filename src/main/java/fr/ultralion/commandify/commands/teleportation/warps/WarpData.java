package fr.ultralion.commandify.commands.teleportation.warps;

import com.google.gson.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class WarpData {

    private static final String FILE_NAME = "warps.json";

    public static class ConsoleColors {
        public static final String RESET = "\u001B[0m";
        public static final String RED = "\u001B[31m";
        public static final String GREEN = "\u001B[32m";
        public static final String YELLOW = "\u001B[33m";
        public static final String CYAN = "\u001B[36m";
    }

    public static class Warp {
        public final String name;
        public final String dimension;
        public final double x, y, z;
        public final float yaw, pitch;

        public Warp(String name, String dimension, double x, double y, double z, float yaw, float pitch) {
            this.name = name;
            this.dimension = dimension;
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;
        }
    }

    private static Path getFile(MinecraftServer server) {
        return server.getWorldPath(LevelResource.ROOT)
                .resolve("data")
                .resolve("commandify")
                .resolve(FILE_NAME);
    }

    public static Map<String, Warp> loadWarps(MinecraftServer server) {
        Map<String, Warp> warps = new HashMap<>();
        Path file = getFile(server);
        if (!Files.exists(file)) {
            System.out.println(ConsoleColors.YELLOW + "[Commandify] No warp file found, skipping load." + ConsoleColors.RESET);
            return warps;
        }

        try (Reader reader = Files.newBufferedReader(file)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            for (String key : json.keySet()) {
                JsonObject data = json.getAsJsonObject(key);
                warps.put(key, new Warp(
                        key,
                        data.get("dimension").getAsString(),
                        data.get("x").getAsDouble(),
                        data.get("y").getAsDouble(),
                        data.get("z").getAsDouble(),
                        data.get("yaw").getAsFloat(),
                        data.get("pitch").getAsFloat()
                ));
            }
            System.out.println(ConsoleColors.GREEN + "[Commandify] Loaded " + warps.size() + " warps." + ConsoleColors.RESET);
        } catch (IOException e) {
            System.out.println(ConsoleColors.RED + "[Commandify] Failed to load warps: " + e.getMessage() + ConsoleColors.RESET);
        }
        return warps;
    }

    public static void saveWarps(MinecraftServer server, Map<String, Warp> warps) {
        try {
            Path file = getFile(server);
            Files.createDirectories(file.getParent());

            JsonObject json = new JsonObject();
            for (var entry : warps.entrySet()) {
                Warp w = entry.getValue();
                JsonObject data = new JsonObject();
                data.addProperty("dimension", w.dimension);
                data.addProperty("x", w.x);
                data.addProperty("y", w.y);
                data.addProperty("z", w.z);
                data.addProperty("yaw", w.yaw);
                data.addProperty("pitch", w.pitch);
                json.add(entry.getKey(), data);
            }

            try (Writer writer = Files.newBufferedWriter(file)) {
                new GsonBuilder().setPrettyPrinting().create().toJson(json, writer);
            }

            System.out.println(ConsoleColors.GREEN + "[Commandify] Saved " + warps.size() + " warps." + ConsoleColors.RESET);
        } catch (IOException e) {
            System.out.println(ConsoleColors.RED + "[Commandify] Failed to save warps: " + e.getMessage() + ConsoleColors.RESET);
        }
    }
}
