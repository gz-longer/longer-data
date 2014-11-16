package net.longersoft.data.metadata.helpers;

import java.util.List;

import net.longersoft.data.context.ExecutionContext;
import net.longersoft.data.metadata.bll.AttributeMetadata;
import net.longersoft.data.metadata.bll.EntityMetadata;
import net.longersoft.data.metadata.bll.RelationshipMetadata;
import net.longersoft.data.metadata.services.AttributeTypeInfo;
import net.longersoft.data.metadata.services.EntityUpdateInfo;
import net.longersoft.helpers.Throw;

public class MetadataHelper {
	private ActionDictionary actionsToExecute = null;
	ActionDictionary allActions = null;
	private ExecutionContext context = null; 
	
	public MetadataHelper(ExecutionContext context){
		this.actionsToExecute = new ActionDictionary();
		this.allActions = new ActionDictionary();
		this.setContext(context);
	}
	
	public void addToQueue(EntityMetadata entityBag, ActionType action) throws Exception {
		Throw.ifParameterEmpty(entityBag, "entityBag");
		EntityMetadata bag = entityBag;
		
		ActionBase newAction  = null;
		switch (action) {
		case Create:
			newAction = new EntityCreateAction(bag, this);
			break;
		case Update:
			newAction = new EntityUpdateAction(bag, this);
			break;
		case Delete:
			newAction = new EntityDeleteAction(bag, this);
			break;
			
		default:
			break;
		}
		
		newAction.validate();
		newAction.injectDefaults();
		ActionKey actionKey = new ActionKey(MetadataObjectType.Entity, action, bag.get_EntityId());
		this.actionsToExecute.put(actionKey, newAction);
	}


	public void addToQueue(AttributeMetadata attributeInfo, ActionType actionType) throws Exception {
		AttributeMetadata bag = attributeInfo;
		
		ActionBase newAction = null;
		switch(actionType){
		case Create:
			newAction = new AttributeCreateAction(bag, this);
			ActionBase entityCreateActionBase = this.allActions.tryGetAction(MetadataObjectType.Entity, ActionType.Create, bag.get_EntityId());
			if( entityCreateActionBase != null){
				((EntityCreateAction)entityCreateActionBase).addAttributeAction(newAction);
				return;
			}
			break;
		case Update:
			newAction = new AttributeUpdateAction(bag, this);
			break;
		case Delete:
			newAction = new AttributeDeleteAction(bag, this);
			break;
		}
		
		if( newAction != null){
			newAction.validate();
			newAction.injectDefaults();
			ActionKey actionKey = new ActionKey(MetadataObjectType.Attribute, actionType, bag.get_AttributeId());
			this.actionsToExecute.put(actionKey, newAction);
		}
	}

	public void addToQueue(RelationshipMetadata relationshipInfo, ActionType actionType) throws Exception {
		RelationshipMetadata bag = relationshipInfo;
		
		ActionBase newAction = null;
		switch(actionType){
		case Create:
			newAction = new RelationshipCreateAction(bag, this);
			break;
		case Update:
			newAction = new RelationshipUpdateAction(bag, this);
			break;
		case Delete:
			newAction = new RelationshipDeleteAction(bag, this);
			break;
		}
		
		if( newAction != null){
			newAction.validate();
			newAction.injectDefaults();
			ActionKey actionKey = new ActionKey(MetadataObjectType.Relationship, actionType, bag.get_RelationshipId());
			this.actionsToExecute.put(actionKey, newAction);
		}	
	}
	
	public void processQueue() throws Exception {
		if( !this.actionsToExecute.isEmpty()){
			List<ActionBase> actions = this.actionsToExecute.values();
			for(ActionBase action : actions){
				action.retrieveDataForOperations();
			}
			
			for(ActionBase action : actions){
				action.doMetadataOperation();
			}
			
			for(ActionBase action : actions){
				action.doDatabaseOperation();
			}
			
			for(ActionBase action : actions){
				action.doViewOperation();
			}
		}
		
		this.actionsToExecute.clear();
		this.allActions.clear();
	}

	private void setContext(ExecutionContext context) {
		this.context = context;
	}

	public ExecutionContext getContext() {
		return context;
	}

	public static String getFullSqlType(AttributeTypeInfo typeInfo, AttributeMetadata attributeInfo) {
		return ExecutionContext.getSqlProvider().getFullSqlType(typeInfo, attributeInfo);
	}

	public static String getEntityPrimaryKeyName(String entityName){
		return simpleName(entityName) + "id".toLowerCase();
	}
	
	public static String getEntityPrimaryFieldName(String entityName){
		return simpleName(entityName) + "name".toLowerCase();
	}
	
	public static String getEntityParentFieldName(String entityName){
		return simpleName(entityName) + "pid".toLowerCase();
	}
	
	static String simpleName(String name){
		return name.replaceAll("^\\w*_", "");
	}
}
