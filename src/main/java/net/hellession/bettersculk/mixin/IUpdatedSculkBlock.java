package net.hellession.bettersculk.mixin;

import net.minecraft.block.entity.SculkSpreadManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;

public interface IUpdatedSculkBlock {
    boolean convertToGreenSculk(SculkSpreadManager spreadManager, WorldAccess world, BlockPos pos, Random random);
    boolean convertToPinkSculk(SculkSpreadManager spreadManager, WorldAccess world, BlockPos pos, Random random);
}
