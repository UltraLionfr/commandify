package fr.ultralion.commandify.commands.teleportation.back;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.core.registries.Registries;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BackManager {

    private static final Map<UUID, BackLocation> lastLocations = new HashMap<>();

    public static class BackLocation {
        public final String dimension;
        public final double x, y, z;
        public final float yaw, pitch;

        public BackLocation(String dimension, double x, double y, double z, float yaw, float pitch) {
            this.dimension = dimension;
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;
        }
    }

    public static void saveLastLocation(ServerPlayer player) {
        String dim = player.level().dimension().location().toString();
        lastLocations.put(player.getUUID(), new BackLocation(dim, player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot()));
    }

    public static boolean teleportBack(ServerPlayer player) {
        BackLocation loc = lastLocations.get(player.getUUID());
        if (loc == null) return false;

        String[] parts = loc.dimension.split(":");
        String namespace = parts.length > 1 ? parts[0] : "minecraft";
        String path = parts.length > 1 ? parts[1] : loc.dimension;

        ResourceKey<Level> dim = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(namespace, path));
        ServerLevel world = player.getServer().getLevel(dim);
        if (world == null) return false;

        player.teleportTo(world, loc.x, loc.y, loc.z, loc.yaw, loc.pitch);
        return true;
    }
}
