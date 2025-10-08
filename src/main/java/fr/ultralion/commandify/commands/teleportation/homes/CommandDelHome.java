package fr.ultralion.commandify.commands.teleportation.homes;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import fr.ultralion.commandify.util.Lang;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;

public class CommandDelHome {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("delhome")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .executes(ctx -> {
                                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                                    String name = StringArgumentType.getString(ctx, "name").toLowerCase();

                                    Map<String, HomeData.Home> homes = HomeData.loadHomes(player);

                                    if (!homes.containsKey(name)) {
                                        player.sendSystemMessage(Lang.t("commandify.delhome.not_found", name));
                                        return 0;
                                    }

                                    homes.remove(name);
                                    HomeData.saveHomes(player, homes);

                                    player.sendSystemMessage(Lang.t("commandify.delhome.success", name));
                                    return 1;
                                })
                        )
        );
    }
}
