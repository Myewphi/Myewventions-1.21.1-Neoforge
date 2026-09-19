package myewphi.myewventions.blockentity.cubezio.logistics;

import myewphi.myewventions.block.cubezio.processors.OvenBlock;
import myewphi.myewventions.blockentity.ModBlockEntities;
import myewphi.myewventions.blockentity.cubezio.AbstractCubezBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class FilterBlockEntity extends AbstractCubezBlockEntity {
    public ItemStackHandler INV = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if(FILTER.getStackInSlot(0).isEmpty() || FILTER.getStackInSlot(0).getItem() == stack.getItem()){
                return super.insertItem(slot, stack, simulate);
            }
            return stack;
        }
    };
    public ItemStackHandler FILTER = new ItemStackHandler(1) {
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


    public FilterBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.FILTER_BE.get(), pos, blockState);
    }
    @Override
    public void dropAllContents(Level level, BlockPos pos) {
        dropContents(level, pos, INV);
        dropContents(level, pos, FILTER);
    }

    //Input/Output
    @Override
    public IItemHandler getItemHandler(@Nullable Direction side) {
        if(side == null){
            return null;
        }

        assert level != null;
        BlockState state = level.getBlockState(worldPosition);
        if (side.equals(state.getValue(OvenBlock.FACING)) || side.equals(state.getValue(OvenBlock.FACING).getOpposite())) {
            return INV;
        }
        if(side.equals(Direction.UP)){
            return FILTER;
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

        blockEntity.COOLDOWN_TIME--;
        if (!blockEntity.isOnCooldown() && blockState.getValue(BlockStateProperties.ENABLED)) {
            blockEntity.setCooldown(0);

            if(tryPushItems(level, blockPos, INV, blockState.getValue(HorizontalDirectionalBlock.FACING).getOpposite())){
                setCooldown(8);
            }
        }
    }

    //Saving and loading
    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.put("inventory", INV.serializeNBT(pRegistries));
        pTag.put("filter", FILTER.serializeNBT(pRegistries));

        super.saveAdditional(pTag, pRegistries);
    }
    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        INV.deserializeNBT(pRegistries, pTag.getCompound("inventory"));
        FILTER.deserializeNBT(pRegistries, pTag.getCompound("filter"));
    }
}
