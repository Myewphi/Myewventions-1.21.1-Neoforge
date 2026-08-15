package myewphi.myewventions.block.blockentity;

import myewphi.myewventions.Myewventions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;


public class HeaterBlockEntity extends AbstractProcessorBlockEntity {
    public HeaterBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.HEATER_BE.get(), pos, blockState);

        BASE_ITEM_HANDLER = baseItemHandler(2, 1);

        UP_ITEM_HANDLER = sidedItemHandler(new int[]{0}, Direction.UP);
        DOWN_ITEM_HANDLER = sidedItemHandler(new int[]{0, 1}, Direction.DOWN);
        NORTH_ITEM_HANDLER = sidedItemHandler(new int[]{1}, Direction.NORTH);
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if(level.getBlockEntity(blockPos) instanceof HeaterBlockEntity heater){
            heater.BASE_ITEM_HANDLER.insertHeat(0, 1, false);
            Myewventions.LOGGER.info(String.valueOf(heater.BASE_ITEM_HANDLER.getHeatInSlot(0)));
        }
    }
}
