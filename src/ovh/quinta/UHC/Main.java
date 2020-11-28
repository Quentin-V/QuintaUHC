package ovh.quinta.UHC;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ovh.quinta.UHC.listeners.ArmorEquipListener;
import ovh.quinta.UHC.listeners.BlockBreakListener;
import ovh.quinta.UHC.listeners.PlayerDeathListener;

public class Main extends JavaPlugin {
	
	Server server;
	ConsoleCommandSender console;
	
	Settings settings;

	StartStopExecutor sse;

	PlayerDeathListener pdl;
	BlockBreakListener bbl;
	ArmorEquipListener ael;
	
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
		sse = new StartStopExecutor(this);
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
		if(settings.started) { // Stops the uhc if started
			bbl.stop(); bbl = null;
			pdl.stop(); pdl = null;
			bi.stop();  bi = null;
			ael.stop(); ael = null;

			sse.pvpTask.cancel();
			sse.healTask.cancel();
			sse.timer.purge();
			sse.timer.cancel();

			for(World w : server.getWorlds()) {
				w.setGameRuleValue("sendCommandFeedback", "true");
				w.setGameRuleValue("naturalRegeneration", "true");
				server.dispatchCommand(console, "worldborder set 30000000");
				w.setPVP(true);
			}
			settings.started = false;
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(label.equalsIgnoreCase("heal") || label.equalsIgnoreCase("h")) {
			if(!sender.hasPermission("uhc.heal")) return false;
			for(Player p : server.getOnlinePlayers()) {
				p.setHealth(p.getMaxHealth());
			}
			server.broadcastMessage(ChatColor.GREEN + "UHC : Tous les joueurs ont �t� heal");
			return true;
		}
		return false;
	}

	public Settings getSettings() {
		return settings;
	}

	public double getAppleDropRate() {
		return settings.getAppleDropRate();
	}
	
}

