package me.udnek.toughasnailsu.util;

import me.udnek.itemscoreu.customattribute.CustomAttribute;
import me.udnek.itemscoreu.customattribute.CustomAttributeModifier;
import me.udnek.itemscoreu.customcomponent.instance.CustomItemAttributesComponent;
import me.udnek.itemscoreu.customequipmentslot.CustomEquipmentSlot;
import me.udnek.itemscoreu.customevent.CustomItemGeneratedEvent;
import me.udnek.itemscoreu.customevent.InitializationEvent;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.customitem.VanillaItemManager;
import me.udnek.itemscoreu.customrecipe.RecipeManager;
import me.udnek.itemscoreu.customrecipe.choice.CustomSingleRecipeChoice;
import me.udnek.itemscoreu.customregistry.InitializationProcess;
import me.udnek.itemscoreu.util.SelfRegisteringListener;
import me.udnek.rpgu.equipment.slot.EquipmentSlots;
import me.udnek.rpgu.item.Items;
import me.udnek.rpgu.mechanic.enchanting.EnchantingRecipe;
import me.udnek.rpgu.mechanic.enchanting.upgrade.EnchantingTableUpgrade;
import me.udnek.toughasnailsu.ToughAsNailsU;
import me.udnek.toughasnailsu.attribute.Attributes;
import me.udnek.toughasnailsu.component.ComponentTypes;
import me.udnek.toughasnailsu.component.DrinkItem;
import me.udnek.toughasnailsu.data.Database;
import me.udnek.toughasnailsu.data.PlayerData;
import me.udnek.toughasnailsu.data.Temperature;
import me.udnek.toughasnailsu.data.Thirst;
import me.udnek.toughasnailsu.effect.Effects;
import me.udnek.toughasnailsu.enchantment.Enchantments;
import me.udnek.toughasnailsu.item.RecipeRegistration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class EventsListener extends SelfRegisteringListener {

    public static final double RESISTANCE_AMOUNT = 0.17;

    public EventsListener(JavaPlugin plugin) {super(plugin);}

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerConsume(PlayerItemConsumeEvent event){
        CustomItem customItem = CustomItem.get(event.getItem());
        if (customItem == null) return;
        customItem.getComponents().getOrDefault(ComponentTypes.DRINK_ITEM).onConsumption(customItem, event);
    }

    @EventHandler
    public void onGenerate(CustomItemGeneratedEvent event){
        CustomItem customItem = event.getCustomItem();
        DrinkItem drinkingComponent = customItem.getComponents().get(ComponentTypes.DRINK_ITEM);
        if (drinkingComponent != null) drinkingComponent.modifyItem(event);

        if (customItem == Items.WOLF_HELMET) armorAttributes(customItem, Attributes.COLD_RESISTANCE, CustomEquipmentSlot.HEAD, RESISTANCE_AMOUNT);
        if (customItem == Items.WOLF_CHESTPLATE) armorAttributes(customItem, Attributes.COLD_RESISTANCE, CustomEquipmentSlot.CHEST, RESISTANCE_AMOUNT);
        if (customItem == Items.WOLF_LEGGINGS) armorAttributes(customItem, Attributes.COLD_RESISTANCE, CustomEquipmentSlot.LEGS, RESISTANCE_AMOUNT);
        if (customItem == Items.WOLF_BOOTS) armorAttributes(customItem, Attributes.COLD_RESISTANCE, CustomEquipmentSlot.FEET, RESISTANCE_AMOUNT);
        if (customItem == Items.FISHERMAN_SNORKEL){
            CustomItemAttributesComponent.safeAddAttribute(customItem,
                    Attributes.WATER_RESISTANCE, new CustomAttributeModifier(0.8, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlots.ARTIFACTS));

        }
    }
    @EventHandler
    public void afterInit(InitializationEvent event){
        if (event.getStep() == InitializationProcess.Step.AFTER_REGISTRIES_INITIALIZATION) {
            RecipeRegistration.run();

            RecipeManager.getInstance().register(
                    new EnchantingRecipe(
                            new NamespacedKey(ToughAsNailsU.getInstance(), "nail"),
                            Enchantments.NAIL.getBukkit(),
                            List.of(new CustomSingleRecipeChoice(Material.MAGMA_CREAM),
                                    new CustomSingleRecipeChoice(Material.SNOWBALL),
                                    new CustomSingleRecipeChoice(Material.AMETHYST_SHARD),
                                    new CustomSingleRecipeChoice(Material.FIRE_CHARGE)),
                            Set.of(EnchantingTableUpgrade.DECENT_BOOKSHELF, EnchantingTableUpgrade.AMETHYST))
            );
        }
        if (event.getStep() == InitializationProcess.Step.BEFORE_VANILLA_MANAGER) {

            armorAttributes(Material.LEATHER_HELMET, Attributes.COLD_RESISTANCE, RESISTANCE_AMOUNT);
            armorAttributes(Material.LEATHER_CHESTPLATE, Attributes.COLD_RESISTANCE, RESISTANCE_AMOUNT);
            armorAttributes(Material.LEATHER_LEGGINGS, Attributes.COLD_RESISTANCE, RESISTANCE_AMOUNT);
            armorAttributes(Material.LEATHER_BOOTS, Attributes.COLD_RESISTANCE, RESISTANCE_AMOUNT);
            armorAttributes(Material.CHAINMAIL_HELMET, Attributes.HEAT_RESISTANCE, RESISTANCE_AMOUNT);
            armorAttributes(Material.CHAINMAIL_CHESTPLATE, Attributes.HEAT_RESISTANCE, RESISTANCE_AMOUNT);
            armorAttributes(Material.CHAINMAIL_LEGGINGS, Attributes.HEAT_RESISTANCE, RESISTANCE_AMOUNT);
            armorAttributes(Material.CHAINMAIL_BOOTS, Attributes.HEAT_RESISTANCE, RESISTANCE_AMOUNT);
        }
    }

    private static void armorAttributes(@NotNull CustomItem customItem, @NotNull CustomAttribute attribute, @NotNull CustomEquipmentSlot slot, double amount) {
        CustomItemAttributesComponent.safeAddAttribute(customItem, attribute, new CustomAttributeModifier(amount, AttributeModifier.Operation.ADD_NUMBER, slot));
    }

    private static void armorAttributes(@NotNull Material material, @NotNull CustomAttribute attribute, double amount) {
        armorAttributes(VanillaItemManager.getReplaced(material), attribute, CustomEquipmentSlot.getFromVanilla(material.getEquipmentSlot()), amount);
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
    public void resetStatsOnDeath(PlayerRespawnEvent event){
        PlayerData playerData = Database.getInstance().get(event.getPlayer());
        playerData.getThirst().set(Thirst.DEFAULT);
        playerData.getTemperature().set(Temperature.DEFAULT);
    }
}
