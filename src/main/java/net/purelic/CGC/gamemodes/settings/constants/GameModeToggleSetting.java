package net.purelic.CGC.gamemodes.settings.constants;

import cloud.commandframework.arguments.standard.StringArgument;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.purelic.CGC.events.GameModeSettingChangeEvent;
import net.purelic.CGC.gamemodes.CustomGameMode;
import net.purelic.CGC.gamemodes.settings.ToggleSetting;
import net.purelic.CGC.managers.GameModeManager;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.BooleanArgument;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.utils.CommandUtils;
import org.bukkit.entity.Player;

public enum GameModeToggleSetting implements GameModeSetting {

    TEAM_SWITCHING(GameModeSettingType.GENERAL, "Team Switching", "Are players allowed to switch teams after their initial join", true),
    ATTACK_DEFENSE(GameModeSettingType.GENERAL, "Attack/Defense", "If blue are defenders and red are attackers", false),
    FRIENDLY_FIRE(GameModeSettingType.GENERAL, "Friendly Fire", "Allow friendly fire", false),
    HUNGER(GameModeSettingType.GENERAL, "Hunger Enabled", "Allow players to lose hunger", false),

    DYNAMIC_REGEN(GameModeSettingType.KILL_REWARDS, "Dynamic Regen", "Regen reward based on damage dealt", false),

    BLACKOUT_RESPAWN(GameModeSettingType.RESPAWN, "Blackout Respawn", "Blackout the respawn screen", false),

    DROP_TRADED_ITEMS(GameModeSettingType.DEATH_DROPS, "Drop Traded Items", "If items traded from villagers should drop", false),

    PLAYER_HELMET_LOCKED(GameModeSettingType.PLAYER_HELMET, "Locked", "Helmet can't be removed", false),
    PLAYER_CHESTPLATE_LOCKED(GameModeSettingType.PLAYER_CHESTPLATE, "Locked", "Chestplate can't be removed", false),
    PLAYER_LEGGINGS_LOCKED(GameModeSettingType.PLAYER_LEGGINGS, "Locked", "Leggings can't be removed", false),
    PLAYER_BOOTS_LOCKED(GameModeSettingType.PLAYER_BOOTS, "Locked", "Boots can't be removed", false),

    PLAYER_SWORD_LOCKED(GameModeSettingType.PLAYER_SWORD, "Locked", "Sword can't be dropped", true),
    PLAYER_SWORD_INSTANT_KILL(GameModeSettingType.PLAYER_SWORD, "Instant Kill", "Sword kills enemies in one hit", false),

    PLAYER_BOW_INFINITY(GameModeSettingType.PLAYER_BOW, "Infinity", "Infinity bow enchantment", false),
    PLAYER_BOW_LOCKED(GameModeSettingType.PLAYER_BOW, "Locked", "Bow can't be dropped", true),
    PLAYER_BOW_INSTANT_KILL(GameModeSettingType.PLAYER_BOW, "Instant Kill", "Bow kills enemies in one shot", false),

    PLAYER_SHEARS_ENABLED(GameModeSettingType.PLAYER_SHEARS, "Enabled", "Spawn with shears", false),
    PLAYER_COMPASS_ENABLED(GameModeSettingType.PLAYER_COMPASS, "Enabled", "Spawn with a tracking compass", false),
    PLAYER_COMPASS_DISPLAY(GameModeSettingType.PLAYER_COMPASS, "Display Info", "Display tracking info when holding a compass", false),
    PLAYER_COMPASS_SPAWN_WITH(GameModeSettingType.PLAYER_COMPASS, "Spawn With", "Spawn with a tracking compass", true),

    PLAYER_NATURAL_REGEN(GameModeSettingType.PLAYER_HEALTH, "Natural Regeneration", "Natural regeneration of heath", true),
    PLAYER_IMMUNE_TO_MELEE(GameModeSettingType.PLAYER_HEALTH, "Immune to Melee", "Immune to melee damage", false),
    PLAYER_IMMUNE_TO_PROJECTILES(GameModeSettingType.PLAYER_HEALTH, "Immune to Projectiles", "Immune to projectile damage", false),
    PLAYER_IMMUNE_TO_FALL_DAMAGE(GameModeSettingType.PLAYER_HEALTH, "Immune to Fall Damage", "Immune to fall damage", false),

    PLAYER_INVISIBILITY(GameModeSettingType.PLAYER_EFFECTS, "Invisibility", "Toggle invisibility", false),
    PLAYER_FIRE_RESISTANCE(GameModeSettingType.PLAYER_EFFECTS, "Fire Resistance", "Toggle fire resistance", false),
    PLAYER_BLINDNESS(GameModeSettingType.PLAYER_EFFECTS, "Blindness", "Toggle blindness", false),

    PLAYER_NAME_VISIBLE(GameModeSettingType.PLAYER_APPEARANCE, "Name Visible", "Toggle name tag visibility", true),

    DEATHMATCH_SCOREBOXES(GameModeSettingType.DEATHMATCH, "Scoreboxes", "Allow scoreboxes", false),

    COLLECT_HEADS_INSTANTLY(GameModeSettingType.HEAD_HUNTER, "Collect Instantly", "Instantly score heads when picked up", true),
    HEAD_COLLECTION_HILLS(GameModeSettingType.HEAD_HUNTER, "Collection Hills", "Enable heads to be collected with head collection hills", false),
    PLAYERS_DROP_HEADS(GameModeSettingType.HEAD_HUNTER, "Players Drop Heads", "Player hills drop a head kill reward", true),

    NEUTRAL_HILLS(GameModeSettingType.KING_OF_THE_HILL, "Neutral Hills", "Only spawn neutral hills", true),
    CAPTURE_LOCK(GameModeSettingType.KING_OF_THE_HILL, "Capture Lock", "Hill stays captured after leaving", true),
    RANDOM_HILLS(GameModeSettingType.KING_OF_THE_HILL, "Random Hills", "Hills are selected randomly", false),
    PERMANENT_HILLS(GameModeSettingType.KING_OF_THE_HILL, "Permanent Hills", "Hills cannot be captured", false),
    CAPTURED_HILLS_TELEPORT(GameModeSettingType.KING_OF_THE_HILL, "Captured Hills Teleport", "Teleport to spawn upon entering a captured hill", false),
    SINGLE_CAPTURE_HILLS(GameModeSettingType.KING_OF_THE_HILL, "Single Capture Hills", "Hills become locked after being captured", false),
    ALL_HILLS_WIN(GameModeSettingType.KING_OF_THE_HILL, "All Hills Win", "Capturing all hills will instant win", false),

    FLAG_CARRIER_DISABLE_SPRINTING(GameModeSettingType.FLAG_CARRIER_MOVEMENT, "Disable Sprinting", "Disable sprinting when carrying flag", false),
    NEUTRAL_FLAGS(GameModeSettingType.CAPTURE_THE_FLAG, "Neutral Flags", "Only spawn neutral flags", false),
    FLAG_GOALS(GameModeSettingType.CAPTURE_THE_FLAG, "Flag Goals", "Allow flags to be captured in CTF goals", true),
    MOVING_FLAG(GameModeSettingType.CAPTURE_THE_FLAG, "Moving Flag", "Flag respawns in a random location", false),
    FLAG_AT_HOME(GameModeSettingType.CAPTURE_THE_FLAG, "Flag at Home", "All flags must be returned before capturing", false),
    TELEPORT_ON_CAPTURE(GameModeSettingType.CAPTURE_THE_FLAG, "Teleport on Capture", "Teleports the carrier back to their spawn when they capture a flag", false),
    RESPAWN_ON_DROP(GameModeSettingType.CAPTURE_THE_FLAG, "Respawn on Drop", "Players can't respawn while a teammate is holding a flag", false),

    // BED_WARS_RUSH_MODE(GameModeSettingType.BED_WARS, "Rush Mode", "Spawners are sped up and shop item prices are discounted.", false),

    SPAWN_PROTECTION(GameModeSettingType.WORLD, "Spawn Protection", "Should spawn areas be protected", true),
    MOB_SPAWNING(GameModeSettingType.WORLD, "Mob Spawning", "Allow mob spawning", false),
    FIRE_SPREAD(GameModeSettingType.WORLD, "Fire Spread", "Allow fire spread", false),
    DAYLIGHT_CYCLE(GameModeSettingType.WORLD, "Daylight Cycle", "Toggle daylight cycle when match starts", false),
    BLOCK_DROPS(GameModeSettingType.WORLD, "Block Drops", "Blocks to drop when destroyed", true),
    ENTITY_DROPS(GameModeSettingType.WORLD, "Entity Drops", "Entities that aren't mobs should drop", true),
    LEAVES_DECAY(GameModeSettingType.WORLD, "Leaves Decay", "If leaves should decay", false),
    JUMP_PADS(GameModeSettingType.WORLD, "Jump Pads", "Allow jump pads", true),
    INSTANT_TNT(GameModeSettingType.WORLD, "Instant TNT", "TNT to charge instantly when placed", true),
    RESET_BLOCKS(GameModeSettingType.WORLD, "Reset Blocks", "Resets player placed blocks every round", true),
    ;

    private final GameModeSettingType section;
    private final String defaultName;
    private String name;
    private final String command;
    private final String description;
    private final boolean defaultValue;
    private boolean disabled;

    GameModeToggleSetting(GameModeSettingType section, String name, String description, boolean defaultValue) {
        this.section = section;
        this.defaultName = name;
        this.name = name;
        this.command = "/gm " + this.name().toLowerCase() + " <game mode> <value>";
        this.description = description;
        this.defaultValue = defaultValue;
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
        boolean current = gameMode.getToggleSetting(this).getValue();
        String command = this.command.replaceAll("<game mode>", gameMode.getAlias()).replaceAll("<value>", "" + !current);

        return new ComponentBuilder(this.name + ": \n").strikethrough(this.disabled)
            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.disabled ? "Disabled for this game type" : this.description).create()))
            .append(current ? "⦿" : "⦾").reset()
            .color(current ? ChatColor.DARK_GREEN : ChatColor.DARK_RED)
            .event(new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("Toggle " + (current ? "Off" : "On") + "\n")
                    .append(command).italic(true).color(ChatColor.GRAY)
                    .create()))
            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command))
            .append(current ? " On" : " Off")
            .append("\n")
            .create();
    }

    @Override
    public int getLines() {
        return 3;
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
                .argument(BooleanArgument.of(settingArg))
                .handler(c -> {
                    Player player = (Player) c.getSender();
                    String gameModeArg = c.get("game mode");
                    boolean value = c.get(settingArg);

                    CustomGameMode gameMode = GameModeManager.getGameModeByNameOrAlias(gameModeArg);

                    if (gameMode == null) {
                        CommandUtils.sendErrorMessage(player, "Can't find game mode \"" + gameModeArg + "\"!");
                        return;
                    }

                    ToggleSetting setting = gameMode.getToggleSetting(this);
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
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

}
