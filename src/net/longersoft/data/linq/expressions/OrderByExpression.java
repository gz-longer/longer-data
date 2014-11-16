package net.longersoft.data.linq.expressions;

import net.longersoft.data.linq.visitors.ExpressionVisitor;

public class OrderByExpression extends Expression {
	private String orderColume;
	private boolean desc = false;
	
	@Override
	public void acceptVisitor(ExpressionVisitor visitor) {
		visitor.visit(this);
	}
	
	public void setOrderBy(String colume, boolean desc){
		this.orderColume = colume;
		this.desc = desc;
	}
	
	public String getOrderColume(){
		return this.orderColume;
	}
	
	public boolean getDesc(){
		return this.desc;
	}
}
