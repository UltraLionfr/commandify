package fr.ultralion.commandify.commands.teleportation.teleport;

import com.mojang.brigadier.CommandDispatcher;
import fr.ultralion.commandify.util.Lang;
import fr.ultralion.commandify.commands.teleportation.back.BackManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class CommandTpaccept {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("tpaccept")
                        .executes(ctx -> {
                            ServerPlayer target = ctx.getSource().getPlayerOrException();

                            TeleportManager.TpaRequest request = TeleportManager.getRequest(target.getUUID());
                            if (request == null) {
                                target.sendSystemMessage(Lang.t("commandify.tpaccept.none"));
                                return 0;
                            }

                            UUID requesterId = request.getRequester();
                            ServerPlayer requester = target.getServer().getPlayerList().getPlayer(requesterId);

                            if (requester == null) {
                                target.sendSystemMessage(Lang.t("commandify.tpaccept.offline"));
                                TeleportManager.removeRequest(target.getUUID());
                                return 0;
                            }

                            ServerLevel world = target.getServer().getLevel(Level.OVERWORLD);

                            if (request.isHere()) {
                                BackManager.saveLastLocation(target);

                                target.teleportTo(world,
                                        requester.getX(), requester.getY(), requester.getZ(),
                                        requester.getYRot(), requester.getXRot());

                                target.sendSystemMessage(Lang.t("commandify.tpaccept.here_target", requester.getName().getString()));
                                requester.sendSystemMessage(Lang.t("commandify.tpaccept.here_requester", target.getName().getString()));
                            }
                            else {
                                BackManager.saveLastLocation(requester);

                                requester.teleportTo(world,
                                        target.getX(), target.getY(), target.getZ(),
                                        target.getYRot(), target.getXRot());

                                requester.sendSystemMessage(Lang.t("commandify.tpaccept.tpa_requester", target.getName().getString()));
                                target.sendSystemMessage(Lang.t("commandify.tpaccept.tpa_target", requester.getName().getString()));
                            }

                            TeleportManager.removeRequest(target.getUUID());
                            return 1;
                        })
        );
    }
}
