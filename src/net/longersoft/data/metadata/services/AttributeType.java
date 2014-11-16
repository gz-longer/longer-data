package net.longersoft.data.metadata.services;

public enum AttributeType {
	Boolean,
	String,
	Integer,
	Float,
	PrimaryKey,
	PrimaryField,
	ParentField,
	Lookup,
	Pickup,
	DateTime,
	;
	
	public static AttributeType fromName(String attributeTypeId){
		return AttributeType.valueOf(AttributeType.class, attributeTypeId);
	}
	
	public String toAttributeTypeId(){
		return this.name();
	}
}
