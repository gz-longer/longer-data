package net.longersoft.data.linq;

import java.sql.*;

import javax.naming.Context;

import net.longersoft.data.context.ExecutionContext;
import net.longersoft.data.linq.expressions.Q;
import net.longersoft.data.linq.strategy.SqlProvider;
import net.longersoft.data.linq.visitors.SelectVisitor;

public class SelectPlan extends QueryPlan {
	
	
	public SelectPlan(Q query, ExecutionContext context) {
		super(query, new SelectVisitor(context));
	}

}
