package myewphi.myewventions.block.blockentity;

import myewphi.myewventions.capability.CubezInventoryHandler;
import myewphi.myewventions.capability.CubezSidedInventoryHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractProcessorBlockEntity extends BlockEntity {

    public AbstractProcessorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    //Item Handlers
    protected CubezInventoryHandler baseItemHandler(int solidSize, int heatSize){
        return new CubezInventoryHandler(solidSize, heatSize) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                if(!level.isClientSide()) {
                    level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
                }
            }

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

            @Override
            public int getHeatLimit(int slot) {
                return 500;
            }
        };
    }

    protected CubezSidedInventoryHandler sidedItemHandler(int[] slots, String io, Direction dir) {
        return new CubezSidedInventoryHandler(slots, io, dir, BASE_ITEM_HANDLER);
    }
    protected CubezSidedInventoryHandler sidedItemHandler(int[] slots, Direction dir) {
        return sidedItemHandler(slots, "both", dir);
    }

    public CubezInventoryHandler BASE_ITEM_HANDLER = baseItemHandler(0, 0);
    public CubezSidedInventoryHandler UP_ITEM_HANDLER = new CubezSidedInventoryHandler(new int[]{0}, "none", Direction.UP, BASE_ITEM_HANDLER);
    public CubezSidedInventoryHandler DOWN_ITEM_HANDLER = new CubezSidedInventoryHandler(new int[]{0}, "none", Direction.DOWN, BASE_ITEM_HANDLER);
    public CubezSidedInventoryHandler NORTH_ITEM_HANDLER = new CubezSidedInventoryHandler(new int[]{0}, "none", Direction.NORTH, BASE_ITEM_HANDLER);
    public CubezSidedInventoryHandler EAST_ITEM_HANDLER = new CubezSidedInventoryHandler(new int[]{0}, "none", Direction.EAST, BASE_ITEM_HANDLER);
    public CubezSidedInventoryHandler SOUTH_ITEM_HANDLER = new CubezSidedInventoryHandler(new int[]{0}, "none", Direction.SOUTH, BASE_ITEM_HANDLER);
    public CubezSidedInventoryHandler WEST_ITEM_HANDLER = new CubezSidedInventoryHandler(new int[]{0}, "none", Direction.WEST, BASE_ITEM_HANDLER);

    public IItemHandler getItemHandler(@Nullable Direction side) {
        if(side != null){
            switch(side){
                case UP -> {
                    return UP_ITEM_HANDLER;
                }
                case DOWN -> {
                    return DOWN_ITEM_HANDLER;
                }
                case NORTH -> {
                    return NORTH_ITEM_HANDLER;
                }
                case EAST -> {
                    return EAST_ITEM_HANDLER;
                }
                case SOUTH -> {
                    return SOUTH_ITEM_HANDLER;
                }
                case WEST -> {
                    return WEST_ITEM_HANDLER;
                }

            }
        }
        return null;
    }

    public void dropContents(Level level, BlockPos pos){
        for(int i = 0; i < BASE_ITEM_HANDLER.getSlots(); i++){
            if(!BASE_ITEM_HANDLER.getStackInSlot(i).isEmpty()){
                level.addFreshEntity(new ItemEntity(level, pos.getCenter().x, pos.getCenter().y, pos.getCenter().z, BASE_ITEM_HANDLER.getStackInSlot(i)));
            }
        }
    }

    //Saving and loading
    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.put("inventory", BASE_ITEM_HANDLER.serializeNBT(pRegistries));

        super.saveAdditional(pTag, pRegistries);
    }
    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        BASE_ITEM_HANDLER.deserializeNBT(pRegistries, pTag.getCompound("inventory"));
    }
}
