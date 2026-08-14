package myewphi.myewventions.block.blockentity;

import myewphi.myewventions.Myewventions;
import myewphi.myewventions.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Myewventions.MOD_ID);



    public static final Supplier<BlockEntityType<PedestalBlockEntity>> PEDESTAL_BE =
            BLOCK_ENTITIES.register("pedestal_be", () -> BlockEntityType.Builder.of(
                    PedestalBlockEntity::new, ModBlocks.PEDESTAL_BLOCK.get()).build(null));

    public static final Supplier<BlockEntityType<HeaterBlockEntity>> HEATER_BE =
            BLOCK_ENTITIES.register("heater_be", () -> BlockEntityType.Builder.of(
                    HeaterBlockEntity::new, ModBlocks.HEATER_BLOCK.get()).build(null));



    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
