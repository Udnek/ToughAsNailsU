package me.udnek.toughasnailsu.hud;

import me.udnek.itemscoreu.customhud.CustomHud;
import me.udnek.itemscoreu.customhud.CustomHudManager;
import me.udnek.toughasnailsu.ToughAsNailsU;
import me.udnek.toughasnailsu.data.PlayerData;
import me.udnek.toughasnailsu.data.PlayerDatabase;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class Hud implements CustomHud {
    
    private static Hud instance;
    private final PlayerDatabase database;

    private Hud(){
        CustomHudManager.addTicket(ToughAsNailsU.getInstance(), this);
        database = PlayerDatabase.getInstance();
    }

    public static Hud getInstance() {
        if (instance == null) instance = new Hud();
        return instance;
        
    }
    
    @Override
    public Component getText(Player player) {
        PlayerData playerData = database.get(player);
        return playerData.getHud();
    }
}
