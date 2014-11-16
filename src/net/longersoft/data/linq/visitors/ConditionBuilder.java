package net.longersoft.data.linq.visitors;


public final class ConditionBuilder{
	private String logicalOperator;
	private StringBuffer sqlClause = new StringBuffer();
	
	public ConditionBuilder(String logicalOperator) {
		this.logicalOperator = logicalOperator;
	}
	
	public Object makeSql() {
		String sql = this.sqlClause.toString();
		if( sql.length() == 0) sql = "1=1";
		return String.format("(%s)", sql);
	}

	public void appendCondition(String condition){
		if( this.sqlClause.length() > 0 ){
			this.sqlClause.append(String.format(" %s ", this.logicalOperator));
		}
		this.sqlClause.append(condition);
	}
}