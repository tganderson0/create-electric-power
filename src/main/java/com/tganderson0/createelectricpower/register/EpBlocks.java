package com.tganderson0.createelectricpower.register;

import com.simibubi.create.AllTags;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tganderson0.createelectricpower.configs.server.kinetics.EpStress;
import com.tganderson0.createelectricpower.content.electricmotor.ElectricMotorBlock;
import com.tganderson0.createelectricpower.content.electricmotor.ElectricMotorGenerator;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.material.MapColor;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;
import static com.tganderson0.createelectricpower.CreateElectricPower.REG;

public class EpBlocks {

    public static final BlockEntry<ElectricMotorBlock> ELECTRIC_MOTOR = REG.block("electric_motor", ElectricMotorBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.noOcclusion().mapColor(MapColor.TERRACOTTA_CYAN))
            .transform(pickaxeOnly())
            .tag(AllTags.AllBlockTags.BRITTLE.tag)
            .blockstate(new ElectricMotorGenerator()::generate)
            .transform(EpStress.setCapacity(8192.0))
            .onRegister(BlockStressValues.setGeneratorSpeed(256, true))
            .item()
            .properties(p -> p.rarity(Rarity.UNCOMMON))
            .transform(customItemModel())
            .register();

    public static void register() {}
}
