package me.udnek.toughasnailsu.item;

import me.udnek.itemscoreu.customcomponent.instance.RightClickableItem;
import me.udnek.itemscoreu.customitem.ConstructableCustomItem;
import me.udnek.itemscoreu.customitem.CustomItem;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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
        getComponents().set(new DrinkingGlassBottleComponent());
    }


    public static class DrinkingGlassBottleComponent implements RightClickableItem {
        private static final List<Material> WATER_BLOCK = Arrays.asList(Material.WATER, Material.KELP, Material.SEAGRASS,  Material.TALL_SEAGRASS, Material.BUBBLE_COLUMN);

        public @NotNull WaterType getWaterType(@NotNull Biome biome){
            String name = biome.getKey().getKey();
            if (name.contains("river") || name.contains("yellowstone")) return WaterType.PURE;
            if (name.contains("sea") || name.contains("ocean") || name.contains("beach") || name.contains("shore")) return WaterType.SEA;
            return WaterType.DIRTY;
        }

        public @NotNull WaterType getWaterType(@NotNull Location origin){
            final int range = 5;
            final int step = 2;

            Location tempLoc = origin.clone();
            World world = origin.getWorld();
            if (getWaterType(world.getBiome(origin)) == WaterType.PURE) return WaterType.PURE;
            for (double x = origin.x()-range; x <= origin.x()+range; x+=step) {
                for (double z = origin.z()-range; z <= origin.z()+range; z+=step) {
                    tempLoc.set(x, tempLoc.y(), z);
                    Biome biome = world.getBiome(tempLoc);
                    if (getWaterType(biome) == WaterType.PURE) return WaterType.PURE;
                }
            }
            return getWaterType(world.getBiome(origin));
        }


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

            if (!(WATER_BLOCK.contains(block.getType())) && !(block.getBlockData() instanceof Waterlogged)) return;
            if (block.getBlockData() instanceof Waterlogged waterlogged){
                if (!waterlogged.isWaterlogged()) return;
            }

            ItemStack bottle = switch (getWaterType(location)){
                case PURE -> Items.PURE_WATER_BOTTLE.getItem();
                case DIRTY -> Items.DIRTY_WATER_BOTTLE.getItem();
                case SEA -> Items.SEA_WATER_BOTTLE.getItem();
            };

            inventory.setItem(hand,inventory.getItem(hand).add(-1));
            if (inventory.getItem(hand).getType() == Material.AIR){inventory.setItem(hand,bottle);}
            else {inventory.addItem(bottle);}
        }
    }

    public enum WaterType{
        PURE,
        SEA,
        DIRTY
    }
}
