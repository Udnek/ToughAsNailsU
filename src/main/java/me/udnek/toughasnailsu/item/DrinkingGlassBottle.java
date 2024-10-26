package me.udnek.toughasnailsu.item;

import me.udnek.itemscoreu.customitem.ConstructableCustomItem;
import org.bukkit.Material;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class DrinkingGlassBottle extends ConstructableCustomItem implements ToughAsNailsUCustomItem{
    @Override
    public @NotNull String getRawId() {return "drinking_glass_bottle";}
    @Override
    public @NotNull Material getMaterial() {return Material.GUNPOWDER;}
    @Override
    public @Nullable Integer getCustomModelData() {return 3000;}

    @Override
    protected void generateRecipes(@NotNull Consumer<@NotNull Recipe> consumer) {
        ShapedRecipe recipe = new ShapedRecipe(getNewRecipeKey(), getItem());
        recipe.shape(
                "G G",
                "G G",
                " G ");

        RecipeChoice.MaterialChoice glass = new RecipeChoice.MaterialChoice(Material.GLASS);
        recipe.setIngredient('G', glass);

        consumer.accept(recipe);
    }
}
