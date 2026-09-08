package myewphi.myewventions.block.cubezio;

import com.mojang.serialization.MapCodec;
import myewphi.myewventions.blockentity.ModBlockEntities;
import myewphi.myewventions.blockentity.cubezio.HeaterBlockEntity;
import myewphi.myewventions.blockentity.cubezio.OvenBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class OvenBlock extends AbstractCubezBlock {
    public static final MapCodec<OvenBlock> CODEC = simpleCodec(OvenBlock::new);

    public OvenBlock(Properties properties) {
        super(properties);
    }
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new OvenBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if(level.isClientSide()) {
            return null;
        }

        return createTickerHelper(blockEntityType, ModBlockEntities.OVEN_BE.get(),
                (level1, blockPos, blockState, blockEntity) -> blockEntity.tick(level1, blockPos, blockState));
    }

    @Override
    protected void sayMachineInfo(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, BlockEntity blockEntity) {
        super.sayMachineInfo(state, level, pos, player, hitResult, blockEntity);

        OvenBlockEntity ovenBlockEntity = (OvenBlockEntity) blockEntity;

        sayMachineInfoLine(player, "Processor", state.getBlock().getDescriptionId());
        sayMachineInfoLine(player, "Input", ovenBlockEntity.INPUT.getStackInSlot(0));
        sayMachineInfoLine(player, "Output", ovenBlockEntity.OUTPUT.getStackInSlot(0));
        sayMachineInfoLine(player, "Heat", String.valueOf((ovenBlockEntity.HEAT.getHeatInSlot(0))));
        sayMachineInfoLine(player, "Progress", String.valueOf(ovenBlockEntity.PROGRESS));
    }
}
