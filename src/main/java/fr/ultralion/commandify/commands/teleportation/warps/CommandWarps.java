package fr.ultralion.commandify.commands.teleportation.warps;

import com.mojang.brigadier.CommandDispatcher;
import fr.ultralion.commandify.Config;
import fr.ultralion.commandify.util.Lang;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;

public class CommandWarps {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("warps")
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();

                            if (!Config.ENABLE_WARPS_COMMAND.get()) {
                                player.sendSystemMessage(Lang.t("commandify.warps.disabled"));
                                return 0;
                            }

                            Map<String, WarpData.Warp> warps = WarpData.loadWarps(player.getServer());

                            if (warps.isEmpty()) {
                                player.sendSystemMessage(Lang.t("commandify.warps.empty"));
                                return 1;
                            }

                            player.sendSystemMessage(Lang.t("commandify.warps.header"));

                            for (var entry : warps.entrySet()) {
                                String name = entry.getKey();

                                MutableComponent warpLine = Component.literal("§e• §f" + name + " ")
                                        .append(
                                                Component.literal("§a[TP]")
                                                        .withStyle(style -> style
                                                                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/warp " + name))
                                                                .withHoverEvent(new HoverEvent(
                                                                        HoverEvent.Action.SHOW_TEXT,
                                                                        Lang.t("commandify.warps.hover", name)
                                                                ))
                                                        )
                                        );

                                player.sendSystemMessage(warpLine);
                            }

                            player.sendSystemMessage(Lang.t("commandify.warps.footer"));
                            return 1;
                        })
        );
    }
}
