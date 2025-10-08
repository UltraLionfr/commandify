package fr.ultralion.commandify.commands.teleportation.warps;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import fr.ultralion.commandify.Config;
import fr.ultralion.commandify.commands.teleportation.back.BackManager;
import fr.ultralion.commandify.util.Lang;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.Map;

public class CommandWarp {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("warp")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .executes(ctx -> {
                                    ServerPlayer player = ctx.getSource().getPlayerOrException();

                                    if (!Config.ENABLE_WARP_COMMAND.get()) {
                                        player.sendSystemMessage(Lang.t("commandify.warp.disabled"));
                                        return 0;
                                    }

                                    String name = StringArgumentType.getString(ctx, "name").toLowerCase();
                                    Map<String, WarpData.Warp> warps = WarpData.loadWarps(player.getServer());
                                    WarpData.Warp warp = warps.get(name);

                                    if (warp == null) {
                                        player.sendSystemMessage(Lang.t("commandify.warp.not_found", name));
                                        return 0;
                                    }

                                    String[] parts = warp.dimension().split(":");
                                    String namespace = parts.length > 1 ? parts[0] : "minecraft";
                                    String path = parts.length > 1 ? parts[1] : warp.dimension();
                                    ResourceKey<Level> dim = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(namespace, path));

                                    ServerLevel world = player.getServer().getLevel(dim);
                                    if (world == null) {
                                        player.sendSystemMessage(Lang.t("commandify.warp.invalid_world"));
                                        return 0;
                                    }

                                    BackManager.saveLastLocation(player);

                                    player.teleportTo(world, warp.x(), warp.y(), warp.z(), warp.yaw(), warp.pitch());
                                    player.sendSystemMessage(Lang.t("commandify.warp.success", name));
                                    return 1;
                                })
                        )
        );
    }
}
