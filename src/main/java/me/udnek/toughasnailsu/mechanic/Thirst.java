package me.udnek.toughasnailsu.mechanic;

public class Thirst {

    public static final double MAX_THIRST = 20;
    public static final double MIN_THIRST = 0;
    public static final double DEFAULT_THIRST = 20;

    private double value;

    public Thirst(double value){this.value = value;}

    public Thirst(){this(DEFAULT_THIRST);}

    public double get(){return value;}

    public void set(double newValue){
        if (newValue > MAX_THIRST){
            value = MAX_THIRST;
            return;
        }
        if (newValue < MIN_THIRST){
            value = MIN_THIRST;
            return;
        }
        value = newValue;
    }
}
