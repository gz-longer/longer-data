package net.longersoft.data.database.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.longersoft.data.database.annotations.Entity;
import net.longersoft.framework.LongerModule;
import net.longersoft.framework.LongerWebService;

public class DatabaseService extends LongerWebService {
	public void create(LongerModule module, List<Class<?>> classes) throws Exception{
		List<EntityCreateHandler> list = new ArrayList<EntityCreateHandler>();
		
		for(Class<?> clazz : classes){
			Entity aEntity = (Entity) clazz.getAnnotation(Entity.class);
			if(aEntity == null) continue;
			list.add(new EntityCreateHandler(module, clazz));
		}
		
		for(EntityCreateHandler handler : list){
			handler.initialize();
		}
		
		for(EntityCreateHandler handler : list){
			handler.createEntity();
		}
		
		for(EntityCreateHandler handler : list){
			handler.createRelationship();
		}
	}
	
	

	public void delete(LongerModule module, List<Class<?>> classes) throws Exception {
		List<EntityDeleteHandler> list = new ArrayList<EntityDeleteHandler>();
		for(Class<?> clazz : classes){
			Entity aEntity = (Entity) clazz.getAnnotation(Entity.class);
			if(aEntity == null) continue;
			list.add(new EntityDeleteHandler(module, clazz));
		}
		
		
		for(EntityDeleteHandler handler : list){
			try{ handler.initialize(); } catch(Exception e){}
		}
		
		for(EntityDeleteHandler handler : list){
			try{ handler.deleteRelationship(); } catch(Exception e){}
		}		
		
		for(EntityDeleteHandler handler : list){
			try{ handler.deleteEntity(); } catch(Exception e){}
		}
		
	}

	public void update(LongerModule module, List<Class<?>> classes) {
		List<EntityUpdateHandler> list = new ArrayList<EntityUpdateHandler>();
		for(Class<?> clazz : classes){
			Entity aEntity = (Entity) clazz.getAnnotation(Entity.class);
			if(aEntity == null) continue;
			list.add(new EntityUpdateHandler(module, clazz));
		}
		
		for(EntityUpdateHandler handler : list){
			try{ handler.initialize(); } catch(Exception e){}
		}
		
		for(EntityUpdateHandler handler : list){
			try{ handler.updateEntity(); } catch(Exception e){}
		}
	}

}
