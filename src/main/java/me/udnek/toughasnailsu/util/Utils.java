package me.udnek.toughasnailsu.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static boolean isSameSign(double a, double b){
        return a*b >= 0;
    }

    public static String roundForDebug(Double v){
        return String.format("%.2f", v);
    }

    public static @NotNull List<Component> resetStyle(@NotNull List<Component> components){
        return resetStyle(components, null);
    }

    public static @NotNull List<Component> resetStyle(@NotNull List<Component> components, @Nullable TextColor color){
        List<Component> componentList = new ArrayList<>();
        for (Component component : components) resetStyle(component, color);
        return componentList;
    }

    public static @NotNull Component resetStyle(@NotNull Component component){
        return resetStyle(component, null);
    }

    public static @NotNull Component resetStyle(@NotNull Component component, @Nullable TextColor color){
        if (color == null) color = NamedTextColor.WHITE;
        return component.decoration(TextDecoration.ITALIC, false).color(color);
    }

}
