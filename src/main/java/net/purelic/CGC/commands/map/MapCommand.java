package net.purelic.CGC.commands.map;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.purelic.CGC.maps.CustomMap;
import net.purelic.CGC.maps.MapYaml;
import net.purelic.CGC.managers.MapManager;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.Fetcher;
import net.purelic.commons.utils.PlayerUtils;
import net.purelic.commons.utils.book.BookBuilder;
import net.purelic.commons.utils.book.PageBuilder;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ocpsoft.prettytime.PrettyTime;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

public class MapCommand implements CustomCommand {

    private final PrettyTime pt = new PrettyTime();
    private final SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    private final int authorLimit = 3;

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("map")
            .senderType(Player.class)
            .argument(StringArgument.optional("name", StringArgument.StringMode.GREEDY))
            .handler(context -> mgr.taskRecipe().begin(context).synchronous(c -> {
                Player player = (Player) c.getSender();
                Optional<String> nameArg = c.getOptional("name");

                if (!nameArg.isPresent()) {
                    if (player.getWorld() == Commons.getLobby()) {
                        CommandUtils.sendAlertMessage(player, "You are currently in the Lobby");
                    } else {
                        CustomMap map = MapManager.getMap(player.getWorld().getName());
                        MapYaml yaml = map.getYaml();
                        List<String> authors = yaml.getAuthors();

                        PageBuilder pageBuilder = new PageBuilder()
                            .add("Name: ").newLine()
                            .add(map.getName()).newLine().newLine()
                            .add("Created: ").newLine()
                            .add(new ComponentBuilder(this.pt.format(yaml.getCreated()))
                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.df.format(yaml.getCreated())).create()))
                                .create()).newLine().newLine()
                            .add("Updated: ").newLine()
                            .add(new ComponentBuilder(this.pt.format(yaml.getUpdated()))
                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.df.format(yaml.getUpdated())).create()))
                                .create()).newLine().newLine();

                        if (authors.size() == 1) {
                            pageBuilder.add("Author:").newLine().add(Fetcher.getFancyName(authors.get(0)));
                        } else {
                            pageBuilder.add("Authors:");

                            boolean compact = authors.size() > this.authorLimit;

                            int l = 0;
                            for (String uuid : authors) {
                                if (compact && l == this.authorLimit - 1) break;
                                l++;
                                pageBuilder.newLine().add(" • ").add(Fetcher.getFancyName(uuid));
                            }

                            if (compact) {
                                String hover = "";

                                for (int i = this.authorLimit - 1; i < authors.size(); i++) {
                                    if (i != this.authorLimit - 1) hover += "\n";
                                    hover += " • " + Fetcher.getBasicName(authors.get(i));
                                }

                                pageBuilder.newLine().add(new ComponentBuilder(" • and " + (authors.size() - 2) + " others")
                                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()))
                                    .create());
                            }
                        }

                        new BookBuilder().pages(pageBuilder).open(player);
                    }
                    return;
                }

                String name = nameArg.get();

                if (MapManager.hasMap(name)) {
                    CustomMap map = MapManager.getMap(name);
                    String mapName = map.getName();

                    if (MapManager.isDownloaded(name)) {
                        player.setGameMode(GameMode.CREATIVE);
                        player.setFlying(true);
                        map.teleport(player);
                        CommandUtils.sendSuccessMessage(player, "Teleported to \"" + mapName + "\"!");
                    } else {
                        // Run the download command if the map isn't downloaded
                        PlayerUtils.performCommand(player, "download " + mapName);
                    }
                } else {
                    CommandUtils.sendErrorMessage(player, "Can't find map \"" + name + "\"");
                }
            }).execute());
    }

}
