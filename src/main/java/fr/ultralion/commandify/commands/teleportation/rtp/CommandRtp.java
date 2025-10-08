package fr.ultralion.commandify.commands.teleportation.rtp;

import com.mojang.brigadier.CommandDispatcher;
import fr.ultralion.commandify.Config;
import fr.ultralion.commandify.util.Lang;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.*;

public class CommandRtp {

    private static final int MAX_ATTEMPTS = 40;
    private static final Map<UUID, Long> rtpCooldowns = new HashMap<>();
    private static final Random RANDOM = new Random();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("rtp")
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            ServerLevel world = player.getServer().getLevel(Level.OVERWORLD);

                            if (world == null) {
                                player.sendSystemMessage(Lang.t("commandify.rtp.no_world"));
                                return 0;
                            }

                            boolean enabled;
                            int globalRange;
                            int cooldownSeconds;

                            try {
                                enabled = Config.ENABLE_RTP_COMMAND.get();
                                globalRange = Config.RTP_MAX_DISTANCE.get();
                                cooldownSeconds = Config.RTP_COOLDOWN.get();
                            } catch (IllegalStateException e) {
                                enabled = true;
                                globalRange = 30000;
                                cooldownSeconds = 60;
                            }

                            if (!enabled) {
                                player.sendSystemMessage(Lang.t("commandify.rtp.disabled"));
                                return 0;
                            }

                            UUID uuid = player.getUUID();
                            long now = System.currentTimeMillis();
                            long cooldownEnd = rtpCooldowns.getOrDefault(uuid, 0L);
                            long cooldownMs = cooldownSeconds * 1000L;

                            if (now < cooldownEnd) {
                                long secondsLeft = (cooldownEnd - now) / 1000;
                                player.sendSystemMessage(Lang.t("commandify.rtp.cooldown", String.valueOf(secondsLeft)));
                                return 0;
                            }

                            player.sendSystemMessage(Lang.t("commandify.rtp.searching"));

                            final ServerLevel worldFinal = world;
                            final long nowFinal = now;
                            final long cooldownMsFinal = cooldownMs;
                            final int rangeFinal = globalRange;

                            new Thread(() -> {
                                BlockPos safePos = findSafePosition(worldFinal, rangeFinal);

                                if (safePos == null) {
                                    player.server.execute(() ->
                                            player.sendSystemMessage(Lang.t("commandify.rtp.no_safe"))
                                    );
                                    return;
                                }

                                rtpCooldowns.put(uuid, nowFinal + cooldownMsFinal);

                                player.server.execute(() -> {
                                    if (!player.isAlive()) return;

                                    player.teleportTo(
                                            worldFinal,
                                            safePos.getX() + 0.5,
                                            safePos.getY(),
                                            safePos.getZ() + 0.5,
                                            player.getYRot(),
                                            player.getXRot()
                                    );

                                    player.sendSystemMessage(Lang.t(
                                            "commandify.rtp.success",
                                            safePos.getX() + " " + safePos.getY() + " " + safePos.getZ()
                                    ));
                                });

                            }, "Commandify-RTP-Thread-" + player.getName().getString()).start();

                            return 1;
                        })
        );
    }

    private static BlockPos findSafePosition(ServerLevel world, int maxDistance) {
        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            int x = RANDOM.nextInt(maxDistance * 2) - maxDistance;
            int z = RANDOM.nextInt(maxDistance * 2) - maxDistance;

            world.getChunk(new BlockPos(x, 0, z));
            int y = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
            BlockPos pos = new BlockPos(x, y, z);

            if (!isSafe(world, pos)) continue;

            String biomeName = getBiomeName(world, pos);
            if (biomeName.contains("ocean") || biomeName.contains("river") || biomeName.contains("deep")) continue;

            return pos;
        }
        return null;
    }

    private static boolean isSafe(ServerLevel world, BlockPos pos) {
        if (pos.getY() <= world.getMinBuildHeight() + 1 || pos.getY() >= world.getMaxBuildHeight() - 2)
            return false;

        var below = world.getBlockState(pos.below());
        var at = world.getBlockState(pos);
        var above = world.getBlockState(pos.above());

        if (!below.isSolidRender(world, pos.below())) return false;
        if (!at.isAir() || !above.isAir()) return false;

        return !(below.is(Blocks.LAVA) ||
                below.is(Blocks.WATER) ||
                below.is(Blocks.MAGMA_BLOCK) ||
                below.is(Blocks.CACTUS) ||
                below.is(Blocks.FIRE) ||
                below.is(Blocks.POWDER_SNOW));
    }

    private static String getBiomeName(ServerLevel world, BlockPos pos) {
        try {
            var biome = world.getBiome(pos).value();
            Optional<ResourceKey<Biome>> biomeKey = world.registryAccess()
                    .registryOrThrow(net.minecraft.core.registries.Registries.BIOME)
                    .getResourceKey(biome);
            return biomeKey.map(key -> key.location().toString()).orElse("unknown");
        } catch (Exception e) {
            return "unknown";
        }
    }
}