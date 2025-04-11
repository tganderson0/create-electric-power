package com.tganderson0.createelectricpower.content.electricmotor;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.motor.KineticScrollValueBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;

import com.simibubi.create.foundation.utility.CreateLang;
import com.tganderson0.createelectricpower.register.EpBETypes;
import com.tganderson0.createelectricpower.register.EpBlocks;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.math.VecHelper;
import net.createmod.catnip.math.AngleHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class ElectricMotorBlockEntity extends GeneratingKineticBlockEntity {

    public static final int DEFAULT_SPEED = 16;
    public static final int MAX_SPEED = 256;

    protected ScrollValueBehaviour generatedSpeed;

    protected final IEnergyStorage energyStorage;
    protected float motorSpeed;
    protected boolean firstRun = true;
    protected boolean active = true;

    public ElectricMotorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        energyStorage = new EnergyStorage((int) ElectricMotorBlock.STRESS_CAPACITY);

        setLazyTickRate(20);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        int max = MAX_SPEED;
        generatedSpeed = new KineticScrollValueBehaviour(Component.literal("Electric Motor"),
                this, new MotorValueBox());
        generatedSpeed.between(-max, max);
        generatedSpeed.value = DEFAULT_SPEED;
        generatedSpeed.withCallback(this::updateGeneratedRotation);
        behaviours.add(generatedSpeed);
    }

    @Override
    public void initialize() {
        super.initialize();
        if (!hasSource() || getGeneratedSpeed() > getTheoreticalSpeed())
            updateGeneratedRotation();
    }

    @Override
    public float getGeneratedSpeed() {
        if (!EpBlocks.ELECTRIC_MOTOR.has(getBlockState()))
            return 0;
        return convertToDirection(active ? motorSpeed : 0, getBlockState().getValue(ElectricMotorBlock.FACING));
    }

    class MotorValueBox extends ValueBoxTransform.Sided {

        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8, 8, 12.5);
        }

        @Override
        public Vec3 getLocalOffset(LevelAccessor level, BlockPos pos, BlockState state) {
            Direction facing = state.getValue(ElectricMotorBlock.FACING);
            return super.getLocalOffset(level, pos, state).add(Vec3.atLowerCornerOf(facing.getNormal())
                    .scale(-1 / 16f));
        }

        @Override
        public void rotate(LevelAccessor level, BlockPos pos, BlockState state, PoseStack ms) {
            super.rotate(level, pos, state, ms);
            Direction facing = state.getValue(ElectricMotorBlock.FACING);
            if (facing.getAxis() == Axis.Y)
                return;
            if (getSide() != Direction.UP)
                return;
            TransformStack.of(ms)
                    .rotateZDegrees(-AngleHelper.horizontalAngle(facing) + 180);
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            Direction facing = state.getValue(ElectricMotorBlock.FACING);
            if (facing.getAxis() != Axis.Y && direction == Direction.DOWN)
                return false;
            return direction.getAxis() != facing.getAxis();
        }

    }

    public static int getRequiredInput(float rpm) {
        if (rpm == 0f) {
            return 0;
        }
        return (int)(2 * Math.abs(rpm));
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, EpBETypes.ELECTRIC_MOTOR.get(), (be, context) -> {
            if (context == null) {
                return null;
            }
            return be.energyStorage;
        });
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        String spacing = " ";
        tooltip.add(Component.literal(spacing)
                .append(Component.literal("Energy Consumption").withStyle(ChatFormatting.GRAY)));
        tooltip.add(Component.literal(spacing)
                .append(Component.literal(" " + getRequiredInput(generatedSpeed.getValue()) + "fe/t ")
                        .withStyle(ChatFormatting.AQUA))
                .append(CreateLang.translateDirect("gui.goggles.at_current_speed")
                        .withStyle(ChatFormatting.DARK_GRAY)));
        return true;
    }

    public void updateGeneratedRotation(int rpm){
        motorSpeed = rpm;
        super.updateGeneratedRotation();
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
    }

    @Override
    public void tick(){
        super.tick();
        if (firstRun){
            motorSpeed = generatedSpeed.getValue();
            updateGeneratedRotation();
            firstRun = false;
        }

        assert level != null;
        if (level.isClientSide()) return;
        int requiredInput = getRequiredInput(motorSpeed);
        if(!active){
            if (energyStorage.getEnergyStored() > requiredInput * 2 && !getBlockState().getValue(ElectricMotorBlock.POWERED)) {
                active = true;
                updateGeneratedRotation();
            }
        }
        else {
            int extractedEnergy = energyStorage.extractEnergy(requiredInput, false);
            if (extractedEnergy < requiredInput || getBlockState().getValue(ElectricMotorBlock.POWERED)) {
                active = false;
                updateGeneratedRotation();
            }
        }
    }

    @Override
    public float calculateAddedStressCapacity() {
        float capacity = (float) (ElectricMotorBlock.STRESS_CAPACITY / 256f);
        this.lastCapacityProvided = capacity;
        return capacity;
    }
}
