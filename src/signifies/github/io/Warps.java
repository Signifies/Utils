package signifies.github.io;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.logging.Level;

public class Warps
{
    private FileConfiguration WarpConfig = null;
    private File WarpConfigFile = null;
    private static final String fileName = "Warps.yml";

    private Utils instance;

    public Warps(Utils value)
    {
        instance = value;
    }


    public void reloadWarpConfig() {
        if (WarpConfigFile == null) {
            WarpConfigFile = new File(instance.getDataFolder(),fileName);
        }
        WarpConfig = YamlConfiguration.loadConfiguration(WarpConfigFile);

        // Look for defaults in the jar
        Reader defConfigStream = null;
        try {
            defConfigStream = new InputStreamReader(instance.getResource(fileName), "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            WarpConfig.setDefaults(defConfig);
        }
    }

    public FileConfiguration getWarpConfig() {
        if (WarpConfig == null) {
            reloadWarpConfig();
        }
        return WarpConfig;
    }

    public void saveWarpConfig() {
        if (WarpConfig == null || WarpConfigFile == null) {
            return;
        }
        try {
            getWarpConfig().save(WarpConfigFile);
        } catch (IOException ex) {
            instance.getLogger().log(Level.SEVERE, "Could not save config to " + WarpConfigFile, ex);
        }
    }

    public void saveDefaultWarpConfig() {
        if (WarpConfigFile == null) {
            WarpConfigFile = new File(instance.getDataFolder(), fileName);
        }
        if (!WarpConfigFile.exists()) {
            instance.saveResource(fileName, false);
        }
    }
}