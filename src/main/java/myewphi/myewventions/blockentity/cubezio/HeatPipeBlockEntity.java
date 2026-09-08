package myewphi.myewventions.blockentity.cubezio;

import myewphi.myewventions.block.cubezio.AbstractPipeBlock;
import myewphi.myewventions.blockentity.ModBlockEntities;
import myewphi.myewventions.capability.HeatHandler;
import myewphi.myewventions.capability.IHeatHandler;
import myewphi.myewventions.capability.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public class HeatPipeBlockEntity extends AbstractCubezBlockEntity{
    protected int cooldownTime = -1;
    private int HEAT_LIMIT = 1;
    public HeatHandler INV = new HeatHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }

        @Override
        public int getHeatLimit(int slot) {
            return HEAT_LIMIT;
        }
    };

    public HeatPipeBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.HEAT_PIPE_BE.get(), pos, blockState);
    }
    public HeatPipeBlockEntity(BlockPos pos, BlockState blockState, int heatLimit) {
        super(ModBlockEntities.HEAT_PIPE_BE.get(), pos, blockState);
        HEAT_LIMIT = heatLimit;
    }

    @Override
    public void dropAllContents(Level level, BlockPos pos) {

    }

    @Override
    public IHeatHandler getHeatHandler(@Nullable Direction side) {
        return INV;
    }

    public void tick(Level level, BlockPos pos, BlockState blockState, HeatPipeBlockEntity blockEntity){
        if(level.isClientSide()){
            return;
        }
        blockEntity.cooldownTime--;
        if (!blockEntity.isOnCooldown()) {
            blockEntity.setCooldown(0);
            tryPushHeat(level, pos);
            tryPullHeat(level, pos);
        }
    }
    protected boolean tryPushHeat(Level level, BlockPos pos){
        //is slot empty?
        if(INV.getHeatInSlot(0) == 0){
            return false;
        }

        //do we have an output?
        BlockState state = level.getBlockState(pos);
        if(state.getValue(AbstractPipeBlock.HAS_OUTPUT).equals(Boolean.FALSE)){
            return false;
        }

        //does output block have an IHeatHandler?
        Direction outputDir = state.getValue(AbstractPipeBlock.OUTPUT_FACE);
        IHeatHandler outputHeatHandler = level.getCapability(ModCapabilities.HeatHandler.BLOCK, pos.relative(outputDir), outputDir.getOpposite());
        if(outputHeatHandler == null){
            return false;
        }

        //try and push
        int insertSlot = getHeatInsertSlot(outputHeatHandler);
        if(insertSlot != -1){
            //first; extract heat from INV
            int heat = INV.extractHeat(0, INV.getHeatLimit(0), false);

            //second; insert heat into outputItemHandler
            heat = outputHeatHandler.insertHeat(insertSlot, heat, false);

            //finally; insert any remainder back into INV and start cooldown
            INV.insertHeat(0, heat, false);
            setCooldown(8);
            return true;
        }

        return false;
    }
    protected boolean tryPullHeat(Level level, BlockPos pos){
        //is slot full?
        if(INV.getHeatInSlot(0) == INV.getHeatLimit(0)){
            return false;
        }

        //do we have an input?
        BlockState state = level.getBlockState(pos);
        if(state.getValue(AbstractPipeBlock.HAS_INPUT).equals(Boolean.FALSE)){
            return false;
        }

        //does input block have an IHeatHandler?
        Direction inputDir = state.getValue(AbstractPipeBlock.INPUT_FACE);
        IHeatHandler inputHeatHandler = level.getCapability(ModCapabilities.HeatHandler.BLOCK, pos.relative(inputDir), inputDir.getOpposite());
        if(inputHeatHandler == null){
            return false;
        }

        //try and pull
        int extractSlot = getHeatExtractSlot(inputHeatHandler);
        if(extractSlot != -1){
            //first; extract heat from inputHeatHandler
            int heat = inputHeatHandler.extractHeat(extractSlot, INV.getHeatLimit(0), false);

            //second; insert heat into INV
            heat = INV.insertHeat(0, heat, false);

            //finally; insert any remainder back into inputHeatHandler and start cooldown
            inputHeatHandler.insertHeat(extractSlot, heat, false);
            setCooldown(8);
            return true;
        }

        return false;
    }
    int getHeatInsertSlot(IHeatHandler heatHandler){
        //Return -1 if no heat can be inserted

        for(int i = 0; i < heatHandler.getHeatSlots(); i++){
            //if heat slot isnt full, then we can insert into this slot
            if(heatHandler.getHeatInSlot(i) != heatHandler.getHeatLimit(i)){
                return i;
            }
        }

        return -1;
    }
    int getHeatExtractSlot(IHeatHandler heatHandler){
        //Return -1 if no heat can be extracted

        for(int i = 0; i < heatHandler.getHeatSlots(); i++){
            //if heat slot isn't empty, then we can extract from this slot
            if(heatHandler.getHeatInSlot(i) != 0){
                return i;
            }
        }

        return -1;
    }

    public void setCooldown(int cooldownTime) {
        this.cooldownTime = cooldownTime;
    }
    protected boolean isOnCooldown() {
        return this.cooldownTime > 0;
    }
}
