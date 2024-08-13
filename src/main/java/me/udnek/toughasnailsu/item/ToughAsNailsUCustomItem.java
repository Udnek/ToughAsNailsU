package me.udnek.toughasnailsu.item;

import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customitem.CustomItemProperties;

public interface ToughAsNailsUCustomItem extends CustomItem, CustomItemProperties {
    @Override
    default String getRawItemName() {return "item.toughasnailsu." + getRawId();}
}
