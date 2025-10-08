package fr.ultralion.commandify.commands.chat;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import fr.ultralion.commandify.util.Lang;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class CommandBroadcast {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("broadcast")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.argument("message", StringArgumentType.greedyString())
                                .executes(ctx -> {
                                    String message = StringArgumentType.getString(ctx, "message");

                                    ctx.getSource().getServer().getPlayerList().broadcastSystemMessage(
                                            Lang.t("commandify.broadcast.format", message),
                                            false
                                    );

                                    ctx.getSource().sendSystemMessage(Lang.t("commandify.broadcast.sent"));
                                    return 1;
                                })
                        )
        );
    }
}
