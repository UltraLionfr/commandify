package fr.ultralion.commandify.commands.teleportation.spawn;

import com.mojang.brigadier.CommandDispatcher;
import fr.ultralion.commandify.Config;
import fr.ultralion.commandify.commands.teleportation.spawn.data.SpawnData;
import fr.ultralion.commandify.util.Lang;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class SpawnCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("spawn")
                        .executes(ctx -> {
                            CommandSourceStack source = ctx.getSource();

                            if (!Config.ENABLE_SPAWN_COMMAND.get()) {
                                source.sendFailure(Lang.t("commandify.spawn.disabled"));
                                return 0;
                            }

                            if (!(source.getEntity() instanceof ServerPlayer player)) {
                                source.sendFailure(Lang.t("commandify.spawn.player_only"));
                                return 0;
                            }

                            SpawnData data = SpawnData.load(player.getServer());
                            if (data == null) {
                                player.sendSystemMessage(Lang.t("commandify.spawn.not_set"));
                                return 0;
                            }

                            ServerLevel world = player.getServer().getLevel(Level.OVERWORLD);
                            if (world == null) {
                                player.sendSystemMessage(Lang.t("commandify.spawn.no_world"));
                                return 0;
                            }

                            player.teleportTo(world, data.getX(), data.getY(), data.getZ(), data.getYaw(), data.getPitch());
                            player.sendSystemMessage(Lang.t("commandify.spawn.success"));

                            return 1;
                        })
        );
    }
}
