package fr.ultralion.commandify.commands.teleportation.warps;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import fr.ultralion.commandify.Config;
import fr.ultralion.commandify.util.Lang;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;

public class CommandDelWarp {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("delwarp")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .requires(src -> src.hasPermission(2))
                                .executes(ctx -> {
                                    ServerPlayer player = ctx.getSource().getPlayerOrException();

                                    if (!Config.ENABLE_DELWARP_COMMAND.get()) {
                                        player.sendSystemMessage(Lang.t("commandify.delwarp.disabled"));
                                        return 0;
                                    }

                                    String name = StringArgumentType.getString(ctx, "name").toLowerCase();
                                    Map<String, WarpData.Warp> warps = WarpData.loadWarps(player.getServer());

                                    if (!warps.containsKey(name)) {
                                        player.sendSystemMessage(Lang.t("commandify.delwarp.not_found", name));
                                        return 0;
                                    }

                                    warps.remove(name);
                                    WarpData.saveWarps(player.getServer(), warps);
                                    player.sendSystemMessage(Lang.t("commandify.delwarp.success", name));
                                    return 1;
                                })
                        )
        );
    }
}
