package me.udnek.toughasnailsu.effect;

import me.udnek.itemscoreu.customeffect.CustomEffect;
import me.udnek.itemscoreu.customregistry.CustomRegistries;
import me.udnek.toughasnailsu.ToughAsNailsU;

public class Effects {
    public static final CustomEffect THIRST = register(new ThirstEffect());

    private static CustomEffect register(CustomEffect customEffect){
        return CustomRegistries.EFFECT.register(ToughAsNailsU.getInstance(), customEffect);
    }
}