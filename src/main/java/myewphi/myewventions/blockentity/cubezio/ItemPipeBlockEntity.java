package myewphi.myewventions.blockentity.cubezio;

import myewphi.myewventions.blockentity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ItemPipeBlockEntity extends AbstractPipeBlockEntity {

    public ItemPipeBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.ITEM_PIPE_BE.get(), pos, blockState);

        ITEM_SPEED = 1;
    }
}
