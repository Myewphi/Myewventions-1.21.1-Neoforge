package myewphi.myewventions.worldgen;

import com.mojang.serialization.MapCodec;
import myewphi.myewventions.Myewventions;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBiomeSources {
    public static final DeferredRegister<MapCodec<? extends BiomeSource>> BIOME_SOURCES =
            DeferredRegister.create(Registries.BIOME_SOURCE, Myewventions.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends BiomeSource>, MapCodec<FixedBiomeSource>> TEST_SOURCE =
            BIOME_SOURCES.register("myewtest", key -> FixedBiomeSource.CODEC);

    public static void register(IEventBus eventBus){
        BIOME_SOURCES.register(eventBus);
    }
}
