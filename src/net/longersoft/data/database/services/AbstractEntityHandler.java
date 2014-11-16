package net.longersoft.data.database.services;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import net.longersoft.data.database.annotations.DefaultValue;
import net.longersoft.data.database.annotations.DisplayName;
import net.longersoft.data.database.annotations.Entity;
import net.longersoft.data.database.annotations.ForeinKey;
import net.longersoft.data.database.annotations.ParentField;
import net.longersoft.data.database.annotations.PrimaryField;
import net.longersoft.data.database.annotations.PrimaryKey;
import net.longersoft.data.database.annotations.RequiredLevel;
import net.longersoft.data.metadata.bll.AttributeMetadata;
import net.longersoft.data.metadata.services.AttributeInfo;
import net.longersoft.data.metadata.services.AttributeType;
import net.longersoft.framework.LongerModule;
import net.longersoft.framework.ServiceSession;

public abstract class AbstractEntityHandler {
	protected LongerModule module;
	protected String entityName;
	protected String displayName;
	protected List<AttributeInfo> attributes;
	protected Class<?> entityClazz;	
	protected boolean inited = false;
	protected ServiceSession system = null;
	
	public AbstractEntityHandler(LongerModule module, Class<?> clazz){
		this.module = module;
		this.entityClazz = clazz;
		this.system = new ServiceSession().initByUserId("system");
	}
	
	public void initialize() throws Exception{
		this.entityName = this.getEntityName();
		this.displayName = this.getDisplayName();
		this.attributes = makeAttributesFromClass(this.entityClazz);
		this.inited = true;
	}
	

	private String getEntityName() {
		Entity aEntity = (Entity) entityClazz.getAnnotation(Entity.class);
		String entityName = entityClazz.getSimpleName();
		if(aEntity != null) entityName = aEntity.value();
		
		return entityName;
	}
	
	private String getDisplayName(){
		DisplayName aDisplayName = (DisplayName) entityClazz.getAnnotation(DisplayName.class);
		if(aDisplayName != null) return aDisplayName.value();
		
		return getEntityName();
	}
	
	private List<AttributeInfo> makeAttributesFromClass(Class<?> clazz) {
		ArrayList<AttributeInfo> list = new ArrayList<AttributeInfo>();
		
		for(Field f : clazz.getDeclaredFields()){
			Annotation[] annotations = f.getAnnotations();
			if(annotations == null || annotations.length == 0) continue;
			
			makeAttributeFromField(list, f);
		}
		return list;
	}

	private void makeAttributeFromField(ArrayList<AttributeInfo> list, Field f) {
		String attributeName = f.getName();
		
		Boolean isPrimaryKey = f.getAnnotation(PrimaryKey.class) != null;
		Boolean isPrimaryField = f.getAnnotation(PrimaryField.class) != null;
		Boolean isParentField = f.getAnnotation(ParentField.class) != null;
		DisplayName aDisplayName = f.getAnnotation(DisplayName.class);
		String displayName = attributeName;
		if(aDisplayName != null) displayName = aDisplayName.value();
		
		ForeinKey aForeinKey = f.getAnnotation(ForeinKey.class);
		Boolean isForeinKey = aForeinKey != null;
		
		int requiredLevel = RequiredLevel.Optional;
		if(isPrimaryField) requiredLevel = RequiredLevel.Recommend;
		if(isPrimaryKey) requiredLevel = RequiredLevel.Required;
		
		RequiredLevel aRequireLevel = f.getAnnotation(RequiredLevel.class);
		if(aRequireLevel != null) requiredLevel = aRequireLevel.value();
		
		AttributeType attributeType = getAttributeType(f);
		if(isForeinKey) attributeType = AttributeType.Lookup;
		if(isPrimaryField) attributeType = AttributeType.PrimaryField;
		if(isParentField) attributeType = AttributeType.ParentField;
		if(isPrimaryKey) attributeType = AttributeType.PrimaryKey;
		
		DefaultValue aDefaultValue = f.getAnnotation(DefaultValue.class);
		String defaultValue = "";
		switch(attributeType){
		case Boolean: defaultValue = "0"; break;
		case DateTime: defaultValue = "2000-1-1"; break;
		case Float: defaultValue = "0.0"; break;
		case Integer: defaultValue = "0"; break;
		default: break;
		}
		if(aDefaultValue!= null) defaultValue = aDefaultValue.value();
		
		AttributeInfo attributeInfo = new AttributeInfo(attributeType);
		AttributeMetadata bag = attributeInfo.getBag();
		bag.set_AttributeId(UUID.randomUUID().toString());
		bag.set_AttributeName(attributeName);
		bag.set_DisplayName(displayName);
		bag.set_RequiredLevel(requiredLevel);
		
		if(defaultValue != "")bag.set_DefaultValue(defaultValue);
		
		bag.set_IsLookupAttribute(isForeinKey);
		bag.set_IsPrimaryKey(isPrimaryKey);
		bag.set_IsParentAttribute(isParentField);
		bag.set_IsPrimaryAttribute(isPrimaryField);
		
		if(isForeinKey){
			bag.set_LookupEntityName(aForeinKey.value());
		}
		
		list.add(attributeInfo);
		
		if(isForeinKey){
			AttributeInfo nameAttribute = makeForeinKeyNameAttribute(attributeInfo);
			list.add(nameAttribute);
		}
	}

	private AttributeInfo makeForeinKeyNameAttribute(AttributeInfo foreinInfo) {
		AttributeInfo attributeInfo = new AttributeInfo(AttributeType.String);
		AttributeMetadata bag = attributeInfo.getBag();
		bag.set_AttributeId(UUID.randomUUID().toString());
		bag.set_AttributeName(foreinInfo.getBag().get_AttributeName() + "_name");
		bag.set_DisplayName(foreinInfo.getBag().get_DisplayName()); 
		bag.set_IsLogical(true);
		return attributeInfo;
	}
	
	private List<AttributeInfo> makeAuditFields(){
		List<AttributeInfo> audits = new ArrayList<AttributeInfo>();
		
		{
			AttributeInfo createby = new AttributeInfo(AttributeType.String);
			AttributeMetadata bag = createby.getBag();
			bag.set_AttributeId(UUID.randomUUID().toString());
			bag.set_AttributeName("__createby");
			bag.set_DisplayName("创建人ID"); 
			audits.add(createby);
		}
		
		{
			AttributeInfo modifyby = new AttributeInfo(AttributeType.String);
			AttributeMetadata bag = modifyby.getBag();
			bag.set_AttributeId(UUID.randomUUID().toString());
			bag.set_AttributeName("__modifyby");
			bag.set_DisplayName("修改人ID"); 
			audits.add(modifyby);			
		}
		
		{
			AttributeInfo createby_name = new AttributeInfo(AttributeType.String);
			AttributeMetadata bag = createby_name.getBag();
			bag.set_AttributeId(UUID.randomUUID().toString());
			bag.set_AttributeName("__createby_name");
			bag.set_DisplayName("创建人姓名"); 
			audits.add(createby_name);
		}
		
		{
			AttributeInfo modifyby_name = new AttributeInfo(AttributeType.String);
			AttributeMetadata bag = modifyby_name.getBag();
			bag.set_AttributeId(UUID.randomUUID().toString());
			bag.set_AttributeName("__modifyby_name");
			bag.set_DisplayName("修改人姓名"); 
			audits.add(modifyby_name);			
		}
		
		{
			AttributeInfo createtime = new AttributeInfo(AttributeType.DateTime);
			AttributeMetadata bag = createtime.getBag();
			bag.set_AttributeId(UUID.randomUUID().toString());
			bag.set_AttributeName("__createtime");
			bag.set_DisplayName("创建时间"); 
			audits.add(createtime);
		}
		
		{
			AttributeInfo modifytime = new AttributeInfo(AttributeType.DateTime);
			AttributeMetadata bag = modifytime.getBag();
			bag.set_AttributeId(UUID.randomUUID().toString());
			bag.set_AttributeName("__modifytime");
			bag.set_DisplayName("修改时间"); 
			audits.add(modifytime);			
		}
				
		return audits;
	}

	private AttributeType getAttributeType(Field f) {
		Class<?> type = f.getType();
		if( type.equals(Integer.class)) return AttributeType.Integer;
		if( type.equals(Float.class)) return AttributeType.Float;
		if( type.isEnum()) return AttributeType.String;
		if( type.equals(Boolean.class)) return AttributeType.Boolean;
		if( type.equals(Date.class)) return AttributeType.DateTime;
		
		return AttributeType.String;
	}	
}
