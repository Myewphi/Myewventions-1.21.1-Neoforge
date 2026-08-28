package myewphi.myewventions.block.cubezio;

import com.mojang.serialization.MapCodec;
import myewphi.myewventions.Myewventions;
import myewphi.myewventions.blockentity.ModBlockEntities;
import myewphi.myewventions.blockentity.cubezio.ItemPipeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
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
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
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
                .setValue(HAS_INPUT, Boolean.FALSE)
                .setValue(OUTPUT_FACE, Direction.DOWN)
                .setValue(HAS_OUTPUT, Boolean.FALSE)
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
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return getPipeShape(defaultBlockState(), context.getLevel(), context.getClickedPos());
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return getPipeShape(state, (Level) level, pos);
    }

    private BlockState getPipeShape(BlockState state, Level level, BlockPos pos){
        boolean hasInput = state.getValue(ItemPipeBlock.HAS_INPUT);
        boolean hasOutput = state.getValue(ItemPipeBlock.HAS_OUTPUT);
        Direction inputDir = state.getValue(ItemPipeBlock.INPUT_FACE);
        Direction outputDir = state.getValue(ItemPipeBlock.OUTPUT_FACE);

        //get input facing
        if(hasInput){
            if(!isValidPipeOrContainer(level, pos, inputDir, HAS_INPUT, INPUT_FACE, HAS_OUTPUT, OUTPUT_FACE)){
                hasInput = false;
            }
        }
        else {
            for(Direction dir : Direction.values()){
                if(isValidPipe(level, pos, dir, HAS_INPUT, INPUT_FACE, HAS_OUTPUT, OUTPUT_FACE)){
                    inputDir = dir;
                    hasInput = true;
                    break;
                }
            }
        }
        //get output facing
        if(hasOutput){
            if(!isValidPipeOrContainer(level, pos, outputDir, HAS_OUTPUT, OUTPUT_FACE, HAS_INPUT, INPUT_FACE)){
                hasOutput = false;
            }
        }
        else {
            for(Direction dir : Direction.values()){
                if(!inputDir.equals(dir)){
                    if(isValidPipe(level, pos, dir, HAS_OUTPUT, OUTPUT_FACE, HAS_INPUT, INPUT_FACE)){
                        outputDir = dir;
                        hasOutput = true;
                        break;
                    }
                }
            }
        }

        return defaultBlockState()
                .setValue(INPUT_FACE, inputDir)
                .setValue(HAS_INPUT, hasInput)
                .setValue(OUTPUT_FACE, outputDir)
                .setValue(HAS_OUTPUT, hasOutput);
    }

    boolean isValidPipeOrContainer(Level level, BlockPos pos, Direction dir, BooleanProperty hasSame, DirectionProperty sameFace, BooleanProperty hasOther, DirectionProperty otherFace){
        return isValidPipe(level, pos, dir, hasSame, sameFace, hasOther, otherFace) || isContainer(level, pos, dir);
    }
    boolean isValidPipe(Level level, BlockPos pos, Direction dir, BooleanProperty hasSame, DirectionProperty sameFace, BooleanProperty hasOther, DirectionProperty otherFace){
        BlockState state = level.getBlockState(pos.relative(dir));
        if(state.getBlock() instanceof ItemPipeBlock){
            if(state.getValue(hasSame).equals(Boolean.TRUE) && state.getValue(sameFace).equals(dir.getOpposite())){
                return false;
            }
            if(state.getValue(hasOther).equals(Boolean.TRUE) && !state.getValue(otherFace).equals(dir.getOpposite())){
                return false;
            }
            return true;
        }
        return false;
    }
    boolean isContainer(Level level, BlockPos pos, Direction dir){
        if(level.getCapability(Capabilities.ItemHandler.BLOCK, pos.relative(dir), dir.getOpposite()) instanceof IItemHandler ){
            return true;
        }
        return false;
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
