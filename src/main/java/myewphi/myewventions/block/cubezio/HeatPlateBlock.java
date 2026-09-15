package myewphi.myewventions.block.cubezio;

import com.mojang.serialization.MapCodec;
import myewphi.myewventions.Myewventions;
import myewphi.myewventions.blockentity.ModBlockEntities;
import myewphi.myewventions.blockentity.cubezio.HeatPlateBlockEntity;
import myewphi.myewventions.capability.IHeatHandler;
import myewphi.myewventions.capability.ModCapabilities;
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
import org.jetbrains.annotations.Nullable;

public class HeatPlateBlock extends AbstractCubezBlock{
    public static final MapCodec<HeatPlateBlock> CODEC = simpleCodec(HeatPlateBlock::new);

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

    public HeatPlateBlock(Properties properties) {
        super(properties);

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
        return new HeatPlateBlockEntity(pos, state);
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
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean north =
                context.getLevel().getCapability(ModCapabilities.HeatHandler.BLOCK, context.getClickedPos().relative(Direction.NORTH), Direction.SOUTH) instanceof IHeatHandler;
        boolean east =
                context.getLevel().getCapability(ModCapabilities.HeatHandler.BLOCK, context.getClickedPos().relative(Direction.EAST), Direction.WEST) instanceof IHeatHandler;
        boolean south =
                context.getLevel().getCapability(ModCapabilities.HeatHandler.BLOCK, context.getClickedPos().relative(Direction.SOUTH), Direction.NORTH) instanceof IHeatHandler;
        boolean west =
                context.getLevel().getCapability(ModCapabilities.HeatHandler.BLOCK, context.getClickedPos().relative(Direction.WEST), Direction.EAST) instanceof IHeatHandler;
        boolean up =
                context.getLevel().getCapability(ModCapabilities.HeatHandler.BLOCK, context.getClickedPos().relative(Direction.UP), Direction.DOWN) instanceof IHeatHandler;
        boolean down =
                context.getLevel().getCapability(ModCapabilities.HeatHandler.BLOCK, context.getClickedPos().relative(Direction.DOWN), Direction.UP) instanceof IHeatHandler;

        return defaultBlockState()
                .setValue(NORTH, north)
                .setValue(EAST, east)
                .setValue(SOUTH, south)
                .setValue(WEST, west)
                .setValue(UP, up)
                .setValue(DOWN, down);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return state.setValue(dirToBooleanProperty(direction), ((Level)level).getCapability(ModCapabilities.HeatHandler.BLOCK, neighborPos, direction.getOpposite()) instanceof IHeatHandler);
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

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide()) {
            return null;
        }

        return createTickerHelper(blockEntityType, ModBlockEntities.HEAT_PLATE_BE.get(),
                (level1, blockPos, blockState, blockEntity) -> blockEntity.tick(level1, blockPos, blockState, blockEntity));
    }

    protected void sayMachineInfo(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, BlockEntity blockEntity) {
        super.sayMachineInfo(state, level, pos, player, hitResult, blockEntity);

        HeatPlateBlockEntity heatPlateBlockEntity = (HeatPlateBlockEntity) blockEntity;

        sayMachineInfoLine(player, "Pipe", state.getBlock().getDescriptionId());
        sayMachineInfoLine(player, "Heat", String.valueOf(heatPlateBlockEntity.HEAT.getHeat()));
    }
}
