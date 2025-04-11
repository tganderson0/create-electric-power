package com.tganderson0.createelectricpower.register;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllCreativeModeTabs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.tganderson0.createelectricpower.CreateElectricPower.MODID;

public class EpCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> REG = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = REG.register("base", () -> CreativeModeTab.builder()
            .title(Component.literal("Electric Motors"))
            .withTabsBefore(AllCreativeModeTabs.PALETTES_CREATIVE_TAB.getKey())
            .icon(AllBlocks.CREATIVE_MOTOR::asStack)
            .build()
    );

    public static void register(IEventBus modEventBus) {
        REG.register(modEventBus);
    }
}
