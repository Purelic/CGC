package net.purelic.CGC.maps.constants;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;

public enum MatchTeam {

    SOLO(ChatColor.WHITE, DyeColor.WHITE, Color.WHITE),
    BLUE(ChatColor.BLUE, DyeColor.BLUE, Color.BLUE),
    RED(ChatColor.RED, DyeColor.RED, Color.RED),
    GREEN(ChatColor.GREEN, DyeColor.LIME, Color.LIME),
    YELLOW(ChatColor.YELLOW, DyeColor.YELLOW, Color.YELLOW),
    AQUA(ChatColor.AQUA, DyeColor.LIGHT_BLUE, Color.AQUA),
    PINK(ChatColor.LIGHT_PURPLE, DyeColor.PINK, Color.FUCHSIA),
    WHITE(ChatColor.WHITE, DyeColor.BLACK, Color.WHITE),
    GRAY(ChatColor.GRAY, DyeColor.GRAY, Color.GRAY);

    private final ChatColor chatColor;
    private final DyeColor dyeColor;
    private final Color color;

    MatchTeam(ChatColor chatColor, DyeColor dyeColor, Color color) {
        this.chatColor = chatColor;
        this.dyeColor = dyeColor;
        this.color = color;
    }

    public ChatColor getChatColor() {
        return this.chatColor;
    }

    public DyeColor getDyeColor() {
        return this.dyeColor;
    }

    public Color getColor() {
        return this.color;
    }

}
