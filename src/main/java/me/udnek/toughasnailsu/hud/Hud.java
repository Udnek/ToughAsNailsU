package me.udnek.toughasnailsu.hud;

import me.udnek.itemscoreu.customhud.CustomHud;
import me.udnek.itemscoreu.customhud.CustomHudManager;
import me.udnek.toughasnailsu.ToughAsNailsU;
import me.udnek.toughasnailsu.data.Database;
import me.udnek.toughasnailsu.data.PlayerData;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Hud implements CustomHud {
    
    private static Hud instance;
    private final Database database;

    private Hud(){
        CustomHudManager.getInstance().addTicket(ToughAsNailsU.getInstance(), this);
        database = Database.getInstance();
    }

    public static Hud getInstance() {
        if (instance == null) instance = new Hud();
        return instance;
        
    }
    
    @Override
    public @NotNull Component getText(@NotNull Player player) {
        PlayerData playerData = database.get(player);
        return playerData.getHud();
    }
}
