package myewphi.myewventions.block.cubezio;

import myewphi.myewventions.blockentity.cubezio.AbstractCubezBlockEntity;
import myewphi.myewventions.blockentity.cubezio.DeprecatedAbstractCubezBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;


public abstract class AbstractCubezBlock extends BaseEntityBlock {

    protected AbstractCubezBlock(Properties properties) {
        super(properties);
    }
    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(state.getBlock() != newState.getBlock()){
            if(level.getBlockEntity(pos) instanceof AbstractCubezBlockEntity blockEntity){
                if(blockEntity.getBlockState().getBlock() == this){
                    blockEntity.dropAllContents(level, pos);
                    level.updateNeighbourForOutputSignal(pos, this);
                }
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(!level.isClientSide()){
            if(level.getBlockEntity(pos) instanceof AbstractCubezBlockEntity blockEntity){
                if(blockEntity.getBlockState().getBlock() == this){
                    sayMachineInfo(state, level, pos, player, hitResult, blockEntity);
                }
            }
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }
    protected void sayMachineInfo(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, BlockEntity blockEntity){}
    protected void sayMachineInfoLine(Player player, String title, String info){
        player.sendSystemMessage(Component.literal(title + ": " + info));
    }
}
