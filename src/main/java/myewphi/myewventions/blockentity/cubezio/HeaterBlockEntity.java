package myewphi.myewventions.blockentity.cubezio;

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


public class HeaterBlockEntity extends AbstractCubezBlockEntity {
    public ItemStackHandler FUEL = new ItemStackHandler(1) {
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
    public HeatHandler HEAT = new HeatHandler() {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };
    private int burnTime = 0;

    public HeaterBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.HEATER_BE.get(), pos, blockState);
    }

    @Override
    public void dropAllContents(Level level, BlockPos pos) {
        dropContents(level, pos, FUEL);
    }

    @Override
    public IItemHandler getItemHandler(@Nullable Direction side) {
        if(side == null){
            return null;
        }
        assert level != null;
        BlockState state = level.getBlockState(worldPosition);
        if (side.equals(state.getValue(OvenBlock.FACING))) {
            return FUEL;
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
        if(burnTime > 0){
            burnTime -= 1;
            if(burnTime <= 0){
                HEAT.setHeat(0);
            }
        }
        else {
            Optional<RecipeHolder<HeaterRecipe>> recipe = getCurrentRecipe();

            if(!recipe.isEmpty()) {
                burnTime = recipe.get().value().burnTime();
                HEAT.setHeat(recipe.get().value().output());
                FUEL.extractItem(0, 1, false);
            }
        }
    }
    private Optional<RecipeHolder<HeaterRecipe>> getCurrentRecipe() {
        return this.level.getRecipeManager()
                .getRecipeFor(ModRecipes.HEATER_TYPE.get(), new HeaterRecipeInput(this.FUEL.getStackInSlot(0)), level);
    }

    //Saving and loading
    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.put("fuel", FUEL.serializeNBT(pRegistries));
        pTag.put("heat", HEAT.serializeNBT(pRegistries));

        super.saveAdditional(pTag, pRegistries);
    }
    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);

        FUEL.deserializeNBT(pRegistries, pTag.getCompound("fuel"));
        HEAT.deserializeNBT(pRegistries, pTag.getCompound("heat"));
    }
}
