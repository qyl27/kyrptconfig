package net.kyrptonaught.cmdkeybind.config.config;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class BooleanItem extends ConfigItem<Boolean> {
    private boolean value, defaultValue;
    private ButtonWidget boolWidget, resetButton;

    public BooleanItem(Text name, Boolean value, Boolean defaultValue) {
        super(name);
        this.value = value;
        this.defaultValue = defaultValue;
        this.boolWidget = new ButtonWidget(0, 0, 100, 20, new LiteralText(value ? "True" : "False"), widget -> {
            this.value = !this.value;
            boolWidget.setMessage(new LiteralText(this.value ? "True" : "False"));
        });
        this.resetButton = new ButtonWidget(0, 0, 33, 20, new TranslatableText("reset"), widget -> {
            this.value = defaultValue;
            boolWidget.setMessage(new LiteralText(this.value ? "True" : "False"));
        });
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        boolWidget.mouseClicked(mouseX, mouseY, button);
        resetButton.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void save() {
        super.runSaveConsumer(value);
    }

    @Override
    public void render(MatrixStack matrices, int x, int y, int mouseX, int mouseY, float delta) {
        super.render(matrices, x, y, mouseX, mouseY, delta);
        this.boolWidget.y = y;
        this.resetButton.y = y;

        Window window = MinecraftClient.getInstance().getWindow();
        this.resetButton.x = window.getScaledWidth() - resetButton.getWidth() - 20;
        this.boolWidget.x = resetButton.x - resetButton.getWidth() - (boolWidget.getWidth() / 2) - 20;

        resetButton.active = (value == defaultValue);
        boolWidget.render(matrices, mouseX, mouseY, delta);
        resetButton.render(matrices, mouseX, mouseY, delta);
    }
}
