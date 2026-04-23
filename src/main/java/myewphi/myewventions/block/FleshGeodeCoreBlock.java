package myewphi.myewventions.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class FleshGeodeCoreBlock extends FleshGeodeBlock{
    public FleshGeodeCoreBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        level.setBlock(getRandomSide(pos), ModBlocks.FLESH_GEODE_STEM.get().defaultBlockState(), 3);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int radius = 20;
        Optional<BlockPos> closestStem = BlockPos.findClosestMatch(pos, radius, radius, pos2 -> level.getBlockState(pos2).getBlock().equals(ModBlocks.FLESH_GEODE_STEM.get()));
        if(closestStem.isEmpty()){
            level.setBlock(pos, ModBlocks.FLESH_GEODE_INNER_MEAT.get().defaultBlockState(), 3);
        }
    }
}
