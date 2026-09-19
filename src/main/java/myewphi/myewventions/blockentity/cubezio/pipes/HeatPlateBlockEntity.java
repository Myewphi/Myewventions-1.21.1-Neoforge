package myewphi.myewventions.blockentity.cubezio.pipes;

import myewphi.myewventions.blockentity.ModBlockEntities;
import myewphi.myewventions.blockentity.cubezio.AbstractCubezBlockEntity;
import myewphi.myewventions.capability.HeatHandler;
import myewphi.myewventions.capability.IHeatHandler;
import myewphi.myewventions.capability.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class HeatPlateBlockEntity extends AbstractCubezBlockEntity {
    public HeatHandler HEAT = new HeatHandler() {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    public HeatPlateBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.HEAT_PLATE_BE.get(), pos, blockState);
    }

    @Override
    public void dropAllContents(Level level, BlockPos pos) {

    }

    @Override
    public IHeatHandler getHeatHandler(@Nullable Direction side) {
        return HEAT;
    }

    public void tick(Level level, BlockPos pos, BlockState blockState, HeatPlateBlockEntity blockEntity){
        if(level.isClientSide()){
            return;
        }

        blockEntity.COOLDOWN_TIME--;
        if (!blockEntity.isOnCooldown()) {
            HEAT.setHeat(Math.max(0, getSurroundingHeat(level, pos) - 1));
            if(HEAT.getHeat() != 0){
                setCooldown(8);
            }
        }
    }
    int getSurroundingHeat(Level level, BlockPos pos){
        int heat = 0;
        for(Direction dir : Direction.values()){
            IHeatHandler heatHandler = level.getCapability(ModCapabilities.HeatHandler.BLOCK, pos.relative(dir), dir.getOpposite());

            if(heatHandler == null){
                continue;
            }

            heat = Math.max(heat, heatHandler.getHeat());
        }

        return heat;
    }

    //Saving and loading
    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.put("heat", HEAT.serializeNBT(pRegistries));

        super.saveAdditional(pTag, pRegistries);
    }
    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        HEAT.deserializeNBT(pRegistries, pTag.getCompound("heat"));
    }
}
