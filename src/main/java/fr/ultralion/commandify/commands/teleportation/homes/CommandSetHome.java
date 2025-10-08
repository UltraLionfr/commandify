package fr.ultralion.commandify.commands.teleportation.homes;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import fr.ultralion.commandify.Config;
import fr.ultralion.commandify.util.Lang;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;

public class CommandSetHome {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("sethome")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .executes(ctx -> {
                                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                                    String name = StringArgumentType.getString(ctx, "name").toLowerCase();

                                    Map<String, HomeData.Home> homes = HomeData.loadHomes(player);

                                    if (homes.size() >= Config.MAX_HOMES.get() && !homes.containsKey(name)) {
                                        player.sendSystemMessage(Lang.t("commandify.sethome.limit_reached", String.valueOf(Config.MAX_HOMES.get())));
                                        return 0;
                                    }

                                    HomeData.Home home = new HomeData.Home(
                                            name,
                                            player.level().dimension().location().toString(),
                                            player.getX(), player.getY(), player.getZ(),
                                            player.getYRot(), player.getXRot()
                                    );

                                    homes.put(name, home);
                                    HomeData.saveHomes(player, homes);

                                    player.sendSystemMessage(Lang.t("commandify.sethome.success", name));
                                    return 1;
                                })
                        )
        );
    }
}
