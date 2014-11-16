package net.longersoft.data.linq.expressions;

import java.util.ArrayList;

import net.longersoft.data.linq.visitors.ExpressionVisitor;

public class ConditionExpression extends Expression {
	private String attributeName;
	private ConditionOperator operator;
	private ArrayList<Object> values;
	private FilterExpression filter;
	
	public ConditionExpression(String attributeName, ConditionOperator op, ArrayList<Object> values){
		init(attributeName, op, values);
	}
	
	public ConditionExpression(String attributeName, ConditionOperator op, Object... values){
		ArrayList<Object> list = new ArrayList<Object>();
		for(Object obj : values) list.add(obj);
		init(attributeName, op, list);
	}

	private void init(String attributeName, ConditionOperator op,
			ArrayList<Object> values) {
		this.setAttributeName(attributeName);
		this.setOperator(op);
		this.setValues(values);
	}
	
	@Override
	public void acceptVisitor(ExpressionVisitor visitor) {
		visitor.visit(this);
	}

	private void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeName() {
		return attributeName;
	}

	private void setOperator(ConditionOperator operator) {
		this.operator = operator;
	}

	public ConditionOperator getOperator() {
		return operator;
	}

	private void setValues(ArrayList<Object> values) {
		this.values = values;
	}

	public ArrayList<Object> getValues() {
		return values;
	}

	public void setFilter(FilterExpression filter) {
		this.filter = filter;
	}

	public FilterExpression getFilter() {
		return filter;
	}
}
