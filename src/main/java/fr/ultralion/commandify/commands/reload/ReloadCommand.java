package fr.ultralion.commandify.commands.reload;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.mojang.brigadier.CommandDispatcher;
import fr.ultralion.commandify.Config;
import fr.ultralion.commandify.util.Lang;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.nio.file.Path;
import java.nio.file.Paths;


public class ReloadCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("commandify")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("reload")
                                .executes(ctx -> {
                                    CommandSourceStack source = ctx.getSource();

                                    try {
                                        Path path = Paths.get("config", "commandify-server.toml");

                                        CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().build();
                                        configData.load();

                                        Config.ENABLE_SPAWN_COMMAND.set(configData.getOrElse("enableSpawnCommand", true));
                                        Config.ENABLE_SETSPAWN_COMMAND.set(configData.getOrElse("enableSetSpawnCommand", true));

                                        configData.close();

                                        source.sendSuccess(() -> Lang.t("commandify.reload.success"), true);
                                        System.out.println("[Commandify] Configuration successfully reloaded!");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        source.sendFailure(Lang.t("commandify.reload.error"));
                                    }

                                    return 1;
                                })
                        )
        );
    }
}
