package ovh.quinta.UHC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import org.bukkit.ChatColor;

public class TeamMaker implements CommandExecutor {

	private Main plugin;
	private Scoreboard sc;

	public static final String[] COLORS_LIST = {"black", "dark_blue", "dark_green", "dark_aqua", "dark_red", "dark_purple", "gold", "gray", "dark_gray", "blue", "green", "aqua", "red", "light_purple", "yellow", "white"};

	public static final String[] RANDOM_TEAMS_COLORS = {"red",   "blue", "gold",  "purple", "pink", "green", "black", "aqua"};
	public static final String[] RANDOM_TEAMS_NAMES  = {"Rouge", "Bleu", "Jaune", "Violet", "Rose", "Vert",  "Noir",  "Cyan"};

	public TeamMaker(Main plugin) {
		this.plugin = plugin;
		sc = plugin.server.getScoreboardManager().getMainScoreboard();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("teams")) {
			try {
				switch(args[0]) {
				case "add":
					return addTeam(args, sender);
				case "remove":
					return removeTeam(args, sender);
				case "color":
					return teamColor(args, sender);
				case "join":
					return joinTeam(args, sender);
				case "random":
					return randomTeam(args, sender);
				case "show":
					showTeams(sender);
					return true;
				default:
					sender.sendMessage(ChatColor.RED + "Usage : teams [add | remove | color | join | random | show] <options>");
					break;
				}
			}catch(ArrayIndexOutOfBoundsException antiOOB) {
				sender.sendMessage(ChatColor.RED + "Usage : teams [add | remove | color | join | random | show] <options>");
			}
		}
		return false;
	}

	private boolean addTeam(String[] args, CommandSender sender) {
		boolean addedPlayers = false;
		try {
			Team addedTeam = sc.registerNewTeam(args[1]);
			sender.sendMessage(ChatColor.GREEN + "Created team : " + addedTeam.getDisplayName());
			String[] players = Arrays.copyOfRange(args, 2, args.length);
			for(String pName : players) {
				try {
					Player p = plugin.server.getPlayer(pName);
					if(p == null) throw new NoSuchElementException(pName);
					addedTeam.addEntry(p.getName());
					addedPlayers = true;
				}catch(NoSuchElementException nse) {
					sender.sendMessage(ChatColor.RED + "UHC : Failed to add " + nse.getMessage() + " to the team (Player not found)");
				}
			}
			if(addedPlayers) sender.sendMessage(ChatColor.GREEN + "Added " + Arrays.toString(players) + " to the created team");
			return true;
		}catch(ArrayIndexOutOfBoundsException | NoSuchElementException e) {
			sender.sendMessage(ChatColor.RED + "Usage : teams add <Nom de la team> (Joueur1 Joueur2 ...)");
		}
		return false;
	}

	private boolean removeTeam(String[] args, CommandSender sender) {
		try {
			Team toRemove = sc.getTeam(args[1]);
			if(toRemove == null) throw new NoSuchElementException(args[1]);
			String teamName = toRemove.getDisplayName();
			toRemove.unregister();
			sender.sendMessage(ChatColor.GREEN + "Successfuly removed " + teamName);
			return true;
		}catch(ArrayIndexOutOfBoundsException oob) {
			sender.sendMessage(ChatColor.RED + "Usage : team ");
		}catch(NoSuchElementException nse) {
			sender.sendMessage(ChatColor.RED + "Unable to find team : " + ChatColor.BLUE + nse.getMessage());
		}
		return false;
	}

	private boolean teamColor(String[] args, CommandSender sender) {
		try {
			String teamName = args[1];
			String color = args[2];
			Team team = sc.getTeam(teamName);
			if(team == null) throw new NoSuchElementException("t:"+teamName);
			if(!Arrays.asList(COLORS_LIST).contains(color)) throw new NoSuchElementException("c:"+color);
			plugin.server.dispatchCommand(plugin.console, "scoreboard teams option " + teamName + " color " + color);
			return true;
		}catch(ArrayIndexOutOfBoundsException oob) {
			sender.sendMessage(ChatColor.RED + "Usage : teams color <Nom de la team> <Couleur>");
		}catch(NoSuchElementException nse) {
			if(nse.getMessage().charAt(0) == 't') {
				sender.sendMessage(ChatColor.RED + "Unable to find team " + nse.getMessage().substring(2));
			}else {
				sender.sendMessage("Unable to find color " + nse.getMessage().substring(2));
			}
		}
		return false;
	}

	private boolean joinTeam(String[] args, CommandSender sender) {
		try {
			Team t = sc.getTeam(args[1]);
			if(t == null) throw new NoSuchElementException("t:"+args[1]);
			String[] players = Arrays.copyOfRange(args, 2, args.length);
			if(args.length < 3) throw new NoSuchElementException("p");
			for(String pName : players) {
				try {
					Player p = plugin.server.getPlayer(pName);
					if(p == null) throw new NoSuchElementException(pName);
					t.addEntry(p.getName());
					sender.sendMessage(ChatColor.GREEN + "UHC : Added " + pName + " to " + t.getDisplayName());
				}catch(NoSuchElementException nse) {
					sender.sendMessage(ChatColor.RED + "UHC : Failed to add " + nse.getMessage() + " to the team (Player not found)");
				}
			}
			return true;
		}catch(ArrayIndexOutOfBoundsException oob) {
			sender.sendMessage(ChatColor.RED + "Usage : teams join <Team> [Joueur1 Joueur2 ...]");
		}catch(NoSuchElementException nse) {
			if(nse.getMessage().charAt(0) == 't')
				sender.sendMessage(ChatColor.RED + "Unable to find team " + nse.getMessage().substring(2));
			else
				sender.sendMessage(ChatColor.RED + "Usage : teams join <Team> [Joueur1 Joueur2 ...]");
		}
		return false;
	}

	private boolean randomTeam(String[] args, CommandSender sender) {
		try {
			int playersPerTeam = Integer.parseInt(args[1]);
			double teamCount = (double)plugin.server.getOnlinePlayers().size() / playersPerTeam;
			int iTeamCount = (int) Math.ceil(teamCount);
			if(teamCount != iTeamCount) sender.sendMessage(ChatColor.GOLD + "UHC : Attention, au moins une équipe ne sera pas complète");
			for(int i = 0; i < iTeamCount; ++i) {
				Team t = sc.getTeam(RANDOM_TEAMS_NAMES[i]);
				if (t != null) {
					t.unregister();
				}
				sc.registerNewTeam(RANDOM_TEAMS_NAMES[i]);
				plugin.server.dispatchCommand(plugin.console, "scoreboard teams option " + RANDOM_TEAMS_NAMES[i] + " color " + RANDOM_TEAMS_COLORS[i]);
			}
			ArrayList<Player> playerArrayList = new ArrayList<>(plugin.server.getOnlinePlayers());
			for(int i = 0; i < playerArrayList.size(); ++i) {
				int random = (int) (Math.random() * playerArrayList.size());
				sc.getTeam(RANDOM_TEAMS_NAMES[i]).addEntry(playerArrayList.remove(random).getName());
			}
			sender.sendMessage(ChatColor.GREEN + "UHC : " + iTeamCount + " teams créées");
			showTeams(sender);
		}catch(ArrayIndexOutOfBoundsException oob) {
			sender.sendMessage(ChatColor.RED + "Usage : teams random <Nombre de joueurs par team>");
		}
		return false;
	}

	private void showTeams(CommandSender sender) {
		StringBuilder msg = new StringBuilder(ChatColor.GREEN + "Teams : \n");
		for(Team t : sc.getTeams()) {
			msg.append("  ").append(t.getDisplayName()).append(" : ").append(Arrays.toString(t.getEntries().toArray())).append("\n");
		}
		sender.sendMessage(msg.toString());
	}

}

