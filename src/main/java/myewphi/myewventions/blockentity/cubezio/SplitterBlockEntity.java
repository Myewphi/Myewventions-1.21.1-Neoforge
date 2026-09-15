package myewphi.myewventions.blockentity.cubezio;

import myewphi.myewventions.block.cubezio.ISideRestrictedIO;
import myewphi.myewventions.blockentity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class SplitterBlockEntity extends AbstractCubezBlockEntity implements ISideRestrictedIO {
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

    @Override
    public boolean isOutputSide(Direction dir) {
        return false;
    }
    @Override
    public boolean isInputSide(Direction dir) {
        return false;
    }
}
