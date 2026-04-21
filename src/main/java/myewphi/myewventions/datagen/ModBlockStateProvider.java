package myewphi.myewventions.datagen;

import myewphi.myewventions.Myewventions;
import myewphi.myewventions.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

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
    }

    private void blockWithItem(DeferredBlock<?> deferredBlock){
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }
}
