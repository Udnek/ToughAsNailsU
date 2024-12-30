package me.udnek.toughasnailsu.enchantment;

import me.udnek.itemscoreu.customenchantment.ConstructableCustomEnchantment;
import me.udnek.itemscoreu.customequipmentslot.CustomEquipmentSlot;
import me.udnek.toughasnailsu.attribute.Attributes;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public class Nail extends ConstructableCustomEnchantment {

    public static final int MAX_LEVEL = 4;
    public static final double MAX_RESISTANCE = 0.9;

    @Override
    public void getCustomAttributes(int level, @NotNull CustomAttributeConsumer consumer) {
        consumer.accept(Attributes.COLD_RESISTANCE, MAX_RESISTANCE/MAX_LEVEL/4d*level, AttributeModifier.Operation.ADD_NUMBER, CustomEquipmentSlot.ARMOR);
        consumer.accept(Attributes.HEAT_RESISTANCE, MAX_RESISTANCE/MAX_LEVEL/4d*level, AttributeModifier.Operation.ADD_NUMBER, CustomEquipmentSlot.ARMOR);
    }

    @Override
    public @NotNull Iterable<@NotNull Material> getSupportedItems() {
        return Tag.ITEMS_ENCHANTABLE_ARMOR.getValues();
    }

    @Override
    public @NotNull EquipmentSlotGroup[] getSlots() {
        return new EquipmentSlotGroup[]{EquipmentSlotGroup.ARMOR};
    }

    @Override
    public @Range(from = 1L, to = 255L) int getMaxLevel() {
        return MAX_LEVEL;
    }

    @Override
    public @NotNull String getRawId() {
        return "nail";
    }
}
