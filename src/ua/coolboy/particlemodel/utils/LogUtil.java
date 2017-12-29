package ua.coolboy.particlemodel.utils;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import ua.coolboy.particlemodel.ParticleModel;

public abstract class LogUtil {
    
    private static boolean log = ParticleModel.log;
    private static Logger logger = Bukkit.getLogger();
    
    public static void log(String string) {
        if(!log) return;
        logger.info(string);
    }
    
    public static void info(String string) {
        logger.info(string);
    }
    
    public static void warn(String string) {
        logger.warning(string);
    }

}
