package myewphi.myewventions.block.cubezio;

import myewphi.myewventions.blockentity.cubezio.AbstractProcessorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;


public abstract class AbstractProcessorBlock extends BaseEntityBlock {

    protected AbstractProcessorBlock(Properties properties) {
        super(properties);
    }
    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(state.getBlock() != newState.getBlock()){
            if(level.getBlockEntity(pos) instanceof AbstractProcessorBlockEntity blockEntity){
                if(blockEntity.getBlockState().getBlock() == this){
                    blockEntity.dropContents(level, pos);
                    level.updateNeighbourForOutputSignal(pos, this);
                }
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(!level.isClientSide()){
            if(level.getBlockEntity(pos) instanceof AbstractProcessorBlockEntity blockEntity){
                if(blockEntity.getBlockState().getBlock() == this){
                    player.sendSystemMessage(Component.literal("Processor").setStyle(Style.EMPTY.withColor(0xfffc00).withBold(true).withUnderlined(true)));
                    player.sendSystemMessage(Component.translatable(state.getBlock().getDescriptionId()));
                    player.sendSystemMessage(Component.literal("Items").setStyle(Style.EMPTY.withColor(0xfffc00).withBold(true).withUnderlined(true)));
                    for(int i = 0; i < blockEntity.BASE_INVENTORY_HANDLER.getSlots(); i++){
                        player.sendSystemMessage(Component.literal(blockEntity.BASE_INVENTORY_HANDLER.getStackInSlot(i).toString()));
                    }

                    player.sendSystemMessage(Component.literal("Heats").setStyle(Style.EMPTY.withColor(0xfffc00).withBold(true).withUnderlined(true)));
                    for(int i = 0; i < blockEntity.BASE_INVENTORY_HANDLER.getHeatSlots(); i++){
                        player.sendSystemMessage(Component.literal(String.valueOf(blockEntity.BASE_INVENTORY_HANDLER.getHeatInSlot(i))));
                    }
                }
            }
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }
}
