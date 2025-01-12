package me.udnek.toughasnailsu.component;

import me.udnek.itemscoreu.customcomponent.CustomComponent;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.customitem.CustomItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public interface InventoryInteractableItem extends CustomComponent<CustomItem> {
    InventoryInteractableItem EMPTY = new InventoryInteractableItem() {
        @Override
        public void onBeingClicked(@NotNull CustomItem item, @NotNull InventoryClickEvent event) {}
        @Override
        public void onClickWith(@NotNull CustomItem item, @NotNull InventoryClickEvent event) {}
    };

    void onBeingClicked(@NotNull CustomItem item, @NotNull InventoryClickEvent event);
    void onClickWith(@NotNull CustomItem item, @NotNull InventoryClickEvent event);

    default @NotNull CustomComponentType<CustomItem, ?> getType() {
        return ComponentTypes.INVENTORY_INTERACTABLE_ITEM;
    }
}
