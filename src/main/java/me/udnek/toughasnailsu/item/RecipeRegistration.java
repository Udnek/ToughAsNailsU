package me.udnek.toughasnailsu.item;

import me.udnek.coreu.custom.recipe.builder.CookingRecipeBuilder;
import me.udnek.coreu.custom.recipe.builder.ShapelessRecipeBuilder;
import me.udnek.toughasnailsu.ToughAsNailsU;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.RecipeChoice;
import org.jspecify.annotations.NullMarked;

import java.util.List;

import static me.udnek.coreu.custom.recipe.builder.CookingRecipeBuilder.Type.*;

@NullMarked
public class RecipeRegistration {
    public static void run(){
        new CookingRecipeBuilder(Items.PURE_WATER_BOTTLE, new RecipeChoice.ExactChoice(List.of(Items.DIRTY_WATER_BOTTLE.getItem(), Items.SEA_WATER_BOTTLE.getItem())))
                .types(FURNACE, SMOKER).build(ToughAsNailsU.getInstance());
        new CookingRecipeBuilder(Items.BOILING_WATER_BOTTLE, new RecipeChoice.ExactChoice(Items.PURE_WATER_BOTTLE.getItem()))
                .types(FURNACE, SMOKER).build(ToughAsNailsU.getInstance());
        new CookingRecipeBuilder(Items.MILK_CACAO_BOTTLE, new RecipeChoice.ExactChoice(Items.RAW_MILK_CACAO_BOTTLE.getItem()))
                .types(FURNACE, SMOKER).build(ToughAsNailsU.getInstance());

        new ShapelessRecipeBuilder(Items.AMETHYST_WATER_BOTTLE).addIngredient(Material.AMETHYST_SHARD, 2)
                .addIngredient(Items.PURE_WATER_BOTTLE, 1).build(ToughAsNailsU.getInstance());
        new ShapelessRecipeBuilder(Items.CARROT_JUICE_BOTTLE).addIngredient(Material.CARROT, 2)
                .addIngredient(Items.DRINKING_GLASS_BOTTLE, 1).build(ToughAsNailsU.getInstance());
        new ShapelessRecipeBuilder(Items.SWEET_BERRY_JUICE_BOTTLE).addIngredient(Material.SWEET_BERRIES, 2)
                .addIngredient(Items.DRINKING_GLASS_BOTTLE, 1).build(ToughAsNailsU.getInstance());
        new ShapelessRecipeBuilder(Items.MELON_JUICE_BOTTLE).addIngredient(Material.MELON_SLICE, 2)
                .addIngredient(Items.DRINKING_GLASS_BOTTLE, 1).build(ToughAsNailsU.getInstance());
        new ShapelessRecipeBuilder(Items.RAW_MILK_CACAO_BOTTLE).addIngredient(Material.MILK_BUCKET, 1)
                .addIngredient(Material.SUGAR, 1).addIngredient(Material.COCOA_BEANS, 1)
                .addIngredient(Material.COCOA_BEANS, 1).addIngredient(Items.DRINKING_GLASS_BOTTLE, 1)
                .build(ToughAsNailsU.getInstance());
        new ShapelessRecipeBuilder(Items.GREEN_SWEET_BERRY_TEA_BOTTLE).addIngredient(Material.SWEET_BERRIES, 1).addIngredient(Tag.LEAVES, 2)
                .addIngredient(Items.BOILING_WATER_BOTTLE, 1).build(ToughAsNailsU.getInstance());
        new ShapelessRecipeBuilder(Items.GREEN_GLOW_BERRY_TEA_BOTTLE).addIngredient(Material.GLOW_BERRIES, 1).addIngredient(Tag.LEAVES, 2)
                .addIngredient(Items.BOILING_WATER_BOTTLE, 1).build(ToughAsNailsU.getInstance());
        new ShapelessRecipeBuilder(Items.GREEN_SUGAR_TEA_BOTTLE).addIngredient(Material.SUGAR, 1).addIngredient(Tag.LEAVES, 2)
                .addIngredient(Items.BOILING_WATER_BOTTLE, 1).build(ToughAsNailsU.getInstance());
    }
}
