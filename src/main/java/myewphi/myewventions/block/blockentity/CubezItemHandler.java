package myewphi.myewventions.block.blockentity;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;

public class CubezItemHandler implements IItemHandler, IItemHandlerModifiable {
    public ItemStackHandler baseHandler;
    private int INDEX;
    private String IO;
    private Direction DIR;

    public CubezItemHandler(int index,String io, Direction dir, ItemStackHandler itemHandler) {
        INDEX = index;
        IO = io;
        DIR = dir;
        baseHandler = itemHandler;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return baseHandler.getStackInSlot(INDEX);
    }

    @Override
    public int getSlotLimit(int slot) {
        return baseHandler.getSlotLimit(INDEX);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return baseHandler.isItemValid(INDEX, stack);
    }

    @Override
    public int getSlots() {
        return baseHandler.getSlots();
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if(IO.equals("input") || IO.equals("both")){
            return baseHandler.insertItem(INDEX, stack, simulate);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if(IO.equals("output") || IO.equals("both")){
            return baseHandler.extractItem(INDEX, amount, simulate);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        baseHandler.setStackInSlot(INDEX, stack);
    }
}
