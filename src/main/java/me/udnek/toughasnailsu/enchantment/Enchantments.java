package me.udnek.toughasnailsu.enchantment;

import me.udnek.coreu.custom.enchantment.CustomEnchantment;
import me.udnek.coreu.custom.registry.CustomRegistries;
import me.udnek.toughasnailsu.ToughAsNailsU;

public class Enchantments {
    public static final CustomEnchantment NAIL = CustomRegistries.ENCHANTMENT.register(ToughAsNailsU.getInstance(), new Nail());
}
