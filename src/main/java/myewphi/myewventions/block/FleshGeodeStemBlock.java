package myewphi.myewventions.block;

import myewphi.myewventions.Myewventions;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class FleshGeodeStemBlock extends FleshGeodeBlock {
    public FleshGeodeStemBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int radius = 20;
        Optional<BlockPos> core = BlockPos.findClosestMatch(pos, radius, radius, pos2 -> level.getBlockState(pos2).getBlock().equals(ModBlocks.FLESH_GEODE_CORE.get()));
        if(core.isPresent()){
            if(pos.distToCenterSqr(core.get().getCenter()) < radius * 2.1){ //Myewventions.distance(pos, core.get()) < radius
                Optional<BlockPos> randomSide = getRandomSide(pos, blockPos -> !(level.getBlockState(blockPos).getBlock() instanceof FleshGeodeBlock));
                if(randomSide.isPresent()){
                    level.setBlock(randomSide.get(), ModBlocks.FLESH_GEODE_STEM.get().defaultBlockState(), 3);
                    return;
                } else {
                    level.setBlock(pos, ModBlocks.FLESH_GEODE_MEAT.get().defaultBlockState(), 3);
                    return;
                }
            }
        }
        level.setBlock(pos, ModBlocks.FLESH_GEODE_CRUST.get().defaultBlockState(), 3);
    }

    private Optional<BlockPos> getRandomSide(BlockPos pos, Predicate<BlockPos> posFilter){
        List<BlockPos> unfilteredSides = List.of(pos.above(), pos.below(), pos.north(), pos.east(), pos.south(), pos.west());
        ArrayList<BlockPos> filteredSides = new ArrayList<>();
        unfilteredSides.forEach(side -> {
            if(posFilter.test(side)){
                filteredSides.add(side);
            }
        });
        if(!filteredSides.isEmpty()){
            return Optional.of(filteredSides.get((int)Math.round(Math.floor(Math.random() * filteredSides.size()))));
        }
        else {
            return Optional.empty();
        }
    }
}

