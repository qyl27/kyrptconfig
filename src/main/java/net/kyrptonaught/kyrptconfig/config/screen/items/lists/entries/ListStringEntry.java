package net.kyrptonaught.kyrptconfig.config.screen.items.lists.entries;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.kyrptonaught.kyrptconfig.config.screen.NotSuckyButton;
import net.kyrptonaught.kyrptconfig.config.screen.items.ConfigItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ListStringEntry extends ConfigItem<String> {
    boolean deleted = false;
    TextFieldWidget valueEntry;
    protected NotSuckyButton delButton;

    public ListStringEntry(String value) {
        super(new LiteralText(""), value, value);
        valueEntry = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, 0, 0, 96, 17, new LiteralText("Text Entry"));
        valueEntry.setText(value);
        this.delButton = new NotSuckyButton(0, 0, 35, 20, new TranslatableText("key.kyrptconfig.config.delete"), widget -> {
            setDeleted(true);
        });
    }

    public void setDeleted(boolean isDeleted) {
        this.deleted = isDeleted;
        if (deleted) valueEntry.setText("");
    }

    public String getValue() {
        if (deleted || valueEntry.getText() == null) return null;
        if (valueEntry.getText().isEmpty() || valueEntry.getText().isBlank()) return null;
        return valueEntry.getText();
    }

    @Override
    public void tick() {
        valueEntry.tick();
    }

    @Override
    public int getSize() {
        if (deleted) return -3;
        return super.getSize();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        super.keyPressed(keyCode, scanCode, modifiers);
        if (deleted) return false;
        return valueEntry.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (deleted) return false;
        return valueEntry.charTyped(chr, modifiers);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (deleted) return;
        delButton.mouseClicked(mouseX, mouseY, button);
        valueEntry.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(MatrixStack matrices, int x, int y, int mouseX, int mouseY, float delta) {
        if (deleted) return;
        super.render(matrices, x, y, mouseX, mouseY, delta);
        this.delButton.y = y;
        this.delButton.x = MinecraftClient.getInstance().getWindow().getScaledWidth() - delButton.getWidth() - 20;
        this.delButton.render(matrices, mouseX, mouseY, delta);

        this.valueEntry.y = y + 2;
        this.valueEntry.x = delButton.x - delButton.getWidth() - (valueEntry.getWidth() / 2) - 20;
        this.valueEntry.render(matrices, mouseX, mouseY, delta);
    }
}