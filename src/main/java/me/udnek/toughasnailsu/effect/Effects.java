package me.udnek.toughasnailsu.effect;

import me.udnek.coreu.custom.effect.CustomEffect;
import me.udnek.coreu.custom.registry.CustomRegistries;
import me.udnek.toughasnailsu.ToughAsNailsU;

public class Effects {
    public static final CustomEffect THIRST = register(new ThirstEffect());
    public static final CustomEffect COLLING = register(new CollingEffect());
    public static final CustomEffect HEATING = register(new HeatingEffect());
    public static final CustomEffect TEMPERATURE_FORTIFY = register(new TemperatureFortifyEffect());

    private static CustomEffect register(CustomEffect customEffect){
        return CustomRegistries.EFFECT.register(ToughAsNailsU.getInstance(), customEffect);
    }
}