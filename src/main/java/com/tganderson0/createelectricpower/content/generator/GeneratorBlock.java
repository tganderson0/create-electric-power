package com.tganderson0.createelectricpower.content.generator;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.foundation.block.IBE;
import com.tganderson0.createelectricpower.content.electricmotor.ElectricMotorBlockEntity;
import com.tganderson0.createelectricpower.register.EpBETypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class GeneratorBlock extends DirectionalKineticBlock implements IBE<GeneratorBlockEntity>, IRotate {
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AllShapes.MOTOR_BLOCK.get(state.getValue(FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction preferred = getPreferredFacing(context);
        if ((context.getPlayer() != null && context.getPlayer()
                .isShiftKeyDown()) || preferred == null)
            return super.getStateForPlacement(context);
        return defaultBlockState().setValue(FACING, preferred);
    }

    @Override
    public boolean hideStressImpact() {
        return false;
    }

    public GeneratorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(FACING);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING)
                .getAxis();
    }

    @Override
    protected boolean isPathfindable(@NotNull BlockState state, @NotNull PathComputationType pathComputationType) {
        return false;
    }

    @Override
    public Class<GeneratorBlockEntity> getBlockEntityClass() {
        return GeneratorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends GeneratorBlockEntity> getBlockEntityType() {
        return EpBETypes.GENERATOR.get();
    }

    @Override
    public void neighborChanged(BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull Block blockIn, @NotNull BlockPos fromPos, boolean isMoving) {
        BlockEntity tileentity = state.hasBlockEntity() ? worldIn.getBlockEntity(pos) : null;
        if(tileentity != null) {
            if(tileentity instanceof GeneratorBlockEntity) {
                ((GeneratorBlockEntity)tileentity).updateCache();
            }
        }
    }

}
