package myewphi.myewventions.item;

import myewphi.myewventions.Myewventions;
import myewphi.myewventions.block.ModBlocks;
import myewphi.myewventions.block.misc.StardustBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class StardustItem extends Item {
    public StardustItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos blockpos = context.getClickedPos();
        Level level = context.getLevel();
        Direction dir = context.getClickedFace();
        context.getClickLocation();

        Myewventions.LOGGER.info("TEST: " + (context.getClickLocation().subtract(blockpos.getCenter())));

        if(Block.isFaceFull(level.getBlockState(blockpos).getShape(level, blockpos), dir)){
            if(level.getBlockState(blockpos.relative(dir)).isAir()){
                level.setBlock(blockpos.relative(dir), getPlacementState(context), 3);
            }
        }

        return InteractionResult.PASS;
    }

    BlockState getPlacementState(UseOnContext context){
        BlockPos blockpos = context.getClickedPos();
        Vec3 clickPos = context.getClickLocation().subtract(blockpos.getCenter());

        if(clickPos.z > 0){
            if(clickPos.x > 0){
                return ModBlocks.STARDUST_BLOCK.get().defaultBlockState().setValue(StardustBlock.SECorner, 1);
            }
            return ModBlocks.STARDUST_BLOCK.get().defaultBlockState().setValue(StardustBlock.SWCorner, 1);
        }
        else if(clickPos.x > 0){
            return ModBlocks.STARDUST_BLOCK.get().defaultBlockState().setValue(StardustBlock.NECorner, 1);
        }
        return ModBlocks.STARDUST_BLOCK.get().defaultBlockState().setValue(StardustBlock.NWCorner, 1);
    }
}
