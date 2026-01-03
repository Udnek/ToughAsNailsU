package me.udnek.toughasnailsu.util;

import me.udnek.coreu.custom.item.CustomItem;
import me.udnek.toughasnailsu.ToughAsNailsU;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static me.udnek.toughasnailsu.item.Items.*;

public class Tags {

    public static final org.bukkit.Tag<CustomItem> TEAS = new BaseTag<>(CustomItem.class, new NamespacedKey(ToughAsNailsU.getInstance(), "teas"),
            List.of(GREEN_SUGAR_TEA_BOTTLE, GREEN_GLOW_BERRY_TEA_BOTTLE, GREEN_SWEET_BERRY_TEA_BOTTLE));

    public static final org.bukkit.Tag<CustomItem> JUICES = new BaseTag<>(CustomItem.class, new NamespacedKey(ToughAsNailsU.getInstance(), "teas"),
            List.of(CARROT_JUICE_BOTTLE, SWEET_BERRY_JUICE_BOTTLE, MELON_JUICE_BOTTLE));



    public static class BaseTag<T extends Keyed, C extends io.papermc.paper.tag.BaseTag<T, C>> extends io.papermc.paper.tag.BaseTag<T, C> {


        public BaseTag(@NotNull Class<T> clazz, @NotNull NamespacedKey key, @NotNull Collection<T> values) {
            super(clazz, key, values);
        }

        @Override
        protected @NotNull Set<T> getAllPossibleValues() {return tagged;}

        @Override
        protected @NotNull String getName(@NotNull Keyed keyed) {return keyed.key().asString();}
    }
}