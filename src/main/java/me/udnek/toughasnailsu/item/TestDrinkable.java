package me.udnek.toughasnailsu.item;

import me.udnek.itemscoreu.customitem.ConstructableCustomItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TestDrinkable extends ConstructableCustomItem implements DrinkableItem {
    @Override
    public double getThirstRestoration(@Nullable ItemStack itemStack) {
        return 5;
    }

    @Override
    public @NotNull String getRawId() {
        return "test";
    }

    @Override
    public Material getMaterial() {
        return Material.SWEET_BERRIES;
    }
}
