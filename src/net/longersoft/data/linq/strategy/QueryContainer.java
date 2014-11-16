package net.longersoft.data.linq.strategy;

import java.util.Hashtable;

import net.longersoft.data.linq.expressions.Q;

public class QueryContainer {
	private StringBuffer fromClause = new StringBuffer();
	private StringBuffer whereClause = new StringBuffer();
	private StringBuffer orderbyClause = new StringBuffer();
	private StringBuffer groupbyClause = new StringBuffer();
	private StringBuffer selectClause = new StringBuffer();
	private StringBuffer distinctClause = new StringBuffer();
	private StringBuffer linkClause = new StringBuffer();
	private StringBuffer pagingClause = new StringBuffer();
	private Hashtable<String, String> attributes = new Hashtable<String, String>();
	private Q query = null;
	
	public StringBuffer getFromClause() {
		return fromClause;
	}
	public StringBuffer getWhereClause() {
		return whereClause;
	}
	public StringBuffer getOrderbyClause() {
		return orderbyClause;
	}
	public StringBuffer getSelectClause() {
		return selectClause;
	}
	public StringBuffer getDistinctClause() {
		return distinctClause;
	}
	public Hashtable<String, String> getAttributes() {
		return attributes;
	}
	public void setQuery(Q query) {
		this.query = query;
	}
	public Q getQuery() {
		return query;
	}
	public StringBuffer getLinkClause() {
		return linkClause;
	}
	public StringBuffer getPagingClause() {
		return pagingClause;
	}
	public StringBuffer getGroupbyClause() {
		return groupbyClause;
	}
}
