package myewphi.myewventions.datagen;

import myewphi.myewventions.Myewventions;
import myewphi.myewventions.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jetbrains.annotations.NotNull;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Myewventions.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.SILVER_BLOCK);

        blockWithItem(ModBlocks.FLESH_GEODE_CORE);
        blockWithItem(ModBlocks.FLESH_GEODE_CRUST);
        blockWithItem(ModBlocks.FLESH_GEODE_STEM);
        blockWithItem(ModBlocks.FLESH_GEODE_MEAT);
        blockWithItem(ModBlocks.FLESH_GEODE_INNER_MEAT);
        crossBlock(ModBlocks.TOOTHY_SPIKES);

        blockWithItem(ModBlocks.ANCIENT_STONE);
        blockWithItem(ModBlocks.ANCIENT_STONE_FOSSIL);
    }

    private void blockWithItem(DeferredBlock<?> deferredBlock){
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }
    private void crossBlock(DeferredBlock<?> deferredBlock){
        simpleBlock(deferredBlock.get(), models().cross(name(deferredBlock.get()), blockTexture(deferredBlock.get())).renderType("cutout"));
    }

    private String name(Block block) {
        return key(block).getPath();
    }
    private ResourceLocation key(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }
    public @NotNull ResourceLocation blockTexture(@NotNull Block block) {
        ResourceLocation name = key(block);
        return ResourceLocation.fromNamespaceAndPath(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + name.getPath());
    }
}
