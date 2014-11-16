package net.longersoft.data.linq.expressions;

import java.util.ArrayList;
import java.util.List;

import net.longersoft.data.EntityRow;
import net.longersoft.data.linq.visitors.ExpressionVisitor;

public class LinkEntityExpression extends EntityExpressionBase {
	private ColumnSetExpression columnSet = new ColumnSetExpression();
	private ConditionOperator conditionOperator;
    private FilterExpression criteria;
    private String entityName;
    private String tableName;
    private JoinOperator joinOperator;
    private FilterExpression linkCriteria;
    private List<LinkEntityExpression> linkEntities = new ArrayList<LinkEntityExpression>();
    private OrderByExpression order;
    private Q parentEntity;
    private LinkEntityExpression parentLinkEntity;
    private String tableAlias;
    private ColumnSetExpression groupAtrributes;
    private String attributeFrom;
    private String attributeTo;
    
    @Override
    public void acceptVisitor(ExpressionVisitor visitor) {
    	visitor.visit(this);
    }
    
	public void setEntityName(String entityName) {
		this.entityName = entityName;
		this.tableName = entityName + EntityRow.TABLE_TAIL;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setAttributeFrom(String attributeFrom) {
		this.attributeFrom = attributeFrom;
	}
	public String getAttributeFrom() {
		return attributeFrom;
	}
	public void setAttributeTo(String attributeTo) {
		this.attributeTo = attributeTo;
	}
	public String getAttributeTo() {
		return attributeTo;
	}
	public void setParentLinkEntity(LinkEntityExpression parentLinkEntity) {
		this.parentLinkEntity = parentLinkEntity;
	}
	public LinkEntityExpression getParentLinkEntity() {
		return parentLinkEntity;
	}
	public void setParentEntity(Q parentEntity) {
		this.parentEntity = parentEntity;
	}
	public Q getParentEntity() {
		return parentEntity;
	}
	public void setTableAlias(String tableAlias) {
		this.tableAlias = tableAlias;
	}
	public String getTableAlias() {
		return tableAlias;
	}
	public void setJoinOperator(JoinOperator joinOperator) {
		this.joinOperator = joinOperator;
	}
	public JoinOperator getJoinOperator() {
		return joinOperator;
	}
	public void setConditionOperator(ConditionOperator conditionOperator) {
		this.conditionOperator = conditionOperator;
	}
	public ConditionOperator getConditionOperator() {
		return conditionOperator;
	}

	public String getTableName() {
		return tableName;
	}
    
    
}
