//package net.purelic.CGC.commands.archive.chests;
//
//import cloud.commandframework.Command;
//import cloud.commandframework.arguments.standard.EnumArgument;
//import cloud.commandframework.bukkit.BukkitCommandManager;
//import net.md_5.bungee.api.ChatColor;
//import net.md_5.bungee.api.chat.BaseComponent;
//import net.md_5.bungee.api.chat.ClickEvent;
//import net.md_5.bungee.api.chat.ComponentBuilder;
//import net.md_5.bungee.api.chat.HoverEvent;
//import net.purelic.CGC.core.maps.CustomMap;
//import net.purelic.CGC.managers.MapManager;
//import net.purelic.CGC.core.maps.MapYaml;
//import net.purelic.CGC.core.maps.constants.ChestTier;
//import net.purelic.CGC.utils.YamlUtils;
//import net.purelic.commons.Commons;
//import net.purelic.commons.commands.parsers.CustomCommand;
//import net.purelic.commons.utils.constants.PermissionType;
//import net.purelic.commons.utils.CommandUtils;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;
//import org.bukkit.inventory.ItemStack;
//import xyz.upperlevel.spigot.book.BookUtil;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//public class ChestsCommand implements CustomCommand {
//
//    @Override
//    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
//        return mgr.commandBuilder("chests")
//                .senderType(Player.class)
//                .argument(EnumArgument.optional(ChestTier.class, "tier"))
//                .handler(c -> {
//                    Player player = (Player) c.getSender();
//                    Optional<ChestTier> tierArg = c.getOptional("tier");
//
//                    if (!CommandUtils.hasPermission(player, PermissionType.MAP_DEV, true)) {
//                        return;
//                    }
//
//                    if (player.getWorld() == Commons.getLobby()) {
//                        CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
//                    } else {
//                        CustomMap map = MapManager.getMap(player.getWorld().getName());
//                        MapYaml yaml = map.getYaml();
//                        yaml.cleanChests(player.getWorld());
//
//                        if (tierArg.isPresent()) {
//                            ChestsCommand.openBook(player, tierArg.get(), yaml);
//                        } else {
//                            ItemStack book = BookUtil.writtenBook()
//                                    .pages(
//                                        new BookUtil.PageBuilder()
//                                            .add(new ComponentBuilder("⬅ ")
//                                                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Back").create()))
//                                                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/edit"))
//                                                    .bold(true)
//                                                    .create())
//                                            .add("Chests").newLine().newLine()
//                                            .add(this.getChestLine(1, yaml, ChestTier.TIER_1)).newLine()
//                                            .add(this.getChestLine(2, yaml, ChestTier.TIER_2)).newLine()
//                                            .add(this.getChestLine(3, yaml, ChestTier.TIER_3)).newLine()
//                                            .build()
//                                    )
//                                    .build();
//
//                            BookUtil.openPlayer(player, book);
//                        }
//                    }
//                });
//    }
//
//    private BaseComponent[] getChestLine(int index, MapYaml yaml, ChestTier tier) {
//        return new ComponentBuilder(index + ". " + YamlUtils.formatEnumString(tier.name()))
//                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/chests " + tier.name().toLowerCase()).create()))
//                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chests " + tier.name().toLowerCase()))
//                .append(" (" + yaml.getChests(tier).size() + ")").color(ChatColor.GRAY)
//                .create();
//    }
//
//    public static void openBook(Player player, ChestTier tier, MapYaml yaml) {
//        String tierName = YamlUtils.formatEnumString(tier.name());
//        List<String> chests = yaml.getChests(tier);
//
//        int total = chests.size();
//
//        if (total == 0) {
//            openEmptyBook(player, tier);
//            return;
//        }
//
//        List<BaseComponent[]> pages = new ArrayList<>();
//        BookUtil.PageBuilder page = new BookUtil.PageBuilder();
//        String cmd = "/chest add " + tier.name().toLowerCase();
//
//        int i = 0;
//
//        for (String chest : chests) {
//            if (i % 10 == 0) {
//                if (i != 0) pages.add(page.build());
//                page = new BookUtil.PageBuilder();
//                page
//                        .add(new ComponentBuilder("⬅ ")
//                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Back").create()))
//                                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chests"))
//                                .bold(true)
//                                .create())
//                        .add(total + " " + tierName + " chest" + (total == 1 ? "" : "s")).newLine().newLine()
//                        .add(new ComponentBuilder("+ ADD")
//                                .color(ChatColor.DARK_GREEN)
//                                .bold(true)
//                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Add " + tier.name().toLowerCase() + " Chest\n" + cmd).create()))
//                                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd))
//                                .create()).newLine();
//            }
//
//            int number = i + 1;
//            String name = YamlUtils.formatCoords(chest, true, true);
//            String command = "/chest tp " + tierName.toLowerCase() + "_" + number;
//            String hover = "Teleport to Chest\n" + command;
//
//            page.newLine()
//                    .add(
//                            new ComponentBuilder(number + ". ")
//                                    .append(name)
//                                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()))
//                                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command))
//                                    .create())
//                    .add(
//                            new ComponentBuilder("  ")
//                                    .append("✗")
//                                    .color(ChatColor.DARK_RED)
//                                    .bold(true)
//                                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("DELETE").color(ChatColor.RED).create()))
//                                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chest remove " + tier.name().toLowerCase() + " " + number))
//                                    .create());
//
//            i++;
//        }
//
//        pages.add(page.build());
//
//        ItemStack book = BookUtil.writtenBook().pages(pages).build();
//        BookUtil.openPlayer(player, book);
//    }
//
//    private static void openEmptyBook(Player player, ChestTier tier) {
//        String tierName = YamlUtils.formatEnumString(tier.name());
//        String cmd = "/chest add " + tier.name().toLowerCase();
//
//        BookUtil.PageBuilder page = new BookUtil.PageBuilder()
//                .add(new ComponentBuilder("⬅ ")
//                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Back").create()))
//                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chests"))
//                        .bold(true)
//                        .create())
//                .add("0 " + tierName + " chests").newLine().newLine()
//                .add(new ComponentBuilder("+ ADD")
//                        .color(ChatColor.DARK_GREEN)
//                        .bold(true)
//                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Add " + tierName + " Chest\n" + cmd).create()))
//                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd))
//                        .create()).newLine();
//
//        ItemStack book = BookUtil.writtenBook().pages(page.build()).build();
//        BookUtil.openPlayer(player, book);
//    }
//
//}
