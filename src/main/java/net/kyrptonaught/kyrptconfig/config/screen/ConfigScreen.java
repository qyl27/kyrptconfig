package net.kyrptonaught.kyrptconfig.config.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ConfigScreen extends Screen {

    int selectedSection = 0;
    List<ConfigSection> sections = new ArrayList<>();
    private Runnable saveRunnable;
    Screen previousScreen;

    public ConfigScreen(Screen previousScreen, Text title) {
        super(title);
        this.previousScreen = previousScreen;
    }

    protected void init() {
        int center = this.width / 2;
        this.addDrawableChild(new NotSuckyButton(center - 153, height - 25, 150, 20, Text.translatable("key.kyrptconfig.config.exit"), widget -> {
            this.client.setScreen(previousScreen);
        }));

        this.addDrawableChild(new NotSuckyButton(center + 3, height - 25, 150, 20, Text.translatable("key.kyrptconfig.config.saveExit"), widget -> {
            save();
            this.client.setScreen(previousScreen);
        }));
        for (ConfigSection section : sections) {
            section.init(client, width, height - 55 - 30);
        }
    }

    public void setSavingEvent(Runnable save) {
        this.saveRunnable = save;
    }

    public void save() {
        for (ConfigSection section : sections) {
            section.save();
        }
        if (saveRunnable != null)
            saveRunnable.run();
    }

    public void addConfigSection(ConfigSection item) {
        item.selectionIndex = sections.size();
        if (sections.size() == 0)
            item.sectionSelectionBTN.setX(10);
        else
            item.sectionSelectionBTN.setX(sections.get(sections.size() - 1).sectionSelectionBTN.getX() + sections.get(sections.size() - 1).sectionSelectionBTN.getWidth() + 3);
        item.sectionSelectionBTN.setWidth(MinecraftClient.getInstance().textRenderer.getWidth(item.title) + 10);

        this.sections.add(item);
    }

    @Override
    public void tick() {
        super.tick();
        for (ConfigSection section : sections) {
            section.tick();
        }
    }

    public void setSelectedSection(int selectedSection) {
        this.selectedSection = selectedSection;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (sections.get(selectedSection).keyPressed(keyCode, scanCode, modifiers)) return true;
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return sections.get(selectedSection).charTyped(chr, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);

        for (ConfigSection section : sections)
            if (section.sectionSelectionBTN.mouseClicked(mouseX, mouseY, button)) return true;
        return sections.get(selectedSection).mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return sections.get(selectedSection).mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.getMatrices().push();
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

        renderBackgroundTexture(context);
        context.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);

        sections.get(selectedSection).render(context, 55, mouseX, mouseY, delta);

        context.getMatrices().translate(0, 0, 1);
        context.setShaderColor(64 / 255f, 64 / 255f, 64 / 255f, 1);
        context.drawTexture(OPTIONS_BACKGROUND_TEXTURE, 0, 0, 0, 0, this.width, 55, 64, 64);
        context.drawTexture(OPTIONS_BACKGROUND_TEXTURE, 0, this.height - 30, 0, 0, this.width, 30, 64, 64);
        context.setShaderColor(1, 1, 1, 1);

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 13, 0xffffff);
        for (int i = 0; i < sections.size(); i++) {
            sections.get(i).sectionSelectionBTN.active = i != selectedSection;
            sections.get(i).sectionSelectionBTN.render(context, mouseX, mouseY, delta);
        }
        sections.get(selectedSection).render2(context, 55, mouseX, mouseY, delta);

        super.render(context, mouseX, mouseY, delta);
        context.getMatrices().pop();
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {

    }
}
