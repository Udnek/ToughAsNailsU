package me.udnek.toughasnailsu.data;

import me.udnek.itemscoreu.utils.TickingTask;

public class DataTicker extends TickingTask {

    public static final int DELAY = 1;
    PlayerDatabase database = PlayerDatabase.getInstance();
    
    private static DataTicker instance;
    private DataTicker(){}
    public static DataTicker getInstance() {
        if (instance == null) instance = new DataTicker();
        return instance;
    }
    
    @Override
    public int getDelay() {
        return DELAY;
    }

    @Override
    public void run() {
        for (PlayerData playerData : database.getAllData().values()) {
            playerData.tick();
        }
    }
}
