//package net.purelic.CGC.core.maps;
//
//import net.md_5.bungee.api.chat.ClickEvent;
//import net.md_5.bungee.api.chat.ComponentBuilder;
//import net.md_5.bungee.api.chat.HoverEvent;
//import net.purelic.CGC.core.maps.constants.FlagDirection;
//import net.purelic.CGC.core.maps.constants.MatchTeam;
//import net.purelic.CGC.maps.settings.MapEnumSetting;
//import net.purelic.commons.Commons;
//import net.purelic.commons.utils.CommandUtils;
//import net.purelic.commons.utils.YamlUtils;
//import net.purelic.commons.utils.book.BookBuilder;
//import net.purelic.commons.utils.book.PageBuilder;
//import org.bukkit.Location;
//import org.bukkit.Material;
//import org.bukkit.Sound;
//import org.bukkit.block.Banner;
//import org.bukkit.block.Block;
//import org.bukkit.entity.Player;
//import org.bukkit.material.Directional;
//import org.bukkit.scheduler.BukkitRunnable;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//public class Flag {
//
//    private boolean previewing = false;
//
//    private final String location;
//    private String name;
//    private MatchTeam owner;
//    private FlagDirection direction;
//
//    private final MapEnumSetting ownerSetting;
//    private final MapEnumSetting directionSetting;
//
//    public Flag(Map<String, Object> data) {
//        this(
//            (String) data.get("location"),
//            (String) data.get("name"),
//            MatchTeam.valueOf((String) data.getOrDefault("owner", "SOLO")),
//            FlagDirection.valueOf((String) data.getOrDefault("direction", "NORTH"))
//        );
//    }
//
//    public Flag(Location location, String name, MatchTeam owner, FlagDirection direction) {
//        this(
//            YamlUtils.locationToCoords(location, false),
//            name,
//            owner,
//            direction
//        );
//    }
//
//    public Flag(String location, String name, MatchTeam owner, FlagDirection direction) {
//        this.location = location;
//        this.name = name;
//        this.owner = owner;
//        this.direction = direction;
//
//        this.ownerSetting = new MapEnumSetting(
//            "Owner",
//            "Flag can't be captured by this team unless neutral",
//            "/flag owner <id> <value>",
//            this.owner.name(),
//            new ArrayList<>(MatchTeam.getNames())
//        );
//
//        this.directionSetting = new MapEnumSetting(
//            "Direction",
//            "Direction the flag will face",
//            "/flag direction <id> <value>",
//            this.direction.name(),
//            new ArrayList<>(FlagDirection.getNames())
//        );
//    }
//
//    public String getName() {
//        return this.name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
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
//    public FlagDirection getDirection() {
//        return this.direction;
//    }
//
//    public void setDirection(FlagDirection direction) {
//        this.direction = direction;
//        this.directionSetting.setCurrent(direction.name());
//    }
//
//    public Map<String, Object> toYaml() {
//        Map<String, Object> hill = new HashMap<>();
//        hill.put("location", this.location);
//        hill.put("name", this.name);
//        if (this.owner != MatchTeam.SOLO) hill.put("owner", this.owner.name());
//        if (this.direction != FlagDirection.NORTH) hill.put("direction", this.direction.name());
//        return hill;
//    }
//
//    public void openBook(Player player, int id) {
//        new BookBuilder()
//            .pages(new PageBuilder()
//                .add(new ComponentBuilder("⬅ ")
//                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Back").create()))
//                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/flags"))
//                    .bold(true)
//                    .create())
//                .add(new ComponentBuilder("Flag #" + id)
//                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.name + "\n" + this.location + "\n/flag preview " + id).create()))
//                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/flag preview " + id))
//                    .create()).newLine().newLine()
//                .add(this.ownerSetting.getName()).newLine()
//                .add(this.ownerSetting.getValue(id)).newLine().newLine()
//                .add(this.directionSetting.getName()).newLine()
//                .add(this.directionSetting.getValue(id)).newLine().newLine()
//                .build()
//            )
//            .open(player);
//
//        player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
//    }
//
//    public void preview(Player player) {
//        if (this.previewing) return;
//
//        this.previewing = true;
//
//        CommandUtils.sendAlertMessage(player, "This preview will last for 10 seconds and shows you what this flag will look like in-game");
//
//        Location location = YamlUtils.getLocationFromCoords(player, this.location);
//        player.teleport(location.clone().add(0.5, 0, 0.5));
//        Block center = player.getWorld().getBlockAt(location);
//        this.createFlag(center);
//
//        Waypoint waypoint = new Waypoint(player.getWorld(), this);
//
//        new BukkitRunnable() {
//
//            @Override
//            public void run() {
//                center.setType(Material.AIR);
//                previewing = false;
//                waypoint.destroy();
//            }
//
//        }.runTaskLater(Commons.getPlugin(), 200);
//    }
//
//    private void createFlag(Block center) {
//        center.setType(Material.STANDING_BANNER);
//        Banner banner = (Banner) center.getState();
//        ((Directional) banner.getData()).setFacingDirection(this.direction.getBlockFace());
//        banner.setBaseColor(this.owner.getDyeColor());
//        banner.update();
//    }
//
//}
