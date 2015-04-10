package swordman.hub.warp;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import swordman.hub.Main;
import swordman.hub.network.NetworkManager;
import swordman.hub.network.Server;

public class WarpManager {

	private final File file;
	private static List<Warp> warps;

	public WarpManager() {
		file = new File("plugins/" + Main.NAME + "/warps.yml");
		warps = new ArrayList<Warp>();
	}

	public static boolean contains(String name) {
		for (Warp w : warps) {
			if (w.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	public static Warp getWarp(String name) {
		for (Warp w : warps) {
			if (w.getName().equalsIgnoreCase(name)) {
				return w;
			}
		}
		return null;
	}
	
	public static List<Warp> getWarps() {
		return warps;
	}
	
	public static void addWarp(Warp warp) {
		warps.add(warp);
	}
	
	public static void removeWarp(Warp warp) {
		warps.remove(warp);
	}

	public void saveWarps() {
		try {
			saveWarpsUnsafe();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void loadWarps() {
		try {
			loadWarpsUnsafe();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void saveWarpsUnsafe() throws Exception {
		YamlConfiguration config = new YamlConfiguration();

		if (!file.exists()) {
			if (file.getParentFile() != null) {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
		}

		config.load(file);

		for (Warp w : warps) {
			if (w instanceof ExternalWarp) {
				ExternalWarp ew = (ExternalWarp) w;

				config.set("External." + w.getName() + ".Server-Name", ew.getServer().getName());
			} else if (w instanceof LocalWarp) {
				LocalWarp lw = (LocalWarp) w;

				String s = "Local." + w.getName() + ".";

				config.set(s + "World", lw.getLocation().getWorld().getName());
				config.set(s + "X", lw.getLocation().getX());
				config.set(s + "Y", lw.getLocation().getY());
				config.set(s + "Z", lw.getLocation().getZ());
				config.set(s + "Yaw", lw.getLocation().getYaw());
				config.set(s + "Pitch", lw.getLocation().getPitch());
			}
		}

		config.save(file);
	}

	private void loadWarpsUnsafe() throws Exception {
		YamlConfiguration config = new YamlConfiguration();

		if (!file.exists()) {
			if (file.getParentFile() != null) {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
			return;
		}

		config.load(file);

		if (config.contains("External")) {
			Iterator<String> it = config.getConfigurationSection("External").getKeys(false).iterator();

			while (it.hasNext()) {
				String name = it.next();

				if (config.contains("External." + name + ".Server-Name")) {
					String server_name = config.getString("External." + name + ".Server-Name");
					Server server;

					if (NetworkManager.containsServer(server_name)) {
						server = NetworkManager.getServer(server_name);
					} else {
						server = new Server(server_name);
					}

					NetworkManager.addServer(server);

					ExternalWarp ew = new ExternalWarp(name, server);
					warps.add(ew);
				}
			}
		}

		if (config.contains("Local")) {
			Iterator<String> it = config.getConfigurationSection("Local").getKeys(false).iterator();

			while (it.hasNext()) {
				String name = it.next();

				String s = "Local." + name + ".";

				if (config.contains(s + "World") && config.contains(s + "X") && config.contains(s + "Y") && config.contains(s + "Z") && config.contains(s + "Yaw") && config.contains(s + "Pitch")) {
					if (Bukkit.getWorld(config.getString(s + "World")) != null) {
						World w = Bukkit.getWorld(config.getString(s + "World"));
						double x = config.getDouble(s + "X");
						double y = config.getDouble(s + "Y");
						double z = config.getDouble(s + "Z");
						float yaw = (float) config.getDouble(s + "Yaw");
						float pitch = (float) config.getDouble(s + "Pitch");

						Location loc = new Location(w, x, y, z, yaw, pitch);

						LocalWarp lw = new LocalWarp(name, loc);
						warps.add(lw);
					}
				}
			}
		}

		config.save(file);
	}
}
