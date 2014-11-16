package net.longersoft.data.metadata.helpers;

import net.longersoft.data.metadata.bll.AttributeMetadata;

public class AttributeUpdateAction extends ActionBase {
	private AttributeMetadata attributeInfo;
	
	public AttributeUpdateAction(AttributeMetadata attributeInfo, MetadataHelper helper) {
		super(ActionType.Update, MetadataObjectType.Attribute, attributeInfo.get_AttributeId(), helper);

		this.attributeInfo = attributeInfo;
	}
	
	@Override
	public void doMetadataOperation() throws Exception {
		super.doMetadataOperation();
		
		this.attributeInfo.updateIt(this.context);
	}
}
