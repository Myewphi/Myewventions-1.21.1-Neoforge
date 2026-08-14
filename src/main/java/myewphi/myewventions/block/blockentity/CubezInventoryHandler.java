package myewphi.myewventions.block.blockentity;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

public class CubezInventoryHandler implements IItemHandler, IHeatHandler, IItemHandlerModifiable, INBTSerializable<CompoundTag> {
    protected NonNullList<ItemStack> stacks;
    protected NonNullList<Integer> heats;

    public CubezInventoryHandler() {
        this(1, 0);
    }
    public CubezInventoryHandler(int solidSize, int heatSize) {
        stacks = NonNullList.withSize(solidSize, ItemStack.EMPTY);
        heats = NonNullList.withSize(heatSize, 0);
    }
    public void setSize(int solidSize, int heatSize) {
        stacks = NonNullList.withSize(solidSize, ItemStack.EMPTY);
        heats = NonNullList.withSize(heatSize, 0);
    }

    //Solid
    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        validateSlotIndex(slot);
        this.stacks.set(slot, stack);
        onContentsChanged(slot);
    }
    @Override
    public int getSlots() {
        return stacks.size();
    }
    @Override
    public ItemStack getStackInSlot(int slot) {
        validateSlotIndex(slot);
        return this.stacks.get(slot);
    }
    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (stack.isEmpty())
            return ItemStack.EMPTY;

        if (!isItemValid(slot, stack))
            return stack;

        validateSlotIndex(slot);

        ItemStack existing = this.stacks.get(slot);

        int limit = getStackLimit(slot, stack);

        if (!existing.isEmpty()) {
            if (!ItemStack.isSameItemSameComponents(stack, existing))
                return stack;

            limit -= existing.getCount();
        }

        if (limit <= 0)
            return stack;

        boolean reachedLimit = stack.getCount() > limit;

        if (!simulate) {
            if (existing.isEmpty()) {
                this.stacks.set(slot, reachedLimit ? stack.copyWithCount(limit) : stack);
            } else {
                existing.grow(reachedLimit ? limit : stack.getCount());
            }
            onContentsChanged(slot);
        }

        return reachedLimit ? stack.copyWithCount(stack.getCount() - limit) : ItemStack.EMPTY;
    }
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0)
            return ItemStack.EMPTY;

        validateSlotIndex(slot);

        ItemStack existing = this.stacks.get(slot);

        if (existing.isEmpty())
            return ItemStack.EMPTY;

        int toExtract = Math.min(amount, existing.getMaxStackSize());

        if (existing.getCount() <= toExtract) {
            if (!simulate) {
                this.stacks.set(slot, ItemStack.EMPTY);
                onContentsChanged(slot);
                return existing;
            } else {
                return existing.copy();
            }
        } else {
            if (!simulate) {
                this.stacks.set(slot, existing.copyWithCount(existing.getCount() - toExtract));
                onContentsChanged(slot);
            }

            return existing.copyWithCount(toExtract);
        }
    }
    @Override
    public int getSlotLimit(int slot) {
        return Item.ABSOLUTE_MAX_STACK_SIZE;
    }
    protected int getStackLimit(int slot, ItemStack stack) {
        return Math.min(getSlotLimit(slot), stack.getMaxStackSize());
    }
    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return true;
    }

    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= stacks.size())
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + stacks.size() + ")");
    }
    protected void onLoad() {}
    protected void onContentsChanged(int slot) {}

    //Heat
    @Override
    public int getHeatSlots() {
        return 0;
    }
    @Override
    public int getHeatInSlot(int slot) {
        return 0;
    }
    @Override
    public int insertHeat(int slot, int heat, boolean simulate) {
        return 0;
    }
    @Override
    public int extractHeat(int slot, int amount, boolean simulate) {
        return 0;
    }
    @Override
    public int getHeatLimit(int slot) {
        return 0;
    }

    //Saving and Loading
    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        ListTag itemTagList = new ListTag();
        ListTag heatTagList = new ListTag();

        for (int i = 0; i < stacks.size(); i++) {
            if (!stacks.get(i).isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putInt("Slot", i);
                itemTagList.add(stacks.get(i).save(provider, itemTag));
            }
        }

        for (int i = 0; i < heats.size(); i++) {
            CompoundTag heatTag = new CompoundTag();
            heatTag.putInt("Slot", i);
            heatTag.putInt("Heat", heats.get(i));
            heatTagList.add(heatTag);
        }

        CompoundTag nbt = new CompoundTag();
        nbt.put("Items", itemTagList);
        nbt.putInt("SolidSize", stacks.size());
        nbt.put("Heats", heatTagList);
        nbt.putInt("HeatSize", heats.size());
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        setSize(
                nbt.contains("SolidSize", Tag.TAG_INT) ? nbt.getInt("SolidSize") : stacks.size(),
                nbt.contains("HeatSize", Tag.TAG_INT) ? nbt.getInt("HeatSize") : heats.size());

        ListTag solidTagList = nbt.getList("Items", Tag.TAG_COMPOUND);
        ListTag heatTagList = nbt.getList("Heats", Tag.TAG_COMPOUND);

        for (int i = 0; i < solidTagList.size(); i++) {
            CompoundTag itemTags = solidTagList.getCompound(i);
            int slot = itemTags.getInt("Slot");

            if (slot >= 0 && slot < stacks.size()) {
                ItemStack.parse(provider, itemTags).ifPresent(stack -> stacks.set(slot, stack));
            }
        }

        for (int i = 0; i < heatTagList.size(); i++) {
            CompoundTag heatTags = heatTagList.getCompound(i);
            int slot = heatTags.getInt("Slot");

            if (slot >= 0 && slot < heats.size()) {
                heats.set(slot, heatTags.getInt("Heat"));
            }
        }

        onLoad();
    }
}
