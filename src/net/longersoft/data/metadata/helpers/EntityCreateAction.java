package net.longersoft.data.metadata.helpers;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import net.longersoft.data.SqlHelper;
import net.longersoft.data.metadata.bll.AttributeMetadata;
import net.longersoft.data.metadata.bll.EntityMetadata;
import net.longersoft.data.metadata.services.AttributeTypeInfo;
import net.longersoft.data.metadata.services.AttributeTypes;
import net.longersoft.helpers.Throw;

public class EntityCreateAction extends ActionBase {
	private EntityMetadata entityInfo;
	private List<ActionBase> actions = new ArrayList<ActionBase>();
	
	private static Logger log = Logger.getLogger(EntityCreateAction.class);
	
	public EntityCreateAction(EntityMetadata bag, MetadataHelper helper) {
		super(ActionType.Create, MetadataObjectType.Entity, bag.get_EntityId(), helper);
		this.entityInfo = bag;
		this.helper = helper;
	}
	
	public void addAttributeAction(ActionBase action){
		this.actions.add(action);
	}
	
	@Override
	public void doMetadataOperation() throws Exception {
		super.doMetadataOperation();
		
		this.entityInfo.createIt(this.context);
//		MetadataEntity metadataEntity = new MetadataEntity();
		
		
		for(ActionBase action : this.getActions()){
			action.doMetadataOperation();
		}
	}
	
	@Override
	public void doDatabaseOperation() throws Exception {
		super.doDatabaseOperation();
		
		// the metadata_* tables are already created when initializing database
		String tableName = this.entityInfo.get_TableName();
		if(tableName.startsWith("metadata_")) return;
		
		StringBuffer builder = new StringBuffer();
		builder.append(String.format("create table %s (\n", tableName));
		Boolean first = true;
		for(ActionBase action : this.actions){
			AttributeMetadata attributeInfo = ((AttributeCreateAction)action).getAttributeInfo();
			AttributeTypeInfo typeInfo = AttributeTypes.getAttributeTypeInfo(attributeInfo.get_AttributeType());
			
			String sqlType = MetadataHelper.getFullSqlType(typeInfo, attributeInfo);
			String isNullable = attributeInfo.get_IsNullable() ? "null" : "not null";
			String sqlDefault =  (attributeInfo.get_DefaultValue() == null ) ? "" 
					: String.format("default '%s'", attributeInfo.get_DefaultValue());
			String sqlPk = attributeInfo.get_IsPrimaryKey() ? "primary key" : "";
			
			if(!first) builder.append(", \n");
			builder.append(String.format("%s %s %s %s %s", 
					attributeInfo.get_AttributeName(), 
					sqlType, isNullable, sqlDefault, sqlPk));
			
			first = false;
		}
		builder.append(")");
		String sql = super.modifySql(builder.toString());
		
		PreparedStatement statement = this.helper.getContext().getConnection().prepareStatement(sql);
		SqlHelper.execute(statement);
	}
	
	@Override
	public void validate() throws Exception {
		super.validate();
		
		Throw.ifParameterEmpty(this.entityInfo.get_EntityName(), "name");
		Throw.ifParameterEmpty(this.entityInfo.get_EntityId(), "entityId");
	}

	private void setActions(List<ActionBase> actions) {
		this.actions = actions;
	}

	public List<ActionBase> getActions() {
		return actions;
	}
}
