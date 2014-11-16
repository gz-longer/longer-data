package net.longersoft.data.metadata.services;

import java.util.Hashtable;

import net.longersoft.data.context.ExecutionContext;

public class AttributeTypes {

	public static AttributeTypeInfo getAttributeTypeInfo(AttributeType attributeType){
		return ExecutionContext.getSqlProvider().getAttributeTypeInfo(attributeType);
	}
}

