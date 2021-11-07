package net.hybrid.buildserver.commands;

import net.hybrid.buildserver.BuildSystemPlugin;
import net.hybrid.buildserver.menus.MapsMenu;
import net.hybrid.core.data.Language;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import net.hybrid.core.utility.SoundManager;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class MapCommand extends PlayerCommand {

    public MapCommand() {
        super("map", "world", "worlds", "maps");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isAdmin()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_admin"));
            return;
        }

        if (args.length == 0) {
            SoundManager.playSound(player, "CLICK");
            player.openInventory(MapsMenu.getMapsMenu());
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
            hybridPlayer.sendMessage("&cYou are already in this map!");
            return;
        }

        Bukkit.createWorld(new WorldCreator(finalWorldName));

        World world = Bukkit.getWorld(config.getString(finalWorldName + ".spawnLocation.world"));
        world.setDifficulty(Difficulty.PEACEFUL);
        world.setThundering(false);
        world.setStorm(false);
        world.setMonsterSpawnLimit(0);
        world.setWeatherDuration(0);
        world.setTime(5000);
        world.setGameRuleValue("doDaylightCycle", "false");

        int x = config.getInt(finalWorldName + ".spawnLocation.x");
        int y = config.getInt(finalWorldName + ".spawnLocation.y");
        int z = config.getInt(finalWorldName + ".spawnLocation.z");
        int yaw = config.getInt(finalWorldName + ".spawnLocation.yaw");
        int pitch = config.getInt(finalWorldName + ".spawnLocation.pitch");

        Location spawn = new Location(world, x, y, z, yaw, pitch);
        player.teleport(spawn);

        SoundManager.playSound(player, "NOTE_PLING");
        hybridPlayer.sendMessage("&a&lMAP FOUND! &aThe map &e" + name.trim() + " &awas found, teleporting you there...");
        player.setGameMode(GameMode.CREATIVE);
        player.setFlying(true);
        player.setAllowFlight(true);
        player.getInventory().clear();

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

    }
}















