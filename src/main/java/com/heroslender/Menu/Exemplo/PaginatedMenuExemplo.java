package com.heroslender.Menu.Exemplo;

import com.heroslender.ItemStack.HItem;
import com.heroslender.Menu.PaginatedMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public class PaginatedMenuExemplo extends PaginatedMenu<Player> {

    public PaginatedMenuExemplo() {
        /*
        Defenir o nome do menu, e o tamanho
        O tamanho tb pode ser Int, para um numero de slots costumizado
         */
        super(
                "§aMenu de §eExemplo §a- §e%currentpage%§a/§e%maxpage%",
                MenuSize.THREE_LINES,
                new ArrayList<>(Bukkit.getOnlinePlayers()),
                player -> {
                    ItemStack item = new HItem(
                            Material.SKULL_ITEM,
                            (short) SkullType.PLAYER.ordinal(),
                            String.format("§e%s", player.getName()),
                            "", " §fEste player está online!", ""
                    );
                    SkullMeta meta = (SkullMeta) item.getItemMeta();
                    meta.setOwner(player.getName());
                    item.setItemMeta(meta);

                    return item;
                }
        );

        setPreviousPageItem(new HItem(
                Material.ARROW, "§cPágina anterior",
                "", " §fClique para ir à página anterior", ""
        ));

        setNextPageItem(new HItem(
                Material.ARROW, "§aPágina anterior",
                "", " §fClique para ir à próxima página", ""
        ));
    }
}

