package dev.elijuh.fishing.utils;

/**
 * @author elijuh
 */
public class HSVColor {
    private float h;
    private final float s, v;

    public HSVColor(float h, float s, float v) {
        this.h = h;
        this.s = s;
        this.v = v;
    }

    public static HSVColor fromRGB(int red, int green, int blue) {
        float r = red / 255f;
        float g = green / 255f;
        float b = blue / 255f;
        float min = Math.min(r, Math.min(g, b));
        float max = Math.max(r, Math.max(g, b));
        float delta = max - min;
        float s = max == 0f ? 0f : delta / max;

        if (s == 0.0F) {
            return new HSVColor(0f, s, max);
        } else {
            float h;
            if (r == max) {
                h = (g - b) / delta;
            } else if (g == max) {
                h = 2f + (b - r) / delta;
            } else {
                h = 4f + (r - g) / delta;
            }

            h *= 60f;
            if (h < 0f) {
                h += 360f;
            }

            return new HSVColor(h / 360f, s, max);
        }
    }

    public void progressHue(float percentage) {
        this.h += percentage;
        if (this.h > 1f || this.h < 0f) {
            this.h = Math.abs(this.h % 1f);
        }
    }

    private int color(float r, float g, float b) {
        return ((int)(r * 255f) << 16) + ((int)(g * 255f) << 8) + (int)(b * 255f);
    }

    public int toRGB() {
        float s = this.s;
        float v = this.v;
        if (s == 0f) return color(v, v, v);

        float h = this.h * 6f;
        int i = (int) Math.floor(h);
        float f = h - (float) i;
        float p = v * (1f - s);
        float q = v * (1f - s * f);
        float t = v * (1f - s * (1f - f));
        switch (i) {
            case 0: return color(v, t, p);
            case 1: return color(q, v, p);
            case 2: return color(p, v, t);
            case 3: return color(p, q, v);
            case 4: return color(t, p, v);
            default: return color(v, p, q);
        }
    }
}