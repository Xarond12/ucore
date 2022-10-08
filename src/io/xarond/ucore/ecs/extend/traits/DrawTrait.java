package io.xarond.ucore.ecs.extend.traits;

import io.xarond.ucore.ecs.Spark;
import io.xarond.ucore.ecs.Trait;
import io.xarond.ucore.function.Consumer;

public class DrawTrait extends Trait{
	public Consumer<Spark> drawer;
	
	public DrawTrait(Consumer<Spark> drawer){
		this.drawer = drawer;
	}
	
	//no-arg constructor for things like Kryo/JSON
	private DrawTrait(){}
}
