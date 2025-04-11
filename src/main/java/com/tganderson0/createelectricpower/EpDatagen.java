package com.tganderson0.createelectricpower;

import com.tterrag.registrate.providers.RegistrateDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

import static com.tganderson0.createelectricpower.CreateElectricPower.MODID;
import static com.tganderson0.createelectricpower.CreateElectricPower.REG;

public class EpDatagen {
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        event.getGenerator().addProvider(true, REG.setDataProvider(new RegistrateDataProvider(REG, MODID, event)));
    }
}
