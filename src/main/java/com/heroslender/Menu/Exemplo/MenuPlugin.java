package com.heroslender.Menu.Exemplo;

import com.heroslender.Menu.Menu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

class MenuPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Registar o Listener do menu - IMPORTANTE
        Menu.registar(this);
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