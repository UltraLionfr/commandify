package fr.ultralion.commandify.commands.chat;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import fr.ultralion.commandify.util.IgnoreManager;
import fr.ultralion.commandify.util.Lang;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;

import java.util.Set;
import java.util.UUID;

public class CommandIgnored {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("ignores")
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            Set<UUID> ignored = IgnoreManager.getIgnoredList(player);

                            if (ignored.isEmpty()) {
                                player.sendSystemMessage(Lang.t("commandify.ignored.empty"));
                                return 1;
                            }

                            player.sendSystemMessage(Lang.t("commandify.ignored.header"));

                            for (UUID uuid : ignored) {
                                String name = player.server.getProfileCache()
                                        .get(uuid)
                                        .map(GameProfile::getName)
                                        .orElse(uuid.toString().substring(0, 8));

                                MutableComponent clickableName = Component.literal("§e" + name)
                                        .setStyle(
                                                Style.EMPTY
                                                        .withUnderlined(true)
                                                        .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/unignore " + name))
                                                        .withHoverEvent(
                                                                new net.minecraft.network.chat.HoverEvent(
                                                                        net.minecraft.network.chat.HoverEvent.Action.SHOW_TEXT,
                                                                        Lang.t("commandify.ignored.hover", name)
                                                                )
                                                        )
                                        );

                                player.sendSystemMessage(Component.literal(" §7- ").append(clickableName));
                            }

                            return 1;
                        })
        );
    }
}
