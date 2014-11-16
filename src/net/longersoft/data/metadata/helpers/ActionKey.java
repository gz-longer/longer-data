package net.longersoft.data.metadata.helpers;

public class ActionKey {
	private MetadataObjectType objectType;
	private ActionType actionType;
	private String objectId;
	
	public ActionKey(MetadataObjectType objectType, ActionType actionType, String objectId){
		this.actionType = actionType;
		this.objectId = objectId;
		this.objectType = objectType;
	}
	
	private String makeString(){
		return objectId.toString() + this.objectType + this.actionType;
	}
	
	@Override
	public boolean equals(Object obj) {
		ActionKey other = (ActionKey)obj;
		return this.makeString().equals(other.makeString());
	}
	
	@Override
	public int hashCode() {
		return this.makeString().hashCode();
	}
}
