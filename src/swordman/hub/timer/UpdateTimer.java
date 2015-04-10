package swordman.hub.timer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import swordman.hub.Main;

public class UpdateTimer implements Runnable {

	Main plugin;
	int i = 0;

	public UpdateTimer(Main plugin) {
		this.plugin = plugin;
	}

	public void run() {
		if (i == 0) {
			i = 1;

			if (Bukkit.getOnlinePlayers().length > 0) {
				Player p = Bukkit.getOnlinePlayers()[0];

				plugin.getNetworkManager().update(p);
			}
		} else {
			i = 0;

			// Update something
		}
	}

}