package myewphi.myewventions.capability;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class LiquidHandler implements IFluidHandler, INBTSerializable<CompoundTag> {
    protected FluidStack fluid = FluidStack.EMPTY;
    protected int pressure;

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return fluid;
    }

    @Override
    public int getTankCapacity(int tank) {
        return 100000;
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return true;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (resource.isEmpty() || !isFluidValid(0, resource)) {
            return 0;
        }
        if (action.simulate()) {
            if (fluid.isEmpty()) {
                return Math.min(getTankCapacity(0), resource.getAmount());
            }
            if (!FluidStack.isSameFluidSameComponents(fluid, resource)) {
                return 0;
            }
            return Math.min(getTankCapacity(0) - fluid.getAmount(), resource.getAmount());
        }
        if (fluid.isEmpty()) {
            fluid = resource.copyWithAmount(Math.min(getTankCapacity(0), resource.getAmount()));
            onContentsChanged();
            return fluid.getAmount();
        }
        if (!FluidStack.isSameFluidSameComponents(fluid, resource)) {
            return 0;
        }
        int filled = getTankCapacity(0) - fluid.getAmount();

        if (resource.getAmount() < filled) {
            fluid.grow(resource.getAmount());
            filled = resource.getAmount();
        } else {
            fluid.setAmount(getTankCapacity(0));
        }
        if (filled > 0)
            onContentsChanged();
        return filled;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (resource.isEmpty() || !FluidStack.isSameFluidSameComponents(resource, fluid)) {
            return FluidStack.EMPTY;
        }
        return drain(resource.getAmount(), action);
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        int drained = maxDrain;
        if (fluid.getAmount() < drained) {
            drained = fluid.getAmount();
        }
        FluidStack stack = fluid.copyWithAmount(drained);
        if (action.execute() && drained > 0) {
            fluid.shrink(drained);
            onContentsChanged();
        }
        return stack;
    }

    //Pressure
    public int getPressure(){
        return pressure;
    }
    public int getTotalPressure(){
        return fluid.getAmount() + getPressure();
    }
    public void setPressure(int pressure_){
        pressure = pressure_;
    }

    //Saving and Loading
    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag nbt = new CompoundTag();
        if(!fluid.isEmpty()){
            nbt.put("fluid", fluid.save(provider));
        }
        nbt.putInt("pressure", pressure);
        return nbt;
    }
    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        FluidStack.parse(provider, nbt.getCompound("fluid")).ifPresent(thisFluid -> fluid = thisFluid);
        pressure = nbt.getInt("pressure");

        onLoad();
    }

    protected void onLoad() {}
    protected void onContentsChanged() {}
}
