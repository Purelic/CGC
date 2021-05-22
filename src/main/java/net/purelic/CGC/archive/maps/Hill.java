//package net.purelic.CGC.core.maps;
//
//import net.md_5.bungee.api.chat.ClickEvent;
//import net.md_5.bungee.api.chat.ComponentBuilder;
//import net.md_5.bungee.api.chat.HoverEvent;
//import net.purelic.CGC.core.maps.constants.HillType;
//import net.purelic.CGC.core.maps.constants.MatchTeam;
//import net.purelic.CGC.maps.settings.MapEnumSetting;
//import net.purelic.CGC.maps.settings.MapNumberSetting;
//import net.purelic.CGC.maps.settings.MapToggleSetting;
//import net.purelic.commons.Commons;
//import net.purelic.commons.modules.BlockPhysicsModule;
//import net.purelic.commons.utils.CommandUtils;
//import net.purelic.commons.utils.YamlUtils;
//import net.purelic.commons.utils.book.BookBuilder;
//import net.purelic.commons.utils.book.PageBuilder;
//import org.bukkit.*;
//import org.bukkit.block.Block;
//import org.bukkit.block.BlockState;
//import org.bukkit.entity.Player;
//import org.bukkit.material.MaterialData;
//import org.bukkit.scheduler.BukkitRunnable;
//
//import java.util.*;
//
//public class Hill {
//
//    private boolean previewing = false;
//    private Set<BlockState> blocks = new HashSet<>();
//
//    private final String location;
//    private String name;
//    private MatchTeam owner;
//    private Material material;
//    private HillType type;
//    private int radius;
//    private boolean circle;
//
//    private final MapEnumSetting typeSetting;
//    private final MapEnumSetting ownerSetting;
//    private final MapEnumSetting materialSetting;
//    private final MapNumberSetting radiusSetting;
//    private final MapToggleSetting circleSetting;
//
//    public Hill(Map<String, Object> data) {
//        this(
//            (String) data.get("location"),
//            (String) data.get("name"),
//            HillType.valueOf((String) data.getOrDefault("type", "KOTH_HILL")),
//            MatchTeam.valueOf((String) data.getOrDefault("owner", "SOLO")),
//            Material.valueOf((String) data.getOrDefault("material", "WOOL")),
//            (int) data.getOrDefault("radius", 3),
//            (boolean) data.getOrDefault("circle", true)
//        );
//    }
//
//    public Hill(Location location, String name, HillType type, MatchTeam owner, Material material, int radius, boolean circle) {
//        this(
//            YamlUtils.locationToCoords(location, false),
//            name,
//            type,
//            owner,
//            material,
//            radius,
//            circle
//        );
//    }
//
//    public Hill(String location, String name, HillType type, MatchTeam owner, Material material, int radius, boolean circle) {
//        this.location = location;
//        this.name = name;
//        this.type = type;
//        this.owner = owner;
//        this.material = material;
//        this.radius = radius;
//        this.circle = circle;
//
//        this.typeSetting = new MapEnumSetting(
//                "Type",
//                "Type of hill",
//                "/hill type <id> <value>",
//                this.type.name(),
//                new ArrayList<>(HillType.getNames())
//        );
//
//        this.ownerSetting = new MapEnumSetting(
//                "Owner",
//                "Hill can't be captured by this team unless neutral",
//                "/hill owner <id> <value>",
//                this.owner.name(),
//                new ArrayList<>(MatchTeam.getNames())
//        );
//
//        this.materialSetting = new MapEnumSetting(
//                "Material",
//                "Material of the hill",
//                "/hill material <id> <value>",
//                this.material.name(),
//                Arrays.asList(Material.WOOL.name(), Material.STAINED_CLAY.name(), Material.STAINED_GLASS.name()));
//
//        this.radiusSetting = new MapNumberSetting(
//                "Radius",
//                "Radius of the hill",
//                "/hill radius <id> <value>",
//                this.radius,
//                " block",
//                2,
//                8,
//                1,
//                true);
//
//        this.circleSetting = new MapToggleSetting(
//                "Circle",
//                "Whether the hill is a circle or square",
//                "/hill circle <id> <value>",
//                this.circle);
//    }
//
//    public String getName() {
//        return this.name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//
//    }
//
//    public String getLocation() {
//        return this.location;
//    }
//
//    public HillType getType() {
//        return this.type;
//    }
//
//    public void setType(HillType type) {
//        this.type = type;
//        this.typeSetting.setCurrent(type.name());
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
//    public Material getMaterial() {
//        return this.material;
//    }
//
//    public void setMaterial(Material material) {
//        this.material = material;
//        this.materialSetting.setCurrent(material.name());
//    }
//
//    public MapEnumSetting getMaterialSetting() {
//        return this.materialSetting;
//    }
//
//    public int getRadius() {
//        return this.radius;
//    }
//
//    public void setRadius(int radius) {
//        this.radius= radius;
//        this.radiusSetting.setCurrent(radius);
//    }
//
//    public MapNumberSetting getDelaySetting() {
//        return this.radiusSetting;
//    }
//
//    public boolean isCircle() {
//        return this.circle;
//    }
//
//    public void setCircle(boolean circle) {
//        this.circle = circle;
//        this.circleSetting.setCurrent(circle);
//    }
//
//    public MapToggleSetting getCircleSetting() {
//        return this.circleSetting;
//    }
//
//    public Map<String, Object> toYaml() {
//        Map<String, Object> hill = new HashMap<>();
//        hill.put("location", this.location);
//        hill.put("name", this.name);
//        if (this.type != HillType.KOTH_HILL) hill.put("type", this.type.name());
//        if (this.owner != MatchTeam.SOLO) hill.put("owner", this.owner.name());
//        if (this.material != Material.WOOL) hill.put("material", this.material.name());
//        if (this.radius != 3) hill.put("radius", this.radius);
//        if (!this.circle) hill.put("circle", false);
//        return hill;
//    }
//
//    public void openBook(Player player, int id) {
//        new BookBuilder()
//                .pages(new PageBuilder()
//                        .add(new ComponentBuilder("⬅ ")
//                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Back").create()))
//                                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hills"))
//                                .bold(true)
//                                .create())
//                        .add(new ComponentBuilder("Hill #" + id)
//                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.name + "\n" + this.location + "\n/hill preview " + id).create()))
//                                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hill preview " + id))
//                                .create()).newLine().newLine()
//                        .add(this.typeSetting.getName()).newLine()
//                        .add(this.typeSetting.getValue(id)).newLine().newLine()
//                        .add(this.ownerSetting.getName()).newLine()
//                        .add(this.ownerSetting.getValue(id)).newLine().newLine()
//                        .add(this.materialSetting.getName()).newLine()
//                        .add(this.materialSetting.getValue(id)).newLine().newLine()
//                        .add(this.radiusSetting.getName())
//                        .add(this.radiusSetting.getValue(id)).newLine().newLine()
//                        .add(this.circleSetting.getName())
//                        .add(this.circleSetting.getValue(id))
//                        .build()
//                )
//                .open(player);
//
//        player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
//    }
//
//    public void preview(Player player) {
//        if (this.previewing) return;
//
//        this.previewing = true;
//        BlockPhysicsModule.setBlockPhysics(false);
//
//        CommandUtils.sendAlertMessage(player, "This preview will last for 10 seconds and shows you what this hill will look like in-game");
//        Location location = YamlUtils.getLocationFromCoords(player, this.location);
//        player.teleport(location.clone().add(0.5, 1, 0.5));
//        Block center = player.getWorld().getBlockAt(location);
//        this.createHill(center);
//
//        Waypoint waypoint = null;
//
//        if (this.type != HillType.CTF_GOAL) waypoint = new Waypoint(player.getWorld(), this);
//
//        final Waypoint finalWaypoint = waypoint;
//        new BukkitRunnable() {
//
//            @Override
//            public void run() {
//                blocks.forEach(state -> state.update(true));
//                blocks.clear();
//                previewing = false;
//                BlockPhysicsModule.setBlockPhysics(true);
//                if (finalWaypoint != null) finalWaypoint.destroy();
//            }
//
//        }.runTaskLater(Commons.getPlugin(), 200);
//    }
//
//    private void createHill(Block center) {
//        DyeColor color = this.owner.getDyeColor();
//        World world = center.getWorld();
//
//        for (int xPoint = center.getX() - this.radius; xPoint <= center.getX() + this.radius; xPoint++) {
//            for (int zPoint = center.getZ() - this.radius; zPoint <= center.getZ() + this.radius; zPoint++) {
//                Block block = world.getBlockAt(xPoint, center.getY(), zPoint);
//                this.blocks.add(block.getState());
//
//                if (this.circle) {
//                    if (this.insideCircle(center, block)) this.updateBlockColor(block, color);
//                } else {
//                    this.updateBlockColor(block, color);
//                }
//            }
//        }
//    }
//
//    private void updateBlockColor(Block block, DyeColor color) {
//        this.updateBlockColor(block, color, this.material);
//    }
//
//    private void updateBlockColor(Block block, DyeColor color, Material material) {
//        if (block.getType() != material) block.setType(material);
//        BlockState state = block.getState();
//        MaterialData data = state.getData();
//        data.setData(color.getData());
//        state.update();
//    }
//
//    private boolean insideCircle(Block center, Block block) {
//        return this.insideCircle(center.getLocation(), block.getLocation());
//    }
//
//    private boolean insideCircle(Location center, Location location) {
//        return center.distance(location) <= (this.radius + 0.5);
//    }
//
//}
