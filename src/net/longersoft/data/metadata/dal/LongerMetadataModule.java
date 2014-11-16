package net.longersoft.data.metadata.dal;

import net.longersoft.data.database.annotations.DisplayName;
import net.longersoft.data.metadata.module.LongerDataModule;
import net.longersoft.framework.LongerModule;

@DisplayName("元数据模块")
public class LongerMetadataModule extends LongerDataModule{
	@Override
	public LongerModule[] getMeDependentOnModules() throws Exception {
		return null;
	}
	
	public Class<?>[] getClasses(){
		return new Class<?>[]{
			_MetadataAttributeRow.class,
			_MetadataEntityRow.class,
			_MetadataEntityRuleRow.class,
			_MetadataModuleRow.class,
			_MetadataNamespaceRow.class,
			_MetadataRelationshipRow.class
		};
	}

	@Override
	protected void init() throws Exception {
	}
}
