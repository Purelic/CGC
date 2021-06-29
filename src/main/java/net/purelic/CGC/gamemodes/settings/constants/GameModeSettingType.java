package net.purelic.CGC.gamemodes.settings.constants;

import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.arguments.standard.StringArgument;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.purelic.CGC.gamemodes.CustomGameMode;
import net.purelic.CGC.gamemodes.constants.GameType;
import net.purelic.CGC.gamemodes.settings.Setting;
import net.purelic.CGC.managers.GameModeManager;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.utils.CommandUtils;
import org.bukkit.entity.Player;

import java.util.*;

public enum GameModeSettingType implements GameModeSetting {

    GENERAL(null, "General"),
    KILL_REWARDS(GENERAL, "Kill Rewards"),
    DEATH_DROPS(GENERAL, "Death Drops"),
    RESPAWN(null, "Respawn"),
    PLAYER_TRAITS(null, "Player Traits"),
    PLAYER_SPAWN_KIT(PLAYER_TRAITS, "Spawn Kit"),
    PLAYER_ARMOR(PLAYER_SPAWN_KIT, "Armor"),
    PLAYER_HELMET(PLAYER_ARMOR, "Helmet"),
    PLAYER_CHESTPLATE(PLAYER_ARMOR, "Chestplate"),
    PLAYER_LEGGINGS(PLAYER_ARMOR, "Leggings"),
    PLAYER_BOOTS(PLAYER_ARMOR, "Boots"),
    PLAYER_SWORD(PLAYER_SPAWN_KIT, "Sword"),
    PLAYER_BOW(PLAYER_SPAWN_KIT, "Bow"),
    PLAYER_TOOLS(PLAYER_SPAWN_KIT, "Tools"),
    PLAYER_SHEARS(PLAYER_TOOLS, "Shears"),
    PLAYER_COMPASS(PLAYER_TOOLS, "Tracking Compass"),
    // PLAYER_FLINT(PLAYER_TOOLS, "Flint & Steel"),
    // PLAYER_ROD(PLAYER_TOOLS, "Fishing Rod"),
    PLAYER_ITEMS(PLAYER_SPAWN_KIT, "Items"),
    PLAYER_HEALTH(PLAYER_TRAITS, "Health"),
    PLAYER_EFFECTS(PLAYER_TRAITS, "Effects"),
    PLAYER_APPEARANCE(PLAYER_TRAITS, "Appearance"),
    DEATHMATCH(null, "Deathmatch", GameType.DEATHMATCH, GameType.HEAD_HUNTER, GameType.KING_OF_THE_HILL),
    HEAD_HUNTER(null, "Head Hunter", GameType.HEAD_HUNTER),
    CAPTURE_THE_FLAG(null, "Capture the Flag", GameType.CAPTURE_THE_FLAG),
    FLAG_CARRIER_TRAITS(CAPTURE_THE_FLAG, "Carrier Traits", GameType.CAPTURE_THE_FLAG),
    FLAG_CARRIER_HEALTH(FLAG_CARRIER_TRAITS, "Health", GameType.CAPTURE_THE_FLAG),
    FLAG_CARRIER_DAMAGE(FLAG_CARRIER_TRAITS, "Damage", GameType.CAPTURE_THE_FLAG),
    FLAG_CARRIER_MOVEMENT(FLAG_CARRIER_TRAITS, "Movement", GameType.CAPTURE_THE_FLAG),
    KING_OF_THE_HILL(null, "King of the Hill", GameType.DEATHMATCH, GameType.KING_OF_THE_HILL, GameType.HEAD_HUNTER, GameType.CAPTURE_THE_FLAG),
    BED_WARS(null, "Bed Wars", GameType.BED_WARS),
    SURVIVAL_GAMES(null, "Survival Games", GameType.SURVIVAL_GAMES),
    WORLD(null, "World"),
    WORLD_BORDER(WORLD, "World Border"),
//    INFECTION(null, GameType.INFECTION),
//    INFECTED_TRAITS(INFECTION),
//    INFECTED_SPAWN_KIT(INFECTED_TRAITS),
//    INFECTED_ARMOR(INFECTED_SPAWN_KIT),
//    INFECTED_HELMET(INFECTED_ARMOR),
//    INFECTED_CHESTPLATE(INFECTED_ARMOR),
//    INFECTED_LEGGINGS(INFECTED_ARMOR),
//    INFECTED_BOOTS(INFECTED_ARMOR),
//    INFECTED_SWORD(INFECTED_SPAWN_KIT),
//    INFECTED_BOW(INFECTED_SPAWN_KIT),
//    INFECTED_TOOLS(INFECTED_SPAWN_KIT),
//    INFECTED_ITEMS(INFECTED_SPAWN_KIT),
//    INFECTED_HEALTH(INFECTED_TRAITS),
//    INFECTED_MOVEMENT(INFECTED_TRAITS),
//    INFECTED_APPEARANCE(INFECTED_TRAITS),
    ;

    private final GameModeSettingType parent;
    private final String defaultName;
    private String name;
    private final Set<GameType> gameTypes;
    private final List<GameModeSetting> settings;
    private List<List<GameModeSetting>> pages;
    private boolean disabled;

    GameModeSettingType(GameModeSettingType parent, String name) {
        this(parent, name, GameType.values());
    }

    GameModeSettingType(GameModeSettingType parent, String name, GameType... gameTypes) {
        this.parent = parent;
        this.defaultName = name;
        this.name = name;
        this.gameTypes = new HashSet<>(Arrays.asList(gameTypes));
        this.settings = new ArrayList<>();
        this.pages = new ArrayList<>();
        this.disabled = false;
    }

    public boolean hasParent() {
        return parent != null;
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

    public boolean isDisabled() {
        return this.disabled;
    }

    public boolean isValid(GameType gameType) {
        return this.gameTypes.contains(gameType);
    }

    public List<GameModeSetting> getSettings() {
        return this.settings;
    }

    public void addSetting(GameModeSetting setting) {
        this.settings.add(setting);
        this.updatePages();
    }

    public Map<String, Object> toYaml(CustomGameMode gameMode) {
        Map<String, Object> data = new LinkedHashMap<>();

        for (GameModeSetting gameModeSetting : this.settings) {
            Object val;

            if (gameModeSetting instanceof GameModeSettingType) {
                val = ((GameModeSettingType) gameModeSetting).toYaml(gameMode);
            } else if (gameModeSetting == GameModeEnumSetting.GAME_TYPE) {
                val = null;
            } else {
                Setting setting = gameMode.getSetting(gameModeSetting);
                val = setting.isDefault() ? null : setting.getValue();
            }

            if (val != null) {
                data.put(gameModeSetting.getKey(), val);
            }
        }

        return data.size() == 0 ? null : data;
    }

    public void updatePages() {
        int lines = 0;
        int index = 0;

        this.pages = new ArrayList<>();
        this.pages.add(new ArrayList<>());

        for (GameModeSetting setting : this.settings) {
            lines += setting.getLines();

            if (lines > 12) {
                this.pages.add(new ArrayList<>());
                lines = setting.getLines();
                index++;
            }

            this.pages.get(index).add(setting);
        }
    }

    @Override
    public void registerCommand() {
        if (this.hasParent()) {
            this.parent.addSetting(this);
        }

        Commons.registerCommand(mgr ->
            mgr.commandBuilder("gamemode", "gm")
                .senderType(Player.class)
                .permission(Permission.isMapDev(true))
                .literal(this.name().toLowerCase())
                .argument(StringArgument.quoted("game mode"))
                .argument(IntegerArgument.optional("page", 1))
                .handler(c -> {
                    Player player = (Player) c.getSender();
                    String gameModeArg = c.get("game mode");
                    int page = c.get("page");

                    if (page < 1 || page > this.pages.size()) {
                        page = 1;
                    }

                    CustomGameMode gameMode = GameModeManager.getGameModeByNameOrAlias(gameModeArg);

                    if (gameMode == null) {
                        CommandUtils.sendErrorMessage(player, "Can't find game mode \"" + gameModeArg + "\"!");
                        return;
                    }

                    this.openBook(player, gameMode, page);
                }));
    }

    public void openBook(Player player, CustomGameMode gameMode, GameModeSetting setting) {
        this.openBook(player, gameMode, this.getPageOfSetting(setting));
    }

    private int getPageOfSetting(GameModeSetting setting) {
        int page = 1;

        for (List<GameModeSetting> settings : this.pages) {
            if (settings.contains(setting)) return page;
            else page += 1;
        }

        return 1;
    }

    public void openBook(Player player, CustomGameMode gameMode, int page) {
        ComponentBuilder header = new ComponentBuilder("");

        if (page == 1) {
            String backCmd = "/gm " + (this.parent == null ? "edit" : this.parent.name().toLowerCase()) + " " + gameMode.getAlias();
            header
                .append("<- Back")
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Back").create()))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, backCmd));
        } else {
            String backCmd = "/gm " + this.name().toLowerCase() + " " + gameMode.getAlias() + " " + (page - 1);
            header
                .append("<- Page " + (page - 1))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Back").create()))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, backCmd));
        }

        if (page != this.pages.size() && this.pages.size() > 0) {
            header
                .append("  |  ").reset()
                .append("Page " + (page + 1) + " ->")
                .event(new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder("Next").create()))
                .event(new ClickEvent(
                    ClickEvent.Action.RUN_COMMAND,
                    "/gm " + this.name().toLowerCase() + " " + gameMode.getAlias() + " " + (page + 1)));
        }

        gameMode.openSettingsBook(
            player,
            header.create(),
            this.pages.size() == 0 ? new ArrayList<>() : this.pages.get(page - 1));
    }

    @Override
    public String getKey() {
        return this.name().toLowerCase();
    }

    @Override
    public Object getDefaultValue() {
        return null;
    }

    @Override
    public BaseComponent[] getName(CustomGameMode gameMode) {
        String command = "/gm " + this.name().toLowerCase() + " " + gameMode.getAlias();

        return new ComponentBuilder(this.name + " ➜").strikethrough(this.disabled)
            .event(new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("Edit " + this.name + " Settings\n")
                    .append(command).italic(true).color(ChatColor.GRAY)
                    .create()))
            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command))
            .append("\n")
            .create();
    }

    @Override
    public int getLines() {
        return 2;
    }

}
