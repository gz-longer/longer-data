package net.longersoft.data.metadata.dal;

import net.longersoft.data.EntityRow;
import net.longersoft.data.database.annotations.*;

@Entity(_MetadataEntityRuleRow.EntityName)
@DisplayName(_MetadataEntityRuleRow.DisplayName)
public class _MetadataEntityRuleRow extends EntityRow {
	public static final String EntityName = "metadata_entityrule";
	public static final String DisplayName = "规则";
	
	@PrimaryKey
	private String entityruleId;
	@PrimaryField
	@DisplayName("规则名称")
	@RequiredLevel(RequiredLevel.Required)
	private String entityruleName;
	@ForeinKey(_MetadataEntityRow.EntityName)
	@DisplayName("所属实体")
	@RequiredLevel(RequiredLevel.Required)
	private String entityId;
	
	public _MetadataEntityRuleRow() {
		super(EntityName);
	}

	public void set_EntityruleId(String entityruleId) {
		this.setAttributeValue("entityruleId", entityruleId);
	}

	public String get_EntityruleId() {
		return (String)this.getAttributeValue("entityruleId");
	}

	public void set_EntityruleName(String entityruleName) {
		this.setAttributeValue("entityruleName", entityruleName);
	}

	public String get_EntityruleName() {
		return (String)this.getAttributeValue("entityruleName");
	}

	public void set_Entity(String entityId, String entityName) {
		this.setAttributeValue("entityId", entityId);
		this.setAttributeValue("entityId_name", entityName);
	}

	public String get_EntityId() {
		return (String)this.getAttributeValue("entityId");
	}

	public String get_EntityId_name() {
		return (String)this.getAttributeValue("entityId_name");
	} 
}
