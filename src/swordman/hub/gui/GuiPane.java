package swordman.hub.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class GuiPane implements Listener, InventoryHolder {

	private final Inventory inventory;
	private int size = 9;
	private GuiButton[] content;

	public GuiPane(int size, String title) {
		this.size = size * 9;
		if (title != null)
			this.inventory = Bukkit.createInventory(this, this.size, title);
		else
			this.inventory = Bukkit.createInventory(this, this.size);
		this.content = new GuiButton[this.size];
	}

	public void paint() {
		for (int i = 0; i < size; i++) {
			if (content[i] != null)
				inventory.setItem(i, content[i].getIcon());
		}
	}

	public void addButton(GuiButton button, int index) {
		if (index < size) {
			content[index] = button;
			paint();
		}
	}

	public void show(Player p) {
		p.openInventory(inventory);
	}

	@EventHandler
	public void clickEvent(InventoryClickEvent ev) {
		if (ev.getInventory().getHolder() == this) {
			ev.setCancelled(true);
			if (ev.getRawSlot() < size) {
				if (content[ev.getRawSlot()] != null)
					content[ev.getRawSlot()].onClick(ev);
			}
		}
	}

	@EventHandler
	public void dragEvent(InventoryDragEvent ev) {
		if (ev.getInventory().getHolder() == this) {
			ev.setCancelled(true);
		}
	}

	@EventHandler
	public void closeEvent(InventoryCloseEvent ev) {

	}

	public int getSize() {
		return size;
	}

	public GuiButton[] getContent() {
		return content;
	}

	public Inventory getInventory() {
		return inventory;
	}

}
