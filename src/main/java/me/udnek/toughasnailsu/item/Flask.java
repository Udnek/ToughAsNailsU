package me.udnek.toughasnailsu.item;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.BundleContents;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.udnek.coreu.custom.component.CustomComponent;
import me.udnek.coreu.custom.component.CustomComponentType;
import me.udnek.coreu.custom.component.instance.InventoryInteractableItem;
import me.udnek.coreu.custom.component.instance.RightClickableItem;
import me.udnek.coreu.custom.equipment.universal.BaseUniversalSlot;
import me.udnek.coreu.custom.equipment.universal.UniversalInventorySlot;
import me.udnek.coreu.custom.item.ConstructableCustomItem;
import me.udnek.coreu.custom.item.CustomItem;
import me.udnek.coreu.custom.item.ItemUtils;
import me.udnek.coreu.nms.Nms;
import me.udnek.coreu.rpgu.component.RPGUActiveItem;
import me.udnek.coreu.rpgu.component.RPGUComponents;
import me.udnek.coreu.rpgu.component.ability.active.RPGUConstructableActiveAbility;
import me.udnek.coreu.rpgu.component.ability.property.AttributeBasedProperty;
import me.udnek.coreu.util.LoreBuilder;
import me.udnek.toughasnailsu.component.ComponentTypes;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static me.udnek.toughasnailsu.util.Utils.resetStyle;

public class Flask extends ConstructableCustomItem {
    public static final Material DRINKING_MATERIAL = Material.GUNPOWDER;
    public static final Material DEFALT_MATERIAL = Material.BUNDLE;

    @Override
    public @NotNull String getRawId() {return "flask";}

    @Override
    public @NotNull Material getMaterial() {return DEFALT_MATERIAL;}

    @Override
    public boolean isUpdateMaterial() {return false;}
    @Override
    public void getComponentsToUpdate(@NotNull ComponentConsumer consumer) {
        consumer.accept(DataComponentTypes.ITEM_MODEL);
    }

    @Override
    protected void generateRecipes(@NotNull Consumer<Recipe> consumer) {
        ShapedRecipe recipe = new ShapedRecipe(getNewRecipeKey(), getItem());
        recipe.shape(
                "SFP",
                "FBF",
                " F ");

        recipe.setIngredient('P', new RecipeChoice.MaterialChoice(Tag.WOODEN_BUTTONS));
        recipe.setIngredient('F', new RecipeChoice.ExactChoice(me.udnek.rpgu.item.Items.FABRIC.getItem()));
        recipe.setIngredient('S', new RecipeChoice.MaterialChoice(Material.STRING));
        recipe.setIngredient('B', new RecipeChoice.MaterialChoice(Material.BUCKET));
        consumer.accept(recipe);
    }

    @Override
    public @Nullable DataSupplier<Integer> getMaxStackSize() {
        return DataSupplier.of(1);
    }

    @Override
    public void initializeComponents() {
        super.initializeComponents();

        getComponents().set(new FlaskRightClickable());
        getComponents().set(new FlaskInventoryInteractable());
        getComponents().getOrCreateDefault(RPGUComponents.ACTIVE_ABILITY_ITEM).getComponents().set(new ActiveAbilityComponent());
    }

    public static class FlaskRightClickable implements RightClickableItem {
        @Override
        public void onRightClick(@NotNull CustomItem customItem, @NotNull PlayerInteractEvent playerInteractEvent) {
            ItemStack flask = playerInteractEvent.getItem();
            BundleContents bundleData = flask.getData(DataComponentTypes.BUNDLE_CONTENTS);
            if (flask.getType() == DRINKING_MATERIAL || bundleData.contents().isEmpty() || playerInteractEvent.getPlayer().hasCooldown(flask)) return;

            flask.setData(DataComponentTypes.CONSUMABLE, Material.POTION.getDefaultData(DataComponentTypes.CONSUMABLE));

            ItemStack firstItem = bundleData.contents().getFirst();
            List<Component> oldLore = flask.getData(DataComponentTypes.LORE).lines();
            List<Component> drinkLore = CustomItem.get(firstItem).getComponents().getOrException(ComponentTypes.DRINK_ITEM).generateLore();
            Component itemName = firstItem.getData(DataComponentTypes.ITEM_NAME);

            List<Component> loreList = new ArrayList<>();
            loreList.add(Component.empty());
            loreList.add(resetStyle(itemName));
            loreList.addAll(drinkLore);
            loreList.addAll(oldLore);
            flask.setData(DataComponentTypes.LORE, ItemLore.lore(loreList));

            //openBundle(flask);

            playerInteractEvent.getPlayer().getInventory().setItem(playerInteractEvent.getHand(), flask.withType(DRINKING_MATERIAL));
            playerInteractEvent.setCancelled(true);
        }
    }

    public static class ActiveAbilityComponent extends RPGUConstructableActiveAbility<PlayerItemConsumeEvent> {

        public static final ActiveAbilityComponent DEFAULT = new ActiveAbilityComponent();

        public ActiveAbilityComponent(){
            getComponents().set(new AttributeBasedProperty(20, RPGUComponents.ABILITY_COOLDOWN_TIME));
        }

        @Override
        public @NotNull ActionResult action(@NotNull CustomItem customItem, @NotNull LivingEntity livingEntity, @NotNull UniversalInventorySlot universalInventorySlot, @NotNull PlayerItemConsumeEvent event) {
            Player player = event.getPlayer();

            ItemStack flask = event.getItem();
            flask.unsetData(DataComponentTypes.CONSUMABLE);

            List<ItemStack> contents = new ArrayList<>(flask.getData(DataComponentTypes.BUNDLE_CONTENTS).contents());

            ItemStack firstItem = contents.getFirst();
            CustomItem.get(firstItem).getComponents().getOrException(ComponentTypes.DRINK_ITEM).onConsumption(customItem, event);
            firstItem.add(-1);
            if (firstItem.isEmpty()) {contents.removeFirst();}

            flask.setData(DataComponentTypes.BUNDLE_CONTENTS, BundleContents.bundleContents(contents));

            flask.setData(DataComponentTypes.LORE, Objects.requireNonNull(Items.FLASK.getItem().getData(DataComponentTypes.LORE)));

            //closeBundle(flask);

            player.getInventory().setItem(event.getHand(), flask.withType(DEFALT_MATERIAL));
            return ActionResult.FULL_COOLDOWN;
        }

        @Override
        public @Nullable Pair<List<String>, List<String>> getEngAndRuDescription() {
            return null;//TODO добавить описане
        }

        @Override
        public void getLore(@NotNull LoreBuilder loreBuilder) {}

        public void onConsume(@NotNull CustomItem customItem, @NotNull PlayerItemConsumeEvent event) {
            event.setCancelled(true);
            activate(customItem, event.getPlayer(), new BaseUniversalSlot(event.getHand()), event);
        }

        @Override
        public @NotNull CustomComponentType<? super RPGUActiveItem, ? extends CustomComponent<? super RPGUActiveItem>> getType() {
            return ComponentTypes.FLASK_ACTIVE_ABILITY;
        }
    }


    public static class FlaskInventoryInteractable implements InventoryInteractableItem {
        @Override
        public void onBeingClicked(@NotNull CustomItem item, @NotNull InventoryClickEvent event) {
            ClickType click = event.getClick();
            Player player = (Player) event.getWhoClicked();
            ItemStack flask = event.getCurrentItem();
            switch (click) {
                case LEFT -> {
                    if (event.getAction() == InventoryAction.PICKUP_ALL) return;
                    ItemStack cursorItem = event.getCursor();
                    CustomItem drinkItem = CustomItem.get(cursorItem);

                    if (drinkItem == null || !(drinkItem.getComponents().has(ComponentTypes.DRINK_ITEM)) ) {
                        event.setCancelled(true);
                        return;
                    }

                    BundleContents bundleFlask = flask.getData(DataComponentTypes.BUNDLE_CONTENTS);
                    addItemToBundle(bundleFlask, cursorItem, player);
                }
                case RIGHT -> {
                    event.setCancelled(true);
                    Material flaskMaterial = flask.getType();
                    if (flaskMaterial == DEFALT_MATERIAL) {return;}
                    if (flaskMaterial == DRINKING_MATERIAL) {
                        flask.unsetData(DataComponentTypes.CONSUMABLE);
                        flask.setData(DataComponentTypes.LORE, Objects.requireNonNull(Items.FLASK.getItem().getData(DataComponentTypes.LORE)));

                        //closeBundle(flask);

                        player.getInventory().setItem(event.getSlot(), flask.withType(DEFALT_MATERIAL));
                    }
                }
            }
        }

        @Override
        public void onClickWith(@NotNull CustomItem item, @NotNull InventoryClickEvent event) {
            ClickType click = event.getClick();
            InventoryAction action = event.getAction();
            if (action == InventoryAction.PLACE_ONE && click == ClickType.RIGHT) {event.setCancelled(true); return;}
            if (click != ClickType.LEFT) {return;}
            ItemStack currentItem = event.getCurrentItem();
            CustomItem drinkItem = CustomItem.get(currentItem);
            if (currentItem == null || currentItem.isEmpty()) return;
            if (drinkItem == null || !(drinkItem.getComponents().has(ComponentTypes.DRINK_ITEM))) {
                event.setCancelled(true);
                return;
            }

            BundleContents bundleFlask = event.getCursor().getData(DataComponentTypes.BUNDLE_CONTENTS);
            addItemToBundle(bundleFlask, currentItem, (Player) event.getWhoClicked());
        }
    }

    public static void addItemToBundle(@NotNull BundleContents flask, @NotNull ItemStack drink, @NotNull Player player) {
        ItemStack useRemainder = drink.getData(DataComponentTypes.USE_REMAINDER).transformInto();
        useRemainder.setAmount(Math.min(Nms.get().getMaxAmountCanFitInBundle(flask, drink), drink.getAmount()) * useRemainder.getAmount());
        ItemUtils.giveAndDropLeftover(player, useRemainder);
    }
}
