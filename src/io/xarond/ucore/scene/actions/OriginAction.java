package io.xarond.ucore.scene.actions;

import com.badlogic.gdx.utils.Align;

import io.xarond.ucore.scene.Action;

public class OriginAction extends Action{

	@Override
	public boolean act(float delta){
		actor.setOrigin(Align.center);
		return true;
	}

}
