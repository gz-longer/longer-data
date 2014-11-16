package net.longersoft.data.metadata.dal;

import javax.swing.text.StyledEditorKit.BoldAction;

import net.longersoft.data.EntityRow;
import net.longersoft.data.database.annotations.*;
import net.longersoft.data.metadata.services.AttributeType;

@Entity(_MetadataAttributeRow.EntityName)
@DisplayName(_MetadataAttributeRow.DiaplayName)
public class _MetadataAttributeRow extends EntityRow {
	public static final String EntityName = "metadata_attribute";
	public static final String DiaplayName = "属性";
	
	@PrimaryKey
	private String attributeId;
	@PrimaryField
	@DisplayName("属性名称")
	private String attributeName;
	@ForeinKey(_MetadataEntityRow.EntityName)
	private String entityId;
	@Attribute
	@DisplayName("属性类型")
	@RequiredLevel(RequiredLevel.Required)
	private AttributeType attributeTypeId;
	@Attribute
	@RequiredLevel(RequiredLevel.Required)
	private String requiredLevel;
	@Attribute
	@DisplayName("输入法")
	private String imeMode;
	@Attribute
	@DisplayName("默认值")
	private String defaultValue;
	@Attribute
	@DisplayName("是否可空")
	private Boolean isNullable;
	@Attribute
	@DisplayName("逻辑属性")
	private Boolean isLogical;
	@Attribute
	@DisplayName("列序号")
	@RequiredLevel(RequiredLevel.Required)
	private Integer columnNumber;
	@Attribute
	@DisplayName("是否主键")
	@RequiredLevel(RequiredLevel.Required)
	private Boolean isPrimaryKey;
	@Attribute
	@DisplayName("是否主属性")
	@RequiredLevel(RequiredLevel.Required)
	private Boolean isPrimaryAttribute;
	@Attribute
	@DisplayName("是否父属性")
	@RequiredLevel(RequiredLevel.Required)
	private Boolean isParentAttribute;
	@Attribute
	@DisplayName("是否查找属性")
	@RequiredLevel(RequiredLevel.Required)
	private Boolean isLookupAttribute;
	@Attribute
	@DisplayName("查找实体")
	private String lookupEntityName;
	@Attribute
	@DisplayName("字段长度")
	private Integer length;
	@Attribute
	@DisplayName("显示名称")
	private String displayName;
	@Attribute
	@DisplayName("显示在列表视图")
	@RequiredLevel(RequiredLevel.Required)
	private Boolean showInListView;
	@Attribute
	@DisplayName("显示在查找视图")
	@RequiredLevel(RequiredLevel.Required)
	private Boolean showInLookupView;
	@Attribute
	@DisplayName("显示在表单视图")
	@RequiredLevel(RequiredLevel.Required)
	private Boolean showInFormView;
	
	public _MetadataAttributeRow() {
		super(EntityName);
	}

	@Override
	public void init() {
		this.set_IsLogical(false);
		this.set_IsNullable(true);
		this.set_ColumnNumber(1000);
		this.set_IsPrimaryKey(false);
		this.set_IsPrimaryAttribute(false);
		this.set_IsParentAttribute(false);
		this.set_IsLookupAttribute(false);
		this.set_Length(0);
	}

	public void set_AttributeId(String attributeId) {
		super.setAttributeValue("attributeId", attributeId);
	}

	public String get_AttributeId() {
		return (String)super.getAttributeValue("attributeId");
	}

	public void set_AttributeName(String attributeName) {
		super.setAttributeValue("attributeName", attributeName);
	}

	public String get_AttributeName() {
		return (String)super.getAttributeValue("attributeName");
	}

	public void set_DefaultValue(String defaultValue) {
		super.setAttributeValue("defaultValue", defaultValue);
	}

	public String get_DefaultValue() {
		return (String)super.getAttributeValue("defaultValue");
	}

	public void set_IsNullable(Boolean isNullable) {
		super.setAttributeValue("isNullable", isNullable);
	}

	public Boolean get_IsNullable() {
		return (Boolean)super.getAttributeValue("isNullable");
	}

	public void set_IsLogical(Boolean isLogical) {
		super.setAttributeValue("isLogical", isLogical);
	}

	public Boolean get_IsLogical() {
		return (Boolean)super.getAttributeValue("isLogical");
	}

	public void set_ColumnNumber(int columnNumber) {
		super.setAttributeValue("columnNumber", columnNumber);
	}

	public Integer get_ColumnNumber() {
		return (Integer)super.getAttributeValue("columnNumber");
	}

	public void set_IsPrimaryKey(Boolean isPrimaryKey) {
		super.setAttributeValue("isPrimaryKey", isPrimaryKey);
	}

	public Boolean get_IsPrimaryKey() {
		return (Boolean)super.getAttributeValue("isPrimaryKey");
	}

	public void set_IsPrimaryAttribute(Boolean isPrimaryAttribute) {
		super.setAttributeValue("isPrimaryAttribute", isPrimaryAttribute);
	}

	public Boolean get_IsPrimaryAttribute() {
		return (Boolean)super.getAttributeValue("isPrimaryAttribute");
	}
	
	public void set_IsParentAttribute(Boolean isParentAttribute) {
		super.setAttributeValue("isParentAttribute", isParentAttribute);
	}

	public Boolean get_IsParentAttribute() {
		return (Boolean)super.getAttributeValue("isParentAttribute");
	}	
	
	public void set_IsLookupAttribute(Boolean isLookupAttribute){
		super.setAttributeValue("isLookupAttribute", isLookupAttribute);
	}
	
	public Boolean get_IsLookupAttribute(){
		return (Boolean)super.getAttributeValue("isLookupAttribute");
	}
	
	public void set_LookupEntityName(String lookupEntityName){
		super.setAttributeValue("lookupEntityName", lookupEntityName);
	}
	
	public String get_LookupEntityName(){
		return(String)super.getAttributeValue("lookupEntityName");
	}

	public void set_Entity(String entityId, String entityName) {
		super.setAttributeValue("entityId", entityId);
		super.setAttributeValue("entityId_name", entityName);
	}

	public String get_EntityId() {
		return (String)super.getAttributeValue("entityId");
	}
	
	public String get_EntityId_name(){
		return (String)super.getAttributeValue("entityId_name");
	}

	public void set_AttributeType(AttributeType attributeType) {
		super.setAttributeValue("attributeTypeId", attributeType.toAttributeTypeId());
	}

	public AttributeType get_AttributeType() {
		return AttributeType.fromName((String)super.getAttributeValue("attributeTypeId"));
	}

	public void set_Length(int length) {
		super.setAttributeValue("length", length);
	}

	public Integer get_Length() {
		return (Integer)super.getAttributeValue("length");
	}

	public String get_DisplayName(){
		return (String)this.getAttributeValue("displayName");
	}
	
	public void set_DisplayName(String displayName){
		this.setAttributeValue("displayName", displayName);
	}

	public void set_RequiredLevel(Integer requiredLevel) {
		this.setAttributeValue("requiredLevel", requiredLevel);
	}

	public Integer get_RequiredLevel() {
		return (Integer)this.getAttributeValue("requiredLevel");
	}

	public void set_ImeMode(String imeMode) {
		this.imeMode = imeMode;
		this.setAttributeValue("imeMode", imeMode);
	}

	public String get_ImeMode() {
		return (String)this.getAttributeValue("imeMode");
	}

	public void set_ShowInListView(Boolean showInListView) {
		super.setAttributeValue("showInListView", showInListView);
	}

	public Boolean get_ShowInListView() {
		return (Boolean)super.getAttributeValue("showInListView");
	}

	public void set_ShowInLookupView(Boolean showInLookupView) {
		super.setAttributeValue("showInLookupView", showInLookupView);
	}

	public Boolean get_ShowInLookupView() {
		return (Boolean)super.getAttributeValue("showInLookupView");
	}

	public void set_ShowInFormView(Boolean showInFormView) {
		super.setAttributeValue("showInFormView", showInFormView);
	}

	public Boolean get_ShowInFormView() {
		return (Boolean)super.getAttributeValue("showInFormView");
	}

	
}
