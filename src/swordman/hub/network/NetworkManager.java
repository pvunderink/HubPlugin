package swordman.hub.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import swordman.hub.Main;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class NetworkManager implements PluginMessageListener {

	private static Main plugin;

	private static List<Server> serverList = new ArrayList<Server>();
	private static String[] tempServerList = new String[0];

	public NetworkManager(Main plugin) {
		NetworkManager.plugin = plugin;
	}

	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}

		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();

		if (subchannel.equals("GetServers")) {
			tempServerList = in.readUTF().split(", ");
		} else if (subchannel.equalsIgnoreCase("ServerIP")) {
			String name = in.readUTF();
			String ip = in.readUTF();
			short port = in.readShort();

			if (containsTempServer(name)) {
				if (containsServer(name)) {
					serverList.remove(getServer(name));
				}

				Server server = new Server(name);
				server.setHostName(ip);
				server.setPort(port);

				serverList.add(server);
			}
		}
	}

	public void update(Player player) {
		this.requestServerList(player);
		this.requestServerIPAll(player);
		this.requestPlayerCountAll();
	}

	public void requestServerList(Player player) {
		NetworkManager.sendPluginMessage(player, "GetServers");
	}

	public void requestServerIP(Player player, String server) {
		NetworkManager.sendPluginMessage(player, "ServerIP", server);
	}

	public void requestServerIPAll(Player player) {
		for (int i = 0; i < tempServerList.length; i++) {
			this.requestServerIP(player, tempServerList[i]);
		}
	}

	public void requestPlayerCount(Server server) {
		try {
			Socket socket = new Socket(server.getHostName(), server.getPort());

			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			DataInputStream in = new DataInputStream(socket.getInputStream());

			out.write(0xFE);

			int b;
			StringBuffer str = new StringBuffer();

			while ((b = in.read()) != -1) {
				if (b != 0 && b > 16 && b != 255 && b != 23 && b != 24) {
					str.append((char) b);
				}
			}

			socket.close();

			String[] data = str.toString().split("§");
			int player_count = Integer.parseInt(data[1]);
			int max_players = Integer.parseInt(data[2]);

			server.setMaxPlayers(max_players);
			server.setPlayerCount(player_count);
		} catch (UnknownHostException e) {
		} catch (IOException e) {
		}
	}

	public void requestPlayerCountAll() {
		for (int i = 0; i < serverList.size(); i++) {
			requestPlayerCount(serverList.get(i));
		}
	}

	public static void connectPlayerToServer(Player player, Server server) {
		NetworkManager.sendPluginMessage(player, "Connect", server.getName());
	}

	public static void sendPluginMessage(Player player, String subchannel, Object... args) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(subchannel);

		for (int i = 0; i < args.length; i++) {
			if (args[i] instanceof String) {
				out.writeUTF((String) args[i]);
			} else if (args[i] instanceof Short) {
				out.writeShort((Short) args[i]);
			}
		}

		player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}

	private boolean containsTempServer(String server) {
		for (int i = 0; i < tempServerList.length; i++) {
			if (tempServerList[i].equalsIgnoreCase(server)) {
				return true;
			}
		}
		return false;
	}

	public static boolean containsServer(String server) {
		for (Server s : serverList) {
			if (s.getName().equalsIgnoreCase((server))) {
				return true;
			}
		}
		return false;
	}

	public static boolean containsServer(String hostname, short port) {
		for (Server s : serverList) {
			if (s.getHostName().equalsIgnoreCase((hostname)) && s.getPort() == port) {
				return true;
			}
		}
		return false;
	}

	public static Server getServer(String server) {
		for (Server s : serverList) {
			if (s.getName().equalsIgnoreCase((server))) {
				return s;
			}
		}
		return null;
	}

	public static Server getServer(String hostname, int port) {
		for (Server s : serverList) {
			if (s.getHostName().equalsIgnoreCase((hostname)) && s.getPort() == port) {
				return s;
			}
		}
		return null;
	}

	public static void addServer(Server server) {
		if (containsServer(server.getName())) {
			serverList.remove(getServer(server.getName()));
		}
		serverList.add(server);
	}

	public String[] getTempServerList() {
		return tempServerList;
	}

	public void setTempServerList(String[] tempServerList) {
		NetworkManager.tempServerList = tempServerList;
	}

}
