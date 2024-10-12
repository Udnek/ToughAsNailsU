package me.udnek.toughasnailsu.data;

import me.udnek.itemscoreu.utils.ComponentU;
import me.udnek.toughasnailsu.attribute.Attributes;
import me.udnek.toughasnailsu.component.DrinkItemComponent;
import me.udnek.toughasnailsu.util.RangedValue;
import me.udnek.toughasnailsu.util.Utils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.EnumMap;

public class Temperature extends RangedValue {
    public static final double MAX = 100;
    public static final double MIN = -100;
    public static final double DEFAULT = 0;

    public static final double EXTERNAL_IMPACT_MULTIPLIER = 5;
    public static final double IMPACT_SPEED = 0.1;
    public static final double ADAPTATION_MULTIPLIER = 0.5;
    public static final double NATURAL_RESTORE_VALUE = 5;
    public static final double NATURAL_RESTORE_RANGE = 10;

    public static final int BLOCK_AROUND_RADIUS_SCANNER = 5;

    public static final double FOOD_MULTIPLIER = 40;
    public static final double INTERNAL_IMPACT_MULTIPLIER = 1;

    static final EnumMap<Material, Double> AROUND_BLOCK_MAP = new EnumMap<>(Material.class);
    static final EnumMap<Material, Double> UNDER_BLOCK_MAP = new EnumMap<>(Material.class);

    static {
        AROUND_BLOCK_MAP.put(Material.LAVA, 15d);
        AROUND_BLOCK_MAP.put(Material.CAMPFIRE, 15d);

        UNDER_BLOCK_MAP.put(Material.ICE, -1d);
        UNDER_BLOCK_MAP.put(Material.PACKED_ICE, -2d);
        UNDER_BLOCK_MAP.put(Material.BLUE_ICE, -5d);
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

    double heatResistance = 1;
    double coldResistance = 1;

    double foodImpact = 0;
    int foodDuration = 0;

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

        if (foodDuration == 0) return;
        foodDuration -= DataTicker.DELAY;
        if (foodDuration <= 0){
            foodDuration = 0;
            foodImpact = 0;
        }
    }
    public void updateBiomeData(){
        biomeHumidity = data.location.getBlock().getHumidity();
        biomeTemperature = data.location.getBlock().getTemperature();
    }
    public void updateSun(){
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
        coldResistance = Attributes.COLD_RESISTANCE.calculate(data.player);
        heatResistance = Attributes.HEAT_RESISTANCE.calculate(data.player);
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
        double internalImpact = calculateInternalImpact();

        double impact = externalImpact + internalImpact;

        data.debugger.addLine("externalImpact", externalImpact + " (" + externalImpact/EXTERNAL_IMPACT_MULTIPLIER + ")");
        data.debugger.addLine("internalImpact", internalImpact + " (" + internalImpact/INTERNAL_IMPACT_MULTIPLIER + ")");
        data.debugger.addLine("impact", impact);

        if (impact < 0) impact += NATURAL_RESTORE_VALUE;
        else            impact -= NATURAL_RESTORE_VALUE;

        if (data.player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE) && impact > 0){
            return 0;
        }

        if (Utils.isSameSign(impact, value)){
            impact *= ADAPTATION_MULTIPLIER;
        }

        if (Math.abs(impact) <= NATURAL_RESTORE_RANGE){
            stabilizing = true;
            if (Math.abs(value) < NATURAL_RESTORE_VALUE) impact = -value / IMPACT_SPEED;
            else impact = NATURAL_RESTORE_VALUE * -Math.signum(impact);
        } else {
            stabilizing = false;
        }

        data.debugger.addLine("finalImpact", impact);

        return impact * IMPACT_SPEED;
    }
    public double calculateExternalImpact(){
        double impactSum =
                + (biomeTemperature - 0.8)* 5 * (biomeHumidity + 0.5)
                + sun
                + activity * 2.5
                + wet * -10
                + rain * -1
                + blockAroundImpact
                + blockUnderImpact;

        if (impactSum < 0) impactSum *= 1 - (coldResistance-1);
        else               impactSum *= 1 - (heatResistance-1);

        return impactSum * EXTERNAL_IMPACT_MULTIPLIER;
    }
    public double calculateInternalImpact(){
        data.debugger.addLine("foodImpact", foodImpact);
        data.debugger.addLine("foodImpactDuration", DrinkItemComponent.generateEffectDuration(foodDuration) + " (" + foodDuration +")");
        return foodImpact * FOOD_MULTIPLIER * INTERNAL_IMPACT_MULTIPLIER;
    }
    public double calculateAroundBlocksImpact(){
        final int radius = BLOCK_AROUND_RADIUS_SCANNER;

        double totalImpact = 0;
        Location loc = data.location.clone();
        for (double i = data.location.x()-radius; i < data.location.x()+radius; i++) {
            for (double j = data.location.y()-radius; j < data.location.y()+radius; j++) {
                for (double k = data.location.z()-radius; k < data.location.z()+radius; k++) {
                    loc.set(i, j, k);
                    double impact = AROUND_BLOCK_MAP.getOrDefault(loc.getBlock().getType(), 0d);
                    totalImpact += impact;
                }
            }
        }
        return totalImpact;
    }

    public void setFoodImpact(double impact, int duration){
        foodImpact = impact;
        foodDuration = duration;
    }

    public class Hud{
        public static final Key FONT = Key.key("toughasnailsu:temperature");
        public static final int FRAMES = 21;
        public static final Vector FREEZE_COLOR = new Vector(20, 78, 112).multiply((double) 1/255);
        public static final Vector HEAT_COLOR = new Vector(217, 131, 44).multiply((double) 1/255);
        public static final int SIZE = 14;
        public static final int OFFSET = -7;

        int animation = -1;
        Component icon;
        TextColor color;
        public Component get(){
            if (animation == -1 && (justStartedRising || justStartedDropping)){
                animation = 0;
            }
            if (animation >= FRAMES) animation = -1;

            updateComponents();
            return ComponentU.textWithNoSpace(OFFSET, icon.font(FONT).color(color), SIZE);
        }
        public void updateComponents(){
            if (stabilizing) {
                icon = Component.translatable("image.toughasnailsu.temperature.stabilizing");
                color = smoothColor();
                return;
            }
            else if (getValue() == Temperature.MAX){
                icon = Component.translatable("image.toughasnailsu.temperature.heat");
                color = NamedTextColor.WHITE;
                return;
            }
            else if (getValue() == Temperature.MIN){
                icon = Component.translatable("image.toughasnailsu.temperature.freeze");
                color = NamedTextColor.WHITE;
                return;
            }

            String type = lastImpact > 0 ? "rise" : "drop";
            if (animation == -1) {
                icon = Component.translatable("image.toughasnailsu.temperature."+type);
            } else {
                icon = Component.translatable("animation.toughasnailsu.temperature." + type +'.' + animation++);
            }
            color = smoothColor();
        }
        public TextColor smoothColor(){
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
