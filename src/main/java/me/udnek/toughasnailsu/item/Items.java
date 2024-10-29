package me.udnek.toughasnailsu.item;

import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import me.udnek.toughasnailsu.ToughAsNailsU;

public class Items{

    public static final CustomItem  DRINKING_GLASS_BOTTLE = register(new DrinkingGlassBottle());

    public static final CustomItem DIRTY_WATER_BOTTLE = register(ConstructableDrinkableItem.dirty("dirty_water_bottle", 500000, 3, -0.1, 20*15));
    public static final CustomItem SEA_WATER_BOTTLE = register(ConstructableDrinkableItem.dirty("sea_water_bottle", 500001, 3, -0.1, 20*25));
    public static final CustomItem PURE_WATER_BOTTLE = register(ConstructableDrinkableItem.normal("pure_water_bottle", 500002, 6, -0.15, 20*20));
    public static final CustomItem BOILING_WATER_BOTTLE = register(ConstructableDrinkableItem.normal("boiling_water_bottle", 500003, 6, 0.15, 20*20));

    public static final CustomItem GREEN_SWEET_BERRY_TEA_BOTTLE = register(ConstructableDrinkableItem.normal("green_sweet_berry_tea_bottle", 500200, 10, 0.3, 20*(40+60)));
    public static final CustomItem GREEN_GLOW_BERRY_TEA_BOTTLE = register(ConstructableDrinkableItem.normal("green_glow_berry_tea_bottle", 500201, 10, 0.3, 20*(40+60)));
    public static final CustomItem GREEN_SUGAR_TEA_BOTTLE = register(ConstructableDrinkableItem.normal("green_sugar_tea_bottle", 500202, 10, 0.3, 20*(40+60)));

    public static final CustomItem CARROT_JUICE_BOTTLE = register(ConstructableDrinkableItem.normal("carrot_juice_bottle", 500100, 10, -0.2, 20*40));
    public static final CustomItem SWEET_BERRY_JUICE_BOTTLE = register(ConstructableDrinkableItem.normal("sweet_berry_juice_bottle", 500101, 10, -0.2, 20*40));
    public static final CustomItem MELON_JUICE_BOTTLE = register(ConstructableDrinkableItem.normal("melon_juice_bottle", 500102, 10, -0.2, 20*40));

    public static final CustomItem AMETHYST_WATER_BOTTLE = register(ConstructableDrinkableItem.normal("amethyst_water_bottle", 500110, 10, -0.35, 20*(40+60)));

    public static final CustomItem MILK_CACAO_BOTTLE = register(ConstructableDrinkableItem.normal("milk_cacao_bottle", 500300, 8, -0.15, 20*50));
    public static final CustomItem RAW_MILK_CACAO_BOTTLE = register(ConstructableDrinkableItem.normal("raw_milk_cacao_bottle", 500301, 10, 0.3, 20*(40+60)));

    public static CustomItem register(CustomItem customItem){
        return CustomRegistries.ITEM.register(ToughAsNailsU.getInstance(), customItem);
    }
}
