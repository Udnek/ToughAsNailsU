package me.udnek.toughasnailsu.effect;

import me.udnek.coreu.custom.effect.ConstructableCustomEffect;
import org.bukkit.Color;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffectTypeCategory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HeatingEffect extends ConstructableCustomEffect {
    @Override
    public @NotNull PotionEffectTypeCategory getCategory() {
        return PotionEffectTypeCategory.BENEFICIAL;
    }

    @Override
    public @Nullable PotionEffectType getVanillaDisguise() {return null;}

    @Override
    public @NotNull String getRawId() {
        return "heating";
    }

    @Override
    public int getColorIfDefaultParticle() {
        return Color.ORANGE.asRGB();
    }
}
