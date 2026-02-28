package me.brody.immortal;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ImmortalPlugin extends JavaPlugin implements Listener {

    private final Set<UUID> immortalPlayers = new HashSet<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!immortalPlayers.contains(player.getUniqueId())) return;

        boolean hasTotem =
                player.getInventory().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING ||
                player.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING;

        if (hasTotem) return;

        double healthAfter = player.getHealth() - event.getFinalDamage();
        if (healthAfter <= 1.0) {
            event.setDamage(Math.max(0, player.getHealth() - 1.0));
            player.setHealth(1.0);
            player.setFireTicks(0);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (args.length != 1) {
            player.sendMessage("§cUsage: /immortal <on|off>");
            return true;
        }

        if (args[0].equalsIgnoreCase("on")) {
            immortalPlayers.add(player.getUniqueId());
            player.sendMessage("§aImmortality enabled.");
        } else if (args[0].equalsIgnoreCase("off")) {
            immortalPlayers.remove(player.getUniqueId());
            player.sendMessage("§cImmortality disabled.");
        }

        return true;
    }
}
