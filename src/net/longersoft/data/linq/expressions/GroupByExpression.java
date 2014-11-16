package net.longersoft.data.linq.expressions;

import net.longersoft.data.linq.visitors.ExpressionVisitor;

public class GroupByExpression extends Expression {
	private ColumnSetExpression cols = new ColumnSetExpression();
	
	public void acceptVisitor(ExpressionVisitor visitor) {
		visitor.visit(this);
	}
	
	public void setCols(String[] cols){
		for(String col : cols){
			this.cols.addColumn(col, null, null);
		}
	}
	
	public ColumnSetExpression getCols(){
		return this.cols;
	}
}
