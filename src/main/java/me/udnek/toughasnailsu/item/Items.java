package me.udnek.toughasnailsu.item;

import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import me.udnek.toughasnailsu.ToughAsNailsU;

public class Items {

    public static final CustomItem DIRTY_WATER_BOTTLE = register(ConstructableDrinkableItem.dirty("dirty_water_bottle", 500000, 6, 1, 20*10));
    public static final CustomItem SEA_WATER_BOTTLE = register(ConstructableDrinkableItem.normal("sea_water_bottle", 500001, 6, -1, 20*20));
    public static final CustomItem PURE_WATER_BOTTLE = register(ConstructableDrinkableItem.normal("pure_water_bottle", 500002, 6, 1, 20*20));
    public static final CustomItem BOILING_WATER_BOTTLE = register(ConstructableDrinkableItem.normal("boiling_water_bottle", 500003, 6, 1, 20*20));

    public static final CustomItem CARROT_JUICE_BOTTLE = register(ConstructableDrinkableItem.normal("carrot_juice_bottle", 500100, 6, 1, 20*20));
    public static final CustomItem SWEET_BERRY_JUICE_BOTTLE = register(ConstructableDrinkableItem.normal("sweet_berry_juice_bottle", 500101, 6, 1, 20*20));

    public static final CustomItem AMETHYST_WATER_BOTTLE = register(ConstructableDrinkableItem.normal("amethyst_water_bottle", 500110, 6, 1, 20*20));

    public static final CustomItem GREEN_SWEET_BERRY_TEA_BOTTLE = register(ConstructableDrinkableItem.normal("green_sweet_berry_tea_bottle", 500200, 6, 1, 20*20));
    public static final CustomItem GREEN_GLOW_BERRY_TEA_BOTTLE = register(ConstructableDrinkableItem.normal("green_glow_berry_tea_bottle", 500201, 6, 1, 20*20));
    public static final CustomItem GREEN_SUGAR_TEA_BOTTLE = register(ConstructableDrinkableItem.normal("green_sugar_tea_bottle", 500202, 6, 1, 20*20));

    public static CustomItem register(CustomItem customItem){
        return CustomRegistries.ITEM.register(ToughAsNailsU.getInstance(), customItem);
    }
}
