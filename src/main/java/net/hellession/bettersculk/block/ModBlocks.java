package net.hellession.bettersculk.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.hellession.bettersculk.BetterSculk;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.OreBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {

    public static final Block GREEN_SCULK = registerBlock("green_sculk",
            new CustomSculkBlock(FabricBlockSettings.of(Material.SCULK).sounds(BlockSoundGroup.SCULK).strength(1f, 0.4f), 2, true),
            ItemGroup.MISC);
    public static final Block PINK_SCULK = registerBlock("pink_sculk",
            new CustomSculkBlock(FabricBlockSettings.of(Material.SCULK).sounds(BlockSoundGroup.SCULK).strength(1.4f, 0.7f), 4, false),
            ItemGroup.MISC);

    private static Block registerBlock(String name, Block block, ItemGroup group)
    {
        registerBlockItem(name, block, group);
        return Registry.register(Registry.BLOCK, new Identifier(BetterSculk.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup group)
    {
        return Registry.register(Registry.ITEM, new Identifier(BetterSculk.MOD_ID, name), new BlockItem(block, new FabricItemSettings().group(group)));
    }

    public static void registerModBlocks()
    {
        BetterSculk.LOGGER.info("Registering ModBlocks for " + BetterSculk.MOD_ID);
    }
}
