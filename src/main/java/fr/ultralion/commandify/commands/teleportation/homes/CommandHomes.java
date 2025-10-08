package fr.ultralion.commandify.commands.teleportation.homes;

import com.mojang.brigadier.CommandDispatcher;
import fr.ultralion.commandify.Config;
import fr.ultralion.commandify.util.Lang;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;

public class CommandHomes {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("homes")
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();

                            if (!Config.ENABLE_HOME_COMMAND.get()) {
                                player.sendSystemMessage(Lang.t("commandify.homes.disabled"));
                                return 0;
                            }

                            Map<String, HomeData.Home> homes = HomeData.loadHomes(player);

                            if (homes.isEmpty()) {
                                player.sendSystemMessage(Lang.t("commandify.homes.none"));
                                return 0;
                            }

                            player.sendSystemMessage(Lang.t("commandify.homes.header", String.valueOf(homes.size())));

                            for (var entry : homes.entrySet()) {
                                String name = entry.getKey();

                                MutableComponent homeText = Component.literal("§e• " + name + " ");

                                MutableComponent tpButton = Component.literal("§a[TP]")
                                        .setStyle(
                                                Style.EMPTY
                                                        .withClickEvent(new ClickEvent(
                                                                ClickEvent.Action.RUN_COMMAND,
                                                                "/home " + name
                                                        ))
                                                        .withHoverEvent(
                                                                new net.minecraft.network.chat.HoverEvent(
                                                                        net.minecraft.network.chat.HoverEvent.Action.SHOW_TEXT,
                                                                        Lang.t("commandify.homes.hover_tp", name)
                                                                )
                                                        )
                                        );

                                player.sendSystemMessage(homeText.append(tpButton));
                            }

                            player.sendSystemMessage(Lang.t("commandify.homes.footer"));
                            return 1;
                        })
        );
    }
}
