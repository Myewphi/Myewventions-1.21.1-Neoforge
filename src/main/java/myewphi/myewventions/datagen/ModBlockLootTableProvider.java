package myewphi.myewventions.datagen;

import myewphi.myewventions.block.ModBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {

    protected ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.SILVER_BLOCK.get());
        dropSelf(ModBlocks.FLESH_GEODE_CORE.get());
        dropSelf(ModBlocks.FLESH_GEODE_CRUST.get());
        dropSelf(ModBlocks.FLESH_GEODE_MEAT.get());
        dropSelf(ModBlocks.FLESH_GEODE_INNER_MEAT.get());
        dropSelf(ModBlocks.FLESH_GEODE_STEM.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks(){
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
