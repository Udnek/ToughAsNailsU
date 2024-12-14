package me.udnek.toughasnailsu.enchantment;

import me.udnek.itemscoreu.customenchantment.CustomEnchantment;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import me.udnek.toughasnailsu.ToughAsNailsU;

public class Enchantments {
    public static final CustomEnchantment NAIL = CustomRegistries.ENCHANTMENT.register(ToughAsNailsU.getInstance(), new Nail());
}
