//package net.purelic.CGC.core.maps;
//
//import net.md_5.bungee.api.chat.ClickEvent;
//import net.md_5.bungee.api.chat.ComponentBuilder;
//import net.md_5.bungee.api.chat.HoverEvent;
//import net.purelic.CGC.maps.settings.MapEnumSetting;
//import net.purelic.CGC.maps.settings.MapNumberSetting;
//import net.purelic.CGC.maps.settings.MapToggleSetting;
//import net.purelic.commons.Commons;
//import net.purelic.commons.utils.CommandUtils;
//import net.purelic.commons.utils.YamlUtils;
//import net.purelic.commons.utils.book.BookBuilder;
//import net.purelic.commons.utils.book.PageBuilder;
//import org.bukkit.ChatColor;
//import org.bukkit.Location;
//import org.bukkit.Material;
//import org.bukkit.Sound;
//import org.bukkit.block.Block;
//import org.bukkit.block.BlockState;
//import org.bukkit.block.CreatureSpawner;
//import org.bukkit.entity.ArmorStand;
//import org.bukkit.entity.EntityType;
//import org.bukkit.entity.Player;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.scheduler.BukkitRunnable;
//import org.bukkit.util.EulerAngle;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
//public class Spawner {
//
//    private boolean previewing = false;
//
//    private final String location;
//    private Material material;
//    private int delay;
//    private int amount;
//    private boolean infinite;
//    private boolean hologram;
//    private boolean startSpawned;
//    private boolean destructive;
//
//    private final MapEnumSetting materialSetting;
//    private final MapNumberSetting delaySetting;
//    private final MapNumberSetting amountSetting;
//    private final MapToggleSetting infiniteSetting;
//    private final MapToggleSetting hologramSetting;
//    private final MapToggleSetting startSpawnedSetting;
//    private final MapToggleSetting destructiveSetting;
//
//    public Spawner(Map<String, Object> data) {
//        this(
//            (String) data.get("location"),
//            Material.valueOf((String) data.getOrDefault("material", "GOLDEN_APPLE")),
//            (int) data.getOrDefault("delay", 20),
//            (int) data.getOrDefault("amount", 1),
//            (boolean) data.getOrDefault("infinite", false),
//            (boolean) data.getOrDefault("hologram", true),
//            (boolean) data.getOrDefault("start_spawned", false),
//            (boolean) data.getOrDefault("destructive", true)
//        );
//    }
//
//    public Spawner(Location location, Material material, int delay, int amount, boolean infinite, boolean hologram, boolean startSpawned, boolean destructive) {
//        this(
//            YamlUtils.locationToCoords(location, false),
//            material,
//            delay,
//            amount,
//            infinite,
//            hologram,
//            startSpawned,
//            destructive
//        );
//    }
//
//    public Spawner(String location, Material material, int delay, int amount, boolean infinite, boolean hologram, boolean startSpawned, boolean destructive) {
//        this.location = location;
//        this.material = material;
//        this.delay = delay;
//        this.amount = amount;
//        this.infinite = infinite;
//        this.hologram = hologram;
//        this.startSpawned = startSpawned;
//        this.destructive = destructive;
//
//        this.materialSetting = new MapEnumSetting(
//            "Material",
//            "Item to spawn",
//            "/spawner material <id> <value>",
//            this.material.name(),
//            Arrays.asList(Material.GOLDEN_APPLE.name(), Material.ARROW.name(), Material.WOOL.name(), Material.SKULL_ITEM.name(),
//                Material.IRON_INGOT.name(), Material.GOLD_INGOT.name(), Material.DIAMOND.name(), Material.EMERALD.name()));
//
//        this.delaySetting = new MapNumberSetting(
//            "Delay",
//            "Time between item spawns",
//            "/spawner delay <id> <value>",
//            this.delay,
//            "s",
//            1,
//            180,
//            5,
//            false);
//
//        this.amountSetting = new MapNumberSetting(
//            "Amount",
//            "Number of items to spawn",
//            "/spawner amount <id> <value>",
//            this.amount,
//            " item",
//            1,
//            64,
//            1,
//            true);
//
//        this.infiniteSetting = new MapToggleSetting(
//            "Infinite",
//            "Continuously spawn items",
//            "/spawner infinite <id> <value>",
//            this.infinite);
//
//        this.hologramSetting = new MapToggleSetting(
//            "Hologram",
//            "Show timer hologram above spawner",
//            "/spawner hologram <id> <value>",
//            this.hologram);
//
//        this.startSpawnedSetting = new MapToggleSetting(
//            "Start Spawned",
//            "Spawner to start with the item(s) spawned",
//            "/spawner start_spawned <id> <value>",
//            this.startSpawned);
//
//        this.destructiveSetting = new MapToggleSetting(
//            "Destructive",
//            "Spawner to replace itself with a spawner block",
//            "/spawner destructive <id> <value>",
//            this.destructive);
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
//    public int getDelay() {
//        return this.delay;
//    }
//
//    public void setDelay(int delay) {
//        this.delay = delay;
//        this.delaySetting.setCurrent(delay);
//    }
//
//    public MapNumberSetting getDelaySetting() {
//        return this.delaySetting;
//    }
//
//    public int getAmount() {
//        return this.amount;
//    }
//
//    public void setAmount(int amount) {
//        this.amount = amount;
//        this.amountSetting.setCurrent(amount);
//    }
//
//    public MapNumberSetting getAmountSetting() {
//        return this.amountSetting;
//    }
//
//    public boolean isInfinite() {
//        return this.infinite;
//    }
//
//    public void setInfinite(boolean infinite) {
//        this.infinite = infinite;
//        this.infiniteSetting.setCurrent(infinite);
//    }
//
//    public MapToggleSetting getInfiniteSetting() {
//        return this.infiniteSetting;
//    }
//
//    public boolean hasHologram() {
//        return this.hologram;
//    }
//
//    public void setHologram(boolean hologram) {
//        this.hologram = hologram;
//        this.hologramSetting.setCurrent(hologram);
//    }
//
//    public MapToggleSetting getHologramSetting() {
//        return this.hologramSetting;
//    }
//
//    public void setStartSpawned(boolean startSpawned) {
//        this.startSpawned = startSpawned;
//        this.startSpawnedSetting.setCurrent(startSpawned);
//    }
//
//    public boolean isStartSpawned() {
//        return this.startSpawned;
//    }
//
//    public MapToggleSetting getStartSpawnedSetting() {
//        return this.startSpawnedSetting;
//    }
//
//    public void setDestructive(boolean destructive) {
//        this.destructive = destructive;
//        this.destructiveSetting.setCurrent(destructive);
//    }
//
//    public boolean isDestructive() {
//        return this.destructive;
//    }
//
//    public Map<String, Object> toYaml() {
//        Map<String, Object> spawner = new HashMap<>();
//        spawner.put("location", this.location);
//        if (this.material != Material.GOLDEN_APPLE) spawner.put("material", this.material.name());
//        if (this.delay != 20) spawner.put("delay", this.delay);
//        if (this.amount != 1) spawner.put("amount", this.amount);
//        if (this.infinite) spawner.put("infinite", true);
//        if (!this.hologram) spawner.put("hologram", false);
//        if (this.startSpawned) spawner.put("start_spawned", true);
//        if (!this.destructive) spawner.put("destructive", false);
//        return spawner;
//    }
//
//    public void openBook(Player player, int id) {
//        new BookBuilder()
//            .pages(
//                new PageBuilder()
//                    .add(new ComponentBuilder("⬅ ")
//                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Back").create()))
//                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/spawners"))
//                        .bold(true)
//                        .create())
//                    .add(new ComponentBuilder("Spawner #" + id)
//                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.location + "\n" + "/spawner preview " + id).create()))
//                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/spawner preview " + id))
//                        .create()).newLine().newLine()
//                    .add(this.materialSetting.getName()).newLine()
//                    .add(this.materialSetting.getValue(id)).newLine().newLine()
//                    .add(this.delaySetting.getName())
//                    .add(this.delaySetting.getValue(id)).newLine().newLine()
//                    .add(this.amountSetting.getName())
//                    .add(this.amountSetting.getValue(id)).newLine().newLine()
//                    .add(this.infiniteSetting.getName())
//                    .add(this.infiniteSetting.getValue(id)).newLine().newLine()
//                    .add(this.hologramSetting.getName())
//                    .add(this.hologramSetting.getValue(id)).newLine().newLine()
//                    .add(this.startSpawnedSetting.getName())
//                    .add(this.startSpawnedSetting.getValue(id))
//                    .build(),
//                new PageBuilder()
//                    .add(new ComponentBuilder("⬅ ")
//                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Back").create()))
//                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/spawners"))
//                        .bold(true)
//                        .create())
//                    .add(new ComponentBuilder("Spawner #" + id)
//                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.location + "\n" + "/spawner preview " + id).create()))
//                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/spawner preview " + id))
//                        .create()).newLine().newLine()
//                    .add(this.destructiveSetting.getName())
//                    .add(this.destructiveSetting.getValue(id))
//                    .build()
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
//        CommandUtils.sendAlertMessage(player, "This preview will last for 10 seconds and shows you what this spawner will look like in-game");
//        Location location = YamlUtils.getLocationFromCoords(player, this.location);
//        player.teleport(location.clone().add(0.5, 1, 0.5));
//
//        Block block = player.getWorld().getBlockAt(location);
//        BlockState state = block.getState();
//
//        if (this.destructive) {
//            block.setType(Material.MOB_SPAWNER);
//            BlockState current = block.getState();
//
//            CreatureSpawner spawner = (CreatureSpawner) current;
//            spawner.setSpawnedType(EntityType.FALLING_BLOCK);
//            spawner.setDelay(Integer.MAX_VALUE);
//        }
//
//        ArmorStand stand = this.getArmorStand(location);
//
//        new BukkitRunnable() {
//
//            @Override
//            public void run() {
//                state.update(true);
//                stand.remove();
//                previewing = false;
//            }
//
//        }.runTaskLater(Commons.getPlugin(), 200);
//    }
//
//    private ArmorStand getArmorStand(Location location) {
//        ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(location.clone().add(0.5, 2, 0.5), EntityType.ARMOR_STAND);
//        stand.setSmall(true);
//        stand.setCanPickupItems(false);
//        stand.setMarker(true);
//        stand.setGravity(false);
//        stand.setVisible(false);
//        stand.setRemoveWhenFarAway(false);
//        stand.setBasePlate(false);
//        stand.setLeftLegPose(new EulerAngle(Math.PI, 0.0D, 0.0D));
//        stand.setRightLegPose(new EulerAngle(Math.PI, 0.0D, 0.0D));
//
//        if (this.hologram) {
//            stand.setCustomName(this.getHologramColor() + "" + this.delay + "s" + (this.amount == 1 ? "" : " (x" + this.amount + ")"));
//            stand.setCustomNameVisible(true);
//
//            if (this.material == Material.ARROW) stand.setHelmet(new ItemStack(Material.IRON_BLOCK));
//            else if (this.material == Material.GOLDEN_APPLE) stand.setHelmet(new ItemStack(Material.GOLD_BLOCK));
//            else if (this.material == Material.DIAMOND) stand.setHelmet(new ItemStack(Material.DIAMOND_BLOCK));
//            else if (this.material == Material.EMERALD) stand.setHelmet(new ItemStack(Material.EMERALD_BLOCK));
//        }
//
//        return stand;
//    }
//
//    private ChatColor getHologramColor() {
//        if (this.material == Material.GOLDEN_APPLE) return ChatColor.YELLOW;
//        else if (this.material == Material.DIAMOND) return ChatColor.AQUA;
//        else if (this.material == Material.EMERALD) return ChatColor.GREEN;
//        else return ChatColor.WHITE;
//    }
//
//}
