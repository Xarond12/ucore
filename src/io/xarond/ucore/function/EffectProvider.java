package io.xarond.ucore.function;

import com.badlogic.gdx.graphics.Color;

import io.xarond.ucore.core.Effects.Effect;

public interface EffectProvider{
	public void createEffect(Effect effect, Color color, float x, float y, float rotation);
}
