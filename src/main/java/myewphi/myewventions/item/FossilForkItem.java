package myewphi.myewventions.item;

import myewphi.myewventions.Myewtilities;
import myewphi.myewventions.block.AncientStoneBlock;
import myewphi.myewventions.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class FossilForkItem extends Item {
    public FossilForkItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if(level.isClientSide()){
            if(level.getBlockState(pos).getBlock() instanceof AncientStoneBlock){
                int adjacentFossils = Myewtilities.pruneBlockPosList(Myewtilities.getSides(pos), level, ModBlocks.ANCIENT_STONE_FOSSIL.get()).size();
                context.getPlayer().sendSystemMessage(Component.literal("Adjacent Fossils: " + adjacentFossils));
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        } else {
            return super.useOn(context);
        }
    }
}
