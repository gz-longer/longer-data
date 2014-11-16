package net.longersoft.data.metadata.helpers;

import java.sql.PreparedStatement;

import net.longersoft.data.SqlHelper;
import net.longersoft.data.metadata.bll.AttributeMetadata;
import net.longersoft.data.metadata.bll.EntityMetadata;
import net.longersoft.data.metadata.bll.RelationshipMetadata;

public class RelationshipDeleteAction extends ActionBase {
	private RelationshipMetadata bag;
	private EntityMetadata referencedEntity;
	private AttributeMetadata referencedAttribute;
	private EntityMetadata referencingEntity;
	private AttributeMetadata referencingAttribute;
	
	public RelationshipDeleteAction(RelationshipMetadata bag, MetadataHelper helper) {
		super(ActionType.Create, MetadataObjectType.Relationship, bag.get_RelationshipId(), helper);
		this.bag = bag;
	}
	
	@Override
	public void retrieveDataForOperations() throws Exception {
		super.retrieveDataForOperations();
		
		this.referencedEntity = new EntityMetadata();
		this.referencedEntity.set_EntityId(this.bag.get_ReferencedEntityId());
		this.referencedEntity.fillIt(context, "entityName", "tableName");
		
		this.referencingEntity = new EntityMetadata();
		this.referencingEntity.set_EntityId(this.bag.get_ReferencingEntityId());
		this.referencingEntity.fillIt(context, "entityName", "tableName");
		
		this.referencedAttribute = new AttributeMetadata();
		this.referencedAttribute.set_AttributeId(this.bag.get_ReferencedAttributeId());
		this.referencedAttribute.fillIt(context, "attributeName");
		
		this.referencingAttribute = new AttributeMetadata();
		this.referencingAttribute.set_AttributeId(this.bag.get_ReferencingAttributeId());
		this.referencingAttribute.fillIt(context, "attributeName");
		
	}
	
	@Override
	public void doMetadataOperation() throws Exception {
		try{
			super.doMetadataOperation();
			this.bag.deleteIt(context);
		}catch(Exception e){}
	}
	
	@Override
	public void doDatabaseOperation() throws Exception {
		try{
			super.doDatabaseOperation();
			String constraintName = RelationshipCreateAction.makeFkconstraint(referencedEntity, referencedAttribute, referencingEntity, referencingAttribute);
			String sql = String.format("alter table %s drop constraint %s;", this.referencingEntity.get_TableName(), constraintName);
			PreparedStatement statement = this.context.getConnection().prepareStatement(super.modifySql(sql));
			SqlHelper.execute(statement);
		}catch(Exception e){}
	}
}