package me.udnek.toughasnailsu.data;

import me.udnek.itemscoreu.util.ComponentU;
import me.udnek.toughasnailsu.effect.Effects;
import me.udnek.toughasnailsu.util.RangedValue;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class Thirst extends RangedValue {
    public static final double MAX = 20;
    public static final double MIN = 0;
    public static final double DEFAULT = 20;

    final PlayerData data;
    final Hud hud = Bukkit.getPluginManager().isPluginEnabled("RpgU") ? new RpguHud() : new NormalHud();

    public Thirst(PlayerData data){
        set(DEFAULT);
        this.data = data;
    }
    @Override
    public double getMax() {return MAX;}
    @Override
    public double getMin() {return MIN;}


    public void tick(){
        if (data.thirst.value == 0){
            data.player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 20, 1, false, true));
            data.player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20, 0, false, true));
        }
    }

    public boolean isThirsty(){
        return Effects.THIRST.has(data.player);
    }

    public interface Hud{
        @NotNull Component get();
    }

    public class RpguHud implements Hud{
        public static final Key FONT = Key.key("toughasnailsu:rpgu_thirst");

        private static final String BASE_PREFIX = "hud.rpgu.stat.level.";
        private static final Component BACKGROUND = Component.translatable("hud.rpgu.stat.background");
        private static final Component ICON = Component.translatable("hud.rpgu.stat.icon");
        private static final Component OVERLAY = Component.translatable("hud.rpgu.stat.overlay");

        public static final TextColor ICON_NORMAL_COLOR = NamedTextColor.WHITE;
        public static final TextColor ICON_THIRST_COLOR = TextColor.fromHexString("#2f9e35");
        public static final TextColor BAR_NORMAL_COLOR = TextColor.fromHexString("#0096da");
        public static final TextColor BAR_THIRST_COLOR = ICON_THIRST_COLOR;

        @Override
        public @NotNull Component get(){
            Component text = Component.translatable(BASE_PREFIX +((int) (Math.ceil(getValue()/getMax()*75))));

            TextColor iconColor;
            TextColor barColor;
            if (Effects.THIRST.has(data.player)) {
                iconColor = ICON_THIRST_COLOR;
                barColor = BAR_THIRST_COLOR;
            }
            else {
                iconColor = ICON_NORMAL_COLOR;
                barColor = BAR_NORMAL_COLOR;
            }
            return ComponentU.textWithNoSpace(
                    10,
                    BACKGROUND.color(ComponentU.NO_SHADOW_COLOR)
                            .append(text.color(barColor))
                            .append(ICON.color(iconColor))
                            .append(OVERLAY)
                            .color(ComponentU.NO_SHADOW_COLOR).font(FONT),
                    0);
        }
    }

    public class NormalHud implements Hud{
        public static final Key FONT_NORMAL = Key.key("toughasnailsu:thirst");
        public static final Key FONT_THIRSTY = Key.key("toughasnailsu:thirst_thirsty");

        @Override
        public @NotNull Component get(){
            Key font = isThirsty() ? FONT_THIRSTY : FONT_NORMAL;
            return ComponentU.textWithNoSpace(
                    11,
                    Component.translatable("hud.toughasnailsu.thirst.level."+((int) value)).font(font).color(ComponentU.NO_SHADOW_COLOR),
                    82
            );
        }
    }
}
