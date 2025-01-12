package me.udnek.toughasnailsu.item;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.BundleContents;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.udnek.itemscoreu.customcomponent.instance.RightClickableItem;
import me.udnek.itemscoreu.customitem.ConstructableCustomItem;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.util.LoreBuilder;
import me.udnek.rpgu.component.ability.active.ConstructableActiveAbilityComponent;
import me.udnek.rpgu.component.ability.property.AttributeBasedProperty;
import me.udnek.toughasnailsu.ToughAsNailsU;
import me.udnek.toughasnailsu.component.ComponentTypes;
import me.udnek.toughasnailsu.component.InventoryInteractableItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
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
    protected void generateRecipes(@NotNull Consumer<@NotNull Recipe> consumer) {
        ShapedRecipe recipe = new ShapedRecipe(getNewRecipeKey(), getItem().add(2));
        recipe.shape(
                " LS",
                "LBL",
                " L ");

        recipe.setIngredient('L', new RecipeChoice.MaterialChoice(Material.LEATHER));
        recipe.setIngredient('S', new RecipeChoice.MaterialChoice(Material.STRING));
        recipe.setIngredient('B', new RecipeChoice.MaterialChoice(Material.BUCKET));
        consumer.accept(recipe);
    }

    @Override
    public void initializeComponents() {
        super.initializeComponents();

        getComponents().set(new FlaskRightClickable());
        getComponents().set(new FlaskInventoryInteractable());
        getComponents().set(new FlaskActiveAbilityComponent());
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

            openBundle(flask);

            playerInteractEvent.getPlayer().getInventory().setItem(playerInteractEvent.getHand(), flask.withType(DRINKING_MATERIAL));
            playerInteractEvent.setCancelled(true);
        }
    }

    public static class FlaskActiveAbilityComponent extends ConstructableActiveAbilityComponent<PlayerItemConsumeEvent> {
        public FlaskActiveAbilityComponent(){
            getComponents().set(new AttributeBasedProperty(20, me.udnek.rpgu.component.ComponentTypes.ABILITY_COOLDOWN));
        }

        @Override
        public void getLore(@NotNull LoreBuilder loreBuilder) {}

        @Override
        public @NotNull ActionResult action(@NotNull CustomItem customItem, @NotNull Player player, @NotNull PlayerItemConsumeEvent playerItemConsumeEvent) {
            ItemStack flask = playerItemConsumeEvent.getItem();
            flask.unsetData(DataComponentTypes.CONSUMABLE);

            List<ItemStack> contents = new java.util.ArrayList<>(flask.getData(DataComponentTypes.BUNDLE_CONTENTS).contents());
            ItemStack firstItem = contents.getFirst();

            firstItem.add(-1);
            if (firstItem.isEmpty()) {contents.removeFirst();}
            flask.setData(DataComponentTypes.BUNDLE_CONTENTS, BundleContents.bundleContents(contents));

            List<Component> oldLore = flask.getData(DataComponentTypes.LORE).lines();
            flask.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(oldLore.get(oldLore.size() - 1))));

            closeBundle(flask);

            CustomItem.get(firstItem).getComponents().getOrException(ComponentTypes.DRINK_ITEM).onConsumption(customItem,playerItemConsumeEvent);
            player.getInventory().setItem(playerItemConsumeEvent.getHand(), flask.withType(DEFALT_MATERIAL));
            return ActionResult.FULL_COOLDOWN;
        }

        @Override
        public void onConsume(@NotNull CustomItem customItem, @NotNull PlayerItemConsumeEvent event) {
            event.setCancelled(true);
            activate(customItem, event.getPlayer(), event);
        }
    }


    public static class FlaskInventoryInteractable implements InventoryInteractableItem {
        @Override
        public void onBeingClicked(@NotNull CustomItem item, @NotNull InventoryClickEvent event) {
            System.out.println(event.getClickedInventory());
            System.out.println(event.getClick());
            System.out.println(event.getInventory());
            System.out.println(event.getAction());
            System.out.println(event.getCurrentItem());
            System.out.println(event.getCursor());
            System.out.println(event.getHandlers());
        }

        @Override
        public void onClickWith(@NotNull CustomItem item, @NotNull InventoryClickEvent event) {
            System.out.println(event.getClickedInventory());
            System.out.println(event.getClick());
            System.out.println(event.getInventory());
            System.out.println(event.getAction());
            System.out.println(event.getCurrentItem());
            System.out.println(event.getCursor());
            System.out.println(event.getHandlers());
        }
    }

    public static void openBundle(@NotNull ItemStack itemStack) {
        String modelData = itemStack.getData(DataComponentTypes.ITEM_MODEL).value() + "_opened";
        itemStack.setData(DataComponentTypes.ITEM_MODEL, new NamespacedKey(ToughAsNailsU.getInstance(), modelData));
    }

    public static void closeBundle(@NotNull ItemStack itemStack) {
        String modelData = itemStack.getData(DataComponentTypes.ITEM_MODEL).value().replace("_opened", "");
        itemStack.setData(DataComponentTypes.ITEM_MODEL, new NamespacedKey(ToughAsNailsU.getInstance(), modelData));
    }
}
