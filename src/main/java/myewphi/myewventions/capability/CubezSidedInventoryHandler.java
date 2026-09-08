package myewphi.myewventions.capability;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

public class CubezSidedInventoryHandler implements IItemHandler, IHeatHandler, IItemHandlerModifiable {
    public CubezInventoryHandler baseHandler;
    private final int[] SLOTS;
    private final String IO;
    private final Direction DIR;

    public CubezSidedInventoryHandler(int[] slots, String io, Direction dir, CubezInventoryHandler itemHandler) {
        SLOTS = slots;
        IO = io;
        DIR = dir;
        baseHandler = itemHandler;
    }

    public int[] getSLOTS(){
        return SLOTS;
    }
    public String getIO(){
        return IO;
    }
    public Direction getDIR(){
        return DIR;
    }

    //Solid
    @Override
    public ItemStack getStackInSlot(int slot) {
        return baseHandler.getStackInSlot(slot);
    }
    @Override
    public int getSlotLimit(int slot) {
        return baseHandler.getSlotLimit(slot);
    }
    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return baseHandler.isItemValid(slot, stack);
    }
    @Override
    public int getSlots() {
        return baseHandler.getSlots();
    }
    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if(IO.equals("input") || IO.equals("both") && validateSlotIndex(slot, SLOTS)){
            return baseHandler.insertItem(slot, stack, simulate);
        }
        return stack;
    }
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if(IO.equals("output") || IO.equals("both") && validateSlotIndex(slot, SLOTS)){
            return baseHandler.extractItem(slot, amount, simulate);
        }
        return ItemStack.EMPTY;
    }
    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        baseHandler.setStackInSlot(slot, stack);
    }

    //Heat
    @Override
    public int getHeatSlots() {
        return baseHandler.getHeatSlots();
    }
    @Override
    public int getHeatInSlot(int slot) {
        return baseHandler.getHeatInSlot(slot);
    }
    @Override
    public int insertHeat(int slot, int heat, boolean simulate) {
        if(IO.equals("input") || IO.equals("both") && validateSlotIndex(slot, SLOTS)){
            return baseHandler.insertHeat(slot, heat, simulate);
        }
        return heat;
    }
    @Override
    public int extractHeat(int slot, int amount, boolean simulate) {
        if(IO.equals("output") || IO.equals("both") && validateSlotIndex(slot, SLOTS)){
            return baseHandler.extractHeat(slot, amount, simulate);
        }
        return 0;
    }
    @Override
    public int getHeatLimit(int slot) {
        return baseHandler.getHeatLimit(slot);
    }

    //Misc
    boolean validateSlotIndex(int findInt, int[] intArray){
        for (int i : intArray) {
            if (i == findInt) {
                return true;
            }
        }
        return false;
    }
}
