package fr.ultralion.commandify.commands.admin;

import com.mojang.brigadier.CommandDispatcher;
import fr.ultralion.commandify.util.Lang;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;

public class CommandInvsee {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("invsee")
                        .requires(src -> src.hasPermission(2))
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(ctx -> {
                                    ServerPlayer viewer = ctx.getSource().getPlayerOrException();
                                    ServerPlayer target = EntityArgument.getPlayer(ctx, "target");

                                    if (viewer.getUUID().equals(target.getUUID())) {
                                        viewer.sendSystemMessage(Lang.t("commandify.invsee.self"));
                                        return 0;
                                    }

                                    try {
                                        openEditableInventory(viewer, target);
                                        viewer.sendSystemMessage(Lang.t("commandify.invsee.open", target.getName().getString()));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        viewer.sendSystemMessage(Lang.t("commandify.invsee.error", e.getMessage()));
                                    }

                                    return 1;
                                })
                        )
        );
    }

    private static void openEditableInventory(ServerPlayer viewer, ServerPlayer target) {
        SimpleContainer container = new SimpleContainer(45);

        for (int i = 0; i < 36; i++) {
            container.setItem(i, target.getInventory().items.get(i).copy());
        }

        container.setItem(36, target.getInventory().armor.get(3).copy());
        container.setItem(37, target.getInventory().armor.get(2).copy());
        container.setItem(38, target.getInventory().armor.get(1).copy());
        container.setItem(39, target.getInventory().armor.get(0).copy());
        container.setItem(40, target.getInventory().offhand.get(0).copy());

        viewer.openMenu(new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Lang.t("commandify.invsee.title", target.getName().getString());
            }

            @Override
            public AbstractContainerMenu createMenu(int id, Inventory viewerInventory, Player player) {
                return new ChestMenu(MenuType.GENERIC_9x5, id, viewerInventory, container, 5) {
                    @Override
                    public void removed(Player player) {
                        super.removed(player);
                        applyChangesToTarget(container, target);
                        viewer.sendSystemMessage(Lang.t("commandify.invsee.edited", target.getName().getString()));
                    }
                };
            }
        });
    }

    private static void applyChangesToTarget(SimpleContainer container, ServerPlayer target) {
        for (int i = 0; i < 36; i++) {
            target.getInventory().items.set(i, container.getItem(i).copy());
        }

        target.getInventory().armor.set(3, container.getItem(36).copy());
        target.getInventory().armor.set(2, container.getItem(37).copy());
        target.getInventory().armor.set(1, container.getItem(38).copy());
        target.getInventory().armor.set(0, container.getItem(39).copy());
        target.getInventory().offhand.set(0, container.getItem(40).copy());

        target.containerMenu.broadcastChanges();
    }
}
