package me.udnek.toughasnailsu.data;

import me.udnek.itemscoreu.serializabledata.SerializableData;
import me.udnek.itemscoreu.serializabledata.SerializableDataManager;
import me.udnek.toughasnailsu.ToughAsNailsU;
import me.udnek.toughasnailsu.util.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class PlayerData implements SerializableData {
    private static boolean doDebug = false;

    final Player player;
    Location location;
    int step = 0;

    final Temperature temperature;
    final Thirst thirst;
    final Debugger debugger = new Debugger();

    private PlayerData(@NotNull Player player){
        this.player = player;
        temperature = new Temperature(this);
        thirst = new Thirst(this);
        debugger.initialize();
    }
    public @NotNull Temperature getTemperature() {return temperature;}
    public @NotNull Thirst getThirst() {return thirst;}
    public void tick(){
        location = player.getLocation();
        debugger.prepare();
        temperature.tick();
        thirst.tick();
        debugger.debug();
        step++;
    }
    public @NotNull Component getHud(){
        return temperature.hud.get().append(thirst.hud.get());
    }
    public boolean shouldSkipTick(int tickEvery, int ticket){
        return (step%tickEvery) != (ticket%tickEvery);
    }
    public boolean shouldSkipTick(int tickEvery){
        return shouldSkipTick(tickEvery, 0);
    }
    ///////////////////////////////////////////////////////////////////////////
    // TECHNICAL
    ///////////////////////////////////////////////////////////////////////////
    public static @NotNull PlayerData empty(@NotNull Player player){
        return new PlayerData(player);
    }
    public static @NotNull PlayerData deserialize(@NotNull Player player){
        PlayerData playerData = PlayerData.empty(player);
        SerializableDataManager.read(playerData, ToughAsNailsU.getInstance(), player);
        return playerData;
    }
    @Override
    public @NotNull String serialize() {
        return temperature.getValue() + "," + thirst.getValue();
    }
    @Override
    public void deserialize(String data) {
        if (data == null) return;
        String[] split = data.split(",");
        if (split.length != 2) return;
        float temperatureValue = Float.parseFloat(split[0]);
        float thirstValue = Float.parseFloat(split[1]);
        thirst.set(thirstValue);
        temperature.set(temperatureValue);
    }
    @Override
    public @NotNull String getDataName() {return "data";}
    ///////////////////////////////////////////////////////////////////////////
    // DEBUG
    ///////////////////////////////////////////////////////////////////////////
    public static void switchDebug(){
        doDebug = !doDebug;
        if (doDebug){
            for (PlayerData playerData : Database.getInstance().getAllData().values()) {
                playerData.player.setScoreboard(playerData.debugger.scoreboard);
            }
        } else{
            for (PlayerData playerData : Database.getInstance().getAllData().values()) {
                playerData.player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
            }
        }

    }

    public class Debugger {
        private Scoreboard scoreboard;
        private final static String SIDEBAR_NAME = "tanu";
        private int debugLines;
        private void initialize(){
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            player.setScoreboard(scoreboard);
            Objective objective = scoreboard.getObjective(SIDEBAR_NAME);
            if (objective != null) objective.unregister();
        }
        private void prepare(){
            if (!doDebug) return;
            player.setScoreboard(scoreboard);
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
            addLine("biomeTemp, biomeHum", Utils.roundForDebug(temperature.biomeTemperature), Utils.roundForDebug(temperature.biomeHumidity));
            addLine("blockAround, blockUnder", Utils.roundForDebug(temperature.blockAroundImpact), Utils.roundForDebug(temperature.blockUnderImpact));
            addLine("stabilizing", temperature.stabilizing);
            addLine("anim", temperature.hud.animation);
            addLine("tempNormalized", temperature.getNormalized());
            addLine("coldResistance", temperature.coldResistanceMultiplier);
            addLine("heatResistance", temperature.heatResistanceMultiplier);
        }
        void addLine(String name, Object ...value){
            addLine(name + ": " + Arrays.toString(value));
        }
        void addLine(String name, Object value){
            if (value instanceof Double d){
                addLine(name + ": " + Utils.roundForDebug(d));
            } else addLine(name + ": " + value);
        }
        void addLine(){
            addLine("");
        }
        void addLine(String value){
            if (!doDebug) return;
            scoreboard.getObjective(SIDEBAR_NAME).getScore(value).setScore(debugLines--);
        }
    }
}















