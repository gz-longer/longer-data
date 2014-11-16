package net.longersoft.data.metadata.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.longersoft.data.metadata.bll.EntityMetadata;

public class EntityCreateInfo {
	private EntityMetadata bag;
	private AttributeInfo primaryField = null;
	private AttributeInfo primaryKey = null;
	private List<AttributeInfo> normalFields = new ArrayList<AttributeInfo>();
	
	public EntityCreateInfo(){
		this.bag = new EntityMetadata();
		this.bag.init();
		this.bag.set_EntityId(UUID.randomUUID().toString());
	}

	public void setBag(EntityMetadata bag) {
		this.bag = bag;
	}

	public EntityMetadata getBag() {
		return bag;
	}

	public void setPrimaryField(AttributeInfo primaryField) {
		this.primaryField = primaryField;
	}

	public AttributeInfo getPrimaryField() {
		return primaryField;
	}

	public List<AttributeInfo> getNormalFields() {
		return normalFields;
	}

	public void setPrimaryKey(AttributeInfo primaryKey) {
		this.primaryKey = primaryKey;
	}

	public AttributeInfo getPrimaryKey() {
		return primaryKey;
	}
}
