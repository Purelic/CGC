package net.purelic.CGC;

import net.purelic.CGC.commands.gamemode.*;
import net.purelic.CGC.commands.map.*;
import net.purelic.CGC.commands.world.ClearEntitiesCommand;
import net.purelic.CGC.commands.world.LobbyCommand;
import net.purelic.CGC.commands.world.TeleportCommand;
import net.purelic.CGC.commands.world.TimeCommand;
import net.purelic.CGC.gamemodes.settings.constants.GameModeSetting;
import net.purelic.CGC.listeners.MapLoad;
import net.purelic.CGC.listeners.OpStatusChange;
import net.purelic.CGC.listeners.PlayerJoin;
import net.purelic.CGC.listeners.PlayerQuit;
import net.purelic.CGC.maps.constants.MapElementType;
import net.purelic.CGC.modules.DynamicGameModeSettingsModules;
import net.purelic.CGC.modules.GriefProtectionModule;
import net.purelic.CGC.modules.TieredChestModule;
import net.purelic.CGC.runnables.AutoSave;
import net.purelic.commons.Commons;
import net.purelic.commons.modules.*;
import net.purelic.commons.profile.Preference;
import net.purelic.commons.runnables.MapDownloader;
import net.purelic.commons.utils.TaskUtils;
import org.bukkit.plugin.java.JavaPlugin;

public class CGC extends JavaPlugin {

    @Override
    public void onEnable() {
        this.registerListeners();
        this.registerModules();
        this.registerCommands();
        TaskUtils.runAsync(new MapDownloader(this.getLobby(), true));
        Commons.setServerReady();
    }

    private String getLobby() {
        if (Commons.hasOwner()) {
            return (String) Commons.getProfile(Commons.getOwnerId()).getPreference(Preference.LOBBY, "Default Lobby");
        } else {
            return "Default Lobby";
        }
    }

    @Override
    public void onDisable() {
        if (AutoSave.isEnabled()) AutoSave.saveAll(false, true);
    }

    private void registerListeners() {
        Commons.registerListener(new MapLoad());
        Commons.registerListener(new OpStatusChange());
        Commons.registerListener(new PlayerJoin());
        Commons.registerListener(new PlayerQuit());
    }

    private void registerModules() {
        // Commons
        new BasicScoreboardModule().register();
        new BlockPhysicsModule().register();
        new FancyChatModule().register();
        new NoHungerModule().register();
        new NoLeavesDecayModule().register();
        new NoPlayerDamageModule(true).register();

        // CGC
        new DynamicGameModeSettingsModules().register();
        new GriefProtectionModule().register();
        new TieredChestModule().register();
    }

    private void registerCommands() {
        // Map Element Commands
        for (MapElementType objectType : MapElementType.values()) {
            objectType.registerCommands();
        }

        // Game Mode Commands
        GameModeSetting.registerSettingCommands();
        Commons.registerCommand(new GameModeAliasCommand());
        Commons.registerCommand(new GameModeCopyCommand());
        Commons.registerCommand(new GameModeCreateCommand());
        Commons.registerCommand(new GameModeDeleteCommand());
        Commons.registerCommand(new GameModeDeleteConfirmCommand());
        Commons.registerCommand(new GameModeDescriptionCommand());
        Commons.registerCommand(new GameModeEditCommand());
        Commons.registerCommand(new GameModePublishCommand());
        Commons.registerCommand(new GameModePullCommand());
        Commons.registerCommand(new GameModePushCommand());
        Commons.registerCommand(new GameModeRenameCommand());
        Commons.registerCommand(new GameModeSaveCommand());
        Commons.registerCommand(new GameModesCommand());
        Commons.registerCommand(new GameModeUnpublishCommand());

        // Map Commands
        Commons.registerCommand(new AuthorAddCommand());
        Commons.registerCommand(new AuthorRemoveCommand());
        Commons.registerCommand(new AutoSaveCommand());
        Commons.registerCommand(new CommandsCommand());
        Commons.registerCommand(new CopyCommand());
        Commons.registerCommand(new CreateCommand());
        Commons.registerCommand(new DeleteConfirmMapCommand());
        Commons.registerCommand(new DeleteMapCommand());
        Commons.registerCommand(new DownloadCommand());
        Commons.registerCommand(new EditCommand());
        Commons.registerCommand(new ExportCommand());
        Commons.registerCommand(new MapCommand());
        Commons.registerCommand(new MapsCommand());
        Commons.registerCommand(new OffsetCommand());
        Commons.registerCommand(new PublishCommand());
        Commons.registerCommand(new PublishedCommand());
        Commons.registerCommand(new PullCommand());
        Commons.registerCommand(new PushCommand());
        Commons.registerCommand(new RenameCommand());
        Commons.registerCommand(new SaveAllCommand());
        Commons.registerCommand(new SaveCommand());
        Commons.registerCommand(new SetObsSpawnCommand());
        Commons.registerCommand(new ShopCommand());

        // World Commands
        Commons.registerCommand(new ClearEntitiesCommand());
        Commons.registerCommand(new LobbyCommand());
        Commons.registerCommand(new TeleportCommand());
        Commons.registerCommand(new TimeCommand());
    }

}
