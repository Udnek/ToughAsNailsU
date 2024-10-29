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
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;
import org.bukkit.util.RayTraceResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class DrinkingGlassBottle extends ConstructableCustomItem implements ToughAsNailsUCustomItem{
    @Override
    public @NotNull String getRawId() {return "drinking_glass_bottle";}
    @Override
    public @NotNull Material getMaterial() {return Material.GUNPOWDER;}
    @Override
    public @Nullable Integer getCustomModelData() {return 3000;}

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
    public void afterInitialization() {
        super.afterInitialization();
        setComponent(new DrinkingGlassBottleComponent());
    }

    public static  class DrinkingGlassBottleComponent extends RightClickableItem {
        private static final List<Biome> PURE_WATER_BIOMES = Arrays.asList(Biome.RIVER, Biome.FROZEN_RIVER);
        private static final List<Biome> SEA_WATER_BIOMES = Arrays.asList(Biome.OCEAN, Biome.FROZEN_OCEAN, Biome.DEEP_OCEAN, Biome.WARM_OCEAN, Biome.LUKEWARM_OCEAN, Biome.COLD_OCEAN,
                Biome.DEEP_COLD_OCEAN, Biome.DEEP_LUKEWARM_OCEAN, Biome.DEEP_FROZEN_OCEAN);
        private static final List<Material> WATER_BLOCK = Arrays.asList(Material.WATER, Material.KELP, Material.SEAGRASS,  Material.TALL_SEAGRASS, Material.BUBBLE_COLUMN);
        @Override
        public void onRightClick(@NotNull CustomItem customItem, @NotNull PlayerInteractEvent event) {
            Player player = event.getPlayer();
            PlayerInventory inventory = player.getInventory();
            RayTraceResult rayTraceResult = player.rayTraceBlocks(player.getAttribute(Attribute.PLAYER_BLOCK_INTERACTION_RANGE).getValue(), FluidCollisionMode.ALWAYS);
            if (rayTraceResult == null){return;}
            Block block = rayTraceResult.getHitBlock();
            if (block == null){return;}
            Location location = block.getLocation();
            Biome biome = location.getWorld().getBiome(location);
            if (!(block.getBlockData() instanceof Waterlogged) && !(WATER_BLOCK.contains(block.getType()))){return;}
            ItemStack bottle;


            if (PURE_WATER_BIOMES.contains(biome)) {bottle = Items.PURE_WATER_BOTTLE.getItem();}
            else if (SEA_WATER_BIOMES.contains(biome)) {bottle = Items.SEA_WATER_BOTTLE.getItem();}
            else {bottle = Items.DIRTY_WATER_BOTTLE.getItem();}

            inventory.removeItem(Items.DRINKING_GLASS_BOTTLE.getItem());
            if (!(event.getHand().isHand())){inventory.setItem(event.getHand(),bottle);}
            else {inventory.addItem(bottle);}

        }
    }
}
