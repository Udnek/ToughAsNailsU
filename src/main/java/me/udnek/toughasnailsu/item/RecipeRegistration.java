package me.udnek.toughasnailsu.item;

import me.udnek.itemscoreu.customitem.CustomItem;
import org.bukkit.Material;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RecipeRegistration {
    public static void run(){
        registerFurnace(Items.PURE_WATER_BOTTLE,  Items.DIRTY_WATER_BOTTLE);
        registerFurnace(Items.PURE_WATER_BOTTLE,  Items.SEA_WATER_BOTTLE);
        registerShapeless(Items.AMETHYST_WATER_BOTTLE, new Material[]{Material.AMETHYST_SHARD, Material.AMETHYST_SHARD}, new CustomItem[]{Items.PURE_WATER_BOTTLE});
        registerFurnace(Items.BOILING_WATER_BOTTLE, Items.PURE_WATER_BOTTLE);
        registerShapeless(Items.CARROT_JUICE_BOTTLE, new Material[]{Material.CARROT, Material.CARROT}, new CustomItem[]{Items.DRINKING_GLASS_BOTTLE});
        registerShapeless(Items.SWEET_BERRY_JUICE_BOTTLE, new Material[]{Material.SWEET_BERRIES, Material.SWEET_BERRIES}, new CustomItem[]{Items.DRINKING_GLASS_BOTTLE});
    }

    public static void registerShapeless(@NotNull CustomItem result, @Nullable Material[] materials, @Nullable CustomItem[] customItems){
        ShapelessRecipe recipe = new ShapelessRecipe(result.getNewRecipeKey(), result.getItem());

        for (Material material : materials ){if  (material != null){recipe.addIngredient(new RecipeChoice.MaterialChoice(material));}}
        for (CustomItem customItem : customItems){if (customItem != null){recipe.addIngredient(new RecipeChoice.ExactChoice(customItem.getItem()));}}

        result.registerRecipe(recipe);
    }

    public static void registerFurnace(@NotNull CustomItem result, @NotNull Material material){
            result.registerRecipe(new FurnaceRecipe(result.getNewRecipeKey(), result.getItem(), material, 0.7f, 200));
    }

    public static void registerFurnace(@NotNull CustomItem result, @NotNull CustomItem customItem){
        result.registerRecipe(new FurnaceRecipe(result.getNewRecipeKey(), result.getItem(), new RecipeChoice.ExactChoice(customItem.getItem()), 0.7f, 200));
    }
}
