package myewphi.myewventions.blockentity.cubezio;

import myewphi.myewventions.Myewventions;
import myewphi.myewventions.capability.IHeatHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractCubezBlockEntity extends BlockEntity {
    protected int COOLDOWN_TIME = -1;

    public AbstractCubezBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public static boolean tryPushItems(Level level, BlockPos pos, IItemHandler inv, Direction outputDir){
        //is IItemHandler null?
        if(inv == null){
            return false;
        }

        //does output block have an IItemHandler?
        IItemHandler outputItemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos.relative(outputDir), outputDir.getOpposite());
        if(outputItemHandler == null){
            return false;
        }

        //try and push
        int extractSlot = getItemExtractSlot(inv, outputItemHandler);
        if (extractSlot == -1) {
            return false;
        }

        int insertSlot = getItemInsertSlot(outputItemHandler, inv.getStackInSlot(extractSlot));
        if(insertSlot == -1){
            return false;
        }

        //first; extract stack from INV
        ItemStack stack = inv.extractItem(extractSlot, inv.getSlotLimit(0), false);

        //second; insert stack into outputItemHandler
        stack = outputItemHandler.insertItem(insertSlot, stack, false);

        //finally; insert any remainder back into INV and start cooldown
        inv.insertItem(extractSlot, stack, false);
        return true;
    }
    public static int getItemExtractSlot(IItemHandler extractHandler, IItemHandler insertHandler){
        //Return -1 if no item can be extracted
        for(int i = 0; i < extractHandler.getSlots(); i++){
            if(extractHandler.getStackInSlot(i).isEmpty()){
                continue;
            }

            int insertSlot = getItemInsertSlot(insertHandler, extractHandler.getStackInSlot(i));

            if(insertSlot != -1){
                return i;
            }
        }

        return -1;
    }
    public static int getItemInsertSlot(IItemHandler itemHandler, ItemStack stack){
        //Return -1 if no item can be inserted

        for(int i = 0; i < itemHandler.getSlots(); i++){
            ItemStack remainder = itemHandler.insertItem(i, stack, true);

            if(remainder.getCount() != stack.getCount()){
                return i;
            }
        }

        return -1;
    }

    public abstract void dropAllContents(Level level, BlockPos pos);
    protected void dropContents(Level level, BlockPos pos, ItemStackHandler stackHandler){
        for(int i = 0; i < stackHandler.getSlots(); i++){
            if(!stackHandler.getStackInSlot(i).isEmpty()){
                level.addFreshEntity(new ItemEntity(level, pos.getCenter().x, pos.getCenter().y, pos.getCenter().z, stackHandler.getStackInSlot(i)));
            }
        }
    }

    public void setCooldown(int cooldownTime) {
        this.COOLDOWN_TIME = cooldownTime;
    }
    protected boolean isOnCooldown() {
        return this.COOLDOWN_TIME > 0;
    }

    public IItemHandler getItemHandler(@Nullable Direction side) {
        return null;
    }
    public IHeatHandler getHeatHandler(@Nullable Direction side) {
        return null;
    }
}
