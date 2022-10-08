package io.xarond.ucore.scene.builders;

import io.xarond.ucore.function.FieldListenable;
import io.xarond.ucore.scene.ui.TextField;

public class field extends builder<field, TextField>{
	
	public field(String text, FieldListenable listener){
		element = new TextField(text);
		element.changed(()->{
			listener.listen(element.getText());
		});
		cell = context().add(element);
	}
	
	public field(FieldListenable listener){
		this("", listener);
	}
}
