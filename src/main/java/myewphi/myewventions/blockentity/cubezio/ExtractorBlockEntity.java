package myewphi.myewventions.blockentity.cubezio;

import myewphi.myewventions.block.cubezio.ExtractorBlock;
import myewphi.myewventions.blockentity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

public class ExtractorBlockEntity extends AbstractCubezBlockEntity{
    public ExtractorBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.EXTRACTOR_BE.get(), pos, blockState);
    }
    @Override
    public void dropAllContents(Level level, BlockPos pos) { }

    public void tick(Level level, BlockPos pos, BlockState blockState, AbstractCubezBlockEntity blockEntity) {
        if(level.isClientSide()){
            return;
        }
        blockEntity.COOLDOWN_TIME--;
        if (!blockEntity.isOnCooldown()) {
            blockEntity.setCooldown(0);

            IItemHandler itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos.relative(getInputSide(blockState)), getInputSide(blockState));

            if(tryPushItems(level, pos, itemHandler, getOutputSide(blockState))){
                setCooldown(8);
            }
        }
    }
    Direction getInputSide(BlockState state){
        return state.getValue(ExtractorBlock.FACING);
    }
    Direction getOutputSide(BlockState state){
        return state.getValue(ExtractorBlock.FACING).getOpposite();
    }
}
