package net.kyrptonaught.kyrptconfig.config.mixin;

import net.kyrptonaught.kyrptconfig.config.NonConflicting.ModifyableDefaultKey;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(KeyBinding.class)
public class KeybindingMixin implements ModifyableDefaultKey {

    @Mutable
    @Shadow
    private InputUtil.Key defaultKey;

    public void setDefaultKey(InputUtil.Key newKey) {
        this.defaultKey = newKey;
    }
}
