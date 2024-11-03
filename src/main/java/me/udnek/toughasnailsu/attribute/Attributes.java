package me.udnek.toughasnailsu.attribute;

import me.udnek.itemscoreu.customattribute.ConstructableCustomAttribute;
import me.udnek.itemscoreu.customattribute.CustomAttribute;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import me.udnek.toughasnailsu.ToughAsNailsU;

public class Attributes {

    public static final CustomAttribute COLD_RESISTANCE = register(
            new ConstructableCustomAttribute("cold_resistance", 1,1, 2));
    public static final CustomAttribute HEAT_RESISTANCE = register(
            new ConstructableCustomAttribute("heat_resistance", 1,1, 2));

    public static CustomAttribute register(CustomAttribute attribute){
        return CustomRegistries.ATTRIBUTE.register(ToughAsNailsU.getInstance(), attribute);
    }
}
