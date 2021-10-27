package net.hybrid.buildserver.commands;

import net.hybrid.core.utility.PlayerCommand;
import net.hybrid.core.utility.SoundManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class BackCommand extends PlayerCommand {

    public BackCommand() {
        super("back");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        if (player.getWorld().getName().equalsIgnoreCase("staffhub")) {
            player.sendMessage("§cYou are already at the staff hub!");
            return;
        }

        player.sendMessage("§7Sending you to the staff hub...");
        SoundManager.playSound(player, "ENDERMAN_TELEPORT");

        Location spawn = new Location(
                Bukkit.getWorld("staffhub"),
                -36.5,
                67,
                -7.5,
                -90,
                0
        );

        player.teleport(spawn);

    }
}















