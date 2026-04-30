package myewphi.myewventions.item;

import myewphi.myewventions.Myewtilities;
import myewphi.myewventions.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Optional;

public class FossilPlacerItem extends Item {
    public FossilPlacerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        //variables for easy debugging by swapping out values as needed
        int radius = 3;
        int fossilSize = Myewtilities.getRandomRange(5, 15);
        Block stoneBlock = ModBlocks.ANCIENT_STONE.get();
        Block fossilBlock = ModBlocks.ANCIENT_STONE_FOSSIL.get();

        List<BlockPos> cube = Myewtilities.iterableToList(BlockPos.withinManhattan(player.getOnPos().below(radius), radius, radius, radius));
        cube.forEach(blockPos -> level.setBlock(blockPos, stoneBlock.defaultBlockState(), 3));
        BlockPos pos = Myewtilities.getRandomListElement(cube);
        for(int i = 0; i < fossilSize; i++){
            level.setBlock(pos, fossilBlock.defaultBlockState(), 3);

            List<BlockPos> nearbyFossils = Myewtilities.pruneBlockPosList(BlockPos.withinManhattan(pos, 2, 2, 2), level, fossilBlock);
            List<BlockPos> validFossils = Myewtilities.pruneBlockPosList(nearbyFossils,
                    blockPos -> Myewtilities.getRandomSide(blockPos, level, stoneBlock).isPresent());

            Optional<BlockPos> newPos = Myewtilities.getRandomSide(Myewtilities.getRandomListElement(validFossils), level, stoneBlock);
            if(newPos.isPresent()){
                pos = newPos.get();
            }
            else {
                break;
            }
        }

        return super.use(level, player, usedHand);
    }
}
