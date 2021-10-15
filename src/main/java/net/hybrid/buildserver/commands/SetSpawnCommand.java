package net.hybrid.buildserver.commands;

import net.hybrid.buildserver.BuildSystemPlugin;
import net.hybrid.core.data.Language;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import net.hybrid.core.utility.SoundManager;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class SetSpawnCommand extends PlayerCommand {

    public SetSpawnCommand(){
        super("setspawn");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isAdmin()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_admin"));
            return;
        }

        if (player.getWorld().getName().equalsIgnoreCase("staffhub")) {
            hybridPlayer.sendMessage("&cThis command cannot be performed here!");
            return;
        }

        int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockY();
        int z = player.getLocation().getBlockZ();
        String name = player.getWorld().getName() + ".spawnLocation.";

        FileConfiguration config = YamlConfiguration.loadConfiguration(BuildSystemPlugin.getInstance().getMapsFile());
        config.set(name + "x", x);
        config.set(name + "y", y);
        config.set(name + "z", z);
        config.set(name + "yaw", player.getLocation().getYaw());
        config.set(name + "pitch", player.getLocation().getPitch());

        try {
            config.save(BuildSystemPlugin.getInstance().getMapsFile());
        } catch (IOException exception) {
            hybridPlayer.sendMessage("&cSomething went wrong while saving the new spawn location! Please try again.");
        }

        hybridPlayer.sendMessage("&a&lSPAWN UPDATED! &aA new spawn has been set at " +
                "&e" + x + "&a, &e" + y + "&a, &e" + z + "&a.");
        SoundManager.playSound(player, "LEVEL_UP");
    }
}
















