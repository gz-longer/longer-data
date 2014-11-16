package net.longersoft.data.metadata.services;

import net.longersoft.data.metadata.bll.EntityMetadata;

public class EntityUpdateInfo {
	private String newDescription;
	private String newDisplayName;
	private String newDisplayCollectionName;
	
	private EntityMetadata bag;
	
	public EntityMetadata getInnerData(){
		return this.bag;
	}
}
