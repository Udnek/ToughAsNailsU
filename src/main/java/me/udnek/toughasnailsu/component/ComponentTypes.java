package me.udnek.toughasnailsu.component;

import me.udnek.itemscoreu.customcomponent.ConstructableComponentType;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import me.udnek.toughasnailsu.ToughAsNailsU;

public class ComponentTypes {
    public static final CustomComponentType<CustomItem, DrinkItem>
            DRINK_ITEM = register(new ConstructableComponentType("drink_item", DrinkItem.DEFAULT));

    public static final CustomComponentType<CustomItem, InventoryInteractableItem>
            INVENTORY_INTERACTABLE_ITEM = register(new ConstructableComponentType("inventory_interactable_item", InventoryInteractableItem.EMPTY));

    private static CustomComponentType register(CustomComponentType type){
        return CustomRegistries.COMPONENT_TYPE.register(ToughAsNailsU.getInstance(), type);
    }
}
