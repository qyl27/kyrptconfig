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
    public String rawKey = "";
    public String defaultKey = "";
    public InputUtil.Key parsedKey;
    public boolean doParseKey = true;
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
        customKeyBinding.defaultKey = defaultKey;
        return customKeyBinding;
    }

    public CustomKeyBinding setRaw(String key) {
        rawKey = key;
        doParseKey = true;
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
        if (doParseKey) {
            parsedKey = getKeybinding().orElse(null);
            doParseKey = false;
        }
    }

    public boolean isKeybindPressed() {
        parseKeycode();
        if (parsedKey == null) // Invalid key
            return false;
        if (parsedKey == InputUtil.UNKNOWN_KEY)
            return unknownIsActivated; // Always pressed for empty or explicitly "key.keyboard.unknown"
        boolean pressed;
        if (parsedKey.getCategory() == InputUtil.Type.MOUSE)
            pressed = GLFW.glfwGetMouseButton(MinecraftClient.getInstance().getWindow().getHandle(), parsedKey.getCode()) == 1;
        else
            pressed = GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), parsedKey.getCode()) == 1;
        return pressed;
    }

    public boolean matches(int keyCode, InputUtil.Type type) {
        parseKeycode();
        if (parsedKey == null) return false;
        return parsedKey.getCategory() == type && parsedKey.getCode() == keyCode;
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

    public InputUtil.Key getDefaultKey() {
        if (defaultKey == null || defaultKey.isEmpty())
            return InputUtil.UNKNOWN_KEY;
        try {
            return InputUtil.fromTranslationKey(defaultKey);
        } catch (IllegalArgumentException e) {
            System.out.println(MOD_ID + ": unknown default key entered");
            return InputUtil.UNKNOWN_KEY;
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
