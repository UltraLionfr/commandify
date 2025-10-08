package fr.ultralion.commandify.commands.teleportation.teleport;

import com.mojang.brigadier.CommandDispatcher;
import fr.ultralion.commandify.util.Lang;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class CommandTpatoggle {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("tpatoggle")
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();

                            boolean enabled = TeleportManager.toggleTpa(player);

                            if (enabled) {
                                player.sendSystemMessage(Lang.t("commandify.tpatoggle.enabled"));
                            } else {
                                player.sendSystemMessage(Lang.t("commandify.tpatoggle.disabled"));
                            }

                            return 1;
                        })
        );
    }
}
