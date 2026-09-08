package myewphi.myewventions.recipe;

import myewphi.myewventions.Myewventions;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, Myewventions.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, Myewventions.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PedestalRecipe>> PEDESTAL_SERIALIZER =
            SERIALIZERS.register("pedestal", PedestalRecipe.Serializer::new);
    public static final DeferredHolder<RecipeType<?>, RecipeType<PedestalRecipe>> PEDESTAL_TYPE =
            TYPES.register("pedestal", () -> new RecipeType<PedestalRecipe>() {
                @Override
                public String toString() {
                    return "pedestal";
                }
            });

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<HeaterRecipe>> HEATER_SERIALIZER =
            SERIALIZERS.register("heater", HeaterRecipe.Serializer::new);
    public static final DeferredHolder<RecipeType<?>, RecipeType<HeaterRecipe>> HEATER_TYPE =
            TYPES.register("heater", () -> new RecipeType<HeaterRecipe>() {
                @Override
                public String toString() {
                    return "heater";
                }
            });

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<OvenRecipe>> OVEN_SERIALIZER =
            SERIALIZERS.register("oven", OvenRecipe.Serializer::new);
    public static final DeferredHolder<RecipeType<?>, RecipeType<OvenRecipe>> OVEN_TYPE =
            TYPES.register("oven", () -> new RecipeType<OvenRecipe>() {
                @Override
                public String toString() {
                    return "oven";
                }
            });


    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}
