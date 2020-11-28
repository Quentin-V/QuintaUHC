package ovh.quinta.UHC;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ovh.quinta.UHC.listeners.BlockBreakListener;
import ovh.quinta.UHC.listeners.PlayerDeathListener;

public class Main extends JavaPlugin {
	
	Server server;
	ConsoleCommandSender console;
	
	Settings settings;

	PlayerDeathListener pdl;
	BlockBreakListener bbl;
	
	BorderInfo bi;
	
	@Override
	public void onEnable() {
		// startups
		// reloads
		// plugin relaods
		server = this.getServer();
		console = server.getConsoleSender();
		settings = new Settings();
		server.broadcastMessage(ChatColor.GREEN + "QuintaUHC " + Bukkit.getVersion() + " enabled");
		getCommand("uhc").setExecutor(settings);
		getCommand("uhc").setTabCompleter(new UhcTabComplete());
		getCommand("inv").setExecutor(new InventoryExecutor(this));
		StartStopExecutor sse = new StartStopExecutor(this);
		getCommand("startuhc").setExecutor(sse);
		getCommand("stopuhc").setExecutor(sse);
		getCommand("teams").setExecutor(new TeamMaker(this));
		getCommand("teams").setTabCompleter(new TeamTabCompleter(this));

	}
	
	@Override
	public void onDisable() {
		// Shutdowns
		// Reloads
		// Plugin reloads
		//getServer( ).broadcastMessage( "UHC disabled" );
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(label.equalsIgnoreCase("heal") || label.equalsIgnoreCase("h")) {
			if(!sender.hasPermission("uhc.heal")) return false;
			for(Player p : server.getOnlinePlayers()) {
				p.setHealth(p.getMaxHealth());
			}
			server.broadcastMessage(ChatColor.GREEN + "UHC : Tous les joueurs ont été heal");
			return true;
		}
		return false;
	}
	
	

	public double getAppleDropRate() {
		return settings.getAppleDropRate();
	}
	
}

