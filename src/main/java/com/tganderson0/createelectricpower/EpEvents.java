package com.tganderson0.createelectricpower;

import com.tganderson0.createelectricpower.content.electricmotor.ElectricMotorBlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class EpEvents {
    @EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents {
        @SubscribeEvent
        public static void registerCapabilities(RegisterCapabilitiesEvent event) {
            ElectricMotorBlockEntity.registerCapabilities(event);
        }
    }
}
