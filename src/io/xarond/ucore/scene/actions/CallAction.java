package io.xarond.ucore.scene.actions;

import io.xarond.ucore.function.Callable;
import io.xarond.ucore.scene.Action;

public class CallAction extends Action{
    public Callable call;

    @Override
    public boolean act(float delta) {
        call.run();
        return true;
    }
}
