package myewphi.myewventions.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class FleshGeodeBlock extends Block {
    public FleshGeodeBlock(Properties properties) {
        super(properties);
    }

    protected BlockPos getRandomSide(BlockPos pos){
        List<BlockPos> sides = List.of(pos.above(), pos.below(), pos.north(), pos.east(), pos.south(), pos.west());
        return sides.get((int)Math.round(Math.floor(Math.random() * 6)));
    }
    protected Optional<BlockPos> getRandomSide(BlockPos pos, Predicate<BlockPos> posFilter){
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
    protected double getDistanceToCore(BlockPos pos, BlockPos core){
        return pos.distToCenterSqr(core.getCenter());
    }
}
