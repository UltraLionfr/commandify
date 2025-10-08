package fr.ultralion.commandify.commands.teleportation.teleport;

import com.mojang.brigadier.CommandDispatcher;
import fr.ultralion.commandify.util.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

public class CommandTpahere {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("tpahere")
                        .then(Commands.argument("player", net.minecraft.commands.arguments.EntityArgument.player())
                                .executes(ctx -> {
                                    ServerPlayer sender = ctx.getSource().getPlayerOrException();
                                    ServerPlayer target = net.minecraft.commands.arguments.EntityArgument.getPlayer(ctx, "player");

                                    if (target.getUUID().equals(sender.getUUID())) {
                                        sender.sendSystemMessage(Lang.t("commandify.tpahere.self"));
                                        return 0;
                                    }

                                    if (!TeleportManager.sendRequest(sender, target, true)) {
                                        sender.sendSystemMessage(Lang.t("commandify.tpahere.blocked"));
                                        return 0;
                                    }

                                    sender.sendSystemMessage(Lang.t("commandify.tpahere.sent", target.getName().getString()));

                                    MutableComponent acceptButton = Lang.raw("commandify.tpahere.accept_button").copy()
                                            .withStyle(style -> style.withColor(ChatFormatting.GREEN)
                                                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept"))
                                                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Lang.t("commandify.tpahere.accept_hover"))));

                                    MutableComponent denyButton = Lang.raw("commandify.tpahere.deny_button").copy()
                                            .withStyle(style -> style.withColor(ChatFormatting.RED)
                                                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpadeny"))
                                                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Lang.t("commandify.tpahere.deny_hover"))));

                                    Component message = Component.empty()
                                            .append(Component.literal(sender.getName().getString()).withStyle(ChatFormatting.GOLD))
                                            .append(Lang.t("commandify.tpahere.request_message"))
                                            .append(Component.literal(" "))
                                            .append(acceptButton)
                                            .append(Component.literal(" "))
                                            .append(denyButton);

                                    target.sendSystemMessage(message);
                                    return 1;
                                })
                        )
        );
    }
}
