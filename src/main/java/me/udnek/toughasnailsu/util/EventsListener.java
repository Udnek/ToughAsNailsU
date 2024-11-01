package me.udnek.toughasnailsu.util;

import me.udnek.itemscoreu.customattribute.CustomAttributesContainer;
import me.udnek.itemscoreu.customcomponent.instance.CustomItemAttributesComponent;
import me.udnek.itemscoreu.customequipmentslot.CustomEquipmentSlot;
import me.udnek.itemscoreu.customevent.CustomItemGeneratedEvent;
import me.udnek.itemscoreu.customevent.InitializationEvent;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customitem.VanillaBasedCustomItem;
import me.udnek.itemscoreu.util.InitializationProcess;
import me.udnek.itemscoreu.util.SelfRegisteringListener;
import me.udnek.itemscoreu.util.VanillaItemManager;
import me.udnek.toughasnailsu.attribute.Attributes;
import me.udnek.toughasnailsu.component.ComponentTypes;
import me.udnek.toughasnailsu.component.DrinkItemComponent;
import me.udnek.toughasnailsu.item.RecipeRegistration;
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
    @EventHandler(priority = EventPriority.MONITOR)
    public void onGenerate(CustomItemGeneratedEvent event){
        CustomItem customItem = event.getCustomItem();
        DrinkItemComponent component = customItem.getComponent(ComponentTypes.DRINK_ITEM);
        if (component != null) component.modifyItem(event);
    }
    @EventHandler
    public void afterInit(InitializationEvent event){
        if (event.getStep() == InitializationProcess.Step.AFTER_REGISTRIES_INITIALIZATION) RecipeRegistration.run();
        if (event.getStep() == InitializationProcess.Step.BEFORE_VANILLA_MANAGER) {
            VanillaBasedCustomItem replacedLeatherChestplate = VanillaItemManager.getReplaced(Material.LEATHER_CHESTPLATE);
            replacedLeatherChestplate.setComponent(
                    new CustomItemAttributesComponent(
                            new CustomAttributesContainer.Builder()
                                    .add(Attributes.COLD_RESISTANCE, 0.1, AttributeModifier.Operation.ADD_NUMBER, CustomEquipmentSlot.CHEST)
                                    .build()
                    )
            );

            VanillaBasedCustomItem replacedLeatherHelmet = VanillaItemManager.getReplaced(Material.LEATHER_HELMET);
            replacedLeatherHelmet.setComponent(
                    new CustomItemAttributesComponent(
                            new CustomAttributesContainer.Builder()
                                    .add(Attributes.COLD_RESISTANCE, 0.1, AttributeModifier.Operation.ADD_NUMBER, CustomEquipmentSlot.HEAD)
                                    .build()
                    )
            );

            VanillaBasedCustomItem replacedLeatherLeggings = VanillaItemManager.getReplaced(Material.LEATHER_LEGGINGS);
            replacedLeatherLeggings.setComponent(
                    new CustomItemAttributesComponent(
                            new CustomAttributesContainer.Builder()
                                    .add(Attributes.COLD_RESISTANCE, 0.1, AttributeModifier.Operation.ADD_NUMBER, CustomEquipmentSlot.LEGS)
                                    .build()
                    )
            );

            VanillaBasedCustomItem replacedLeatherBoots = VanillaItemManager.getReplaced(Material.LEATHER_BOOTS);
            replacedLeatherBoots.setComponent(
                    new CustomItemAttributesComponent(
                            new CustomAttributesContainer.Builder()
                                    .add(Attributes.COLD_RESISTANCE, 0.1, AttributeModifier.Operation.ADD_NUMBER, CustomEquipmentSlot.FEET)
                                    .build()
                    )
            );

            VanillaBasedCustomItem replacedChainmailChestplate = VanillaItemManager.getReplaced(Material.CHAINMAIL_CHESTPLATE);
            replacedChainmailChestplate.setComponent(
                    new CustomItemAttributesComponent(
                            new CustomAttributesContainer.Builder()
                                    .add(Attributes.HEAT_RESISTANCE, 0.5, AttributeModifier.Operation.ADD_SCALAR, CustomEquipmentSlot.CHEST)
                                    .build()
                    )
            );

            VanillaBasedCustomItem replacedChainmailHelmet = VanillaItemManager.getReplaced(Material.CHAINMAIL_HELMET);
            replacedChainmailHelmet.setComponent(
                    new CustomItemAttributesComponent(
                            new CustomAttributesContainer.Builder()
                                    .add(Attributes.HEAT_RESISTANCE, 0.5, AttributeModifier.Operation.ADD_SCALAR, CustomEquipmentSlot.HEAD)
                                    .build()
                    )
            );

            VanillaBasedCustomItem replacedChainmailLeggings = VanillaItemManager.getReplaced(Material.CHAINMAIL_LEGGINGS);
            replacedChainmailLeggings.setComponent(
                    new CustomItemAttributesComponent(
                            new CustomAttributesContainer.Builder()
                                    .add(Attributes.HEAT_RESISTANCE, 0.5, AttributeModifier.Operation.ADD_SCALAR, CustomEquipmentSlot.LEGS)
                                    .build()
                    )
            );

            VanillaBasedCustomItem replacedChainmailBoots = VanillaItemManager.getReplaced(Material.CHAINMAIL_BOOTS);
            replacedChainmailBoots.setComponent(
                    new CustomItemAttributesComponent(
                            new CustomAttributesContainer.Builder()
                                    .add(Attributes.HEAT_RESISTANCE, 0.5, AttributeModifier.Operation.ADD_SCALAR, CustomEquipmentSlot.FEET)
                                    .build()
                    )
            );
        }
    }
}
