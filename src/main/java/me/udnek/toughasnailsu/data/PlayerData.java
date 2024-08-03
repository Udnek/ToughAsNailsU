package me.udnek.toughasnailsu.data;

import me.udnek.itemscoreu.nms.DownfallType;
import me.udnek.itemscoreu.nms.Nms;
import me.udnek.itemscoreu.serializabledata.SerializableData;
import me.udnek.itemscoreu.serializabledata.SerializableDataManager;
import me.udnek.toughasnailsu.ToughAsNailsU;
import me.udnek.toughasnailsu.mechanic.Temperature;
import me.udnek.toughasnailsu.mechanic.Thirst;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class PlayerData implements SerializableData {

    public static final boolean DEBUG = false;

    public static final double EXTERNAL_IMPACT_MULTIPLIER = 10;
    public static final double IMPACT_SPEED = 0.01;
    public static final double ADAPTATION_MULTIPLIER = 0.5;
    public static final double NATURAL_RESTORE_VALUE = 5;

    private final Player player;
    private Location location;
    private int step = 0;

    private Temperature temperature;
    private Thirst thirst;

    private double biomeHumidity;
    private double biomeTemperature;
    private double sun; // [0 ... 1]
    private double activity; // [0, 1]
    private double wet; // [0, 1]
    private double weather; // [0, 1]

    private PlayerData(Player player, Temperature temperature, Thirst thirst){
        this.player = player;
        this.temperature = temperature;
        this.thirst = thirst;
        initDebug();
    }
    public void tick(){
        preDebug();
        updateAll();
        updateTemperature();
        debug();
        step++;
    }
    public Component getHud(){
        return Component.text(
                "temp=" + Math.rint(temperature.get()) +
                        " thirst="+Math.rint(thirst.get()));
    }
    public boolean isTicketTick(int k, int n){
        return (step%n == k);
    }
    ///////////////////////////////////////////////////////////////////////////
    // CALCULATIONS
    ///////////////////////////////////////////////////////////////////////////
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
    public double calculateWeather(){
//        addDebug("weatherDuration", location.getWorld().getWeatherDuration());
//        addDebug("clearWeatherDuration", location.getWorld().getClearWeatherDuration());
//        addDebug("hasStorm", location.getWorld().hasStorm());
//        addDebug("isThunder", location.getWorld().isThundering());

        if (!location.getWorld().hasStorm()) return 0;
        if (Nms.get().getDownfallType(location) == DownfallType.NONE) return 0;
        double highestY = location.getWorld().getHighestBlockAt((int) location.x(), (int) location.z()).getY();
        if (highestY > location.getY()) return 0;
        addDebug("underRoof", highestY > location.getY());
        return 1;
    }
    public void updateAll(){
        location = player.getLocation();
        activity = (player.isSprinting() || player.isSwimming()) ? 1 : 0;
        wet = player.isInWater() ? 1 : 0;
        weather = calculateWeather();
        updateBiomeData();
        updateSun();
    }
    public void updateTemperature(){
        double impact = calculateFinalImpact();
        addDebug("finalImpact", impact);
        temperature.add(impact);
    }
    public double calculateFinalImpact(){
        double impact = calculateExternalImpact();
        addDebug("externalImpact", impact/EXTERNAL_IMPACT_MULTIPLIER);

        if (impact < 0) impact += NATURAL_RESTORE_VALUE;
        else impact -= NATURAL_RESTORE_VALUE;

        if (player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE) && impact > 0){
            return 0;
        }

        double temperatureValue = this.temperature.get();

        if (isSameSign(impact, temperatureValue)){
            impact *= ADAPTATION_MULTIPLIER;
        }

        if (Math.abs(impact) <= NATURAL_RESTORE_VALUE && Math.abs(temperatureValue) < NATURAL_RESTORE_VALUE){
            impact = -temperatureValue;
        }
        return impact * IMPACT_SPEED;
    }

    public double calculateExternalImpact(){
        double impactSum =
                + biomeTemperature * 5 * (biomeHumidity + 0.5)
                + sun
                + activity
                + wet * -10
                + weather * -1;

        return impactSum * EXTERNAL_IMPACT_MULTIPLIER;
    }

    public boolean isSameSign(double a, double b){
        return a*b > 0;
    }
    ///////////////////////////////////////////////////////////////////////////
    // DEBUG
    ///////////////////////////////////////////////////////////////////////////
    private final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    private final static String SIDEBAR_NAME = "tanu";
    private int debugLines;
    private void initDebug(){
        //sc= Bukkit.getScoreboardManager().getNewScoreboard();
        player.setScoreboard(scoreboard);
        Objective objective = scoreboard.getObjective(SIDEBAR_NAME);
        if (objective != null) objective.unregister();
    }
    private void preDebug(){
        if (!DEBUG) return;
        debugLines = 99;
        Objective objective = scoreboard.getObjective(SIDEBAR_NAME);
        if (objective != null) objective.unregister();
        objective = scoreboard.registerNewObjective(SIDEBAR_NAME, Criteria.DUMMY, Component.text(SIDEBAR_NAME));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }
    private void debug(){
        addDebug("---------------------------------");
        addDebug("temp", temperature.get());
        addDebug("thirst", thirst.get());
        addDebug();
        addDebug("humidity", biomeHumidity);
        addDebug("biomeTemp", biomeTemperature);
        addDebug("sunMul", sun);
        addDebug("activity", activity);
        addDebug("wet", wet);
        addDebug("underWeather", wet);
    }
    private void addDebug(String name, Object value){addDebug(name + ": " + value);}
    private void addDebug(){addDebug("");}
    private void addDebug(String value){
        if (!DEBUG) return;
        scoreboard.getObjective(SIDEBAR_NAME).getScore(value).setScore(debugLines--);
    }
    ///////////////////////////////////////////////////////////////////////////
    // TECHNICAL
    ///////////////////////////////////////////////////////////////////////////
    public static PlayerData empty(Player player){
        return new PlayerData(player, new Temperature(), new Thirst());
    }
    public static PlayerData deserialize(Player player){
        PlayerData playerData = PlayerData.empty(player);
        SerializableDataManager.read(playerData, ToughAsNailsU.getInstance(), player);
        return playerData;
    }
    @Override
    public String serialize() {
        return temperature.get() + "," + thirst.get();
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
}















