package net.purelic.CGC.managers;

import net.purelic.CGC.gamemodes.CustomGameMode;
import net.purelic.commons.utils.DatabaseUtils;
import shaded.com.google.cloud.firestore.QueryDocumentSnapshot;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

public class GameModeManager {

    private static final Map<String, CustomGameMode> GAME_MODES_BY_NAME = new TreeMap<>();
    private static final Map<String, CustomGameMode> GAME_MODES_BY_ALIAS = new TreeMap<>();

    public static void loadGameModes(UUID uuid) {
        List<QueryDocumentSnapshot> gameModeDocs = DatabaseUtils.getGameModes(uuid);
        gameModeDocs.forEach(doc -> addGameMode(new CustomGameMode(doc.getId(), doc.getData())));
    }

    public static void addGameMode(CustomGameMode gameMode) {
        GAME_MODES_BY_NAME.put(gameMode.getName().toLowerCase(), gameMode);
        GAME_MODES_BY_ALIAS.put(gameMode.getAlias().toLowerCase(), gameMode);
    }

    public static void removeGameMode(CustomGameMode gameMode) {
        GAME_MODES_BY_NAME.remove(gameMode.getName().toLowerCase());
        GAME_MODES_BY_ALIAS.remove(gameMode.getAlias().toLowerCase());
    }

    public static Map<String, CustomGameMode> getGameModes() {
        return GAME_MODES_BY_NAME;
    }

    public static CustomGameMode getGameModeByName(String name) {
        return GAME_MODES_BY_NAME.get(name.toLowerCase());
    }

    public static CustomGameMode getGameModeByAlias(String alias) {
        return GAME_MODES_BY_ALIAS.get(alias.toLowerCase());
    }

    public static CustomGameMode getGameModeByNameOrAlias(String value) {
        CustomGameMode gameMode = getGameModeByName(value);
        return gameMode != null ? gameMode : getGameModeByAlias(value);
    }

    public static List<CustomGameMode> getUnsavedGameModes() {
        return getGameModes().values().stream()
            .filter(CustomGameMode::hasUnsavedChanges)
            .collect(Collectors.toList());
    }

}
