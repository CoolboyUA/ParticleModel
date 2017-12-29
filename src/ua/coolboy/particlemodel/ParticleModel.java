package ua.coolboy.particlemodel;

import com.owens.oobjloader.builder.Build;
import com.owens.oobjloader.parser.Parse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ua.coolboy.particlemodel.command.CommandParser;
import ua.coolboy.particlemodel.drawer.Drawer;

public class ParticleModel extends JavaPlugin {

    private static ParticleModel plugin;
    public static final double MIN_DISTANCE = 0.3;
    public static boolean log = true;
    private static final HashMap<String, Drawer> MODELS = new HashMap<>();
    public static final Particle PARTICLE = Particle.REDSTONE;

    @Override
    public void onEnable() {
        ParticleModel.plugin = this;
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            return CommandParser.parse((Player) sender, command, label, args);
        }
        return true;
    }

    public static ParticleModel getInstance() {
        return plugin;
    }

    public boolean loadModel(String name) {
        File file = new File(getDataFolder(), "/" + name + "/" + name + ".obj");
        if (!file.exists()) {
            Bukkit.getLogger().info("Can't find file: " + name);
            return false;
        }
        try {
            Build build = new Build();
            new Parse(build, file);
            MODELS.put(name, new Drawer(build, name));
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Can't load model {0}", name);
            return false;
        }
        return true;
    }

    public static Drawer getModel(String name) {
        return MODELS.get(name);
    }

    public static HashMap<String, Drawer> getModels() {
        return MODELS;
    }
}
