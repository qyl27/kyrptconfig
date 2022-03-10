package net.kyrptonaught.kyrptconfig.config.screen.items;

import net.kyrptonaught.kyrptconfig.config.screen.NotSuckyButton;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Language;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class ConfigItem<T> {
    private final Text fieldTitle;
    private List<Text> toolTipText;
    protected Consumer<T> saveConsumer;
    protected NotSuckyButton resetButton;
    protected T value, defaultValue;
    private boolean requiresRestart = false;

    public ConfigItem(Text name, T value, T defaultValue) {
        this.fieldTitle = name;
        this.value = value;
        this.defaultValue = defaultValue;
    }

    public ConfigItem<?> setSaveConsumer(Consumer<T> save) {
        this.saveConsumer = save;
        return this;
    }

    public ConfigItem<?> setRequiresRestart() {
        requiresRestart = true;
        ((MutableText) fieldTitle).append(" *");
        return this;
    }

    public ConfigItem<?> setToolTipWithNewLine(String translatableKey){
        String[] translated = Language.getInstance().get(translatableKey).split("\n");
        this.toolTipText = new ArrayList<>();
        for (String line : translated) {
            this.toolTipText.add(new LiteralText(line));
        }

        return this;
    }

    public ConfigItem<?> setToolTip(Text toolTip) {
        this.toolTipText = List.of(toolTip);
        return this;
    }

    public ConfigItem<?> setToolTip(Text... toolTips) {
        this.toolTipText = List.of(toolTips);
        return this;
    }

    public boolean requiresRestart() {
        return requiresRestart;
    }

    protected void runSaveConsumer(T value) {
        if (saveConsumer != null && value != null)
            saveConsumer.accept(value);
    }

    public void save() {
        runSaveConsumer(value);
    }

    public int getSize() {
        return 20;
    }

    public void useDefaultResetBTN() {
        this.resetButton = new NotSuckyButton(0, 0, 35, 20, new TranslatableText("key.kyrptconfig.config.reset"), widget -> {
            resetToDefault();
        });
    }

    public void resetToDefault() {
        setValue(defaultValue);
    }

    public boolean isValueDefault() {
        return value.equals(defaultValue);
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void tick() {
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (resetButton != null)
            resetButton.mouseClicked(mouseX, mouseY, button);
    }

    public boolean charTyped(char chr, int modifiers) {
        return false;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    public void render(MatrixStack matrices, int x, int y, int mouseX, int mouseY, float delta) {
        MinecraftClient.getInstance().textRenderer.draw(matrices, this.fieldTitle, x, y + 5, 16777215);
        if (resetButton != null) {
            this.resetButton.y = y;
            this.resetButton.x = MinecraftClient.getInstance().getWindow().getScaledWidth() - resetButton.getWidth() - 20;
            resetButton.active = !isValueDefault();
            resetButton.render(matrices, mouseX, mouseY, delta);
        }
    }

    public void render2(MatrixStack matrices, int x, int y, int mouseX, int mouseY, float delta) {
        if (mouseX > x && mouseX < x + MinecraftClient.getInstance().textRenderer.getWidth(fieldTitle) &&
                mouseY > y && mouseY < y + 12)
            renderToolTip(matrices, mouseX, mouseY);
    }

    public void renderToolTip(MatrixStack matrices, int x, int y) {
        if (toolTipText != null)
            MinecraftClient.getInstance().currentScreen.renderTooltip(matrices, toolTipText, x, y);
        else if (requiresRestart) {
            MinecraftClient.getInstance().currentScreen.renderTooltip(matrices, new TranslatableText("key.kyrptconfig.config.restartRequired"), x, y);
        }
    }
      /*  maybe save this for later.
      public ConfigItem(Text name, ConfigWDefaults config, Function<AbstractConfigFile,T> getValue) {
        this(name, getValue.apply(config), getValue.apply(config.getDefaults()));
    }*/
}
