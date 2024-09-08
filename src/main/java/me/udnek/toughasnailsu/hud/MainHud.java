package me.udnek.toughasnailsu.hud;

import me.udnek.itemscoreu.customhud.CustomHud;
import me.udnek.itemscoreu.customhud.CustomHudManager;
import me.udnek.toughasnailsu.ToughAsNailsU;
import me.udnek.toughasnailsu.data.PlayerData;
import me.udnek.toughasnailsu.data.Database;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class MainHud implements CustomHud {
    
    private static MainHud instance;
    private final Database database;

    private MainHud(){
        CustomHudManager.addTicket(ToughAsNailsU.getInstance(), this);
        database = Database.getInstance();
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
