package swordman.hub.command;

import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import swordman.hub.Main;
import swordman.hub.gui.GuiManager;

public class GuiCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;

			if (label.equalsIgnoreCase("gui")) {
				if (args.length > 0) {
					String gui = args[0];

					if (GuiManager.containsGui(gui)) {
						GuiManager.getGui(gui).show(p);
					} else {
						p.sendMessage(Main.PREFIX + ChatColor.RED + "Could not find that GUI");
					}
				} else {
					p.sendMessage(Main.PREFIX + ChatColor.RED + "Usage: /gui <gui_name>");
				}
			} else if (label.equalsIgnoreCase("guis") || label.equalsIgnoreCase("guilist")) {
				if (GuiManager.getGuiList().size() > 0) {
					StringBuilder sb = new StringBuilder(Main.PREFIX + ChatColor.GOLD + "GUIs" + ChatColor.DARK_RED + ": " + ChatColor.GOLD);

					Iterator<String> it = GuiManager.getGuiList().keySet().iterator();
					while (it.hasNext()) {
						String gui = it.next();
						
						sb.append(gui);
						sb.append(ChatColor.DARK_RED + ", " + ChatColor.GOLD);
					}
					sb.delete(sb.length() - 5, sb.length() - 1);

					p.sendMessage(sb.toString());
				} else {
					p.sendMessage(Main.PREFIX + ChatColor.RED + "No warps found");
				}
			}
		}
		return true;
	}

}
