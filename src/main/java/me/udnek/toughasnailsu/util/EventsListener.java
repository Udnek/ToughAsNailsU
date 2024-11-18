package me.udnek.toughasnailsu.util;

import me.udnek.itemscoreu.customattribute.CustomAttribute;
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
import me.udnek.toughasnailsu.data.Database;
import me.udnek.toughasnailsu.data.Thirst;
import me.udnek.toughasnailsu.effect.Effects;
import me.udnek.toughasnailsu.item.RecipeRegistration;
import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

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
            double amount = 0.15;
            armorAttributes(Material.LEATHER_HELMET, Attributes.COLD_RESISTANCE, amount);
            armorAttributes(Material.LEATHER_CHESTPLATE, Attributes.COLD_RESISTANCE, amount);
            armorAttributes(Material.LEATHER_LEGGINGS, Attributes.COLD_RESISTANCE, amount);
            armorAttributes(Material.LEATHER_BOOTS, Attributes.COLD_RESISTANCE, amount);
            armorAttributes(Material.CHAINMAIL_HELMET, Attributes.HEAT_RESISTANCE, amount);
            armorAttributes(Material.CHAINMAIL_CHESTPLATE, Attributes.HEAT_RESISTANCE, amount);
            armorAttributes(Material.CHAINMAIL_LEGGINGS, Attributes.HEAT_RESISTANCE, amount);
            armorAttributes(Material.CHAINMAIL_BOOTS, Attributes.HEAT_RESISTANCE, amount);
        }
    }

    private static void armorAttributes(@NotNull Material material, @NotNull CustomAttribute attribute, double amount) {
        VanillaBasedCustomItem replacedLeatherChestplate = VanillaItemManager.getReplaced(material);
        replacedLeatherChestplate.setComponent(
                new CustomItemAttributesComponent(
                        new CustomAttributesContainer.Builder()
                                .add(attribute, amount, AttributeModifier.Operation.ADD_NUMBER, CustomEquipmentSlot.getFromVanilla(material.getEquipmentSlot()))
                                .build()
                )
        );
    }

    @EventHandler
    public void thirstChanges(FoodLevelChangeEvent event){
        Player player = (Player) event.getEntity();
        int differenceFood = player.getFoodLevel() - event.getFoodLevel();
        if (differenceFood < 0) return;
        Thirst thirst = Database.getInstance().get(player).getThirst();
        double thirstValue = thirst.getValue();

        if (thirst.isThirsty()) thirst.set((thirstValue - differenceFood) * (Effects.THIRST.getAppliedLevel(player) + 1));
        else thirst.set(thirstValue - differenceFood / 2d);
    }

    @EventHandler
    public void resetThirstAtDeath(PlayerRespawnEvent event){
        Database.getInstance().get(event.getPlayer()).getThirst().set(20);
    }
}
