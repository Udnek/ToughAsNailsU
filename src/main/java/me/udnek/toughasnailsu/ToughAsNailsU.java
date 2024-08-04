package me.udnek.toughasnailsu;

import me.udnek.itemscoreu.resourcepack.ResourcePackablePlugin;
import me.udnek.toughasnailsu.data.DataTicker;
import me.udnek.toughasnailsu.data.PlayerDatabase;
import me.udnek.toughasnailsu.hud.MainHud;
import me.udnek.toughasnailsu.util.EventsListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class ToughAsNailsU extends JavaPlugin implements ResourcePackablePlugin {
    private static ToughAsNailsU instance;
    public static ToughAsNailsU getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        new EventsListener(this);

        PlayerDatabase.getInstance();
        DataTicker.getInstance().start(this);
        MainHud.getInstance();
    }

    @Override
    public void onDisable() {
        DataTicker.getInstance().stop();
    }
}
