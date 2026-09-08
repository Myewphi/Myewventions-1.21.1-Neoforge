package myewphi.myewventions;

import myewphi.myewventions.block.ModBlocks;
import myewphi.myewventions.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Myewventions.MOD_ID);

    public static final Supplier<CreativeModeTab> MYEWVENTIONS_TAB =
            CREATIVE_MODE_TAB.register("myewventions_tab", () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.SILVER_INGOT.get()))
                    .title(Component.translatable("creativetab.myewventions_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.HEATER_BLOCK);
                        output.accept(ModBlocks.OVEN_BLOCK);
                        output.accept(ModBlocks.ITEM_PIPE);
                        output.accept(ModBlocks.HEAT_PIPE);

                        output.accept(ModItems.SILVER_INGOT);
                        output.accept(ModBlocks.SILVER_BLOCK);

                        output.accept(ModBlocks.FLESH_GEODE_CORE);
                        output.accept(ModBlocks.FLESH_GEODE_CRUST);
                        output.accept(ModBlocks.FLESH_GEODE_MEAT);
                        output.accept(ModBlocks.FLESH_GEODE_INNER_MEAT);
                        output.accept(ModBlocks.FLESH_GEODE_STEM);
                        output.accept(ModBlocks.TOOTHY_SPIKES);

                        output.accept(ModBlocks.ANCIENT_STONE);
                        output.accept(ModBlocks.ANCIENT_STONE_FOSSIL);
                        output.accept(ModItems.FOSSIL_PLACER);
                        output.accept(ModItems.FOSSIL_FORK);
                        output.accept(ModItems.CHISEL);
                    }).build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
