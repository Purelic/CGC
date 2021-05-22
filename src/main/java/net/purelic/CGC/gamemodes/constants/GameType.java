package net.purelic.CGC.gamemodes.constants;

public enum GameType {

    DEATHMATCH("Kill the most players to win!"),
    KING_OF_THE_HILL("Capture hills for points!"),
    CAPTURE_THE_FLAG("Capture flags for points"),
    BED_WARS("Destroy enemy beds to win!"),
    SURVIVAL_GAMES("Survive until the end!"),
    // INFECTION("Infect all the survivors!"),
    // UHC("Survive till the end with no natural regen!"),
    HEAD_HUNTER("Kill other players and collect their head for points!"),
    ;

    private final String description;

    GameType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

}
