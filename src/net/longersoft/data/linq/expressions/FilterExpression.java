package net.longersoft.data.linq.expressions;

import net.longersoft.data.linq.visitors.ExpressionVisitor;

import java.util.ArrayList;
import java.util.List;

public class FilterExpression extends Expression {
	private List<ConditionExpression> conditions = new ArrayList<ConditionExpression>();
	private List<FilterExpression> filters = new ArrayList<FilterExpression>();
	private LogicalOperator logicalOperator = LogicalOperator.And;
	
	public FilterExpression(){
		this(LogicalOperator.And);
	}
	
	public FilterExpression(LogicalOperator logicalOperator) {
		this.setLogicalOperator(logicalOperator);
	}
	
	public FilterExpression where(String attributeName, ConditionOperator op, Object... values){
		ConditionExpression condition = new ConditionExpression(attributeName, op, values);
		return this.addCondition(condition);
	}
	
	public FilterExpression where(String attributeName, ConditionOperator op, Object value){
		ArrayList<Object> values = new ArrayList<Object>();
		values.add(value);
		return where(attributeName, op, values);
	}
	
	@Override
	public void acceptVisitor(ExpressionVisitor visitor) {
		visitor.visit(this);
	}
	
	public FilterExpression addFilter(LogicalOperator logicalOperator){
		FilterExpression subFilter = new FilterExpression(logicalOperator);
		this.filters.add(subFilter);
		return subFilter;
	}

	public FilterExpression addCondition(ConditionExpression condition) {
		condition.setFilter(this);
		this.getConditions().add(condition);
		return this;
	}

	public List<ConditionExpression> getConditions() {
		return conditions;
	}
	
	public List<FilterExpression> getFilters(){
		return filters;
	}

	private void setLogicalOperator(LogicalOperator logicalOperator) {
		this.logicalOperator = logicalOperator;
	}

	public LogicalOperator getLogicalOperator() {
		return logicalOperator;
	}
}
