package io.xarond.ucore.util;

import com.badlogic.gdx.math.Vector2;

public class Translator extends Vector2{

    public Translator trns(float angle, float amount){
        if(amount < 0) angle += 180f;
        set(amount, 0).rotate(angle);

        return this;
    }

    public Translator trns(float angle, float x, float y){
        set(x, y).rotate(angle);

        return this;
    }

    public Translator rnd(float length){
        setToRandomDirection().scl(length);
        return this;
    }

}
