package myewphi.myewventions.item;

import myewphi.myewventions.block.ModBlocks;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class ChiselItem extends TieredItem {
    private static final List<Block> chisellableBlocks = List.of(
            ModBlocks.ANCIENT_STONE.get(),
            ModBlocks.ANCIENT_STONE_FOSSIL.get()
    );

    public ChiselItem(Properties properties) {
        super(Tiers.IRON, properties.component(DataComponents.TOOL, createToolProperties()));
    }

    public static Tool createToolProperties() {
        return new Tool(List.of(Tool.Rule.minesAndDrops(chisellableBlocks, 6.0F)), 1.0F, 2);
    }
}
