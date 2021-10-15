package net.hybrid.buildserver.menus;

import net.hybrid.buildserver.BuildSystemPlugin;
import net.hybrid.buildserver.commands.CreateMapCommand;
import net.hybrid.core.utility.AnvilGUI;
import net.hybrid.core.utility.CC;
import net.hybrid.core.utility.ItemBuilder;
import net.hybrid.core.utility.SoundManager;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public class MapCreationMenu implements Listener {

    public static Inventory getWorldCreationMenu() {
        Inventory inv = Bukkit.createInventory(null, 54, "Map Creation");

        ItemStack overworld = new ItemBuilder(Material.GRASS)
                .setDisplayName("&aOverworld")
                .setLore(
                        CC.translate("&7Create a new map with the"),
                        CC.translate("&7overworld template selected."),
                        CC.translate(""),
                        CC.translate("&eClick to create")
                )
                .build();

        ItemStack nether = new ItemBuilder(Material.NETHER_BRICK)
                .setDisplayName("&aNether")
                .setLore(
                        CC.translate("&7Create a new map with the"),
                        CC.translate("&7nether template selected."),
                        CC.translate(""),
                        CC.translate("&eClick to create")
                )
                .build();

        ItemStack end = new ItemBuilder(Material.ENDER_STONE)
                .setDisplayName("&aThe End")
                .setLore(
                        CC.translate("&7Create a new map with the"),
                        CC.translate("&7end template selected."),
                        CC.translate(""),
                        CC.translate("&eClick to create")
                )
                .build();

        ItemStack flat = new ItemBuilder(Material.DIRT)
                .setDisplayName("&aFlat World")
                .setLore(
                        CC.translate("&7Create a new map with the"),
                        CC.translate("&7flat world template selected."),
                        CC.translate(""),
                        CC.translate("&eClick to create")
                )
                .build();

        ItemStack voidWorld = new ItemBuilder(Material.BEDROCK)
                .setDisplayName("&aVoid World")
                .setLore(
                        CC.translate("&7Create a new map with the"),
                        CC.translate("&7void world template selected."),
                        CC.translate(""),
                        CC.translate("&eClick to create")
                )
                .build();

        ItemStack close = new ItemBuilder(Material.BARRIER)
                .setDisplayName("&cClose")
                .build();


        inv.setItem(11, overworld);
        inv.setItem(13, nether);
        inv.setItem(15, end);
        inv.setItem(30, flat);
        inv.setItem(32, voidWorld);
        inv.setItem(49, close);
        return inv;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (!event.getView().getTitle().equalsIgnoreCase("Map Creation")) return;
        if (event.getCurrentItem() == null) return;
        if (!event.getCurrentItem().hasItemMeta()) return;
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        String displayName = event.getCurrentItem().getItemMeta().getDisplayName();

        if (displayName.equalsIgnoreCase(CC.translate("&aOverworld"))) {
            CreateMapCommand.hashWorldType.put(player.getUniqueId(), "overworld");
        }

        if (displayName.equalsIgnoreCase(CC.translate("&aNether"))) {
            CreateMapCommand.hashWorldType.put(player.getUniqueId(), "nether");
        }

        if (displayName.equalsIgnoreCase(CC.translate("&aThe End"))) {
            CreateMapCommand.hashWorldType.put(player.getUniqueId(), "end");
        }

        if (displayName.equalsIgnoreCase(CC.translate("&aFlat World"))) {
            CreateMapCommand.hashWorldType.put(player.getUniqueId(), "flat");
        }

        if (displayName.equalsIgnoreCase(CC.translate("&aVoid World"))) {
            CreateMapCommand.hashWorldType.put(player.getUniqueId(), "void");
        }

        SoundManager.playSound(player, "CLICK");
        player.sendMessage("§7Enter a valid map name...");

        AnvilGUI gui = new AnvilGUI(player, anvil -> {

            if (anvil.getSlot() == AnvilGUI.AnvilSlot.OUTPUT) {
                anvil.setWillClose(true);
                anvil.setWillDestroy(true);

                String name;

                try {
                    name = anvil.getName();
                } catch (Exception exception) {
                    player.sendMessage("§cInvalid map name! Try again!");
                    return;
                }

                CreateMapCommand.hashName.put(player.getUniqueId(), name.trim());
                player.sendMessage("§aStarting map creation of map §e" + name.trim() + "§a...");
                createWorld(player);

            } else {
                anvil.setWillClose(false);
                anvil.setWillDestroy(false);
            }
        });
        gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, new ItemBuilder(Material.NAME_TAG).setDisplayName(" ").build());
        gui.open();

        if (displayName.equalsIgnoreCase("§cClose")) {
            player.closeInventory();
            player.updateInventory();
            CreateMapCommand.hashWorldType.remove(player.getUniqueId());
        }
    }

    public void createWorld(Player player) {
        String name = CreateMapCommand.hashName.get(player.getUniqueId());
        String type = CreateMapCommand.hashWorldType.get(player.getUniqueId());

        WorldCreator creator = new WorldCreator(name.trim().replace(" ", "_").toLowerCase());

        if (type.equalsIgnoreCase("overworld")) {
            creator.environment(World.Environment.NORMAL);
        }

        if (type.equalsIgnoreCase("nether")) {
            creator.environment(World.Environment.NETHER);
        }

        if (type.equalsIgnoreCase("end")) {
            creator.environment(World.Environment.THE_END);
        }

        if (type.equalsIgnoreCase("flat")) {
            creator.environment(World.Environment.NORMAL);
            creator.type(WorldType.FLAT);
        }

        if (type.equalsIgnoreCase("void")) {
            creator.environment(World.Environment.NORMAL);
            creator.type(WorldType.FLAT);
            creator.generatorSettings("2;0;1;");
        }

        creator.createWorld();

        FileConfiguration config = YamlConfiguration.loadConfiguration(BuildSystemPlugin.getInstance().getMapsFile());
        config.set(name.toLowerCase().replace(" ", "_") + ".type", type);
        config.set(name.toLowerCase().replace(" ", "_") + ".name", name.replace(" ", "_"));
        config.set(name.toLowerCase().replace(" ", "_") + ".displayName", name);
        config.set(name.toLowerCase().replace(" ", "_") + ".creatorUuid", player.getUniqueId().toString());
        config.set(name.toLowerCase().replace(" ", "_") + ".difficulty", Difficulty.PEACEFUL.name());
        config.set(name.toLowerCase().replace(" ", "_") + ".spawnLocation.world", name.replace(" ", "_").toLowerCase());
        config.set(name.toLowerCase().replace(" ", "_") + ".spawnLocation.x", 0);
        config.set(name.toLowerCase().replace(" ", "_") + ".spawnLocation.y", 64);
        config.set(name.toLowerCase().replace(" ", "_") + ".spawnLocation.z", 0);
        config.set(name.toLowerCase().replace(" ", "_") + ".spawnLocation.yaw", 0);
        config.set(name.toLowerCase().replace(" ", "_") + ".spawnLocation.pitch", 0);
        try {
            config.save(BuildSystemPlugin.getInstance().getMapsFile());
        } catch (IOException exception) { exception.printStackTrace(); }

        player.sendMessage(CC.translate("&a&lDONE! &aCreation of " + name.trim() + " has been completed. Teleporting..."));
        SoundManager.playSound(player, "LEVEL_UP");

        new BukkitRunnable(){
            @Override
            public void run() {
                World world = Bukkit.getWorld(name.replace(" ", "_").toLowerCase());

                world.setDifficulty(Difficulty.PEACEFUL);
                world.setThundering(false);
                world.setStorm(false);
                world.setMonsterSpawnLimit(0);
                world.setWeatherDuration(0);
                world.setTime(5000);
                world.setGameRuleValue("doDaylightCycle", "false");

                player.teleport(new Location(world, 0, 65, 0));
                player.setGameMode(GameMode.CREATIVE);
                player.setAllowFlight(true);
                player.setFlying(true);

                for (PotionEffect effect : player.getActivePotionEffects()) {
                    player.removePotionEffect(effect.getType());
                }

                player.getInventory().clear();

            }
        }.runTaskLater(BuildSystemPlugin.getInstance(), 10L);

        CreateMapCommand.hashName.remove(player.getUniqueId());
        CreateMapCommand.hashWorldType.remove(player.getUniqueId());
    }
}







