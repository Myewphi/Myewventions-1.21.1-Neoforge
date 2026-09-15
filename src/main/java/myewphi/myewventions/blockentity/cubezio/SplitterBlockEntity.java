package myewphi.myewventions.blockentity.cubezio;

import myewphi.myewventions.block.cubezio.SplitterBlock;
import myewphi.myewventions.blockentity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class SplitterBlockEntity extends AbstractCubezBlockEntity {
    public ItemStackHandler INV = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    };
    int roundRobin = 0;

    public SplitterBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.SPLITTER_BE.get(), pos, blockState);
    }

    @Override
    public void dropAllContents(Level level, BlockPos pos) {
        dropContents(level, pos, INV);
    }

    //Input/Output
    @Override
    public IItemHandler getItemHandler(@Nullable Direction side) {
        if(side == null){
            return null;
        }

        if(!side.equals(Direction.UP) && !side.equals(Direction.DOWN)){
            return INV;
        }

        return null;
    }

    public void tick(Level level, BlockPos pos, BlockState blockState, SplitterBlockEntity blockEntity) {
        if(level.isClientSide()){
            return;
        }
        blockEntity.COOLDOWN_TIME--;
        if (!blockEntity.isOnCooldown()) {
            int count = getOutputCount(level, pos, blockState);
            if(roundRobin >= count){
                roundRobin = 0;
            }

            if(tryPushItems(level, pos, INV, getOutputDirection(level, pos, blockState, count - roundRobin))){
                setCooldown(8);
                roundRobin += 1;
            }
        }
    }
    int getOutputCount(Level level, BlockPos pos, BlockState state){
        int outputCount = 0;
        Direction facing = state.getValue(SplitterBlock.FACING);

        if(level.getCapability(Capabilities.ItemHandler.BLOCK, pos.relative(facing.getClockWise()), facing.getClockWise().getOpposite()) != null){
            outputCount += 1;
        }
        if(level.getCapability(Capabilities.ItemHandler.BLOCK, pos.relative(facing.getOpposite()), facing.getOpposite()) != null){
            outputCount += 1;
        }
        if(level.getCapability(Capabilities.ItemHandler.BLOCK, pos.relative(facing.getCounterClockWise()), facing.getCounterClockWise().getOpposite()) != null){
            outputCount += 1;
        }

        return outputCount;
    }
    Direction getOutputDirection(Level level, BlockPos pos, BlockState state, int count){
        Direction outputDir = state.getValue(SplitterBlock.FACING);

        for(int i = 0; i < 3; i++){
            outputDir = outputDir.getClockWise();
            if(level.getCapability(Capabilities.ItemHandler.BLOCK, pos.relative(outputDir), outputDir.getOpposite()) != null){
                count--;
            }
            if(count == 0){
                return outputDir;
            }
        }
        return null;
    }

    //Saving and loading
    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.put("inventory", INV.serializeNBT(pRegistries));
        pTag.putInt("roundRobin", roundRobin);

        super.saveAdditional(pTag, pRegistries);
    }
    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        INV.deserializeNBT(pRegistries, pTag.getCompound("inventory"));
        roundRobin = pTag.getInt("roundRobin");
    }
}
