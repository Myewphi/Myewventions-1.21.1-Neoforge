package myewphi.myewventions.datagen;

import myewphi.myewventions.Myewventions;
import myewphi.myewventions.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Myewventions.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.PEDESTAL_BLOCK.getKey())
                .add(ModBlocks.SILVER_BLOCK.getKey())
                .add(ModBlocks.ANCIENT_STONE.getKey());
        this.tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.SILVER_BLOCK.getKey())
                .add(ModBlocks.ANCIENT_STONE.getKey())
                .add(ModBlocks.ANCIENT_STONE_FOSSIL.getKey());
    }
}
