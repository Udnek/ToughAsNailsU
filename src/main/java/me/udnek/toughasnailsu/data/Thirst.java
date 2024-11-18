package me.udnek.toughasnailsu.data;

import me.udnek.itemscoreu.util.ComponentU;
import me.udnek.toughasnailsu.effect.Effects;
import me.udnek.toughasnailsu.util.RangedValue;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Thirst extends RangedValue {
    public static final double MAX = 20;
    public static final double MIN = 0;
    public static final double DEFAULT = 20;

    final PlayerData data;
    final Hud hud = new Hud();

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

    public class Hud{
        public static final Key FONT_NORMAL = Key.key("toughasnailsu:thirst");
        public static final Key FONT_THIRSTY = Key.key("toughasnailsu:thirst_thirsty");
        public static final int SIZE = 82;
        public static final int OFFSET = 11;

        public Component get(){
            Key font = isThirsty() ? FONT_THIRSTY : FONT_NORMAL;
            return ComponentU.textWithNoSpace(
                    OFFSET,
                    Component.translatable("hud.toughasnailsu.thirst.level."+((int) value)).font(font).color(ComponentU.NO_SHADOW_COLOR),
                    SIZE
            );
        }
    }
}
