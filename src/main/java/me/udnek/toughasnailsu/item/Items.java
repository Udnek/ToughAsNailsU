package me.udnek.toughasnailsu.item;

import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import me.udnek.toughasnailsu.ToughAsNailsU;

public class Items{
    public static int TEA_DURATION = 5 * 60 * 20;
    public static int JUICE_DURATION = 3 * 60 * 20;
    public static double TEA_IMPACT = 18;
    public static double JUICE_IMPACT = -8;

    public static final CustomItem DRINKING_GLASS_BOTTLE = register(new DrinkingGlassBottle());
    public static final CustomItem FLASK = register(new Flask());

    public static final CustomItem DIRTY_WATER_BOTTLE = register(ConstructableDrinkableItem.dirty("dirty_water_bottle", 3, -2, 20*15));
    public static final CustomItem SEA_WATER_BOTTLE = register(ConstructableDrinkableItem.dirty("sea_water_bottle", 3, -2, 20*25));
    public static final CustomItem PURE_WATER_BOTTLE = register(ConstructableDrinkableItem.normal("pure_water_bottle", 6, -3, 20*20));
    public static final CustomItem BOILING_WATER_BOTTLE = register(ConstructableDrinkableItem.normal("boiling_water_bottle", 6, 6, 20*20));

    public static final CustomItem GREEN_SWEET_BERRY_TEA_BOTTLE = register(ConstructableDrinkableItem.normal("green_sweet_berry_tea_bottle", 10, TEA_IMPACT, TEA_DURATION));
    public static final CustomItem GREEN_GLOW_BERRY_TEA_BOTTLE = register(ConstructableDrinkableItem.normal("green_glow_berry_tea_bottle", 10, TEA_IMPACT, TEA_DURATION));
    public static final CustomItem GREEN_SUGAR_TEA_BOTTLE = register(ConstructableDrinkableItem.normal("green_sugar_tea_bottle", 10, TEA_IMPACT, TEA_DURATION));

    public static final CustomItem CARROT_JUICE_BOTTLE = register(ConstructableDrinkableItem.normal("carrot_juice_bottle", 10, JUICE_IMPACT, JUICE_DURATION));
    public static final CustomItem SWEET_BERRY_JUICE_BOTTLE = register(ConstructableDrinkableItem.normal("sweet_berry_juice_bottle",  10, JUICE_IMPACT, JUICE_DURATION));
    public static final CustomItem MELON_JUICE_BOTTLE = register(ConstructableDrinkableItem.normal("melon_juice_bottle", 10, JUICE_IMPACT, JUICE_DURATION));

    public static final CustomItem AMETHYST_WATER_BOTTLE = register(ConstructableDrinkableItem.normal("amethyst_water_bottle", 10, -15, 5 * 60 * 20));

    public static final CustomItem RAW_MILK_CACAO_BOTTLE = register(ConstructableDrinkableItem.normal("raw_milk_cacao_bottle", 8, -3, 20*50));
    public static final CustomItem MILK_CACAO_BOTTLE = register(ConstructableDrinkableItem.normal("milk_cacao_bottle", 10, TEA_IMPACT, 6 * 60 * 20));

    public static CustomItem register(CustomItem customItem){
        return CustomRegistries.ITEM.register(ToughAsNailsU.getInstance(), customItem);
    }
}
