package net.hybrid.buildserver.commands;

import net.hybrid.core.data.Language;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.ItemStackUtil;
import net.hybrid.core.utility.PlayerCommand;
import net.hybrid.core.utility.SoundManager;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SkullCommand extends PlayerCommand {

    public SkullCommand() {
        super("skull", "head");
    }

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isAdmin()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_admin"));
            return;
        }

        if (args.length == 0) {
            hybridPlayer.sendMessage("&cMissing arguments! Use /skull <player>");
            return;
        }

        String name = args[0];

        if (player.getInventory().firstEmpty() == -1) {
            hybridPlayer.sendMessage("&c&lInventory is full!");
            return;
        }

        player.getInventory().addItem(ItemStackUtil.createPlayerSkull(name));
        SoundManager.playSound(player, Sound.NOTE_PLING);
        hybridPlayer.sendMessage("&a&lHEAD FOUND! &aA player head of &e" + name + " &awas added to your inventory.");

    }
}














