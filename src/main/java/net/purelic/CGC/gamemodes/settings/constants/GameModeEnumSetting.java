package net.purelic.CGC.gamemodes.settings.constants;

import cloud.commandframework.arguments.standard.StringArgument;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.purelic.CGC.events.GameModeSettingChangeEvent;
import net.purelic.CGC.gamemodes.CustomGameMode;
import net.purelic.CGC.gamemodes.constants.*;
import net.purelic.CGC.gamemodes.settings.EnumSetting;
import net.purelic.CGC.managers.GameModeManager;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.YamlUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public enum GameModeEnumSetting implements GameModeSetting {

    GAME_TYPE(GameModeSettingType.GENERAL, "Game Type", "Type of game mode", GameType.DEATHMATCH, GameType.class),
    TEAM_TYPE(GameModeSettingType.GENERAL, "Team Type", "Number of teams\nSolo = No Teams\nTeams = 2 teams\nMulti-Team = 4 teams\nDoubles = 8 teams", TeamType.SOLO, TeamType.class),
    TEAM_SIZE(GameModeSettingType.GENERAL, "Team Size", "Size of teams\nSingles = 1 Player\nDoubles = 2 Players\nTrios = 3 Players\nFives = 5 Players\nMini = 0.5x\nNormal = 1x\nBig = 2x", TeamSize.NORMAL, TeamSize.class),

    WOOL_DROP_TYPE(GameModeSettingType.DEATH_DROPS, "Wool", "Wool blocks dropped upon death", DropType.NONE, DropType.class),
    ARROW_DROP_TYPE(GameModeSettingType.DEATH_DROPS, "Arrows", "Arrows dropped upon death", DropType.HALF, DropType.class),
    GAPPLE_DROP_TYPE(GameModeSettingType.DEATH_DROPS, "Gapples", "Gapples dropped upon death", DropType.NONE, DropType.class),
    EMERALD_DROP_TYPE(GameModeSettingType.DEATH_DROPS, "Emeralds", "Emeralds dropped upon death", DropType.ALL, DropType.class),
    PEARL_DROP_TYPE(GameModeSettingType.DEATH_DROPS, "Pearls", "Ender pearls dropped upon death", DropType.NONE, DropType.class),

    PLAYER_HELMET_TYPE(GameModeSettingType.PLAYER_HELMET, "Armor Type", "Helmet armor type", ArmorType.LEATHER, ArmorType.class),
    PLAYER_CHESTPLATE_TYPE(GameModeSettingType.PLAYER_CHESTPLATE, "Armor Type", "Chestplate armor type", ArmorType.LEATHER, ArmorType.class),
    PLAYER_LEGGINGS_TYPE(GameModeSettingType.PLAYER_LEGGINGS, "Armor Type", "Leggings armor type", ArmorType.LEATHER, ArmorType.class),
    PLAYER_BOOTS_TYPE(GameModeSettingType.PLAYER_BOOTS, "Armor Type", "Boots armor type", ArmorType.LEATHER, ArmorType.class),

    PLAYER_SWORD_TYPE(GameModeSettingType.PLAYER_SWORD, "Sword Type", "Sword material type", SwordType.GOLD, SwordType.class),
    PLAYER_BOW_TYPE(GameModeSettingType.PLAYER_BOW, "Bow Type", "Bow material type", BowType.BOW, BowType.class),
    PLAYER_COMPASS_TYPE(GameModeSettingType.PLAYER_COMPASS, "Tracking Type", "Compass tracking type", CompassTrackingType.PLAYER, CompassTrackingType.class),
    PLAYER_PICKAXE_TYPE(GameModeSettingType.PLAYER_PICKAXE, "Pickaxe Type", "Pickaxe tool type", ToolType.NONE, ToolType.class),
    PLAYER_AXE_TYPE(GameModeSettingType.PLAYER_AXE, "Axe Type", "Axe tool type", ToolType.NONE, ToolType.class),
    PLAYER_SHOVEL_TYPE(GameModeSettingType.PLAYER_SHOVEL, "Shovel Type", "Shovel tool type", ToolType.NONE, ToolType.class),
    PLAYER_FOOD_TYPE(GameModeSettingType.PLAYER_ITEMS, "Food Type", "Food item type", FoodType.GOLDEN_CARROT, FoodType.class, true),
    PLAYER_BLOCK_TYPE(GameModeSettingType.PLAYER_ITEMS, "Block Type", "Block item type", BlockType.WOOL, BlockType.class, true),

    LOOT_TYPE(GameModeSettingType.SURVIVAL_GAMES, "Loot Type", "Chest loot item types", LootType.SG_NORMAL, LootType.class),

    // FLAG_CARRIER_WAYPOINT_VISIBILITY(GameModeSettingType.FLAG_CARRIER_APPEARANCE, "Waypoint Visibility", "Who can see the carrier waypoint", WaypointVisibility.EVERYONE, WaypointVisibility.class),
    ;

    private final GameModeSettingType section;
    private final String defaultName;
    private String name;
    private final String type;
    private final String command;
    private final String description;
    private final String defaultValue;
    private final boolean material;
    private final List<String> options;
    private boolean disabled;

    GameModeEnumSetting(GameModeSettingType section, String name, String description, Object defaultValue, Class<? extends Enum<?>> clazz) {
        this(section, name, description, defaultValue, clazz, false);
    }

    GameModeEnumSetting(GameModeSettingType section, String name, String description, Object defaultValue, Class<? extends Enum<?>> clazz, boolean material) {
        this.section = section;
        this.defaultName = name;
        this.name = name;
        this.type = clazz.getSimpleName().replaceAll("(.)([A-Z])", "$1 $2");
        this.command = "/gm " + this.name().toLowerCase() + " <game mode> <value>";
        this.description = description;
        this.defaultValue = defaultValue.toString();
        this.material = material;
        this.options = Arrays.asList(Arrays.stream(clazz.getEnumConstants()).map(Enum::name).toArray(String[]::new));
        this.disabled = false;
    }

    public List<String> getOptions() {
        return this.options;
    }

    @Override
    public void registerCommand() {
        this.section.addSetting(this);

        String settingName = this.name().toLowerCase();
        String settingArg = settingName.replaceAll("_", " ");

        Commons.registerCommand(mgr ->
            mgr.commandBuilder("gamemode", "gm")
                .senderType(Player.class)
                .permission(Permission.isMapDev(true))
                .literal(settingName)
                .argument(StringArgument.quoted("game mode"))
                .argument(StringArgument.of(settingArg))
                .handler(c -> {
                    Player player = (Player) c.getSender();
                    String gameModeArg = c.get("game mode");
                    String value = c.get(settingArg);

                    CustomGameMode gameMode = GameModeManager.getGameModeByNameOrAlias(gameModeArg);

                    if (gameMode == null) {
                        CommandUtils.sendErrorMessage(player, "Can't find game mode \"" + gameModeArg + "\"!");
                        return;
                    }

                    if (this.material) {
                        try {
                            Material.valueOf(value.toUpperCase());
                        } catch (Exception e) {
                            CommandUtils.sendErrorMessage(player, "Unknown material \"" + value + "\"!");
                        }
                    } else if (!this.options.contains(value.toUpperCase())) {
                        CommandUtils.sendErrorMessage(player, "Can't find " + settingArg + " \"" + value + "\"!");
                        return;
                    }

                    EnumSetting setting = gameMode.getListSetting(this);
                    setting.setValue(value.toUpperCase());

                    Commons.callEvent(new GameModeSettingChangeEvent(gameMode, this, value.toUpperCase()));

                    this.section.openBook(player, gameMode, this);
                }));
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void reset() {
        this.name = this.defaultName;
        this.disabled = false;
    }

    @Override
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public String getKey() {
        return this.name().toLowerCase();
    }

    @Override
    public Object getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public BaseComponent[] getName(CustomGameMode gameMode) {
        String alias = gameMode.getAlias();
        String current = gameMode.getListSetting(this).getValue();

        String prevCmd = this.command.replaceAll("<game mode>", alias).replaceAll("<value>", this.getPreviousOption(current));
        String nextCmd = this.command.replaceAll("<game mode>", alias).replaceAll("<value>", this.getNextOption(current));

        return new ComponentBuilder(this.name + ": \n").strikethrough(this.disabled)
            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(this.disabled ? "Disabled for this game type" : this.description).create()))
            .append("《 ").reset()
            .event(new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("Previous " + this.type + "\n")
                    .append(prevCmd).italic(true).color(ChatColor.GRAY)
                    .create()))
            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, prevCmd))
            .append(YamlUtils.formatEnumString(current)).reset()
            .append(" 》")
            .event(new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("Next " + this.type + "\n")
                    .append(nextCmd).italic(true).color(ChatColor.GRAY)
                    .create()))
            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, nextCmd))
            .append("\n").create();
    }

    @Override
    public int getLines() {
        return 3;
    }

    private String getNextOption(String current) {
        int index = this.options.indexOf(current) + 1;
        index = index == this.options.size() ? 0 : index;
        return this.options.get(index).toLowerCase();
    }

    private String getPreviousOption(String current) {
        int index = this.options.indexOf(current) + -1;
        index = index < 0 ? this.options.size() - 1 : index;
        return this.options.get(index).toLowerCase();
    }

}
