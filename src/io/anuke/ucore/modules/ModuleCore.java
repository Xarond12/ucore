package io.anuke.ucore.modules;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;

import io.anuke.ucore.core.Draw;
import io.anuke.ucore.graphics.Hue;

public abstract class ModuleCore<T extends ModuleCore<T>> extends ApplicationAdapter{
	private static ModuleCore<?> instance;
	protected ObjectMap<Class<? extends Module<T>>, Module<T>> modules = new ObjectMap<Class<? extends Module<T>>, Module<T>>();
	protected Array<Module<T>> modulearray = new Array<Module<T>>();
	
	{
		instance=this; 
		Hue.init();
	}
	
	abstract public void init();
	public void preInit(){}
	public void postInit(){}
	public void update(){}
	
	public <N extends Module<T>> void add(N t){
		addModule(t);
	}
	
	public void addModule(Class<? extends Module<T>> c){
		try{
			Module<T> m = ClassReflection.newInstance(c);
			m.main = (T)this;
			modules.put(c, m);
			modulearray.add(m);
			m.preInit();
		}catch (RuntimeException e){
			throw e;
		}catch (Exception e){
			e.printStackTrace();
			Gdx.app.exit();
		}
	}
	
	public <N extends Module<T>> void addModule(N t){
		try{
			modules.put((Class<? extends Module<T>>) t.getClass(), t);
			t.main = (T)this;
			modulearray.add(t);
			t.preInit();
		}catch (RuntimeException e){
			throw e;
		}
	}
	
	public <N> N getModule(Class<N> c){
		return (N)(modules.get((Class<? extends Module<T>>)c));
	}
	
	public <N> N get(Class<N> c){
		return (N)(modules.get((Class<? extends Module<T>>)c));
	}
	
	public static  <N> N module(Class<N> c){
		return (N)(instance.getModule(c));
	}
	
	@Override
	public void resize(int width, int height){
		Module.screen.set(width, height);
		Draw.resize();
		for(Module<T> module : modulearray){
			module.resize(width, height);
		}
	}
	
	@Override
	public final void create(){
		init();
		preInit();
		for(Module<T> module : modulearray){
			module.init();
		}
		postInit();
	}
	
	@Override
	public void render(){
		for(Module<T> module : modulearray){
			module.update();
		}
		
		update();
	}
	
	@Override
	public void pause(){
		for(Module<T> module : modulearray)
			module.pause();
	}
	
	@Override
	public void resume(){
		for(Module<T> module : modulearray)
			module.resume();
	}
	
	@Override
	public void dispose(){
		for(Module<T> module : modulearray)
			module.dispose();
	}
}