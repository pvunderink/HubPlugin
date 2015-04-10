package swordman.hub.network;

import org.bukkit.entity.Player;

public class Server {
		
	String name;
	String host;
	short port;
	int max_players;
	int player_count;
	
	public Server(String name) {
		this.name = name;
	}
	
	public void connect(Player p) {
		NetworkManager.connectPlayerToServer(p, this);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setHostName(String host) {
		this.host = host;
	}
	
	public void setPort(short port) {
		this.port = port;
	}
	
	public void setMaxPlayers(int max_players) {
		this.max_players = max_players;
	}
	
	public void setPlayerCount(int player_count) {
		this.player_count = player_count;
	}
	
	public String getName() {
		return name;
	}
	
	public String getHostName() {
		return host;
	}
	
	public short getPort() {
		return port;
	}
	
	public int getMaxPlayers() {
		return max_players;
	}
	
	public int getPlayerCount() {
		return player_count;
	}
	
}
