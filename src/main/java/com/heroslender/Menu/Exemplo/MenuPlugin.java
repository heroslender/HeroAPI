package com.heroslender.Menu.Exemplo;

import com.heroslender.Menu.Menu;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

class MenuPlugin extends JavaPlugin {

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

    public void abrirMenuExemplo2(Player player){
        // Inicializar o menu
        Menu menu = new Menu("Menu exemplo 2", Menu.MenuSize.THREE_LINES);

        // Definir o conteudo
        menu.setItem(11, new ItemStack(Material.BEDROCK), menuClick -> menuClick.getWhoClicked().sendMessage("Clicas-te na Bedrock!"));
        menu.setItem(13, new ItemStack(Material.DIAMOND));
        menu.setItem(15, new ItemStack(Material.APPLE), menuClick -> {
            menuClick.getWhoClicked().sendMessage("Clicas-te na maça!");

            menuClick.getWhoClicked().closeInventory();
            menuClick.getWhoClicked().getInventory().addItem(new ItemStack(Material.APPLE));
        });

        // Abrir o menu para o Player
        menu.open(player);
    }
}