package ovh.quinta.UHC.listeners;

import com.codingforcookies.armorequip.ArmorEquipEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import ovh.quinta.UHC.Main;

public class ArmorEquipListener implements Listener {

    private Main plugin;

    public ArmorEquipListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void equip(ArmorEquipEvent event) {
        int maxD = plugin.getSettings().getMaxDiamondArmorPces();
        Player p = event.getPlayer();
        if(countDiamondPieces(p) >= maxD && isDiamondArmor(event.getNewArmorPiece())) {
            event.setCancelled(true);
            p.sendMessage(ChatColor.GOLD + "Impossible d'équiper cette pièce d'armure");
        }
    }

    private int countDiamondPieces(Player p) {
        int diamondCount = 0;
        ItemStack[] armor = p.getInventory().getArmorContents();
        for(ItemStack item : armor) {
            if(isDiamondArmor(item)) {
                ++diamondCount;
            }
        }
        return diamondCount;
    }

    private boolean isDiamondArmor(ItemStack item) {
        Material itemMaterial = item.getData().getItemType();
        return  itemMaterial.equals(Material.DIAMOND_HELMET) ||
                itemMaterial.equals(Material.DIAMOND_CHESTPLATE) ||
                itemMaterial.equals(Material.DIAMOND_LEGGINGS) ||
                itemMaterial.equals(Material.DIAMOND_BOOTS);
    }

    public void stop() {
        HandlerList.unregisterAll(this);
    }

}
