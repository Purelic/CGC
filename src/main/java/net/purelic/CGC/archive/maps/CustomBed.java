//package net.purelic.CGC.core.maps;
//
//import net.md_5.bungee.api.chat.ClickEvent;
//import net.md_5.bungee.api.chat.ComponentBuilder;
//import net.md_5.bungee.api.chat.HoverEvent;
//import net.purelic.CGC.core.maps.constants.BedDefenseType;
//import net.purelic.CGC.core.maps.constants.BedDirection;
//import net.purelic.CGC.core.maps.constants.MatchTeam;
//import net.purelic.CGC.maps.settings.MapEnumSetting;
//import net.purelic.commons.Commons;
//import net.purelic.commons.utils.CommandUtils;
//import net.purelic.commons.utils.YamlUtils;
//import net.purelic.commons.utils.book.BookBuilder;
//import net.purelic.commons.utils.book.PageBuilder;
//import org.bukkit.Location;
//import org.bukkit.Sound;
//import org.bukkit.block.BlockState;
//import org.bukkit.entity.Player;
//import org.bukkit.scheduler.BukkitRunnable;
//
//import java.util.*;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//public class CustomBed {
//
//    private boolean previewing = false;
//
//    private final String location;
//    private MatchTeam owner;
//    private BedDirection direction;
//    private BedDefenseType defense;
//
//    private final MapEnumSetting ownerSetting;
//    private final MapEnumSetting directionSetting;
//    private final MapEnumSetting defenseSetting;
//
//    public CustomBed(Map<String, Object> data) {
//        this(
//            (String) data.get("location"),
//            MatchTeam.valueOf((String) data.getOrDefault("owner", "BLUE")),
//            BedDirection.valueOf((String) data.getOrDefault("direction", "NORTH")),
//            BedDefenseType.valueOf((String) data.getOrDefault("defense", "NONE"))
//        );
//    }
//
//    public CustomBed(Location location, MatchTeam owner, BedDirection direction, BedDefenseType defense) {
//        this(
//            YamlUtils.locationToCoords(location, false),
//            owner,
//            direction,
//            defense
//        );
//    }
//
//    public CustomBed(String location, MatchTeam owner, BedDirection direction, BedDefenseType defense) {
//        this.location = location;
//        this.owner = owner;
//        this.direction = direction;
//        this.defense = defense;
//
//        List<String> bedOwners = new ArrayList<>(MatchTeam.getNames());
//        bedOwners.remove("SOLO");
//
//        this.ownerSetting = new MapEnumSetting(
//            "Owner",
//            "Bed can't be broken by this team",
//            "/bed owner <id> <value>",
//            this.owner.name(),
//            bedOwners
//        );
//
//        this.directionSetting = new MapEnumSetting(
//            "Direction",
//            "Direction the bed will face",
//            "/bed direction <id> <value>",
//            this.direction.name(),
//            Stream.of(BedDirection.values()).map(Enum::name).collect(Collectors.toList())
//        );
//
//        this.defenseSetting = new MapEnumSetting(
//            "Defense",
//            "Pre-built bed defense",
//            "/bed defense <id> <value>",
//            this.defense.name(),
//            Stream.of(BedDefenseType.values()).map(Enum::name).collect(Collectors.toList())
//        );
//    }
//
//    public String getLocation() {
//        return this.location;
//    }
//
//    public MatchTeam getOwner() {
//        return this.owner;
//    }
//
//    public void setOwner(MatchTeam owner) {
//        this.owner = owner;
//        this.ownerSetting.setCurrent(owner.name());
//    }
//
//    public BedDirection getDirection() {
//        return this.direction;
//    }
//
//    public void setDirection(BedDirection direction) {
//        this.direction = direction;
//        this.directionSetting.setCurrent(direction.name());
//    }
//
//    public MapEnumSetting getDirectionSetting() {
//        return this.directionSetting;
//    }
//
//    public BedDefenseType getDefense() {
//        return this.defense;
//    }
//
//    public void setDefense(BedDefenseType defense) {
//        this.defense = defense;
//        this.defenseSetting.setCurrent(defense.name());
//    }
//
//    public MapEnumSetting getDefenseSetting() {
//        return this.defenseSetting;
//    }
//
//    public Map<String, Object> toYaml() {
//        Map<String, Object> bed = new HashMap<>();
//        bed.put("location", this.location);
//        if (this.owner != MatchTeam.BLUE) bed.put("owner", this.owner.name());
//        if (this.direction != BedDirection.NORTH) bed.put("direction", this.direction.name());
//        if (this.defense != BedDefenseType.NONE) bed.put("defense", this.defense.name());
//        return bed;
//    }
//
//    public void openBook(Player player, int id) {
//        new BookBuilder()
//            .pages(new PageBuilder()
//                .add(new ComponentBuilder("⬅ ")
//                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Back").create()))
//                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/beds"))
//                    .bold(true)
//                    .create())
//                .add(new ComponentBuilder("Bed #" + id)
//                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.location + "\n" + "/bed preview " + id).create()))
//                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bed preview " + id))
//                    .create()).newLine().newLine()
//                .add(this.ownerSetting.getName()).newLine()
//                .add(this.ownerSetting.getValue(id)).newLine().newLine()
//                .add(this.directionSetting.getName()).newLine()
//                .add(this.directionSetting.getValue(id)).newLine().newLine()
//                .add(this.defenseSetting.getName()).newLine()
//                .add(this.defenseSetting.getValue(id))
//                .build()
//            )
//            .open(player);
//
//        player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
//    }
//
//    public void preview(Player player) {
//        if (this.previewing) return;
//        else this.previewing = true;
//
//        CommandUtils.sendAlertMessage(player, "This preview will last for 10 seconds and shows you what this bed will look like in-game");
//        Location location = YamlUtils.getLocationFromCoords(player, this.location);
//        player.teleport(location.clone().add(0.5, 3, 0.5));
//        Set<BlockState> states = this.defense.build(location, this.direction, this.owner);
//
//        new BukkitRunnable() {
//
//            @Override
//            public void run() {
//                states.forEach(state -> state.update(true));
//                previewing = false;
//            }
//
//        }.runTaskLater(Commons.getPlugin(), 200);
//    }
//
//}
