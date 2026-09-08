package myewphi.myewventions.blockentity.cubezio;

import myewphi.myewventions.blockentity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class HeatPipeBlockEntity extends AbstractPipeBlockEntity{
    public HeatPipeBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.HEAT_PIPE_BE.get(), pos, blockState);

        HEAT_SPEED = 16;
    }
}
