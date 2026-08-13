package myewphi.myewventions.block;

import myewphi.myewventions.Myewtilities;
import myewphi.myewventions.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;

public class AncientStoneBlock extends Block {

    public AncientStoneBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if(!level.isClientSide()){
            onAncientStoneDestroyed(state, level, pos, player);
        }

        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    public void onAncientStoneDestroyed(BlockState state, Level level, BlockPos pos, Player player){
        if(!(state.getBlock() instanceof AncientStoneFossilBlock) && player.getMainHandItem().is(ModItems.CHISEL)){
            Myewtilities.getSides(pos).forEach(blockPos -> {
                if(level.getBlockState(blockPos).getBlock() instanceof AncientStoneBlock){
                    level.destroyBlock(blockPos, true);
                }
            });
        }
    }
}
