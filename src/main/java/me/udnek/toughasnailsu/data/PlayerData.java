package me.udnek.toughasnailsu.data;

import me.udnek.itemscoreu.nms.DownfallType;
import me.udnek.itemscoreu.nms.Nms;
import me.udnek.itemscoreu.serializabledata.SerializableData;
import me.udnek.itemscoreu.serializabledata.SerializableDataManager;
import me.udnek.toughasnailsu.ToughAsNailsU;
import me.udnek.toughasnailsu.util.RangedValue;
import me.udnek.toughasnailsu.util.Utils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

public class PlayerData implements SerializableData {

    public static final boolean DEBUG = true;

    private final Player player;
    private Location location;
    private int step = 0;

    private Temperature temperature;
    private Thirst thirst;
    private final Hud hud = new Hud();
    private final Debug debug = new Debug();

    private PlayerData(Player player){
        this.player = player;
        this.temperature = new Temperature();
        this.thirst = new Thirst();
        debug.initialize();
    }

    public Temperature getTemperature() {return temperature;}
    public Thirst getThirst() {return thirst;}

    public void tick(){
        if (Bukkit.getCurrentTick() % 5 != 0) return;


        debug.prepare();

        temperature.tick();
        thirst.tick();

        debug.debug();
        step++;
    }
    public Component getHud(){return hud.get();}
    public boolean shouldSkipTick(int every, int ticket){
        return (step%every == ticket);
    }

    ///////////////////////////////////////////////////////////////////////////
    // TECHNICAL
    ///////////////////////////////////////////////////////////////////////////
    public static PlayerData empty(Player player){
        return new PlayerData(player);
    }
    public static PlayerData deserialize(Player player){
        PlayerData playerData = PlayerData.empty(player);
        SerializableDataManager.read(playerData, ToughAsNailsU.getInstance(), player);
        return playerData;
    }
    @Override
    public String serialize() {
        return temperature.getValue() + "," + thirst.getValue();
    }
    @Override
    public void deserialize(String data) {
        if (data == null){
            setDefaultData();
            return;
        }
        String[] split = data.split(",");
        if (split.length != 2) {
            setDefaultData();
            return;
        }
        float temperatureValue = Float.parseFloat(split[0]);
        float thirstValue = Float.parseFloat(split[1]);
        thirst = new Thirst(thirstValue);
        temperature = new Temperature(temperatureValue);
    }
    public void setDefaultData(){
        thirst = new Thirst();
        temperature = new Temperature();
    }
    @Override
    public String getDataName() {return "data";}

    ///////////////////////////////////////////////////////////////////////////
    // SUBS
    ///////////////////////////////////////////////////////////////////////////
    public class Hud{
        public static final Key FONT = Key.key("toughasnailsu:temperature");
        public static final int FRAMES = 21;
        public static final Vector FREEZE_COLOR = new Vector(20, 78, 112).multiply((double) 1/255);
        public static final Vector HEAT_COLOR = new Vector(217, 131, 44).multiply((double) 1/255);

        int animation = -1;
        public Component get(){
            if (animation == -1 && (temperature.justStartedRising || temperature.justStartedDropping)){
                animation = 0;
            }
            if (animation >= FRAMES) animation = -1;

            return getIcon().font(FONT).color(getColor());
        }
        public Component getIcon(){
            if (temperature.stabilizing) {
                return Component.translatable("image.toughasnailsu.temperature.stabilizing");
            }
            String type = temperature.lastImpact > 0 ? "rise" : "drop";
            if (animation == -1) {
                return Component.translatable("image.toughasnailsu.temperature."+type);
            } else {
                return Component.translatable("animation.toughasnailsu.temperature." + type +'.' + animation++);
            }
        }
        public TextColor getColor(){
            double normalized = temperature.getNormalized();
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
    public class Thirst extends RangedValue{
        public static final double MAX = 20;
        public static final double MIN = 0;
        public static final double DEFAULT = 20;

        public Thirst(double value){set(value);}
        public Thirst(){this(DEFAULT);}
        @Override
        public double getMax() {return MAX;}
        @Override
        public double getMin() {return MIN;}

        public void tick(){

        }

    }
    public class Temperature extends RangedValue {
        public static final double MAX = 100;
        public static final double MIN = -100;
        public static final double DEFAULT = 0;

        public static final double EXTERNAL_IMPACT_MULTIPLIER = 5;
        public static final double IMPACT_SPEED = 1;
        public static final double ADAPTATION_MULTIPLIER = 0.5;
        public static final double NATURAL_RESTORE_VALUE = 5;
        public static final double NATURAL_RESTORE_RANGE = 10;

        double biomeHumidity;
        double biomeTemperature;
        double sun; // [0 ... 1]
        double activity; // [0, 1]
        double wet; // [0, 1]
        double weather; // [0, 1]

        double lastImpact = 0;
        boolean justStartedRising = false;
        boolean justStartedDropping = false;
        boolean stabilizing = false;

        public Temperature(double value){ set(value); }
        public Temperature(){this(DEFAULT);}
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
        }

        public void updateBiomeData(){
            biomeHumidity = location.getBlock().getHumidity();
            biomeTemperature = location.getBlock().getTemperature();
        }
        public void updateSun(){
            Block block = location.getBlock();
            byte currentMaxLight = block.getLightFromSky();
            byte surfaceLight = location.getWorld().getBlockAt(0, 1000, 0).getLightLevel();
            //addDebug("surfaceLight", surfaceLight);
            //addDebug("currentMaxLight", currentMaxLight);
            if (surfaceLight > currentMaxLight) {
                sun = (double) currentMaxLight / 15;
            } else {
                sun =  (double) surfaceLight / 15;
            }
        }
        public void updateAll(){
            location = player.getLocation();
            activity = (player.isSprinting() || player.isSwimming()) ? 1 : 0;
            wet = player.isInWater() ? 1 : 0;
            weather = player.isInRain() ? 1 : 0;
            updateBiomeData();
            updateSun();
        }
        public double calculateImpact(){
            double impact = calculateExternalImpact();
            debug.addLine("externalImpact", impact + "(" + impact/EXTERNAL_IMPACT_MULTIPLIER + ")");

            if (impact < 0) impact += NATURAL_RESTORE_VALUE;
            else            impact -= NATURAL_RESTORE_VALUE;

            if (player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE) && impact > 0){
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

            debug.addLine("finalImpact", impact);

            return impact * IMPACT_SPEED;
        }

        public double calculateExternalImpact(){
            double impactSum =
                            + (biomeTemperature - 0.8)* 5 * (biomeHumidity + 0.5)
                            + sun
                            + activity * 2.5
                            + wet * -10
                            + weather * -1;

            return impactSum * EXTERNAL_IMPACT_MULTIPLIER;
        }

        public double calculateWeather(){
/*            addDebug("weatherDuration", location.getWorld().getWeatherDuration());
            addDebug("clearWeatherDuration", location.getWorld().getClearWeatherDuration());
            addDebug("hasStorm", location.getWorld().hasStorm());
            addDebug("isThunder", location.getWorld().isThundering());*/
            if (!location.getWorld().hasStorm()) return 0;
            if (Nms.get().getDownfallType(location) == DownfallType.NONE) return 0;
            double highestY = location.getWorld().getHighestBlockAt((int) location.x(), (int) location.z()).getY();
            if (highestY > location.getY()) return 0;
            debug.addLine("underRoof", highestY > location.getY());
            return 1;
        }
    }
    public class Debug{
        private Scoreboard scoreboard;
        private final static String SIDEBAR_NAME = "tanu";
        private int debugLines;
        private void initialize(){
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            //sc= Bukkit.getScoreboardManager().getNewScoreboard();
            player.setScoreboard(scoreboard);
            Objective objective = scoreboard.getObjective(SIDEBAR_NAME);
            if (objective != null) objective.unregister();
        }
        private void prepare(){
            if (!DEBUG) return;
            debugLines = 99;
            Objective objective = scoreboard.getObjective(SIDEBAR_NAME);
            if (objective != null) objective.unregister();
            objective = scoreboard.registerNewObjective(SIDEBAR_NAME, Criteria.DUMMY, Component.text(SIDEBAR_NAME));
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }
        private void debug(){
            addLine("---------------------------------");
            addLine("temp", temperature.getValue());
            addLine("thirst", thirst.getValue());
            addLine();
            addLine("humidity", temperature.biomeHumidity);
            addLine("biomeTemp", temperature.biomeTemperature);
            //addLine("sunMul", temperature.sun);
            //addLine("activity", temperature.activity);
            //addLine("wet", temperature.wet);
            //addLine("underWeather", temperature.weather);
            addLine();
            addLine("jsRising", temperature.justStartedRising);
            addLine("jsDropping", temperature.justStartedDropping);
            addLine("stabilizing", temperature.stabilizing);
            addLine("anim", hud.animation);
            addLine("tempNormalized", temperature.getNormalized());
        }
        private void addLine(String name, Object value){
            addLine(name + ": " + value);}
        private void addLine(){
            addLine("");}
        private void addLine(String value){
            if (!DEBUG) return;
            scoreboard.getObjective(SIDEBAR_NAME).getScore(value).setScore(debugLines--);
        }
    }
}















