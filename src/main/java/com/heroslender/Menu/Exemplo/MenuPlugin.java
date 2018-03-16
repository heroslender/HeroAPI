package com.heroslender.Menu.Exemplo;

import com.heroslender.Menu.MenuHolder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

class MenuPlugin extends JavaPlugin {

    @Override
    public void onEnable() {

        // Registar o Listener do menu - IMPORTANTE
        getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            private void onInvClick(InventoryClickEvent e) {
                if (!e.isCancelled() && e.getInventory().getHolder() instanceof MenuHolder)
                    ((MenuHolder) e.getInventory().getHolder()).getMenu().inventoryClick(e);
            }
        }, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        // Criar uma instancia do menu e abrir para o player
        MenuExemplo menu = new MenuExemplo();
        menu.open(player);

        player.sendMessage("Abriste o menu de exemplo.");
        return true;
    }
}