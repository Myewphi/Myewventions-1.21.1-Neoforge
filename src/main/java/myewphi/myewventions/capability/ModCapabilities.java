package myewphi.myewventions.capability;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;

public class ModCapabilities {
    public static final class HeatHandler{
        public static final BlockCapability<IHeatHandler, @Nullable Direction> BLOCK = BlockCapability.createSided(create("heat_handler"), IHeatHandler.class);

        private HeatHandler(){}
    }


    private static ResourceLocation create(String path) {
        return ResourceLocation.fromNamespaceAndPath("neoforge", path);
    }
}
