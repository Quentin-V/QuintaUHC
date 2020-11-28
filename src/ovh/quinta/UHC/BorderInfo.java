package ovh.quinta.UHC;

import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;

public class BorderInfo {
	
	Plugin plugin;
	Server server;
	Timer timer;
	TimerTask infoTask;
	
	public BorderInfo(Plugin plugin) {
		this.plugin = plugin;
		this.server = plugin.getServer();
		timer = new Timer();
		infoTask = new TimerTask() {
			public void run() {
				double size = server.getWorlds().get(0).getWorldBorder().getSize();
				int iSize = (int) Math.ceil(size);
				server.broadcastMessage(ChatColor.BLUE + "Taille de la zone " + iSize + " blocks");
			}
		};
		timer.schedule(infoTask, 0, 5 * 60 * 1000);
	}

	public void stop() {
		infoTask.cancel();
		timer.purge();
	}
}
