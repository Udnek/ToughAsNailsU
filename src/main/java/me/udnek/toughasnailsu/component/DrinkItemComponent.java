package me.udnek.toughasnailsu.component;

import me.udnek.itemscoreu.customcomponent.CustomComponent;
import me.udnek.itemscoreu.customcomponent.CustomComponentType;
import me.udnek.itemscoreu.customevent.CustomItemGeneratedEvent;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.util.ComponentU;
import me.udnek.itemscoreu.util.LoreBuilder;
import me.udnek.itemscoreu.util.Utils;
import me.udnek.toughasnailsu.data.Database;
import me.udnek.toughasnailsu.data.PlayerData;
import me.udnek.toughasnailsu.effect.Effects;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DrinkItemComponent implements CustomComponent<CustomItem> {

    public static final Key LORE_FONT_NORMAL = Key.key("toughasnailsu:thirst");
    public static final Key LORE_FONT_THIRST = Key.key("toughasnailsu:thirst_thirsty");

    public static final TextColor FREEZE_COLOR = TextColor.color(99, 155, 255);
    public static final TextColor HEAT_COLOR = TextColor.color(217, 131, 44);

    public static final DrinkItemComponent DEFAULT = new DrinkItemComponent(0, false, 0, 0){
        @Override
        public void modifyItem(@NotNull CustomItemGeneratedEvent event){}
    };

    protected final int thirstRestoration;
    protected final boolean inflictsThirst;
    protected final double temperatureImpact;
    protected final int temperatureImpactDuration;

    public DrinkItemComponent(int thirstRestoration, boolean inflictsThirst, double temperatureImpact, int temperatureImpactDuration){
        this.thirstRestoration = thirstRestoration;
        this.inflictsThirst = inflictsThirst;
        this.temperatureImpact = temperatureImpact;
        this.temperatureImpactDuration = temperatureImpactDuration;
    }

    public double getTemperatureImpact() {return temperatureImpact;}
    public int getTemperatureImpactDuration() {return temperatureImpactDuration;}
    public int getThirstRestoration() {return thirstRestoration;}
    public boolean isInflictsThirst() {return inflictsThirst;}

    public void onConsumption(@NotNull CustomItem customItem, PlayerItemConsumeEvent event){
        PlayerData playerData = Database.getInstance().get(event.getPlayer());
        if (getThirstRestoration() != 0) {
            playerData.getThirst().add(getThirstRestoration());
        }
        if (getTemperatureImpact() != 0){
            playerData.getTemperature().setFoodImpact(getTemperatureImpact(), getTemperatureImpactDuration());
        }
        if (inflictsThirst){
            Effects.THIRST.apply(event.getPlayer(), 40 * 20, 0);
        }
    }
    public void modifyItem(@NotNull CustomItemGeneratedEvent event){
        LoreBuilder loreBuilder = event.getLoreBuilder();

        Key font = isInflictsThirst() ? LORE_FONT_THIRST : LORE_FONT_NORMAL;
        Component thirst = Component.translatable(
                        "lore.toughasnailsu.thirst.level." + getThirstRestoration())
                .font(font)
                .color(ComponentU.NO_SHADOW_COLOR)
                .decoration(TextDecoration.ITALIC, false);

        Component effect = Component.translatable(getTemperatureImpact() > 0 ? "effect.drinkable.heating" : "effect.drinkable.cooling");
        TextColor color = getTemperatureImpact() > 0 ? HEAT_COLOR : FREEZE_COLOR;
        Component amount = Component.text(Utils.roundToTwoDigits(Math.abs(getTemperatureImpact())));
        Component temperature = Component.translatable("lore.drinkable.effect", List.of(effect, amount, Component.text(generateEffectDuration(getTemperatureImpactDuration()))))
                .color(color).decoration(TextDecoration.ITALIC, false);

        loreBuilder.add(LoreBuilder.Position.ATTRIBUTES.priority + 50, temperature);
        loreBuilder.add(LoreBuilder.Position.ATTRIBUTES.priority + 50, thirst);
    }

    public static String generateEffectDuration(int duration){
        int hours = duration / (20*60*60);
        duration = duration % (20*60*60);
        int minutes = duration / (20*60);
        duration = duration % (20*60);
        int seconds = duration / (20);

        String sHours = "";
        if (hours > 0){
            if (hours <= 9) sHours += "0";
            sHours += hours;
            sHours += ":";
        }
        String sOther = "";
        if (minutes <= 9) sOther += "0";
        sOther += minutes;
        sOther += ":";
        if (seconds <= 9) sOther += "0";
        sOther += seconds;

        return sHours + sOther;
    }

    @Override
    public @NotNull CustomComponentType<CustomItem, ?> getType() {
        return ComponentTypes.DRINK_ITEM;
    }
}
