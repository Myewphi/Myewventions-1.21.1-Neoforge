package myewphi.myewventions.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class FleshGeodeInnerMeatBlock extends FleshGeodeBlock{
    public FleshGeodeInnerMeatBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int radius = 15;
        Optional<BlockPos> core = BlockPos.findClosestMatch(pos, radius, radius, pos2 -> level.getBlockState(pos2).getBlock().equals(ModBlocks.FLESH_GEODE_CORE.get()));
        if(core.isEmpty()){
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        }
    }
}
