package myewphi.myewventions.block.cubezio;

import com.mojang.serialization.MapCodec;
import myewphi.myewventions.Myewventions;
import myewphi.myewventions.blockentity.ModBlockEntities;
import myewphi.myewventions.blockentity.cubezio.ItemPipeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.Nullable;

public class ItemPipeBlock extends AbstractCubezBlock {
    public static final MapCodec<ItemPipeBlock> CODEC = simpleCodec(ItemPipeBlock::new);
    private int SLOT_LIMIT = 1;

    private static final VoxelShape NORTH_BOX = Block.box(2, 2, 0, 14, 14, 2);
    private static final VoxelShape EAST_BOX = Block.box(14, 2, 2, 16, 14, 14);
    private static final VoxelShape SOUTH_BOX = Block.box(2, 2, 14, 14, 14, 16);
    private static final VoxelShape WEST_BOX = Block.box(0, 2, 2, 2, 14, 14);
    private static final VoxelShape UP_BOX = Block.box(2, 14, 2, 14, 16, 14);
    private static final VoxelShape DOWN_BOX = Block.box(2, 0, 2, 14, 2, 14);

    private static final BooleanProperty NORTH = BooleanProperty.create("north");
    private static final BooleanProperty EAST = BooleanProperty.create("east");
    private static final BooleanProperty SOUTH = BooleanProperty.create("south");
    private static final BooleanProperty WEST = BooleanProperty.create("west");
    private static final BooleanProperty UP = BooleanProperty.create("up");
    private static final BooleanProperty DOWN = BooleanProperty.create("down");

    public ItemPipeBlock(Properties properties){
        super(properties);
    }
    public ItemPipeBlock(Properties properties, int slotLimit){
        super(properties);
        SLOT_LIMIT = slotLimit;

        registerDefaultState(stateDefinition.any()
                .setValue(NORTH, Boolean.FALSE)
                .setValue(EAST, Boolean.FALSE)
                .setValue(SOUTH, Boolean.FALSE)
                .setValue(WEST, Boolean.FALSE)
                .setValue(UP, Boolean.FALSE)
                .setValue(DOWN, Boolean.FALSE)
        );
    }
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ItemPipeBlockEntity(pos, state, SLOT_LIMIT);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape base = Block.box(2, 2, 2, 14, 14, 14);
        if(state.getValue(NORTH).equals(Boolean.TRUE)){
            base = Shapes.or(base, NORTH_BOX);
        }
        if(state.getValue(EAST).equals(Boolean.TRUE)){
            base = Shapes.or(base, EAST_BOX);
        }
        if(state.getValue(SOUTH).equals(Boolean.TRUE)){
            base = Shapes.or(base, SOUTH_BOX);
        }
        if(state.getValue(WEST).equals(Boolean.TRUE)){
            base = Shapes.or(base, WEST_BOX);
        }
        if(state.getValue(UP).equals(Boolean.TRUE)){
            base = Shapes.or(base, UP_BOX);
        }
        if(state.getValue(DOWN).equals(Boolean.TRUE)){
            base = Shapes.or(base, DOWN_BOX);
        }
        return base;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        Direction connectionOne = null;
        Direction connectionTwo = null;

        //look for item pipes first
        for(Direction dir : Direction.values()){
            BlockState neighborState = level.getBlockState(pos.relative(dir));

            if(!(neighborState.getBlock() instanceof ItemPipeBlock)){
                continue;
            }
            if(!isValidPipeNeighbor(neighborState, dir)){
                continue;
            }
            if(connectionOne == null){
                connectionOne = dir;
                continue;
            }
            //if connectionTwo == null
            connectionTwo = dir;
            break;
        }

        //look for all other containers second
        if(connectionTwo == null){
            for(Direction dir : Direction.values()){
                if(!isValidContainerNeighbor(level, pos.relative(dir), dir)){
                    continue;
                }
                if(connectionOne == null){
                    connectionOne = dir;
                    continue;
                }
                if(dir.equals(connectionOne)){
                    continue;
                }
                connectionTwo = dir;
                break;
            }
        }

        Myewventions.LOGGER.info(connectionOne + " " + connectionTwo);

        //set blockstate
        boolean north = false;
        boolean east = false;
        boolean south = false;
        boolean west = false;
        boolean up = false;
        boolean down = false;

        if(connectionOne != null){
            switch(connectionOne){
                case Direction.NORTH -> north = true;
                case Direction.EAST -> east = true;
                case Direction.SOUTH -> south = true;
                case Direction.WEST -> west = true;
                case Direction.UP -> up = true;
                case Direction.DOWN -> down = true;
            }
        }
        if(connectionTwo != null){
            switch(connectionTwo){
                case Direction.NORTH -> north = true;
                case Direction.EAST -> east = true;
                case Direction.SOUTH -> south = true;
                case Direction.WEST -> west = true;
                case Direction.UP -> up = true;
                case Direction.DOWN -> down = true;
            }
        }

        return defaultBlockState()
                .setValue(NORTH, north)
                .setValue(EAST, east)
                .setValue(SOUTH, south)
                .setValue(WEST, west)
                .setValue(UP, up)
                .setValue(DOWN, down);
    }
    @Override
    protected BlockState updateShape(BlockState state, Direction dir, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if(getConnections(state) != null){
            if(getConnections(state).length == 2 && !hasConnection(state, dir)){
                return state;
            }
        }

        if(isValidPipeNeighbor(neighborState, dir) || isValidContainerNeighbor((Level)level, neighborPos, dir)){
            return state.setValue(dirToBooleanProperty(dir), true);
        }
        else {
            return state.setValue(dirToBooleanProperty(dir), false);
        }
    }

    public static boolean hasConnection(BlockState state, Direction dir){
        if(state.getBlock() instanceof ItemPipeBlock){ }
        else { return false; }

        switch(dir){
            case Direction.NORTH -> { return state.getValue(NORTH); }
            case Direction.EAST -> { return state.getValue(EAST); }
            case Direction.SOUTH -> { return state.getValue(SOUTH); }
            case Direction.WEST -> { return state.getValue(WEST); }
            case Direction.UP -> { return state.getValue(UP); }
            case Direction.DOWN -> { return state.getValue(DOWN); }
        }

        return false;
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

    public static Direction[] getConnections(BlockState state){
        Direction[] connections = new Direction[2];

        for(Direction dir : Direction.values()){
            if(hasConnection(state, dir)){
                if(connections[0] == null){
                    connections[0] = dir;
                    continue;
                }
                connections[1] = dir;
                break;
            }
        }

        if(connections[0] == null){
            return null;
        }
        if(connections[1] == null){
            return new Direction[]{ connections[0] };
        }
        return connections;
    }
    boolean isValidPipeNeighbor(BlockState neighborState, Direction dir){
        if(neighborState.getBlock() instanceof ItemPipeBlock){ }
        else { return false; }
        if(getConnections(neighborState) != null){
            if(getConnections(neighborState).length == 2 && !hasConnection(neighborState, dir.getOpposite())){
                return false;
            }
        }
        return true;
    }
    boolean isValidContainerNeighbor(Level level, BlockPos pos, Direction dir){
        if(level.getCapability(Capabilities.ItemHandler.BLOCK, pos, dir.getOpposite()) != null){
            return true;
        }
        return false;
    }
    BooleanProperty dirToBooleanProperty(Direction dir){
        switch (dir){
            case NORTH -> { return NORTH; }
            case EAST -> { return EAST; }
            case SOUTH -> { return SOUTH; }
            case WEST -> { return WEST; }
            case UP -> { return UP; }
            case DOWN -> { return DOWN; }
            default -> { return null; }
        }
    }

    @Override
    protected void sayMachineInfo(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, BlockEntity blockEntity) {
        super.sayMachineInfo(state, level, pos, player, hitResult, blockEntity);

        ItemPipeBlockEntity itemPipeBlockEntity = (ItemPipeBlockEntity) blockEntity;

        sayMachineInfoLine(player, "Pipe", state.getBlock().getDescriptionId());
        sayMachineInfoLine(player, "Inventory", itemPipeBlockEntity.INV_ONE.getStackInSlot(0));
        sayMachineInfoLine(player, "", itemPipeBlockEntity.INV_TWO.getStackInSlot(0));
    }
}
