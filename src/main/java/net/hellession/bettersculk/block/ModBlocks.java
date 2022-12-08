package net.hellession.bettersculk.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.hellession.bettersculk.BetterSculk;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block PINK_SCULK = registerBlock("pink_sculk",
            new CustomSculkBlock(FabricBlockSettings.of(Material.SCULK).sounds(BlockSoundGroup.SCULK).strength(1.4f, 0.7f), 4, false),
            ItemGroups.NATURAL, Items.SCULK);

    public static final Block GREEN_SCULK = registerBlock("green_sculk",
            new CustomSculkBlock(FabricBlockSettings.of(Material.SCULK).sounds(BlockSoundGroup.SCULK).strength(1f, 0.4f), 2, true),
            ItemGroups.NATURAL, Items.SCULK);

    private static Block registerBlock(String name, Block block, ItemGroup group, Item placeAfter)
    {
        Item item = registerBlockItem(name, block, group);
        //not sure I like this. I don't think the behavior is predictable for these static final constructors.
        ItemGroupEvents.modifyEntriesEvent(group).register(entries -> entries.addAfter(placeAfter, item));
        //I don't think I should add a block into an item group, right?.. forgot how this works lol
        return Registry.register(Registries.BLOCK, new Identifier(BetterSculk.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup group)
    {
        return Registry.register(Registries.ITEM, new Identifier(BetterSculk.MOD_ID, name), new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModBlocks()
    {
        BetterSculk.LOGGER.info("Registering ModBlocks for " + BetterSculk.MOD_ID);
    }
}
