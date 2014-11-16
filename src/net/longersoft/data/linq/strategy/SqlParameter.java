package net.longersoft.data.linq.strategy;

public class SqlParameter{
	private Integer index = 0;
	private Object value = null;
	private String name = null;
	
	public SqlParameter(String name, Integer index, Object value) {
		this.setIndex(index);
		this.setValue(value);
		this.setName(name);
	}
	private void setIndex(Integer index) {
		this.index = index;
	}
	public Integer getIndex() {
		return index;
	}
	private void setValue(Object value) {
		this.value = value;
	}
	public Object getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return String.format("%s@%s = %s", this.getName(), this.getIndex(), this.getValue());
	}
	private void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
}
