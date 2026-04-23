package myewphi.myewventions.item;

import myewphi.myewventions.Myewventions;
import myewphi.myewventions.block.ModBlocks;
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
                        output.accept(ModItems.SILVER_INGOT);
                        output.accept(ModBlocks.SILVER_BLOCK);
                        output.accept(ModBlocks.FLESH_GEODE_CORE);
                        output.accept(ModBlocks.FLESH_GEODE_CRUST);
                        output.accept(ModBlocks.FLESH_GEODE_MEAT);
                        output.accept(ModBlocks.FLESH_GEODE_STEM);
                    }).build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
