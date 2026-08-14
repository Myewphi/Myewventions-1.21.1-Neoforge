package myewphi.myewventions.block.blockentity;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

public class CubezItemHandler extends ItemStackHandler {
    public ItemStackHandler baseHandler;
    private int INDEX;
    private String IO;
    private Direction DIR;

    public CubezItemHandler(int index,String io, Direction dir, ItemStackHandler itemHandler) {
        super(0);
        INDEX = index;
        IO = io;
        DIR = dir;
        baseHandler = itemHandler;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return baseHandler.getStackInSlot(slot);
    }

    @Override
    public int getSlotLimit(int slot) {
        return baseHandler.getSlotLimit(slot);
    }

    @Override
    public int getSlots() {
        return baseHandler.getSlots();
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if(IO.equals("input") || IO.equals("both")){
            return baseHandler.insertItem(slot, stack, simulate);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if(IO.equals("output") || IO.equals("both")){
            return baseHandler.extractItem(slot, amount, simulate);
        }
        return ItemStack.EMPTY;
    }
}
