package net.hybrid.buildserver.commands;

import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.PlayerCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.UUID;

public class FlyCommand extends PlayerCommand implements Listener {

    private final ArrayList<UUID> flyingPlayers;

    public FlyCommand() {
        super("fly");

        flyingPlayers = new ArrayList<>();
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {

        if (flyingPlayers.contains(player.getUniqueId())) {
            player.setAllowFlight(false);
            player.setFlying(false);

            player.sendMessage(CC.translate("&b&lTurned off flight!"));
            flyingPlayers.remove(player.getUniqueId());
        } else {
            player.setAllowFlight(false);
            player.setFlying(true);

            player.sendMessage(CC.translate("&b&lTurned on flight!"));
            flyingPlayers.add(player.getUniqueId());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        flyingPlayers.remove(event.getPlayer().getUniqueId());
    }

}


















