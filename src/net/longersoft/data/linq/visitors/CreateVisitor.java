package net.longersoft.data.linq.visitors;

import org.apache.log4j.Logger;

import net.longersoft.data.context.ExecutionContext;
import net.longersoft.data.linq.expressions.AttributeExpression;
import net.longersoft.data.linq.expressions.ColumnSetExpression;
import net.longersoft.data.linq.expressions.Q;
import net.longersoft.data.linq.strategy.SqlParameter;

public class CreateVisitor extends ExpressionVisitor {
	private StringBuffer sqlBuilder = new StringBuffer();
	private static Logger log = Logger.getLogger(CreateVisitor.class);

	public CreateVisitor(ExecutionContext context) {
		super(context);
	}

	@Override
	public void visit(Q query) {
		super.visit(query);

		this.sqlBuilder.append(String.format("insert into %s ", query
				.getTableName()));
		query.getColumnSet().acceptVisitor(this);
	}

	@Override
	public void visit(ColumnSetExpression colset) {
		super.visit(colset);

		Boolean first = true;
		StringBuffer columnBuilder = new StringBuffer();
		StringBuffer valueBuilder = new StringBuffer();

		for (AttributeExpression attribute : colset.getAttributes()) {
			if (!first) {
				columnBuilder.append(", ");
				valueBuilder.append(", ");
			}
			columnBuilder.append(attribute.getAttributeName());
			valueBuilder.append(this.makeParameter(attribute));
			first = false;
		}

		this.sqlBuilder.append(String.format("(%s) values (%s)", columnBuilder,
				valueBuilder));
	}

	private Object makeParameter(AttributeExpression attribute) {
		Object value = attribute.getAttributeValue();
		if (value == null) {
			return "null";
		}

		SqlParameter p = new SqlParameter(attribute.getAttributeName(),
				this.parameters.size() + 1, value);
		this.parameters.add(p);
		return "?";
	}

	@Override
	public String getCommandString() {
		return this.sqlBuilder.toString();
	}

}
