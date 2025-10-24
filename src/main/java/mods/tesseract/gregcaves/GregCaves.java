package mods.tesseract.gregcaves;

import mods.tesseract.gregcaves.event.GregCaveEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

@Mod(modid = "gregcaves", acceptableRemoteVersions = "*")
public final class GregCaves {
    public static boolean smoothBedrock;
    public static boolean reduceOreGen;
    public static int reduceOreGenY;
    public static float reduceOreGenRate;
    public static int caveLavaLevel;
    public static boolean disableUndergroundLiquid;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        syncConfig(e.getSuggestedConfigurationFile());
        MinecraftForge.TERRAIN_GEN_BUS.register(new GregCaveEvents());
    }

    public static void syncConfig(File f) {
        Configuration configuration = new Configuration(f);
        smoothBedrock = configuration.getBoolean("smoothBedrock", Configuration.CATEGORY_GENERAL, false, "Only generates one layer of bedrock.");
        reduceOreGen = configuration.getBoolean("reduceOreGen", Configuration.CATEGORY_GENERAL, true, "Reduce ores in the deep.");
        reduceOreGenY = configuration.getInt("reduceOreGenY", Configuration.CATEGORY_GENERAL, 33, 0, 256, "Reduce ores start height.");
        reduceOreGenRate = configuration.getFloat("reduceOreGenRate", Configuration.CATEGORY_GENERAL, 0.6F, 0, 1, "1 = all, 0 = does nothing");
        caveLavaLevel = configuration.getInt("caveLavaLevel", Configuration.CATEGORY_GENERAL, 2, 0, 256, "For greg caves requires smoothBedrock true to work.");
        disableUndergroundLiquid = configuration.getBoolean("disableUndergroundLiquid", Configuration.CATEGORY_GENERAL, true, "Disable water and lava source generations in underground.");
        if (configuration.hasChanged())
            configuration.save();
    }
}
