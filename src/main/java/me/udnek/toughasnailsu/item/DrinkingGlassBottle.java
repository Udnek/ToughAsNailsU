package me.udnek.toughasnailsu.item;

import me.udnek.itemscoreu.customcomponent.instance.RightClickableItem;
import me.udnek.itemscoreu.customitem.ConstructableCustomItem;
import me.udnek.itemscoreu.customitem.CustomItem;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class DrinkingGlassBottle extends ConstructableCustomItem implements ToughAsNailsUCustomItem{
    @Override
    public @NotNull String getRawId() {return "drinking_glass_bottle";}
    @Override
    public @NotNull Material getMaterial() {return Material.GUNPOWDER;}

    @Override
    protected void generateRecipes(@NotNull Consumer<@NotNull Recipe> consumer) {
        ShapedRecipe recipe = new ShapedRecipe(getNewRecipeKey(), getItem());
        recipe.shape(
                "G G",
                "G G",
                " G ");

        RecipeChoice.MaterialChoice glass = new RecipeChoice.MaterialChoice(Material.GLASS);
        recipe.setIngredient('G', glass);

        consumer.accept(recipe);
    }

    @Override
    public void initializeComponents() {
        super.initializeComponents();
        setComponent(new DrinkingGlassBottleComponent());
    }

    public static class DrinkingGlassBottleComponent implements RightClickableItem {
        private static final List<Biome> PURE_WATER_BIOMES = Arrays.asList(Biome.RIVER, Biome.FROZEN_RIVER);
        private static final List<Biome> SEA_WATER_BIOMES = Arrays.asList(Biome.OCEAN, Biome.FROZEN_OCEAN, Biome.DEEP_OCEAN, Biome.WARM_OCEAN, Biome.LUKEWARM_OCEAN, Biome.COLD_OCEAN,
                Biome.DEEP_COLD_OCEAN, Biome.DEEP_LUKEWARM_OCEAN, Biome.DEEP_FROZEN_OCEAN, Biome.BEACH, Biome.SNOWY_BEACH, Biome.STONY_SHORE);
        private static final List<Material> WATER_BLOCK = Arrays.asList(Material.WATER, Material.KELP, Material.SEAGRASS,  Material.TALL_SEAGRASS, Material.BUBBLE_COLUMN);

        @Override
        public void onRightClick(@NotNull CustomItem customItem, @NotNull PlayerInteractEvent event) {
            Player player = event.getPlayer();
            PlayerInventory inventory = player.getInventory();
            RayTraceResult rayTraceResult = player.rayTraceBlocks(player.getAttribute(Attribute.BLOCK_INTERACTION_RANGE).getValue(), FluidCollisionMode.ALWAYS);
            if (rayTraceResult == null) return;
            Block block = rayTraceResult.getHitBlock();
            EquipmentSlot hand = event.getHand();
            if (block == null || hand == null) return;
            Location location = block.getLocation();
            Biome biome = location.getWorld().getBiome(location);
            if (!(WATER_BLOCK.contains(block.getType())) && !(block.getBlockData() instanceof Waterlogged)) return;
            if (block.getBlockData() instanceof Waterlogged waterlogged){
                if (!waterlogged.isWaterlogged()) return;
            }

            ItemStack bottle;
            if (SEA_WATER_BIOMES.contains(biome)) {bottle = Items.SEA_WATER_BOTTLE.getItem();}
            else {bottle = Items.DIRTY_WATER_BOTTLE.getItem();}
            for (Biome pure_water_biome : PURE_WATER_BIOMES){
                if (location.getWorld().locateNearestBiome(location, 5, 2, 2, pure_water_biome) != null) {
                    bottle = Items.PURE_WATER_BOTTLE.getItem();
                    break;
                }
            }

            inventory.setItem(hand,inventory.getItem(hand).add(-1));
            if (inventory.getItem(hand).getType() == Material.AIR){inventory.setItem(hand,bottle);}
            else {inventory.addItem(bottle);}
        }
    }
}
