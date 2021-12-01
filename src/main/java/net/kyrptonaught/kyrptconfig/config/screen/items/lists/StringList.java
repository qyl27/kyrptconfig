package net.kyrptonaught.kyrptconfig.config.screen.items.lists;


import net.kyrptonaught.kyrptconfig.config.screen.NotSuckyButton;
import net.kyrptonaught.kyrptconfig.config.screen.items.ConfigItem;
import net.kyrptonaught.kyrptconfig.config.screen.items.SubItem;
import net.kyrptonaught.kyrptconfig.config.screen.items.lists.entries.ListStringEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.List;

public class StringList extends SubItem<List<String>> {
    protected NotSuckyButton addButton, clearButton;

    public StringList(Text name, List<String> value, List<String> defaultValue) {
        super(name, false);
        this.value = value;
        this.defaultValue = defaultValue;
        populateFromList();
        this.addButton = new NotSuckyButton(0, 0, 35, 20, new TranslatableText("key.kyrptconfig.config.add"), widget -> {
            addConfigItem(createNewEntry(""));
        });
        this.clearButton = new NotSuckyButton(0, 0, 35, 20, new TranslatableText("key.kyrptconfig.config.clear"), widget -> {
            setValue(new ArrayList<>());
        });
        useDefaultResetBTN();
    }

    public StringList setExpanded(boolean expanded) {
        this.expanded = expanded;
        return this;
    }

    @Override
    public void setValue(List<String> value) {
        super.setValue(value);
        configs.forEach(configItem -> {
            if (configItem instanceof ListStringEntry stringListEntry)
                stringListEntry.setDeleted(true);
        });
        populateFromList();
    }

    public void populateFromList() {
        for (String s : value) {
            addConfigItem(createNewEntry(s));
        }
    }

    public ListStringEntry createNewEntry(String string) {
        return new ListStringEntry(string);
    }

    private List<String> getNewValues() {
        List<String> newValues = new ArrayList<>();
        configs.forEach(configItem -> {
            if (configItem instanceof ListStringEntry stringListEntry) {
                String result = stringListEntry.getValue();
                if (result != null) newValues.add(result);
            }
        });
        return newValues;
    }

    @Override
    public boolean isValueDefault() {
        return getNewValues().equals(defaultValue);
    }

    @Override
    protected void runSaveConsumer(List<String> value) {
        super.runSaveConsumer(getNewValues());
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (expanded && (addButton.mouseClicked(mouseX, mouseY, button) || clearButton.mouseClicked(mouseX, mouseY, button)) || resetButton.mouseClicked(mouseX, mouseY, button))
            return;
        super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(MatrixStack matrices, int x, int y, int mouseX, int mouseY, float delta) {
        super.render(matrices, x, y, mouseX, mouseY, delta);
        MinecraftClient.getInstance().textRenderer.draw(matrices, expanded ? "-" : "+", x - 10, y + 5, 16777215);
        subStart = y;
        if (expanded) {
            this.clearButton.y = y;
            this.clearButton.x = resetButton.x - (clearButton.getWidth() / 2) - 20;
            this.clearButton.render(matrices, mouseX, mouseY, delta);
            this.addButton.y = y;
            this.addButton.x = clearButton.x - (addButton.getWidth() / 2) - 20;
            this.addButton.render(matrices, mouseX, mouseY, delta);
            int runningY = subStart + 23;
            for (ConfigItem item : configs) {
                item.render(matrices, 30, runningY, mouseX, mouseY, delta);
                runningY += item.getSize() + 3;
            }
        }
    }
}
