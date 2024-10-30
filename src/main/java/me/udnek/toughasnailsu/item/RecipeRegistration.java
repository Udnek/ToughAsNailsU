package me.udnek.toughasnailsu.item;

import me.udnek.itemscoreu.customitem.CustomItem;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class RecipeRegistration {
    public static void run(){
        registerFurnaceItemStack(Items.PURE_WATER_BOTTLE,  List.of(Items.DIRTY_WATER_BOTTLE.getItem(), Items.SEA_WATER_BOTTLE.getItem()));
        registerShapeless(Items.AMETHYST_WATER_BOTTLE, new Material[]{Material.AMETHYST_SHARD, Material.AMETHYST_SHARD}, new CustomItem[]{Items.PURE_WATER_BOTTLE});
        registerFurnaceItemStack(Items.BOILING_WATER_BOTTLE, List.of(Items.PURE_WATER_BOTTLE.getItem()));
        registerShapeless(Items.CARROT_JUICE_BOTTLE, new Material[]{Material.CARROT, Material.CARROT}, new CustomItem[]{Items.DRINKING_GLASS_BOTTLE});
        registerShapeless(Items.SWEET_BERRY_JUICE_BOTTLE, new Material[]{Material.SWEET_BERRIES, Material.SWEET_BERRIES}, new CustomItem[]{Items.DRINKING_GLASS_BOTTLE});
        registerShapeless(Items.MELON_JUICE_BOTTLE, new Material[]{Material.MELON_SLICE, Material.MELON_SLICE}, new CustomItem[]{Items.DRINKING_GLASS_BOTTLE});
        registerShapeless(Items.RAW_MILK_CACAO_BOTTLE, new Material[]{Material.MILK_BUCKET, Material.SUGAR, Material.COCOA_BEANS, Material.COCOA_BEANS}, new CustomItem[]{Items.DRINKING_GLASS_BOTTLE});
        registerFurnaceItemStack(Items.MILK_CACAO_BOTTLE, List.of(Items.RAW_MILK_CACAO_BOTTLE.getItem()));
        registerShapeless(Items.GREEN_SWEET_BERRY_TEA_BOTTLE, new Material[]{Material.SWEET_BERRIES}, new CustomItem[]{Items.BOILING_WATER_BOTTLE}, new Tag[]{Tag.LEAVES, Tag.LEAVES});
        registerShapeless(Items.GREEN_GLOW_BERRY_TEA_BOTTLE, new Material[]{Material.GLOW_BERRIES}, new CustomItem[]{Items.BOILING_WATER_BOTTLE}, new Tag[]{Tag.LEAVES, Tag.LEAVES});
        registerShapeless(Items.GREEN_SUGAR_TEA_BOTTLE, new Material[]{Material.SUGAR}, new CustomItem[]{Items.BOILING_WATER_BOTTLE}, new Tag[]{Tag.LEAVES, Tag.LEAVES});
    }

    public static void registerShapeless(@NotNull CustomItem result, @NotNull Material[] materials, @NotNull CustomItem[] customItems){
        registerShapeless(result, materials, customItems, new Tag[0], new Tag[0]);
    }

    public static void registerShapeless(@NotNull CustomItem result, @NotNull Material[] materials, @NotNull CustomItem[] customItems, @NotNull Tag<Material>[] materialsTags){
        registerShapeless(result, materials, customItems, materialsTags, new Tag[0]);
    }

    public static void registerShapeless(@NotNull CustomItem result, @NotNull Material[] materials, @NotNull CustomItem[] customItems, @NotNull Tag<Material>[] materialsTags, @NotNull Tag<CustomItem>[] customItemTags){
        ShapelessRecipe recipe = new ShapelessRecipe(result.getNewRecipeKey(), result.getItem());
        ArrayList<ItemStack> itemStacks = new ArrayList<>();


        for (Material material : materials ){recipe.addIngredient(new RecipeChoice.MaterialChoice(material));}
        for (CustomItem customItem : customItems){recipe.addIngredient(new RecipeChoice.ExactChoice(customItem.getItem()));}
        for (Tag<Material> materialTag : materialsTags){recipe.addIngredient(new RecipeChoice.MaterialChoice(materialTag));}
        for (Tag<CustomItem> customItemTag : customItemTags){
            for (CustomItem customItem : customItemTag.getValues()){
                itemStacks.add(customItem.getItem());
           recipe.addIngredient(new RecipeChoice.ExactChoice(itemStacks));}}

        result.registerRecipe(recipe);
    }

    public static void registerFurnaceItemStack(@NotNull CustomItem result, @NotNull List<ItemStack> input){
        registerFurnaceItemStack(result,  input, true, true);
    }

    public static void registerFurnaceMaterial(@NotNull CustomItem result, @NotNull List<Material> input){
        registerFurnaceMaterial(result,  input, true, true);
    }

    public static void registerFurnaceMaterial(@NotNull CustomItem result, @NotNull List<Material> input, boolean addFurnace, boolean addSmoker){
        if (addFurnace){
            result.registerRecipe(new FurnaceRecipe(result.getNewRecipeKey(), result.getItem(),
                    new RecipeChoice.MaterialChoice(input), 0.7f, 200));}
        if (addSmoker){
            result.registerRecipe(new SmokingRecipe(result.getNewRecipeKey(), result.getItem(),
                    new RecipeChoice.MaterialChoice(input), 0.7f, 100));}
    }

    public static void registerFurnaceItemStack(@NotNull CustomItem result, @NotNull List<ItemStack> input, boolean addFurnace, boolean addSmoker){
        if (addFurnace){
            result.registerRecipe(new FurnaceRecipe(result.getNewRecipeKey(), result.getItem(),
                    new RecipeChoice.ExactChoice(input), 0.7f, 200));}
        if (addSmoker){
            result.registerRecipe(new SmokingRecipe(result.getNewRecipeKey(), result.getItem(),
                    new RecipeChoice.ExactChoice(input), 0.7f, 100));}
    }
}
