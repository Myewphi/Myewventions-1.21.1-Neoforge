package myewphi.myewventions.capability;

import myewphi.myewventions.Myewventions;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class HeatHandler implements IHeatHandler, INBTSerializable<CompoundTag> {
    protected NonNullList<Integer> heats;

    public HeatHandler(int size){
        heats = NonNullList.withSize(size, 0);
    }
    public void setSize(int heatSize) {
        heats = NonNullList.withSize(heatSize, 0);
    }

    @Override
    public int getHeatSlots() {
        return heats.size();
    }
    @Override
    public int getHeatInSlot(int slot) {
        validateSlotIndex(slot);
        return this.heats.get(slot);
    }
    @Override
    public int insertHeat(int slot, int heat, boolean simulate) {
        int overflow = 0;

        validateSlotIndex(slot);

        heats.set(slot, heat + getHeatInSlot(slot));

        if(getHeatInSlot(slot) > getHeatLimit(slot)){
            overflow =  getHeatInSlot(slot) - getHeatLimit(slot);
            heats.set(slot, getHeatLimit(slot));
        }

        onContentsChanged(slot);
        return overflow;
    }
    @Override
    public int extractHeat(int slot, int amount, boolean simulate) {
        int extractedAmount = amount;

        validateSlotIndex(slot);

        if(heats.get(slot) >= amount){
            heats.set(slot, heats.get(slot) - amount);
        }
        else {
            extractedAmount = heats.get(slot);
            heats.set(slot, 0);
        }

        onContentsChanged(slot);
        return extractedAmount;
    }
    @Override
    public int getHeatLimit(int slot) {
        return 0;
    }
    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= heats.size())
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + heats.size() + ")");
    }

    //Saving and Loading
    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        ListTag heatTagList = new ListTag();

        for (int i = 0; i < heats.size(); i++) {
            CompoundTag heatTag = new CompoundTag();
            heatTag.putInt("Slot", i);
            heatTag.putInt("Heat", heats.get(i));
            heatTagList.add(heatTag);
        }

        CompoundTag nbt = new CompoundTag();
        nbt.put("Heats", heatTagList);
        nbt.putInt("HeatSize", heats.size());
        return nbt;
    }
    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        setSize(nbt.contains("HeatSize", Tag.TAG_INT) ? nbt.getInt("HeatSize") : heats.size());

        ListTag heatTagList = nbt.getList("Heats", Tag.TAG_COMPOUND);

        for (int i = 0; i < heatTagList.size(); i++) {
            CompoundTag heatTags = heatTagList.getCompound(i);
            int slot = heatTags.getInt("Slot");

            if (slot >= 0 && slot < heats.size()) {
                Myewventions.LOGGER.info("LOADING: " + heatTags.getInt("Heat"));
                heats.set(slot, heatTags.getInt("Heat"));
            }
        }

        onLoad();
    }
    protected void onLoad() {}
    protected void onContentsChanged(int slot) {}
}
