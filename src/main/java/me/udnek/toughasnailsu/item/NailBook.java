package me.udnek.toughasnailsu.item;

import me.udnek.itemscoreu.customenchantment.ConstructableCustomEnchantment;
import me.udnek.itemscoreu.customitem.ConstructableCustomItem;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.toughasnailsu.enchantment.Enchantments;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class NailBook extends ConstructableCustomItem {
    @Override
    public @NotNull String getRawId() {
        return "nail_book";
    }

    @Override
    public @Nullable NamespacedKey getItemModel() {return null;}

    @Override
    public @Nullable Component getItemName() {
        return null;
    }

    @Override
    protected void modifyFinalItemStack(@NotNull ItemStack itemStack) {
        super.modifyFinalItemStack(itemStack);
        Enchantments.NAIL.enchantBook(itemStack, 1);
    }

    @Override
    public void getRecipes(@NotNull Consumer<@NotNull Recipe> consumer) {
        ShapedRecipe recipe = new ShapedRecipe(getNewRecipeKey(), getItem());
        recipe.shape(
                " M ",
                "ABH",
                " S ");

        recipe.setIngredient('B', new RecipeChoice.MaterialChoice(Material.BOOK));
        recipe.setIngredient('A', new RecipeChoice.MaterialChoice(Material.AMETHYST_SHARD));
        recipe.setIngredient('M', new RecipeChoice.MaterialChoice(Material.MAGMA_CREAM));
        recipe.setIngredient('S', new RecipeChoice.MaterialChoice(Material.SNOWBALL));
        recipe.setIngredient('H', new RecipeChoice.MaterialChoice(Material.HONEYCOMB));
        consumer.accept(recipe);
    }

    @Override
    public @NotNull Material getMaterial() {
        return Material.ENCHANTED_BOOK;
    }
}
