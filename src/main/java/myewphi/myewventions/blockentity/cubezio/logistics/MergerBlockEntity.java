package myewphi.myewventions.blockentity.cubezio.logistics;

import myewphi.myewventions.block.cubezio.processors.OvenBlock;
import myewphi.myewventions.blockentity.ModBlockEntities;
import myewphi.myewventions.blockentity.cubezio.AbstractCubezBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class MergerBlockEntity extends AbstractCubezBlockEntity {
    public ItemStackHandler INV = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    public MergerBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.MERGER_BE.get(), pos, blockState);
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

    public void tick(Level level, BlockPos pos, BlockState blockState, MergerBlockEntity blockEntity) {
        if(level.isClientSide()){
            return;
        }

        boolean powered = isPowered(level, pos);
        if(blockState.getValue(BlockStateProperties.ENABLED) == powered){
            level.setBlock(pos, blockState.setValue(BlockStateProperties.ENABLED, !powered), 3);
        }

        blockEntity.COOLDOWN_TIME--;
        if (!blockEntity.isOnCooldown() && blockState.getValue(BlockStateProperties.ENABLED)) {
            if(tryPushItems(level, pos, INV, blockState.getValue(OvenBlock.FACING).getOpposite())){
                setCooldown(8);
            }
        }
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
