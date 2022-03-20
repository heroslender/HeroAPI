package com.heroslender.menu;

import com.heroslender.exceptions.inventory.ItemNotDefinedException;
import com.heroslender.exceptions.inventory.PageGreaterThanMaxPageException;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Function;

public class PaginatedMenu<T> extends Menu {

    private final List<T> inventoryData;
    private final Function<? super T, ? extends ItemStack> transformFunction;

    private final int previousPageSlot, nextPageSlot, maxPages;

    private int currentPage;

    public PaginatedMenu(
            String name, int size,
            List<T> inventoryData,
            Function<? super T, ? extends ItemStack> transformFunction,
            int previousPageSlot, int nextPageSlot
    ) {
        super(name, size);

        this.previousPageSlot = previousPageSlot;
        this.nextPageSlot = nextPageSlot;

        this.inventoryData = inventoryData;
        this.transformFunction = transformFunction;

        this.maxPages = (int) Math.ceil(inventoryData.size() / (size - 2f));
    }

    public PaginatedMenu(
            String name, MenuSize size,
            List<T> inventoryData,
            Function<? super T, ? extends ItemStack> transformFunction
    ) {
        this(name, size.getSlots(), inventoryData, transformFunction, size.getSlots() - 9, size.getSlots() - 1);
    }

    @Override
    public Menu open(HumanEntity humanEntity) {
        return open(humanEntity, 1);
    }

    /**
     * Open the menu to some {@link org.bukkit.entity.HumanEntity} in specific page, it can be a {@link Player}<br><br>
     * <p>
     * This method can throw {@link ItemNotDefinedException} if you not define the previous or next page items,
     * and {@link PageGreaterThanMaxPageException} if you use a page greater than the max page<br><br>
     * <p>
     * How the max page is calculated?
     * Assuming that we know the size of entries to be shown in this inventory (the data you used),
     * we can calculate it using {@code Math.ceil(data.size() / (inventorySize - 2f))}.<br>
     * The {@code -2f} is because we have the previous and next page items, so it occupy 2 slots in inventory
     *
     * @param humanEntity A bit obvious right?
     * @param page        the page to show
     * @return the current menu
     */
    public PaginatedMenu<T> open(HumanEntity humanEntity, int page) {
        if (items[previousPageSlot] == null)
            throw new ItemNotDefinedException("Você não definiu o item de voltar a página");
        if (items[nextPageSlot] == null)
            throw new ItemNotDefinedException("Você não definiu o item de adiantar a página");

        if (page > maxPages) throw new PageGreaterThanMaxPageException(page, maxPages);

        this.currentPage = page;

        MenuHolder holder = new MenuHolder(this);
        Inventory inventory = Bukkit.createInventory(
                holder,
                items.length,
                name.replace("%currentpage%", String.valueOf(page)).replace("%maxpage%", String.valueOf(maxPages))
        );
        holder.setInventory(inventory);

        inventory.setItem(previousPageSlot, items[previousPageSlot].getIcon());
        inventory.setItem(nextPageSlot, items[nextPageSlot].getIcon());

        // -2 porque existem os items de voltar e adiantar a página
        int index = (items.length - 2) * (page - 1), inventorySlot = 0;
        for (T t : inventoryData.subList(index, Math.min(index + items.length - 2, inventoryData.size()))) {
            if (inventorySlot == previousPageSlot || inventorySlot == nextPageSlot) ++inventorySlot;

            inventory.setItem(inventorySlot++, transformFunction.apply(t));
        }

        humanEntity.openInventory(inventory);

        return this;
    }

    public PaginatedMenu<T> setPreviousPageItem(ItemStack itemStack) {
        items[previousPageSlot] = new MenuItem(itemStack, e -> {
            if (currentPage < 1) return;
            e.setCancelled(true);
            e.getWhoClicked().getOpenInventory().close();
            open(e.getWhoClicked(), currentPage - 1);
        });

        return this;
    }

    public PaginatedMenu<T> setNextPageItem(ItemStack itemStack) {
        items[nextPageSlot] = new MenuItem(itemStack, e -> {
            if (currentPage >= maxPages) return;
            e.setCancelled(true);
            e.getWhoClicked().getOpenInventory().close();
            open(e.getWhoClicked(), currentPage + 1);
        });

        return this;
    }

    public int getCurrentPage() {
        return currentPage;
    }
}
