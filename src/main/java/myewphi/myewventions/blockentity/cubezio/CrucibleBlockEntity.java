package myewphi.myewventions.blockentity.cubezio;

import myewphi.myewventions.blockentity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CrucibleBlockEntity extends DeprecatedAbstractCubezBlockEntity {
    public CrucibleBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.CRUCIBLE_BE.get(), pos, blockState);
    }
}
