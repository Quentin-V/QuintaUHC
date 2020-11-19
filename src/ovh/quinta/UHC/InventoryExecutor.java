package ovh.quinta.UHC;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import net.md_5.bungee.api.ChatColor;
import ovh.quinta.UHC.listeners.InventoryClickListener;

public class InventoryExecutor implements CommandExecutor {

	private Main plugin;
	private Server server;
	
	public InventoryExecutor(Main plugin) {
		this.plugin = plugin;
		this.server = plugin.getServer();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("inv")) {
			return seeInv(sender);
		}
		return false;
	}

	boolean seeInv(CommandSender sender) {
		Player plSender;
		try {
			plSender = (Player)sender;
		}catch(Exception e) {
			sender.sendMessage("Cette commande doit être effectuée par un joueur");
			return false;
		}
		
		if(!plSender.isOp() && plSender.getGameMode() != GameMode.SPECTATOR) return false;
		
		int alivePlayersNb = 0;
		ArrayList<Player> alivePlayers = new ArrayList<>();
		for(Player p : server.getOnlinePlayers()) {
			if(p.getGameMode() == GameMode.SURVIVAL) {
				++alivePlayersNb;
				alivePlayers.add(p);
			}
		}
		if(alivePlayersNb == 0) {
			plSender.sendMessage(ChatColor.GREEN + "UHC : Aucun joueur en vie, impossible de voir des inventaires");
			return false;
		}
		
		while(alivePlayersNb % 9 != 0) ++alivePlayersNb;
		Inventory selPlayerGui = Bukkit.createInventory(null, alivePlayersNb);
		
		for(Player aliveP : alivePlayers) {
			ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
			SkullMeta meta = (SkullMeta) head.getItemMeta();
	        meta.setDisplayName(aliveP.getName());
	        meta.setOwner(aliveP.getName());
	        head.setItemMeta(meta);
			selPlayerGui.addItem(head);
		}
		plSender.openInventory(selPlayerGui);
		new InventoryClickListener(plugin, plSender);
		return true;
	}
}
