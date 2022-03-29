package com.heroslender.menu.exemplo;

import com.heroslender.ItemStack.HItem;
import com.heroslender.menu.Menu;
import com.heroslender.menu.PaginatedMenu;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

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

    public void abrirMenuExemplo2(Player player) {
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

    public void abrirMenuPaginado(Player player, int page) {
        // Inicializar o menu
        PaginatedMenu<EntityType> menu = new PaginatedMenu<>(
                "§aEntidades disponíveis §a- §e%currentpage%§a/§e%maxpage%",
                Menu.MenuSize.THREE_LINES,
                Arrays.asList(EntityType.values()),
                entity -> {
                    ItemStack item = new HItem(
                            Material.SKULL_ITEM,
                            (short) SkullType.PLAYER.ordinal(),
                            String.format("§e%s", entity.name()),
                            "", " §fCabeça de " + entity.name() + "!", ""
                    );
                    SkullMeta meta = (SkullMeta) item.getItemMeta();
                    String name = entity.name();
                    meta.setOwner("MHF_" + name.charAt(0) + name.substring(1).toLowerCase());
                    item.setItemMeta(meta);

                    return item;
                }
        );

        menu.setPreviousPageItem(new HItem(
                Material.ARROW, "§cPágina anterior",
                "", " §fClique para ir à página anterior", ""
        )).setNextPageItem(new HItem(
                Material.ARROW, "§aPágina anterior",
                "", " §fClique para ir à próxima página", ""
        ));

        // Abrir o menu para o Player
        menu.open(player, page);
    }

    static class MyHolder extends Menu.MenuHolder {
        int count = 0;
    }

    public void abrirMenuComCustomHolder(Player player) {
        // Inicializar o menu
        Menu menu = new Menu("Menu exemplo 2", Menu.MenuSize.THREE_LINES);

        // Definir o conteudo
        menu.setItem(11, new ItemStack(Material.BEDROCK), menuClick -> menuClick.getWhoClicked().sendMessage("Clicas-te na Bedrock!"));
        menu.setItem(13, new ItemStack(Material.DIAMOND));
        menu.setItem(15, new ItemStack(Material.APPLE), menuClick -> {
            MyHolder holder = (MyHolder) menuClick.getHolder();
            menuClick.getWhoClicked().sendMessage("Clicas-te na maça " + holder.count + " vezes!");

            holder.count++;

            menuClick.getWhoClicked().closeInventory();
            menuClick.getWhoClicked().getInventory().addItem(new ItemStack(Material.APPLE));
        });


        // Abrir o menu para o Player
        menu.open(player, new MyHolder());
    }
}