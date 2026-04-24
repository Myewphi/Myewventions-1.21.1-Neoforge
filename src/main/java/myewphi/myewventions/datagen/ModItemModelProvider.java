package myewphi.myewventions.datagen;

import myewphi.myewventions.Myewventions;
import myewphi.myewventions.block.ModBlocks;
import myewphi.myewventions.item.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Myewventions.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.SILVER_INGOT.get());
        basicItem(BuiltInRegistries.BLOCK.getKey(ModBlocks.TOOTHY_SPIKES.get()));
        handheldItem(ModItems.HUNGRY_FLESH_SWORD.get());
    }
}
