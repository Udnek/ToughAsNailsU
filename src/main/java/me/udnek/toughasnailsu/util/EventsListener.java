package me.udnek.toughasnailsu.util;

import me.udnek.itemscoreu.utils.SelfRegisteringListener;
import me.udnek.toughasnailsu.data.PlayerDatabase;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.plugin.java.JavaPlugin;

public class EventsListener extends SelfRegisteringListener {
    public EventsListener(JavaPlugin plugin) {super(plugin);}

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerEats(PlayerItemConsumeEvent event){
        FoodComponent foodComponent = event.getItem().getItemMeta().getFood();
        float food = foodComponent.getSaturation() + foodComponent.getNutrition();
        PlayerDatabase.getInstance().get(event.getPlayer()).getThirst().add(-food);
    }
}
