package com.heroslender.Menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Menu {

    private final String nome;
    private MenuItem[] items;

    public Menu(String nome, int tamanho) {
        this.nome = nome;
        items = new MenuItem[tamanho];
    }

    public Menu(String nome, MenuSize tamanho) {
        this.nome = nome;
        items = new MenuItem[tamanho.getSlots()];
    }

    public void setItem(int slot, MenuItem menuItem) {
        items[slot] = menuItem;
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

    public void inventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
        int slot = e.getRawSlot();
        if (slot < items.length && items[slot] != null)
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

    @AllArgsConstructor
    public class MenuItem {
        @Getter
        private final ItemStack icon;
        private final MenuItemClick itemClick;

        public MenuItem(ItemStack icon) {
            this.icon = icon;
            itemClick = null;
        }

        void onClick(InventoryClickEvent e) {
            if (itemClick != null)
                itemClick.onClick(e);
        }
    }
}
