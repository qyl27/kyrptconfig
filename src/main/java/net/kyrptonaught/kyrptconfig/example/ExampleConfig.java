package net.kyrptonaught.kyrptconfig.example;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.loader.api.FabricLoader;
import net.kyrptonaught.kyrptconfig.config.screen.ConfigScreen;
import net.kyrptonaught.kyrptconfig.config.screen.ConfigSection;
import net.kyrptonaught.kyrptconfig.config.screen.items.BooleanItem;
import net.kyrptonaught.kyrptconfig.config.screen.items.ButtonItem;
import net.kyrptonaught.kyrptconfig.config.screen.items.KeybindItem;
import net.kyrptonaught.kyrptconfig.config.screen.items.TextItem;
import net.kyrptonaught.kyrptconfig.config.screen.items.lists.StringList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class ExampleConfig implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {

        return (screen) -> {
            if (!FabricLoader.getInstance().isDevelopmentEnvironment())
                return null;

            // regex replace (\.setSaveConsumer\()([\S\h])*val\) & (options\.)([^\,])+
            ConfigScreen configScreen = new ConfigScreen(screen, Text.translatable("key.kyrptconfig.exampleconfig"));

            ConfigSection displaySection = new ConfigSection(configScreen, Text.translatable("key.kyrptconfig.exampleconfig.category.display"));
            displaySection.addConfigItem(new BooleanItem(Text.translatable("key.kyrptconfig.exampleconfig.displaysort"), true, true));
            displaySection.addConfigItem(new BooleanItem(Text.translatable("key.kyrptconfig.exampleconfig.seperatebtn"), false, true));
            for (int i = 0; i < 10; i++)
                displaySection.addConfigItem(new BooleanItem(Text.translatable("key.kyrptconfig.exampleconfig.displaytooltip"), true, true));

            ConfigSection logicSection = new ConfigSection(configScreen, Text.translatable("key.kyrptconfig.exampleconfig.category.logic"));
            logicSection.addConfigItem(new BooleanItem(Text.translatable("key.kyrptconfig.exampleconfig.sortplayer"), true, false));

            ConfigSection activationSection = new ConfigSection(configScreen, Text.translatable("key.kyrptconfig.exampleconfig.category.activation"));
            activationSection.addConfigItem(new KeybindItem(Text.translatable("key.inventorysorter.sort"), "key.keyboard.l", "key.keyboard.p"));
            activationSection.addConfigItem(new BooleanItem(Text.translatable("key.kyrptconfig.exampleconfig.middleclick"), true, true));
            activationSection.addConfigItem(new BooleanItem(Text.translatable("key.kyrptconfig.exampleconfig.doubleclick"), true, true));
            activationSection.addConfigItem(new BooleanItem(Text.translatable("key.kyrptconfig.exampleconfig.sortmousehighlighted"), true, true));

            ConfigSection blackListSection = new ConfigSection(configScreen, Text.translatable("key.kyrptconfig.exampleconfig.category.blacklist"));

            blackListSection.addConfigItem(new BooleanItem(Text.translatable("key.kyrptconfig.exampleconfig.showdebug"), true, false).setToolTipWithNewLine("key.kyrptconfig.exampleconfig.debugtooltip"));

            String DOWNLOAD_URL = "https://raw.githubusercontent.com/kyrptonaught/Inventory-Sorter/1.19/DownloadableBlacklist.json5";
            TextItem blackListURL = new TextItem(Text.translatable("key.kyrptconfig.exampleconfig.blacklistURL"), DOWNLOAD_URL, DOWNLOAD_URL).setMaxLength(1024);
            blackListSection.addConfigItem(blackListURL);

            StringList hideList = (StringList) new StringList(Text.translatable("key.kyrptconfig.exampleconfig.hidesort"), new ArrayList<>(), new ArrayList<>()).setToolTipWithNewLine("key.kyrptconfig.exampleconfig.hidetooltip");
            StringList nosortList = (StringList) new StringList(Text.translatable("key.kyrptconfig.exampleconfig.nosort"), new ArrayList<>(), new ArrayList<>()).setToolTipWithNewLine("key.kyrptconfig.exampleconfig.nosorttooltip");

            blackListSection.addConfigItem(new ButtonItem(Text.translatable("key.kyrptconfig.exampleconfig.downloadListButton")).setClickEvent(() -> {
                SystemToast.add(MinecraftClient.getInstance().getToastManager(), SystemToast.Type.TUTORIAL_HINT, Text.translatable("key.inventorysorter.toast.error"), Text.translatable("key.inventorysorter.toast.error2"));

            }));

            blackListSection.addConfigItem(hideList);
            blackListSection.addConfigItem(nosortList);

            return configScreen;
        };
    }
}
