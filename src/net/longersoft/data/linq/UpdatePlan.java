package net.longersoft.data.linq;

import net.longersoft.data.context.ExecutionContext;
import net.longersoft.data.linq.expressions.Q;
import net.longersoft.data.linq.visitors.ExpressionVisitor;
import net.longersoft.data.linq.visitors.UpdateVisitor;

public class UpdatePlan extends QueryPlan {

	public UpdatePlan(Q query, ExecutionContext context) {
		super(query, new UpdateVisitor(context));
	}

}
