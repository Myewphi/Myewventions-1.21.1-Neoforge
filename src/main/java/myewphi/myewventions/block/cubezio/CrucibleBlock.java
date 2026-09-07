package myewphi.myewventions.block.cubezio;

import com.mojang.serialization.MapCodec;
import myewphi.myewventions.blockentity.cubezio.CrucibleBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CrucibleBlock extends AbstractCubezBlock {
    public static final MapCodec<CrucibleBlock> CODEC = simpleCodec(CrucibleBlock::new);

    public CrucibleBlock(Properties properties) {
        super(properties);
    }
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CrucibleBlockEntity(pos, state);
    }
}
