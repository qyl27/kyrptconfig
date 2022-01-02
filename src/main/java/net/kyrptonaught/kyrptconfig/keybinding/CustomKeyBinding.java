package net.kyrptonaught.kyrptconfig.keybinding;

import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonPrimitive;
import blue.endless.jankson.api.Marshaller;
import net.kyrptonaught.kyrptconfig.config.ConfigDefaultCopyable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.Optional;

public class CustomKeyBinding implements ConfigDefaultCopyable {
    public boolean unknownIsActivated = false;
    public String rawKey;
    public InputUtil.Key defaultKey;
    public InputUtil.Key keycode;
    public boolean doParseKeycode = true;
    private String MOD_ID;

    public CustomKeyBinding(String MOD_ID) {
        this.MOD_ID = MOD_ID;
    }

    public CustomKeyBinding(String MOD_ID, boolean unknownIsActivated) {
        this.unknownIsActivated = unknownIsActivated;
        this.MOD_ID = MOD_ID;
    }

    public static CustomKeyBinding configDefault(String defaultKey) {
        return configDefault("config", defaultKey);
    }

    public static CustomKeyBinding configDefault(String MOD_ID, String defaultKey) {
        CustomKeyBinding customKeyBinding = new CustomKeyBinding(MOD_ID).setRaw(defaultKey);
        customKeyBinding.defaultKey = InputUtil.fromTranslationKey(defaultKey);
        return customKeyBinding;
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

    private void parseKeycode() {
        if (doParseKeycode) {
            keycode = getKeybinding().orElse(null);
            doParseKeycode = false;
        }
    }

    public boolean isKeybindPressed() {
        parseKeycode();
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

    public boolean matches(int keyCode, InputUtil.Type type) {
        parseKeycode();
        if (keycode == null) return false;
        return keycode.getCategory() == type && keycode.getCode() == keyCode;
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

    @Override
    public void copyFromDefault(ConfigDefaultCopyable otherDefault) {
        this.MOD_ID = ((CustomKeyBinding) otherDefault).MOD_ID;
        this.defaultKey = ((CustomKeyBinding) otherDefault).defaultKey;
        this.unknownIsActivated = ((CustomKeyBinding) otherDefault).unknownIsActivated;
    }
}
