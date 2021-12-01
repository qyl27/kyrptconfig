package net.kyrptonaught.kyrptconfig.config.screen.items.lists;

import net.kyrptonaught.kyrptconfig.config.screen.items.lists.entries.ItemIconEntry;
import net.kyrptonaught.kyrptconfig.config.screen.items.lists.entries.ListStringEntry;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.List;

public class ItemIconList extends StringList {
    boolean allowTags = false;

    public ItemIconList(Text name, List<String> value, List<String> defaultValue) {
        super(name, value, defaultValue);
        setToolTip();
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

    public ItemIconList setAllowTags(boolean allowTags) {
        this.allowTags = allowTags;
        setToolTip();
        return this;
    }

    public ListStringEntry createNewEntry(String string) {
        return new ItemIconEntry(string, allowTags);
    }
}
