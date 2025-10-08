package fr.ultralion.commandify.commands.teleportation.spawn.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SpawnData {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_NAME = "commandify_spawn.json";

    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public SpawnData(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public static SpawnData load(MinecraftServer server) {
        File worldDir = server.getWorldPath(LevelResource.ROOT).toFile();
        File file = new File(worldDir, FILE_NAME);
        if (!file.exists()) return null;

        try (FileReader reader = new FileReader(file)) {
            return GSON.fromJson(reader, SpawnData.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void save(MinecraftServer server) {
        File worldDir = server.getWorldPath(LevelResource.ROOT).toFile();
        File file = new File(worldDir, FILE_NAME);
        try (FileWriter writer = new FileWriter(file)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BlockPos toBlockPos() {
        return new BlockPos((int) x, (int) y, (int) z);
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public float getYaw() { return yaw; }
    public float getPitch() { return pitch; }
}
