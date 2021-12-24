package net.kyrptonaught.kyrptconfig.mixin.displaykeybind;

import net.kyrptonaught.kyrptconfig.keybinding.DisplayOnlyKeyBind;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameOptions.class)
public class GameOptionsMixin {

    @Redirect(method = "accept", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;getTranslationKey()Ljava/lang/String;"))
    public String spoofDisplayOnlyKeyBinds(KeyBinding instance) {
        if (instance instanceof DisplayOnlyKeyBind) {
            return "kryptconfig_displayonly";
        }
        return instance.getTranslationKey();
    }

    @Redirect(method = "accept", at = @At(value = "INVOKE", target = " Lnet/minecraft/client/option/GameOptions$Visitor;visitString(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;"))
    public String skipDisplayOnlyKeyBinds(GameOptions.Visitor instance, String key, String value) {
        if (key.equals("key_" + "kryptconfig_displayonly")) {
            return value;
        }
        return instance.visitString(key, value);
    }
}
