package io.anuke.ucore.ecs;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import io.anuke.ucore.ecs.extend.traits.*;

public class Spark{
	private static int lastid;
	
	private int id;
	private Basis basis;
	
	private final Prototype type;
	private Array<Trait> traitlist;
	private ObjectMap<Class<? extends Trait>, Trait> traitmap = new ObjectMap<>();
	
	//cached pos trait, since it's used so much anyway
	private PosTrait pos;
	
	/**Only to be used for things like serialization. Uses the provided list of traits, instead of the prototype traits.*/
	public static Spark createCustom(Prototype type, Array<Trait> traits){
		Spark spark = new Spark(type, traits);
		return spark;
	}
	
	public Spark(Prototype type){
		this(type, type.traits().asArray());
	}
	
	//internal use only
	private Spark(Prototype type, Array<Trait> traits){
		this.type = type;
		id = lastid++;
		
		traitlist = traits;
		for(Trait t : traitlist){
			t.init(this);
			traitmap.put(t.getClass(), t);
		}
		
		type.init(this);
	}
	
	public <T extends Trait> boolean has(Class<T> c){
		return traitmap.containsKey(c);
	}
	
	public <T extends Trait> T get(Class<T> c){
		T t = (T)traitmap.get(c);
		if(t == null) throw new IllegalArgumentException("Trait type not found in spark: \"" + c + "\"");
		return t;
	}
	
	public Array<Trait> getTraits(){
		return traitlist;
	}
	
	public Spark add(){
		Basis.instance().addSpark(this);
		return this;
	}
	
	public void remove(){
		basis.removeSpark(this);
	}
	
	public int getID(){
		return id;
	}
	
	public Basis getBasis(){
		return basis;
	}
	
	protected void setBasis(Basis basis){
		this.basis = basis;
	}
	
	public Prototype getType(){
		return type;
	}
	
	public boolean isType(Prototype type){
		return this.type == type;
	}
	
	/**Resets the entity ID- do not use!*/
	public void resetID(int id){
		this.id = id;
	}
	
	//shortcut trait methods...
	
	public PosTrait pos(){
		if(pos == null)
			pos = get(PosTrait.class);
		return pos;
	}
	
	public HealthTrait health(){
		return get(HealthTrait.class);
	}
	
	public VelocityTrait velocity(){
		return get(VelocityTrait.class);
	}
	
	public LifetimeTrait life(){
		return get(LifetimeTrait.class);
	}
	
	public ProjectileTrait projectile(){
		return get(ProjectileTrait.class);
	}
}
