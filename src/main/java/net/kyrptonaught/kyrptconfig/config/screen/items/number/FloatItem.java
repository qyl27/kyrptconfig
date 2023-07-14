package net.kyrptonaught.kyrptconfig.config.screen.items.number;

import net.minecraft.text.Text;

public class FloatItem extends NumberItem<Float> {

    public FloatItem(Text name, Float value, Float defaultValue) {
        super(name, value, defaultValue);
        setMinMax(Float.MIN_VALUE, Float.MAX_VALUE);
    }

    @Override
    public void onTyped(String s) {
        if (s.isBlank() || s.equals("-") || s.charAt(s.length() - 1) == '.') return;
        String fixed = fixInput(s);
        if (s.equals(fixed))
            value = Float.parseFloat(s);
        else
            valueEntry.setText(fixed);
    }

    public String fixInput(String input) {
        try {
            return fixInput(Float.parseFloat(input)).toString();
        } catch (NumberFormatException ignored) {
        }
        return value.toString();
    }
}