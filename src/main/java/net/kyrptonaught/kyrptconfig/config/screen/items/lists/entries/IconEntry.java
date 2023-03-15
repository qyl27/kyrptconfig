package net.kyrptonaught.kyrptconfig.config.screen.items.lists.entries;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.List;

public class IconEntry<E> extends ListStringEntry {
    protected boolean allowTags = false;
    protected List<E> enteredTag;
    int selectedTag = 0;
    float deltas;

    public IconEntry(String value, boolean allowTags) {
        super(value);
        this.allowTags = allowTags;
    }

    public ItemConvertible getItemToRender(float delta) {
        return Items.BARRIER;
    }

    public void tickTags(float delta) {
        deltas += delta;
        if (deltas > 45 && allowTags && enteredTag != null) {
            selectedTag++;
            deltas = 0;
        }
        if (enteredTag == null || selectedTag >= enteredTag.size()) selectedTag = 0;
    }

    @Override
    public void render(MatrixStack matrices, int x, int y, int mouseX, int mouseY, float delta) {
        super.render(matrices, x, y, mouseX, mouseY, delta);
        if (deleted) return;
        ItemConvertible item = getItemToRender(delta);
        renderGuiItemModel(matrices, new ItemStack(item), x, y);
    }

    protected void renderGuiItemModel(MatrixStack matrices, ItemStack stack, int x, int y) {
        BakedModel model = MinecraftClient.getInstance().getItemRenderer().getModel(stack, null, null, 0);
        matrices.push();
        MinecraftClient.getInstance().getTextureManager().getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).setFilter(false, false);
        RenderSystem.setShaderTexture(0, SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        matrices.translate(x, y, 100);
        matrices.translate(8.0, 8.0, 0.0);
        matrices.scale(1.0f, -1.0f, 1.0f);
        matrices.scale(16.0f, 16.0f, 16.0f);
        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        boolean bl = !model.isSideLit();
        if (bl) {
            DiffuseLighting.disableGuiDepthLighting();
        }
        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.GUI, false, matrices, immediate, LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, model);
        immediate.draw();
        if (bl) {
            DiffuseLighting.enableGuiDepthLighting();
        }
        matrices.pop();
    }
}
