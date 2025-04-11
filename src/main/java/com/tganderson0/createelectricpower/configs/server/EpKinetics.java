package com.tganderson0.createelectricpower.configs.server;

import com.tganderson0.createelectricpower.configs.server.kinetics.EpStress;
import net.createmod.catnip.config.ConfigBase;
import org.jetbrains.annotations.NotNull;

public class EpKinetics extends ConfigBase {
    public final EpStress stressValues = nested(1, EpStress::new, "Fine tune the kinetic stats of individual components");

    @Override
    public @NotNull String getName() {
        return "kinetics";
    }
}
