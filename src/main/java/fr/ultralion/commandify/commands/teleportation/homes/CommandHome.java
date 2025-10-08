package fr.ultralion.commandify.commands.teleportation.homes;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import fr.ultralion.commandify.commands.teleportation.back.BackManager;
import fr.ultralion.commandify.util.Lang;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.Level;

import java.util.Map;


public class CommandHome {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("home")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .executes(ctx -> {
                                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                                    String name = StringArgumentType.getString(ctx, "name").toLowerCase();

                                    Map<String, HomeData.Home> homes = HomeData.loadHomes(player);
                                    HomeData.Home home = homes.get(name);

                                    if (home == null) {
                                        player.sendSystemMessage(Lang.t("commandify.home.not_found", name));
                                        return 0;
                                    }

                                    String[] parts = home.dimension.split(":");
                                    String namespace = parts.length > 1 ? parts[0] : "minecraft";
                                    String path = parts.length > 1 ? parts[1] : home.dimension;

                                    ResourceLocation location = ResourceLocation.fromNamespaceAndPath(namespace, path);
                                    ResourceKey<Level> dim = ResourceKey.create(Registries.DIMENSION, location);

                                    ServerLevel world = player.getServer().getLevel(dim);
                                    if (world == null) {
                                        player.sendSystemMessage(Lang.t("commandify.home.invalid_world"));
                                        return 0;
                                    }

                                    BackManager.saveLastLocation(player);

                                    player.teleportTo(world, home.x, home.y, home.z, home.yaw, home.pitch);
                                    player.sendSystemMessage(Lang.t("commandify.home.success", name));

                                    return 1;
                                })
                        )
        );
    }
}
