package com.heroslender.Menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class Menu {
    private final String nome;
    private MenuItem[] items;

    public static void registar(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            private void onInvClick(InventoryClickEvent e) {
                if (!e.isCancelled() && e.getInventory().getHolder() instanceof MenuHolder)
                    ((MenuHolder) e.getInventory().getHolder()).getMenu().inventoryClick(e);
            }
        }, plugin);
    }

    public Menu(String nome, int tamanho) {
        this.nome = nome;
        items = new MenuItem[tamanho];
    }

    public Menu(String nome, MenuSize tamanho) {
        this.nome = nome;
        items = new MenuItem[tamanho.getSlots()];
    }

    protected void setItem(int slot, ItemStack itemStack) {
        setItem(slot, itemStack, null);
    }

    protected void setItem(int slot, ItemStack itemStack, MenuItemClick itemClick) {
        items[slot] = new MenuItem(itemStack, itemClick);
    }

    public void open(Player player) {
        MenuHolder holder = new MenuHolder(this);
        Inventory inventory = Bukkit.createInventory(holder, items.length, nome);
        holder.setInventory(inventory);

        for (int i = 0; i < this.items.length; ++i) {
            if (this.items[i] != null)
                inventory.setItem(i, this.items[i].getIcon());
            else
                inventory.setItem(i, new ItemStack(Material.AIR));
        }

        player.openInventory(inventory);
    }

    private void inventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
        int slot = e.getRawSlot();
        if (slot > 0 && slot < items.length && items[slot] != null)
            items[slot].onClick(e);
    }

    public enum MenuSize {
        UMA_LINHA(9),
        DUAS_LINHAS(18),
        TRES_LINHAS(27),
        QUATRO_LINHAS(36),
        CINCO_LINHAS(45),
        SEIS_LINHAS(54);

        private final int slots;

        MenuSize(int slots) {
            this.slots = slots;
        }

        public int getSlots() {
            return slots;
        }
    }

    public interface MenuItemClick {
        void onClick(InventoryClickEvent e);
    }

    private class MenuItem {
        private final ItemStack icon;
        private final MenuItemClick itemClick;

        MenuItem(ItemStack icon, MenuItemClick itemClick) {
            this.icon = icon;
            this.itemClick = itemClick;
        }

        void onClick(InventoryClickEvent e) {
            if (itemClick != null)
                itemClick.onClick(e);
        }

        ItemStack getIcon() {
            return icon;
        }
    }

    private class MenuHolder implements InventoryHolder {
        private final Menu menu;
        private Inventory inventory;

        MenuHolder(Menu menu) {
            this.menu = menu;
        }

        Menu getMenu() {
            return menu;
        }

        public Inventory getInventory() {
            return this.inventory;
        }

        void setInventory(Inventory inventory) {
            this.inventory = inventory;
        }
    }
}
