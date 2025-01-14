package me.udnek.toughasnailsu.attribute;

import me.udnek.itemscoreu.customattribute.ConstructableCustomAttribute;
import me.udnek.itemscoreu.customattribute.CustomAttribute;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import me.udnek.toughasnailsu.ToughAsNailsU;

public class Attributes {

    public static final CustomAttribute COLD_RESISTANCE = register(
            new ConstructableCustomAttribute("cold_resistance", 0,0, 1, true, true));
    public static final CustomAttribute HEAT_RESISTANCE = register(
            new ConstructableCustomAttribute("heat_resistance", 0,0, 1, true, true));
    public static final CustomAttribute WATER_RESISTANCE = register(
            new ConstructableCustomAttribute("water_resistance", 0,0, 1, true, true));


    public static CustomAttribute register(CustomAttribute attribute){
        return CustomRegistries.ATTRIBUTE.register(ToughAsNailsU.getInstance(), attribute);
    }
}
