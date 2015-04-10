package swordman.hub.warp;

import org.bukkit.entity.Player;

import swordman.hub.network.Server;

public class ExternalWarp extends Warp {
	
	private final Server server;
	
	public ExternalWarp(String name, Server server) {
		super(name);
		this.server = server;
	}

	public void teleport(Player p) {
		server.connect(p);
	}
	
	public Server getServer() {
		return server;
	}
	
}
