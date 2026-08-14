package myewphi.myewventions.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record PedestalRecipe(Ingredient inputItem, ItemStack output) implements Recipe<PedestalRecipeInput> {

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(inputItem);
        return list;
    }

    @Override
    public boolean matches(PedestalRecipeInput pedestalRecipeInput, Level level) {
        if (level.isClientSide()) {
            return false;
        }

        return inputItem.test(pedestalRecipeInput.getItem(0));
    }

    @Override
    public ItemStack assemble(PedestalRecipeInput pedestalRecipeInput, HolderLookup.Provider registries) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.PEDESTAL_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.PEDESTAL_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<PedestalRecipe>{
        public static final MapCodec<PedestalRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(PedestalRecipe::inputItem),
                ItemStack.CODEC.fieldOf("result").forGetter(PedestalRecipe::output)
        ).apply(inst, PedestalRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, PedestalRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, PedestalRecipe::inputItem,
                        ItemStack.STREAM_CODEC, PedestalRecipe::output,
                        PedestalRecipe::new);

        @Override
        public MapCodec<PedestalRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, PedestalRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
