package me.udnek.toughasnailsu.util;

import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.utils.SelfRegisteringListener;
import me.udnek.toughasnailsu.item.DrinkableItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EventsListener extends SelfRegisteringListener {
    public EventsListener(JavaPlugin plugin) {super(plugin);}

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerConsume(PlayerItemConsumeEvent event){
        CustomItem customItem = CustomItem.get(event.getItem());
        if (customItem instanceof DrinkableItem drinkableItem){
            drinkableItem.onConsumption(event);
        }
    }
}
