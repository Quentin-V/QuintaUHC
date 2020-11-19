package ovh.quinta.UHC;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Settings implements CommandExecutor {
	
	int borderSize;
	int shrinkedBorderSize;
	int shrinkTime;
	int spreadDistance;
	double appleDropRate;
	int timeBeforePVP;
	int timeBeforeFinalHeal;
	
	boolean started;
	
	Settings() {
		borderSize = 800;
		shrinkedBorderSize = 50;
		shrinkTime = 90;
		spreadDistance = 350;
		appleDropRate = 4;
		timeBeforePVP = 10;
		timeBeforeFinalHeal = 5;
		
		started = false;
	}
	
	public double getAppleDropRate() {
		return appleDropRate / 100.0;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(label.equalsIgnoreCase("uhc")) {
			if(sender.hasPermission("uhc.settings"))
				return settings(args, sender);
		}
		return false;
	}
	
	private boolean settings(String[] args, CommandSender sender) {
		try {
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
				case "show":
					sender.sendMessage("UHC Settings :\n  - Border size : " + borderSize + 
									   "\n  - Shrinked border size : " + shrinkedBorderSize + 
									   "\n  - Shrink time : " + shrinkTime + " minutes" +
									   "\n  - AppleDropRate : " + appleDropRate + "%" + 
									   "\n  - PVP : " + (timeBeforePVP != 0 ? "Enabled after " + timeBeforePVP + " minutes" : "Enabled at begining") +
									   "\n  - Final heal : " + (timeBeforeFinalHeal != 0 ? timeBeforeFinalHeal + " minutes" : "Disabled"));
					return true;
				default:
					throw new IllegalArgumentException("Wrong argument");
			}
		}catch(ArrayIndexOutOfBoundsException | IllegalArgumentException ia) {
			sender.sendMessage(ChatColor.RED + "Usage : /uhc [borderSize | shrinkedSize | shrinkTime | spreadDistance | appleDropRate | pvp | heal | show] <option>");
			return false;
		}
	}
}
