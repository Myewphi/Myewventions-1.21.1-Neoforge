package myewphi.myewventions.block;

import myewphi.myewventions.Myewventions;
import myewphi.myewventions.block.cubezio.CrucibleBlock;
import myewphi.myewventions.block.cubezio.HeatPipeBlock;
import myewphi.myewventions.block.cubezio.HeaterBlock;
import myewphi.myewventions.block.cubezio.ItemPipeBlock;
import myewphi.myewventions.block.fleshgeode.*;
import myewphi.myewventions.block.misc.*;
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

    public static final DeferredBlock<Block> PEDESTAL_BLOCK = registerBlock("pedestal_block",
            () -> new PedestalBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()));
    public static final DeferredBlock<Block> HEATER_BLOCK = registerBlock("heater_block",
            () -> new HeaterBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()));
    public static final DeferredBlock<Block> CRUCIBLE_BLOCK = registerBlock("crucible_block",
            () -> new CrucibleBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion()));
    public static final DeferredBlock<Block> ITEM_PIPE = registerBlock("item_pipe",
            () -> new ItemPipeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion(), 1));
    public static final DeferredBlock<Block> HEAT_PIPE = registerBlock("heat_pipe",
            () -> new HeatPipeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noOcclusion(), 16));

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

    public static final DeferredBlock<Block> ANCIENT_STONE = registerBlock("ancient_stone",
            () -> new AncientStoneBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
    public static final DeferredBlock<Block> ANCIENT_STONE_FOSSIL = registerBlock("ancient_stone_fossil",
            () -> new AncientStoneFossilBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));

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
