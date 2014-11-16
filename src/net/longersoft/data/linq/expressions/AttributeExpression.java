package net.longersoft.data.linq.expressions;

import net.longersoft.data.linq.visitors.ExpressionVisitor;

public class AttributeExpression extends Expression {
	private String attributeName = null;
	private String attributeAlias = null;
	private Object attributeValue = null;
	
	public AttributeExpression(String attributeName, String attributeAlias, Object value){
		if( attributeAlias == null || attributeAlias.length() == 0){
			String[] arr = attributeName.split("\\.");
			attributeAlias = arr[arr.length - 1];
		}
		
		this.setAttributeName(attributeName);
		this.setAttributeAlias(attributeAlias);
		this.setAttributeValue(value);
	}

	private void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeName() {
		return attributeName;
	}

	private void setAttributeAlias(String alias) {
		this.attributeAlias = alias;
	}

	public String getAttributeAlias() {
		return attributeAlias;
	}
	
	@Override
	public void acceptVisitor(ExpressionVisitor visitor) {
		visitor.visit(this);
	}

	private void setAttributeValue(Object attributeValue) {
		this.attributeValue = attributeValue;
	}

	public Object getAttributeValue() {
		return attributeValue;
	}
}
