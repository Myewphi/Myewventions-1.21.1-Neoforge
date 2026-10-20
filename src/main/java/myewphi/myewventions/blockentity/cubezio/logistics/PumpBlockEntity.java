package myewphi.myewventions.blockentity.cubezio.logistics;

import myewphi.myewventions.blockentity.ModBlockEntities;
import myewphi.myewventions.blockentity.cubezio.AbstractCubezBlockEntity;
import myewphi.myewventions.capability.LiquidHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

public class PumpBlockEntity extends AbstractCubezBlockEntity {
    public LiquidHandler INPUT = new LiquidHandler() {
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
    public LiquidHandler OUTPUT = new LiquidHandler() {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }

        @Override
        public int getPressure() {
            return 10000;
        }
    };
    int PUMP_COOLDOWN;

    public PumpBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.PUMP_BE.get(), pos, blockState);
    }

    @Override
    public void dropAllContents(Level level, BlockPos pos) {

    }

    //Input/Output
    @Override
    public IFluidHandler getFluidHandler(@Nullable Direction side) {
        if(side == null){
            return null;
        }

        assert level != null;
        BlockState state = level.getBlockState(worldPosition);
        if(side.equals(state.getValue(DirectionalBlock.FACING))){
            return INPUT;
        }
        if(side.equals(state.getValue(DirectionalBlock.FACING).getOpposite())){
            return OUTPUT;
        }

        return null;
    }
    public void tick(Level level, BlockPos blockPos, BlockState blockState, AbstractCubezBlockEntity blockEntity) {
        if(level.isClientSide()){
            return;
        }

        boolean powered = isPowered(level, blockPos);
        if(blockState.getValue(BlockStateProperties.ENABLED) == powered){
            level.setBlock(blockPos, blockState.setValue(BlockStateProperties.ENABLED, !powered), 3);
        }

        if(!blockState.getValue(BlockStateProperties.ENABLED)){
            return;
        }

        PUMP_COOLDOWN--;
        if(PUMP_COOLDOWN <= 0){
            FluidState fluidState = level.getFluidState(blockPos.relative(blockState.getValue(DirectionalBlock.FACING)));
            if(!fluidState.isEmpty()){
                level.setBlock(blockPos.relative(blockState.getValue(DirectionalBlock.FACING)), Blocks.AIR.defaultBlockState(), 3);
                INPUT.fill(new FluidStack(fluidState.getType(), 1000), IFluidHandler.FluidAction.EXECUTE);
                PUMP_COOLDOWN = 60;
            }
        }

        blockEntity.COOLDOWN_TIME--;
        if (!blockEntity.isOnCooldown()) {
            blockEntity.setCooldown(0);

            tryPushLiquid(level, blockPos, INPUT, OUTPUT.getTankCapacity(0), OUTPUT);

            if(tryPushLiquid(level, blockPos, OUTPUT, 100, blockState.getValue(DirectionalBlock.FACING).getOpposite())){
                setCooldown(8);
            }
        }
    }

    //Saving and loading
    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.put("input", INPUT.serializeNBT(pRegistries));
        pTag.put("output", OUTPUT.serializeNBT(pRegistries));
        pTag.putInt("pumpCooldown", PUMP_COOLDOWN);

        super.saveAdditional(pTag, pRegistries);
    }
    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        INPUT.deserializeNBT(pRegistries, pTag.getCompound("input"));
        OUTPUT.deserializeNBT(pRegistries, pTag.getCompound("output"));
        PUMP_COOLDOWN = pTag.getInt("pumpCooldown");
    }
}
