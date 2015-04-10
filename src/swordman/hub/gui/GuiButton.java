package swordman.hub.gui;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GuiButton {

	public static final int NORMAL_TYPE = 0;
	public static final int WARP_TYPE = 1;
	public static final int LINK_TYPE = 2;
	
	String id;
	String parent;
	Material icon_material;
	String icon_title;
	List<String> icon_description;

	public GuiButton(String id, Material icon_material, String icon_title, List<String> icon_description, String parent) {
		this.id = id;
		this.icon_material = icon_material;
		this.icon_title = icon_title;
		this.icon_description = icon_description;
		this.parent = parent;
	}

	public void onClick(InventoryClickEvent ev) {

	}

	public ItemStack getIcon() {
		ItemStack result = new ItemStack(icon_material);

		ItemMeta meta = result.getItemMeta();
		if (icon_title != null) {
			meta.setDisplayName(icon_title);
		}
		meta.setLore(icon_description);
		result.setItemMeta(meta);

		return result;
	}
	
	public int getType() {
		return 0;
	}

}
