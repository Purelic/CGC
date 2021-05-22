package net.purelic.CGC.listeners;

import net.md_5.bungee.api.ChatColor;
import net.purelic.commons.Commons;
import net.purelic.commons.utils.*;
import net.purelic.commons.utils.text.TextBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        PlayerUtils.reset(player);

        player.teleport(Commons.getLobby().getSpawnLocation());
        player.setGameMode(GameMode.ADVENTURE);

        this.setSpawnKit(player);
        this.sendHelpMessage(player);
    }

    private void setSpawnKit(Player player) {
        Inventory inventory = player.getInventory();

        // Only apply their kit if their inventory is empty
        if (InventoryUtils.isEmpty(inventory)) return;

        if (CommandUtils.isOp(player)) {
            inventory.addItem(this.getMapsBook(), this.getEditItem(), this.getGameModesBook(), new ItemStack(Material.WOOD_AXE));
        }

        inventory.addItem(new ItemStack(Material.COMPASS));
    }

    private ItemStack getMapsBook() {
        return new ItemCrafter(Material.ENCHANTED_BOOK)
            .name("" + ChatColor.RESET + ChatColor.BOLD + "List Maps" + ChatColor.RESET + ChatColor.GRAY + " (/maps)")
            .command("maps", true)
            .craft();
    }

    private ItemStack getEditItem() {
        return new ItemCrafter(Material.PAPER)
            .name("" + ChatColor.RESET + ChatColor.BOLD + "Edit Map Config" + ChatColor.RESET + ChatColor.GRAY + " (/edit)")
            .command("edit", true)
            .craft();
    }

    private ItemStack getGameModesBook() {
        return new ItemCrafter(Material.BOOK)
            .name("" + ChatColor.RESET + ChatColor.BOLD + "List Game Modes" + ChatColor.RESET + ChatColor.GRAY + " (/gamemodes)")
            .command("gamemodes", true)
            .craft();
    }

    private void sendHelpMessage(Player player) {
        player.sendMessage("");
        player.sendMessage(ChatUtils.getHeader("Need Help?"));
        player.sendMessage(TextBuilder.bullet().build(), TextBuilder.of("/commands").color(ChatColor.AQUA).build(), TextBuilder.of(" - List the basic commands for getting started").build());
        player.sendMessage(TextBuilder.bullet().build(), TextBuilder.of("/docs").color(ChatColor.AQUA).build(), TextBuilder.of(" - Guides for making maps and games").build());
        player.sendMessage(TextBuilder.bullet().build(), TextBuilder.of("/discord").color(ChatColor.AQUA).build(), TextBuilder.of(" - Ask the community or staff for help in Discord").build());
        player.sendMessage(TextBuilder.bullet().build(), TextBuilder.of("/support").color(ChatColor.AQUA).build(), TextBuilder.of(" - Request help/support (notifies online and offline staff)").build());
        player.sendMessage("");
    }

}
