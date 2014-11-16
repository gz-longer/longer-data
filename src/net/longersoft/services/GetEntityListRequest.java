package net.longersoft.services;

import java.util.Map;

import net.longersoft.framework.ServiceRequest;
import net.longersoft.helpers.MapHelper;

public class GetEntityListRequest extends ServiceRequest {
	String moduleId;
	@Override
	public void fromClient(Map<String, Object> map) throws Exception {
		super.fromClient(map);
		this.moduleId = MapHelper.getAndRemove(map, "moduleid");
	}
}
