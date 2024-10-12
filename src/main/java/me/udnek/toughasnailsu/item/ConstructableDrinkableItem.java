package me.udnek.toughasnailsu.item;

import me.udnek.itemscoreu.customitem.ConstructableCustomItem;
import me.udnek.itemscoreu.customitem.CustomItem;
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
    protected final Integer customModelData;

    protected final int thirstRestoration;
    protected final boolean inflictsThirst;
    protected final double temperatureImpact;
    protected final int temperatureImpactDuration;

    protected ConstructableDrinkableItem(Material material, String rawId, Integer customModelData, int thirstRestoration, boolean inflictsThirst, double temperatureImpact, int temperatureImpactDuration){
        this.material = material;
        this.rawId = rawId;
        this.customModelData = customModelData;
        this.thirstRestoration = thirstRestoration;
        this.inflictsThirst = inflictsThirst;
        this.temperatureImpact = temperatureImpact;
        this.temperatureImpactDuration = temperatureImpactDuration;
    }

    @Override
    public void afterInitialization() {
        super.afterInitialization();
        setComponent(new DrinkItemComponent(thirstRestoration, inflictsThirst, temperatureImpact, temperatureImpactDuration));
    }

    public static CustomItem normal(String rawId, Integer customModelData, int thirstRestoration, double temperatureImpact, int temperatureImpactDuration){
        return new ConstructableDrinkableItem(Material.POTION, rawId, customModelData, thirstRestoration, false, temperatureImpact, temperatureImpactDuration);
    }
    public static CustomItem dirty(String rawId, Integer customModelData, int thirstRestoration, double temperatureImpact, int temperatureImpactDuration){
        return new ConstructableDrinkableItem(Material.POTION, rawId, customModelData, thirstRestoration, true, temperatureImpact, temperatureImpactDuration);
    }
    @Override
    public @NotNull String getRawId() {return rawId;}
    @Override
    public @NotNull Material getMaterial() {return material;}
    @Nullable
    @Override
    public Integer getCustomModelData() {
        return customModelData;
    }

    @Override
    protected void modifyFinalItemMeta(ItemMeta itemMeta) {
        if (!(itemMeta instanceof PotionMeta potionMeta)) return;
        potionMeta.setBasePotionType(null);
        potionMeta.setColor(Color.WHITE);
        potionMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
    }
}
