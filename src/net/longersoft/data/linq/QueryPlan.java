package net.longersoft.data.linq;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.apache.log4j.Logger;

import net.longersoft.data.context.ExecutionContext;
import net.longersoft.data.context.ExecutionContextFun;
import net.longersoft.data.context.ExecutionContextHelper;
import net.longersoft.data.linq.expressions.Q;
import net.longersoft.data.linq.strategy.SqlProvider;
import net.longersoft.data.linq.visitors.ExpressionVisitor;
import net.longersoft.helpers.PropertiesHelper;

public class QueryPlan {
	protected Q query = null;
	protected ExpressionVisitor visitor = null;
	
	private static Logger log = Logger.getLogger(QueryPlan.class);
	
	public QueryPlan(Q query, ExpressionVisitor visitor){
		this.query = query;
		this.visitor = visitor;
	}
	
	protected void makeSql(){
		this.getEntityExpression().acceptVisitor(this.getVisitor());
	}

	protected void setEntityExpression(Q query) {
		this.query = query;
	}

	protected Q getEntityExpression() {
		return query;
	}

	protected void setVisitor(ExpressionVisitor visitor) {
		this.visitor = visitor;
	}

	protected ExpressionVisitor getVisitor() {
		return visitor;
	}
	
	public final PreparedStatement getCommand(){
		this.makeSql();
		return this.getVisitor().getCommand();
	}
	
	public final String getCommandString(){
		this.makeSql();
		return this.getVisitor().getCommandString();
	}
}
