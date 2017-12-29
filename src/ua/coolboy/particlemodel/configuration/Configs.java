package ua.coolboy.particlemodel.configuration;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.YamlConfiguration;
import ua.coolboy.particlemodel.ParticleModel;
import ua.coolboy.particlemodel.utils.LogUtil;

public class Configs {
    
    private static YamlConfiguration main;
    private static File mainFile;
    
    private static File folder;
    private static final ParticleModel PLUGIN = ParticleModel.getInstance();
    
    public Configs() {
        folder = PLUGIN.getDataFolder();
        mainFile = new File(folder, "config.yml");
        if (!mainFile.exists()) {
            PLUGIN.saveResource("config.yml", false);
        }
        main = YamlConfiguration.loadConfiguration(mainFile);
    }
    
    public YamlConfiguration getMainConfig() {
        return main;
    }
    
    public YamlConfiguration getConfig(String path, boolean create) {
        return getConfig(new File(folder, path), create);
    }
    
    public YamlConfiguration getConfig(File file, boolean create) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                LogUtil.warn("Can't create config: " + file.getName());
            }
        }
        return YamlConfiguration.loadConfiguration(file);
    }
    
    public void save(YamlConfiguration config, File file) {
        try {
            config.save(file);
        } catch (IOException ex) {
            LogUtil.warn("Can't save config: " + file.getName());
        }
    }
    
    public void save(YamlConfiguration config, String path) {
        try {
            config.save(path);
        } catch (IOException ex) {
            LogUtil.warn("Can't save config: " + path);
        }
    }
    
    public YamlConfiguration reload(File file) {
        return YamlConfiguration.loadConfiguration(file);
    }
    
}
