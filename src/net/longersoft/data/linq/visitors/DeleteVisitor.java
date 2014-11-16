package net.longersoft.data.linq.visitors;

import java.util.ArrayList;
import java.util.Stack;

import net.longersoft.data.context.ExecutionContext;
import net.longersoft.data.linq.expressions.ConditionExpression;
import net.longersoft.data.linq.expressions.ConditionOperator;
import net.longersoft.data.linq.expressions.Q;
import net.longersoft.data.linq.expressions.FilterExpression;
import net.longersoft.data.linq.expressions.LogicalOperator;
import net.longersoft.data.linq.strategy.QueryContainer;
import net.longersoft.data.linq.strategy.SqlParameter;

public class DeleteVisitor extends ExpressionVisitor {
	
	StringBuffer sqlBuilder = new StringBuffer();
	protected Stack<ConditionBuilder> conditionBuilders = new Stack<ConditionBuilder>();	
	protected QueryContainer queryContainer = new QueryContainer();

	public DeleteVisitor(ExecutionContext context) {
		super(context);
	}
	
	@Override
	public void visit(Q query) {
		super.visit(query);
		
		this.sqlBuilder.append(String.format("delete from %s where ", query.getTableName()));
		query.getFilter().acceptVisitor(this);
	}
	
	@Override
	public void visit(FilterExpression filter) {
		super.visit(filter);
		
		this.beginWritingFilter(filter);
		for(ConditionExpression condition : filter.getConditions()){
			condition.acceptVisitor(this);
		}
		this.endWritingFilter(filter);
	}
	
	@Override
	public void visit(ConditionExpression conditionExpression) {
		super.visit(conditionExpression);
		
		this.addCondition(conditionExpression);
	}
	
	@Override
	public String getCommandString() {
		return this.sqlBuilder.toString();
	}
	
	public void addCondition(ConditionExpression condition) {
		StringBuffer sql = new StringBuffer();

		ConditionOperator op = condition.getOperator();
		ArrayList<Object> values = condition.getValues();
		values = op.prepareValues(values);
		for(int i=0; i<values.size(); i++) {
			this.createParameter(condition.getAttributeName(), values.get(i));
		}
		sql.append(op.value().replaceAll("@", condition.getAttributeName()));

		this.internalAddCondition(sql.toString());
	}

	private String createParameter(String name, Object value){
		SqlParameter p = new SqlParameter(name, this.parameters.size() + 1, value);
		this.parameters.add(p);
		return "";
	}
	
	private void internalAddCondition(String condition) {
		this.conditionBuilders.peek().appendCondition(String.format("(%s)", condition));
	}	
	
	public void beginWritingFilter(FilterExpression filter) {
		this.conditionBuilders.add(new ConditionBuilder(filter.getLogicalOperator().name().toLowerCase()));
	}

	public void endWritingFilter(FilterExpression filter) {
		ConditionBuilder builder = this.conditionBuilders.pop();
		if( this.queryContainer.getWhereClause().length() > 0){
			this.queryContainer.getWhereClause().append(String.format(" %s ", LogicalOperator.And.name().toLowerCase()));
		}
		
		this.queryContainer.getWhereClause().append(builder.makeSql());
		
		this.sqlBuilder.append(this.queryContainer.getWhereClause());
	}	
}
