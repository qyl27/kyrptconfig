package net.kyrptonaught.quickshulker.config.screen;

import net.kyrptonaught.quickshulker.config.screen.items.ConfigItem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ConfigSection extends Screen {

    Text title;
    List<ConfigItem> configs = new ArrayList<>();
    public NotSuckyButton sectionSelectionBTN;
    int selectionIndex = 0;

    public ConfigSection(ConfigScreen configScreen, Text title) {
        super(title);
        this.title = title;
        this.sectionSelectionBTN = new NotSuckyButton(0, 32, 10, 20, title, widget -> {
            configScreen.setSelectedSection(selectionIndex);
        });
        configScreen.addConfigSection(this);
    }

    public void save() {
        for (ConfigItem item : configs) {
            item.save();
        }
    }

    public int getTotalSectionSize() {
        int size = configs.size() * 3 + 5;
        for (ConfigItem item : configs) {
            size += item.getSize();
        }
        return size;
    }

    public void addConfigItem(ConfigItem item) {
        this.configs.add(item);
    }

    @Override
    public void tick() {
        super.tick();
        for (ConfigItem item : configs) {
            item.tick();
        }
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (ConfigItem item : configs) {
            if (item.keyPressed(keyCode, scanCode, modifiers))
                return true;
        }
        return false;
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        for (ConfigItem item : configs) {
            if (item.charTyped(chr, modifiers))
                return true;
        }
        return false;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (ConfigItem item : configs) {
            item.mouseClicked(mouseX, mouseY, button);
        }
        return false;
    }

    public void render(MatrixStack matrices, int startY, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        int runningY = startY + 5;
        for (ConfigItem item : configs) {
            item.render(matrices, 20, runningY, mouseX, mouseY, delta);
            runningY += item.getSize() + 3;
        }
    }
}