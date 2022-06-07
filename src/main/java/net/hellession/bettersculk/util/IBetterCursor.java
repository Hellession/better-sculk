package net.hellession.bettersculk.util;

import net.hellession.bettersculk.BetterSculk;
import net.hellession.bettersculk.mixin.BetterCursor;
import net.minecraft.block.entity.SculkSpreadManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;
import java.util.Set;

public interface IBetterCursor {
    int getMobMaxExperience();
    void setMobMaxExperience(int value);

    int getCharge();
    int getUpdate();
    Set<Direction> getFaces();
    BlockPos getPos();
    int getDecay();

    void setUpdate(int val);
    void setFaces(Set<Direction> faces);
    void setDecay(int decay);

    //IBetterCursor bigConstructor(BlockPos pos, int charge, int decay, int update, Optional<Set<Direction>> faces);

    static IBetterCursor createCursor(BlockPos pos, int charge, int decay, int update, Optional<Set<Direction>> faces, int mobMaxExperience)
    {
        BetterSculk.LOGGER.info("createCursor");
        IBetterCursor newObj = (IBetterCursor) new SculkSpreadManager.Cursor(pos, charge);
        newObj.setDecay(decay);
        newObj.setUpdate(update);
        if(!faces.isEmpty())
            newObj.setFaces(faces.get());
        newObj.setMobMaxExperience(mobMaxExperience);
        return newObj;
    }
}
