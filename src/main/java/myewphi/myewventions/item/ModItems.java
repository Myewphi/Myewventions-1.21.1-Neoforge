package myewphi.myewventions.item;

import myewphi.myewventions.Myewventions;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;


public class ModItems {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(Myewventions.MOD_ID);

    public static final DeferredItem<Item> SILVER_INGOT = ITEMS.register("silver_ingot",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> HUNGRY_FLESH_SWORD = ITEMS.register("hungry_flesh_sword",
            () -> new HungryFleshSwordItem(new Item.Properties()));
    public static final DeferredItem<Item> FOSSIL_PLACER = ITEMS.register("fossil_placer",
            () -> new FossilPlacerItem(new Item.Properties()));
    public static final DeferredItem<Item> FOSSIL_FORK = ITEMS.register("fossil_fork",
            () -> new FossilForkItem(new Item.Properties()));
    public static final DeferredItem<Item> CHISEL = ITEMS.register("chisel",
            () -> new ChiselItem(new Item.Properties()));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
