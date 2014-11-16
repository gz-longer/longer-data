package net.longersoft.data.metadata.dal;

import net.longersoft.data.EntityRow;
import net.longersoft.data.database.annotations.*;

@Entity(_MetadataEntityRow.EntityName)
@DisplayName(_MetadataEntityRow.DisplayName)
public class _MetadataEntityRow extends EntityRow {
	public static final String EntityName = "metadata_entity";
	public static final String DisplayName = "实体";
	
	@PrimaryKey
	private String entityId;
	@PrimaryField
	@DisplayName("实体名称")
	private String entityName;
	@Attribute
	@DisplayName("表名称")
	@RequiredLevel(RequiredLevel.Required)
	private String tableName;
	@Attribute
	@DisplayName("视图名称")
	private String viewName;
	@Attribute
	@DisplayName("显示名称")
	private String displayName;
	@ForeinKey(_MetadataModuleRow.EntityName)
	@DisplayName("所属模块")
	private String moduleId;

	public _MetadataEntityRow() {
		super(EntityName);
	}

	public void set_EntityId(String entityId) {
		this.setAttributeValue("entityId", entityId);
	}

	public String get_EntityId() {
		return (String)this.getAttributeValue("entityId");
	}

	public void set_EntityName(String entityName) {
		this.setAttributeValue("entityName", entityName);
	}

	public String get_EntityName() {
		return (String)this.getAttributeValue("entityName");
	}

	public void set_TableName(String tableName) {
		this.setAttributeValue("tableName", tableName);
	}

	public String get_TableName() {
		return (String)this.getAttributeValue("tableName");
	}

	public void set_ViewName(String viewName) {
		this.setAttributeValue("viewName", viewName);
	}

	public String get_ViewName() {
		return (String)this.getAttributeValue("viewName");
	}
	
	public String get_DisplayName(){
		return (String)this.getAttributeValue("displayName");
	}
	
	public void set_DisplayName(String displayName){
		this.setAttributeValue("displayName", displayName);
	}

	public void set_Module(String moduleId, String moduleName) {
		this.setAttributeValue("moduleId", moduleId);
		this.setAttributeValue("moduleId_name", moduleName);
	}

	public String get_ModuleId() {
		return (String)this.getAttributeValue("moduleId");
	}
	
	public String get_ModuleId_name(){
		return (String)this.getAttributeValue("moduleId_name");
	}
	
	@Override
	public void init() {
		
	}
}
