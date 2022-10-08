package io.xarond.ucore.ecs.extend.traits;

import io.xarond.ucore.ecs.Prototype;
import io.xarond.ucore.ecs.Trait;
import io.xarond.ucore.ecs.extend.Events.Collision;
import io.xarond.ucore.ecs.extend.Events.Damaged;

public class ContactDamageTrait extends Trait{
	public int damage;
	
	public ContactDamageTrait(){
		
	}

	public ContactDamageTrait(int damage){
		this.damage = damage;
	}
	
	@Override
	public void registerEvents(Prototype type){
		
		type.traitEvent(Collision.class, (spark, other)->{
			
			if(other.has(HealthTrait.class)){
				int damage = spark.get(ContactDamageTrait.class).damage;
				
				other.getType().callEvent(Damaged.class, other, spark, damage);
			}
		});
	}
	
}
