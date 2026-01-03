package me.udnek.toughasnailsu.effect;

import me.udnek.coreu.custom.attribute.CustomAttributeConsumer;
import me.udnek.coreu.custom.attribute.CustomAttributeModifier;
import me.udnek.coreu.custom.effect.ConstructableCustomEffect;
import me.udnek.toughasnailsu.attribute.Attributes;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffectTypeCategory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TemperatureFortifyEffect extends ConstructableCustomEffect {
    @Override
    public @NotNull PotionEffectTypeCategory getCategory() {
        return PotionEffectTypeCategory.BENEFICIAL;
    }

    @Override
    public @Nullable PotionEffectType getVanillaDisguise() {
        return PotionEffectType.INSTANT_HEALTH;
    }

    @Override
    public void getCustomAttributes(@NotNull PotionEffect context, @NotNull CustomAttributeConsumer consumer) {
        consumer.accept(Attributes.HEAT_RESISTANCE, new CustomAttributeModifier(100, AttributeModifier.Operation.ADD_NUMBER));
        consumer.accept(Attributes.COLD_RESISTANCE, new CustomAttributeModifier(100, AttributeModifier.Operation.ADD_NUMBER));
    }

    @Override
    public int getColorIfDefaultParticle() {
        return TextColor.fromHexString("#55f6df").value();
    }

    @Override
    public @NotNull String getRawId() {
        return "temperature_fortify";
    }
}
