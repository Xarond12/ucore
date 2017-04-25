package io.anuke.ucore.entities;

import java.util.HashMap;
import java.util.function.Consumer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntSet;
import com.badlogic.gdx.utils.ObjectSet;

import io.anuke.ucore.util.Mathf;
import io.anuke.ucore.util.QuadTree;
import io.anuke.ucore.util.QuadTree.QuadTreeObject;

public class Entities{
	private static HashMap<Long, Entity> entities = new HashMap<Long, Entity>();
	protected static ObjectSet<Long> entitiesToRemove = new ObjectSet<Long>();
	protected static ObjectSet<Entity> entitiesToAdd = new ObjectSet<Entity>();
	
	public static QuadTree<SolidEntity> tree;
	public static boolean physics = false;
	
	private static IntSet collided = new IntSet();
	
	public static void initPhysics(float x, float y, float w, float h){
		tree = new QuadTree(4, new Rectangle(x, y, w, h));
		physics = true;
	}
	
	public static void resizeTree(float x, float y, float w, float h){
		initPhysics(x, y, w, h);
	}
	
	public static void getNearby(Rectangle rect, Consumer<SolidEntity> out){
		tree.getMaybeIntersecting(out, rect);
	}
	
	public static void getNearby(float x, float y, float size, Consumer<SolidEntity> out){
		tree.getMaybeIntersecting(out, Rectangle.tmp.setSize(size).setCenter(x, y));
	}
	
	public static void clear(){
		entitiesToAdd.clear();
		entities.clear();
		entitiesToRemove.clear();
	}
	
	public static Iterable<Entity> all(){
		return entities.values();
	}
	
	public static Entity get(long id){
		return entities.get(id);
	}
	
	private static void updatePhysics(){
		collided.clear();
		
		tree.clear();

		for(Entity entity : all()){
			if(entity instanceof SolidEntity)
			tree.insert((SolidEntity)entity);
		}
		
		for(Entity entity : all()){
			if(!(entity instanceof SolidEntity)) continue;
			if(collided.contains((int)entity.id)) continue;
				
			((QuadTreeObject)entity).getBoundingBox(Rectangle.tmp);
			
			tree.getMaybeIntersecting(c->{
				if(!collided.contains((int)c.id))
						checkCollide(entity, c);
			}, Rectangle.tmp);
			
			collided.add((int)entity.id);
		}
	}
	
	private static boolean checkCollide(Entity entity, Entity other){
		SolidEntity a = (SolidEntity) entity;
		SolidEntity b = (SolidEntity) other;
		
		if(a.collides(b) 
				&& b.collides(a)
				 && Mathf.intersect(entity.x, entity.y, a.hitsize/2, other.x, other.y, b.hitsize/2)){
			a.collision(b);
			b.collision(a);
			return true;
		}
		
		return false;
	}
	
	public static void update(){
		update(true);
	}
	
	public static void update(boolean update){
		Entity.delta = Gdx.graphics.getDeltaTime() * 60f;
		
		if(physics)
			updatePhysics();
		
		if(update)
		for(Entity e : entities.values()){
			e.update();
		}

		for(Long l : entitiesToRemove){
			entities.remove(l);
			
		}
		entitiesToRemove.clear();

		for(Entity e : entitiesToAdd){
			entities.put(e.id, e);
			e.added();
		}
		entitiesToAdd.clear();
	}
}
