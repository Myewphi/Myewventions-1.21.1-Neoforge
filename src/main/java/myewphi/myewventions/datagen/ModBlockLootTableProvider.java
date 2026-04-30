package myewphi.myewventions.datagen;

import myewphi.myewventions.block.ModBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {

    private static final List<Block> unknownBlocks = List.of(
            ModBlocks.ANCIENT_STONE_FOSSIL.get()
    );

    protected ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.SILVER_BLOCK.get());

        dropSelf(ModBlocks.FLESH_GEODE_CORE.get());
        dropSelf(ModBlocks.FLESH_GEODE_CRUST.get());
        dropSelf(ModBlocks.FLESH_GEODE_MEAT.get());
        dropOther(ModBlocks.FLESH_GEODE_INNER_MEAT.get(), ModBlocks.FLESH_GEODE_MEAT.get());
        dropSelf(ModBlocks.FLESH_GEODE_STEM.get());
        dropSelf(ModBlocks.TOOTHY_SPIKES.get());

        dropSelf(ModBlocks.ANCIENT_STONE.get());
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks(){
        Iterable<Block> allBlocks = ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
        ArrayList<Block> knownBlocks = new ArrayList<>();
        allBlocks.forEach(block -> {
            if(!unknownBlocks.contains(block)){
                knownBlocks.add(block);
            }
        });
        return knownBlocks;
    }
}
