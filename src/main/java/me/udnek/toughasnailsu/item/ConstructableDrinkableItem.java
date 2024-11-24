package me.udnek.toughasnailsu.item;

import me.udnek.itemscoreu.customitem.ConstructableCustomItem;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.nms.ConsumableComponent;
import me.udnek.itemscoreu.nms.Nms;
import me.udnek.toughasnailsu.component.DrinkItemComponent;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConstructableDrinkableItem extends ConstructableCustomItem implements ToughAsNailsUCustomItem {

    protected final Material material;
    protected final String rawId;

    protected final int thirstRestoration;
    protected final boolean inflictsThirst;
    protected final double temperatureImpact;
    protected final int temperatureImpactDuration;

    protected ConstructableDrinkableItem(Material material, String rawId, int thirstRestoration, boolean inflictsThirst, double temperatureImpact, int temperatureImpactDuration){
        this.material = material;
        this.rawId = rawId;
        this.thirstRestoration = thirstRestoration;
        this.inflictsThirst = inflictsThirst;
        this.temperatureImpact = temperatureImpact;
        this.temperatureImpactDuration = temperatureImpactDuration;
    }

    @Override
    public @Nullable ConsumableComponent getConsumable() {
        return Nms.get().getConsumableComponent(Material.POTION);
    }

    @Override
    public void initializeComponents() {
        super.initializeComponents();
        getComponents().set(new DrinkItemComponent(thirstRestoration, inflictsThirst, temperatureImpact, temperatureImpactDuration));
    }

    @Override
    public @Nullable CustomItem getUseRemainderCustom() {return Items.DRINKING_GLASS_BOTTLE;}

    public static CustomItem normal(String rawId, int thirstRestoration, double temperatureImpact, int temperatureImpactDuration){
        return new ConstructableDrinkableItem(Material.GUNPOWDER, rawId, thirstRestoration, false, temperatureImpact, temperatureImpactDuration);
    }
    public static CustomItem dirty(String rawId, int thirstRestoration, double temperatureImpact, int temperatureImpactDuration){
        return new ConstructableDrinkableItem(Material.GUNPOWDER, rawId, thirstRestoration, true, temperatureImpact, temperatureImpactDuration);
    }
    @Override
    public @NotNull String getRawId() {return rawId;}
    @Override
    public @NotNull Material getMaterial() {return material;}

    @Override
    protected void modifyFinalItemMeta(@NotNull ItemMeta itemMeta) {
        if (!(itemMeta instanceof PotionMeta potionMeta)) return;
        potionMeta.setBasePotionType(null);
        potionMeta.setColor(Color.WHITE);
        potionMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
    }
}
