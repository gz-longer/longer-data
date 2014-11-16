package net.longersoft.data.metadata.helpers;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class ActionDictionary extends ArrayList<KeyAction> {
	private static final long serialVersionUID = 1L;
	
	public ActionBase tryGetAction(MetadataObjectType objectType, ActionType actionType, String objectId){
		ActionKey key = new ActionKey(objectType, actionType, objectId);

		for(KeyAction ka : this){
			if(ka.getKey().equals(key)){
				return ka.getAction();
			}
		}
		return null;
	}
	
	public void put(ActionKey key, ActionBase action){
		this.add(new KeyAction(key, action));
	}
	
	public List<ActionBase> values(){
		ArrayList<ActionBase> list = new ArrayList<ActionBase>();
		
		for(KeyAction ka : this){
			list.add(ka.getAction());
		}
		
		return list;
	}
}

class KeyAction{
	private ActionKey key;
	private ActionBase action;
	
	public KeyAction(ActionKey key, ActionBase action){
		this.setKey(key);
		this.setAction(action);
	}
	
	public void setKey(ActionKey key) {
		this.key = key;
	}
	public ActionKey getKey() {
		return key;
	}
	public void setAction(ActionBase action) {
		this.action = action;
	}
	public ActionBase getAction() {
		return action;
	}
	
	
}