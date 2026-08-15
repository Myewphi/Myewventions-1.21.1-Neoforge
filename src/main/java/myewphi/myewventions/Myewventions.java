package myewphi.myewventions;

import myewphi.myewventions.block.blockentity.HeaterBlockEntity;
import myewphi.myewventions.block.blockentity.ModBlockEntities;
import myewphi.myewventions.block.fleshgeode.FleshGeodeMeatBlock;
import myewphi.myewventions.block.ModBlocks;
import myewphi.myewventions.item.ModCreativeModeTabs;
import myewphi.myewventions.item.ModItems;
import myewphi.myewventions.recipe.ModRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import java.util.*;

@Mod(Myewventions.MOD_ID)
public class Myewventions {
    public static final String MOD_ID = "myewventions";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final ResourceKey<DamageType> FLESH_GEODE_DAMAGE =
            ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(MOD_ID, "flesh_geode"));

    public Myewventions(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModRecipes.register(modEventBus);

        modEventBus.addListener(this::onRegisterCapabilities);

        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void onRegisterCapabilities(RegisterCapabilitiesEvent event){
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.HEATER_BE.get(),
                HeaterBlockEntity::getItemHandler);
    }

    @SubscribeEvent
    public void onConsume(LivingDeathEvent deathEvent){
        Level level = deathEvent.getEntity().level();

        if(deathEvent.getSource().typeHolder() == level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(FLESH_GEODE_DAMAGE)){
            int radius = 5;
            ArrayList<BlockPos> nearbyMeat = Myewtilities.pruneBlockPosList(
                    BlockPos.withinManhattan(deathEvent.getEntity().getOnPos(), radius, radius, radius),
                    blockPos -> level.getBlockState(blockPos) == ModBlocks.FLESH_GEODE_MEAT.get().defaultBlockState() && Myewtilities.getRandomSide(blockPos, blockPos1 -> {
                        BlockState state = level.getBlockState(blockPos1);
                        return state.isAir() || state.getBlock().equals(ModBlocks.TOOTHY_SPIKES.get());
                    }).isPresent());

            if(!nearbyMeat.isEmpty()) {
                BlockPos pos =  Myewtilities.getRandomListElement(nearbyMeat);
                ((FleshGeodeMeatBlock) level.getBlockState(pos).getBlock()).feedFleshGeodeBlock(deathEvent, pos);
            }
        }
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        /*
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
        */
    }
    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        //LOGGER.info("HELLO from server starting");
    }
    public void Examples(LivingEntity livingEntity){
        //all Minecraft.getInstance() calls are client sided!!!
        assert Minecraft.getInstance().player != null;
        Minecraft.getInstance().player.sendSystemMessage(Component.literal("This message will display in chat without a sender!"));
        //try to use entity.level() for server sidedness instead of Minecraft.getInstance().level() which is client side
        livingEntity.level().setBlock(livingEntity.getOnPos(), Blocks.TNT.defaultBlockState(), 3);
        //how to grab and set attributes!!!
        Objects.requireNonNull(livingEntity.getAttributes().getInstance(Attributes.MOVEMENT_SPEED)).setBaseValue(10);
    }

}
