package net.longersoft.data.metadata.bll;

import java.util.ArrayList;
import java.util.List;

import net.longersoft.data.EntityRow;
import net.longersoft.data.linq.expressions.ConditionExpression;
import net.longersoft.data.linq.expressions.ConditionOperator;
import net.longersoft.data.linq.expressions.FilterExpression;
import net.longersoft.data.linq.expressions.FilterExpressionFun;
import net.longersoft.data.linq.expressions.LogicalOperator;
import net.longersoft.data.linq.expressions.Q;
import net.longersoft.data.metadata.dal._MetadataEntityRow;
import net.longersoft.framework.ServiceSession;

public class EntityMetadata extends _MetadataEntityRow {
	
	public EntityMetadata initByName(String entityName) throws Exception{
		ServiceSession system = new ServiceSession().initByUserId("system");
		this.set_EntityName(entityName);
		this.fillIt(system, "*");
		return this;
	}
	
	public EntityMetadata initById(String entityId) throws Exception{
		ServiceSession system = new ServiceSession().initByUserId("system");
		this.set_EntityId(entityId);
		this.fillIt(system, "*");
		return this;
	}
	
	
	@Override
	public void set_EntityName(String entityName) {
		super.set_EntityName(entityName);
		
		this.set_TableName(entityName + EntityRow.TABLE_TAIL);
		this.set_ViewName(entityName);
		
	}	
	
	public List<EntityRow> getReferMeShips() throws Exception{
		Q q = Q.from(RelationshipMetadata.EntityName + RelationshipMetadata.TABLE_TAIL)
		.where("referencedentityid", ConditionOperator.Equal, this.get_EntityId())
		.where("entityrelationshiptype", ConditionOperator.Equal, "OneToMany")
		.select("*");
		
		List<EntityRow> ships = q.returnCollection();
		return ships;
	}
	
	public List<EntityMetadata> getReferMeEntities() throws Exception{
		List<EntityRow> ships = getReferMeShips();
		
		ArrayList<EntityMetadata> list = new ArrayList<EntityMetadata>();
		for(EntityRow ship : ships){
			String id = (String)ship.getAttributeValue("referencingentityid");
			EntityMetadata entity = new EntityMetadata().initById(id);
			list.add(entity);
		}
		
		return list;
	}
	
	public List<EntityRow> getMeReferShips() throws Exception{
		return getMeReferShips(null);
	}
	
	public List<EntityRow> getMeReferShips(FilterExpressionFun fun) throws Exception{
		Q q = Q.from(RelationshipMetadata.EntityName + RelationshipMetadata.TABLE_TAIL)
		.where("referencingentityid", ConditionOperator.Equal, this.get_EntityId())
		.where("entityrelationshiptype", ConditionOperator.Equal, "OneToMany")
		.select("*");
		
		if(fun != null) q.where(fun);
		
		List<EntityRow> ships = q.returnCollection();
		return ships;
	}
	
	public List<EntityMetadata> getMeReferEntities() throws Exception{
		List<EntityRow> ships = getMeReferShips();
		
		ArrayList<EntityMetadata> list = new ArrayList<EntityMetadata>();
		for(EntityRow ship : ships){
			String id = (String)ship.getAttributeValue("referencedentityid");
			EntityMetadata entity = new EntityMetadata().initById(id);
			list.add(entity);
		}
		
		return list;
	}
	
	public List<EntityRow> getAssociateShips() throws Exception{
		Q q = Q.from(RelationshipMetadata.EntityName + RelationshipMetadata.TABLE_TAIL)
		.where(new FilterExpressionFun() {
			@Override
			public void call(FilterExpression filter) {
				filter.addCondition(new ConditionExpression("entityrelationshiptype", ConditionOperator.Equal, "ManyToMany"));
				
				FilterExpression f1 = filter.addFilter(LogicalOperator.Or);
				f1.addCondition(new ConditionExpression("referencedentityid", ConditionOperator.Equal, EntityMetadata.this.get_EntityId()));
				f1.addCondition(new ConditionExpression("referencingentityid", ConditionOperator.Equal, EntityMetadata.this.get_EntityId()));
			}
		})
		.select("*");
		List<EntityRow> ships = q.returnCollection();
		return ships;
	}
	
	public List<EntityMetadata> getAssociateEntities() throws Exception{
		List<EntityRow> ships = getAssociateShips();
		ArrayList<EntityMetadata> list = new ArrayList<EntityMetadata>();
		for(EntityRow ship : ships){
			String id = ship.getAttributeValue("referencedentityid").equals(this.get_EntityId())
						? ship.getAttributeValue("referencingentityid").toString()
								: ship.getAttributeValue("referencedentityid").toString();
			EntityMetadata entity = new EntityMetadata().initById(id);
			list.add(entity);
		}
		return list;
	}


	public List<AttributeMetadata> getAttributeList() throws Exception {
		Q q = Q.from(AttributeMetadata.EntityName + AttributeMetadata.TABLE_TAIL)
				.where("entityid", ConditionOperator.Equal, this.get_EntityId())
				//.where("islogical", ConditionOperator.Equal, false)
				.order_by("columnNumber")
				.select("*");
		List<EntityRow> rows = q.returnCollection();
		ArrayList<AttributeMetadata> list = new ArrayList<AttributeMetadata>();
		for(EntityRow row : rows){
			AttributeMetadata a = new AttributeMetadata();
			a.copyFrom(row);
			list.add(a);
		}
		return list;
	}
	
	public List<String> getAttributeNameList() throws Exception {
		Q q = Q.from(AttributeMetadata.EntityName + AttributeMetadata.TABLE_TAIL)
				.where("entityid", ConditionOperator.Equal, this.get_EntityId())
				.select("*");
		List<EntityRow> rows = q.returnCollection();
		ArrayList<String> list = new ArrayList<String>();
		for(EntityRow row : rows){
			AttributeMetadata a = new AttributeMetadata();
			a.copyFrom(row);
			list.add(a.get_AttributeName().toLowerCase());
		}
		return list;
	}
}
