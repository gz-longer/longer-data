package net.longersoft.data.metadata.helpers;

import java.sql.PreparedStatement;
import java.util.UUID;

import net.longersoft.data.SqlHelper;
import net.longersoft.data.metadata.bll.AttributeMetadata;
import net.longersoft.data.metadata.bll.EntityMetadata;
import net.longersoft.data.metadata.bll.RelationshipMetadata;

public class RelationshipCreateAction extends ActionBase {
	private RelationshipMetadata bag;
	
	public RelationshipCreateAction(RelationshipMetadata bag, MetadataHelper helper) {
		super(ActionType.Create, MetadataObjectType.Relationship, bag.get_RelationshipId(), helper);
		this.bag = bag;
	}

	@Override
	public void retrieveDataForOperations() throws Exception {
		super.retrieveDataForOperations();
		
	}
	
	@Override
	public void doMetadataOperation() throws Exception {
		super.doMetadataOperation();

		this.bag.createIt(this.context);
	}
	
	@Override
	public void doDatabaseOperation() throws Exception {
		super.doDatabaseOperation();
		EntityMetadata referencedEntity;
		AttributeMetadata referencedAttribute;
		EntityMetadata referencingEntity;
		AttributeMetadata referencingAttribute;
		
		referencedEntity = new EntityMetadata();
		referencedEntity.set_EntityId(this.bag.get_ReferencedEntityId());
		referencedEntity.fillIt(this.context, "entityName", "tableName");
		
		referencingEntity = new EntityMetadata();
		referencingEntity.set_EntityId(this.bag.get_ReferencingEntityId());
		referencingEntity.fillIt(this.context, "entityName", "tableName");
		
		referencedAttribute = new AttributeMetadata();
		referencedAttribute.set_AttributeId(this.bag.get_ReferencedAttributeId());
		referencedAttribute.fillIt(this.context, "attributeName");
		
		referencingAttribute = new AttributeMetadata();
		referencingAttribute.set_AttributeId(this.bag.get_ReferencingAttributeId());
		referencingAttribute.fillIt(this.context, "attributeName");
		
		String constraintName = makeFkconstraint(referencedEntity, referencedAttribute, referencingEntity, referencingAttribute);
		
		String sql = String.format("alter table %1$s add constraint %2$s foreign key (%3$s) references %4$s (%5$s)", 
				referencingEntity.get_TableName(), /*table name*/ 
				constraintName,
				referencingAttribute.get_AttributeName(),
				referencedEntity.get_TableName(), /* refered entity name */
				referencedAttribute.get_AttributeName() /* refered attribute name */
				);
		
		PreparedStatement statement = this.context.getConnection().prepareStatement(super.modifySql(sql));
		SqlHelper.execute(statement);
	}

	static String makeFkconstraint(EntityMetadata referencedEntity,
			AttributeMetadata referencedAttribute,
			EntityMetadata referencingEntity,
			AttributeMetadata referencingAttribute) {
		
		return String.format("fk_%1$s_%2$s_%3$s", 
				zip(referencingEntity.get_EntityName()),
				zip(referencedEntity.get_EntityName()),
				referencingAttribute.get_AttributeName()
				);
	}
	
	private static String zip(String name){
		return name.replaceAll("\\w+_", "");
	}
}
