package swordman.hub;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import swordman.hub.command.GuiCommand;
import swordman.hub.command.HubCommand;
import swordman.hub.command.WarpCommand;
import swordman.hub.gui.GuiManager;
import swordman.hub.network.NetworkManager;
import swordman.hub.timer.UpdateTimer;
import swordman.hub.warp.WarpManager;

public class Main extends JavaPlugin {

	public static String NAME = "Hub";
	public static String VERSION = "v1.0";
	public static String PREFIX = "[Hub]";

	private NetworkManager network_manager;
	private WarpManager warp_manager;
	private GuiManager gui_manager;
	
	public void onEnable() {
		NAME = getDescription().getName();
		VERSION = getDescription().getVersion();
		PREFIX = ChatColor.BLACK + "[" + ChatColor.DARK_RED + "Hub" + ChatColor.BLACK + "] ";
		
		// Load servers
		network_manager = new NetworkManager(this);
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", network_manager);
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, new UpdateTimer(this), 0L, 20L);
		
		// Load warps
		warp_manager = new WarpManager();
		warp_manager.loadWarps();
		
		// Load gui panes and buttons
		gui_manager = new GuiManager(this);
		gui_manager.loadPanesAndButtons();
		
		// Load commands
		getCommand("hub").setExecutor(new HubCommand());
		getCommand("gui").setExecutor(new GuiCommand());
		getCommand("guilist").setExecutor(new GuiCommand());
		getCommand("warp").setExecutor(new WarpCommand());
		getCommand("setwarp").setExecutor(new WarpCommand());
		getCommand("warps").setExecutor(new WarpCommand());

		getLogger().log(Level.INFO, "Has been enabled");
	}

	public void onDisable() {
		// Save warps
		warp_manager.saveWarps();
		
		// Save gui panes and buttons
		gui_manager.savePanesAndButtons();
		
		Bukkit.getScheduler().cancelTasks(this);
		
		getLogger().log(Level.INFO, "Has been disabled");
	}

	public NetworkManager getNetworkManager() {
		return network_manager;
	}
	
	public WarpManager getWarpManager() {
		return warp_manager;
	}

}
