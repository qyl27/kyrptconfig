package net.kyrptonaught.kyrptconfig.config;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonObject;

import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ConfigStorage {
    private final Path saveFile;
    public AbstractConfigFile config;
    private final AbstractConfigFile defaultConfig;

    public ConfigStorage(Path fileName, AbstractConfigFile defaultConfig) {
        this.saveFile = fileName;
        this.defaultConfig = defaultConfig;
    }

    public void save(String MOD_ID, Jankson JANKSON) {
        try (OutputStreamWriter out = new OutputStreamWriter(Files.newOutputStream(saveFile), StandardCharsets.UTF_8)) {

            if (config instanceof ConfigWDefaults) ((ConfigWDefaults) config).beforeSave();
            String json = JANKSON.toJson(config).toJson(true, true);
            out.write(json);
            // out.write(json.getBytes());

        } catch (Exception e) {
            System.out.println(MOD_ID + " Failed to save " + saveFile.getFileName().toString());
        }
    }

    public AbstractConfigFile load(String MOD_ID, Jankson JANKSON) {
        if (!Files.exists(saveFile) || !Files.isReadable(saveFile)) {
            System.out.println(MOD_ID + " Config not found! Creating one.");
            config = defaultConfig;
        }
        boolean failed = false;
        try {
            JsonObject configJson = JANKSON.load(Files.newInputStream(saveFile, StandardOpenOption.READ));
            String regularized = configJson.toJson(false, false, 0);

            //Class<T> genericClazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            config = JANKSON.fromJson(regularized, defaultConfig.getClass());
            if (config instanceof ConfigWDefaults) {
                ((ConfigWDefaults) config).DEFAULTS = defaultConfig;
                ((ConfigWDefaults) config).afterLoad();
            }
        } catch (Exception e) {
            failed = true;
        }
        if (failed || (config == null)) {
            System.out.println(MOD_ID + " Failed to load config! Overwriting with default config.");
            config = defaultConfig;
        }
        return config;
    }
}