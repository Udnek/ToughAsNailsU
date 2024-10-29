package me.udnek.toughasnailsu.util;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

public class Tags {

    //public static BaseTag TEST = new BaseTag(CustomItem.class, new NamespacedKey(ToughAsNailsU.getInstance(), "test"), List.of(Items.DRINKING_GLASS_BOTTLE, Items.SEA_WATER_BOTTLE));



    static class BaseTag<T extends Keyed, C extends io.papermc.paper.tag.BaseTag<T, C>> extends io.papermc.paper.tag.BaseTag<T, C> {


        public BaseTag(@NotNull Class<T> clazz, @NotNull NamespacedKey key, @NotNull Collection<T> values) {
            super(clazz, key, values);
        }

        @Override
        protected @NotNull Set<T> getAllPossibleValues() {return tagged;}

        @Override
        protected @NotNull String getName(@NotNull Keyed keyed) {return keyed.key().asString();}
    }
}