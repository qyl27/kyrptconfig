package net.kyrptonaught.cmdkeybind.config.config;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class KeybindItem extends ConfigItem<String> {
    String key, defaultKey;
    private ButtonWidget keyButton, resetButton;
    private Boolean isListening = false;

    public KeybindItem(Text name, String key, String defaultKey) {
        super(name);
        this.key = key;
        this.defaultKey = defaultKey;
        this.keyButton = new ButtonWidget(0, 0, 100, 20, getCleanName(key), widget -> {
            this.isListening = !this.isListening;
            if (!this.isListening) {
                widget.setMessage(this.getCleanName(this.key));
            } else {
                widget.setMessage(new LiteralText("> ").append(this.getCleanName(this.key).append(new LiteralText(" <"))));
            }
        });
        this.resetButton = new ButtonWidget(0, 0, 33, 20, new TranslatableText("Reset"), widget -> {
            this.key = defaultKey;
            isListening = false;
            widget.setMessage(this.getCleanName(this.key));
        });
    }

    public MutableText getCleanName(String str) {
        if (I18n.hasTranslation(key))
            return new TranslatableText(str);
        return new LiteralText(str.substring(str.length() - 1).toUpperCase());
    }

    @Override
    public void save() {
        super.runSaveConsumer(key);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (isListening) {
            key = InputUtil.fromKeyCode(keyCode, scanCode).getTranslationKey();
            this.keyButton.setMessage(this.getCleanName(key));
            isListening = false;
            return true;
        }
        return false;
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        boolean handled;
        handled = (keyButton.mouseClicked(mouseX, mouseY, button) || resetButton.mouseClicked(mouseX, mouseY, button));
        if (isListening && !handled) {
            key = InputUtil.Type.MOUSE.createFromCode(button).getTranslationKey();
            this.keyButton.setMessage(this.getCleanName(key));
            isListening = false;
        }
    }

    @Override
    public void render(MatrixStack matrices, int x, int y, int mouseX, int mouseY, float delta) {
        super.render(matrices, x, y, mouseX, mouseY, delta);
        this.keyButton.y = y;
        this.resetButton.y = y;

        Window window = MinecraftClient.getInstance().getWindow();
        this.resetButton.x = window.getScaledWidth() - resetButton.getWidth() - 20;
        this.keyButton.x = resetButton.x - resetButton.getWidth() - (keyButton.getWidth() / 2) - 20;

        resetButton.active = !key.equals(defaultKey);
        keyButton.render(matrices, mouseX, mouseY, delta);
        resetButton.render(matrices, mouseX, mouseY, delta);
    }
}
