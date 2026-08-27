package myewphi.myewventions.blockentity.cubezio;

import myewphi.myewventions.Myewventions;
import myewphi.myewventions.blockentity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

public class ItemPipeBlockEntity extends AbstractProcessorBlockEntity {
    public ItemPipeBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.ITEM_PIPE_BE.get(), pos, blockState);
    }

    public void tick(Level level, BlockPos pos, BlockState blockState) {
        if(level.isClientSide()){
            return;
        }

        IItemHandler topItemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos.above(), Direction.DOWN);
        IItemHandler bottomItemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos.below(), Direction.UP);

        int extractSlot = getExtractSlot(bottomItemHandler);

        if(extractSlot >= 0){
            if(canInsertItem(topItemHandler)){
                Myewventions.LOGGER.info(bottomItemHandler.extractItem(extractSlot, 1, true).toString());

                return;
            }
        }

        Myewventions.LOGGER.info("FALSE");
    }

    boolean canInsertItem(IItemHandler itemHandler){
        if(itemHandler != null){
            for(int i = 0; i < itemHandler.getSlots(); i++){
                if(itemHandler.insertItem(i, new ItemStack(Items.DIAMOND), true).isEmpty()){
                    return true;
                }
            }
        }
        return false;
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
