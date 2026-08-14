package myewphi.myewventions.block.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

public abstract class AbstractProcessorBlockEntity extends BlockEntity {

    public AbstractProcessorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        UP_ITEM_HANDLER = new CubezItemHandler(0, UP_IO, Direction.UP, itemHandler);
        DOWN_ITEM_HANDLER = new CubezItemHandler(0, DOWN_IO, Direction.DOWN, itemHandler);
    }

    public final ItemStackHandler itemHandler = new ItemStackHandler(1) {
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

    public final CubezItemHandler UP_ITEM_HANDLER;
    public final CubezItemHandler DOWN_ITEM_HANDLER;

    protected final String UP_IO = "none";
    protected String UP_TYPE = "none";
    protected int[] UP_SLOTS = new int[]{0};
    protected String DOWN_IO = "none";
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

    //Container
    /*
    @Override
    public int getMaxStackSize() {
        return 1;
    }
    @Override
    public int getContainerSize() {
        return this.items.size();
    }
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }
    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.getItems()) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }
    @Override
    public ItemStack getItem(int slot) {
        return this.getItems().get(slot);
    }
    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack itemstack = ContainerHelper.removeItem(this.getItems(), slot, amount);
        if (!itemstack.isEmpty()) {
            this.setChanged();
        }

        return itemstack;
    }
    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.getItems(), slot);
    }
    @Override
    public void setItem(int slot, ItemStack stack) {
        ItemStack itemstack = this.items.get(slot);
        boolean flag = !stack.isEmpty() && ItemStack.isSameItemSameComponents(itemstack, stack);
        this.items.set(slot, stack);
        stack.limitSize(this.getMaxStackSize(stack));
        if(!flag){
            this.setChanged();
        }
    }
    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }
    @Override
    public void clearContent() {
        this.getItems().clear();
    }

    //IO
    @Override
    public int[] getSlotsForFace(Direction side) {
        switch (side){
            case UP -> {
                return UP_SLOTS;
            }
            case DOWN -> {
                return DOWN_SLOTS;
            }
            case NORTH -> {
                return NORTH_SLOTS;
            }
            case EAST -> {
                return EAST_SLOTS;
            }
            case SOUTH -> {
                return SOUTH_SLOTS;
            }
            case WEST -> {
                return WEST_SLOTS;
            }
            default -> {
                return new int[]{0};
            }
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction) {
        if(direction == null){
            return false;
        }
        switch (direction){
            case UP -> {
                return UP_TYPE.equals("solid") && (UP_IO.equals("both") || UP_IO.equals("input"));
            }
            case DOWN -> {
                return DOWN_TYPE.equals("solid") && (DOWN_IO.equals("both") || DOWN_IO.equals("input"));
            }
            case NORTH -> {
                return NORTH_TYPE.equals("solid") && (NORTH_IO.equals("both") || NORTH_IO.equals("input"));
            }
            case EAST -> {
                return EAST_TYPE.equals("solid") && (EAST_IO.equals("both") || EAST_IO.equals("input"));
            }
            case SOUTH -> {
                return SOUTH_TYPE.equals("solid") && (SOUTH_IO.equals("both") || SOUTH_IO.equals("input"));
            }
            case WEST -> {
                return WEST_TYPE.equals("solid") && (WEST_IO.equals("both") || WEST_IO.equals("input"));
            }
            default -> {
                return false;
            }
        }
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        switch (direction){
            case UP -> {
                return UP_TYPE.equals("solid") && (UP_IO.equals("both") || UP_IO.equals("output"));
            }
            case DOWN -> {
                return DOWN_TYPE.equals("solid") && (DOWN_IO.equals("both") || DOWN_IO.equals("output"));
            }
            case NORTH -> {
                return NORTH_TYPE.equals("solid") && (NORTH_IO.equals("both") || NORTH_IO.equals("output"));
            }
            case EAST -> {
                return EAST_TYPE.equals("solid") && (EAST_IO.equals("both") || EAST_IO.equals("output"));
            }
            case SOUTH -> {
                return SOUTH_TYPE.equals("solid") && (SOUTH_IO.equals("both") || SOUTH_IO.equals("output"));
            }
            case WEST -> {
                return WEST_TYPE.equals("solid") && (WEST_IO.equals("both") || WEST_IO.equals("output"));
            }
            default -> {
                return false;
            }
        }
    }
     */
}
