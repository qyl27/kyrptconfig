package net.kyrptonaught.kyrptconfig;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class TagHelper {

    public static List<Block> getBlocksInTag(Identifier blockTagKey) {
        Tag<Block> tags = BlockTags.getTagGroup().getTag(blockTagKey);
        if (tags != null)
            return tags.values();

        return new ArrayList<>();
    }

    public static List<Item> getItemsInTag(Identifier blockTagKey) {

        Tag<Item> tags = ItemTags.getTagGroup().getTag(blockTagKey);
        if (tags != null)
            return tags.values();

        return new ArrayList<>();
    }
}
