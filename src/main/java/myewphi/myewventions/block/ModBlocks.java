package myewphi.myewventions.block;

import myewphi.myewventions.Myewventions;
import myewphi.myewventions.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(Myewventions.MOD_ID);

    public static final DeferredBlock<Block> SILVER_BLOCK = registerBlock("silver_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));

    public static final DeferredBlock<Block> FLESH_GEODE_CORE = registerBlock("flesh_geode_core",
            () -> new FleshGeodeCoreBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_WART_BLOCK).randomTicks()));
    public static final DeferredBlock<Block> FLESH_GEODE_STEM = registerBlock("flesh_geode_stem",
            () -> new FleshGeodeStemBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_WART_BLOCK).randomTicks()));
    public static final DeferredBlock<Block> FLESH_GEODE_MEAT = registerBlock("flesh_geode_meat",
            () -> new FleshGeodeMeatBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_WART_BLOCK)));
    public static final DeferredBlock<Block> FLESH_GEODE_INNER_MEAT = registerBlock("flesh_geode_inner_meat",
            () -> new FleshGeodeInnerMeatBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_WART_BLOCK).randomTicks()));
    public static final DeferredBlock<Block> FLESH_GEODE_CRUST = registerBlock("flesh_geode_crust",
            () -> new FleshGeodeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_WART_BLOCK)));
    public static final DeferredBlock<Block> TOOTHY_SPIKES = registerBlock("toothy_spikes",
            () -> new ToothySpikesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BONE_BLOCK).noOcclusion().noCollission().offsetType(BlockBehaviour.OffsetType.XZ)));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block){
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
