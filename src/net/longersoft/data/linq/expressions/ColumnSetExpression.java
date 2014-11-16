package net.longersoft.data.linq.expressions;

import net.longersoft.data.linq.visitors.ExpressionVisitor;

import java.util.ArrayList;
import java.util.List;

public class ColumnSetExpression extends Expression {
	List<AttributeExpression> attributes = new ArrayList<AttributeExpression>();
	
	@Override
	public void acceptVisitor(ExpressionVisitor visitor) {
		visitor.visit(this);
	}

	public void addColumn(String attributeName, String attributeAlias, Object value) {
		AttributeExpression element = new AttributeExpression(attributeName, attributeAlias, value);
		this.attributes.add(element);
	}

	public List<AttributeExpression> getAttributes() {
		return this.attributes;
	}
}
