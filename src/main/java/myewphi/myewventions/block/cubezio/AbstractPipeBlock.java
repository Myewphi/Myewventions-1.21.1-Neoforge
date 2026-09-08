package myewphi.myewventions.block.cubezio;

import myewphi.myewventions.Myewventions;
import myewphi.myewventions.blockentity.cubezio.AbstractPipeBlockEntity;
import myewphi.myewventions.capability.IHeatHandler;
import myewphi.myewventions.capability.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractPipeBlock extends AbstractCubezBlock {
    private final boolean TRANSFERS_ITEMS;
    private final boolean TRANSFERS_HEAT;

    private static final VoxelShape NORTH = Block.box(2, 2, 0, 14, 14, 2);
    private static final VoxelShape EAST = Block.box(14, 2, 2, 16, 14, 14);
    private static final VoxelShape SOUTH = Block.box(2, 2, 14, 14, 14, 16);
    private static final VoxelShape WEST = Block.box(0, 2, 2, 2, 14, 14);
    private static final VoxelShape UP = Block.box(2, 14, 2, 14, 16, 14);
    private static final VoxelShape DOWN = Block.box(2, 0, 2, 14, 2, 14);

    public static final DirectionProperty INPUT_FACE = DirectionProperty.create("input_facing", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN);
    public static final BooleanProperty HAS_INPUT = BooleanProperty.create("has_input");
    public static final DirectionProperty OUTPUT_FACE = DirectionProperty.create("output_facing", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN);
    public static final BooleanProperty HAS_OUTPUT = BooleanProperty.create("has_output");

    protected AbstractPipeBlock(Properties properties, boolean transfersItems, boolean transfersHeat) {
        super(properties);

        TRANSFERS_ITEMS = transfersItems;
        TRANSFERS_HEAT = transfersHeat;

        registerDefaultState(stateDefinition.any()
                .setValue(INPUT_FACE, Direction.UP)
                .setValue(HAS_INPUT, Boolean.FALSE)
                .setValue(OUTPUT_FACE, Direction.DOWN)
                .setValue(HAS_OUTPUT, Boolean.FALSE)
        );
    }

    abstract boolean isSameBlockType(Block block);
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape base = Block.box(2, 2, 2, 14, 14, 14);
        if(state.getValue(HAS_INPUT).equals(Boolean.TRUE)){
            switch(state.getValue(INPUT_FACE)){
                case Direction.NORTH -> base = Shapes.or(base, NORTH);
                case Direction.EAST -> base = Shapes.or(base, EAST);
                case Direction.SOUTH -> base = Shapes.or(base, SOUTH);
                case Direction.WEST -> base = Shapes.or(base, WEST);
                case Direction.UP -> base = Shapes.or(base, UP);
                case Direction.DOWN -> base = Shapes.or(base, DOWN);
            }
        }
        if(state.getValue(HAS_OUTPUT).equals(Boolean.TRUE)){
            switch(state.getValue(OUTPUT_FACE)){
                case Direction.NORTH -> base = Shapes.or(base, NORTH);
                case Direction.EAST -> base = Shapes.or(base, EAST);
                case Direction.SOUTH -> base = Shapes.or(base, SOUTH);
                case Direction.WEST -> base = Shapes.or(base, WEST);
                case Direction.UP -> base = Shapes.or(base, UP);
                case Direction.DOWN -> base = Shapes.or(base, DOWN);
            }
        }
        return base;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(INPUT_FACE, HAS_INPUT, OUTPUT_FACE, HAS_OUTPUT);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        //Create all valid connections
        boolean hasInput = false;
        boolean hasOutput = false;
        Direction inputDir = Direction.UP;
        Direction outputDir = Direction.UP;

        //look for valid pipe inputs
        for(Direction dir : Direction.values()){
            BlockState neighborState = context.getLevel().getBlockState(context.getClickedPos().relative(dir));

            if(isValidPipeInput(neighborState, dir)){
                hasInput = true;
                inputDir = dir;
                break;
            }
        }

        //look for valid pipe outputs
        for(Direction dir : Direction.values()){
            BlockState neighborState = context.getLevel().getBlockState(context.getClickedPos().relative(dir));

            if(hasInput && inputDir.equals(dir)){
                //we cannot have an input and output going to the same face, so if there is already an input facing this direction, do not connect
                continue;
            }
            if(isValidPipeOutput(neighborState, dir)){
                hasOutput = true;
                outputDir = dir;
                break;
            }
        }

        //look for valid container connections
        for(Direction dir : Direction.values()){
            BlockState neighborState = context.getLevel().getBlockState(context.getClickedPos().relative(dir));
            if(isSameBlockType(neighborState.getBlock())){
                continue;
            }
            if(canConnectToContainer(context.getLevel(), context.getClickedPos().relative(dir), dir.getOpposite())){
                if(!hasInput){
                    if(hasOutput && outputDir.equals(dir)){
                        continue;
                    }
                    hasInput = true;
                    inputDir = dir;
                    continue;
                }
                if(!hasOutput){
                    if(inputDir.equals(dir)){
                        continue;
                    }
                    hasOutput = true;
                    outputDir = dir;
                }
            }
        }

        return defaultBlockState()
                .setValue(INPUT_FACE, inputDir)
                .setValue(HAS_INPUT, hasInput)
                .setValue(OUTPUT_FACE, outputDir)
                .setValue(HAS_OUTPUT, hasOutput);
    }
    @Override
    protected BlockState updateShape(BlockState state, Direction dir, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        //check if neighbor side is still valid and update if not
        boolean hasInput = state.getValue(AbstractPipeBlock.HAS_INPUT);
        boolean hasOutput = state.getValue(AbstractPipeBlock.HAS_OUTPUT);
        Direction inputDir = state.getValue(AbstractPipeBlock.INPUT_FACE);
        Direction outputDir = state.getValue(AbstractPipeBlock.OUTPUT_FACE);

        //item pipe
        if(isSameBlockType(neighborState.getBlock())){
            //pipe input
            if(!hasInput){
                if(isValidPipeInput(neighborState, dir)){
                    //if we don't have an input side yet but the new neighbor is a valid input, we can connect
                    hasInput = true;
                    inputDir = dir;
                }
            }
            else{
                if(inputDir.equals(dir) && !isValidPipeInput(neighborState, dir)){
                    //if we already had an input side but that side is no longer valid, we must disconnect
                    hasInput = false;
                }
            }

            //pipe output
            if(!hasOutput){
                if(isValidPipeOutput(neighborState, dir)){
                    //if we don't have an input side yet but the new neighbor is a valid input, we can connect
                    hasOutput = true;
                    outputDir = dir;
                }
            }
            else{
                if(outputDir.equals(dir) && !isValidPipeOutput(neighborState, dir)){
                    //if we already had an input side but that side is no longer valid, we must disconnect
                    hasOutput = false;
                }
            }
        }

        //other container
        else if(canConnectToContainer((Level) level, pos.relative(dir), dir.getOpposite())){
            if(!hasInput){
                if(!(hasOutput && outputDir.equals(dir))){
                    hasInput = true;
                    inputDir = dir;
                }
            }
            if(!hasOutput){
                if(!inputDir.equals(dir)){
                    hasOutput = true;
                    outputDir = dir;
                }
            }
        }

        else {
            if(hasInput && inputDir.equals(dir)){
                hasInput = false;
            }
            if(hasOutput && outputDir.equals(dir)){
                hasOutput = false;
            }
        }

        return defaultBlockState()
                .setValue(INPUT_FACE, inputDir)
                .setValue(HAS_INPUT, hasInput)
                .setValue(OUTPUT_FACE, outputDir)
                .setValue(HAS_OUTPUT, hasOutput);
    }

    boolean hasOpposingInput(BlockState state, Direction dir){
        return hasOpposingIO(state, dir, state.getValue(HAS_INPUT), state.getValue(INPUT_FACE));
    }
    boolean hasOpposingOutput(BlockState state, Direction dir){
        return hasOpposingIO(state, dir, state.getValue(HAS_OUTPUT), state.getValue(OUTPUT_FACE));
    }
    boolean hasOpposingIO(BlockState state, Direction dir, Boolean hasIO, Direction ioFace){
        if(!hasIO){
            //if there is no IO, then there cant be an opposing IO
            return false;
        }
        if(!ioFace.equals(dir.getOpposite())){
            //if the IO facing is not facing towards us then it is not opposing
            //dir.getOpposite() is getting whatever is the opposite side to the direction it is from us
            //so if the block is to the north of us, the side facing us would be the south side of that block
            return false;
        }
        return true;
    }

    boolean isValidPipeInput(BlockState neighborState, Direction dir){
        if(!isSameBlockType(neighborState.getBlock())){
            //move on if not an item pipe
            return false;
        }
        return isValidPipeIO(neighborState.getValue(HAS_INPUT), hasOpposingInput(neighborState, dir), neighborState.getValue(HAS_OUTPUT), hasOpposingOutput(neighborState, dir));
    }
    boolean isValidPipeOutput(BlockState neighborState, Direction dir){
        if(!isSameBlockType(neighborState.getBlock())){
            //move on if not an item pipe
            return false;
        }
        return isValidPipeIO(neighborState.getValue(HAS_OUTPUT), hasOpposingOutput(neighborState, dir), neighborState.getValue(HAS_INPUT), hasOpposingInput(neighborState, dir));
    }
    boolean isValidPipeIO(Boolean hasSameIO, Boolean hasOpposingSameIO, Boolean hasOppositeIO, Boolean hasOpposingOppositeIO){
        if(hasOpposingSameIO){
            //two outputs cannot connect so if the neighbor has an output facing towards us, we do not want to connect
            return false;
        }
        if(hasOppositeIO && !hasOpposingOppositeIO){
            //neighbor cannot have two inputs, so if it already has an input and its not facing us, we do not want to connect
            return false;
        }
        //if none of the above statements were true, then we can connect!
        return true;
    }

    boolean canConnectToContainer(Level level, BlockPos pos, Direction dir){
        Myewventions.LOGGER.info(TRANSFERS_ITEMS + " " + TRANSFERS_HEAT);
        if(TRANSFERS_ITEMS && level.getCapability(Capabilities.ItemHandler.BLOCK, pos, dir) instanceof IItemHandler){
            return true;
        }
        if(TRANSFERS_HEAT && level.getCapability(ModCapabilities.HeatHandler.BLOCK, pos, dir) instanceof IHeatHandler){
            return true;
        }
        return false;
    }
}
