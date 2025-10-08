package fr.ultralion.commandify.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class OtherPlayerInventory implements Container {

    private final ServerPlayer target;

    public OtherPlayerInventory(ServerPlayer target) {
        this.target = target;
    }

    private Inventory getTargetInventory() {
        return target.getInventory();
    }

    @Override
    public int getContainerSize() {
        return 36;
    }

    @Override
    public boolean isEmpty() {
        return getTargetInventory().items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int index) {
        if (index >= 0 && index < getContainerSize()) {
            return getTargetInventory().items.get(index);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack stack = getItem(index);
        if (!stack.isEmpty()) {
            ItemStack result = stack.split(count);
            if (stack.isEmpty()) setItem(index, ItemStack.EMPTY);
            setChanged();
            return result;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack stack = getItem(index);
        if (!stack.isEmpty()) setItem(index, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        if (index >= 0 && index < getContainerSize()) {
            getTargetInventory().items.set(index, stack);
            setChanged();
        }
    }

    @Override
    public void setChanged() {
        if (target != null && target.containerMenu != null)
            target.containerMenu.broadcastChanges();
    }

    @Override
    public boolean stillValid(Player player) {
        return target != null && target.isAlive() && !target.isRemoved();
    }

    @Override
    public void clearContent() {
        getTargetInventory().items.clear();
        setChanged();
    }
}
