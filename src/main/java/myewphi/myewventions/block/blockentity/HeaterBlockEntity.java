package myewphi.myewventions.block.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;


public class HeaterBlockEntity extends AbstractProcessorBlockEntity {
    public HeaterBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.HEATER_BE.get(), pos, blockState);

        UP_IO = "both";
        DOWN_IO = "both";

        /*
        items = NonNullList.withSize(1, ItemStack.EMPTY);
        heats = NonNullList.withSize(1, 0);

        NORTH_IO = "both";
        NORTH_TYPE = "solid";
        DOWN_IO = "output";
        DOWN_TYPE = "solid";
        UP_IO = "output";
        UP_TYPE = "heat";
         */
    }
}
