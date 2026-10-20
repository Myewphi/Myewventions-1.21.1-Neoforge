package myewphi.myewventions.blockentity.cubezio.pipes;

import myewphi.myewventions.blockentity.ModBlockEntities;
import myewphi.myewventions.blockentity.cubezio.AbstractCubezBlockEntity;
import myewphi.myewventions.capability.LiquidHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

public class FluidPipeBlockEntity extends AbstractCubezBlockEntity {
    public LiquidHandler INV = new LiquidHandler() {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }

        @Override
        public int getTankCapacity(int tank) {
            return 1000;
        }
    };

    public FluidPipeBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.FLUID_PIPE_BE.get(), pos, blockState);
    }

    @Override
    public void dropAllContents(Level level, BlockPos pos) { }

    //Input/Output
    @Override
    public IFluidHandler getFluidHandler(@Nullable Direction side) {
        return INV;
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState, AbstractCubezBlockEntity blockEntity) {
        if(level.isClientSide()){
            return;
        }

        updatePressure(level, blockPos, INV);

        blockEntity.COOLDOWN_TIME--;
        if (!blockEntity.isOnCooldown()) {
            blockEntity.setCooldown(0);

            if(tryPushLiquidUsingPressure(level, blockPos, blockState, blockEntity)){
                setCooldown(8);
            }
        }
    }
    public boolean tryPushLiquidUsingPressure(Level level, BlockPos blockPos, BlockState blockState, AbstractCubezBlockEntity blockEntity){
        boolean targetFound = false;
        IFluidHandler currentTarget = null;
        int pressure = INV.getTotalPressure();

        for(Direction dir : Direction.values()){
            IFluidHandler fluidHandler = level.getCapability(Capabilities.FluidHandler.BLOCK, blockPos.relative(dir), dir.getOpposite());
            boolean found = false;

            if(fluidHandler == null){
                continue;
            }

            if(fluidHandler.getTankCapacity(0) <= fluidHandler.getFluidInTank(0).getAmount()){
                continue;
            }

            if(fluidHandler instanceof LiquidHandler){
                if(pressure > ((LiquidHandler) fluidHandler).getTotalPressure()){
                    targetFound = true;
                    currentTarget = fluidHandler;
                    pressure = ((LiquidHandler) fluidHandler).getTotalPressure();
                }
            }
            else if(pressure > fluidHandler.getFluidInTank(0).getAmount()){
                targetFound = true;
                currentTarget = fluidHandler;
                pressure = fluidHandler.getFluidInTank(0).getAmount();
            }
        }

        if(!targetFound){
            return false;
        }

        tryPushLiquid(level, blockPos, INV, 100, currentTarget);
        return true;
    }

    //Saving and loading
    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.put("inventory", INV.serializeNBT(pRegistries));

        super.saveAdditional(pTag, pRegistries);
    }
    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        INV.deserializeNBT(pRegistries, pTag.getCompound("inventory"));
    }
}
