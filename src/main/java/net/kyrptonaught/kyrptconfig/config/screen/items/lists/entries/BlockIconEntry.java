package net.kyrptonaught.kyrptconfig.config.screen.items.lists.entries;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockIconEntry extends IconEntry<Block> {
    public BlockIconEntry(String value, boolean allowTags) {
        super(value, allowTags);
    }

    @Override
    public Item getItemToRender(float delta) {
        String entered = getValue();
        if (entered == null) return Items.BARRIER;
        if (entered.startsWith("#") && allowTags) {
            entered = entered.replaceAll("#", "");
            if (BlockTags.getTagGroup().contains(new Identifier(entered)))
                enteredTag = BlockTags.getTagGroup().getTag(new Identifier(entered));
            else enteredTag = null;
            if (enteredTag != null) {
                tickTags(delta);
                return enteredTag.values().get(selectedTag).asItem();
            }
        }
        if (entered.startsWith("#"))
            return Items.BARRIER;
        return Registry.ITEM.getOrEmpty(new Identifier(entered)).orElse(Items.BARRIER);
    }
}