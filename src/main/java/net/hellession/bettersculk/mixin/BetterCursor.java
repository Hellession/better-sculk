package net.hellession.bettersculk.mixin;

import net.hellession.bettersculk.util.IBetterCursor;
import net.minecraft.block.entity.SculkSpreadManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.Set;

@Mixin(SculkSpreadManager.Cursor.class)
public class BetterCursor implements IBetterCursor {
    private int mobMaxExperience;

    @Inject(method = "<init>(Lnet/minecraft/util/math/BlockPos;IIILjava/util/Optional;)V", at = @At("TAIL"))
    private void init(BlockPos pos, int charge, int decay, int update, Optional<Set<Direction>> faces, CallbackInfo info)
    {
        this.mobMaxExperience = charge;
    }

    @Inject(method = "merge", at = @At("TAIL"))
    private void merge(SculkSpreadManager.Cursor cursor, CallbackInfo info)
    {
        //this feels so aids. I hope it works
        this.mobMaxExperience = Math.max(this.mobMaxExperience, ((IBetterCursor)cursor).getMobMaxExperience());
    }

    @Override
    public int getMobMaxExperience() {
        return mobMaxExperience;
    }
}
