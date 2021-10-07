package net.hybrid.buildserver.commands;

import net.hybrid.buildserver.BuildSystemPlugin;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import net.hybrid.core.utility.SoundManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class MapDeleteCommand extends PlayerCommand {

    public MapDeleteCommand() {
        super("deletemap", "mapdelete", "removemap");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isAdmin()) {
            hybridPlayer.sendMessage("&cThis command can only be performed by an admin or above!");
            return;
        }

        if (args.length == 0) {
            hybridPlayer.sendMessage("&cMissing arguments! Use /mapdelete <map> to specify which map to delete.");
            return;
        }

        String name;

        try {
            StringBuilder builder = new StringBuilder();

            for (String s : args) {
                builder.append(s).append(" ");
            }

            name = builder.toString();
        } catch (Exception exception) {
            hybridPlayer.sendMessage("&cInvalid name! Try again and use /map <name>");
            return;
        }

        final String finalWorldName = name.replace(" ", "_").substring(0, name.replace(" ", "_").length() - 1);
        FileConfiguration config = YamlConfiguration.loadConfiguration(BuildSystemPlugin.getInstance().getMapsFile());
        if (config.getConfigurationSection(finalWorldName.toLowerCase()) == null) {
            hybridPlayer.sendMessage("&cThis map does not seem exist. Are you sure you typed the name right?");
            return;
        }

        if (finalWorldName.equalsIgnoreCase(player.getWorld().getName())) {
            hybridPlayer.sendMessage("&cYou are in this map which makes it un-deletable! Perform &6/map staffhub &cto leave!");
            return;
        }

        config.set(finalWorldName, null);
        try {
            config.save(BuildSystemPlugin.getInstance().getMapsFile());
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        World targetDelete = Bukkit.getWorld(finalWorldName);
        targetDelete.setAutoSave(false);

        File deleteFolder = targetDelete.getWorldFolder();
        deleteWorld(deleteFolder);

        SoundManager.playSound(player, Sound.NOTE_PLING);
        hybridPlayer.sendMessage("&a&lMAP DELETED! &aThe map &e" + name.trim() + " &ahas been deleted.");
    }

    public void deleteWorld(File path) {
        if(path.exists()) {
            File[] files = path.listFiles();

            for (int i = 0; i< Objects.requireNonNull(files).length; i++) {
                if (files[i].isDirectory()) {
                    deleteWorld(files[i]);

                } else {
                    files[i].delete();
                }
            }
        }

        path.delete();
    }
}

















