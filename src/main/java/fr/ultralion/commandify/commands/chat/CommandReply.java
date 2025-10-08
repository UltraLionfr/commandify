package fr.ultralion.commandify.commands.chat;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import fr.ultralion.commandify.util.ChatManager;
import fr.ultralion.commandify.util.IgnoreManager;
import fr.ultralion.commandify.util.Lang;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class CommandReply {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("r")
                        .then(Commands.argument("message", StringArgumentType.greedyString())
                                .executes(ctx -> {
                                    ServerPlayer sender = ctx.getSource().getPlayerOrException();
                                    String message = StringArgumentType.getString(ctx, "message");

                                    UUID lastUUID = ChatManager.getLastMessaged(sender);
                                    if (lastUUID == null) {
                                        sender.sendSystemMessage(Lang.t("commandify.reply.none"));
                                        return 0;
                                    }

                                    ServerPlayer target = sender.server.getPlayerList().getPlayer(lastUUID);
                                    if (target == null) {
                                        sender.sendSystemMessage(Lang.t("commandify.reply.offline"));
                                        return 0;
                                    }

                                    if (IgnoreManager.isIgnoring(target, sender)) {
                                        sender.sendSystemMessage(Lang.t("commandify.reply.ignored"));
                                        return 0;
                                    }

                                    Component toSender = Lang.t("commandify.reply.to_sender", target.getName().getString(), message);
                                    Component toTarget = Lang.t("commandify.reply.to_target", sender.getName().getString(), message);

                                    sender.sendSystemMessage(toSender);
                                    target.sendSystemMessage(toTarget);

                                    ChatManager.setLastMessaged(sender, target);

                                    return 1;
                                })
                        )
        );
    }
}
