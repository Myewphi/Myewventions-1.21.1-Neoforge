package myewphi.myewventions.capability;

import myewphi.myewventions.Myewventions;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class HeatHandler implements IHeatHandler, INBTSerializable<CompoundTag> {
    protected int heat;

    @Override
    public int getHeat() {
        return heat;
    }
    @Override
    public void setHeat(int value) {
        heat = value;
    }

    //Saving and Loading
    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("Heat", heat);
        return nbt;
    }
    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        heat = nbt.getInt("Heat");

        onLoad();
    }
    protected void onLoad() {}
    protected void onContentsChanged(int slot) {}
}
