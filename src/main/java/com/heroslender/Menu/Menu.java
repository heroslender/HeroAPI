package com.heroslender.menu;

import com.heroslender.exceptions.inventory.InvalidInventorySizeException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("UnusedReturnValue")
public class Menu {
    protected final String name;
    protected final MenuItem[] items;

    static {
        register(JavaPlugin.getProvidingPlugin(Menu.class));
    }

    /**
     * Create a new menu
     *
     * @param name Inventory name
     * @param size Inventory size
     */
    public Menu(String name, int size) {
        if (size % 9 != 0) throw new InvalidInventorySizeException(size);

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
        this(name, size.getSlots());
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
                if (!e.isCancelled() && e.getInventory().getHolder() instanceof MenuHolder) {
                    MenuHolder holder = (MenuHolder) e.getInventory().getHolder();
                    holder.getMenu().inventoryClick(e, holder);
                }
            }
        }, plugin);
    }

    /**
     * Set an item on the menu
     *
     * @param slot      The slot to display the item
     * @param itemStack The item to display
     */
    public Menu setItem(int slot, ItemStack itemStack) {
        return setItem(slot, itemStack, null);
    }

    /**
     * Set an item on the menu
     *
     * @param slot      The slot to display the item
     * @param itemStack The item to display
     * @param itemClick The callback when the player clicks the inventory
     */
    public Menu setItem(int slot, ItemStack itemStack, MenuClickCallback itemClick) {
        return setItem(slot, new MenuItem(itemStack, itemClick));
    }

    /**
     * Set an item on the menu
     *
     * @param slot     The slot to display the item
     * @param menuItem The item to display
     */
    public Menu setItem(int slot, MenuItem menuItem) {
        items[slot] = menuItem;
        return this;
    }

    /**
     * Open the menu to some {@link Player}.
     *
     * @param player The player to open the menu to
     * @return The menu
     */
    public Menu open(Player player) {
        return open(player, new MenuHolder());
    }

    /**
     * Open the menu to some {@link Player}.
     *
     * @param player The player to open the menu to
     * @param holder The holder of the menu
     * @return The menu
     */
    public Menu open(Player player, MenuHolder holder) {
        Inventory inventory = Bukkit.createInventory(holder, items.length, name);
        holder.setMenu(this);
        holder.setInventory(inventory);

        for (int i = 0; i < this.items.length; ++i) {
            if (this.items[i] != null)
                inventory.setItem(i, this.items[i].getIcon());
            else
                inventory.setItem(i, new ItemStack(Material.AIR));
        }

        player.openInventory(inventory);
        return this;
    }

    private void inventoryClick(InventoryClickEvent e, MenuHolder holder) {
        e.setCancelled(true);

        int slot = e.getRawSlot();
        if (slot >= 0 && slot < items.length && items[slot] != null) {
            MenuClick menuClick = new MenuClick((Player) e.getWhoClicked(), holder, e);
            items[slot].onClick(menuClick);
        }
    }

    @SuppressWarnings("unused")
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

    public static class MenuHolder implements InventoryHolder {
        private Menu menu;
        private Inventory inventory;

        public MenuHolder() {
        }

        public Menu getMenu() {
            return menu;
        }

        public void setMenu(Menu menu) {
            this.menu = menu;
        }

        public Inventory getInventory() {
            return this.inventory;
        }

        public void setInventory(Inventory inventory) {
            this.inventory = inventory;
        }
    }

    public static class MenuItem {
        private final ItemStack icon;
        private final MenuClickCallback itemClick;

        public MenuItem(ItemStack icon, MenuClickCallback itemClick) {
            this.icon = icon;
            this.itemClick = itemClick;
        }

        void onClick(MenuClick menuClick) {
            if (itemClick != null) {
                itemClick.onClick(menuClick);
            }
        }

        public ItemStack getIcon() {
            return icon;
        }
    }

    public interface MenuClickCallback {
        void onClick(MenuClick e);
    }

    @SuppressWarnings("unused")
    public static class MenuClick implements Cancellable {
        private final Player player;
        private final MenuHolder holder;
        private final InventoryClickEvent event;

        public MenuClick(Player player, MenuHolder holder, InventoryClickEvent event) {
            this.player = player;
            this.holder = holder;
            this.event = event;
        }

        public Player getPlayer() {
            return player;
        }

        public Player getWhoClicked() {
            return player;
        }

        public MenuHolder getHolder() {
            return holder;
        }

        public InventoryClickEvent getEvent() {
            return event;
        }

        public int getSlot() {
            return event.getRawSlot();
        }

        public Inventory getInventory() {
            return event.getInventory();
        }

        public Inventory getClickedInventory() {
            return event.getClickedInventory();
        }


        @Override
        public boolean isCancelled() {
            return event.isCancelled();
        }

        @Override
        public void setCancelled(boolean cancel) {
            event.setCancelled(cancel);
        }
    }
}
