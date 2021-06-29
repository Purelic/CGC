package net.purelic.CGC.gamemodes.settings.constants;

import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.arguments.standard.StringArgument;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.purelic.CGC.events.GameModeSettingChangeEvent;
import net.purelic.CGC.gamemodes.CustomGameMode;
import net.purelic.CGC.gamemodes.settings.NumberSetting;
import net.purelic.CGC.managers.GameModeManager;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.utils.CommandUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum GameModeNumberSetting implements GameModeSetting {

    SCORE_LIMIT(GameModeSettingType.GENERAL, "Score Limit", "Points to win", 25, 0, 1000, 5, " Point", true),
    ROUNDS(GameModeSettingType.GENERAL, "Rounds", "Number of rounds", 1, 1, 10, 1, " Round", true),
    TIME_LIMIT(GameModeSettingType.GENERAL, "Time Limit", "Match time limit\n0 = no time limit", 10, 0, 180, 1, "m", false),
    OVERTIME(GameModeSettingType.GENERAL, "Overtime", "Overtime if round ends in a draw", 30, 0, 300, 15, "s", false),

    KILL_REWARD_ARROWS(GameModeSettingType.KILL_REWARDS, "Arrows", "Arrows rewarded for an enemy kill", 0, 0, 64, 4, " Arrow", true),
    KILL_REWARD_GAPPLES(GameModeSettingType.KILL_REWARDS, "Gapples", "Gapples rewarded for an enemy kill", 1, 0, 64, 1, " Gapple", true),
    KILL_REWARD_EMERALDS(GameModeSettingType.KILL_REWARDS, "Emeralds", "Emeralds rewarded for an enemy kill", 0, 0, 64, 1, " Emerald", true),
    KILL_REWARD_PEARLS(GameModeSettingType.KILL_REWARDS, "Pearls", "Ender pearls rewarded for an enemy kill", 0, 0, 16, 1, " Pearl", true),

    LIVES_PER_ROUND(GameModeSettingType.RESPAWN, "Lives Per Round", "Number of lives each round\n0 = Unlimited Lives", 0, 0, 10, 1, "", false),
    RESPAWN_TIME(GameModeSettingType.RESPAWN, "Respawn Time", "Seconds to respawn", 5, 0, 60, 1, "s", false),
    RESPAWN_SUICIDE_PENALTY(GameModeSettingType.RESPAWN, "Suicide Penalty", "Additional seconds to respawn if suicided", 5, 0, 60, 1, "s", false),
    RESPAWN_TIME_GROWTH(GameModeSettingType.RESPAWN, "Respawn Time Growth", "Additional seconds to respawn each death", 0, 0, 60, 1, "s", false),
    RESPAWN_MAX_TIME(GameModeSettingType.RESPAWN, "Max Respawn Time", "Max seconds to respawn", 15, 1, 60, 1, "s", false),

    PLAYER_HELMET_PROT(GameModeSettingType.PLAYER_HELMET, "Protection", "Protection enchantment level", 3, 0, 4, 1, "", false),
    PLAYER_CHESTPLATE_PROT(GameModeSettingType.PLAYER_CHESTPLATE, "Protection", "Protection enchantment level", 3, 0, 4, 1, "", false),
    PLAYER_LEGGINGS_PROT(GameModeSettingType.PLAYER_LEGGINGS, "Protection", "Protection enchantment level", 3, 0, 4, 1, "", false),
    PLAYER_BOOTS_PROT(GameModeSettingType.PLAYER_BOOTS, "Protection", "Protection enchantment level", 3, 0, 4, 1, "", false),
    PLAYER_BOOTS_FF(GameModeSettingType.PLAYER_BOOTS, "Feather Falling", "Feather Falling enchantment level", 1, 0, 4, 1, "", false),

    PLAYER_SWORD_SHARP(GameModeSettingType.PLAYER_SWORD, "Sharpness", "Sharpness enchantment level", 2, 0, 5, 1, "", false),
    PLAYER_SWORD_KB(GameModeSettingType.PLAYER_SWORD, "Knockback", "Knockback enchantment level", 0, 0, 2, 1, "", false),

    PLAYER_BOW_POWER(GameModeSettingType.PLAYER_BOW, "Power", "Power enchantment level", 1, 0, 5, 1, "", false),
    PLAYER_BOW_PUNCH(GameModeSettingType.PLAYER_BOW, "Punch", "Punch enchantment level", 0, 0, 2, 1, "", false),

    PLAYER_SHEAR_EFF(GameModeSettingType.PLAYER_SHEARS, "Efficiency", "Efficiency enchantment level", 3, 0, 5, 1, "", false),
    // PLAYER_FLINT_USES(GameModeSettingType.PLAYER_FLINT, "Durability", "Number of uses before breaking\n0 = No Flint & Steel", 0, 0, 64, 1, " Use", true),
    // PLAYER_ROD_USES(GameModeSettingType.PLAYER_ROD, "Durability", "Number of uses before breaking\n0 = No Fishing Rod", 0, 0, 64, 4, " Use", true),

    PLAYER_WOOL(GameModeSettingType.PLAYER_ITEMS, "Wool", "Wool blocks given upon respawn", 0, 0, 64, 4, " Block", true),
    PLAYER_ARROWS(GameModeSettingType.PLAYER_ITEMS, "Arrows", "Arrows given upon respawn", 8, 0, 64, 4, " Arrow", true),
    PLAYER_GAPPLES(GameModeSettingType.PLAYER_ITEMS, "Gapples", "Gapples given upon respawn", 1, 0, 64, 1, " Gapple", true),
    PLAYER_EMERALDS(GameModeSettingType.PLAYER_ITEMS, "Emeralds", "Emeralds given upon respawn", 0, 0, 64, 1, " Emerald", true),
    PLAYER_PEARLS(GameModeSettingType.PLAYER_ITEMS, "Pearls", "Ender pearls given upon respawn", 0, 0, 16, 1, " Pearl", true),

    PLAYER_RESISTANCE(GameModeSettingType.PLAYER_EFFECTS, "Resistance", "Resistance level", 0, 0, 10, 1, "", false),
    PLAYER_SPEED(GameModeSettingType.PLAYER_EFFECTS, "Speed", "Speed level", 0, 0, 10, 1, "", false),
    PLAYER_JUMP_BOOST(GameModeSettingType.PLAYER_EFFECTS, "Jump Boost", "Jump boost level", 0, 0, 10, 1, "", false),
    PLAYER_HASTE(GameModeSettingType.PLAYER_EFFECTS, "Haste", "Haste level", 0, 0, 10, 1, "", false),
    PLAYER_STRENGTH(GameModeSettingType.PLAYER_EFFECTS, "Strength", "Strength level", 0, 0, 10, 1, "", false),

    DEATHMATCH_KILL_POINTS(GameModeSettingType.DEATHMATCH, "Kill Points", "Points for killing an enemy", 1, -50, 50, 1, " Point", true),
    DEATHMATCH_ASSIST_POINTS(GameModeSettingType.DEATHMATCH, "Assist Points", "Points for getting an assist >= 50%", 0, -50, 50, 1, " Point", true),
    DEATHMATCH_DEATH_POINTS(GameModeSettingType.DEATHMATCH, "Death Points", "Points for dying", 0, -50, 50, 1, " Point", true),
    DEATHMATCH_SUICIDE_POINTS(GameModeSettingType.DEATHMATCH, "Suicide Points", "Points for committing suicide", -1, -50, 50, 1, " Point", true),
    // DEATHMATCH_BETRAYAL_POINTS(GameModeSettingType.DEATHMATCH, "Betrayal Points", "Points for killing a teammate", -1, -50, 50, 1, " Point", true),

    HEAD_COLLECTED_POINTS(GameModeSettingType.HEAD_HUNTER, "Head Collected Pts", "Points for collecting a head from your kill", 1, -50, 50, 1, " Point", true),
    HEAD_STOLEN_POINTS(GameModeSettingType.HEAD_HUNTER, "Head Stolen Pts", "Points for stealing a head from an enemy kill", 1, -50, 50, 1, " Point", true),
    HEAD_RECOVERED_POINTS(GameModeSettingType.HEAD_HUNTER, "Head Recovered Pts", "Points for recovering your own head", 1, -50, 50, 1, " Point", true),
    HEAD_COLLECTION_INTERVAL(GameModeSettingType.HEAD_HUNTER, "Collection Interval", "How often heads get collected from inventories\n0 = Disabled", 0, 0, 300, 15, "s", false),

    HILL_CAPTURE_POINTS(GameModeSettingType.KING_OF_THE_HILL, "Capture Points", "Points per second when hill is captured", 1, 0, 50, 1, " Point", true),
    HILL_CAPTURE_DELAY(GameModeSettingType.KING_OF_THE_HILL, "Capture Delay", "Seconds to capture a hill\n0 = Instant", 5, 0, 60, 5, "s", false),
    HILL_CAPTURE_MULTIPLIER(GameModeSettingType.KING_OF_THE_HILL, "Capture Multiplier", "Multiplier when multiple players are capturing a hill\n0 = No Multiplier", 50, 0, 100, 10, "%", false),
    TOTAL_HILLS(GameModeSettingType.KING_OF_THE_HILL, "Total Hills", "Total hills to spawn\n0 = All", 0, 0, 10, 1, " Hill", true),
    HILL_MOVE_INTERVAL(GameModeSettingType.KING_OF_THE_HILL, "Hill Move Interval", "Frequency of hills moving\n0 = Doesn't Move", 0, 0, 120, 15, "s", false),

    FLAG_CARRIER_MAX_HEALTH(GameModeSettingType.FLAG_CARRIER_HEALTH, "Max Health", "Max health when carrying a flag", 10, 1, 20, 1, " ❤", false),
    FLAG_CARRIER_RESISTANCE(GameModeSettingType.FLAG_CARRIER_HEALTH, "Resistance", "Resistance level", 0, 0, 3, 1, "", false),
    FLAG_CARRIER_MELEE_MODIFIER(GameModeSettingType.FLAG_CARRIER_DAMAGE, "Melee Modifier", "Damage multiplier for melee damage", 100, 0, 200, 25, "%", false),
    FLAG_CARRIER_SPEED(GameModeSettingType.FLAG_CARRIER_MOVEMENT, "Speed", "Speed level", 0, 0, 3, 1, "", false),
    FLAG_CARRIER_SLOWNESS(GameModeSettingType.FLAG_CARRIER_MOVEMENT, "Slowness", "Slowness level", 0, 0, 3, 1, "", false),
    FLAG_CARRIER_JUMP_BOOST(GameModeSettingType.FLAG_CARRIER_MOVEMENT, "Jump Boost", "Jump boost level", 0, 0, 3, 1, "", false),

    TOTAL_FLAGS(GameModeSettingType.CAPTURE_THE_FLAG, "Total Flags", "Total flags to spawn\n0 = All", 0, 0, 10, 1, " Flag", true),
    FLAG_RESET_DELAY(GameModeSettingType.CAPTURE_THE_FLAG, "Reset Delay", "Seconds for the flag to reset after being dropped", 30, 0, 180, 5, "s", false),
    FLAG_RESPAWN_DELAY(GameModeSettingType.CAPTURE_THE_FLAG, "Respawn Delay", "Seconds for the flag to respawn after being returned/reset", 10, 0, 180, 5, "s", false),
    FLAG_RETURN_DELAY(GameModeSettingType.CAPTURE_THE_FLAG, "Return Delay", "Seconds to return your flag when standing next to it", 15, 0, 120, 5, "s", false),
    FLAG_RETURN_MULTIPLIER(GameModeSettingType.CAPTURE_THE_FLAG, "Return Multiplier", "Multiplier when multiple players are returning a flag\n0 = No Multiplier", 50, 0, 100, 10, "%", false),
    FLAG_CARRIER_POINTS(GameModeSettingType.CAPTURE_THE_FLAG, "Carrier Points", "Points per second when holding a flag", 0, 0, 10, 1, " Point", true),
    FLAG_COLLECTION_INTERVAL(GameModeSettingType.CAPTURE_THE_FLAG, "Collection Interval", "Seconds to capture flags collected in a goal", 0, 0, 180, 15, "s", false),

    BED_WARS_SUDDEN_DEATH(GameModeSettingType.BED_WARS, "Sudden Death", "All beds break when this many minutes are left\n0 = No Sudden Death", 5, 0, 60, 5, "m", false),

    REFILL_EVENT(GameModeSettingType.SURVIVAL_GAMES, "Refill Event", "How often chests are refilled\n0 = No Refill Events", 3, 0, 60, 1, "m", false),

    WB_MAX_SIZE(GameModeSettingType.WORLD_BORDER, "Max Size", "Max border size", 500, 10, 2000, 100, "", false),
    WB_MIN_SIZE(GameModeSettingType.WORLD_BORDER, "Min Size", "Min border size", 500, 10, 2000, 100, "", false),
    WB_SHRINK_DELAY(GameModeSettingType.WORLD_BORDER, "Shrink Delay", "Delay before the border starts moving\n0 = No Delay", 0, 0, 180, 5, "m", false),
    WB_SHRINK_SPEED(GameModeSettingType.WORLD_BORDER, "Shrink Speed", "Time the border will change from max to min size\n0 = No Change", 0, 0, 180, 5, "m", false),
    ;

    private final GameModeSettingType section;
    private final String defaultName;
    private String name;
    private final String command;
    private final String description;
    private final int defaultValue;
    private final int min;
    private final int max;
    private final int increment;
    private final String suffix;
    private final boolean plural;
    private boolean disabled;

    GameModeNumberSetting(GameModeSettingType section, String name, String description, int defaultValue, int min, int max, int increment, String suffix, boolean plural) {
        this.section = section;
        this.defaultName = name;
        this.name = name;
        this.command = "/gm " + this.name().toLowerCase() + " <game mode> <value>";
        this.description = description;
        this.defaultValue = defaultValue;
        this.min = min;
        this.max = max;
        this.increment = increment;
        this.suffix = suffix;
        this.plural = plural;
        this.disabled = false;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
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
                .argument(IntegerArgument
                    .<CommandSender>newBuilder(settingArg)
                    .withMin(this.min)
                    .withMax(this.max)
                    .asRequired())
                .handler(c -> {
                    Player player = (Player) c.getSender();
                    String gameModeArg = c.get("game mode");
                    int value = c.get(settingArg);

                    CustomGameMode gameMode = GameModeManager.getGameModeByNameOrAlias(gameModeArg);

                    if (gameMode == null) {
                        CommandUtils.sendErrorMessage(player, "Can't find game mode \"" + gameModeArg + "\"!");
                        return;
                    }

                    NumberSetting setting = gameMode.getNumberSetting(this);
                    setting.setValue(value);

                    Commons.callEvent(new GameModeSettingChangeEvent(gameMode, this, value));

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
    public String getKey() {
        return this.name().toLowerCase();
    }

    @Override
    public Object getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public BaseComponent[] getName(CustomGameMode gameMode) {
        ComponentBuilder builder = new ComponentBuilder(this.name + ":\n").strikethrough(this.disabled)
            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.disabled ? "Disabled for this game type" : this.description).create()));

        int current = gameMode.getNumberSetting(this).getValue();

        int minusVal = current - this.increment;
        int plusVal = current + this.increment;

        String minusCmd = this.command.replaceAll("<game mode>", gameMode.getAlias()).replaceAll("<value>", "" + minusVal);
        String plusCmd = this.command.replaceAll("<game mode>", gameMode.getAlias()).replaceAll("<value>", "" + plusVal);

        if (minusVal >= this.min) {
            builder
                .append("- ").reset().color(ChatColor.DARK_RED)
                .event(new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder("-" + this.increment + this.getSuffix() + "\n")
                        .append(minusCmd).italic(true).color(ChatColor.GRAY)
                        .create()))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, minusCmd));
        } else {
            builder
                .append("- ").reset().color(ChatColor.DARK_RED)
                .color(ChatColor.GRAY)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Minimum " + this.min).create()));
        }

        builder.append(current + (!this.plural ? this.getSuffix() : this.getValueSuffix(current))).reset().color(ChatColor.BLACK);

        if (plusVal <= this.max) {
            builder
                .append(" +").color(ChatColor.DARK_GREEN)
                .event(new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder("+" + this.increment + this.getSuffix() + "\n")
                        .append(plusCmd).italic(true).color(ChatColor.GRAY)
                        .create()))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, plusCmd));
        } else {
            builder
                .append(" +").color(ChatColor.DARK_GREEN)
                .color(ChatColor.GRAY)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Maximum " + this.max).create()));
        }

        return builder.append("\n").create();
    }

    @Override
    public int getLines() {
        return 3;
    }

    private String getSuffix() {
        return this.suffix + (this.plural && this.increment != 1 ? "s" : "");
    }

    private String getValueSuffix(int current) {
        return this.suffix + (current != 1 ? "s" : "");
    }

}
