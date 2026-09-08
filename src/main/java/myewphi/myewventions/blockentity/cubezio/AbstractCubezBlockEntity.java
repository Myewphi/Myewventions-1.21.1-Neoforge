package myewphi.myewventions.blockentity.cubezio;

import myewphi.myewventions.capability.IHeatHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractCubezBlockEntity extends BlockEntity {
    public AbstractCubezBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public abstract void dropAllContents(Level level, BlockPos pos);
    protected void dropContents(Level level, BlockPos pos, ItemStackHandler stackHandler){
        for(int i = 0; i < stackHandler.getSlots(); i++){
            if(!stackHandler.getStackInSlot(i).isEmpty()){
                level.addFreshEntity(new ItemEntity(level, pos.getCenter().x, pos.getCenter().y, pos.getCenter().z, stackHandler.getStackInSlot(i)));
            }
        }
    }

    public IItemHandler getItemHandler(@Nullable Direction side) {
        return null;
    }
    public IHeatHandler getHeatHandler(@Nullable Direction side) {
        return null;
    }
}
