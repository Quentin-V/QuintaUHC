package ovh.quinta.UHC;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Settings implements CommandExecutor {
	
	int borderSize;
	int shrinkedBorderSize;
	int shrinkTime;
	int spreadDistance;
	double appleDropRate;
	int timeBeforePVP;
	int timeBeforeFinalHeal;
	int maxDiamondArmorPces;
	int borderInfoInterval;
	
	boolean started;

	ArrayList<Player> readyPlayers;
	
	Settings() {
		borderSize = 800;
		shrinkedBorderSize = 50;
		shrinkTime = 90;
		spreadDistance = 350;
		appleDropRate = 4;
		timeBeforePVP = 10;
		timeBeforeFinalHeal = 5;
		maxDiamondArmorPces = 2;
		borderInfoInterval = 5;

		readyPlayers = new ArrayList<>();

		started = false;
	}
	
	public double getAppleDropRate() {
		return appleDropRate / 100.0;
	}

	public int getMaxDiamondArmorPces() {
		return maxDiamondArmorPces;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(label.equalsIgnoreCase("uhc")) {
				return settings(args, sender);
		}
		return false;
	}
	
	private boolean settings(String[] args, CommandSender sender) {
		try {
			if(!args[0].equalsIgnoreCase("show") && !sender.hasPermission("uhc.settings")) throw new IllegalArgumentException();
			switch(args[0].toLowerCase()) {
				case "bordersize":
					try {
						borderSize = Integer.parseInt(args[1]);
						sender.sendMessage(ChatColor.GREEN + "UHC : Border size set to " + args[1]);
						return true;
					}catch(NumberFormatException nfe) {
						sender.sendMessage(ChatColor.RED + "Usage : /uhc bordersize <Taille>");
						return false;
					}
				case "shrinkedsize":
					try {
						shrinkedBorderSize = Integer.parseInt(args[1]);
						sender.sendMessage(ChatColor.GREEN + "UHC : Shrinked border size set to " + args[1]);
						return true;
					}catch(NumberFormatException nfe) {
						sender.sendMessage(ChatColor.RED + "Usage : /uhc shrinkedsize <Taille>");
						return false;
					}
				case "shrinktime":
					try {
						shrinkTime = Integer.parseInt(args[1]) * 60;
						sender.sendMessage(ChatColor.GREEN + "UHC : Shrink time set to " + args[1] + " minutes");
						return true;
					}catch(NumberFormatException nfe) {
						sender.sendMessage(ChatColor.RED + "Usage : /uhc shrinkTime <Temps en minutes>");
						return false;
					}
				case "appledroprate":
					try {
						double dr = Double.parseDouble(args[1]);
						if(dr > 100 || appleDropRate < 0) throw new IllegalArgumentException("Apple drop rate must be between 0 and 100");
						appleDropRate = dr;
						sender.sendMessage(ChatColor.GREEN + "UHC : Apple droprate set to " + args[1] + "%");
						return true;
					}catch(NumberFormatException nfe) {
						sender.sendMessage(ChatColor.RED + "Usage : /uhc appleDropRate <Pourcentage de drop par feuille cassee>");
						return false;
					}
				case "spreaddistance":
					try {
						int dist = Integer.parseInt(args[1]);
						if(dist < 0) throw new IllegalArgumentException("SpreadDistance must be > 0");
						spreadDistance = dist;
						sender.sendMessage(ChatColor.GREEN + "SpreadDistance set to " + args[1] + " blocks");
						return true;
					}catch(IllegalArgumentException iae) {
						sender.sendMessage(ChatColor.RED + "Usage : /uhc spreadDistance <Distance de séparation des équipes au lancement>");
						return false;
					}
				case "pvp":
					try {
						int time = Integer.parseInt(args[1]);
						if(time < 0) throw new IllegalArgumentException("Time before PVP must be > 0");
						timeBeforePVP = time;
						sender.sendMessage(ChatColor.GREEN + "Le PVP sera activé après " + time + " minutes");
						return true;
					}catch(IllegalArgumentException iae) {
						sender.sendMessage(ChatColor.RED + "Usage : /uhc pvp <Temps en minutes avant l'activation du PVP>");
						return false;
					}
				case "heal":
					try {
						int time = Integer.parseInt(args[1]);
						if(time < 0) throw new IllegalArgumentException("Time before final heal must be > 0");
						timeBeforeFinalHeal = time;
						sender.sendMessage(ChatColor.GREEN + "Le final heal se fera après " + time + " minutes");
						return true;
					}catch(IllegalArgumentException iae) {
						sender.sendMessage(ChatColor.RED + "Usage : /uhc heal <Temps en minutes avant le final heal>");
						return false;
					}
				case "maxdiamondarmorpieces":
					try {
						int maxPces = Integer.parseInt(args[1]);
						if(maxPces < 0) maxPces = 0;
						if(maxPces > 4) maxPces = 4;
						maxDiamondArmorPces = maxPces;
						if(maxDiamondArmorPces > 0) {
							sender.sendMessage(ChatColor.GREEN + "Le nombre maximum de pieces d'armure en diamant autorisé est : " + maxPces);
						}else {
							sender.sendMessage(ChatColor.GREEN + "Les armures en diamant sont interdites");
						}
						return true;
					}catch(IllegalArgumentException iae) {
						sender.sendMessage(ChatColor.RED + "Usage : /uhc maxDiamondArmorPieces <Nombre maximal de pieces d'armure en diamant>");
						return false;
					}
				case "borderinfointerval":
					try {
						int interval = Integer.parseInt(args[1]);
						borderInfoInterval = Math.max(interval, 0);
						sender.sendMessage("Le message d'information sera envoyé toutes les " + borderInfoInterval + " minutes");
						return true;
					}catch(IllegalArgumentException iae) {
						sender.sendMessage(ChatColor.RED + "Usage : /uhc borderInfoInterval <Minutes entre le message d'info>");
						return false;
					}
				case "show":
					sender.sendMessage("UHC Settings :\n  - Border size : " + borderSize + 
									   "\n  - Shrinked border size : " + shrinkedBorderSize + 
									   "\n  - Shrink time : " + shrinkTime + " minutes" +
									   "\n  - AppleDropRate : " + appleDropRate + "%" + 
									   "\n  - PVP : " + (timeBeforePVP != 0 ? "Enabled after " + timeBeforePVP + " minutes" : "Enabled at begining") +
									   "\n  - Final heal : " + (timeBeforeFinalHeal != 0 ? timeBeforeFinalHeal + " minutes" : "Disabled") +
									   "\n  - Max diamond armor pieces : " + maxDiamondArmorPces +
									   "\n  - Border info interval : " + borderInfoInterval);
					return true;
				default:
					throw new IllegalArgumentException("Wrong argument");
			}
		}catch(ArrayIndexOutOfBoundsException | IllegalArgumentException ia) {
			if(!sender.hasPermission("uhc.settings")) sender.sendMessage(ChatColor.RED + "Usage : /uhc show");
			else sender.sendMessage(ChatColor.RED + "Usage : /uhc [borderSize | shrinkedSize | shrinkTime | spreadDistance | appleDropRate | pvp | heal | maxDiamondArmorPieces | show] <option>");
			return false;
		}
	}
}
