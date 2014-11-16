package net.longersoft.data.database.services;

import net.longersoft.data.metadata.bll.AttributeMetadata;
import net.longersoft.data.metadata.bll.EntityMetadata;
import net.longersoft.data.metadata.bll.RelationshipMetadata;
import net.longersoft.data.metadata.services.AttributeInfo;
import net.longersoft.data.metadata.services.EntityService;
import net.longersoft.data.metadata.services.RelationshipService;
import net.longersoft.framework.LongerModule;

public class EntityDeleteHandler extends AbstractEntityHandler{
	private  EntityMetadata referencingEntity;
	
	public EntityDeleteHandler(LongerModule module, Class<?> clazz) {
		super(module, clazz);
	}
	
	@Override
	public void initialize() throws Exception {
		super.initialize();
		
		referencingEntity = new EntityMetadata();
		referencingEntity.set_EntityName(entityName);
		referencingEntity.fillIt(this.system, "entityId");
	}

	public void deleteRelationship() throws Exception {
		if(!this.inited) return;
		
		for(AttributeInfo attributeInfo : attributes){
			AttributeMetadata bag = attributeInfo.getBag();
			if(!bag.get_IsLookupAttribute()) continue;
			
			EntityMetadata referencedEntity = new EntityMetadata();
			referencedEntity.set_EntityName(attributeInfo.getBag().get_LookupEntityName());
			referencedEntity.fillIt(this.system, "entityId");

			
			AttributeMetadata referencingAttribute = new AttributeMetadata();
			referencingAttribute.set_Entity(referencingEntity.get_EntityId(), referencingEntity.get_EntityName());
			referencingAttribute.set_AttributeName(bag.get_AttributeName());
			referencingAttribute.fillIt(this.system, "attributeId");
			
			
			RelationshipMetadata relationship = new RelationshipMetadata();
			relationship.set_ReferencedEntity(referencedEntity.get_EntityId(), referencedEntity.get_EntityName());
			relationship.set_ReferencingAttribute(referencingAttribute.get_AttributeId(), referencingAttribute.get_AttributeName());
			relationship.set_ReferencingEntity(referencingEntity.get_EntityId(), referencingEntity.get_EntityName());
			relationship.fillIt(this.system, "relationshipId");
						
			RelationshipService relationshipService = new RelationshipService();
			relationshipService.delete(relationship.get_RelationshipId());
			
		}		
	}

	public void deleteEntity() throws Exception {
		if(!this.inited) return;
		EntityService service = new EntityService();
		service.delete(this.referencingEntity.get_EntityId());
	}

}
