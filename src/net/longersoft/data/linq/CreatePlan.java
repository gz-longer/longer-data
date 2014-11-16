package net.longersoft.data.linq;

import java.sql.PreparedStatement;

import net.longersoft.data.context.ExecutionContext;
import net.longersoft.data.linq.expressions.ColumnSetExpression;
import net.longersoft.data.linq.expressions.Q;
import net.longersoft.data.linq.visitors.CreateVisitor;
import net.longersoft.data.linq.visitors.ExpressionVisitor;
import net.longersoft.data.linq.visitors.SelectVisitor;

public class CreatePlan extends QueryPlan {

	public CreatePlan(Q query, ExecutionContext context) {
		super(query, new CreateVisitor(context));
	}	
}
