package net.hellession.bettersculk.mixin;

import net.hellession.bettersculk.block.ModBlocks;
import net.hellession.bettersculk.util.IBetterCursor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SculkBlock;
import net.minecraft.block.entity.SculkSpreadManager;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SculkBlock.class)
public class UpdatedSculkBlock {

    /*
    this mixin is responsible for spreading the new types of sculk.
    new types of sculk can only grow OUT of existing sculk blocks.
    because a sculk block is already worth 1 XP, it only takes 1 charge to turn it into green sculk,
    but only if the mob that died is worth at least 10 XP.
    pink sculk can only generate out of green sculk - no mixin required for that, I will need CustomSculkBlock for that.
    */
    @Inject(method = "spread", at = @At("HEAD"), cancellable = true)
    public void spread(SculkSpreadManager.Cursor cursor, WorldAccess world,
                       BlockPos catalystPos, Random random, SculkSpreadManager spreadManager, boolean shouldConvertToBlock,
                       CallbackInfoReturnable info)
    {
        int charge = cursor.getCharge();
        int mobExperience = ((IBetterCursor) cursor).getMobMaxExperience();
        //this appeared to freeze some logic for some reason. I think using 'return' without willing to interrupt the actual method is bad choice.
        //if(charge==0 || mobExperience<10)
        //    return;
        if(charge>0 && mobExperience>=10) {
            BlockPos position = cursor.getPos();
            BlockState blockState2 = ModBlocks.GREEN_SCULK.getDefaultState();
            world.setBlockState(position, blockState2, Block.NOTIFY_ALL);
            world.playSound(null, position, SoundEvents.BLOCK_SCULK_SPREAD, SoundCategory.BLOCKS, 1.0f, 1.0f);
            info.setReturnValue(Integer.valueOf(charge - 1));
        }
    }
}
