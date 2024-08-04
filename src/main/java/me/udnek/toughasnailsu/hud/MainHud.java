package me.udnek.toughasnailsu.hud;

import me.udnek.itemscoreu.customhud.CustomHud;
import me.udnek.itemscoreu.customhud.CustomHudManager;
import me.udnek.toughasnailsu.ToughAsNailsU;
import me.udnek.toughasnailsu.data.PlayerData;
import me.udnek.toughasnailsu.data.PlayerDatabase;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class MainHud implements CustomHud {
    
    private static MainHud instance;
    private final PlayerDatabase database;

    private MainHud(){
        CustomHudManager.addTicket(ToughAsNailsU.getInstance(), this);
        database = PlayerDatabase.getInstance();
    }

    public static MainHud getInstance() {
        if (instance == null) instance = new MainHud();
        return instance;
        
    }
    
    @Override
    public Component getText(Player player) {
        PlayerData playerData = database.get(player);
        return playerData.getHud();
    }
}
