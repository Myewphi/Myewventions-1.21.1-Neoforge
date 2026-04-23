package myewphi.myewventions.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Optional;

public class FleshGeodeStemBlock extends FleshGeodeBlock {
    public FleshGeodeStemBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int radius = 15;
        Optional<BlockPos> core = BlockPos.findClosestMatch(pos, radius, radius, pos2 -> level.getBlockState(pos2).getBlock().equals(ModBlocks.FLESH_GEODE_CORE.get()));
        if(core.isPresent()){
            double distanceToCore = getDistanceToCore(pos, core.get());
            Optional<BlockPos> randomSide = getRandomSide(pos, blockPos -> !(level.getBlockState(blockPos).getBlock() instanceof FleshGeodeBlock));
            if(distanceToCore < radius * 3){
                if(randomSide.isPresent()){
                    level.setBlock(randomSide.get(), ModBlocks.FLESH_GEODE_STEM.get().defaultBlockState(), 3);
                    return;
                }

                if(distanceToCore < radius * 1.5){
                    level.setBlock(pos, ModBlocks.FLESH_GEODE_INNER_MEAT.get().defaultBlockState(), 3);
                    return;
                }
                level.setBlock(pos, ModBlocks.FLESH_GEODE_MEAT.get().defaultBlockState(), 3);
                return;
            }
        }
        level.setBlock(pos, ModBlocks.FLESH_GEODE_CRUST.get().defaultBlockState(), 3);
    }
}

