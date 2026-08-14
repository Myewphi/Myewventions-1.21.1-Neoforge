package myewphi.myewventions.block.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;


public class HeaterBlockEntity extends AbstractProcessorBlockEntity {
    public HeaterBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.HEATER_BE.get(), pos, blockState);

        BASE_ITEM_HANDLER = baseItemHandler(2);

        UP_ITEM_HANDLER = sidedItemHandler(new int[]{0}, Direction.UP);
        DOWN_ITEM_HANDLER = sidedItemHandler(new int[]{0, 1}, Direction.DOWN);
        NORTH_ITEM_HANDLER = sidedItemHandler(new int[]{1}, Direction.NORTH);
    }
}
