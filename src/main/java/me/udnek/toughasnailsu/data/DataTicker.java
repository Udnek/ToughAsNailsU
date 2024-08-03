package me.udnek.toughasnailsu.data;

import me.udnek.itemscoreu.utils.TickingTask;

public class DataTicker extends TickingTask {

    public static final int DELAY = 1;
    PlayerDatabase database = PlayerDatabase.getInstance();
    
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
