package swordman.hub.warp;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class LocalWarp extends Warp {

	private final Location location;
	
	public LocalWarp(String name, Location location) {
		super(name);
		this.location = location;
	}
	
	public void teleport(Player p) {
		p.teleport(location, TeleportCause.PLUGIN);
	}
	
	public Location getLocation() {
		return location;
	}

}
