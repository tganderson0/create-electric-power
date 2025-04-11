package com.tganderson0.createelectricpower;

import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.tganderson0.createelectricpower.register.EpBETypes;
import com.tganderson0.createelectricpower.register.EpBlocks;
import com.tganderson0.createelectricpower.register.EpCreativeTabs;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.fml.ModLoadingContext;
import org.slf4j.Logger;
import com.tterrag.registrate.util.entry.RegistryEntry;

import com.mojang.logging.LogUtils;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

import javax.annotation.Nullable;

@Mod(CreateElectricPower.MODID)
public class CreateElectricPower
{

    public static final String MODID = "createelectricpower";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final CreateRegistrate REG = CreateRegistrate.create(MODID)
            .defaultCreativeTab((ResourceKey<CreativeModeTab>) null);

    static {
        REG.setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                .andThen(TooltipModifier.mapNull(create(item))));
    }

    public CreateElectricPower(IEventBus modEventBus, ModContainer modContainer)
    {
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        REG.registerEventListeners(modEventBus);
        EpCreativeTabs.register(modEventBus);
        EpBlocks.register();
        EpBETypes.register();

        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(CreateElectricPower::init);
        modEventBus.addListener(EventPriority.LOWEST, EpDatagen::gatherData);
    }

    public static void init(final FMLCommonSetupEvent event) {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(EpCreativeTabs.TAB.getKey())) {
            for (RegistryEntry<Item, Item> entry : REG.getAll(Registries.ITEM)) {
                Item item = entry.get();
                if (item instanceof BlockItem) continue;
                if (item instanceof BucketItem) continue;
                event.accept(item);
            }
            for (RegistryEntry<Block, Block> entry : REG.getAll(Registries.BLOCK)) {
                var block = entry.get();
                if (block.asItem() == Items.AIR) continue;
                event.accept(block);
            }
        }
    }

    public static ResourceLocation loc(String loc) {
        return ResourceLocation.fromNamespaceAndPath(MODID, loc);
    }

    public static ResourceLocation asResource(String loc) {
        return ResourceLocation.fromNamespaceAndPath(MODID, loc);
    }

    public static ResourceLocation emptyLoc() {
        return ResourceLocation.fromNamespaceAndPath(MODID, "empty");
    }

    @Nullable
    public static KineticStats create(Item item) {
        if (item instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            if (block instanceof IRotate) return new KineticStats(block);
        }
        return null;
    }
}
