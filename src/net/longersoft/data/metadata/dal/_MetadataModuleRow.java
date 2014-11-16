package net.longersoft.data.metadata.dal;

import java.util.Date;

import net.longersoft.data.EntityRow;
import net.longersoft.data.database.annotations.*;

@Entity(_MetadataModuleRow.EntityName)
@DisplayName(_MetadataModuleRow.DisplayName)
public class _MetadataModuleRow extends EntityRow {
	public static final String EntityName = "metadata_module";
	public static final String DisplayName = "模块";
	
	@PrimaryKey
	private String moduleId;
	@PrimaryField
	@DisplayName("模块名称")
	@RequiredLevel(RequiredLevel.Required)
	private String moduleName;
	@Attribute
	@DisplayName("安装状态")
	@RequiredLevel(RequiredLevel.Required)
	private String installStatus;
	@Attribute
	@DisplayName("安装时间")
	@RequiredLevel(RequiredLevel.Required)
	private Date installTime;
	@Attribute
	@DisplayName("显示名称")
	private String displayName;
	
	public _MetadataModuleRow() {
		super(EntityName);
	}

	public void set_ModuleId(String moduleId) {
		this.setAttributeValue("moduleId", moduleId);
	}

	public String get_ModuleId() {
		return (String)this.getAttributeValue("moduleId");
	}

	public void set_ModuleName(String moduleName) {
		this.setAttributeValue("moduleName", moduleName);
	}

	public String get_ModuleName() {
		return (String)this.getAttributeValue("moduleName");
	}

	public void set_InstallStatus(String installStatus) {
		this.setAttributeValue("installStatus", installStatus);
	}

	public String get_InstallStatus() {
		return (String)this.getAttributeValue("installStatus");
	}

	public void set_InstallTime(Date installTime) {
		this.setAttributeValue("installTime", installTime);
	}

	public Date get_InstallTime() {
		return (Date)this.getAttributeValue("installTime");
	}

	public void set_DisplayName(String displayName) {
		this.setAttributeValue("displayName", displayName);
	}

	public String get_DisplayName() {
		return (String)this.getAttributeValue("displayName");
	}
}
