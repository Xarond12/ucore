package io.xarond.ucore.ecs.extend.processors;

import io.xarond.ucore.ecs.Spark;
import io.xarond.ucore.ecs.TraitProcessor;
import io.xarond.ucore.ecs.extend.traits.DrawTrait;

public class DrawProcessor extends TraitProcessor{
	
	public DrawProcessor(){
		super(DrawTrait.class);
	}

	@Override
	public void update(Spark spark){
		spark.get(DrawTrait.class).drawer.accept(spark);
	}
}
