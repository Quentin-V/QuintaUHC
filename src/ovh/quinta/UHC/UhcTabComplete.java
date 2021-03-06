package ovh.quinta.UHC;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

public class UhcTabComplete implements TabCompleter {
	
	private static List<String> uhcComplete = new ArrayList<>(List.of(
		"borderSize",
		"shrinkedSize",
		"shrinkTime",
		"spreadDistance",
		"appleDropRate",
		"pvp",
		"heal",
		"maxDiamondArmorPieces",
		"borderInfoInterval",
		"show"
	));
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 1) {
			List<String> completions = new ArrayList<>();
			StringUtil.copyPartialMatches(args[0], uhcComplete, completions);
			return completions;
		}
		return List.of("");
	}

}
