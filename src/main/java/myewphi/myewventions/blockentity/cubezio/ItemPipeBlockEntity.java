package myewphi.myewventions.blockentity.cubezio;

import myewphi.myewventions.block.cubezio.ItemPipeBlock;
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

public class ItemPipeBlockEntity extends AbstractCubezBlockEntity {
    private int SLOT_LIMIT = 1;
    public ItemStackHandler INV_ONE = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }

        @Override
        public int getSlotLimit(int slot) {
            return SLOT_LIMIT;
        }
    };
    public ItemStackHandler INV_TWO = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }

        @Override
        public int getSlotLimit(int slot) {
            return SLOT_LIMIT;
        }
    };

    public ItemPipeBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.ITEM_PIPE_BE.get(), pos, blockState);
    }
    public ItemPipeBlockEntity(BlockPos pos, BlockState blockState, int slotLimit) {
        super(ModBlockEntities.ITEM_PIPE_BE.get(), pos, blockState);
        SLOT_LIMIT = slotLimit;
    }

    @Override
    public void dropAllContents(Level level, BlockPos pos) {
        dropContents(level, pos, INV_ONE);
        dropContents(level, pos, INV_TWO);
    }

    @Override
    public IItemHandler getItemHandler(@Nullable Direction side) {
        BlockState state = level.getBlockState(getBlockPos());
        Direction[] directions = ItemPipeBlock.getConnections(state);

        if(directions == null){
            return null;
        }

        if(directions.length > 0){
            if(directions[0].equals(side)){
                return INV_ONE;
            }
        }

        if(directions.length > 1){
            if(directions[1].equals(side)){
                return INV_TWO;
            }
        }

        return null;
    }

    public void tick(Level level, BlockPos pos, BlockState blockState, ItemPipeBlockEntity blockEntity) {
        if(level.isClientSide()){
            return;
        }
        blockEntity.COOLDOWN_TIME--;
        if (!blockEntity.isOnCooldown()) {
            blockEntity.setCooldown(0);

            Direction[] connections = ItemPipeBlock.getConnections(blockState);

            if(connections != null){
                if(connections.length == 2){
                    if(tryPushItems(level, pos, INV_ONE, connections[1])){
                        setCooldown(8);
                    }
                    if(tryPushItems(level, pos, INV_TWO, connections[0])){
                        setCooldown(8);
                    }
                }
            }
        }
    }

    //Saving and loading
    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.put("inv_one", INV_ONE.serializeNBT(pRegistries));
        pTag.put("inv_two", INV_TWO.serializeNBT(pRegistries));
        pTag.putInt("speed", SLOT_LIMIT);

        super.saveAdditional(pTag, pRegistries);
    }
    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        INV_ONE.deserializeNBT(pRegistries, pTag.getCompound("inv_one"));
        INV_TWO.deserializeNBT(pRegistries, pTag.getCompound("inv_two"));
        SLOT_LIMIT = pTag.getInt("speed");
    }
}
