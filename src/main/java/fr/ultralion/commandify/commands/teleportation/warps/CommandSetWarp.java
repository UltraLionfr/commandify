package fr.ultralion.commandify.commands.teleportation.warps;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import fr.ultralion.commandify.Config;
import fr.ultralion.commandify.util.Lang;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;

public class CommandSetWarp {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("setwarp")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .requires(src -> src.hasPermission(2))
                                .executes(ctx -> {
                                    ServerPlayer player = ctx.getSource().getPlayerOrException();

                                    if (!Config.ENABLE_SETWARP_COMMAND.get()) {
                                        player.sendSystemMessage(Lang.t("commandify.setwarp.disabled"));
                                        return 0;
                                    }

                                    String name = StringArgumentType.getString(ctx, "name").toLowerCase();
                                    Map<String, WarpData.Warp> warps = WarpData.loadWarps(player.getServer());

                                    WarpData.Warp warp = new WarpData.Warp(
                                            name,
                                            player.level().dimension().location().toString(),
                                            player.getX(), player.getY(), player.getZ(),
                                            player.getYRot(), player.getXRot()
                                    );

                                    warps.put(name, warp);
                                    WarpData.saveWarps(player.getServer(), warps);

                                    player.sendSystemMessage(Lang.t("commandify.setwarp.success", name));
                                    return 1;
                                })
                        )
        );
    }
}
