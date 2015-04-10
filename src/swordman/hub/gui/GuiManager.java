package swordman.hub.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import swordman.hub.Main;
import swordman.hub.warp.WarpManager;

public class GuiManager {

	private final Main plugin;
	private final File pane_file;
	private final File button_file;
	private static HashMap<String, GuiPane> panes;
	private static HashMap<String, GuiButton> buttons;

	public static GuiPane MAIN = new GuiPane(1, "Main");

	public GuiManager(Main plugin) {
		this.plugin = plugin;
		pane_file = new File("plugins/" + Main.NAME + "/panes.yml");
		button_file = new File("plugins/" + Main.NAME + "/buttons.yml");
		buttons = new HashMap<String, GuiButton>();
		panes = new HashMap<String, GuiPane>();
	}

	public static boolean containsGui(String name) {
		return panes.containsKey(name);
	}

	public static GuiPane getGui(String name) {
		return panes.get(name);
	}
	
	public static HashMap<String, GuiPane> getGuiList() {
		return panes;
	}

	public void savePanesAndButtons() {
		try {
			saveButtonsUnsafe();
			savePanesUnsafe();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void loadPanesAndButtons() {
		try {
			loadButtonsUnsafe();
			loadPanesUnsafe();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void savePanesUnsafe() throws Exception {
		YamlConfiguration config = new YamlConfiguration();

		if (!pane_file.exists()) {
			if (pane_file.getParentFile() != null) {
				pane_file.getParentFile().mkdirs();
			}
			pane_file.createNewFile();
		}

		config.load(pane_file);

		Iterator<String> it = panes.keySet().iterator();

		while (it.hasNext()) {
			String id = it.next();
			GuiPane gp = panes.get(id);

			config.set(id + ".Size", gp.getSize() / 9);

			if (MAIN == gp)
				config.set(id + ".Main", true);

			if (gp.getContent() != null) {
				for (int i = 0; i < gp.getContent().length; i++) {
					GuiButton gb = gp.getContent()[i];

					if (gb != null)
						config.set(id + ".Content." + gb.id + ".Slot", i);
				}
			}
		}

		config.save(pane_file);
	}

	private void saveButtonsUnsafe() throws Exception {
		YamlConfiguration config = new YamlConfiguration();

		if (!button_file.exists()) {
			if (button_file.getParentFile() != null) {
				button_file.getParentFile().mkdirs();
			}
			button_file.createNewFile();
		}

		config.load(button_file);

		Iterator<String> it = buttons.keySet().iterator();

		while (it.hasNext()) {
			String id = it.next();
			GuiButton gb = buttons.get(id);

			config.set(id + ".Material", gb.icon_material.toString());
			config.set(id + ".Title", gb.icon_title);
			config.set(id + ".Description", gb.icon_description);

			if (gb instanceof GuiWarpButton) {
				GuiWarpButton wb = (GuiWarpButton) gb;

				config.set(id + ".Warp", wb.warp.getName());
			}
		}

		config.save(button_file);
	}

	private void loadPanesUnsafe() throws Exception {
		YamlConfiguration config = new YamlConfiguration();

		if (!pane_file.exists()) {
			if (pane_file.getParentFile() != null) {
				pane_file.getParentFile().mkdirs();
			}
			pane_file.createNewFile();
			return;
		}

		config.load(pane_file);

		Iterator<String> it = config.getKeys(false).iterator();
		while (it.hasNext()) {
			String id = it.next();
			if (!panes.containsKey(id))
				loadPane(config, id);
		}

		config.save(pane_file);
	}

	private void loadButtonsUnsafe() throws Exception {
		YamlConfiguration config = new YamlConfiguration();

		if (!button_file.exists()) {
			if (button_file.getParentFile() != null) {
				button_file.getParentFile().mkdirs();
			}
			button_file.createNewFile();
			return;
		}

		config.load(button_file);

		Iterator<String> it = config.getKeys(false).iterator();
		while (it.hasNext()) {
			String id = it.next();
			if (!buttons.containsKey(id))
				loadButton(config, id);
		}

		config.save(button_file);
	}

	private void loadPane(YamlConfiguration config, String id) {
		boolean main = false;
		int size;
		String title = null;
		
		if (config.contains(id + ".Size")) {
			size = config.getInt(id + ".Size");

			if (size > 0) {
				if (config.contains(id + ".Main")) {
					main = config.getBoolean(id + ".Main");
				}

				if (config.contains(id + ".Title")) {
					title = ChatColor.translateAlternateColorCodes('&', config.getString(id + ".Title"));
				}
				
				GuiPane pane = new GuiPane(size, title);

				if (config.contains(id + ".Content")) {
					Iterator<String> it = config.getConfigurationSection(id + ".Content").getKeys(false).iterator();

					while (it.hasNext()) {
						String butt = it.next();

						if (buttons.containsKey(butt)) {
							GuiButton button = buttons.get(butt);
							if (config.contains(id + ".Content." + butt + ".Slot")) {
								int slot = config.getInt(id + ".Content." + butt + ".Slot");

								pane.addButton(button, slot);
							}
						}
					}
				}
				
				Bukkit.getPluginManager().registerEvents(pane, plugin);

				if (main)
					MAIN = pane;
				panes.put(id, pane);
			}
		}
	}

	private void loadButton(YamlConfiguration config, String id) {
		int type = 0;
		Material icon_material = null;
		String icon_title = null;
		List<String> icon_description = null;

		String parent = null;

		if (config.contains(id + ".Type")) {
			type = config.getInt(id + ".Type");
		} else {
			return;
		}

		if (config.contains(id + ".Parent")) {
			String p = config.getString(id + ".Parent");

			if (!buttons.containsKey(p)) {
				loadButton(config, p);
			}

			GuiButton pa = buttons.get(p);

			if (pa != null) {
				parent = p;
				icon_material = pa.getIcon().getType();

				if (pa.getIcon().hasItemMeta()) {
					icon_title = pa.getIcon().getItemMeta().getDisplayName();
					icon_description = pa.getIcon().getItemMeta().getLore();
				}
			} else {
				return;
			}
		}

		if (config.contains(id + ".Material")) {
			icon_material = Material.getMaterial(config.getString(id + ".Material"));
		}
		if (icon_material == null) {
			return;
		}

		if (config.contains(id + ".Title")) {
			icon_title = ChatColor.translateAlternateColorCodes('&', config.getString(id + ".Title"));
		}

		if (config.contains(id + ".Description")) {
			List<String> description = new ArrayList<String>();
			
			for (String s  : config.getStringList(id + ".Description")) {
				description.add(ChatColor.translateAlternateColorCodes('&', s));
			}
			
			icon_description = description;
		}

		if (type == GuiButton.WARP_TYPE) {
			loadWarpButton(config, id, icon_material, icon_title, icon_description, parent);
		} else if (type == GuiButton.LINK_TYPE) {

		} else {
			buttons.put(id, new GuiButton(id, icon_material, icon_title, icon_description, parent));
		}
	}

	private void loadWarpButton(YamlConfiguration config, String id, Material icon_material, String icon_title, List<String> icon_description, String parent) {
		if (config.contains(id + ".Warp")) {
			String warp = config.getString(id + ".Warp");

			if (!WarpManager.contains(warp)) {
				return;
			}

			buttons.put(id, new GuiWarpButton(id, icon_material, icon_title, icon_description, parent, WarpManager.getWarp(warp)));
		}
	}

}
