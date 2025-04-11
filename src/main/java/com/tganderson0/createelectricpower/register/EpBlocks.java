package com.tganderson0.createelectricpower.register;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tganderson0.createelectricpower.CreateElectricPower;
import com.tganderson0.createelectricpower.content.electricmotor.ElectricMotorBlock;
import com.tganderson0.createelectricpower.content.electricmotor.ElectricMotorGenerator;
import com.tganderson0.createelectricpower.content.generator.GeneratorBlock;
import com.tganderson0.createelectricpower.content.generator.GeneratorGenerator;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Rarity;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.tganderson0.createelectricpower.CreateElectricPower.REG;
import static com.tterrag.registrate.providers.RegistrateRecipeProvider.has;

public class EpBlocks {

    public static final BlockEntry<ElectricMotorBlock> ELECTRIC_MOTOR = REG.block("electric_motor", ElectricMotorBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
            .blockstate(new ElectricMotorGenerator()::generate)
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 1)
                    .pattern("ELE")
                    .pattern("CIC")
                    .pattern("EIE")
                    .define('E', AllItems.IRON_SHEET)
                    .define('L', AllItems.ELECTRON_TUBE)
                    .define('C', AllItems.COPPER_SHEET)
                    .define('I', AllItems.ANDESITE_ALLOY)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, CreateElectricPower.loc("crafting/" + c.getName()))
            )
            .item()
            .properties(p -> p.rarity(Rarity.UNCOMMON))
            .transform(customItemModel())
            .register();

    public static final BlockEntry<GeneratorBlock> GENERATOR = REG.block("generator", GeneratorBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
            .blockstate(new GeneratorGenerator()::generate)
            .recipe((c, p) -> ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, c.get(), 1)
                    .pattern("ELE")
                    .pattern("CIC")
                    .pattern("EIE")
                    .define('E', AllItems.STURDY_SHEET)
                    .define('L', AllItems.ELECTRON_TUBE)
                    .define('C', AllItems.COPPER_SHEET)
                    .define('I', AllItems.ANDESITE_ALLOY)
                    .unlockedBy("has_" + c.getName(), has(c.get()))
                    .save(p, CreateElectricPower.loc("crafting/" + c.getName()))
            )
            .item()
            .properties(p -> p.rarity(Rarity.EPIC))
            .transform(customItemModel())
            .register();

    public static void register() {}
}
