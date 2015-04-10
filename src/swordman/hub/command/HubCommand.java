package swordman.hub.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import swordman.hub.gui.GuiManager;

public class HubCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			
			if (GuiManager.MAIN != null) {
				GuiManager.MAIN.show(p);
			}
		}
		return true;
	}

}
