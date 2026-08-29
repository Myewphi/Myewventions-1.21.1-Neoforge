package myewphi.myewventions.blockentity.cubezio;

import myewphi.myewventions.block.cubezio.ItemPipeBlock;
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
        NORTH_INVENTORY_HANDLER = sidedInventoryHandler(new int[]{0}, Direction.NORTH);
        EAST_INVENTORY_HANDLER = sidedInventoryHandler(new int[]{0}, Direction.EAST);
        SOUTH_INVENTORY_HANDLER = sidedInventoryHandler(new int[]{0}, Direction.SOUTH);
        WEST_INVENTORY_HANDLER = sidedInventoryHandler(new int[]{0}, Direction.WEST);
    }

    public void tick(Level level, BlockPos pos, BlockState blockState, ItemPipeBlockEntity blockEntity) {
        if(level.isClientSide()){
            return;
        }
        blockEntity.cooldownTime--;
        if (!blockEntity.isOnCooldown()) {
            blockEntity.setCooldown(0);
            tryPushItems(level, pos);
            tryPullItems(level, pos);
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
        BlockState state = level.getBlockState(pos);
        if(state.getValue(ItemPipeBlock.HAS_OUTPUT).equals(Boolean.FALSE)){
            return false;
        }
        Direction outputDir = state.getValue(ItemPipeBlock.OUTPUT_FACE);
        IItemHandler outputItemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos.relative(outputDir), outputDir.getOpposite());

        int insertSlot = getInsertSlot(outputItemHandler, BASE_INVENTORY_HANDLER.getStackInSlot(0));
        if(insertSlot >= 0){
            outputItemHandler.insertItem(insertSlot, BASE_INVENTORY_HANDLER.extractItem(0, 1, false), false);
            setCooldown(8);
        }

        return false;
    }
    private boolean tryPullItems(Level level, BlockPos pos){
        if(!BASE_INVENTORY_HANDLER.getStackInSlot(0).isEmpty()){
            return false;
        }
        BlockState state = level.getBlockState(pos);
        if(state.getValue(ItemPipeBlock.HAS_INPUT).equals(Boolean.FALSE)){
            return false;
        }
        Direction inputDir = state.getValue(ItemPipeBlock.INPUT_FACE);
        IItemHandler inputItemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos.relative(inputDir), inputDir.getOpposite());

        int extractSlot = getExtractSlot(inputItemHandler);
        if(extractSlot >= 0){
            BASE_INVENTORY_HANDLER.insertItem(0, inputItemHandler.extractItem(extractSlot, 1, false), false);
            setCooldown(8);
            return true;
        }

        return false;
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
