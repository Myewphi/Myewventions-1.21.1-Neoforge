package myewphi.myewventions.block.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;


public class HeaterBlockEntity extends AbstractProcessorBlockEntity {
    public HeaterBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.HEATER_BE.get(), pos, blockState);
    }

    protected String UP_IO = "both";
    protected String UP_TYPE = "none";
    protected int[] UP_SLOTS = new int[]{0};
    protected String DOWN_IO = "both";
    protected String DOWN_TYPE = "none";
    protected int[] DOWN_SLOTS = new int[]{0};
    protected String NORTH_IO = "none";
    protected String NORTH_TYPE = "none";
    protected int[] NORTH_SLOTS = new int[]{0};
    protected String EAST_IO = "none";
    protected String EAST_TYPE = "none";
    protected int[] EAST_SLOTS = new int[]{0};
    protected String SOUTH_IO = "none";
    protected String SOUTH_TYPE = "none";
    protected int[] SOUTH_SLOTS = new int[]{0};
    protected String WEST_IO = "none";
    protected String WEST_TYPE = "none";
    protected int[] WEST_SLOTS = new int[]{0};

    public IItemHandler getItemHandler(@Nullable Direction side) {
        if(side != null){
            switch(side){
                case UP -> {
                    return UP_ITEM_HANDLER;
                }
                case DOWN -> {
                    return DOWN_ITEM_HANDLER;
                }
            }
        }
        return null;
    }

    public ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }

        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return 1;
        }
    };

    public CubezItemHandler UP_ITEM_HANDLER = new CubezItemHandler(0, UP_IO, Direction.UP, itemHandler);
    public CubezItemHandler DOWN_ITEM_HANDLER = new CubezItemHandler(0, DOWN_IO, Direction.DOWN, itemHandler);

    //Saving and loading
    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.put("inventory", itemHandler.serializeNBT(pRegistries));

        super.saveAdditional(pTag, pRegistries);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        itemHandler.deserializeNBT(pRegistries, pTag.getCompound("inventory"));
    }
}
