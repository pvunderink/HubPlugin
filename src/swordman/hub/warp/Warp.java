package swordman.hub.warp;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import swordman.hub.Main;

public class Warp {
	
	String name;
	
	public Warp(String name) {
		this.name = name;
	}
	
	public void teleport(Player p) {
		p.sendMessage(Main.PREFIX + ChatColor.GOLD + "Warped to " + name);
	}
	
	public String getName() {
		return name;
	}

}
