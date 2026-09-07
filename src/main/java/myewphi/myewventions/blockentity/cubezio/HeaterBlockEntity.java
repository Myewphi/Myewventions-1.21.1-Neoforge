package myewphi.myewventions.blockentity.cubezio;

import myewphi.myewventions.blockentity.ModBlockEntities;
import myewphi.myewventions.recipe.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;


public class HeaterBlockEntity extends AbstractCubezBlockEntity {
    private int heatBuffer = 0;

    public HeaterBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.HEATER_BE.get(), pos, blockState);

        BASE_INVENTORY_HANDLER = baseInventoryHandler(2, 1);

        UP_INVENTORY_HANDLER = sidedInventoryHandler(new int[]{0}, Direction.UP);
        DOWN_INVENTORY_HANDLER = sidedInventoryHandler(new int[]{0, 1}, Direction.DOWN);
        NORTH_INVENTORY_HANDLER = sidedInventoryHandler(new int[]{1}, Direction.NORTH);
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if(heatBuffer > 0){
            BASE_INVENTORY_HANDLER.insertHeat(0, 1, false);
        }
        else {
            Optional<RecipeHolder<HeaterRecipe>> recipe = this.level.getRecipeManager()
                    .getRecipeFor(ModRecipes.HEATER_TYPE.get(), new HeaterRecipeInput(this.BASE_INVENTORY_HANDLER.getStackInSlot(0)), level);

            if(!recipe.isEmpty()) {
                heatBuffer = recipe.get().value().output();
                BASE_INVENTORY_HANDLER.extractItem(0, 1, false);
            }
        }
        //Heat dissipates from buffer even if it has nowhere to go
        heatBuffer -= 1;
    }

    private Optional<RecipeHolder<HeaterRecipe>> getCurrentRecipe() {
        return this.level.getRecipeManager()
                .getRecipeFor(ModRecipes.HEATER_TYPE.get(), new HeaterRecipeInput(this.BASE_INVENTORY_HANDLER.getStackInSlot(0)), level);
    }
}
