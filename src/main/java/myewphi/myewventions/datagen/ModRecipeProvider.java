package myewphi.myewventions.datagen;

import myewphi.myewventions.block.ModBlocks;
import myewphi.myewventions.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        compressedBlockThreeByThree(recipeOutput, ModItems.SILVER_INGOT.get(), ModBlocks.SILVER_BLOCK.get());
    }

    private void compressedBlockThreeByThree(RecipeOutput recipeOutput, ItemLike toCompress, ItemLike compressed){
        ShapedRecipeBuilder
                .shaped(RecipeCategory.MISC, compressed)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', toCompress)
                .unlockedBy(getHasName(toCompress), has(toCompress))
                .save(recipeOutput);
        ShapelessRecipeBuilder
                .shapeless(RecipeCategory.MISC, toCompress, 9)
                .requires(compressed)
                .unlockedBy(getHasName(compressed), has(compressed))
                .save(recipeOutput);
    }
}
