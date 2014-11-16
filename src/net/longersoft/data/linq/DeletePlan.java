package net.longersoft.data.linq;

import net.longersoft.data.context.ExecutionContext;
import net.longersoft.data.linq.expressions.Q;
import net.longersoft.data.linq.visitors.DeleteVisitor;
import net.longersoft.data.linq.visitors.ExpressionVisitor;

public class DeletePlan extends QueryPlan {

	public DeletePlan(Q query, ExecutionContext	context) {
		super(query, new DeleteVisitor(context));
	}

}
