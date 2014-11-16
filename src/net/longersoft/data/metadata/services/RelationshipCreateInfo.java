package net.longersoft.data.metadata.services;

public class RelationshipCreateInfo {
	private String referencedEntityName;
	private String referencingEntityName;
	private String relationshipAttributeName;
	private EntityRelationshipType entityRelationshipType = EntityRelationshipType.OneToMany;
	private String displayName;
	
	public void setReferencedEntityName(String referencedEntityName) {
		this.referencedEntityName = referencedEntityName;
	}
	public String getReferencedEntityName() {
		return referencedEntityName;
	}
	public void setReferencingEntityName(String referencingEntityName) {
		this.referencingEntityName = referencingEntityName;
	}
	public String getReferencingEntityName() {
		return referencingEntityName;
	}
	public void setRelationshipAttributeName(String relationshipAttributeName) {
		this.relationshipAttributeName = relationshipAttributeName;
	}
	public String getRelationshipAttributeName() {
		return relationshipAttributeName;
	}
	public void setEntityRelationshipType(EntityRelationshipType entityRelationshipType) {
		this.entityRelationshipType = entityRelationshipType;
	}
	public EntityRelationshipType getEntityRelationshipType() {
		return entityRelationshipType;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getDisplayName() {
		return displayName;
	}
	
}
