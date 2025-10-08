package fr.ultralion.commandify.commands.teleportation.homes;

import com.google.gson.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class HomeData {

    /** ANSI color codes for readable console output */
    public static class ConsoleColors {
        public static final String RESET = "\u001B[0m";
        public static final String RED = "\u001B[31m";
        public static final String GREEN = "\u001B[32m";
        public static final String YELLOW = "\u001B[33m";
        public static final String CYAN = "\u001B[36m";
    }

    private static Path getHomesDir(MinecraftServer server) {
        return server.getWorldPath(LevelResource.ROOT)
                .resolve("data")
                .resolve("commandify")
                .resolve("homes");
    }

    public static class Home {
        public final String name;
        public final String dimension;
        public final double x, y, z;
        public final float yaw, pitch;

        public Home(String name, String dimension, double x, double y, double z, float yaw, float pitch) {
            this.name = name;
            this.dimension = dimension;
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;
        }
    }

    public static Map<String, Home> loadHomes(ServerPlayer player) {
        Map<String, Home> homes = new HashMap<>();
        try {
            Path dir = getHomesDir(player.getServer());
            Files.createDirectories(dir);
            Path file = dir.resolve(player.getUUID() + ".json");

            if (!Files.exists(file)) {
                System.out.println(ConsoleColors.YELLOW + "[Commandify] No homes file found for " + player.getName().getString() + ConsoleColors.RESET);
                return homes;
            }

            try (Reader reader = Files.newBufferedReader(file)) {
                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                for (String key : json.keySet()) {
                    JsonObject data = json.getAsJsonObject(key);
                    homes.put(key, new Home(
                            key,
                            data.get("dimension").getAsString(),
                            data.get("x").getAsDouble(),
                            data.get("y").getAsDouble(),
                            data.get("z").getAsDouble(),
                            data.get("yaw").getAsFloat(),
                            data.get("pitch").getAsFloat()
                    ));
                }
            }

            System.out.println(ConsoleColors.GREEN + "[Commandify] Loaded " + homes.size() + " homes for " + player.getName().getString() + ConsoleColors.RESET);

        } catch (IOException e) {
            System.out.println(ConsoleColors.RED + "[Commandify] Failed to load homes for " + player.getName().getString() + ": " + e.getMessage() + ConsoleColors.RESET);
        }
        return homes;
    }

    public static void saveHomes(ServerPlayer player, Map<String, Home> homes) {
        try {
            Path dir = getHomesDir(player.getServer());
            Files.createDirectories(dir);
            Path file = dir.resolve(player.getUUID() + ".json");

            JsonObject json = new JsonObject();
            for (var entry : homes.entrySet()) {
                Home h = entry.getValue();
                JsonObject data = new JsonObject();
                data.addProperty("dimension", h.dimension);
                data.addProperty("x", h.x);
                data.addProperty("y", h.y);
                data.addProperty("z", h.z);
                data.addProperty("yaw", h.yaw);
                data.addProperty("pitch", h.pitch);
                json.add(entry.getKey(), data);
            }

            try (Writer writer = Files.newBufferedWriter(file)) {
                new GsonBuilder().setPrettyPrinting().create().toJson(json, writer);
            }

            System.out.println(ConsoleColors.GREEN + "[Commandify] Saved " + homes.size() + " homes for " + player.getName().getString() + ConsoleColors.RESET);

        } catch (IOException e) {
            System.out.println(ConsoleColors.RED + "[Commandify] Failed to save homes for " + player.getName().getString() + ": " + e.getMessage() + ConsoleColors.RESET);
        }
    }
}