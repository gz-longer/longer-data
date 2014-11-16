package net.longersoft.data.linq.visitors;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import net.longersoft.data.context.ExecutionContext;
import net.longersoft.data.linq.expressions.Aggregate;
import net.longersoft.data.linq.expressions.AttributeExpression;
import net.longersoft.data.linq.expressions.ColumnSetExpression;
import net.longersoft.data.linq.expressions.ConditionExpression;
import net.longersoft.data.linq.expressions.ConditionOperator;
import net.longersoft.data.linq.expressions.GroupByExpression;
import net.longersoft.data.linq.expressions.JoinOperator;
import net.longersoft.data.linq.expressions.LinkEntityExpression;
import net.longersoft.data.linq.expressions.OrderByExpression;
import net.longersoft.data.linq.expressions.PagingExpression;
import net.longersoft.data.linq.expressions.Q;
import net.longersoft.data.linq.expressions.FilterExpression;
import net.longersoft.data.linq.expressions.LogicalOperator;
import net.longersoft.data.linq.strategy.QueryContainer;
import net.longersoft.data.linq.strategy.SqlParameter;

import org.apache.log4j.Logger;

public class SelectVisitor extends ExpressionVisitor{
	private static Logger log = Logger.getLogger(SelectVisitor.class);
	private String tableAlias = null;
	
	
	protected QueryContainer queryContainer = new QueryContainer();
	protected Stack<ConditionBuilder> conditionBuilders = new Stack<ConditionBuilder>();
	

	public SelectVisitor(ExecutionContext context) {
		super(context);
	}

	@Override
	public void visit(Q query){
		this.queryContainer.setQuery(query);
		super.visit(query);
		
		this.tableAlias = query.getTableAlias();
		this.addFromClause(query);
		
		if( query.getColumnSet() != null) query.getColumnSet().acceptVisitor(this);
		if( query.getLinkEntities().size() > 0){
			for(LinkEntityExpression link : query.getLinkEntities()) link.acceptVisitor(this);
		}
		if( query.getFilter() != null) query.getFilter().acceptVisitor(this);
		if( query.getOrderBy() != null) query.getOrderBy().acceptVisitor(this);
		if( query.getGroupby() != null) query.getGroupby().acceptVisitor(this);
		if( query.getPaging() != null ) query.getPaging().acceptVisitor(this);
		
	}
	
	@Override
	public void visit(ColumnSetExpression columnSet) {
		super.visit(columnSet);
		
		for(AttributeExpression attribute : columnSet.getAttributes()){
			attribute.acceptVisitor(this);
		}
	}
	
	@Override
	public void visit(PagingExpression paging) {
		super.visit(paging);
		
		this.queryContainer.getPagingClause().append(String.format("limit %d, %d", paging.getSkip(), paging.getTake())); 
	}
	
	@Override
	public void visit(AttributeExpression attribute) {
		super.visit(attribute);
		
		this.addAttribute(this.tableAlias, attribute.getAttributeName(), attribute.getAttributeAlias());
	}
	
	@Override
	public void visit(LinkEntityExpression link) {
		super.visit(link);
		
		this.queryContainer.getLinkClause().append(String.format("\nleft join %s as %s on %s.%s %s %s.%s ", 
				link.getTableName(), 
				link.getTableAlias(), 
				link.getTableAlias(), 
				link.getAttributeFrom(),
				link.getConditionOperator() == ConditionOperator.NotEqual ? "<>" : "=",
				link.getParentEntity().getTableAlias(), 
				link.getAttributeTo()));
	}
	
	@Override
	public void visit(ConditionExpression condition) {
		super.visit(condition);
		
		this.addCondition(condition);
	}
	
	@Override
	public void visit(FilterExpression filter) {
		super.visit(filter);
		
		this.beginWritingFilter(filter);
		for(ConditionExpression condition : filter.getConditions()){
			condition.acceptVisitor(this);
		}
		this.endWritingFilter(filter);

		for(FilterExpression subfilter : filter.getFilters()){
			subfilter.acceptVisitor(this);
		}
	}
	
	@Override
	public void visit(GroupByExpression groupby) {
		super.visit(groupby);
		
		int n = 0;
		for(AttributeExpression att : groupby.getCols().getAttributes()){
			if(n > 0)this.queryContainer.getGroupbyClause().append(","); 
			this.queryContainer.getGroupbyClause().append(att.getAttributeName()); 
			n++;
		}
	}
	
	@Override
	public void visit(OrderByExpression order) {
		super.visit(order);
		this.addOrder(order);
	}
	
	private void addOrder(OrderByExpression order) {
		this.queryContainer.getOrderbyClause().append(String.format("%s%s", order.getOrderColume(), order.getDesc() ? " desc" : ""));
	}

	public void addFromClause(Q entity) {
		if( entity.getViewName() != entity.getTableAlias()){
			this.queryContainer.getFromClause().append(String.format("%s as %s", entity.getTableName(), entity.getTableAlias()));
		}else{
			this.queryContainer.getFromClause().append(String.format("%s", entity.getTableName()));
		}
	}
	
	public void addAttribute(String tableAlias, String attributeName,
			String attributeAlias) {
		if( this.queryContainer.getSelectClause().length() > 0){
			this.queryContainer.getSelectClause().append(", ");
		}
		
		if(attributeName.equals(attributeAlias)){
			this.queryContainer.getSelectClause().append(attributeName);
		}else{
			this.queryContainer.getSelectClause().append(String.format("%s as %s", attributeName, attributeAlias));
		}
	}
	
	public void addCondition(ConditionExpression condition) {
		StringBuffer sql = new StringBuffer();

		ConditionOperator op = condition.getOperator();
		ArrayList<Object> values = condition.getValues();
		values = op.prepareValues(values);
		if(op == ConditionOperator.Original){
			sql.append(values.get(0));
		}else if(op == ConditionOperator.In || op == ConditionOperator.NotIn){
			String s = "?";
			for(int i=0; i<values.size(); i++) {
				if(i > 0) s += ",?";
				this.createParameter(condition.getAttributeName(), values.get(i));
			}
			sql.append(op.value().replaceFirst("\\?", s).replaceAll("@", condition.getAttributeName()));
		}else{
			for(int i=0; i<values.size(); i++) {
				this.createParameter(condition.getAttributeName(), values.get(i));
			}
			sql.append(op.value().replaceAll("@", condition.getAttributeName()));
		}
		
		this.internalAddCondition(condition, sql.toString());
	}

	private String createParameter(String name, Object value){
		SqlParameter p = new SqlParameter(name, this.parameters.size() + 1, value);
		this.parameters.add(p);
		return "";
	}
	
	private void internalAddCondition(ConditionExpression cond, String sql) {
		this.conditionBuilders.peek().appendCondition(String.format("(%s)", sql));
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
	}

	@Override
	public String getCommandString() {
		Q query = this.queryContainer.getQuery();
		StringBuffer builder = new StringBuffer();
		builder.append(String.format("select "));
		
		if(query.getAggregate() != null){
			Aggregate agg = query.getAggregate();
			switch (agg.getType()) {
			case Count:
				builder.append(String.format("count(%s) as %s ", agg.getAttribute(), agg.getAlias()));
				break;
			default:
				throw new RuntimeException("unkown aggregate");
			}
		}else{
			builder.append(String.format("%s ", this.queryContainer.getSelectClause()));
		}
		
		builder.append(String.format("\nfrom %s ", this.queryContainer.getFromClause()));
		builder.append(String.format("%s ", this.queryContainer.getLinkClause()));
		
		if( this.queryContainer.getWhereClause().length() > 0){
			builder.append(String.format("\nwhere %s ", this.queryContainer.getWhereClause()));
		}
		
		if( this.queryContainer.getOrderbyClause().length() > 0){
			builder.append(String.format("\norder by %s ", this.queryContainer.getOrderbyClause()));
		}
		
		if( this.queryContainer.getGroupbyClause().length() > 0){
			builder.append(String.format("\ngroup by %s ", this.queryContainer.getGroupbyClause())); 
		}
		
		if( this.queryContainer.getPagingClause().length() > 0){
			builder.append(String.format("\n%s ", this.queryContainer.getPagingClause())); 
		}
		
		return builder.toString();
	}
	
}
