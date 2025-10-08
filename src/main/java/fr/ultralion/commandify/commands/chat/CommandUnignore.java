package fr.ultralion.commandify.commands.chat;

import com.mojang.brigadier.CommandDispatcher;
import fr.ultralion.commandify.util.IgnoreManager;
import fr.ultralion.commandify.util.Lang;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

public class CommandUnignore {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("unignore")
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(ctx -> {
                                    ServerPlayer sender = ctx.getSource().getPlayerOrException();
                                    ServerPlayer target = EntityArgument.getPlayer(ctx, "player");

                                    if (!IgnoreManager.isIgnoring(sender, target)) {
                                        sender.sendSystemMessage(Lang.t("commandify.unignore.not_ignored", target.getName().getString()));
                                        return 0;
                                    }

                                    IgnoreManager.unignore(sender, target);
                                    sender.sendSystemMessage(Lang.t("commandify.unignore.success", target.getName().getString()));
                                    return 1;
                                })
                        )
        );
    }
}
