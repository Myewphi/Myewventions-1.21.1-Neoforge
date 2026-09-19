package myewphi.myewventions.capability;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class LiquidHandler implements IFluidHandler {
    protected NonNullList<FluidStack> stacks;
    protected int pressure;

    @Override
    public int getTanks() {
        return stacks.size();
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return stacks.get(tank);
    }

    @Override
    public int getTankCapacity(int tank) {
        return 1000;
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return stacks.size() > tank;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return 0;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return null;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return null;
    }

    protected void onContentsChanged() {}
}
