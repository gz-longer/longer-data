package net.longersoft.data.linq.expressions;

import net.longersoft.data.linq.visitors.ExpressionVisitor;

public class PagingExpression extends Expression {
	private int skip = 0;
	private int take = 10;
	
	public void setSkip(int skip) {
		this.skip = skip;
	}
	public int getSkip() {
		return skip;
	}
	
	public void setTake(int take) {
		this.take = take;
	}
	public int getTake() {
		return take;
	}
	
	@Override
	public void acceptVisitor(ExpressionVisitor visitor) {
		visitor.visit(this);
	}
}
