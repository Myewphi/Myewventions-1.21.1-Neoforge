package myewphi.myewventions.blockentity;

import myewphi.myewventions.Myewventions;
import myewphi.myewventions.block.ModBlocks;
import myewphi.myewventions.blockentity.cubezio.logistics.ExtractorBlockEntity;
import myewphi.myewventions.blockentity.cubezio.logistics.FilterBlockEntity;
import myewphi.myewventions.blockentity.cubezio.logistics.MergerBlockEntity;
import myewphi.myewventions.blockentity.cubezio.logistics.SplitterBlockEntity;
import myewphi.myewventions.blockentity.cubezio.pipes.HeatPlateBlockEntity;
import myewphi.myewventions.blockentity.cubezio.pipes.ItemPipeBlockEntity;
import myewphi.myewventions.blockentity.cubezio.processors.HeaterBlockEntity;
import myewphi.myewventions.blockentity.cubezio.processors.OvenBlockEntity;
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

    public static final Supplier<BlockEntityType<OvenBlockEntity>> OVEN_BE =
            BLOCK_ENTITIES.register("oven_be", () -> BlockEntityType.Builder.of(
                    OvenBlockEntity::new, ModBlocks.OVEN_BLOCK.get()).build(null));

    public static final Supplier<BlockEntityType<ItemPipeBlockEntity>> ITEM_PIPE_BE =
            BLOCK_ENTITIES.register("item_pipe_be", () -> BlockEntityType.Builder.of(
                    ItemPipeBlockEntity::new, ModBlocks.ITEM_PIPE.get()).build(null));

    public static final Supplier<BlockEntityType<HeatPlateBlockEntity>> HEAT_PLATE_BE =
            BLOCK_ENTITIES.register("heat_plate_be", () -> BlockEntityType.Builder.of(
                    HeatPlateBlockEntity::new, ModBlocks.HEAT_PLATE.get()).build(null));

    public static final Supplier<BlockEntityType<SplitterBlockEntity>> SPLITTER_BE =
            BLOCK_ENTITIES.register("splitter_be", () -> BlockEntityType.Builder.of(
                    SplitterBlockEntity::new, ModBlocks.SPLITTER_BLOCK.get()).build(null));

    public static final Supplier<BlockEntityType<MergerBlockEntity>> MERGER_BE =
            BLOCK_ENTITIES.register("merger_be", () -> BlockEntityType.Builder.of(
                    MergerBlockEntity::new, ModBlocks.MERGER_BLOCK.get()).build(null));

    public static final Supplier<BlockEntityType<ExtractorBlockEntity>> EXTRACTOR_BE =
            BLOCK_ENTITIES.register("extractor_be", () -> BlockEntityType.Builder.of(
                    ExtractorBlockEntity::new, ModBlocks.EXTRACTOR_BLOCK.get()).build(null));

    public static final Supplier<BlockEntityType<FilterBlockEntity>> FILTER_BE =
            BLOCK_ENTITIES.register("filter_be", () -> BlockEntityType.Builder.of(
                    FilterBlockEntity::new, ModBlocks.FILTER_BLOCK.get()).build(null));



    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
