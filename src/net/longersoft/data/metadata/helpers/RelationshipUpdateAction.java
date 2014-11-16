package net.longersoft.data.metadata.helpers;

import net.longersoft.data.metadata.bll.RelationshipMetadata;

public class RelationshipUpdateAction extends ActionBase {
	private RelationshipMetadata relationshipInfo;
	
	public RelationshipUpdateAction(RelationshipMetadata relationshipInfo, MetadataHelper helper) {
		super(ActionType.Update, MetadataObjectType.Relationship, relationshipInfo.get_RelationshipId(), helper);
		
		this.relationshipInfo = relationshipInfo;
	}

	@Override
	public void doMetadataOperation() throws Exception {
		super.doMetadataOperation();
		
		this.relationshipInfo.updateIt(super.context);
	}
}
