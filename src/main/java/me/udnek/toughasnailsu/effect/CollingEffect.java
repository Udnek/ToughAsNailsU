package me.udnek.toughasnailsu.effect;

import me.udnek.coreu.custom.effect.ConstructableCustomEffect;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffectTypeCategory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CollingEffect extends ConstructableCustomEffect {
    @Override
    public @NotNull PotionEffectTypeCategory getCategory() {
        return PotionEffectTypeCategory.BENEFICIAL;
    }

    @Override
    public @Nullable PotionEffectType getVanillaDisguise() {return null;}

    @Override
    public @NotNull String getRawId() {
        return "colling";
    }

    @Override
    public int getColorIfDefaultParticle() {
        return NamedTextColor.AQUA.value();
    }
}
