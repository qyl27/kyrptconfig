package net.kyrptonaught.cmdkeybind.config.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ConfigScreen extends Screen {

    List<ConfigItem> configs = new ArrayList<>();

    public ConfigScreen(Text title) {
        super(title);
    }

    public void addConfigItem(ConfigItem item) {
        this.configs.add(item);
    }

    public void tick() {
        super.tick();

    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        super.keyPressed(keyCode, scanCode, modifiers);
        for (ConfigItem item : configs) {
            boolean pressed = item.keyPressed(keyCode, scanCode, modifiers);
            if (pressed) return true;
        }
        return false;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (ConfigItem item : configs) {
            item.mouseClicked(mouseX, mouseY, button);
        }
        return false;
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        renderBackground(matrices);
        drawCenteredString(matrices, this.textRenderer, "Macros", this.width / 2, 20, 0xffffff);
        int runningY = 50;
        for (ConfigItem item : configs) {
            item.render(matrices, 20, runningY, mouseX, mouseY, delta);
            runningY += item.getSize() + 5;
        }
    }
}
