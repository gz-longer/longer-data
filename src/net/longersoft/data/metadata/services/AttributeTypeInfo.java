package net.longersoft.data.metadata.services;

public class AttributeTypeInfo {
	private AttributeType attributeType;
	private String sqlName;
	private Boolean isQuoted;
	private Class<?> type;
	
	public AttributeTypeInfo(AttributeType attributeType, String sqlName, Boolean isQuoted, Class<?> type) {
		this.setAttributeType(attributeType);
		this.setSqlName(sqlName);
		this.setIsQuoted(isQuoted);
		this.setType(type);
	}

	public void setAttributeType(AttributeType attributeType) {
		this.attributeType = attributeType;
	}

	public AttributeType getAttributeType() {
		return attributeType;
	}
	
	public String getAttributeTypeId(){
		return this.attributeType.name();
	}
	
	public void setSqlName(String sqlName) {
		this.sqlName = sqlName;
	}

	public String getSqlName() {
		return sqlName;
	}

	public void setIsQuoted(Boolean isQuoted) {
		this.isQuoted = isQuoted;
	}

	public Boolean getIsQuoted() {
		return isQuoted;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public Class<?> getType() {
		return type;
	}
}
