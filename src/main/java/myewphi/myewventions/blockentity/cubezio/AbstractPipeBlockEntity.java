package myewphi.myewventions.blockentity.cubezio;

import myewphi.myewventions.block.cubezio.AbstractPipeBlock;
import myewphi.myewventions.capability.IHeatHandler;
import myewphi.myewventions.capability.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

public abstract class AbstractPipeBlockEntity extends DeprecatedAbstractCubezBlockEntity {
    protected int cooldownTime = -1;
    public int ITEM_SPEED = 0;
    public int HEAT_SPEED = 0;

    public AbstractPipeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);

        BASE_INVENTORY_HANDLER = baseInventoryHandler(1, 0);
        UP_INVENTORY_HANDLER = sidedInventoryHandler(new int[]{0}, Direction.UP);
        DOWN_INVENTORY_HANDLER = sidedInventoryHandler(new int[]{0}, Direction.DOWN);
        NORTH_INVENTORY_HANDLER = sidedInventoryHandler(new int[]{0}, Direction.NORTH);
        EAST_INVENTORY_HANDLER = sidedInventoryHandler(new int[]{0}, Direction.EAST);
        SOUTH_INVENTORY_HANDLER = sidedInventoryHandler(new int[]{0}, Direction.SOUTH);
        WEST_INVENTORY_HANDLER = sidedInventoryHandler(new int[]{0}, Direction.WEST);
    }

    public void itemMoveTick(Level level, BlockPos pos, BlockState blockState, AbstractPipeBlockEntity blockEntity) {
        if(level.isClientSide()){
            return;
        }
        blockEntity.cooldownTime--;
        if (!blockEntity.isOnCooldown()) {
            blockEntity.setCooldown(0);
            tryPushItems(level, pos);
            tryPullItems(level, pos);
        }
    }
    protected boolean tryPushItems(Level level, BlockPos pos){
        if(BASE_INVENTORY_HANDLER.getStackInSlot(0).isEmpty()){
            return false;
        }

        BlockState state = level.getBlockState(pos);
        if(state.getValue(AbstractPipeBlock.HAS_OUTPUT).equals(Boolean.FALSE)){
            return false;
        }
        Direction outputDir = state.getValue(AbstractPipeBlock.OUTPUT_FACE);
        IItemHandler outputItemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos.relative(outputDir), outputDir.getOpposite());

        int insertSlot = getItemInsertSlot(outputItemHandler, BASE_INVENTORY_HANDLER.getStackInSlot(0));
        if(insertSlot >= 0){
            outputItemHandler.insertItem(insertSlot, BASE_INVENTORY_HANDLER.extractItem(0, 1, false), false);
            setCooldown(8);
        }

        return false;
    }
    protected boolean tryPullItems(Level level, BlockPos pos){
        if(!BASE_INVENTORY_HANDLER.getStackInSlot(0).isEmpty()){
            return false;
        }
        BlockState state = level.getBlockState(pos);
        if(state.getValue(AbstractPipeBlock.HAS_INPUT).equals(Boolean.FALSE)){
            return false;
        }
        Direction inputDir = state.getValue(AbstractPipeBlock.INPUT_FACE);
        IItemHandler inputItemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos.relative(inputDir), inputDir.getOpposite());

        int extractSlot = getItemExtractSlot(inputItemHandler);
        if(extractSlot >= 0){
            BASE_INVENTORY_HANDLER.insertItem(0, inputItemHandler.extractItem(extractSlot, 1, false), false);
            setCooldown(8);
            return true;
        }

        return false;
    }
    int getItemInsertSlot(IItemHandler itemHandler, ItemStack stack){
        if(itemHandler != null){
            for(int i = 0; i < itemHandler.getSlots(); i++){
                ItemStack remainder = itemHandler.insertItem(i, stack, true);
                if(remainder.isEmpty()){
                    return i;
                }
            }
        }
        return -1;
    }
    int getItemExtractSlot(IItemHandler itemHandler){
        if(itemHandler != null){
            for(int i = 0; i < itemHandler.getSlots(); i++){
                ItemStack stack = itemHandler.extractItem(i, 1, true);
                if(!stack.isEmpty()){
                    return i;
                }
            }
        }
        return -1;
    }

    public void heatMoveTick(Level level, BlockPos pos, BlockState blockState, AbstractPipeBlockEntity blockEntity){
        if(level.isClientSide()){
            return;
        }
        blockEntity.cooldownTime--;
        if (!blockEntity.isOnCooldown()) {
            blockEntity.setCooldown(0);
            //tryPushHeat(level, pos);
        }
    }
    protected boolean tryPushHeat(Level level, BlockPos pos){
        if(BASE_INVENTORY_HANDLER.getHeatInSlot(0) == 0){
            return false;
        }
        BlockState state = level.getBlockState(pos);
        if(state.getValue(AbstractPipeBlock.HAS_OUTPUT).equals(Boolean.FALSE)){
            return false;
        }
        Direction outputDir = state.getValue(AbstractPipeBlock.OUTPUT_FACE);
        IHeatHandler outputHeatHandler = level.getCapability(ModCapabilities.HeatHandler.BLOCK, pos.relative(outputDir), outputDir.getOpposite());

        int extractAmount = outputHeatHandler.insertHeat(0, BASE_INVENTORY_HANDLER.extractHeat(0, HEAT_SPEED, true), true);
        if(extractAmount > 0){
            outputHeatHandler.insertHeat(0, BASE_INVENTORY_HANDLER.extractHeat(0, extractAmount, false), false);
            setCooldown(8);
        }

        return false;
    }
    protected boolean tryPullHeat(Level level, BlockPos pos){
        if(BASE_INVENTORY_HANDLER.getHeatInSlot(0) == 0){
            return false;
        }

        BlockState state = level.getBlockState(pos);
        if(state.getValue(AbstractPipeBlock.HAS_INPUT).equals(Boolean.FALSE)){
            return false;
        }

        Direction inputDir = state.getValue(AbstractPipeBlock.INPUT_FACE);
        IHeatHandler inputHeatHandler = level.getCapability(ModCapabilities.HeatHandler.BLOCK, pos.relative(inputDir), inputDir.getOpposite());

        /*
        int extractAmount = inputHeatHandler.insertHeat(0, BASE_INVENTORY_HANDLER.extractHeat(0, HEAT_SPEED, true), true);
        if(extractAmount > 0){
            inputHeatHandler.insertHeat(0, BASE_INVENTORY_HANDLER.extractHeat(0, extractAmount, false), false);
            setCooldown(8);
        }
        */

        return false;
    }

    public void setCooldown(int cooldownTime) {
        this.cooldownTime = cooldownTime;
    }
    protected boolean isOnCooldown() {
        return this.cooldownTime > 0;
    }
}
