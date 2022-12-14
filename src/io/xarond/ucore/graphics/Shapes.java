package io.xarond.ucore.graphics;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import io.xarond.ucore.util.Mathf;

public class Shapes {
    private static Vector2 v = new Vector2();

    public static void laser(String line, String edge, float x, float y, float x2, float y2, float scale) {
        laser(line, edge, x, y, x2, y2, Mathf.atan2(x2 - x, y2 - y), scale);
    }

    public static void laser(String line, String edge, float x, float y, float x2, float y2) {
        laser(line, edge, x, y, x2, y2, Mathf.atan2(x2 - x, y2 - y), 1f);
    }

    public static void laser(String line, String edge, float x, float y, float x2, float y2, float rotation, float scale) {

        Lines.stroke(12f * scale);
        Lines.line(Draw.region(line), x, y, x2, y2, CapStyle.none, 0f);
        Lines.stroke(1f);

        TextureRegion region = Draw.region(edge);

        Draw.rect(edge, x, y, region.getRegionWidth(), region.getRegionHeight() * scale, rotation + 180);

        Draw.rect(edge, x2, y2, region.getRegionWidth(), region.getRegionHeight() * scale, rotation);
    }

    public static void lineShot(float x, float y, float angle, int amount, float fract, float len, float thick, float falloff) {
        float length = len;
        float thickness = thick;
        for (int i = 0; i < amount; i++) {
            Lines.stroke(fract * thickness);
            Lines.lineAngle(x, y, angle, fract * length);
            length *= falloff;
            thickness /= falloff;
        }
    }

    public static void lineShotFade(float x, float y, float angle, int amount, float fract, float len, float thick, float falloff, float thickadd) {
        float length = len;
        float thickness = thick;
        for (int i = 0; i < amount; i++) {
            Lines.stroke(fract * thickness);
            Lines.lineAngle(x, y, angle, length);
            length *= falloff;
            thickness += thickadd;
        }
    }
}
