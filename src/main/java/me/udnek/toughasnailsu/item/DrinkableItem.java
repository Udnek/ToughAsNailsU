package me.udnek.toughasnailsu.item;

import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customitem.CustomItemProperties;
import me.udnek.itemscoreu.utils.ComponentU;
import me.udnek.toughasnailsu.data.PlayerDatabase;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public interface DrinkableItem extends CustomItem, CustomItemProperties {

    Key LORE_FONT = Key.key("toughasnailsu:thirst");

    double getThirstRestoration(@Nullable ItemStack itemStack);

    default void onConsumption(PlayerItemConsumeEvent event){
        double restoration = getThirstRestoration(event.getItem());
        PlayerDatabase.getInstance().get(event.getPlayer()).getThirst().add(restoration);
    }

    @Override
    default List<Component> getLore() {
        return List.of(
                Component.translatable(
                        "lore.toughasnailsu.thirst.level." + ((int) getThirstRestoration(null)))
                        .font(LORE_FONT)
                        .color(ComponentU.NO_SHADOW_COLOR)
                        .decoration(TextDecoration.ITALIC, false)
        );
    }
}
