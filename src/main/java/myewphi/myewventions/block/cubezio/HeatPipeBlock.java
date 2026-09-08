package myewphi.myewventions.block.cubezio;

import com.mojang.serialization.MapCodec;
import myewphi.myewventions.blockentity.ModBlockEntities;
import myewphi.myewventions.blockentity.cubezio.HeatPipeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class HeatPipeBlock extends AbstractPipeBlock{
    public static final MapCodec<HeatPipeBlock> CODEC = simpleCodec(HeatPipeBlock::new);
    private int HEAT_LIMIT = 1;

    public HeatPipeBlock(Properties properties) {
        super(properties, false, true);
    }
    public HeatPipeBlock(Properties properties, int heatLimit) {
        super(properties, false, true);
        HEAT_LIMIT = heatLimit;
    }
    @Override
    boolean isSameBlockType(Block block) {
        return block instanceof HeatPipeBlock;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HeatPipeBlockEntity(pos, state, HEAT_LIMIT);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide()) {
            return null;
        }

        return createTickerHelper(blockEntityType, ModBlockEntities.HEAT_PIPE_BE.get(),
                (level1, blockPos, blockState, blockEntity) -> blockEntity.tick(level1, blockPos, blockState, blockEntity));
    }

    protected void sayMachineInfo(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, BlockEntity blockEntity) {
        super.sayMachineInfo(state, level, pos, player, hitResult, blockEntity);

        HeatPipeBlockEntity heatPipeBlockEntity = (HeatPipeBlockEntity) blockEntity;

        sayMachineInfoLine(player, "Pipe", state.getBlock().getDescriptionId());
        sayMachineInfoLine(player, "Heat", String.valueOf(heatPipeBlockEntity.INV.getHeatInSlot(0)));
    }
}
