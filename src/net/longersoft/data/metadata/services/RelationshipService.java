package net.longersoft.data.metadata.services;

import java.util.UUID;

import net.longersoft.data.context.ExecutionContext;
import net.longersoft.data.context.ExecutionContextFun;
import net.longersoft.data.context.ExecutionContextHelper;
import net.longersoft.data.metadata.bll.AttributeMetadata;
import net.longersoft.data.metadata.bll.EntityMetadata;
import net.longersoft.data.metadata.bll.RelationshipMetadata;
import net.longersoft.data.metadata.helpers.ActionType;
import net.longersoft.data.metadata.helpers.MetadataHelper;
import net.longersoft.framework.LongerWebService;
import net.longersoft.framework.ServiceSession;

public class RelationshipService extends LongerWebService {
	ServiceSession session = null;
	public RelationshipService() {
		this.session = new ServiceSession().initByUserId("system");
	}

	/// 创建关系
	public String create(final RelationshipCreateInfo createInfo) throws Exception {
		final String[] returnValue = new String[1];
		ExecutionContextHelper.tran(session, new ExecutionContextFun() {
			@Override
			public void call(ExecutionContext context) throws Exception {
				returnValue[0] = create(createInfo, context);
			}
		});
		return returnValue[0];
	}

	protected String create(RelationshipCreateInfo createInfo,ExecutionContext context) throws Exception {
		MetadataHelper helper = new MetadataHelper(context);
		
		// referenced
		EntityMetadata referencedEntity = new EntityMetadata();
		referencedEntity.set_EntityName(createInfo.getReferencedEntityName());
		referencedEntity.fillIt(context, "entityId");
		AttributeMetadata referencedAttribute = new AttributeMetadata();
		referencedAttribute.set_Entity(referencedEntity.get_EntityId(), referencedEntity.get_EntityName());
		referencedAttribute.set_IsPrimaryKey(true);
		referencedAttribute.fillIt(context, "attributeId", "attributeName");
		
		// referencing
		EntityMetadata referencingEntity = new EntityMetadata();
		referencingEntity.set_EntityName(createInfo.getReferencingEntityName());
		referencingEntity.fillIt(context, "entityId");
		
		RelationshipMetadata relationshipInfo = new RelationshipMetadata();
		relationshipInfo.init();
		relationshipInfo.set_RelationshipId(UUID.randomUUID().toString());
		relationshipInfo.set_RelationshipName(createInfo.getRelationshipAttributeName());
		relationshipInfo.set_ReferencedEntity(referencedEntity.get_EntityId(), referencedEntity.get_EntityName());
		relationshipInfo.set_ReferencedAttribute(referencedAttribute.get_AttributeId(), referencedAttribute.get_AttributeName());
		relationshipInfo.set_IsLogical(false);
		relationshipInfo.set_ReferencingEntity(referencingEntity.get_EntityId(), referencingEntity.get_EntityName());
		relationshipInfo.set_ReferencingAttribute(UUID.randomUUID().toString(), relationshipInfo.get_RelationshipName());
		relationshipInfo.set_DisplayName(createInfo.getDisplayName());
		
		createRelationshipAttributes(relationshipInfo, referencedEntity, referencingEntity, context, helper);
		helper.addToQueue(relationshipInfo, ActionType.Create);
		helper.processQueue();
		return relationshipInfo.get_RelationshipId();
	}

	private void createRelationshipAttributes(
			RelationshipMetadata relationshipInfo,
			EntityMetadata referencedEntity,
			EntityMetadata referencingEntity, ExecutionContext context,
			MetadataHelper helper) throws Exception {
		AttributeInfo aInfo = new AttributeInfo(AttributeType.Lookup);
		AttributeMetadata bag = aInfo.getBag();
		bag.set_AttributeName(relationshipInfo.get_RelationshipName());
		bag.set_Entity(referencingEntity.get_EntityId(), referencingEntity.get_EntityName());
		bag.set_AttributeId(relationshipInfo.get_ReferencingAttributeId());
		bag.set_IsLookupAttribute(true);
		bag.set_LookupEntityName(referencedEntity.get_EntityName());
		bag.set_DisplayName(relationshipInfo.get_DisplayName());
		AttributeService.createInternal(aInfo, helper, context);
	}

	/// 删除关系
	public void delete(final String relationshipId) throws Exception {
		ExecutionContextHelper.tran(session, new ExecutionContextFun() {
			@Override
			public void call(ExecutionContext context) throws Exception {
				delete(relationshipId, context);
			}
		});
	}

	public void delete(String relationshipId, ExecutionContext context) throws Exception {
		if(relationshipId == null) return;
		
		MetadataHelper helper = new MetadataHelper(context);
		RelationshipMetadata relationshipData = new RelationshipMetadata();
		relationshipData.set_RelationshipId(relationshipId);
		relationshipData.fillIt(context, "referencingEntityId", "referencingAttributeId", "referencedEntityId", "referencedAttributeId");
		helper.addToQueue(relationshipData, ActionType.Delete);
		deleteRelationshipAttribute(relationshipData, helper, context);
		helper.processQueue();
	}

	private void deleteRelationshipAttribute(
			RelationshipMetadata relationshipData,
			MetadataHelper helper,
			ExecutionContext context) throws Exception {
		String attributeId = relationshipData.get_ReferencingAttributeId();
		AttributeMetadata attribute = new AttributeMetadata();
		attribute.set_AttributeId(attributeId);
		
		helper.addToQueue(attribute, ActionType.Delete);
	}
	
	public void update(final String relationshipId, final RelationshipMetadata relationshipData) throws Exception{
		ExecutionContextHelper.tran(session, new ExecutionContextFun() {
			@Override
			public void call(ExecutionContext context) throws Exception {
				update(relationshipId, relationshipData, context);
			}
		});
	}

	protected void update(String relationshipId, RelationshipMetadata relationshipInfo, ExecutionContext context) throws Exception {
		if(relationshipId == null) return;
		
		MetadataHelper helper = new MetadataHelper(context);
		relationshipInfo.set_RelationshipId(relationshipId);
		helper.addToQueue(relationshipInfo, ActionType.Update);
		helper.processQueue();
	}
}
