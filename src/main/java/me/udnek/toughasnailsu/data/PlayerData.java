package me.udnek.toughasnailsu.data;

import me.udnek.itemscoreu.serializabledata.SerializableData;
import me.udnek.itemscoreu.serializabledata.SerializableDataManager;
import me.udnek.toughasnailsu.ToughAsNailsU;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.EnumMap;

public class PlayerData implements SerializableData {
    public static final boolean DEBUG = true;

    final Player player;
    Location location;
    int step = 0;

    final Temperature temperature;
    final Thirst thirst;
    final Debug debug = new Debug();

    private PlayerData(Player player){
        this.player = player;
        temperature = new Temperature(this);
        thirst = new Thirst(this);
        debug.initialize();
    }
    public Temperature getTemperature() {return temperature;}
    public Thirst getThirst() {return thirst;}
    public void tick(){
        location = player.getLocation();
        debug.prepare();
        temperature.tick();
        thirst.tick();
        debug.debug();
        step++;
    }
    public Component getHud(){
        return temperature.hud.get().append(thirst.hud.get());
    }
    public boolean shouldSkipTick(int tickEvery, int ticket){
        return (step%tickEvery) != ticket;
    }
    public boolean shouldSkipTick(int tickEvery){
        return shouldSkipTick(tickEvery, 0);
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
            return;
        }
        String[] split = data.split(",");
        if (split.length != 2) {
            return;
        }
        float temperatureValue = Float.parseFloat(split[0]);
        float thirstValue = Float.parseFloat(split[1]);
        thirst.set(thirstValue);
        temperature.set(temperatureValue);
    }
    @Override
    public String getDataName() {return "data";}

    ///////////////////////////////////////////////////////////////////////////
    // DEBUG
    ///////////////////////////////////////////////////////////////////////////
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
            addLine("step", step);
            addLine("temp", temperature.getValue());
            addLine("thirst", thirst.getValue());
            addLine();
            addLine("humidity", temperature.biomeHumidity);
            addLine("biomeTemp", temperature.biomeTemperature);
            addLine("blockAround", temperature.blockAroundImpact);
            addLine("blockUnder", temperature.blockUnderImpact);
            addLine("onGround", player.isOnGround());
            //addLine("sunMul", temperature.sun);
            //addLine("activity", temperature.activity);
            //addLine("wet", temperature.wet);
            //addLine("underWeather", temperature.weather);
            addLine();
            addLine("jsRising", temperature.justStartedRising);
            addLine("jsDropping", temperature.justStartedDropping);
            addLine("stabilizing", temperature.stabilizing);
            addLine("anim", temperature.hud.animation);
            addLine("tempNormalized", temperature.getNormalized());
        }
        void addLine(String name, Object value){
            addLine(name + ": " + value);}
        void addLine(){
            addLine("");}
        void addLine(String value){
            if (!DEBUG) return;
            scoreboard.getObjective(SIDEBAR_NAME).getScore(value).setScore(debugLines--);
        }
    }
}















