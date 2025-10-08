package fr.ultralion.commandify.commands.chat;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.ultralion.commandify.util.ChatManager;
import fr.ultralion.commandify.util.IgnoreManager;
import fr.ultralion.commandify.util.Lang;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class CommandMsg {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        LiteralArgumentBuilder<CommandSourceStack> base = Commands.literal("msg")
                .then(Commands.argument("target", EntityArgument.player())
                        .then(Commands.argument("message", StringArgumentType.greedyString())
                                .executes(ctx -> {
                                    ServerPlayer sender = ctx.getSource().getPlayerOrException();
                                    ServerPlayer target = EntityArgument.getPlayer(ctx, "target");
                                    String message = StringArgumentType.getString(ctx, "message");

                                    if (sender.getUUID().equals(target.getUUID())) {
                                        sender.sendSystemMessage(Lang.t("commandify.msg.self"));
                                        return 0;
                                    }

                                    if (IgnoreManager.isIgnoring(target, sender)) {
                                        sender.sendSystemMessage(Lang.t("commandify.msg.ignored"));
                                        return 0;
                                    }

                                    Component toSender = Lang.t("commandify.msg.to_sender", target.getName().getString(), message);
                                    Component toTarget = Lang.t("commandify.msg.to_target", sender.getName().getString(), message);

                                    sender.sendSystemMessage(toSender);
                                    target.sendSystemMessage(toTarget);

                                    ChatManager.setLastMessaged(sender, target);
                                    return 1;
                                })
                        )
                );

        dispatcher.register(base);

        dispatcher.register(Commands.literal("tell").redirect(base.build()));
        dispatcher.register(Commands.literal("w").redirect(base.build()));
    }
}
