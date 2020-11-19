package ovh.quinta.UHC;

import java.util.TimerTask;

import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import ovh.quinta.UHC.listeners.BlockBreakListener;
import ovh.quinta.UHC.listeners.PlayerDeathListener;

public class StartStopExecutor implements CommandExecutor {

	private Main plugin;
	Settings settings;
	Server server;
	
	public StartStopExecutor(Main plugin) {
		this.plugin = plugin;
		this.settings = plugin.settings;
		this.server = plugin.server;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("startuhc")) { // D�marre l'UHC
			System.out.println("HERE");
			if(!sender.hasPermission("uhc.start")) return false;
			
			plugin.pdl = new PlayerDeathListener(plugin);
			plugin.bbl = new BlockBreakListener(plugin);
			
			String[] commandsStart = {
					"gamemode 0 @a", // Tout le monde en survival
					"clear @a", // Clear tout le monde
					"effect @a minecraft:saturation 1 99 true", // Restaure la faim de tout le monde
					"spreadplayers 0 0 " + plugin.settings.spreadDistance + " " + (plugin.settings.borderSize/2) + " true @a",
					"heal"
			};
			
			String[] scoreboardCommands = {
					"scoreboard objectives add Kills playerKillCount", // Tous les objectifs
					"scoreboard objectives add vie health",
					"scoreboard objectives add damage stat.damageDealt",
					"scoreboard objectives setdisplay list vie",
					"scoreboard objectives setdisplay sidebar Kills",
					"scoreboard objectives setdisplay belowName damage",
					"scoreboard players reset @a Kills",
					"scoreboard players reset @a damage"
			};
			
			String[] worldBorderCommands = {
					"worldborder center 0 0", // Border
					"worldborder set " + plugin.settings.borderSize,
					"worldborder set " + plugin.settings.shrinkedBorderSize + " " + (plugin.settings.shrinkTime * 60),
					"worldborder warning distance 10",
					"worldborder warning time 30"
			};
			
			for(World w : server.getWorlds()) { // Send commands in every world
				w.setDifficulty(Difficulty.HARD);
				w.setGameRuleValue("sendCommandFeedback", "false");
				w.setGameRuleValue("naturalRegeneration", "false");
				if(plugin.settings.timeBeforePVP != 0) w.setPVP(false);
				w.setTime(0);
				for(String c : worldBorderCommands) {
					server.dispatchCommand(plugin.console, c);
				}
			}
			
			for(String c : scoreboardCommands) {
				server.dispatchCommand(plugin.console, c);
			}
			
			for(String c : commandsStart) {
				server.dispatchCommand(plugin.console, c);
			}
			
			plugin.bi = new BorderInfo(plugin); // Creates the borderinfo
			
			if(settings.timeBeforePVP != 0) { // Schedule pvp if needed
				TimerTask tt = new TimerTask() {
					public void run() {
						for(World w : server.getWorlds()) {
							w.setPVP(true);
							w.getPlayers().forEach(p -> p.sendMessage(ChatColor.GREEN + "UHC : PVP Enabled"));
						}
					}
				};
				plugin.timer.schedule(tt, settings.timeBeforePVP * 60 * 1000);
			}
			
			if(settings.timeBeforeFinalHeal != 0) { // Schedule heal if needed
				TimerTask tt = new TimerTask() {
					public void run() {
						server.broadcastMessage(ChatColor.GREEN + "UHC : Final heal !");
						server.dispatchCommand(plugin.console, "heal");
					}
				};
				plugin.timer.schedule(tt, settings.timeBeforeFinalHeal * 60 * 1000);
			}
			
			for(Player p : server.getOnlinePlayers()) { // Play wither sound to everyone and apply resistance for 30 seconds
				p.playSound(p.getLocation(), Sound.WITHER_DEATH, 3, 0);
				p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 99), true); // Resistance 30 secondes
			}
			
			server.broadcastMessage(ChatColor.DARK_RED + "L'UHC commence");
			plugin.settings.started = true;
			return true;
		}
		
		
		if(label.equalsIgnoreCase("stopuhc")) {
			
			if(!sender.hasPermission("uhc.stop")) return false;
			if(!settings.started) {
				sender.sendMessage(ChatColor.RED + "UHC: Pas d'UHC en cours");
				return false;
			}
			plugin.bbl.stop();
			plugin.bbl = null;
			plugin.pdl.stop();
			plugin.pdl = null;
			plugin.bi.stop();
			plugin.bi = null;
			
			for(World w : server.getWorlds()) {
				w.setGameRuleValue("sendCommandFeedback", "true");
				w.setGameRuleValue("gamerule naturalRegeneration", "true");
				server.dispatchCommand(plugin.console, "worldborder set 30000000");
			}
			
			sender.sendMessage(ChatColor.GREEN + "UHC: Stopped UHC");
		}
		
		return false;
	}

}