//package net.purelic.CGC.core.maps;
//
//import net.md_5.bungee.api.chat.ClickEvent;
//import net.md_5.bungee.api.chat.ComponentBuilder;
//import net.md_5.bungee.api.chat.HoverEvent;
//import net.purelic.CGC.maps.settings.MapNumberSetting;
//import net.purelic.CGC.maps.settings.MapToggleSetting;
//import net.purelic.commons.utils.book.BookBuilder;
//import net.purelic.commons.utils.book.PageBuilder;
//import org.bukkit.Sound;
//import org.bukkit.entity.Player;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class GeneralSettings {
//
//    private int minBuildLimit;
//    private int maxBuildLimit;
//    private boolean blockProtection;
//    private boolean blockPlacement;
//    private boolean nightVision;
//
//    private final MapNumberSetting minBuildLimitSetting;
//    private final MapNumberSetting maxBuildLimitSetting;
//    private final MapToggleSetting blockProtectionSetting;
//    private final MapToggleSetting blockPlacementSetting;
//    private final MapToggleSetting nightVisionSetting;
//
//    public GeneralSettings() {
//        this(0, 100, true, true, false);
//    }
//
//    public GeneralSettings(Map<String, Object> data) {
//        this(
//            (int) data.getOrDefault("min_build_limit", 0),
//            (int) data.getOrDefault("max_build_limit", 100),
//            (boolean) data.getOrDefault("block_protection", true),
//            (boolean) data.getOrDefault("block_placement", false),
//            (boolean) data.getOrDefault("night_vision", false)
//        );
//    }
//
//    public GeneralSettings(int minBuildLimit, int maxBuildLimit, boolean blockProtection, boolean blockPlacement, boolean nightVision) {
//        this.minBuildLimit = minBuildLimit;
//        this.maxBuildLimit = maxBuildLimit;
//        this.blockProtection = blockProtection;
//        this.blockPlacement = blockPlacement;
//        this.nightVision = nightVision;
//
//        this.minBuildLimitSetting = new MapNumberSetting(
//            "Min. Build Limit",
//            "Minimum y-level for placing blocks",
//            "/settings min_build_limit <value>",
//            this.minBuildLimit,
//            "",
//            0,
//            250,
//            25,
//            false);
//
//        this.maxBuildLimitSetting = new MapNumberSetting(
//            "Max Build Limit",
//            "Maximum y-level for placing blocks",
//            "/settings max_build_limit <value>",
//            this.maxBuildLimit,
//            "",
//            0,
//            250,
//            25,
//            false);
//
//
//        this.blockProtectionSetting = new MapToggleSetting(
//            "Block Protection",
//            "If players can break non-player placed blocks",
//            "/settings allow_block_breaking <value>",
//            this.blockProtection);
//
//        this.blockPlacementSetting = new MapToggleSetting(
//            "Block Placement",
//            "If players can place blocks",
//            "/settings allow_block_placement <value>",
//            this.blockPlacement);
//
//        this.nightVisionSetting = new MapToggleSetting(
//            "Night Vision",
//            "If the player should spawn with night vision",
//            "/settings night_vision <value>",
//            this.nightVision);
//    }
//
//    public void setMinBuildLimit(int limit) {
//        this.minBuildLimit = limit;
//        this.minBuildLimitSetting.setCurrent(limit);
//    }
//
//    public MapNumberSetting getMinBuildLimitSetting() {
//        return this.minBuildLimitSetting;
//    }
//
//    public void setMaxBuildLimit(int limit) {
//        this.maxBuildLimit = limit;
//        this.maxBuildLimitSetting.setCurrent(limit);
//    }
//
//    public MapNumberSetting getMaxBuildLimitSetting() {
//        return this.maxBuildLimitSetting;
//    }
//
//    public void setBlockProtection(boolean blockProtection) {
//        this.blockProtection = blockProtection;
//        this.blockProtectionSetting.setCurrent(blockProtection);
//    }
//
//    public MapToggleSetting getBlockProtectionSetting() {
//        return this.blockProtectionSetting;
//    }
//
//    public void setBlockPlacement(boolean blockPlacement) {
//        this.blockPlacement = blockPlacement;
//        this.blockPlacementSetting.setCurrent(blockPlacement);
//    }
//
//    public MapToggleSetting getBlockPlacementSetting() {
//        return this.blockPlacementSetting;
//    }
//
//    public void setNightVision(boolean nightVision) {
//        this.nightVision = nightVision;
//        this.nightVisionSetting.setCurrent(nightVision);
//    }
//
//    public Map<String, Object> toYaml() {
//        Map<String, Object> spawner = new HashMap<>();
//        if (this.minBuildLimit != 0) spawner.put("min_build_limit", this.minBuildLimit);
//        if (this.maxBuildLimit != 100) spawner.put("max_build_limit", this.maxBuildLimit);
//        if (!this.blockProtection) spawner.put("block_protection", false);
//        if (this.blockPlacement) spawner.put("block_placement", true);
//        if (this.nightVision) spawner.put("block_placement", true);
//        return spawner;
//    }
//
//    public void openBook(Player player) {
//        new BookBuilder().pages(
//            new PageBuilder()
//                .add(new ComponentBuilder("⬅ ")
//                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Back").create()))
//                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/edit"))
//                    .bold(true)
//                    .create())
//                .add("General Settings").newLine().newLine()
//                .add(this.minBuildLimitSetting.getName())
//                .add(this.minBuildLimitSetting.getValue(0)).newLine().newLine()
//                .add(this.maxBuildLimitSetting.getName())
//                .add(this.maxBuildLimitSetting.getValue(0)).newLine().newLine()
//                .add(this.blockProtectionSetting.getName())
//                .add(this.blockProtectionSetting.getValue(0)).newLine().newLine()
//                .add(this.blockPlacementSetting.getName())
//                .add(this.blockPlacementSetting.getValue(0)).newLine().newLine()
//                .add(this.nightVisionSetting.getName())
//                .add(this.nightVisionSetting.getValue(0))
//                .build())
//            .open(player);
//
//        player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
//    }
//
//}
