package me.udnek.toughasnailsu.item;

import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customitem.CustomItemRegistry;
import me.udnek.toughasnailsu.ToughAsNailsU;

public class Items {

    public static final CustomItem TEST = register(new TestDrinkable());

    public static CustomItem register(CustomItem customItem){
        return CustomItemRegistry.getInstance().register(ToughAsNailsU.getInstance(), customItem);
    }
}
