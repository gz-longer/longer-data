package net.longersoft.data.database.services;

import java.util.UUID;

import net.longersoft.data.metadata.bll.AttributeMetadata;
import net.longersoft.data.metadata.bll.EntityMetadata;
import net.longersoft.data.metadata.services.AttributeInfo;
import net.longersoft.data.metadata.services.EntityCreateInfo;
import net.longersoft.data.metadata.services.EntityRelationshipType;
import net.longersoft.data.metadata.services.EntityService;
import net.longersoft.data.metadata.services.RelationshipCreateInfo;
import net.longersoft.data.metadata.services.RelationshipService;
import net.longersoft.framework.LongerModule;

public class EntityCreateHandler extends AbstractEntityHandler{
	protected EntityCreateInfo entityInfo;
	
	public EntityCreateHandler(LongerModule module, Class<?> clazz) {
		super(module, clazz);
	}
	
	@Override
	public void initialize() throws Exception {
		super.initialize();
		this.entityInfo = makeEntityInfoFromClass(entityClazz);
	}

	private EntityCreateInfo makeEntityInfoFromClass(Class<?> clazz) {
		EntityCreateInfo entityInfo = new EntityCreateInfo();
		EntityMetadata ebag = entityInfo.getBag();
		ebag.set_EntityName(entityName);
		ebag.set_DisplayName(displayName);
		ebag.set_EntityId(UUID.randomUUID().toString());
		
		return entityInfo;
	}



	public void createRelationship() throws Exception {
		for(AttributeInfo attributeInfo : attributes){
			AttributeMetadata bag = attributeInfo.getBag();
			if(!bag.get_IsLookupAttribute()) continue;
			
			RelationshipCreateInfo createInfo = new RelationshipCreateInfo();
			createInfo.setReferencedEntityName(attributeInfo.getBag().get_LookupEntityName());
			createInfo.setReferencingEntityName(entityInfo.getBag().get_EntityName());
			createInfo.setRelationshipAttributeName(attributeInfo.getBag().get_AttributeName());
			createInfo.setEntityRelationshipType(EntityRelationshipType.OneToMany);
			createInfo.setDisplayName(bag.get_DisplayName());
			RelationshipService relationshipService = new RelationshipService();
			relationshipService.create(createInfo);
			
		}
	}

	public void createEntity() throws Exception{
		for(AttributeInfo attributeInfo : attributes){
			AttributeMetadata bag = attributeInfo.getBag();
			if(bag.get_IsLookupAttribute()) continue;
			
			if(bag.get_IsPrimaryKey()){
				entityInfo.setPrimaryKey(attributeInfo);
				continue;
			}
			
			if(bag.get_IsPrimaryAttribute()){
				entityInfo.setPrimaryField(attributeInfo);
				continue;
			}
			
			entityInfo.getNormalFields().add(attributeInfo);
		}
		EntityService service = new EntityService();
		entityInfo.getBag().set_Module(this.module.getModuleId(), this.module.getModuleName());
		service.create(entityInfo);				
	}	
}
