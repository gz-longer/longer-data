package net.longersoft.data.metadata.helpers;

import java.sql.PreparedStatement;

import net.longersoft.data.SqlHelper;
import net.longersoft.data.metadata.bll.AttributeMetadata;
import net.longersoft.data.metadata.bll.EntityMetadata;
import net.longersoft.data.metadata.services.AttributeTypeInfo;
import net.longersoft.data.metadata.services.AttributeTypes;

public class AttributeCreateAction extends ActionBase {
	private EntityMetadata entityInfo;
	private AttributeMetadata attributeInfo;

	public AttributeCreateAction(AttributeMetadata attributeInfo, MetadataHelper helper) {
		super(ActionType.Create, MetadataObjectType.Attribute, attributeInfo.get_AttributeId(), helper);
		
		this.setAttributeInfo(attributeInfo);
	}

	public void setAttributeInfo(AttributeMetadata attributeInfo) {
		this.attributeInfo = attributeInfo;
	}

	public AttributeMetadata getAttributeInfo() {
		return attributeInfo;
	}
	
	@Override
	public void retrieveDataForOperations() throws Exception {
		super.retrieveDataForOperations();
		
		this.entityInfo = new EntityMetadata();
		this.entityInfo.set_EntityId(this.attributeInfo.get_EntityId());
		this.entityInfo.fillIt(this.context, "entityName", "tableName");
	}

	@Override
	public void doMetadataOperation() throws Exception {
		super.doMetadataOperation();
		
		this.attributeInfo.createIt(this.context);
	}
	
	@Override
	public void doDatabaseOperation() throws Exception {
		super.doDatabaseOperation();
		
		String tableName = this.entityInfo.get_TableName();
		if(tableName.startsWith("metadata_")) return;
		
		AttributeTypeInfo typeInfo = AttributeTypes.getAttributeTypeInfo(attributeInfo.get_AttributeType());
		String sqlType = MetadataHelper.getFullSqlType(typeInfo, attributeInfo);
		String isNullable = attributeInfo.get_IsNullable() ? "null" : "not null";
		
		String sql = String.format("alter table %s add %s %s %s", 
				tableName, 
				this.attributeInfo.get_AttributeName(), 
				sqlType, isNullable);
		
		PreparedStatement statement = this.context.getConnection().prepareStatement(super.modifySql(sql));
		SqlHelper.execute(statement);
	}
}
