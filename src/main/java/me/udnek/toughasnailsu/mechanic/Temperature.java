package me.udnek.toughasnailsu.mechanic;

import me.udnek.itemscoreu.utils.LogUtils;

public class Temperature {

    public static final int MAX_TEMPERATURE = 100;
    public static final int MIN_TEMPERATURE = -100;
    public static final int DEFAULT_TEMPERATURE = 0;

    private double value;

    public Temperature(float value){this.value = value;}
    public Temperature(){this(DEFAULT_TEMPERATURE);}
    public double get(){return value;}

    public void add(double add){
        set(value + add);
    }

    public void set(double newValue){
        if (newValue > MAX_TEMPERATURE){
            value = MAX_TEMPERATURE;
            return;
        }
        if (newValue < MIN_TEMPERATURE){
            value = MIN_TEMPERATURE;
            return;
        }
        value = newValue;
    }

}
