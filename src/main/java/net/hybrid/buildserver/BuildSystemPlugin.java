package net.hybrid.buildserver;

import net.hybrid.buildserver.commands.*;
import net.hybrid.buildserver.managers.StaffWorldManager;
import net.hybrid.buildserver.menus.MapCreationMenu;
import net.hybrid.buildserver.menus.MapsMenu;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class BuildSystemPlugin extends JavaPlugin {

    private static BuildSystemPlugin INSTANCE;

    private File mapsFile;

    @Override
    public void onEnable(){
        long time = System.currentTimeMillis();
        INSTANCE = this;

        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
        mapsFile = new File(getDataFolder(), "mapconfig.yml");

        new CreateMapCommand();
        new MapCommand();
        new SetSpawnCommand();
        new FlyCommand();
        new SkullCommand();
        new BackCommand();
        new MapDeleteCommand();

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new MapCreationMenu(), this);
        pm.registerEvents(new StaffWorldManager(), this);
        pm.registerEvents(new FlyCommand(), this);
        pm.registerEvents(new MapsMenu(), this);

        getLogger().info("Hybrid Core system has been SUCCESSFULLY loaded in " + (System.currentTimeMillis() - time) + "ms!");
    }

    @Override
    public void onDisable(){
        INSTANCE = null;
        getLogger().info("Hybrid Core system has SUCCESSFULLY been disabled.");
    }

    public static BuildSystemPlugin getInstance() {
        return INSTANCE;
    }

    public File getMapsFile() {
        return mapsFile;
    }

}
