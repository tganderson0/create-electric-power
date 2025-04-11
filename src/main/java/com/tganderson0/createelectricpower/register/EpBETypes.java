package com.tganderson0.createelectricpower.register;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.OrientedRotatingVisual;
import com.tganderson0.createelectricpower.content.electricmotor.ElectricMotorBlockEntity;
import com.tganderson0.createelectricpower.content.electricmotor.ElectricMotorRenderer;
import com.tganderson0.createelectricpower.content.generator.GeneratorBlockEntity;
import com.tganderson0.createelectricpower.content.generator.GeneratorRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.tganderson0.createelectricpower.CreateElectricPower.REG;

public class EpBETypes {
    public static final BlockEntityEntry<ElectricMotorBlockEntity> ELECTRIC_MOTOR = REG
            .blockEntity("electric_motor", ElectricMotorBlockEntity::new)
            .visual(() -> OrientedRotatingVisual.of(AllPartialModels.SHAFT_HALF), false)
            .validBlocks(EpBlocks.ELECTRIC_MOTOR)
            .renderer(() -> ElectricMotorRenderer::new)
            .register();

    public static final BlockEntityEntry<GeneratorBlockEntity> GENERATOR = REG
            .blockEntity("generator", GeneratorBlockEntity::new)
            .visual(() -> OrientedRotatingVisual.of(AllPartialModels.SHAFT_HALF), false)
            .validBlocks(EpBlocks.GENERATOR)
            .renderer(() -> GeneratorRenderer::new)
            .register();

    public static void register() {}
}
