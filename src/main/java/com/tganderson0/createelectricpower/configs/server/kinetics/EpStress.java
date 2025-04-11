package com.tganderson0.createelectricpower.configs.server.kinetics;

import com.tganderson0.createelectricpower.CreateElectricPower;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.createmod.catnip.config.ConfigBase;
import net.createmod.catnip.registry.RegisteredObjectsHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleSupplier;

import static com.tganderson0.createelectricpower.CreateElectricPower.MODID;

public class EpStress extends ConfigBase {
    private static final Object2DoubleMap<ResourceLocation> DEFAULT_IMPACTS = new Object2DoubleOpenHashMap<>();
    private static final Object2DoubleMap<ResourceLocation> DEFAULT_CAPACITIES = new Object2DoubleOpenHashMap<>();

    protected final Map<ResourceLocation, ModConfigSpec.ConfigValue<Double>> capacities = new HashMap<>();
    protected final Map<ResourceLocation, ModConfigSpec.ConfigValue<Double>> impacts = new HashMap<>();

    @Override
    public void registerAll(ModConfigSpec.Builder builder) {
        builder.comment(".", EpStress.Comments.su, EpStress.Comments.impact).push("impact");
        DEFAULT_IMPACTS.forEach((id, value) -> this.impacts.put(id, builder.define(id.getPath(), value)));
        builder.pop();

        builder.comment(".", EpStress.Comments.su, EpStress.Comments.capacity).push("capacity");
        DEFAULT_CAPACITIES.forEach((id, value) -> this.capacities.put(id, builder.define(id.getPath(), value)));
        builder.pop();
    }

    @Override
    public @NotNull String getName() {
        return "stressValues";
    }

    @Nullable
    public DoubleSupplier getImpact(Block block) {
        ModConfigSpec.ConfigValue<Double> value = this.impacts.get(RegisteredObjectsHelper.getKeyOrThrow(block));
        return value == null ? null : value::get;
    }

    @Nullable
    public DoubleSupplier getCapacity(Block block) {
        ModConfigSpec.ConfigValue<Double> value = this.capacities.get(RegisteredObjectsHelper.getKeyOrThrow(block));
        return value == null ? null : value::get;
    }


    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> setNoImpact() {
        return setImpact(0);
    }

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> setImpact(double value) {
        return builder -> {
            assertFromCreateD2D(builder);
            DEFAULT_IMPACTS.put(CreateElectricPower.loc(builder.getName()), value);
            return builder;
        };
    }

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> setCapacity(double value) {
        return builder -> {
            assertFromCreateD2D(builder);
            DEFAULT_CAPACITIES.put(CreateElectricPower.loc(builder.getName()), value);
            return builder;
        };
    }

    private static void assertFromCreateD2D(BlockBuilder<?, ?> builder) {
        if (!builder.getOwner().getModid().equals(MODID)) {
            throw new IllegalStateException("Non-Create: Electric Power blocks cannot be added to config.");
        }
    }

    private static class Comments {
        static String su = "[in Stress Units]";
        static String impact = "Configure the individual stress impact of mechanical blocks. Note that this cost is doubled for every speed increase it receives.";
        static String capacity = "Configure how much stress a source can accommodate for.";
    }
}
