package me.udnek.toughasnailsu;

import me.udnek.itemscoreu.customhud.CustomHudManager;
import me.udnek.itemscoreu.utils.LogUtils;
import me.udnek.toughasnailsu.data.DataTicker;
import me.udnek.toughasnailsu.data.PlayerDatabase;
import me.udnek.toughasnailsu.hud.Hud;
import org.bukkit.plugin.java.JavaPlugin;

public final class ToughAsNailsU extends JavaPlugin {

    private DataTicker dataTicker;

    private static ToughAsNailsU instance;

    public static ToughAsNailsU getInstance() {
        return instance;
    }



    @Override
    public void onEnable() {
        instance = this;
        PlayerDatabase.getInstance();
        dataTicker = new DataTicker();
        dataTicker.start(this);
        Hud.getInstance();
    }

    @Override
    public void onDisable() {
        dataTicker.stop();
    }
}
