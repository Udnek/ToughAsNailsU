package me.udnek.toughasnailsu.item;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.PotionContents;
import me.udnek.itemscoreu.customitem.ConstructableCustomItem;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.toughasnailsu.component.DrinkItemComponent;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConstructableDrinkableItem extends ConstructableCustomItem implements ToughAsNailsUCustomItem {

    protected final Material material;
    protected final String rawId;

    protected final int thirstRestoration;
    protected final boolean inflictsThirst;
    protected final double temperatureImpact;
    protected final int temperatureImpactDuration;

    protected ConstructableDrinkableItem(@NotNull Material material, @NotNull String rawId, int thirstRestoration, boolean inflictsThirst, double temperatureImpact, int temperatureImpactDuration){
        this.material = material;
        this.rawId = rawId;
        this.thirstRestoration = thirstRestoration;
        this.inflictsThirst = inflictsThirst;
        this.temperatureImpact = temperatureImpact;
        this.temperatureImpactDuration = temperatureImpactDuration;
    }

    @Override
    public @Nullable DataSupplier<Consumable> getConsumable() {
        return DataSupplier.of(Material.POTION.getDefaultData(DataComponentTypes.CONSUMABLE));
    }

    @Override
    public void initializeComponents() {
        super.initializeComponents();
        getComponents().set(new DrinkItemComponent(thirstRestoration, inflictsThirst, temperatureImpact, temperatureImpactDuration));
    }

    @Override
    public @Nullable Boolean getHideAdditionalTooltip() {
        return true;
    }


    @Override
    public @Nullable DataSupplier<PotionContents> getPotionContents() {
        return DataSupplier.of(PotionContents.potionContents().potion(PotionType.WATER).customColor(Color.WHITE).build());
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
}
