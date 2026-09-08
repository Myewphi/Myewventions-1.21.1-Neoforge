package myewphi.myewventions.blockentity.cubezio;

import myewphi.myewventions.Myewventions;
import myewphi.myewventions.block.cubezio.AbstractPipeBlock;
import myewphi.myewventions.blockentity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class ItemPipeBlockEntity extends AbstractCubezBlockEntity {
    protected int cooldownTime = -1;
    private int SLOT_LIMIT = 1;
    public ItemStackHandler INV = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }

        @Override
        public int getSlotLimit(int slot) {
            return SLOT_LIMIT;
        }
    };

    public ItemPipeBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.ITEM_PIPE_BE.get(), pos, blockState);
    }
    public ItemPipeBlockEntity(BlockPos pos, BlockState blockState, int slotLimit) {
        super(ModBlockEntities.ITEM_PIPE_BE.get(), pos, blockState);
        SLOT_LIMIT = slotLimit;
    }

    @Override
    public void dropAllContents(Level level, BlockPos pos) {
        dropContents(level, pos, INV);
    }

    @Override
    public IItemHandler getItemHandler(@Nullable Direction side) {
        return INV;
    }

    public void tick(Level level, BlockPos pos, BlockState blockState, ItemPipeBlockEntity blockEntity) {
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
        //is slot empty?
        if(INV.getStackInSlot(0).isEmpty()){
            return false;
        }

        //do we have an output?
        BlockState state = level.getBlockState(pos);
        if(state.getValue(AbstractPipeBlock.HAS_OUTPUT).equals(Boolean.FALSE)){
            return false;
        }

        //does output block have an IItemHandler?
        Direction outputDir = state.getValue(AbstractPipeBlock.OUTPUT_FACE);
        IItemHandler outputItemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos.relative(outputDir), outputDir.getOpposite());
        if(outputItemHandler == null){
            return false;
        }

        //try and push
        int insertSlot = getItemInsertSlot(outputItemHandler);
        if(insertSlot != -1){
            //first; extract stack from INV
            ItemStack stack = INV.extractItem(0, INV.getSlotLimit(0), false);

            //second; insert stack into outputItemHandler
            stack = outputItemHandler.insertItem(insertSlot, stack, false);

            //finally; insert any remainder back into INV and start cooldown
            INV.insertItem(0, stack, false);
            setCooldown(8);
            return true;
        }

        return false;
    }
    protected boolean tryPullItems(Level level, BlockPos pos){
        //is slot full?
        if(!INV.getStackInSlot(0).isEmpty()){
            if(INV.getStackInSlot(0).getCount() >= INV.getSlotLimit(0)){
                return false;
            }
        }
        
        //do we have an input?
        BlockState state = level.getBlockState(pos);
        if(state.getValue(AbstractPipeBlock.HAS_INPUT).equals(Boolean.FALSE)){
            return false;
        }
        
        //does input block have an IItemHandler?
        Direction inputDir = state.getValue(AbstractPipeBlock.INPUT_FACE);
        IItemHandler inputItemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos.relative(inputDir), inputDir.getOpposite());
        if(inputItemHandler == null){
            return false;
        }

        //try and pull
        int extractSlot = getItemExtractSlot(inputItemHandler);
        if(extractSlot != -1){
            //first; extract stack from inputItemHandler
            ItemStack stack = inputItemHandler.extractItem(extractSlot, INV.getSlotLimit(0), false);

            //second; insert stack into INV
            stack = INV.insertItem(0, stack, false);

            //finally; insert any remainder back into inputItemHandler and start cooldown
            inputItemHandler.insertItem(extractSlot, stack, false);
            setCooldown(8);
            return true;
        }

        return false;
    }
    int getItemInsertSlot(IItemHandler itemHandler){
        //Return -1 if no item can be inserted

        for(int i = 0; i < itemHandler.getSlots(); i++){
            ItemStack stack = itemHandler.insertItem(i, INV.getStackInSlot(0), true);

            if(stack.getCount() != INV.getStackInSlot(0).getCount()){
                return i;
            }
        }

        return -1;
    }
    int getItemExtractSlot(IItemHandler itemHandler){
        //Return -1 if no item can be extracted

        for(int i = 0; i < itemHandler.getSlots(); i++){
            ItemStack stack = itemHandler.extractItem(i, INV.getSlotLimit(0), true);

            //if the amount of items that can be inserted is unchanged from extracted items, then no items can fit and we should keep searching for a slot
            if(stack.getCount() == INV.insertItem(0, stack, true).getCount()){
                continue;
            }

            //if not, we found our slot!
            if(!stack.isEmpty()){
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
