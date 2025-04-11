package com.tganderson0.createelectricpower.register;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tganderson0.createelectricpower.content.electricmotor.ElectricMotorBlock;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.item.Rarity;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.tganderson0.createelectricpower.CreateElectricPower.REG;

public class EpBlocks {

    public static final BlockEntry<ElectricMotorBlock> ELECTRIC_MOTOR = REG.block("electric_motor", ElectricMotorBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
            .item()
            .properties(p -> p.rarity(Rarity.UNCOMMON))
            .transform(customItemModel())
            .register();

    public static void register() {}
}
