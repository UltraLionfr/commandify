package fr.ultralion.commandify.commands.teleportation.spawn;

import com.mojang.brigadier.CommandDispatcher;
import fr.ultralion.commandify.Config;
import fr.ultralion.commandify.commands.teleportation.spawn.data.SpawnData;
import fr.ultralion.commandify.util.Lang;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class SetSpawnCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("setspawn")
                        .requires(source -> source.hasPermission(2))
                        .executes(ctx -> {
                            CommandSourceStack source = ctx.getSource();

                            if (!Config.ENABLE_SETSPAWN_COMMAND.get()) {
                                source.sendFailure(Lang.t("commandify.setspawn.disabled"));
                                return 0;
                            }

                            if (!(source.getEntity() instanceof ServerPlayer player)) {
                                source.sendFailure(Lang.t("commandify.setspawn.player_only"));
                                return 0;
                            }

                            double x = player.getX();
                            double y = player.getY();
                            double z = player.getZ();
                            float yaw = player.getYRot();
                            float pitch = player.getXRot();

                            SpawnData data = new SpawnData(x, y, z, yaw, pitch);
                            data.save(player.getServer());

                            source.sendSuccess(() -> Lang.t("commandify.setspawn.success"), false);
                            return 1;
                        })
        );
    }
}
