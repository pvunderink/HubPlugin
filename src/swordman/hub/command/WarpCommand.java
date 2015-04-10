package swordman.hub.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import swordman.hub.Main;
import swordman.hub.network.NetworkManager;
import swordman.hub.warp.ExternalWarp;
import swordman.hub.warp.LocalWarp;
import swordman.hub.warp.WarpManager;

public class WarpCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (label.equalsIgnoreCase("warp")) {
				if (args.length > 0) {
					if (WarpManager.contains(args[0])) {
						WarpManager.getWarp(args[0]).teleport(p);
					}
				} else {
					p.sendMessage(Main.PREFIX + ChatColor.RED + "Usage: /warp <warp_name>");
				}
			} else if (label.equalsIgnoreCase("setwarp")) {
				if (args.length > 0) {
					if (args.length > 1) {
						if (!WarpManager.contains(args[0])) {
							if (NetworkManager.containsServer(args[1])) {
								ExternalWarp warp = new ExternalWarp(args[0], NetworkManager.getServer(args[1]));
								WarpManager.addWarp(warp);

								p.sendMessage(Main.PREFIX + ChatColor.GOLD + "Created new warp to " + args[1]);
							} else {
								p.sendMessage(Main.PREFIX + ChatColor.RED + "Could not find that server");
							}
						} else {
							p.sendMessage(Main.PREFIX + ChatColor.RED + "That warp already exists");
						}
					} else {
						if (!WarpManager.contains(args[0])) {
							WarpManager.addWarp(new LocalWarp(args[0], p.getLocation()));
						} else {
							p.sendMessage(Main.PREFIX + ChatColor.RED + "That warp already exists");
						}
					}
				} else {
					p.sendMessage(Main.PREFIX + ChatColor.RED + "Usage: /setwarp <warpname> or /setwarp <warp_name> <server_name>");
				}
			} else if (label.equalsIgnoreCase("warps")) {
				if (WarpManager.getWarps().size() > 0) {
					StringBuilder sb = new StringBuilder(Main.PREFIX + ChatColor.GOLD + "Warps" + ChatColor.DARK_RED + ": " + ChatColor.GOLD);

					int j = 0;
					for (int i = 0; i < WarpManager.getWarps().size() - 1; i++) {
						String w = WarpManager.getWarps().get(i).getName();
						sb.append(w);
						sb.append(ChatColor.DARK_RED + ", " + ChatColor.GOLD);
						j++;
					}
					sb.append(WarpManager.getWarps().get(j).getName());

					p.sendMessage(sb.toString());
				} else {
					p.sendMessage(Main.PREFIX + ChatColor.RED + "No warps found");
				}
			}
		}
		return true;
	}

}
