package myewphi.myewventions.blockentity.cubezio;

import myewphi.myewventions.blockentity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

public class ItemPipeBlockEntity extends AbstractProcessorBlockEntity {
    int cooldownTime = -1;

    public ItemPipeBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.ITEM_PIPE_BE.get(), pos, blockState);

        BASE_INVENTORY_HANDLER = baseInventoryHandler(1, 0);
        UP_INVENTORY_HANDLER = sidedInventoryHandler(new int[]{0}, Direction.UP);
        DOWN_INVENTORY_HANDLER = sidedInventoryHandler(new int[]{0}, Direction.DOWN);
    }

    public void tick(Level level, BlockPos pos, BlockState blockState, ItemPipeBlockEntity blockEntity) {
        if(level.isClientSide()){
            return;
        }
        blockEntity.cooldownTime--;
        if (!blockEntity.isOnCooldown()) {
            blockEntity.setCooldown(0);
            moveItems(level, pos, blockState, blockEntity);
        }
    }
    public void setCooldown(int cooldownTime) {
        this.cooldownTime = cooldownTime;
    }

    private boolean isOnCooldown() {
        return this.cooldownTime > 0;
    }

    private boolean tryPushItems(Level level, BlockPos pos){
        if(BASE_INVENTORY_HANDLER.getStackInSlot(0).isEmpty()){
            return false;
        }

        return false;
    }
    private boolean tryPullItems(Level level, BlockPos pos){
        if(!BASE_INVENTORY_HANDLER.getStackInSlot(0).isEmpty()){
            //cant pull in new item if pipe is full so don't try
            return false;
        }

        IItemHandler bottomItemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos.below(), Direction.UP);

        int extractSlot = getExtractSlot(bottomItemHandler);
        if(extractSlot >= 0){
            BASE_INVENTORY_HANDLER.insertItem(0, bottomItemHandler.extractItem(extractSlot, 1, false), false);
            setCooldown(8);
            return true;
        }

        return false;
    }

    private void moveItems(Level level, BlockPos pos, BlockState blockState, ItemPipeBlockEntity blockEntity){
        IItemHandler topItemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos.above(), Direction.DOWN);
        IItemHandler bottomItemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos.below(), Direction.UP);

        int extractSlot = getExtractSlot(bottomItemHandler);
        if(extractSlot >= 0){
            ItemStack fakeStack = bottomItemHandler.extractItem(extractSlot, 1, true);
            int insertSlot = getInsertSlot(topItemHandler, fakeStack);
            if(insertSlot >= 0){
                topItemHandler.insertItem(insertSlot, bottomItemHandler.extractItem(extractSlot, 1, false), false);
                setCooldown(8);
            }
        }
    }

    int getInsertSlot(IItemHandler itemHandler, ItemStack stack){
        if(itemHandler != null){
            for(int i = 0; i < itemHandler.getSlots(); i++){
                ItemStack remainder = itemHandler.insertItem(i, stack, true);
                if(remainder.isEmpty()){
                    return i;
                }
            }
        }
        return -1;
    }
    int getExtractSlot(IItemHandler itemHandler){
        if(itemHandler != null){
            for(int i = 0; i < itemHandler.getSlots(); i++){
                ItemStack stack = itemHandler.extractItem(i, 1, true);
                if(!stack.isEmpty()){
                    return i;
                }
            }
        }
        return -1;
    }

}
