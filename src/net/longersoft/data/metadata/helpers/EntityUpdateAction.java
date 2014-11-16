package net.longersoft.data.metadata.helpers;

import net.longersoft.data.metadata.bll.EntityMetadata;

public class EntityUpdateAction extends ActionBase {
	private EntityMetadata entityInfo;
	
	public EntityUpdateAction(EntityMetadata entityInfo, MetadataHelper helper) {
		super(ActionType.Update, MetadataObjectType.Entity, entityInfo.get_EntityId(), helper);
		
		this.entityInfo = entityInfo;
	}
	
	@Override
	public void doMetadataOperation() throws Exception {
		super.doMetadataOperation();
		
		this.entityInfo.updateIt(super.context);
	}

}
