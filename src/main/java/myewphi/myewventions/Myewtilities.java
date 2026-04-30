package myewphi.myewventions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Myewtilities {
    private static Predicate<BlockPos> simpleBlockFilter(Level level, Block block){
        return blockPos -> level.getBlockState(blockPos).getBlock() == block;
    }

    public static List<BlockPos> getSides(BlockPos pos) {
        return List.of(pos.above(), pos.below(), pos.north(), pos.east(), pos.south(), pos.west());
    }
    public static <T> T getRandomListElement(List<T> list){
        return list.get((int)Math.round(Math.floor(Math.random() * list.size())));
    }
    public static BlockPos getRandomSide(BlockPos pos){
        return getRandomListElement(getSides(pos));
    }
    public static Optional<BlockPos> getRandomSide(BlockPos pos, Predicate<BlockPos> posFilter){
        ArrayList<BlockPos> sides = pruneBlockPosList(getSides(pos), posFilter);
        if(!sides.isEmpty()){
            return Optional.of(getRandomListElement(sides));
        }
        else {
            return Optional.empty();
        }
    }
    public static Optional<BlockPos> getRandomSide(BlockPos pos, Level level, Block block){
        return getRandomSide(pos, simpleBlockFilter(level, block));
    }
    public static int getRandomRange(int minInclusive, int maxInclusive){
        return Math.toIntExact(Math.round(Math.floor(Math.random() * ((maxInclusive + 1) - minInclusive)))) + minInclusive;
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
    public static ArrayList<BlockPos> pruneBlockPosList(Iterable<BlockPos> list,  Level level, Block block){
        return pruneBlockPosList(list, simpleBlockFilter(level, block));
    }
    public static List<BlockPos> iterableToList(Iterable<BlockPos> iterable){
        ArrayList<BlockPos> newList = new ArrayList<>();
        for(BlockPos pos : iterable){
            newList.add(new BlockPos(pos));
        }
        return newList;
    }

    public static Boolean isExposedToAir(Level level, BlockPos pos){
        return getRandomSide(pos, blockPos -> level.getBlockState(blockPos).isAir()).isPresent();
    }
}
