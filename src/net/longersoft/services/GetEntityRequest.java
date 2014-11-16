package net.longersoft.services;

import java.util.Map;

import net.longersoft.framework.ServiceRequest;
import net.longersoft.helpers.MapHelper;

public class GetEntityRequest extends ServiceRequest {
	String entityName;
	@Override
	public void fromClient(Map<String, Object> map) throws Exception {
		super.fromClient(map);
		this.entityName = MapHelper.getAndRemove(map, "entityname");
	}
}
