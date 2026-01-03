package me.udnek.toughasnailsu.util;

import me.udnek.coreu.custom.attribute.CustomAttribute;
import me.udnek.coreu.custom.attribute.CustomAttributeModifier;
import me.udnek.coreu.custom.component.CustomComponentType;
import me.udnek.coreu.custom.equipment.slot.CustomEquipmentSlot;
import me.udnek.coreu.custom.event.CustomItemGeneratedEvent;
import me.udnek.coreu.custom.event.InitializationEvent;
import me.udnek.coreu.custom.item.CustomItem;
import me.udnek.coreu.custom.item.VanillaItemManager;
import me.udnek.coreu.custom.recipe.RecipeManager;
import me.udnek.coreu.custom.recipe.choice.CustomSingleRecipeChoice;
import me.udnek.coreu.custom.registry.InitializationProcess;
import me.udnek.coreu.rpgu.component.RPGUComponents;
import me.udnek.coreu.util.SelfRegisteringListener;
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
import me.udnek.toughasnailsu.item.Flask;
import me.udnek.toughasnailsu.item.RecipeRegistration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class EventListener extends SelfRegisteringListener {

    public static final double RESISTANCE_AMOUNT = 0.45;

    public EventListener(JavaPlugin plugin) {super(plugin);}

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerConsume(PlayerItemConsumeEvent event){
        CustomItem.consumeIfCustom(event.getItem(), customItem -> {
            Flask.ActiveAbilityComponent activeAbilityComponent = customItem.getComponents().getOrCreateDefault(RPGUComponents.ACTIVE_ABILITY_ITEM).getComponents().get(ComponentTypes.FLASK_ACTIVE_ABILITY);
            if (activeAbilityComponent == null) return;
            activeAbilityComponent.onConsume(customItem, event);
        });
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
            customItem.getComponents().getOrCreateDefault(CustomComponentType.CUSTOM_ATTRIBUTED_ITEM).addAttribute(
                    Attributes.WATER_RESISTANCE, new CustomAttributeModifier(0.9, AttributeModifier.Operation.MULTIPLY_SCALAR_1, EquipmentSlots.ARTIFACTS));

        }
    }
    @EventHandler
    public void afterInit(InitializationEvent event){
        if (event.getStep() == InitializationProcess.Step.AFTER_GLOBAL_INITIALIZATION) {
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
        if (event.getStep() == InitializationProcess.Step.GLOBAL_INITIALIZATION) {
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
        customItem.getComponents().getOrCreateDefault(CustomComponentType.CUSTOM_ATTRIBUTED_ITEM)
                .addAttribute(attribute, new CustomAttributeModifier(amount, AttributeModifier.Operation.MULTIPLY_SCALAR_1, slot));
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
    public void onRespawn(PlayerRespawnEvent event){
        if (event.getRespawnReason() != PlayerRespawnEvent.RespawnReason.DEATH) return;
        PlayerData playerData = Database.getInstance().get(event.getPlayer());
        playerData.getThirst().set(Thirst.DEFAULT);
        playerData.getTemperature().set(Temperature.DEFAULT);
        new BukkitRunnable() {
            @Override
            public void run() {
                Effects.TEMPERATURE_FORTIFY.apply(event.getPlayer(), 20*60*2, 0);
            }
        }.runTaskLater(ToughAsNailsU.getInstance(), 1);
    }
}
