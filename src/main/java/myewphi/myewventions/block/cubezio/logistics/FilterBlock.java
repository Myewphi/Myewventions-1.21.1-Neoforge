package myewphi.myewventions.block.cubezio.logistics;

import com.mojang.serialization.MapCodec;
import myewphi.myewventions.Myewventions;
import myewphi.myewventions.block.cubezio.AbstractCubezBlock;
import myewphi.myewventions.blockentity.ModBlockEntities;
import myewphi.myewventions.blockentity.cubezio.logistics.FilterBlockEntity;
import myewphi.myewventions.blockentity.cubezio.processors.OvenBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class FilterBlock extends AbstractCubezBlock {
    public static final MapCodec<ExtractorBlock> CODEC = simpleCodec(ExtractorBlock::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;

    public FilterBlock(Properties properties) {
        super(properties);

        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(ENABLED, Boolean.TRUE));
    }
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FilterBlockEntity(pos, state);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, ENABLED);
    }
    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(hitResult.getDirection() != Direction.UP){
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        FilterBlockEntity blockEntity = (FilterBlockEntity) level.getBlockEntity(pos);
        ItemStack handStack = player.getItemInHand(hand);
        ItemStack filterStack = blockEntity.FILTER.extractItem(0, 1, true);

        if(filterStack.isEmpty() && !handStack.isEmpty()){
            if(blockEntity.FILTER.insertItem(0, handStack, true).getCount() != handStack.getCount()){
                player.setItemInHand(hand, blockEntity.FILTER.insertItem(0, handStack, false));
                level.playSound(player, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 2f);
                return ItemInteractionResult.SUCCESS;
            }
        }

        if(!filterStack.isEmpty() && handStack.isEmpty()){
            ItemStack realFilterStack = blockEntity.FILTER.extractItem(0, 1, false);
            player.setItemInHand(hand, realFilterStack);
            level.playSound(player, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 2f);
            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide()) {
            return null;
        }

        return createTickerHelper(blockEntityType, ModBlockEntities.FILTER_BE.get(),
                (level1, blockPos, blockState, blockEntity) -> blockEntity.tick(level1, blockPos, blockState, blockEntity));
    }
    @Override
    protected void sayMachineInfo(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, BlockEntity blockEntity) {
        super.sayMachineInfo(state, level, pos, player, hitResult, blockEntity);

        FilterBlockEntity filterBlockEntity = (FilterBlockEntity) blockEntity;

        sayMachineInfoLine(player, "Logistics", state.getBlock().getDescriptionId());
        sayMachineInfoLine(player, "Inv", filterBlockEntity.INV.getStackInSlot(0));
        sayMachineInfoLine(player, "Filter", filterBlockEntity.FILTER.getStackInSlot(0));
    }
}
