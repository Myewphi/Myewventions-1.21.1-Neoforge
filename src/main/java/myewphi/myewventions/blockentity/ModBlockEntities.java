package myewphi.myewventions.blockentity;

import myewphi.myewventions.Myewventions;
import myewphi.myewventions.block.ModBlocks;
import myewphi.myewventions.blockentity.cubezio.CrucibleBlockEntity;
import myewphi.myewventions.blockentity.cubezio.HeatPipeBlockEntity;
import myewphi.myewventions.blockentity.cubezio.HeaterBlockEntity;
import myewphi.myewventions.blockentity.cubezio.ItemPipeBlockEntity;
import myewphi.myewventions.blockentity.misc.PedestalBlockEntity;
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

    public static final Supplier<BlockEntityType<CrucibleBlockEntity>> CRUCIBLE_BE =
            BLOCK_ENTITIES.register("crucible_be", () -> BlockEntityType.Builder.of(
                    CrucibleBlockEntity::new, ModBlocks.CRUCIBLE_BLOCK.get()).build(null));

    public static final Supplier<BlockEntityType<ItemPipeBlockEntity>> ITEM_PIPE_BE =
            BLOCK_ENTITIES.register("item_pipe_be", () -> BlockEntityType.Builder.of(
                    ItemPipeBlockEntity::new, ModBlocks.ITEM_PIPE.get()).build(null));

    public static final Supplier<BlockEntityType<HeatPipeBlockEntity>> HEAT_PIPE_BE =
            BLOCK_ENTITIES.register("heat_pipe_be", () -> BlockEntityType.Builder.of(
                    HeatPipeBlockEntity::new, ModBlocks.HEAT_PIPE.get()).build(null));



    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
