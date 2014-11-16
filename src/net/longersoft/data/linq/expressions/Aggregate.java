package net.longersoft.data.linq.expressions;

public class Aggregate {
    private String alias;
    private String attribute;
    private AggregateType type = AggregateType.None;
    
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getAlias() {
		return alias;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setType(AggregateType type) {
		this.type = type;
	}
	public AggregateType getType() {
		return type;
	}
    
    
}


