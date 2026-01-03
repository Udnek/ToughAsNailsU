package me.udnek.toughasnailsu.component;

import me.udnek.coreu.custom.component.ConstructableComponentType;
import me.udnek.coreu.custom.component.CustomComponentType;
import me.udnek.coreu.custom.item.CustomItem;
import me.udnek.coreu.custom.registry.CustomRegistries;
import me.udnek.coreu.rpgu.component.RPGUActiveItem;
import me.udnek.toughasnailsu.ToughAsNailsU;
import me.udnek.toughasnailsu.item.Flask;

public class ComponentTypes {
    public static final CustomComponentType<CustomItem, DrinkItem> DRINK_ITEM =
            register(new ConstructableComponentType<>("drink_item", DrinkItem.DEFAULT));
    public static final CustomComponentType<RPGUActiveItem, Flask.ActiveAbilityComponent> FLASK_ACTIVE_ABILITY =
            register(new ConstructableComponentType<>("flask_active_ability", Flask.ActiveAbilityComponent.DEFAULT));

    private static <T extends CustomComponentType<?, ?>> T register(T type){
        return CustomRegistries.COMPONENT_TYPE.register(ToughAsNailsU.getInstance(), type);
    }
}
