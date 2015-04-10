package swordman.hub.gui;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import swordman.hub.warp.Warp;

public class GuiWarpButton extends GuiButton {
	
	Warp warp;
	
	public GuiWarpButton(String id, Material icon_material, String icon_title, List<String> icon_description, String parent, Warp warp) {
		super(id, icon_material, icon_title, icon_description, parent);
		this.warp = warp;
	}

	public void onClick(InventoryClickEvent ev) {
		Player p = (Player) ev.getWhoClicked();
		
		p.closeInventory();
		
		warp.teleport(p);
	}
	
	public int getType() {
		return 1;
	}

}
