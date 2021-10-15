package net.kyrptonaught.kyrptconfig.config.NonConflicting;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import java.util.function.Consumer;

public class NonConflictingKeyBinding extends KeyBinding {
    private final Consumer<InputUtil.Key> keySet;

    public NonConflictingKeyBinding(String name, InputUtil.Type type, int code, String category, Consumer<InputUtil.Key> keySet) {
        super(name, type, code, category);
        this.keySet = keySet;
    }

    public void setBoundKey(InputUtil.Key boundKey) {
        super.setBoundKey(boundKey);
        keySet.accept(boundKey);
    }
}