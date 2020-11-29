package ovh.quinta.UHC.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.md_5.bungee.api.ChatColor;
import ovh.quinta.UHC.Main;

public class InventoryClickListener implements Listener {
	private static Main plugin;
	private Player sender;
	private int closeNb; // Le nombre de fois que l'inventaire a été fermé
	private boolean hasClicked; // Passe a true si le joueur selectionne un autre joueur
	public InventoryClickListener(Main plugin, Player sender) {
		InventoryClickListener.plugin = plugin;
		this.sender = sender;
		this.closeNb = 0;
		this.hasClicked = false;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent ice) {
		if(ice.getClickedInventory() == null) return;
		if(ice.getClickedInventory().getType() != InventoryType.CHEST || ice.getClickedInventory().getViewers().get(0) != sender) return; // Sécurité
		if(ice.getClickedInventory().getSize() == 45) { // Si on est dans le fenêtre de l'inventaire du joueur et non pas dans la sélection
			ice.setCancelled(true);
			return;
		}
		
		ItemStack item = ice.getCurrentItem();
		if(item == null) return;
		ItemMeta meta = item.getItemMeta();
		SkullMeta skMeta = null;
		if(meta instanceof SkullMeta)
			skMeta = (SkullMeta)item.getItemMeta();
		if(skMeta == null) return;
		
		this.hasClicked = true; // Si le joueur a bien sélectionné une tête
		
		String playerName = skMeta.getDisplayName();
		Player target = null;
		for(Player p : plugin.getServer().getOnlinePlayers()) {
			if(p.getName().equals(playerName)) target = p;
		}
		ice.setCurrentItem(null);
		if(target == null) {
			sender.sendMessage(ChatColor.RED + "UHC : Impossible de retrouver ce joueur");
			return;
		}else if(target.getGameMode() != GameMode.SURVIVAL) {
			sender.sendMessage(ChatColor.RED + "UHC : Ce joueur n'est plus vivant");
			return;
		}
		plugin.getLogger().info(sender.getName() + " regarde l'inventaire de " + target.getName()); // Log
		PlayerInventory targetInventory = target.getInventory(); // Inventaire du joueur cible
		Inventory cpyInventory = Bukkit.createInventory(null, 45, "Inventaire de " + target.getDisplayName()); // Creation d'un inventaire vide pour la copie
		
		for(int i = 0; i < 9; ++i) { // Hotbar
			ItemStack targetItem = targetInventory.getItem(i);
			if(targetItem == null) continue;
			cpyInventory.setItem(i+27, targetItem);
		}
		
		cpyInventory.setItem(36, targetInventory.getHelmet()); // Armure
		cpyInventory.setItem(37, targetInventory.getChestplate());
		cpyInventory.setItem(38, targetInventory.getLeggings());
		cpyInventory.setItem(39, targetInventory.getBoots());
		
		for(int i = 9; i < 36; ++i) { // Reste de l'inventaire
			ItemStack targetItem = targetInventory.getItem(i);
			if(targetItem == null) continue;
			cpyInventory.setItem(i-9, targetItem);
		}
		
		// Cases vides
		ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1);
		ItemMeta glMeta = glass.getItemMeta();
		glMeta.setDisplayName("Rien");
		glass.setItemMeta(glMeta);
		for(int i = 40; i <= 44; ++i) {
			cpyInventory.setItem(i, glass);
		}
		
		sender.openInventory(cpyInventory);
		ice.setCancelled(true);
	}
	
	@EventHandler
	public void onCloseInventory(InventoryCloseEvent ice) {
		closeNb++;
		if(ice.getPlayer().equals(sender) && closeNb >= 2 || ice.getInventory().getSize() == 9 && !this.hasClicked) {
			HandlerList.unregisterAll(this);
		}
	}
	
}
