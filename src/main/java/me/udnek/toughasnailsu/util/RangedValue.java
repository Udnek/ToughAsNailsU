package me.udnek.toughasnailsu.util;

public abstract class RangedValue {

    protected double value;
    public abstract double getMax();
    public abstract double getMin();

    public double getValue() {return value;}

    public void add(double addValue){
        set(value + addValue);
    }
    public void set(double newValue){
        if (newValue > getMax()){
            value = getMax();
            return;
        }
        if (newValue < getMin()){
            value = getMin();
            return;
        }
        value = newValue;
    }
}
