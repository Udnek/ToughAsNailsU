package me.udnek.toughasnailsu;

import me.udnek.itemscoreu.customitem.VanillaItemManager;
import me.udnek.itemscoreu.resourcepack.ResourcePackablePlugin;
import me.udnek.toughasnailsu.attribute.Attributes;
import me.udnek.toughasnailsu.command.DebugCommand;
import me.udnek.toughasnailsu.data.DataTicker;
import me.udnek.toughasnailsu.data.Database;
import me.udnek.toughasnailsu.effect.Effects;
import me.udnek.toughasnailsu.enchantment.Enchantments;
import me.udnek.toughasnailsu.hud.Hud;
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

        Items.SEA_WATER_BOTTLE.key();
        Attributes.COLD_RESISTANCE.key();
        Effects.THIRST.key();
        Enchantments.NAIL.key();


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
        Database.getInstance();
        DataTicker.getInstance().start(this);
        Hud.getInstance();
    }

    @Override
    public void onDisable() {
        DataTicker.getInstance().stop();
    }
}
