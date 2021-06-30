package net.purelic.CGC.gamemodes;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.purelic.CGC.events.GameModeSettingChangeEvent;
import net.purelic.CGC.gamemodes.constants.GameType;
import net.purelic.CGC.gamemodes.settings.EnumSetting;
import net.purelic.CGC.gamemodes.settings.NumberSetting;
import net.purelic.CGC.gamemodes.settings.Setting;
import net.purelic.CGC.gamemodes.settings.ToggleSetting;
import net.purelic.CGC.gamemodes.settings.constants.*;
import net.purelic.commons.Commons;
import net.purelic.commons.utils.book.BookBuilder;
import net.purelic.commons.utils.book.PageBuilder;
import org.bukkit.entity.Player;
import shaded.com.google.cloud.Timestamp;
import shaded.com.google.cloud.firestore.QueryDocumentSnapshot;

import java.util.*;

@SuppressWarnings("unchecked")
public class CustomGameMode {

    private final String id;
    private UUID author;
    private final Timestamp created;
    private final Timestamp updated;
    private boolean isPublic;
    private String name;
    private String alias;
    private GameType gameType;
    private String description;
    private long downloads;
    private final Map<String, NumberSetting> numberSettings;
    private final Map<String, ToggleSetting> toggleSettings;
    private final Map<String, EnumSetting> listSettings;
    private boolean unsavedChanges;

    public CustomGameMode(Player player, String name, String alias, GameType gameType) {
        this.id = UUID.randomUUID().toString();
        this.author = player.getUniqueId();
        this.created = Timestamp.now();
        this.updated = this.created;
        this.isPublic = false;
        this.name = name;
        this.alias = alias.toUpperCase();
        this.gameType = gameType;
        this.description = "";
        this.downloads = 0L;
        this.numberSettings = new HashMap<>();
        this.toggleSettings = new HashMap<>();
        this.listSettings = new HashMap<>();
        this.unsavedChanges = true;
        this.loadSettings(new HashMap<>());
    }

    public CustomGameMode(Player player, String name, String alias, Map<String, Object> yaml) {
        this.id = UUID.randomUUID().toString();
        this.author = player.getUniqueId();
        this.created = Timestamp.now();
        this.updated = this.created;
        this.isPublic = (boolean) yaml.get("public");
        this.name = name;
        this.alias = alias.toUpperCase();
        this.gameType = GameType.valueOf(((String) yaml.get("game_type")).toUpperCase());
        this.description = (String) yaml.getOrDefault("description", "");
        this.downloads = 0L;
        this.numberSettings = new HashMap<>();
        this.toggleSettings = new HashMap<>();
        this.listSettings = new HashMap<>();
        this.unsavedChanges = true;
        this.loadSettings((Map<String, Object>) yaml.getOrDefault("settings", new HashMap<>()));
    }

    public CustomGameMode(QueryDocumentSnapshot documentSnapshot) {
        this(documentSnapshot.getId(), documentSnapshot.getData());
    }

    public CustomGameMode(String id, Map<String, Object> yaml) {
        this.id = id;
        this.author = UUID.fromString((String) yaml.get("author"));
        this.created = (Timestamp) yaml.get("created");
        this.updated = (Timestamp) yaml.get("updated");
        this.isPublic = (boolean) yaml.get("public");
        this.name = (String) yaml.get("name");
        this.alias = (String) yaml.get("alias");
        this.gameType = GameType.valueOf(((String) yaml.get("game_type")).toUpperCase());
        this.description = (String) yaml.getOrDefault("description", "");
        Object downloads = yaml.getOrDefault("downloads", 0L);
        this.downloads = (long) (downloads == null ? 0L : downloads); // fixes downloads set to null
        this.numberSettings = new HashMap<>();
        this.toggleSettings = new HashMap<>();
        this.listSettings = new HashMap<>();
        this.unsavedChanges = false;
        this.loadSettings((Map<String, Object>) yaml.get("settings"));
    }

    private void loadSettings(Map<String, Object> settings) {
        for (GameModeSettingType section : GameModeSettingType.values()) {
            if (section.hasParent()) continue;
            this.loadSettings(section, settings);
        }

        Commons.callEvent(new GameModeSettingChangeEvent(
            this, GameModeEnumSetting.GAME_TYPE, this.gameType.name(), true));
    }

    private void loadSettings(GameModeSettingType section, Map<String, Object> settings) {
        settings = (Map<String, Object>) settings.getOrDefault(section.name().toLowerCase(), new HashMap<>());

        for (GameModeSetting setting : section.getSettings()) {
            String key = setting.getKey();
            Object val;

            if (setting == GameModeEnumSetting.GAME_TYPE) {
                val = this.gameType.name();
            } else {
                val = settings.getOrDefault(key, setting.getDefaultValue());
            }

            if (setting instanceof GameModeNumberSetting) {
                this.numberSettings.put(key, new NumberSetting(key, val));
            } else if (setting instanceof GameModeToggleSetting) {
                this.toggleSettings.put(key, new ToggleSetting(key, val));
            } else if (setting instanceof GameModeEnumSetting) {
                this.listSettings.put(key, new EnumSetting(key, val));
            } else if (setting instanceof GameModeSettingType) {
                this.loadSettings((GameModeSettingType) setting, settings);
            }
        }
    }

    public String getId() {
        return this.id;
    }

    public void setAuthor(UUID author) {
        this.author = author;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isPublic() {
        return this.isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public GameType getGameType() {
        return this.gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDownloads() {
        return this.downloads;
    }

    public void setDownloads(long downloads) {
        this.downloads = downloads;
    }

    public void openBook(Player player) {
        List<GameModeSetting> sections = new ArrayList<>();
        List<BaseComponent[]> pages = new ArrayList<>();
        BaseComponent[] header =
            new ComponentBuilder("<-  Back")
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Back").create()))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gamemodes")).create();

        for (GameModeSettingType section : GameModeSettingType.values()) {
            if (!section.hasParent() && section.isValid(this.gameType) && !section.isDisabled()) sections.add(section);
        }

        PageBuilder pageBuilder = null;

        for (int i = 1; i <= sections.size(); i++) {
            GameModeSetting setting = sections.get(i - 1);

            if (i == 1 || i % 7 == 0) {
                if (i > 1) pages.add(pageBuilder.build());
                pageBuilder = new PageBuilder().add(header).newLines(2);
            }

            pageBuilder.add(setting.getName(this)).newLine();
        }

        if (pageBuilder != null) pages.add(pageBuilder.build());

        new BookBuilder().pages(pages).open(player);
    }

    public NumberSetting getNumberSetting(GameModeNumberSetting setting) {
        return this.numberSettings.get(setting.name().toLowerCase());
    }

    public ToggleSetting getToggleSetting(GameModeToggleSetting setting) {
        return this.toggleSettings.get(setting.name().toLowerCase());
    }

    public EnumSetting getListSetting(GameModeEnumSetting setting) {
        return this.listSettings.get(setting.name().toLowerCase());
    }

    public boolean hasUnsavedChanges() {
        return this.unsavedChanges;
    }

    public void setUnsavedChanges(boolean unsavedChanges) {
        this.unsavedChanges = unsavedChanges;
    }

    public Setting getSetting(GameModeSetting setting) {
        if (setting instanceof GameModeNumberSetting) {
            return this.getNumberSetting((GameModeNumberSetting) setting);
        } else if (setting instanceof GameModeToggleSetting) {
            return this.getToggleSetting((GameModeToggleSetting) setting);
        } else if (setting instanceof GameModeEnumSetting) {
            return this.getListSetting((GameModeEnumSetting) setting);
        } else {
            return null;
        }
    }

    public void openSettingsBook(Player player, BaseComponent[] header, List<GameModeSetting> settings) {
        PageBuilder pageBuilder = new PageBuilder().add(header).newLine().newLine();

        for (GameModeSetting setting : settings) {
            pageBuilder.add(setting.getName(this)).newLine();
        }

        new BookBuilder().pages(pageBuilder.build()).open(player);
    }

    public Map<String, Object> toYaml() {
        Map<String, Object> data = new LinkedHashMap<>();

        data.put("author", this.author.toString());
        data.put("created", this.created);
        data.put("updated", Timestamp.now());
        data.put("public", this.isPublic);
        data.put("name", this.name);
        data.put("alias", this.alias);
        data.put("game_type", this.gameType.name());
        data.put("downloads", this.downloads);

        if (!this.description.isEmpty()) data.put("description", this.description);

        Map<String, Object> settingsData = new LinkedHashMap<>();

        for (GameModeSettingType section : GameModeSettingType.values()) {
            if (section.hasParent() || !section.isValid(this.gameType)) continue;
            Map<String, Object> sectionData = section.toYaml(this);
            if (sectionData != null) settingsData.put(section.name().toLowerCase(), sectionData);
        }

        data.put("settings", settingsData);

        return data;
    }

}
