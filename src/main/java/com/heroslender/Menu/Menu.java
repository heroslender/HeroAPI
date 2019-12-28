package com.heroslender.Menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class Menu {
    private final String name;
    private MenuItem[] items;

    /**
     * Create a new menu
     *
     * @param name Inventory name
     * @param size Inventory size
     */
    public Menu(String name, int size) {
        this.name = name;
        items = new MenuItem[size];
    }

    /**
     * Create a new menu
     *
     * @param name Inventory name
     * @param size Inventory size
     */
    public Menu(String name, MenuSize size) {
        this.name = name;
        items = new MenuItem[size.getSlots()];
    }

    /**
     * Register the Menu Listener
     *
     * @param plugin Your plugin instance
     */
    public static void register(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            private void onInvClick(InventoryClickEvent e) {
                if (!e.isCancelled() && e.getInventory().getHolder() instanceof MenuHolder)
                    ((MenuHolder) e.getInventory().getHolder()).getMenu().inventoryClick(e);
            }
        }, plugin);
    }

    /**
     * Set an item on the menu
     *
     * @param slot      The slot to display the item
     * @param itemStack The item to display
     */
    public void setItem(int slot, ItemStack itemStack) {
        setItem(slot, itemStack, null);
    }

    /**
     * Set an item on the menu
     *
     * @param slot      The slot to display the item
     * @param itemStack The item to display
     * @param itemClick The callback when the player clicks the inventory
     */
    public void setItem(int slot, ItemStack itemStack, MenuItemClick itemClick) {
        setItem(slot, new MenuItem(itemStack, itemClick));
    }

    /**
     * Set an item on the menu
     *
     * @param slot     The slot to display the item
     * @param menuItem The item to display
     */
    public void setItem(int slot, MenuItem menuItem) {
        items[slot] = menuItem;
    }

    /**
     * Open the menu to some {@link org.bukkit.entity.HumanEntity}, it can be a {@link Player}
     *
     * @param humanEntity A bit obvious right?
     */
    public void open(HumanEntity humanEntity) {
        MenuHolder holder = new MenuHolder(this);
        Inventory inventory = Bukkit.createInventory(holder, items.length, name);
        holder.setInventory(inventory);

        for (int i = 0; i < this.items.length; ++i) {
            if (this.items[i] != null)
                inventory.setItem(i, this.items[i].getIcon());
            else
                inventory.setItem(i, new ItemStack(Material.AIR));
        }

        humanEntity.openInventory(inventory);
    }

    private void inventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
        int slot = e.getRawSlot();
        if (slot >= 0 && slot < items.length && items[slot] != null)
            items[slot].onClick(e);
    }

    public enum MenuSize {
        ONE_LINE(9),
        TWO_LINES(18),
        THREE_LINES(27),
        FOUR_LINES(36),
        FIVE_LINES(45),
        SIX_LINES(54);

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

    public class MenuItem {
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
