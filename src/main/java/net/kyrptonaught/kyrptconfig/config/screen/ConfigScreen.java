package net.kyrptonaught.kyrptconfig.config.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class ConfigScreen extends Screen {

    int selectedSection = 0;
    List<ConfigSection> sections = new ArrayList<>();
    private Runnable saveRunnable;
    Screen previousScreen;
    private NotSuckyButton scrollLeftBTN, scrollRightBTN;
    int horizontalScrollOffset = -1;
    private static final Identifier SCROLLER_TEXTURE = new Identifier("widget/scroller");
    private static final Identifier OPTIONS_BACKGROUND_TEXTURE = new Identifier("textures/block/dirt.png");

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

        adjustForHorizontalScroll(this.width);
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

    public boolean adjustForHorizontalScroll(int maxWidth) {
        NotSuckyButton lastBTN = sections.get(sections.size() - 1).sectionSelectionBTN;

        this.scrollLeftBTN = new NotSuckyButton(10, 32, 10, 20, Text.literal("<"), widget -> {
            NotSuckyButton nextBtn = sections.get(0).sectionSelectionBTN;
            for (int i = sections.size() - 1; i >= 0; i--) {
                if (sections.get(i).sectionSelectionBTN.getX() < scrollLeftBTN.getX() + scrollLeftBTN.getWidth() + 3) {
                    nextBtn = sections.get(i).sectionSelectionBTN;
                    if (i == 0)
                        scrollLeftBTN.active = false;
                    break;
                }
            }
            scrollRightBTN.active = true;
            horizontalScrollOffset += (nextBtn.getX()) - (scrollLeftBTN.getX() + scrollLeftBTN.getWidth() + 3);
        });

        this.scrollRightBTN = new NotSuckyButton(this.width - 20, 32, 10, 20, Text.literal(">"), widget -> {
            NotSuckyButton nextBtn = lastBTN;
            for (int i = 0; i < sections.size(); i++) {
                if (sections.get(i).sectionSelectionBTN.getX() + sections.get(i).sectionSelectionBTN.getWidth() + 3 > maxWidth) {
                    nextBtn = sections.get(i).sectionSelectionBTN;
                    if (i == sections.size() - 1)
                        scrollRightBTN.active = false;
                    break;
                }
            }
            scrollLeftBTN.active = true;
            horizontalScrollOffset += (nextBtn.getX() + nextBtn.getWidth() + 3) - (scrollRightBTN.getX());
        });

        if (lastBTN.getX() + lastBTN.getWidth() + 3 > maxWidth) {
            for (ConfigSection section : sections) {
                section.sectionSelectionBTN.setX(section.sectionSelectionBTN.getX() + scrollLeftBTN.getWidth() + 3);
            }


            scrollLeftBTN.active = scrollLeftBTN.visible = true;
            scrollRightBTN.active = scrollRightBTN.visible = true;
            horizontalScrollOffset = 0;
            return true;
        }

        scrollLeftBTN.active = scrollLeftBTN.visible = false;
        scrollRightBTN.active = scrollRightBTN.visible = false;
        horizontalScrollOffset = -1;
        return false;
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

        if (scrollLeftBTN.mouseClicked(mouseX, mouseY, button) || scrollRightBTN.mouseClicked(mouseX, mouseY, button))
            return true;

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

        renderDarkening(context);
        context.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);

        ConfigSection section = sections.get(selectedSection);

        section.render(context, 55, mouseX, mouseY, delta);

        context.getMatrices().translate(0, 0, 1);
        context.setShaderColor(64 / 255f, 64 / 255f, 64 / 255f, 1);
        context.drawTexture(MENU_BACKGROUND_TEXTURE, 0, 0, 0, 0, this.width, 55, 64, 64);
        context.drawTexture(MENU_BACKGROUND_TEXTURE, 0, this.height - 30, 0, 0, this.width, 30, 64, 64);
        context.setShaderColor(1, 1, 1, 1);

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 13, 0xffffff);


        boolean noHover = scrollLeftBTN.detectHover(mouseX, mouseY) | scrollRightBTN.detectHover(mouseX, mouseY);
        for (int i = 0; i < sections.size(); i++) {
            NotSuckyButton selectionBTN = sections.get(i).sectionSelectionBTN;
            selectionBTN.active = i != selectedSection;

            if (horizontalScrollOffset > -1) {
                if (i == 0) {
                    selectionBTN.setX(scrollLeftBTN.getX() + scrollLeftBTN.getWidth() + 3 - horizontalScrollOffset);
                } else {
                    NotSuckyButton previousBTN = sections.get(i - 1).sectionSelectionBTN;
                    selectionBTN.setX(previousBTN.getX() + previousBTN.getWidth() + 3);
                }
                selectionBTN.disableHover = noHover;
            }
            selectionBTN.render(context, mouseX, mouseY, delta);
        }

        if (horizontalScrollOffset > -1) {
            context.getMatrices().translate(0, 0, 1);
            context.setShaderColor(64 / 255f, 64 / 255f, 64 / 255f, 1);
            context.drawTexture(OPTIONS_BACKGROUND_TEXTURE, 0, 0, 0, 0, scrollLeftBTN.getRight() + 1, 55, 64, 64);
            context.drawTexture(OPTIONS_BACKGROUND_TEXTURE, scrollRightBTN.getX() - 1, 0, scrollRightBTN.getX() - 1, 0, this.width - (scrollRightBTN.getX()) + 1, 55, 64, 64);
            context.setShaderColor(1, 1, 1, 1);

            scrollLeftBTN.render(context, mouseX, mouseY, delta);
            scrollRightBTN.render(context, mouseX, mouseY, delta);

            context.getMatrices().translate(0, 0, -1);
        }

        if (section.calculateSectionHeight() > 0) {
            int x = this.width - 6;

            float overflow = ((float) section.calculateSectionHeight() / this.height);
            int height = section.height - MathHelper.lerp(overflow, 0, section.height);
            height = MathHelper.clamp(height, 20, section.height - 8);

            float percentage = (float) -section.scrollOffset / section.calculateSectionHeight();
            int y = MathHelper.lerp(percentage, 55, this.height - 30 - height);

            context.fill(x, 55, x + 6, this.height - 30, -16777216);
            context.drawGuiTexture(SCROLLER_TEXTURE, x, y, 6, height);
        }

        section.render2(context, 55, mouseX, mouseY, delta);

        super.render(context, mouseX, mouseY, delta);
        context.getMatrices().pop();
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {

    }

    private void renderBackgroundTexture(DrawContext context) {
        context.setShaderColor(0.25F, 0.25F, 0.25F, 1.0F);
        context.drawTexture(OPTIONS_BACKGROUND_TEXTURE, 0, 0, 0, 0.0F, 0.0F, this.width, this.height, 32, 32);
        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
