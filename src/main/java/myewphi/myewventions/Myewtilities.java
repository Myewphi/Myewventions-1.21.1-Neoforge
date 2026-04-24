package myewphi.myewventions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Myewtilities {
    public static List<BlockPos> getSides(BlockPos pos) {
        return List.of(pos.above(), pos.below(), pos.north(), pos.east(), pos.south(), pos.west());
    }
    public static int getRandomListElement(int size){
        return (int)Math.round(Math.floor(Math.random() * size));
    }
    public static BlockPos getRandomSide(BlockPos pos){
        return getSides(pos).get(getRandomListElement(6));
    }
    public static Optional<BlockPos> getRandomSide(BlockPos pos, Predicate<BlockPos> posFilter){
        ArrayList<BlockPos> sides = pruneBlockPosList(getSides(pos), posFilter);
        if(!sides.isEmpty()){
            return Optional.of(sides.get(
                    Myewtilities.getRandomListElement(sides.size())));
        }
        else {
            return Optional.empty();
        }
    }

    public static ArrayList<BlockPos> pruneBlockPosList(Iterable<BlockPos> list, Predicate<BlockPos> filter){
        ArrayList<BlockPos> filteredList = new ArrayList<>();
        list.forEach(blockPos -> {
            if(filter.test(blockPos)){
                filteredList.add(new BlockPos(blockPos));
            }
        });
        return filteredList;
    }

    public static Boolean isExposedToAir(Level level, BlockPos pos){
        return getRandomSide(pos, blockPos -> level.getBlockState(blockPos).isAir()).isPresent();
    }
}
