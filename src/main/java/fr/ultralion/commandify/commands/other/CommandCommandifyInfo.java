package fr.ultralion.commandify.commands.other;

import com.mojang.brigadier.CommandDispatcher;
import fr.ultralion.commandify.Commandify;
import fr.ultralion.commandify.util.Lang;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class CommandCommandifyInfo {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("commandify")
                        .then(Commands.literal("info")
                                .executes(ctx -> {
                                    CommandSourceStack source = ctx.getSource();

                                    source.sendSystemMessage(Lang.t("commandify.info.title", Commandify.MOD_VERSION));
                                    source.sendSystemMessage(Lang.t("commandify.info.author"));
                                    source.sendSystemMessage(Lang.t("commandify.info.website"));
                                    source.sendSystemMessage(Component.literal(""));
                                    source.sendSystemMessage(Lang.t("commandify.info.modules_title"));
                                    source.sendSystemMessage(Lang.t("commandify.info.module.teleport"));
                                    source.sendSystemMessage(Lang.t("commandify.info.module.world"));
                                    source.sendSystemMessage(Lang.t("commandify.info.module.chat"));
                                    source.sendSystemMessage(Component.literal(""));
                                    source.sendSystemMessage(Lang.t("commandify.info.modid", Commandify.MOD_ID));
                                    source.sendSystemMessage(Lang.t("commandify.info.server_version", source.getServer().getServerVersion()));
                                    source.sendSystemMessage(Lang.t("commandify.info.players", String.valueOf(source.getServer().getPlayerCount())));

                                    return 1;
                                })
                        )
        );
    }
}
