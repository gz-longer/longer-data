package net.longersoft.data.metadata.dal;

import net.longersoft.data.EntityRow;
import net.longersoft.data.database.annotations.*;

@Entity(_MetadataNamespaceRow.EntityName)
@DisplayName(_MetadataNamespaceRow.DisplayName)
public class _MetadataNamespaceRow extends EntityRow{
	public static final String EntityName = "metadata_namespace";
	public static final String DisplayName = "命名空间";
	
	@PrimaryKey
	private String namespaceId;
	@PrimaryField
	@DisplayName("名称")
	@RequiredLevel(RequiredLevel.Required)
	private String namespaceName;
	@ParentField
	@DisplayName("父节点")
	private String namespacePid;
	@Attribute
	@DisplayName("显示名称")
	private String displayName;
	
	public _MetadataNamespaceRow() {
		super(EntityName);
	}

	public void set_NamespaceId(String namespaceId) {
		this.setAttributeValue("namespaceId", namespaceId);
	}

	public String get_NamespaceId() {
		return (String)this.getAttributeValue("namespaceId");
	}

	public void set_NamespaceName(String namespaceName) {
		this.setAttributeValue("namespaceName", namespaceName);
	}

	public String get_NamespaceName() {
		return (String)this.getAttributeValue("namespaceName");
	}

	public void set_NamespacePid(String namespacePid) {
		this.setAttributeValue("namespacePid", namespacePid);
	}

	public String get_NamespacePid() {
		return (String)this.getAttributeValue("namespacePid");
	}

	
}
