package net.longersoft.data.linq.visitors;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import net.longersoft.data.context.ExecutionContext;
import net.longersoft.data.linq.expressions.AttributeExpression;
import net.longersoft.data.linq.expressions.ColumnSetExpression;
import net.longersoft.data.linq.expressions.ConditionExpression;
import net.longersoft.data.linq.expressions.GroupByExpression;
import net.longersoft.data.linq.expressions.LinkEntityExpression;
import net.longersoft.data.linq.expressions.PagingExpression;
import net.longersoft.data.linq.expressions.Q;
import net.longersoft.data.linq.expressions.FilterExpression;
import net.longersoft.data.linq.expressions.OrderByExpression;
import net.longersoft.data.linq.strategy.SqlParameter;

public abstract class ExpressionVisitor {
	private static Logger log = Logger.getLogger(ExpressionVisitor.class);
	protected ExecutionContext context;
	protected List<SqlParameter> parameters = new ArrayList<SqlParameter>();
		
	
	public ExpressionVisitor(ExecutionContext context){
		this.context = context;
	}

	public void visit(Q query) {
		//log.trace(query);
	}
	
	public void visit(PagingExpression paging){
		//log.trace(paging);
	}
	
	public void visit(GroupByExpression groupby){
		//log.trace(groupby);
	}
	
	public void visit(ColumnSetExpression expression){
		//log.trace(expression);
	}
	
	public void visit(FilterExpression expression){
		//log.trace(expression);
	}

	public void visit(LinkEntityExpression expression){
		//log.trace(expression);
	}

	public void visit(OrderByExpression expression) {
		//log.trace(expression);
	}

	public void visit(AttributeExpression expression){
		//log.trace(expression);
	}

	public void visit(ConditionExpression conditionExpression) {
		//log.trace(conditionExpression);
	}
	
	public abstract String getCommandString();

	public PreparedStatement getCommand() {
		String sql = this.getCommandString();
		PreparedStatement command = null;
		try {
			command = this.context.getConnection().prepareStatement(sql);
			if( this.parameters.size() > 0){
				for(SqlParameter p : this.parameters){
					command.setObject(p.getIndex(), p.getValue());
				}
			}
		} catch (SQLException e) {
			log.error(e);
			throw new RuntimeException(e);
		}
		
		return command;
	}	
}
