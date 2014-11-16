package net.longersoft.data.metadata.services;

import java.util.UUID;
import net.longersoft.data.metadata.bll.AttributeMetadata;

public class AttributeInfo {
	private AttributeMetadata bag = null;
	
	public AttributeInfo(AttributeType attributeType) {
		this.bag = new AttributeMetadata();
		this.bag.init();
		this.bag.set_AttributeId(UUID.randomUUID().toString());
		this.bag.set_AttributeType(attributeType);
		if(attributeType == AttributeType.String ){
			this.bag.set_Length(256);
		}else if(attributeType == AttributeType.PrimaryField){
			this.bag.set_Length(256);
			this.bag.set_IsPrimaryAttribute(true);
		}else if(attributeType == AttributeType.ParentField){
			this.bag.set_Length(50);
		}else if(attributeType == AttributeType.PrimaryKey){
			this.getBag().set_IsPrimaryKey(true);
			this.getBag().set_IsNullable(false);
		}else if(attributeType == AttributeType.Lookup){
			this.getBag().set_IsLookupAttribute(true);
		}else if(attributeType == AttributeType.Boolean){
			this.getBag().set_Length(1);
		}else if(attributeType == AttributeType.Integer){
			this.getBag().set_Length(8);
		}
	}

	public AttributeInfo(AttributeMetadata bag){
		this.setBag(bag);
		this.bag.set_AttributeId(UUID.randomUUID().toString());
	}
	
	public void setBag(AttributeMetadata bag) {
		this.bag = bag;
	}

	public AttributeMetadata getBag() {
		return bag;
	}

	public void setForUpdate(AttributeMetadata updateAttribute) {
		AttributeMetadata attribute = this.getBag();
		if(attribute.getAttributeValue("requiredLevel") != null){
			updateAttribute.set_RequiredLevel(attribute.get_RequiredLevel());
		}
		if(attribute.getAttributeValue("imeMode") != null){
			updateAttribute.set_ImeMode(attribute.get_ImeMode());
		}
		if(attribute.getAttributeValue("displayName") != null){
			updateAttribute.set_DisplayName(attribute.get_DisplayName());
		}
	}
}
