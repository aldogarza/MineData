package me.aldogarza.minedata;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream; // Add this import
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class ConfigManager {
    private final String fileName;
    private Map<String, Object> config;

    public ConfigManager(String fileName) {
        this.fileName = fileName;
        createConfigFolder();
        loadConfig();
    }

    private void createConfigFolder() {
        File configFolder = new File("plugins/MineData");
        if (!configFolder.exists()) {
            configFolder.mkdirs();
            createDefaultConfigFile(configFolder);
        }
    }

    private void createDefaultConfigFile(File configFolder) {
        File configFile = new File(configFolder, fileName);
        if (!configFile.exists()) {
            try (InputStream in = getClass().getClassLoader().getResourceAsStream(fileName);
                 FileOutputStream out = new FileOutputStream(configFile)) {
                if (in != null) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                } else {
                    throw new RuntimeException("Default configuration file not found: " + fileName);
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to create default configuration file: " + fileName, e);
            }
        }
    }

    private void loadConfig() {
        Yaml yaml = new Yaml();
        File configFile = new File("plugins/MineData", fileName);
        try (InputStream in = new FileInputStream(configFile)) {
            config = yaml.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file: " + fileName, e);
        }
    }

    public String getString(String path) {
        String[] keys = path.split("\\.");
        Map<String, Object> current = config;
        for (int i = 0; i < keys.length - 1; i++) {
            current = (Map<String, Object>) current.get(keys[i]);
        }
        return (String) current.get(keys[keys.length - 1]);
    }
}