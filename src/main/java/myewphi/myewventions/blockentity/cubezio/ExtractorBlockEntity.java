package myewphi.myewventions.blockentity.cubezio;

import myewphi.myewventions.block.cubezio.ExtractorBlock;
import myewphi.myewventions.blockentity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
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

        boolean powered = isPowered(level, pos);
        if(blockState.getValue(ExtractorBlock.ENABLED) == powered){
            level.setBlock(pos, blockState.setValue(ExtractorBlock.ENABLED, !powered), 3);
        }

        blockEntity.COOLDOWN_TIME--;
        if (!blockEntity.isOnCooldown() && blockState.getValue(BlockStateProperties.ENABLED)) {
            blockEntity.setCooldown(0);

            IItemHandler itemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos.relative(getInputSide(blockState)), getInputSide(blockState).getOpposite());

            if(tryPushItems(level, pos, itemHandler, getOutputSide(blockState))){
                setCooldown(8);
            }
        }
    }

    Direction getInputSide(BlockState state){
        return state.getValue(ExtractorBlock.FACING);
    }
    public Direction getOutputSide(BlockState state){
        return state.getValue(ExtractorBlock.FACING).getOpposite();
    }
}
