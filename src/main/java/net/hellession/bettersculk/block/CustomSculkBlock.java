package net.hellession.bettersculk.block;

import net.hellession.bettersculk.util.IBetterCursor;
import net.minecraft.block.*;
import net.minecraft.block.entity.SculkSpreadManager;
import net.minecraft.fluid.Fluids;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;

public class CustomSculkBlock
        extends ExperienceDroppingBlock
        implements SculkSpreadable {
    /*
    Normally, I would inherit from SculkBlock directly from Minecraft. The issue, however,
    is that I would then want to call the parent class' constructor (otherwise some shit might go bad. Idk).
    But SculkBlock is set to give a constant value with no customization! Calling SculkBlock's constructor would reset the XP yield to 1.
    I don't know of a better way to do this - maybe I am too much of a noob at Java
     */

    private boolean upgrade;

    public CustomSculkBlock(AbstractBlock.Settings settings, int experience, boolean upgrade) {
        super(settings, ConstantIntProvider.create(experience));
        this.upgrade = upgrade;
    }

    @Override
    public int spread(SculkSpreadManager.Cursor cursor, WorldAccess world, BlockPos catalystPos, Random random, SculkSpreadManager spreadManager, boolean shouldConvertToBlock) {
        int i = cursor.getCharge();
        if(upgrade) {
            int mobExperience = ((IBetterCursor) cursor).getMobMaxExperience();
            if (i > 1 && mobExperience >= 20) {
                BlockPos position = cursor.getPos();
                BlockState blockState2 = ModBlocks.PINK_SCULK.getDefaultState();
                world.setBlockState(position, blockState2, Block.NOTIFY_ALL);
                world.playSound(null, position, SoundEvents.BLOCK_SCULK_SPREAD, SoundCategory.BLOCKS, 1.0f, 1.0f);

                return Math.max(0, i - 2);
            }
        }
        if (i == 0 || random.nextInt(spreadManager.getSpreadChance()) != 0) {
            return i;
        }
        BlockPos blockPos = cursor.getPos();
        boolean bl = blockPos.isWithinDistance(catalystPos, (double)spreadManager.getMaxDistance());
        if (bl || !CustomSculkBlock.shouldNotDecay(world, blockPos)) {
            if (random.nextInt(spreadManager.getDecayChance()) != 0) {
                return i;
            }
            return i - (bl ? 1 : CustomSculkBlock.getDecay(spreadManager, blockPos, catalystPos, i));
        }
        int j = spreadManager.getExtraBlockChance();
        if (random.nextInt(j) < i) {
            BlockPos blockPos2 = blockPos.up();
            BlockState blockState = this.getExtraBlockState(world, blockPos2, random, spreadManager.isWorldGen());
            world.setBlockState(blockPos2, blockState, Block.NOTIFY_ALL);
            world.playSound(null, blockPos, blockState.getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS, 1.0f, 1.0f);
        }
        return Math.max(0, i - j);
    }

    private static int getDecay(SculkSpreadManager spreadManager, BlockPos cursorPos, BlockPos catalystPos, int charge) {
        int i = spreadManager.getMaxDistance();
        float f = MathHelper.square((float)Math.sqrt(cursorPos.getSquaredDistance(catalystPos)) - (float)i);
        int j = MathHelper.square(24 - i);
        float g = Math.min(1.0f, f / (float)j);
        return Math.max(1, (int)((float)charge * g * 0.5f));
    }

    private BlockState getExtraBlockState(WorldAccess world, BlockPos pos, Random random, boolean allowShrieker) {
        BlockState blockState = random.nextInt(11) == 0 ? (BlockState)Blocks.SCULK_SHRIEKER.getDefaultState().with(SculkShriekerBlock.CAN_SUMMON, allowShrieker) : Blocks.SCULK_SENSOR.getDefaultState();
        if (blockState.contains(Properties.WATERLOGGED) && !world.getFluidState(pos).isEmpty()) {
            return (BlockState)blockState.with(Properties.WATERLOGGED, true);
        }
        return blockState;
    }

    private static boolean shouldNotDecay(WorldAccess world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos.up());
        if (!(blockState.isAir() || blockState.isOf(Blocks.WATER) && blockState.getFluidState().isOf(Fluids.WATER))) {
            return false;
        }
        int i = 0;
        for (BlockPos blockPos : BlockPos.iterate(pos.add(-4, 0, -4), pos.add(4, 2, 4))) {
            BlockState blockState2 = world.getBlockState(blockPos);
            if (blockState2.isOf(Blocks.SCULK_SENSOR) || blockState2.isOf(Blocks.SCULK_SHRIEKER)) {
                ++i;
            }
            if (i <= 2) continue;
            return false;
        }
        return true;
    }

    @Override
    public boolean shouldConvertToSpreadable() {
        return false;
    }
}
