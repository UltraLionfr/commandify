package fr.ultralion.commandify.commands.teleportation.teleport;

import com.mojang.brigadier.CommandDispatcher;
import fr.ultralion.commandify.util.Lang;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class CommandTpadeny {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("tpadeny")
                        .executes(ctx -> {
                            ServerPlayer target = ctx.getSource().getPlayerOrException();

                            TeleportManager.TpaRequest request = TeleportManager.getRequest(target.getUUID());

                            if (request == null) {
                                target.sendSystemMessage(Lang.t("commandify.tpadeny.none"));
                                return 0;
                            }

                            UUID requesterId = request.getRequester();
                            ServerPlayer requester = target.getServer().getPlayerList().getPlayer(requesterId);

                            if (requester != null) {
                                requester.sendSystemMessage(Lang.t("commandify.tpadeny.to_requester", target.getName().getString()));
                            }

                            target.sendSystemMessage(Lang.t("commandify.tpadeny.to_target"));

                            TeleportManager.removeRequest(target.getUUID());
                            return 1;
                        })
        );
    }
}
