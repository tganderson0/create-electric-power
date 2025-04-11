package com.tganderson0.createelectricpower.content.generator;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.utility.CreateLang;
import com.tganderson0.createelectricpower.content.electricmotor.ElectricMotorBlock;
import com.tganderson0.createelectricpower.register.EpBETypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;

public class GeneratorBlockEntity extends KineticBlockEntity implements IEnergyStorage {
    private final IEnergyStorage energyStorage;

    public static int GENERATOR_CAPACITY = 4096;

    public static int FE_PER_RPM = 2;

    private EnumSet<Direction> invalidSides = EnumSet.allOf(Direction.class);
    private final EnumMap<Direction, BlockCapabilityCache<IEnergyStorage, Direction>> cache = new EnumMap<>(Direction.class);

    public GeneratorBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        energyStorage = new EnergyStorage(GENERATOR_CAPACITY);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                EpBETypes.GENERATOR.get(),
                (be, context) -> be.energyStorage
        );
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        tooltip.add(Component.literal(" Currently producing").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal(" " + getEnergyProductionRate(getSpeed()) + "fe / t").withStyle(ChatFormatting.AQUA).append(CreateLang.translateDirect("gui.goggles.at_current_speed").withStyle(ChatFormatting.DARK_GRAY)));

        return true;
    }

    @Override
    public float calculateStressApplied() {
        float impact = (float) (ElectricMotorBlock.STRESS_CAPACITY / 256f);
        this.lastStressApplied = impact;
        return impact;
    }

    private boolean firstTick = true;

    @Override
    public void tick() {
        super.tick();
        if (level == null) return;
        if (level.isClientSide()) return;
        if (firstTick) firstTick();
        firstTick = false;

        if (Math.abs(getSpeed()) > 0) {
            energyStorage.receiveEnergy(getEnergyProductionRate(getSpeed()), false);
        }

        for (Direction d : Direction.values()) {
            IEnergyStorage ies = cache.get(d).getCapability();
            if (ies == null) continue;
            int ext = energyStorage.extractEnergy(ies.receiveEnergy(GENERATOR_CAPACITY, true), false);
            ies.receiveEnergy(ext, false);
        }
    }

    public void firstTick() {
        updateCache();
    }

    public void updateCache() {
        if (level == null) return;
        if (level.isClientSide()) return;
        for (Direction side : Direction.values()) {
            cache.put(side, BlockCapabilityCache.create(
                    Capabilities.EnergyStorage.BLOCK,
                    (ServerLevel) level,
                    getBlockPos().relative(side),
                    side.getOpposite(),
                    () -> !this.isRemoved(),
                    () -> { invalidSides.add(side); }
            ));
        }
    }

    public static int getEnergyProductionRate(float rpm) {
        return (int)((double) FE_PER_RPM * ((double)Math.abs(rpm) * 0.8));
    }

    @Override
    public int receiveEnergy(int toReceive, boolean simulate) {
        return 0;
    }

    @Override
    public int extractEnergy(int toExtract, boolean simulate) {
        return energyStorage.extractEnergy(toExtract, simulate);
    }

    @Override
    public int getEnergyStored() {
        return energyStorage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return energyStorage.getMaxEnergyStored();
    }

    @Override
    public boolean canExtract() {
        return energyStorage.canExtract();
    }

    @Override
    public boolean canReceive() {
        return false;
    }
}
