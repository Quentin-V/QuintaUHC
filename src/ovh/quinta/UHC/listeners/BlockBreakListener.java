package ovh.quinta.UHC.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import ovh.quinta.UHC.Main;

public class BlockBreakListener implements Listener {
	
	private Main plugin;
	
	public BlockBreakListener(Main plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent bbe) {
		
		Block block = bbe.getBlock();
		
		if( (block.getType().equals(Material.LEAVES) || block.getType().equals(Material.LEAVES_2)) &&
			 bbe.getPlayer().getItemInHand().getType().equals(Material.SHEARS)) {
			if(Math.random() < plugin.getAppleDropRate()) {
				bbe.setCancelled(true);
				block.setType(Material.AIR);
				block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.APPLE, 1));
			}
		}
	}
	
	public void stop() {
		HandlerList.unregisterAll(this);
	}
	
}
