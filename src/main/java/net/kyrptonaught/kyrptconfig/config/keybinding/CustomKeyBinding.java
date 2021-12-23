package net.kyrptonaught.kyrptconfig.config.keybinding;

import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import blue.endless.jankson.api.Marshaller;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.Optional;

public class CustomKeyBinding {
    public boolean unknownIsActivated = false;
    public String rawKey;
    public InputUtil.Key keycode;
    public boolean doParseKeycode = true;
    private final String MOD_ID;

    public CustomKeyBinding(String MOD_ID) {
        this.MOD_ID = MOD_ID;
    }

    public CustomKeyBinding(String MOD_ID, boolean unknownIsActivated) {
        this.unknownIsActivated = unknownIsActivated;
        this.MOD_ID = MOD_ID;
    }

    public static CustomKeyBinding configDefault(String defaultKey) {
        return new CustomKeyBinding("config").setRaw(defaultKey);
    }

    public CustomKeyBinding setRaw(String key) {
        rawKey = key;
        doParseKeycode = true;
        holding = false;
        return this;
    }

    boolean holding = false;

    public boolean wasPressed() {
        boolean pressed = isKeybindPressed();
        if (!holding) {
            holding = pressed;
            return pressed;
        }
        if (!pressed)
            holding = false;
        return false;
    }

    public boolean isKeybindPressed() {
        if (doParseKeycode) {
            keycode = getKeybinding().orElse(null);
            doParseKeycode = false;
        }
        if (keycode == null) // Invalid key
            return false;
        if (keycode == InputUtil.UNKNOWN_KEY)
            return unknownIsActivated; // Always pressed for empty or explicitly "key.keyboard.unknown"
        boolean pressed;
        if (keycode.getCategory() == InputUtil.Type.MOUSE)
            pressed = GLFW.glfwGetMouseButton(MinecraftClient.getInstance().getWindow().getHandle(), keycode.getCode()) == 1;
        else
            pressed = GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), keycode.getCode()) == 1;
        return pressed;
    }

    public Optional<InputUtil.Key> getKeybinding() {
        if (rawKey.isEmpty())
            return Optional.of(InputUtil.UNKNOWN_KEY);
        try {
            return Optional.of(InputUtil.fromTranslationKey(rawKey));
        } catch (IllegalArgumentException e) {
            System.out.println(MOD_ID + ": unknown key entered");
            return Optional.empty();
        }
    }

    public static JsonElement saveKeybinding(CustomKeyBinding keyBinding, Marshaller m) {
        return new JsonPrimitive(keyBinding.rawKey);
    }

    public static CustomKeyBinding loadKeybinding(String s, Marshaller m) {
        return CustomKeyBinding.configDefault(s);
    }
}
