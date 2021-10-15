package net.kyrptonaught.cmdkeybind.config.config;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class ConfigItem<T> {
    private Text fieldTitle;

    public ConfigItem(Text name) {
        this.fieldTitle = name;
    }

    private Consumer<T> saveConsumer;

    public ConfigItem setSaveConsumer(Consumer<T> save) {
        this.saveConsumer = save;
        return this;
    }

    protected void runSaveConsumer(T value) {
        saveConsumer.accept(value);
    }

    public void save() {

    }

    public int getSize() {
        return 20;
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {

    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    public void render(MatrixStack matrices, int x, int y, int mouseX, int mouseY, float delta) {
        MinecraftClient.getInstance().textRenderer.draw(matrices, this.fieldTitle, x, y + 5, 16777215);
    }
}
