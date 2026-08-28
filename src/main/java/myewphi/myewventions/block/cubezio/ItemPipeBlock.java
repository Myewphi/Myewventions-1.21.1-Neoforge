package myewphi.myewventions.block.cubezio;

import com.mojang.serialization.MapCodec;
import myewphi.myewventions.blockentity.ModBlockEntities;
import myewphi.myewventions.blockentity.cubezio.ItemPipeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;

public class ItemPipeBlock extends AbstractProcessorBlock {
    public static final MapCodec<ItemPipeBlock> CODEC = simpleCodec(ItemPipeBlock::new);
    public static final DirectionProperty INPUT_FACE = DirectionProperty.create("input_facing", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN);
    public static final BooleanProperty HAS_INPUT = BooleanProperty.create("has_input");
    public static final DirectionProperty OUTPUT_FACE = DirectionProperty.create("output_facing", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN);
    public static final BooleanProperty HAS_OUTPUT = BooleanProperty.create("has_output");


    public ItemPipeBlock(Properties properties) {
        super(properties);

        registerDefaultState(stateDefinition.any()
                .setValue(INPUT_FACE, Direction.UP)
                .setValue(HAS_INPUT, Boolean.TRUE)
                .setValue(OUTPUT_FACE, Direction.DOWN)
                .setValue(HAS_OUTPUT, Boolean.TRUE)
        );
    }
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(INPUT_FACE, HAS_INPUT, OUTPUT_FACE, HAS_OUTPUT);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        /*
        return super.getStateForPlacement(pContext)
                .setValue(INPUT_FACE, Direction.UP)
                .setValue(HAS_INPUT, true)
                .setValue(OUTPUT_FACE, Direction.NORTH)
                .setValue(HAS_OUTPUT, true);
         */
        return defaultBlockState();
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ItemPipeBlockEntity(pos, state);
    }
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide()) {
            return null;
        }

        return createTickerHelper(blockEntityType, ModBlockEntities.ITEM_PIPE_BE.get(),
                (level1, blockPos, blockState, blockEntity) -> blockEntity.tick(level1, blockPos, blockState, blockEntity));
    }
}
