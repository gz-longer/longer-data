package net.longersoft.data.metadata.dal;

import net.longersoft.data.EntityRow;
import net.longersoft.data.database.annotations.*;
import net.longersoft.data.metadata.services.EntityRelationshipType;

@Entity(_MetadataRelationshipRow.EntityName)
@DisplayName(_MetadataRelationshipRow.DisplayName)
public class _MetadataRelationshipRow extends EntityRow {
	public static final String EntityName = "metadata_relationship";
	public static final String DisplayName = "关联";

	@PrimaryKey
	private String relationshipId;
	@PrimaryField
	@DisplayName("关联名称")
	private String relationshipName;
	
	@ForeinKey(_MetadataEntityRow.EntityName)
	@DisplayName("实体1")
	@RequiredLevel(RequiredLevel.Required)
	private String referencedEntityId;
	@ForeinKey(_MetadataAttributeRow.EntityName)
	@DisplayName("属性1")
	@RequiredLevel(RequiredLevel.Required)
	private String referencedAttributeId;
	
	@ForeinKey(_MetadataEntityRow.EntityName)
	@DisplayName("实体2")
	@RequiredLevel(RequiredLevel.Required)
	private String referencingEntityId;
	@ForeinKey(_MetadataAttributeRow.EntityName)
	@DisplayName("属性2")
	@RequiredLevel(RequiredLevel.Required)
	private String referencingAttributeId;
	
	@Attribute
	@DisplayName("关联类型")
	@RequiredLevel(RequiredLevel.Required)
	private EntityRelationshipType entityRelationshipType;
	@Attribute
	@DisplayName("是否逻辑")
	@RequiredLevel(RequiredLevel.Required)
	private Boolean isLogical = false;
	@Attribute
	@DisplayName("显示名称")
	private String displayName;
	
	public _MetadataRelationshipRow() {
		super(EntityName);
	}
	
	public void set_RelationshipId(String relationshipId) {
		this.setAttributeValue("relationshipId", relationshipId);
	}
	public String get_RelationshipId() {
		return (String)this.getAttributeValue("relationshipId");
	}
	public void set_RelationshipName(String relationshipName) {
		this.setAttributeValue("relationshipName", relationshipName);
	}
	public String get_RelationshipName() {
		return (String)this.getAttributeValue("relationshipName");
	}
	public void set_ReferencedEntity(String referencedEntityId, String referencedEntityName) {
		this.setAttributeValue("referencedEntityId", referencedEntityId);
		this.setAttributeValue("referencedEntityId_name", referencedEntityName);
	}
	public String get_ReferencedEntityId() {
		return (String)this.getAttributeValue("referencedEntityId");
	}
	
	public String get_ReferencedEntityId_name() {
		return (String)this.getAttributeValue("referencedEntityId_name");
	}
	
	public void set_ReferencedAttribute(String referencedAttributeId, String referencedAttributeName) {
		this.setAttributeValue("referencedAttributeId", referencedAttributeId);
		this.setAttributeValue("referencedAttributeId_name", referencedAttributeName);
	}
	public String get_ReferencedAttributeId() {
		return (String)this.getAttributeValue("referencedAttributeId");
	}
	
	public String get_ReferencedAttributeId_name() {
		return (String)this.getAttributeValue("referencedAttributeId_name");
	}
	
	public void set_ReferencingEntity(String referencingEntityId, String referencingEntityName) {
		this.setAttributeValue("referencingEntityId", referencingEntityId);
		this.setAttributeValue("referencingEntityId_name", referencingEntityName);
	}
	public String get_ReferencingEntityId() {
		return (String)this.getAttributeValue("referencingEntityId");
	}
	
	public String get_ReferencingEntityId_name() {
		return (String)this.getAttributeValue("referencingEntityId_name");
	}
	
	public void set_ReferencingAttribute(String referencingAttributeId, String referencingAttributeName) {
		this.setAttributeValue("referencingAttributeId", referencingAttributeId);
		this.setAttributeValue("referencingAttributeId_name", referencingAttributeName);
	}
	public String get_ReferencingAttributeId() {
		return (String)this.getAttributeValue("referencingAttributeId");
	}
	
	public String get_ReferencingAttributeId_name() {
		return (String)this.getAttributeValue("referencingAttributeId_name");
	}
	
	
	public void set_EntityRelationshipType(EntityRelationshipType entityRelationshipType) {
		this.setAttributeValue("entityRelationshipType", entityRelationshipType.name());
	}
	public EntityRelationshipType get_EntityRelationshipType() {
		String name = (String)this.getAttributeValue("entityRelationshipType");
		return EntityRelationshipType.valueOf(EntityRelationshipType.class, name);
	}
	public void set_IsLogical(Boolean isLogical) {
		this.setAttributeValue("isLogical", isLogical);
	}
	public Boolean get_IsLogical() {
		return (Boolean)this.getAttributeValue("isLogical");
	}	
	
	public String get_DisplayName(){
		return (String)this.getAttributeValue("displayName");
	}
	
	public void set_DisplayName(String displayName){
		this.setAttributeValue("displayName", displayName);
	}	
	
	@Override
	public void init() {
		this.set_EntityRelationshipType(EntityRelationshipType.OneToMany);
		this.set_IsLogical(false);
	}
}
