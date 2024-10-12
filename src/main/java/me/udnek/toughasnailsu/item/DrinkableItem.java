/*
package me.udnek.toughasnailsu.item;

import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customitem.CustomItemProperties;
import me.udnek.itemscoreu.utils.ComponentU;
import me.udnek.toughasnailsu.data.PlayerData;
import me.udnek.toughasnailsu.data.Database;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface DrinkableItem extends CustomItem, CustomItemProperties {

    Key LORE_FONT_NORMAL = Key.key("toughasnailsu:thirst");
    Key LORE_FONT_THIRST = Key.key("toughasnailsu:thirst_thirsty");

    TextColor FREEZE_COLOR = TextColor.color(99, 155, 255);
    TextColor HEAT_COLOR = TextColor.color(217, 131, 44);


    default void onConsumption(PlayerItemConsumeEvent event){
        PlayerData playerData = Database.getInstance().get(event.getPlayer());
        playerData.getThirst().add(getThirstRestoration());
        playerData.getTemperature().setFoodImpact(getTemperatureImpact(), getTemperatureImpactDuration());
    }

    static String generateEffectDuration(int duration){
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
    default List<Component> getLore() {
        Key font = inflictsThirst() ? LORE_FONT_THIRST : LORE_FONT_NORMAL;
        Component thirst = Component.translatable(
                        "lore.toughasnailsu.thirst.level." + ((int) getThirstRestoration()))
                .font(font)
                .color(ComponentU.NO_SHADOW_COLOR)
                .decoration(TextDecoration.ITALIC, false);

        Component effect = Component.translatable(getTemperatureImpact() > 0 ? "effect.drinkable.heating" : "effect.drinkable.cooling");
        TextColor color = getTemperatureImpact() > 0 ? HEAT_COLOR : FREEZE_COLOR;
        Component amount = Component.text((int) Math.abs(getTemperatureImpact()) * 100 + "%");
        Component temperature = Component.translatable("lore.drinkable.effect", List.of(effect, amount, Component.text(generateEffectDuration(getTemperatureImpactDuration()))))
                .color(color).decoration(TextDecoration.ITALIC, false);

        return List.of(temperature, thirst);
    }
    boolean inflictsThirst();
    double getTemperatureImpact();
    int getTemperatureImpactDuration();
    double getThirstRestoration();
    @Override
    @Nullable
    default Integer getMaxStackSize() {
        if (getMaterial().getMaxStackSize() == 64) return null;
        return 64;
    }

    @Override
    default FoodComponent getFoodComponent() {
        FoodComponent food = new ItemStack(Material.SWEET_BERRIES).getItemMeta().getFood();
        food.setSaturation(0);
        food.setNutrition(0);
        food.setCanAlwaysEat(true);
        //food.addEffect(new PotionEffect(PotionEffectType.LUCK, 20*5, 0), 0.5f);
        return food;
    }
}
*/
