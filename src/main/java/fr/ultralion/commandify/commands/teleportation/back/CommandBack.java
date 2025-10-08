package fr.ultralion.commandify.commands.teleportation.back;

import com.mojang.brigadier.CommandDispatcher;
import fr.ultralion.commandify.Config;
import fr.ultralion.commandify.util.Lang;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class CommandBack {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("back")
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();

                            if (!Config.ENABLE_BACK_COMMAND.get()) {
                                player.sendSystemMessage(Lang.t("commandify.back.disabled"));
                                return 0;
                            }

                            boolean ok = BackManager.teleportBack(player);
                            if (!ok) {
                                player.sendSystemMessage(Lang.t("commandify.back.no_previous"));
                                return 0;
                            }

                            player.sendSystemMessage(Lang.t("commandify.back.success"));
                            return 1;
                        })
        );
    }
}
