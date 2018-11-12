package com.heroslender.Menu.Exemplo;

import com.heroslender.Menu.Menu;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MenuExemplo extends Menu {

    public MenuExemplo() {
        /*
        Defenir o nome do menu, e o tamanho
        O tamanho tb pode ser Int, para um numero de slots costumizado
         */
        super("§aMenu de §eExemplo", MenuSize.THREE_LINES);

        // Item clicavel
        setItem(13, new ItemStack(Material.SKULL_ITEM), clickEvent -> {
            clickEvent.getWhoClicked().sendMessage("Clicaste na skull!");
            clickEvent.getWhoClicked().sendMessage("Parabens!");
        });

        // Item não clicavel
        setItem(15, new ItemStack(Material.PAPER));
    }
}

