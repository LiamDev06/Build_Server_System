package net.hybrid.buildserver.commands;

import net.hybrid.buildserver.menus.MapCreationMenu;
import net.hybrid.core.data.Language;
import net.hybrid.core.utility.HybridPlayer;
import net.hybrid.core.utility.PlayerCommand;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class CreateMapCommand extends PlayerCommand {

    public CreateMapCommand() {
        super("createmap");
    }

    public static HashMap<UUID, String> hashWorldType = new HashMap<>();
    public static HashMap<UUID, String> hashName = new HashMap<>();

    @Override
    public void onPlayerCommand(Player player, String[] args) {
        HybridPlayer hybridPlayer = new HybridPlayer(player.getUniqueId());
        if (!hybridPlayer.getRankManager().isAdmin()) {
            hybridPlayer.sendMessage(Language.get(player.getUniqueId(), "requires_admin"));
            return;
        }

        hashName.remove(player.getUniqueId());
        hashWorldType.remove(player.getUniqueId());
        hybridPlayer.sendMessage("&7Starting world creation...");
        player.openInventory(MapCreationMenu.getWorldCreationMenu());
    }
}









