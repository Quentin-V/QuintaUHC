package ovh.quinta.UHC.listeners;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import ovh.quinta.UHC.Main;

public class PlayerDeathListener implements Listener {

	private Main plugin;
	private HashMap<Player, Location> deathLocations;
	
	public PlayerDeathListener(Main plugin) {
		this.plugin = plugin;
		this.deathLocations = new HashMap<>();
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent pde) {
		for(Player p : plugin.getServer().getOnlinePlayers()) {
			Location deathLocation = p.getLocation();
			this.deathLocations.put(p, deathLocation);
			p.sendTitle(pde.getEntity().getDisplayName(), "est mort");
			p.playSound(p.getLocation(), Sound.WITHER_SPAWN, 3, 0);
			pde.getEntity().setGameMode(GameMode.SPECTATOR);
		}
		if(pde.getEntity().getKiller() != null) {
			Player killer = pde.getEntity().getKiller();
			killer.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 1));
		}
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent pre) {
		Player p = pre.getPlayer();
		pre.setRespawnLocation(this.deathLocations.get(p));
		p.sendMessage(ChatColor.GREEN + "UHC : Tu as été téléporté à ta position de mort");
	}
	
	public void stop() {
		HandlerList.unregisterAll(this);
	}
}
