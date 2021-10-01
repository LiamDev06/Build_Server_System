package net.hybrid.buildserver.managers;

import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.SoundManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class StaffWorldManager implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        Location spawn = new Location(Bukkit.getWorld("staffhub"), -26.5, 66, -9.5, -90, 0);
        player.teleport(spawn);

        SoundManager.playSound(player, Sound.ENDERMAN_TELEPORT);
        SoundManager.playSound(player, Sound.LEVEL_UP);
        player.sendMessage(CC.translate("&6Welcome to the Staff Hub, " + player.getName() + "!"));

        player.getInventory().clear();
        player.setLevel(0);
        player.setExp(0);
        player.setGameMode(GameMode.ADVENTURE);
        player.setAllowFlight(true);
        player.setFlying(false);

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        player.getInventory().setHelmet(new ItemStack(Material.AIR));
        player.getInventory().setChestplate(new ItemStack(Material.AIR));
        player.getInventory().setLeggings(new ItemStack(Material.AIR));
        player.getInventory().setBoots(new ItemStack(Material.AIR));

        for (Player target : Bukkit.getWorld("staffhub").getPlayers()) {
            target.sendMessage(CC.translate(
                    hybridPlayer.getRankManager().getRank().getPrefixSpace() + player.getName()
                    + " &ajoined the staff hub!"
            ));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        HybridPlayer hybridPlayer = new HybridPlayer(event.getPlayer().getUniqueId());

        for (Player target : Bukkit.getWorld("staffhub").getPlayers()) {
            target.sendMessage(CC.translate(
                    hybridPlayer.getRankManager().getRank().getPrefixSpace() + event.getPlayer().getName()
                            + " &eleft the staff hub!"
            ));
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            if (!event.getEntity().getWorld().getName().equalsIgnoreCase("staffhub")) return;

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void playerPortal(PlayerPortalEvent event) {
        if (!event.getPlayer().getWorld().getName().equalsIgnoreCase("staffhub")) return;

        event.setCancelled(true);
        HybridPlayer hybridPlayer = new HybridPlayer(event.getPlayer().getUniqueId());
        hybridPlayer.sendMessage("&aTeleporting to main lobby...");
        hybridPlayer.sendToServer("mainlobby1");
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (!event.getEntity().getWorld().getName().equalsIgnoreCase("staffhub")) return;

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        HybridPlayer hybridPlayer = new HybridPlayer(event.getPlayer().getUniqueId());
        if (hybridPlayer.getRankManager().isAdmin()) return;
        if (!event.getPlayer().getWorld().getName().equalsIgnoreCase("staffhub")) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        HybridPlayer hybridPlayer = new HybridPlayer(event.getPlayer().getUniqueId());
        if (hybridPlayer.getRankManager().isAdmin()) return;
        if (!event.getPlayer().getWorld().getName().equalsIgnoreCase("staffhub")) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onChangeToHub(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (!player.getWorld().getName().equalsIgnoreCase("staffhub")) return;

        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        Location spawn = new Location(Bukkit.getWorld("staffhub"), -26.5, 66, -9.5, -90, 0);
        player.teleport(spawn);

        SoundManager.playSound(player, Sound.ENDERMAN_TELEPORT);
        SoundManager.playSound(player, Sound.LEVEL_UP);
        player.sendMessage(CC.translate("&6Welcome to the Staff Hub, " + player.getName() + "!"));

        player.getInventory().clear();
        player.setLevel(0);
        player.setExp(0);
        player.setGameMode(GameMode.ADVENTURE);
        player.setAllowFlight(true);
        player.setFlying(false);

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        player.getInventory().setHelmet(new ItemStack(Material.AIR));
        player.getInventory().setChestplate(new ItemStack(Material.AIR));
        player.getInventory().setLeggings(new ItemStack(Material.AIR));
        player.getInventory().setBoots(new ItemStack(Material.AIR));

        for (Player target : Bukkit.getWorld("staffhub").getPlayers()) {
            target.sendMessage(CC.translate(
                    hybridPlayer.getRankManager().getRank().getPrefixSpace() + player.getName()
                            + " &ajoined the staff hub!"
            ));
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (player.getWorld().getName().equalsIgnoreCase("staffhub")) return;

        Bukkit.unloadWorld(event.getFrom(), true);

        player.getInventory().clear();
        player.setLevel(0);
        player.setExp(0);
        player.setGameMode(GameMode.CREATIVE);
        player.setAllowFlight(true);
        player.setFlying(true);

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        player.getInventory().setHelmet(new ItemStack(Material.AIR));
        player.getInventory().setChestplate(new ItemStack(Material.AIR));
        player.getInventory().setLeggings(new ItemStack(Material.AIR));
        player.getInventory().setBoots(new ItemStack(Material.AIR));
    }

}





