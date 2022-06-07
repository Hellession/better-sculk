package net.hellession.bettersculk.mixin;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.hellession.bettersculk.BetterSculk;
import net.hellession.bettersculk.util.IBetterCursor;
import net.minecraft.block.entity.SculkSpreadManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.Set;

@Mixin(SculkSpreadManager.Cursor.class)
public abstract class BetterCursor implements IBetterCursor {
    @Accessor("charge") public abstract int getCharge();

    private int mobMaxExperience;

    @Inject(method = "<init>(Lnet/minecraft/util/math/BlockPos;I)V", at = @At("TAIL"))
    public void init(BlockPos pos, int charge, CallbackInfo ci)
    {

        BetterSculk.LOGGER.info("init"); this.mobMaxExperience = charge;
    }

    @Inject(method = "merge", at = @At("TAIL"))
    private void merge(SculkSpreadManager.Cursor cursor, CallbackInfo info)
    {
        //this feels so aids. I hope it works
        this.mobMaxExperience = Math.max(this.mobMaxExperience, ((IBetterCursor)cursor).getMobMaxExperience());
    }

    @Accessor("CODEC")
    @Mutable
    private static void setCODEC(Codec<IBetterCursor> codec)
    {
        throw new AssertionError();
    }
    @Accessor("DIRECTION_SET_CODEC")
    @Mutable
    private static void setDIRECTION_SET_CODEC(Codec<Set<Direction>> codec)
    {
        throw new AssertionError();
    }
    @Accessor("DIRECTION_SET_CODEC")
    private static Codec<Set<Direction>> getDIRECTION_SET_CODEC()
    {
        throw new AssertionError();
    }

    @Accessor("update")
    public abstract int getUpdate();
    @Accessor("faces")
    public abstract Set<Direction> getFaces();
    @Accessor("pos")
    public abstract BlockPos getPos();
    @Accessor("decay")
    public abstract int getDecay();
    @Accessor("update")
    public abstract void setUpdate(int val);
    @Accessor("faces")
    public abstract void setFaces(Set<Direction> faces);
    @Accessor("decay")
    public abstract void setDecay(int decay);


    @Inject(method = "<clinit>", at = @At("HEAD"), cancellable = true)
    private static void staticInit(CallbackInfo info)
    {
        BetterSculk.LOGGER.info("staticInit");
        setDIRECTION_SET_CODEC(Direction.CODEC.listOf().xmap((directions) -> {
            return Sets.newEnumSet(directions, Direction.class);
        }, Lists::newArrayList));
        setCODEC(RecordCodecBuilder.create((instance) -> instance.group(BlockPos.CODEC.fieldOf("pos").forGetter(IBetterCursor::getPos),
                Codec.intRange(0, 1000).fieldOf("charge").orElse(0).forGetter(IBetterCursor::getCharge),
                Codec.intRange(0, 1).fieldOf("decay_delay").orElse(1).forGetter(IBetterCursor::getDecay),
                Codec.intRange(0, Integer.MAX_VALUE).fieldOf("update_delay").orElse(0).forGetter((cursor) -> cursor.getUpdate()), getDIRECTION_SET_CODEC().optionalFieldOf("facings").forGetter((cursor) -> Optional.ofNullable((cursor).getFaces())),
                Codec.intRange(0, 1000).fieldOf("mobMaxExperience").orElse(0).forGetter(IBetterCursor::getMobMaxExperience)).apply(instance,
                IBetterCursor::createCursor)));
        info.cancel();
    }

    /*
    @Invoker("init")
    IBetterCursor bigConstructor(BlockPos pos, int charge, int decay, int update, Optional<Set<Direction>> faces)
    {

    }*/


    @Override
    public int getMobMaxExperience() {
        return mobMaxExperience;
    }

    @Override
    public void setMobMaxExperience(int value) {
        mobMaxExperience = value;
    }
}
