package me.udnek.toughasnailsu.effect;

import me.udnek.itemscoreu.customeffect.ConstructableCustomEffect;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffectTypeCategory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ThirstEffect extends ConstructableCustomEffect {
    @Override
    public @NotNull PotionEffectTypeCategory getCategory() {return PotionEffectTypeCategory.HARMFUL;}

    @Override
    public @Nullable PotionEffectType getVanillaDisguise() {return PotionEffectType.INSTANT_DAMAGE;}

    @Override
    public @NotNull String getRawId() {return "thirst";}

    @Override
    public int getColorIfDefaultParticle() {
        return NamedTextColor.GREEN.value();
    }
}
