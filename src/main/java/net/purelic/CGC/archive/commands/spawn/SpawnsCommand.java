//package net.purelic.CGC.commands.archive.spawn;
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
//import net.purelic.CGC.core.maps.MapYaml;
//import net.purelic.CGC.core.maps.constants.MatchTeam;
//import net.purelic.CGC.managers.MapManager;
//import net.purelic.commons.Commons;
//import net.purelic.commons.commands.parsers.CustomCommand;
//import net.purelic.commons.commands.parsers.Permission;
//import net.purelic.commons.utils.CommandUtils;
//import net.purelic.commons.utils.YamlUtils;
//import net.purelic.commons.utils.book.BookBuilder;
//import net.purelic.commons.utils.book.PageBuilder;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//public class SpawnsCommand implements CustomCommand {
//
//    @Override
//    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
//        return mgr.commandBuilder("spawns")
//            .senderType(Player.class)
//            .permission(Permission.isMapDev(true))
//            .argument(EnumArgument.optional(MatchTeam.class, "team"))
//            .handler(c -> {
//                Player player = (Player) c.getSender();
//                Optional<MatchTeam> teamArg = c.getOptional("team");
//
//                if (player.getWorld() == Commons.getLobby()) {
//                    CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
//                } else {
//                    CustomMap map = MapManager.getMap(player.getWorld().getName());
//                    MapYaml yaml = map.getYaml();
//
//                    if (teamArg.isPresent()) {
//                        SpawnsCommand.openBook(player, teamArg.get(), yaml);
//                    } else {
//                        new BookBuilder()
//                            .pages(
//                                new PageBuilder()
//                                    .add(new ComponentBuilder("⬅ ")
//                                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Back").create()))
//                                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/edit"))
//                                        .bold(true)
//                                        .create())
//                                    .add("Spawn Points").newLine().newLine()
//                                    .add(new ComponentBuilder("1. Obs (" + YamlUtils.formatCoords(yaml.getObsSpawn(), true, true) + ")")
//                                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/setobsspawn").create()))
//                                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/setobsspawn"))
//                                        .create()).newLine()
//                                    .add(this.getSpawnLine(2, yaml, MatchTeam.SOLO)).newLine()
//                                    .add(this.getSpawnLine(3, yaml, MatchTeam.BLUE)).newLine()
//                                    .add(this.getSpawnLine(4, yaml, MatchTeam.RED)).newLine()
//                                    .add(this.getSpawnLine(5, yaml, MatchTeam.GREEN)).newLine()
//                                    .add(this.getSpawnLine(6, yaml, MatchTeam.YELLOW)).newLine()
//                                    .add(this.getSpawnLine(7, yaml, MatchTeam.AQUA)).newLine()
//                                    .add(this.getSpawnLine(8, yaml, MatchTeam.PINK)).newLine()
//                                    .add(this.getSpawnLine(9, yaml, MatchTeam.WHITE)).newLine()
//                                    .add(this.getSpawnLine(10, yaml, MatchTeam.GRAY))
//                                    .build()
//                            )
//                            .open(player);
//                    }
//                }
//            });
//    }
//
//    private BaseComponent[] getSpawnLine(int index, MapYaml yaml, MatchTeam team) {
//        return new ComponentBuilder(index + ". " + YamlUtils.formatEnumString(team.name()))
//            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/spawns " + team.name().toLowerCase()).create()))
//            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/spawns " + team.name().toLowerCase()))
//            .append(" (" + yaml.getSpawns(team).size() + ")").color(ChatColor.GRAY)
//            .create();
//    }
//
//    public static void openBook(Player player, MatchTeam team, MapYaml yaml) {
//        String teamName = YamlUtils.formatEnumString(team.name());
//        List<String> spawns = yaml.getSpawns(team);
//
//        int total = spawns.size();
//
//        if (total == 0) {
//            openEmptyBook(player, team);
//            return;
//        }
//
//        List<BaseComponent[]> pages = new ArrayList<>();
//        PageBuilder page = new PageBuilder();
//        String cmd = "/spawn add " + teamName.toLowerCase();
//
//        int i = 0;
//
//        for (String spawn : spawns) {
//            if (i % 10 == 0) {
//                if (i != 0) pages.add(page.build());
//                page = new PageBuilder();
//                page
//                    .add(new ComponentBuilder("⬅ ")
//                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Back").create()))
//                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/spawns"))
//                        .bold(true)
//                        .create())
//                    .add(total + " " + teamName + " spawn point" + (total == 1 ? "" : "s")).newLine().newLine()
//                    .add(new ComponentBuilder("+ ADD")
//                        .color(ChatColor.DARK_GREEN)
//                        .bold(true)
//                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Add " + teamName + " Spawn Point\n" + cmd).create()))
//                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd))
//                        .create()).newLine();
//            }
//
//            int number = i + 1;
//            String name = YamlUtils.formatCoords(spawn, true, true);
//            String command = "/spawn tp " + teamName.toLowerCase() + " " + number;
//            String hover = "Teleport to Spawn Point\n" + command;
//
//            page.newLine()
//                .add(
//                    new ComponentBuilder(number + ". ")
//                        .append(name)
//                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()))
//                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command))
//                        .create())
//                .add(
//                    new ComponentBuilder("  ")
//                        .append("✗")
//                        .color(ChatColor.DARK_RED)
//                        .bold(true)
//                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("DELETE").color(ChatColor.RED).create()))
//                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/spawn remove " + teamName.toLowerCase() + " " + number))
//                        .create());
//
//            i++;
//        }
//
//        pages.add(page.build());
//
//        new BookBuilder().pages(pages).open(player);
//    }
//
//    private static void openEmptyBook(Player player, MatchTeam team) {
//        String teamName = YamlUtils.formatEnumString(team.name());
//        String cmd = "/spawn add " + teamName.toLowerCase();
//
//        PageBuilder pages = new PageBuilder()
//            .add(new ComponentBuilder("⬅ ")
//                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Back").create()))
//                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/spawns"))
//                .bold(true)
//                .create())
//            .add("0 " + teamName + " spawn points").newLine().newLine()
//            .add(new ComponentBuilder("+ ADD")
//                .color(ChatColor.DARK_GREEN)
//                .bold(true)
//                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Add " + teamName + " Spawn Point\n" + cmd).create()))
//                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd))
//                .create()).newLine();
//
//        new BookBuilder().pages(pages).open(player);
//    }
//
//}
