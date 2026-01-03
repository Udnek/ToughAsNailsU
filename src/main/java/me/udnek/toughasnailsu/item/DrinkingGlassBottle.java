package me.udnek.toughasnailsu.item;

import me.udnek.coreu.custom.component.instance.DispensableItem;
import me.udnek.coreu.custom.component.instance.RightClickableItem;
import me.udnek.coreu.custom.item.ConstructableCustomItem;
import me.udnek.coreu.custom.item.CustomItem;
import me.udnek.toughasnailsu.util.WaterSearcher;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class DrinkingGlassBottle extends ConstructableCustomItem implements ToughAsNailsUCustomItem{
    @Override
    public @NotNull String getRawId() {return "drinking_glass_bottle";}

    @Override
    protected void generateRecipes(@NotNull Consumer<Recipe> consumer) {
        ShapedRecipe recipe = new ShapedRecipe(getNewRecipeKey(), getItem().add(2));
        recipe.shape(
                "G G",
                "G G",
                " G ");

        recipe.setIngredient('G', new RecipeChoice.MaterialChoice(Material.GLASS));
        consumer.accept(recipe);
    }

    @Override
    public void initializeComponents() {
        super.initializeComponents();
        getComponents().set(new DrinkingGlassBottleRightClickableItem());
        getComponents().set(new DrinkingGlassBottleDispensableItem());
    }


    public static class DrinkingGlassBottleRightClickableItem implements RightClickableItem {
        @Override
        public void onRightClick(@NotNull CustomItem customItem, @NotNull PlayerInteractEvent event) {
            Player player = event.getPlayer();
            PlayerInventory inventory = player.getInventory();
            RayTraceResult rayTraceResult = player.rayTraceBlocks(player.getAttribute(Attribute.BLOCK_INTERACTION_RANGE).getValue(), FluidCollisionMode.ALWAYS);
            if (rayTraceResult == null) return;
            Block block = rayTraceResult.getHitBlock();
            EquipmentSlot hand = event.getHand();
            if (block == null || hand == null) return;

            ItemStack bottle = WaterSearcher.getBottleType(block);
            if (bottle == null) return;

            inventory.setItem(hand,inventory.getItem(hand).add(-1));
            if (inventory.getItem(hand).getType() == Material.AIR){inventory.setItem(hand,bottle);}
            else {inventory.addItem(bottle);}
        }
    }

    public static class DrinkingGlassBottleDispensableItem implements DispensableItem {

        @Override
        public void onDispense(@NotNull CustomItem customItem, @NotNull BlockDispenseEvent event) {
            Block block = event.getBlock();
            Block relative = block.getRelative(((Dispenser) block.getBlockData()).getFacing());
            ItemStack bottleType = WaterSearcher.getBottleType(relative);
            if (bottleType == null) return;
            Inventory inventory = ((org.bukkit.block.Dispenser) block.getState()).getInventory();
            if (inventory.addItem(bottleType).isEmpty()) {
                event.setCancelled(true);
                return;
            }
            event.setItem(bottleType);
        }

        @Override
        public void onDrop(@NotNull CustomItem customItem, @NotNull BlockDispenseEvent blockDispenseEvent) {}
    }
}
