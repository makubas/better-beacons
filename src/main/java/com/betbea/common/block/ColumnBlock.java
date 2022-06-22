package com.betbea.common.block;

import com.betbea.util.ColumnMaterial;
import com.betbea.util.ColumnPart;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ColumnBlock extends Block {
    public static final EnumProperty<ColumnPart> COLUMN_PART = EnumProperty.of("column_part", ColumnPart.class);
    private final ColumnMaterial material;

    public ColumnBlock(ColumnMaterial material) {
        super(FabricBlockSettings.of(Material.STONE).hardness(5f));
        this.material = material;
        setDefaultState(getStateManager().getDefaultState().with(COLUMN_PART, ColumnPart.BOTTOM));
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        updateColumnPart(world, pos, state);
    }


    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        updateColumnPart(world, pos, state);
    }

    private void updateColumnPart(World world, BlockPos pos, BlockState state) {
        boolean isDown = false;
        Block downBlock = world.getBlockState(pos.down()).getBlock();
        if (downBlock instanceof ColumnBlock) {
            if (((ColumnBlock) downBlock).getMaterial() == getMaterial()) {
                isDown = true;
            }
        }
        boolean isUp = false;
        Block upBlock = world.getBlockState(pos.up()).getBlock();
        if (upBlock instanceof ColumnBlock) {
            if (((ColumnBlock) upBlock).getMaterial() == getMaterial()) {
                isUp = true;
            }
        }

        if (isDown && isUp) {
            world.setBlockState(pos, state.with(COLUMN_PART, ColumnPart.MIDDLE));
        } else if (isDown) {
            world.setBlockState(pos, state.with(COLUMN_PART, ColumnPart.TOP));
        } else if (isUp) {
            world.setBlockState(pos, state.with(COLUMN_PART, ColumnPart.BOTTOM));
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(COLUMN_PART);
        super.appendProperties(builder);
    }

    public ColumnMaterial getMaterial() {
        return material;
    }

    public String getId() {
        return material.idPrefix + "_column";
    }
}
