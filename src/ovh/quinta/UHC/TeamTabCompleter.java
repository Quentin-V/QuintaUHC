package ovh.quinta.UHC;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class TeamTabCompleter implements TabCompleter {

    static List<String> teamsComplete = List.of("add", "remove", "color", "join", "random", "show");

    private Main plugin;
    private Server server;

    TeamTabCompleter(Main plugin) {
        this.plugin = plugin;
        this.server = plugin.server;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 1) {
            List<String> completions = new ArrayList<>();
            StringUtil.copyPartialMatches(args[0], teamsComplete, completions);
            return completions;
        }else if(args.length == 2) {
            switch (args[0]) {
                case "remove":
                case "color":
                case "join":
                    List<String> teams = new ArrayList<>();
                    plugin.server.getScoreboardManager().getMainScoreboard().getTeams().forEach(t -> teams.add(t.getName()));
                    return teams;
                default:
                    return List.of("");
            }
        }else if(args.length == 3) {
            switch (args[0]) {
                case "color":
                    return List.of(TeamMaker.COLORS_LIST);
                case "join":
                    List<String> players = new ArrayList<>();
                    server.getOnlinePlayers().forEach(p -> players.add(p.getName()));
                    return players;
                default:
                    return null;
            }
        }
        return null;
    }
}
