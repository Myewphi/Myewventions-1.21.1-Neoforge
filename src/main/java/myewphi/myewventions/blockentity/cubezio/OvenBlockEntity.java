package myewphi.myewventions.blockentity.cubezio;

import myewphi.myewventions.Myewventions;
import myewphi.myewventions.block.cubezio.OvenBlock;
import myewphi.myewventions.blockentity.ModBlockEntities;
import myewphi.myewventions.capability.HeatHandler;
import myewphi.myewventions.capability.IHeatHandler;
import myewphi.myewventions.recipe.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class OvenBlockEntity extends AbstractCubezBlockEntity {
    public ItemStackHandler INPUT = new ItemStackHandler(1) {
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
    };
    public ItemStackHandler OUTPUT = new ItemStackHandler(1) {
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
    };
    public HeatHandler HEAT = new HeatHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }

        @Override
        public int getHeatLimit(int slot) {
            return 200;
        }
    };
    public int PROGRESS = 0;

    public OvenBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.OVEN_BE.get(), pos, blockState);
    }

    @Override
    public void dropAllContents(Level level, BlockPos pos) {
        dropContents(level, pos, INPUT);
        dropContents(level, pos, OUTPUT);
    }

    @Override
    public IItemHandler getItemHandler(@Nullable Direction side) {
        if(side == null){
            return null;
        }

        assert level != null;
        BlockState state = level.getBlockState(worldPosition);
        if (side.equals(state.getValue(OvenBlock.FACING))) {
            return INPUT;
        }
        if(side.equals(state.getValue(OvenBlock.FACING).getOpposite())){
            return OUTPUT;
        }

        return null;
    }
    @Override
    public IHeatHandler getHeatHandler(@Nullable Direction side) {
        if(side == null){
            return null;
        }

        if (side.equals(Direction.UP)) {
            return HEAT;
        }

        return null;
    }

    //Crafting
    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if(HEAT.getHeatInSlot(0) == 0){
            return;
        }

        Optional<RecipeHolder<OvenRecipe>> recipe = getCurrentRecipe();
        if(recipe.isEmpty()){
            PROGRESS = 0;
            return;
        }

        if(!OUTPUT.insertItem(0, recipe.get().value().result(), true).isEmpty()){
            return;
        }

        PROGRESS += HEAT.extractHeat(0, 1, false);
        if(PROGRESS >= recipe.get().value().heat()){
            INPUT.extractItem(0, recipe.get().value().inputItemCount(), false);
            OUTPUT.insertItem(0, recipe.get().value().result().copy(), false);
            PROGRESS = 0;
        }
    }
    private Optional<RecipeHolder<OvenRecipe>> getCurrentRecipe() {
        return this.level.getRecipeManager()
                .getRecipeFor(ModRecipes.OVEN_TYPE.get(), new OvenRecipeInput(this.INPUT.getStackInSlot(0)), level);
    }

    //Saving and loading
    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.put("input", INPUT.serializeNBT(pRegistries));
        pTag.put("output", OUTPUT.serializeNBT(pRegistries));
        pTag.put("heat", HEAT.serializeNBT(pRegistries));
        pTag.putInt("progress", PROGRESS);

        super.saveAdditional(pTag, pRegistries);
    }
    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        INPUT.deserializeNBT(pRegistries, pTag.getCompound("input"));
        OUTPUT.deserializeNBT(pRegistries, pTag.getCompound("output"));
        HEAT.deserializeNBT(pRegistries, pTag.getCompound("heat"));
        PROGRESS = pTag.getInt("progress");
    }
}
