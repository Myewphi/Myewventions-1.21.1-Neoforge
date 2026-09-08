package myewphi.myewventions.blockentity.cubezio;

import myewphi.myewventions.capability.CubezInventoryHandler;
import myewphi.myewventions.capability.CubezSidedInventoryHandler;
import myewphi.myewventions.capability.IHeatHandler;
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

public abstract class DeprecatedAbstractCubezBlockEntity extends BlockEntity {

    public DeprecatedAbstractCubezBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    //Item Handlers
    protected CubezInventoryHandler baseInventoryHandler(int solidSize, int heatSize){
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

    protected CubezSidedInventoryHandler sidedInventoryHandler(int[] slots, String io, Direction dir) {
        return new CubezSidedInventoryHandler(slots, io, dir, BASE_INVENTORY_HANDLER);
    }
    protected CubezSidedInventoryHandler sidedInventoryHandler(int[] slots, Direction dir) {
        return sidedInventoryHandler(slots, "both", dir);
    }

    public CubezInventoryHandler BASE_INVENTORY_HANDLER = baseInventoryHandler(0, 0);
    public CubezSidedInventoryHandler UP_INVENTORY_HANDLER = new CubezSidedInventoryHandler(new int[]{0}, "none", Direction.UP, BASE_INVENTORY_HANDLER);
    public CubezSidedInventoryHandler DOWN_INVENTORY_HANDLER = new CubezSidedInventoryHandler(new int[]{0}, "none", Direction.DOWN, BASE_INVENTORY_HANDLER);
    public CubezSidedInventoryHandler NORTH_INVENTORY_HANDLER = new CubezSidedInventoryHandler(new int[]{0}, "none", Direction.NORTH, BASE_INVENTORY_HANDLER);
    public CubezSidedInventoryHandler EAST_INVENTORY_HANDLER = new CubezSidedInventoryHandler(new int[]{0}, "none", Direction.EAST, BASE_INVENTORY_HANDLER);
    public CubezSidedInventoryHandler SOUTH_INVENTORY_HANDLER = new CubezSidedInventoryHandler(new int[]{0}, "none", Direction.SOUTH, BASE_INVENTORY_HANDLER);
    public CubezSidedInventoryHandler WEST_INVENTORY_HANDLER = new CubezSidedInventoryHandler(new int[]{0}, "none", Direction.WEST, BASE_INVENTORY_HANDLER);

    public IItemHandler getItemHandler(@Nullable Direction side) {
        if(side != null){
            switch(side){
                case UP -> {
                    return UP_INVENTORY_HANDLER;
                }
                case DOWN -> {
                    return DOWN_INVENTORY_HANDLER;
                }
                case NORTH -> {
                    return NORTH_INVENTORY_HANDLER;
                }
                case EAST -> {
                    return EAST_INVENTORY_HANDLER;
                }
                case SOUTH -> {
                    return SOUTH_INVENTORY_HANDLER;
                }
                case WEST -> {
                    return WEST_INVENTORY_HANDLER;
                }

            }
        }
        return null;
    }
    public IHeatHandler getHeatHandler(@Nullable Direction side) {
        if(side != null){
            switch(side){
                case UP -> {
                    return UP_INVENTORY_HANDLER;
                }
                case DOWN -> {
                    return DOWN_INVENTORY_HANDLER;
                }
                case NORTH -> {
                    return NORTH_INVENTORY_HANDLER;
                }
                case EAST -> {
                    return EAST_INVENTORY_HANDLER;
                }
                case SOUTH -> {
                    return SOUTH_INVENTORY_HANDLER;
                }
                case WEST -> {
                    return WEST_INVENTORY_HANDLER;
                }

            }
        }
        return null;
    }

    public void dropContents(Level level, BlockPos pos){
        for(int i = 0; i < BASE_INVENTORY_HANDLER.getSlots(); i++){
            if(!BASE_INVENTORY_HANDLER.getStackInSlot(i).isEmpty()){
                level.addFreshEntity(new ItemEntity(level, pos.getCenter().x, pos.getCenter().y, pos.getCenter().z, BASE_INVENTORY_HANDLER.getStackInSlot(i)));
            }
        }
    }

    //Saving and loading
    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.put("inventory", BASE_INVENTORY_HANDLER.serializeNBT(pRegistries));

        super.saveAdditional(pTag, pRegistries);
    }
    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        BASE_INVENTORY_HANDLER.deserializeNBT(pRegistries, pTag.getCompound("inventory"));
    }
}
