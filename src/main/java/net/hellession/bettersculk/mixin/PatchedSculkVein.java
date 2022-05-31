package net.hellession.bettersculk.mixin;

import net.hellession.bettersculk.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SculkVeinBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.block.SculkVeinBlock$SculkVeinGrowChecker")
public class PatchedSculkVein {
    @Inject(method = "canGrow(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;" +
            "Lnet/minecraft/block/BlockState;)Z", at = @At("HEAD"), cancellable = true)
    public void canGrow(BlockView world, BlockPos pos, BlockPos growPos, Direction direction, BlockState state, CallbackInfoReturnable info)
    {
        BlockState blockState = world.getBlockState(growPos.offset(direction));
        if (blockState.isOf(ModBlocks.GREEN_SCULK) || blockState.isOf(ModBlocks.PINK_SCULK)) {
            info.setReturnValue(Boolean.valueOf(false));
            return;
        }
    }
}
