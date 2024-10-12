package me.udnek.toughasnailsu.util;

import me.udnek.itemscoreu.customattribute.CustomAttributesContainer;
import me.udnek.itemscoreu.customcomponent.instance.CustomItemAttributesComponent;
import me.udnek.itemscoreu.customequipmentslot.CustomEquipmentSlot;
import me.udnek.itemscoreu.customevent.CustomItemGeneratedEvent;
import me.udnek.itemscoreu.customevent.GlobalInitializationEndEvent;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customitem.VanillaBasedCustomItem;
import me.udnek.itemscoreu.utils.SelfRegisteringListener;
import me.udnek.itemscoreu.utils.VanillaItemManager;
import me.udnek.toughasnailsu.attribute.Attributes;
import me.udnek.toughasnailsu.component.ComponentTypes;
import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EventsListener extends SelfRegisteringListener {
    public EventsListener(JavaPlugin plugin) {super(plugin);}

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerConsume(PlayerItemConsumeEvent event){
        CustomItem customItem = CustomItem.get(event.getItem());
        if (customItem == null) return;
        customItem.getComponentOrDefault(ComponentTypes.DRINK_ITEM).onConsumption(customItem, event);
    }
    @EventHandler
    public void onGenerate(CustomItemGeneratedEvent event){
        CustomItem customItem = event.getCustomItem();
        customItem.getComponentOrDefault(ComponentTypes.DRINK_ITEM).modifyItem(event);
    }
    @EventHandler
    public void afterInit(GlobalInitializationEndEvent event){
        VanillaBasedCustomItem replaced = VanillaItemManager.getReplaced(Material.LEATHER_CHESTPLATE);
        replaced.setComponent(
                new CustomItemAttributesComponent(
                        new CustomAttributesContainer.Builder()
                                .add(Attributes.COLD_RESISTANCE, 0.5, AttributeModifier.Operation.MULTIPLY_SCALAR_1, CustomEquipmentSlot.CHEST)
                                .build()
                )
        );
    }
}
