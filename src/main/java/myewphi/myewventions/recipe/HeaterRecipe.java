package myewphi.myewventions.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record HeaterRecipe(Ingredient inputItem, int output) implements Recipe<HeaterRecipeInput> {
    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(inputItem);
        return list;
    }

    @Override
    public boolean matches(HeaterRecipeInput input, Level level) {
        if (level.isClientSide()) {
            return false;
        }

        return inputItem.test(input.getItem(0));
    }

    @Override
    public ItemStack assemble(HeaterRecipeInput input, HolderLookup.Provider registries) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return null;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.HEATER_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.HEATER_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<HeaterRecipe>{
        public static final MapCodec<HeaterRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(HeaterRecipe::inputItem),
                Codec.INT.fieldOf("heat").forGetter(HeaterRecipe::output)
        ).apply(inst, HeaterRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, HeaterRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, HeaterRecipe::inputItem,
                        ByteBufCodecs.INT, HeaterRecipe::output,
                        HeaterRecipe::new);

        @Override
        public MapCodec<HeaterRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, HeaterRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
