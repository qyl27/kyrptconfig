package net.kyrptonaught.kyrptconfig;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.kyrptonaught.kyrptconfig.config.NonConflicting.AddNonConflictingKeyBind;
import net.kyrptonaught.kyrptconfig.config.NonConflicting.NonConflictingKeyBindData;
import net.kyrptonaught.kyrptconfig.config.NonConflicting.NonConflictingKeyBinding;
import net.minecraft.client.util.InputUtil;

import java.util.ArrayList;
import java.util.List;

public class KyrptConfigClientInit implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        List<NonConflictingKeyBindData> keybindings = new ArrayList<>();
        FabricLoader.getInstance().getEntrypoints("registernonconflicting", AddNonConflictingKeyBind.class).forEach(addNonConflictingKeyBind -> addNonConflictingKeyBind.addKeyBinding(keybindings));
        for (NonConflictingKeyBindData bindData : keybindings) {
            if (bindData.defaultKey == null) bindData.defaultKey = bindData.inputType.createFromCode(bindData.keyCode);
            NonConflictingKeyBinding nonConflictingKeyBinding = new NonConflictingKeyBinding(
                    bindData.name,
                    bindData.defaultKey.getCategory(),
                    bindData.defaultKey.getCode(),
                    bindData.category
            );
            nonConflictingKeyBinding.setBoundKey(bindData.inputType.createFromCode(bindData.keyCode));
        }
    }
}
