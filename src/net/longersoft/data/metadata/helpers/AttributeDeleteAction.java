package net.longersoft.data.metadata.helpers;

import java.sql.PreparedStatement;

import net.longersoft.data.SqlHelper;
import net.longersoft.data.metadata.bll.AttributeMetadata;
import net.longersoft.data.metadata.bll.EntityMetadata;

public class AttributeDeleteAction extends ActionBase {
	private AttributeMetadata attributeInfo;
	private EntityMetadata entityInfo;

	public AttributeDeleteAction(AttributeMetadata attributeInfo, MetadataHelper helper) {
		super(ActionType.Delete, MetadataObjectType.Attribute, attributeInfo.get_AttributeId(), helper);
		
		this.attributeInfo = attributeInfo;
	}
	
	@Override
	public void retrieveDataForOperations() throws Exception {
		super.retrieveDataForOperations();
		
		this.attributeInfo.fillIt(context, "attributeName", "entityId", "attributeTypeId");
		this.entityInfo = new EntityMetadata();
		this.entityInfo.set_EntityId(this.attributeInfo.get_EntityId());
		this.entityInfo.fillIt(context, "entityName", "tableName");
	}
	
	@Override
	public void doMetadataOperation() throws Exception {
		try{
			super.doMetadataOperation();
			this.attributeInfo.deleteIt(this.context);
		}catch(Exception e){}
	}
	
	@Override
	public void doDatabaseOperation() throws Exception {
		try{
			super.doDatabaseOperation();
			String sql = String.format("alter table %s drop column %s", 
					this.entityInfo.get_TableName(), 
					this.attributeInfo.get_AttributeName());
			
			PreparedStatement statement = this.context.getConnection().prepareStatement(super.modifySql(sql));
			SqlHelper.execute(statement);
		}catch(Exception e){}
	}

}
