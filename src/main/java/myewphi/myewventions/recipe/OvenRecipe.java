package myewphi.myewventions.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record OvenRecipe(Ingredient inputItem, int inputItemCount, int heat, ItemStack result) implements Recipe<OvenRecipeInput> {
    @Override
    public boolean matches(OvenRecipeInput input, Level level) {
        if (level.isClientSide()) {
            return false;
        }

        boolean correctItem = inputItem.test(input.getItem(0));

        return input.size() >= inputItemCount && correctItem;
    }

    @Override
    public ItemStack assemble(OvenRecipeInput input, HolderLookup.Provider registries) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return this.result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.OVEN_SERIALIZER.get();
    }
    @Override
    public RecipeType<?> getType() {
        return ModRecipes.OVEN_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<OvenRecipe>{
        public static final MapCodec<OvenRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(OvenRecipe::inputItem),
                Codec.INT.fieldOf("count").forGetter(OvenRecipe::inputItemCount),
                Codec.INT.fieldOf("heat").forGetter(OvenRecipe::heat),
                ItemStack.CODEC.fieldOf("result").forGetter(OvenRecipe::result)
        ).apply(inst, OvenRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, OvenRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, OvenRecipe::inputItem,
                        ByteBufCodecs.INT, OvenRecipe::inputItemCount,
                        ByteBufCodecs.INT, OvenRecipe::heat,
                        ItemStack.STREAM_CODEC, OvenRecipe::result,
                        OvenRecipe::new);

        @Override
        public MapCodec<OvenRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, OvenRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
