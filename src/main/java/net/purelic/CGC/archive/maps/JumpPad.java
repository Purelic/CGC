//package net.purelic.CGC.core.maps;
//
//import net.md_5.bungee.api.chat.ClickEvent;
//import net.md_5.bungee.api.chat.ComponentBuilder;
//import net.md_5.bungee.api.chat.HoverEvent;
//import net.purelic.CGC.maps.settings.MapNumberSetting;
//import net.purelic.CGC.maps.settings.MapToggleSetting;
//import net.purelic.commons.Commons;
//import net.purelic.commons.utils.CommandUtils;
//import net.purelic.commons.utils.YamlUtils;
//import net.purelic.commons.utils.book.BookBuilder;
//import net.purelic.commons.utils.book.PageBuilder;
//import org.bukkit.*;
//import org.bukkit.block.Block;
//import org.bukkit.block.BlockFace;
//import org.bukkit.block.BlockState;
//import org.bukkit.entity.Player;
//import org.bukkit.scheduler.BukkitRunnable;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class JumpPad {
//
//    private boolean previewing = false;
//
//    private final String location;
//    private int power;
//    private int angle;
//    private boolean destructive;
//
//    private final MapNumberSetting powerSetting;
//    private final MapNumberSetting angleSetting;
//    private final MapToggleSetting destructiveSetting;
//
//    public JumpPad(Map<String, Object> data) {
//        this(
//            (String) data.get("location"),
//            (int) data.getOrDefault("power", 5),
//            (int) data.getOrDefault("angle", 45),
//            (boolean) data.getOrDefault("destructive", true)
//        );
//    }
//
//    public JumpPad(Location location, int power, int angle, boolean destructive) {
//        this(
//            YamlUtils.locationToCoords(location, false),
//            power,
//            angle,
//            destructive
//        );
//    }
//
//    public JumpPad(String location, int power, int angle, boolean destructive) {
//        this.location = location;
//        this.power = power;
//        this.angle = angle;
//        this.destructive = destructive;
//
//        this.powerSetting = new MapNumberSetting(
//            "Power",
//            "Power of the jump pad",
//            "/jumppad power <id> <value>",
//            this.power,
//            "",
//            3,
//            10,
//            1,
//            false);
//
//        this.angleSetting = new MapNumberSetting(
//            "Angle",
//            "Angle of the jump pad",
//            "/jumppad angle <id> <value>",
//            this.angle,
//            "°",
//            15,
//            90,
//            15,
//            false);
//
//        this.destructiveSetting = new MapToggleSetting(
//            "Destructive",
//            "Jump pad to replace itself with a slime block",
//            "/jumppad destructive <id> <value>",
//            this.destructive);
//    }
//
//    public String getLocation() {
//        return this.location;
//    }
//
//    public int getPower() {
//        return this.power;
//    }
//
//    public void setPower(int power) {
//        this.power = power;
//        this.powerSetting.setCurrent(power);
//    }
//
//    public int getAngle() {
//        return this.angle;
//    }
//
//    public void setAngle(int angle) {
//        this.angle = angle;
//        this.angleSetting.setCurrent(angle);
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
//        Map<String, Object> pad = new HashMap<>();
//        pad.put("location", this.location);
//        if (this.power != 5) pad.put("power", this.power);
//        if (this.angle != 45) pad.put("angle", this.angle);
//        if (!this.destructive) pad.put("destructive", false);
//        return pad;
//    }
//
//    public void openBook(Player player, int id) {
//        new BookBuilder()
//            .pages(new PageBuilder()
//                .add(new ComponentBuilder("⬅ ")
//                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Back").create()))
//                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/jumppads"))
//                    .bold(true)
//                    .create())
//                .add(new ComponentBuilder("Jump Pad #" + id)
//                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.location + "\n/jumppad preview " + id).create()))
//                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/jumppad preview " + id))
//                    .create()).newLine().newLine()
//                .add(this.powerSetting.getName()).newLine()
//                .add(this.powerSetting.getValue(id)).newLine().newLine()
//                .add(this.angleSetting.getName()).newLine()
//                .add(this.angleSetting.getValue(id)).newLine().newLine()
//                .add(this.destructiveSetting.getName()).newLine()
//                .add(this.destructiveSetting.getValue(id))
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
//        CommandUtils.sendAlertMessage(player, "This preview will last for 10 seconds and shows you what this jump pad will look like in-game");
//
//        Location location = YamlUtils.getLocationFromCoords(player, this.location);
//        Location tp = location.clone().add(0.5, 1, 0.5);
//        tp.setPitch(player.getLocation().getPitch());
//        tp.setYaw(player.getLocation().getYaw());
//        player.teleport(tp);
//
//        Block block = player.getWorld().getBlockAt(location);
//        BlockState state = block.getState();
//
//        if (this.destructive) {
//            block.setType(Material.SLIME_BLOCK);
//        }
//
//        BukkitRunnable launchRunnable = this.getLaunchRunnable(block.getLocation());
//        launchRunnable.runTaskTimerAsynchronously(Commons.getPlugin(), 0L, 2L);
//
//        BukkitRunnable particleRunnable = this.getParticleRunnable(location.clone().add(0.5, 1, 0.5));
//        particleRunnable.runTaskTimerAsynchronously(Commons.getPlugin(), 0L, 5L);
//
//        new BukkitRunnable() {
//
//            @Override
//            public void run() {
//                state.update(true);
//                particleRunnable.cancel();
//                launchRunnable.cancel();
//                previewing = false;
//            }
//
//        }.runTaskLater(Commons.getPlugin(), 200);
//    }
//
//    private BukkitRunnable getLaunchRunnable(Location jumpPadLoc) {
//        return new BukkitRunnable() {
//            @Override
//            public void run() {
//                for (Player player : Bukkit.getOnlinePlayers()) {
//                    if (player.isSneaking()) continue;
//
//                    Location playerLoc = player.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation();
//
//                    if (playerLoc.equals(jumpPadLoc)) {
//                        Location launchLocation = player.getLocation().clone();
//                        launchLocation.setPitch((float) -angle);
//                        player.setVelocity(launchLocation.getDirection().normalize().multiply((power * 5D) / 15D));
//
//                        player.getWorld().playSound(playerLoc, Sound.WITHER_SHOOT, 1F, 1F);
//                        player.getWorld().playEffect(playerLoc.add(0.5, 1.25, 0.5), Effect.EXPLOSION_LARGE, 1, 1);
//                    }
//                }
//            }
//        };
//    }
//
//    private BukkitRunnable getParticleRunnable(Location location) {
//        return new BukkitRunnable() {
//            @Override
//            public void run() {
//                for (Player player : Bukkit.getOnlinePlayers()) {
//                    player.spigot().playEffect(location, Effect.INSTANT_SPELL, 0, 0, 0.2F, 0.25F, 0.20F, 100F, 10, 50);
//                }
//            }
//        };
//    }
//
//}
