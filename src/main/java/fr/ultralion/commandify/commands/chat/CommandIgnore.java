package fr.ultralion.commandify.commands.chat;

import com.mojang.brigadier.CommandDispatcher;
import fr.ultralion.commandify.util.IgnoreManager;
import fr.ultralion.commandify.util.Lang;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

public class CommandIgnore {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("ignore")
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(ctx -> {
                                    ServerPlayer sender = ctx.getSource().getPlayerOrException();
                                    ServerPlayer target = EntityArgument.getPlayer(ctx, "player");

                                    if (sender.getUUID().equals(target.getUUID())) {
                                        sender.sendSystemMessage(Lang.t("commandify.ignore.self"));
                                        return 0;
                                    }

                                    if (IgnoreManager.isIgnoring(sender, target)) {
                                        sender.sendSystemMessage(Lang.t("commandify.ignore.already", target.getName().getString()));
                                        return 0;
                                    }

                                    IgnoreManager.ignore(sender, target);
                                    sender.sendSystemMessage(Lang.t("commandify.ignore.added", target.getName().getString()));
                                    return 1;
                                })
                        )
        );
    }
}
