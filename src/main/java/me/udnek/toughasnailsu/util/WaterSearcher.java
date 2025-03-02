package me.udnek.toughasnailsu.util;

import me.udnek.toughasnailsu.item.Items;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class WaterSearcher {
    private static final List<Material> WATER_BLOCK = Arrays.asList(Material.WATER, Material.KELP, Material.SEAGRASS,  Material.TALL_SEAGRASS, Material.BUBBLE_COLUMN);

    public static @NotNull WaterType getWaterType(@NotNull Biome biome){
        String name = biome.getKey().getKey();
        if (name.contains("river") || name.contains("yellowstone")) return WaterType.PURE;
        if (name.contains("sea") || name.contains("ocean") || name.contains("beach") || name.contains("shore")) return WaterType.SEA;
        return WaterType.DIRTY;
    }

    public static @NotNull WaterType getWaterType(@NotNull Location origin){
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

    public static @Nullable ItemStack getBottleType(@NotNull Block block){
        Location location = block.getLocation();

        if (!(WATER_BLOCK.contains(block.getType())) && !(block.getBlockData() instanceof Waterlogged)) return null;
        if (block.getBlockData() instanceof Waterlogged waterlogged){
            if (!waterlogged.isWaterlogged()) return null;
        }

        return switch (getWaterType(location)){
            case PURE -> Items.PURE_WATER_BOTTLE.getItem();
            case DIRTY -> Items.DIRTY_WATER_BOTTLE.getItem();
            case SEA -> Items.SEA_WATER_BOTTLE.getItem();
        };
    }

    public enum WaterType{
        PURE,
        SEA,
        DIRTY
    }
}
