package me.udnek.toughasnailsu.data;

import me.udnek.itemscoreu.serializabledata.SerializableDataManager;
import me.udnek.itemscoreu.utils.SelfRegisteringListener;
import me.udnek.toughasnailsu.ToughAsNailsU;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.Serializable;
import java.util.HashMap;

public class PlayerDatabase extends SelfRegisteringListener {

    private final HashMap<Player, PlayerData> database = new HashMap<>();
    private static PlayerDatabase instance;

    public PlayerDatabase(JavaPlugin plugin) {
        super(plugin);
    }

    public static PlayerDatabase getInstance() {
        if (instance == null) instance = new PlayerDatabase(ToughAsNailsU.getInstance());
        return instance;

    }

    public HashMap<Player, PlayerData> getAllData() {
        return database;
    }

    public PlayerData get(Player player){
        return database.get(player);
    }

    public void initialize(Player player){
        PlayerData playerData = database.get(player);
        if (playerData == null) {
            PlayerData newData = PlayerData.deserialize(player);
            database.put(player, newData);
        }
    }

    public void terminate(Player player){
        PlayerData playerData = database.get(player);
        if (playerData == null) return;
        SerializableDataManager.write(playerData, ToughAsNailsU.getInstance(), player);
        database.remove(player);
    }

    ///////////////////////////////////////////////////////////////////////////
    // EVENTS
    ///////////////////////////////////////////////////////////////////////////

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        initialize(event.getPlayer());
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        terminate(event.getPlayer());
    }
    @EventHandler
    public void onReload(ServerLoadEvent event){
        if (event.getType() != ServerLoadEvent.LoadType.RELOAD) return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            initialize(player);
        }
    }

}
