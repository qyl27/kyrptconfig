package net.kyrptonaught.kyrptconfig.config.screen.items.lists;

import net.kyrptonaught.kyrptconfig.config.screen.items.lists.entries.BlockIconEntry;
import net.kyrptonaught.kyrptconfig.config.screen.items.lists.entries.ListStringEntry;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.List;

public class BlockIconList extends StringList {
    boolean allowTags = false;

    @Deprecated
    public BlockIconList(Text name, List<String> value, List<String> defaultValue) {
        super(name, value, defaultValue);
        setToolTip();
    }

    public BlockIconList(Text name, List<String> value, List<String> defaultValue, Boolean allowTags) {
        super(name, value, defaultValue, false);
        this.allowTags = allowTags;
        setToolTip();
        populateFromList();
    }

    public void setToolTip() {
        if (allowTags) {
            setToolTip(new TranslatableText("key.kyrptconfig.config.hastags"),
                    new TranslatableText("key.kyrptconfig.config.tagsdisplay"));
        } else {
            setToolTip(new TranslatableText("key.kyrptconfig.config.nothastags"),
                    new TranslatableText("key.kyrptconfig.config.tagsdisplay"));
        }
    }

    @Deprecated
    public BlockIconList setAllowTags(boolean allowTags) {
        this.allowTags = allowTags;
        setToolTip();
        return this;
    }

    @Override
    public ListStringEntry createNewEntry(String string) {
        return new BlockIconEntry(string, allowTags);
    }
}