package net.kyrptonaught.kyrptconfig.config.screen;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class NotSuckyButton extends ButtonWidget {
    int buttonColor = 16777215;

    public NotSuckyButton(int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION_SUPPLIER);
    }

    public void setButtonColor(int color) {
        this.buttonColor = color;
    }

    @Override
    public void drawMessage(MatrixStack matrices, TextRenderer textRenderer, int color) {
        int i = this.active ? buttonColor : 0xA0A0A0;
        this.drawScrollableText(matrices, textRenderer, 2, i);
    }
}
