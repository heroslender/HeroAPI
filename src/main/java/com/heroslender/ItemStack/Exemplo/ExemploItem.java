package com.heroslender.ItemStack.Exemplo;

import com.heroslender.ItemStack.HItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ExemploItem {

    public ExemploItem() {

        // Um exemplo de um item
        ItemStack itemExemplo = new HItem(Material.SKULL_ITEM, "§cNome da skull", "Lore linha 1", "Lore linha 2");

        // Um item com data, por exemplo para colocar cor em lã
        ItemStack laVermelha = new HItem(Material.WOOL, (short) 14, "§cLã Vermelha", "§aUma la mt simples", "§ade cor §cvermelha");

        // Um item sem lore
        ItemStack itemSemLore = new HItem(Material.ENDER_STONE, "§aLinda pedra do fim :)");
    }
}
