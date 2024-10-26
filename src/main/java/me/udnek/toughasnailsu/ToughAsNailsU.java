package me.udnek.toughasnailsu;

import me.udnek.itemscoreu.customattribute.CustomAttribute;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.resourcepack.ResourcePackablePlugin;
import me.udnek.itemscoreu.util.VanillaItemManager;
import me.udnek.toughasnailsu.attribute.Attributes;
import me.udnek.toughasnailsu.command.Deb;
import me.udnek.toughasnailsu.command.DebugCommand;
import me.udnek.toughasnailsu.data.DataTicker;
import me.udnek.toughasnailsu.data.Database;
import me.udnek.toughasnailsu.hud.MainHud;
import me.udnek.toughasnailsu.item.Items;
import me.udnek.toughasnailsu.util.EventsListener;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public final class ToughAsNailsU extends JavaPlugin implements ResourcePackablePlugin {
    private static ToughAsNailsU instance;
    public static ToughAsNailsU getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        CustomItem boot = Items.SEA_WATER_BOTTLE;
        CustomAttribute boot1 = Attributes.COLD_RESISTANCE;


        VanillaItemManager.getInstance().replaceVanillaMaterial(Material.LEATHER_CHESTPLATE);
        VanillaItemManager.getInstance().replaceVanillaMaterial(Material.LEATHER_HELMET);
        VanillaItemManager.getInstance().replaceVanillaMaterial(Material.LEATHER_LEGGINGS);
        VanillaItemManager.getInstance().replaceVanillaMaterial(Material.LEATHER_BOOTS);
        VanillaItemManager.getInstance().replaceVanillaMaterial(Material.CHAINMAIL_CHESTPLATE);
        VanillaItemManager.getInstance().replaceVanillaMaterial(Material.CHAINMAIL_HELMET);
        VanillaItemManager.getInstance().replaceVanillaMaterial(Material.CHAINMAIL_LEGGINGS);
        VanillaItemManager.getInstance().replaceVanillaMaterial(Material.CHAINMAIL_BOOTS);


        new EventsListener(this);
        getCommand("tanudebug").setExecutor(new DebugCommand());
        getCommand("deb").setExecutor(new Deb());
        Database.getInstance();
        DataTicker.getInstance().start(this);
        MainHud.getInstance();
    }

    @Override
    public void onDisable() {
        DataTicker.getInstance().stop();
    }
}
