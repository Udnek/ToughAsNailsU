package me.udnek.toughasnailsu.data;

import me.udnek.itemscoreu.util.ComponentU;
import me.udnek.toughasnailsu.attribute.Attributes;
import me.udnek.toughasnailsu.effect.Effects;
import me.udnek.toughasnailsu.util.RangedValue;
import me.udnek.toughasnailsu.util.Utils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Lightable;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

public class Temperature extends RangedValue {
    public static final double MAX = 100;
    public static final double MIN = -100;
    public static final double DEFAULT = 0;

    public static final double IMPACT_SPEED = 0.005;
    public static final double NATURAL_RESTORE_VALUE = 6;
    public static final double ACCEPTABLE_MAX = 20;
    public static final double ACCEPTABLE_MIN = -8;

    public static final int BLOCK_AROUND_RADIUS_SCANNER = 5;

    public static final EnumMap<Material, Double> AROUND_BLOCK_MAP = new EnumMap<>(Material.class);
    public static final EnumMap<Material, Double> AROUND_LIT_BLOCK_MAP = new EnumMap<>(Material.class);
    public static final EnumMap<Material, Double> UNDER_BLOCK_MAP = new EnumMap<>(Material.class);

    static {
        AROUND_BLOCK_MAP.put(Material.MAGMA_BLOCK, 0.5d);
        AROUND_BLOCK_MAP.put(Material.LAVA, 12d);
        AROUND_BLOCK_MAP.put(Material.FIRE, 5d);
        AROUND_BLOCK_MAP.put(Material.SOUL_FIRE, 10d);
        AROUND_BLOCK_MAP.put(Material.LAVA_CAULDRON, 11d);
        AROUND_BLOCK_MAP.put(Material.SMALL_AMETHYST_BUD, -3d);
        AROUND_BLOCK_MAP.put(Material.MEDIUM_AMETHYST_BUD, -7d);
        AROUND_BLOCK_MAP.put(Material.LARGE_AMETHYST_BUD, -10d);
        AROUND_BLOCK_MAP.put(Material.AMETHYST_CLUSTER, -15d);

        AROUND_LIT_BLOCK_MAP.put(Material.SOUL_CAMPFIRE, 15d);
        AROUND_LIT_BLOCK_MAP.put(Material.CAMPFIRE, 12d);
        AROUND_LIT_BLOCK_MAP.put(Material.FURNACE, 4d);
        AROUND_LIT_BLOCK_MAP.put(Material.SMOKER, 4d);
        AROUND_LIT_BLOCK_MAP.put(Material.BLAST_FURNACE, 4d);

        UNDER_BLOCK_MAP.put(Material.POWDER_SNOW, -6d);
        UNDER_BLOCK_MAP.put(Material.SNOW_BLOCK, -3d);
        UNDER_BLOCK_MAP.put(Material.MAGMA_BLOCK, 3d);
        UNDER_BLOCK_MAP.put(Material.ICE, -3d);
        UNDER_BLOCK_MAP.put(Material.PACKED_ICE, -5d);
        UNDER_BLOCK_MAP.put(Material.BLUE_ICE, -10d);
        UNDER_BLOCK_MAP.put(Material.AMETHYST_BLOCK, -5d);
        UNDER_BLOCK_MAP.put(Material.BUDDING_AMETHYST, -15d);
    }

    final PlayerData data;
    final Hud hud = new Hud();

    double biomeHumidity;
    double biomeTemperature;
    double sun; // [0 ... 1]
    double activity; // [0, 1]
    double wet; // [0, 1]
    double rain; // [0, 1]
    double blockAroundImpact = 0;
    double blockUnderImpact = 0;

    double heatResistanceMultiplier = 0;
    double coldResistanceMultiplier = 0;
    double waterResistanceMultiplier = 0;

    double lastImpact = 0;
    boolean justStartedRising = false;
    boolean justStartedDropping = false;
    boolean stabilizing = false;

    public Temperature(PlayerData data){
        set(DEFAULT);
        this.data = data;
    }
    @Override
    public double getMax() {return MAX;}
    @Override
    public double getMin() {return MIN;}
    public double getNormalized(){
        return (value-MIN)/(MAX-MIN);
    }
    public void tick(){
        updateAll();
        double newImpact = calculateImpact();
        if (!Utils.isSameSign(lastImpact, newImpact)){
            if (newImpact < 0) justStartedDropping = true;
            else justStartedRising = true;
        } else {
            justStartedRising= false;
            justStartedDropping = false;
        }
        add(newImpact);
        lastImpact = newImpact;

        if (!data.player.getGameMode().isInvulnerable()){
            if (isMax() && Bukkit.getCurrentTick() % 20 == 0) {data.player.setFireTicks(20);}
            else if (isMin()) {
                data.player.setFreezeTicks(180);
                if (Bukkit.getCurrentTick() % 20 == 0) data.player.damage(0.5);
            }
        }
    }
    public void updateBiomeData(){
        biomeHumidity = data.location.getBlock().getHumidity();
        biomeTemperature = data.location.getBlock().getTemperature();
    }
    public void updateSun() {
        if (data.location.getWorld().getEnvironment() == World.Environment.NETHER){
            sun = 0.75;
            return;
        }
        Block block = data.location.getBlock();
        byte currentMaxLight = block.getLightFromSky();
        byte surfaceLight = data.location.getWorld().getBlockAt(0, 1000, 0).getLightLevel();
        if (surfaceLight > currentMaxLight) {
            sun = (double) currentMaxLight / 15;
        } else {
            sun =  (double) surfaceLight / 15;
        }
    }
    public void updateAttributes(){
        coldResistanceMultiplier = 1 - Attributes.COLD_RESISTANCE.calculate(data.player);
        heatResistanceMultiplier = 1 - Attributes.HEAT_RESISTANCE.calculate(data.player);
        waterResistanceMultiplier = 1 -Attributes.WATER_RESISTANCE.calculate(data.player);
    }
    public void updateAll(){
        activity = (data.player.isSprinting() || data.player.isSwimming()) ? 1 : 0;
        wet = data.player.isInWater() ? 1 : 0;
        rain = data.player.isInRain() ? 1 : 0;
        blockUnderImpact = UNDER_BLOCK_MAP.getOrDefault(data.location.clone().subtract(0, 0.1, 0).getBlock().getType(), 0d);
        updateBiomeData();
        updateSun();
        if (!data.shouldSkipTick(20*3)) blockAroundImpact = calculateAroundBlocksImpact();
        if (!data.shouldSkipTick(20, 123)) updateAttributes();
    }
    public double calculateImpact(){
        double externalImpact = calculateExternalImpact();

        double impact = externalImpact;
        int colling = Effects.COLLING.getAppliedLevel(data.player) + 1;
        int heating = Effects.HEATING.getAppliedLevel(data.player) + 1;

        data.debugger.addLine("externalImpact", externalImpact );
        data.debugger.addLine("foodColling, foodHeating", colling, heating);
        data.debugger.addLine("coldRes: (mul)", coldResistanceMultiplier);
        data.debugger.addLine("heatRes: (mul)", heatResistanceMultiplier);
        data.debugger.addLine("waterRes: (mul) = " + waterResistanceMultiplier);
        data.debugger.addLine("sun, wet, activity, rain", sun, wet, activity, rain);

        if (data.player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE) && impact > 0 && !isAcceptableImpact(impact)) {
            impact = 0;
        }

        if (!isAcceptableImpact(impact)) {
            if (colling != 0  && impact > 0) impact -= colling;
            if (heating != 0  && impact < 0) impact += heating;
        }

        data.debugger.addLine("impactBeforeRestoreRange", impact);

        if (isAcceptableImpact(impact)){
            stabilizing = true;
            if (Math.abs(value) < NATURAL_RESTORE_VALUE)
                 impact = -value / IMPACT_SPEED;
            else impact = NATURAL_RESTORE_VALUE * -Math.signum(value);
        } else {
            stabilizing = false;
        }

        data.debugger.addLine("finalImpact", impact);

        return impact * IMPACT_SPEED;
    }
    public boolean isAcceptableImpact(double impact){
        return ACCEPTABLE_MIN <= impact && impact <= ACCEPTABLE_MAX;
    }

    public double calculateExternalImpact(){
        double temp =      (biomeTemperature - 0.75) * 23;
        double hum =       (biomeHumidity - 0.5) * 7;
        double sunImpact = (sun - 0.6) * 22;
        double impactSum = 0
                + temp
                + hum
                + sunImpact
                + activity * 8
                + rain * -20
                + blockAroundImpact
                + blockUnderImpact;

        if (impactSum > ACCEPTABLE_MAX) impactSum += wet * -45;
        else                            impactSum += wet * -45 * waterResistanceMultiplier;

        if (impactSum < 0) impactSum *= coldResistanceMultiplier;
        else               impactSum *= heatResistanceMultiplier;

        data.debugger.addLine("temp, hum, sun, sum", temp, hum, sunImpact, impactSum);

        return impactSum;
    }

    public double calculateAroundBlocksImpact(){
        final int radius = BLOCK_AROUND_RADIUS_SCANNER;

        double totalImpact = 0;
        Location loc = data.location.clone();
        for (double i = data.location.x()-radius; i <= data.location.x()+radius; i++) {
            for (double j = data.location.y()-radius; j <= data.location.y()+radius; j++) {
                for (double k = data.location.z()-radius; k < data.location.z()+radius; k++) {
                    loc.set(i, j, k);
                    Material material = loc.getBlock().getType();
                    totalImpact += AROUND_BLOCK_MAP.getOrDefault(material, 0d);
                    if (loc.getBlock().getBlockData() instanceof Lightable lightable){
                        totalImpact += AROUND_LIT_BLOCK_MAP.getOrDefault(material, 0d) * (lightable.isLit() ? 1d : 0d);
                    }
                }
            }
        }
        return totalImpact;
    }

    public class Hud{
        public static final Key FONT = Key.key("toughasnailsu:temperature");
        public static final int FRAMES = 21;
        public static final Vector FREEZE_COLOR = new Vector(20, 78, 112).multiply(1d/255d);
        public static final Vector HEAT_COLOR = new Vector(217, 131, 44).multiply(1d/255d);
        public static final int SIZE = 14;
        public static final int OFFSET = -7;

        int animation = -1;
        Component icon;
        Component fortify;
        TextColor color;
        public @NotNull Component get(){
            if (animation == -1 && (justStartedRising || justStartedDropping)){
                animation = 0;
            }
            if (animation >= FRAMES) animation = -1;

            updateComponents();
            return ComponentU.textWithNoSpace(OFFSET, icon.font(FONT).color(color).append(fortify), SIZE);
        }
        public void updateComponents(){
            if (coldResistanceMultiplier == 0 && heatResistanceMultiplier == 0){
                fortify = ComponentU.textWithNoSpace(-16, Component.translatable("image.toughasnailsu.temperature.fortify").color(ComponentU.NO_SHADOW_COLOR), 16);
            } else fortify = Component.empty();

            if (stabilizing) {
                icon = Component.translatable("image.toughasnailsu.temperature.stabilizing");
                color = smoothColor();
                return;
            }
            else if (isMax()){
                icon = Component.translatable("image.toughasnailsu.temperature.heat");
                color = NamedTextColor.WHITE;
                return;
            }
            else if (isMin()){
                icon = Component.translatable("image.toughasnailsu.temperature.freeze");
                color = NamedTextColor.WHITE;
                return;
            }

            String type = lastImpact > 0 ? "rise" : "drop";
            if (animation == -1) {
                icon = Component.translatable("image.toughasnailsu.temperature." + type);
            } else {
                icon = Component.translatable("animation.toughasnailsu.temperature." + type +'.' + animation++);
            }
            color = smoothColor();
        }
        public @NotNull TextColor smoothColor(){
            double normalized = getNormalized();
            Vector color;
            if (normalized < 0.5){
                color = new Vector(1,1,1).subtract(FREEZE_COLOR.clone()).multiply(1-normalized*2);
            } else {
                color = new Vector(1,1,1).subtract(HEAT_COLOR.clone()).multiply((normalized-0.5)*2);
            }
            color = new Vector(1,1,1).subtract(color);
            return TextColor.color((float) color.getX(), (float) color.getY(), (float) color.getZ());
        }
    }
}
