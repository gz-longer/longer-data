package net.longersoft.services;

import java.util.List;
import java.util.Map;

import net.longersoft.data.EntityRow;
import net.longersoft.data.linq.expressions.ConditionOperator;
import net.longersoft.data.linq.expressions.Q;
import net.longersoft.data.metadata.bll.AttributeMetadata;
import net.longersoft.data.metadata.bll.EntityMetadata;
import net.longersoft.data.metadata.bll.MetadataModule;
import net.longersoft.framework.LongerWebService;
import net.longersoft.helpers.Throw;

public class MetadataService extends LongerWebService{
	public GetModuleListResponse getModuleList(GetModuleListRequest request) throws Exception{
		Q q = Q.from(MetadataModule.EntityName + MetadataModule.TABLE_TAIL)
		.select("*");
		List<EntityRow> rows = q.returnCollection();
		
		return (GetModuleListResponse) new GetModuleListResponse().returnRows(rows);
	}
	
	public GetEntityListResponse getEntityList(GetEntityListRequest request) throws Exception{
		Q q = Q.from(EntityMetadata.EntityName + EntityMetadata.TABLE_TAIL)
			.where("moduleid", ConditionOperator.Equal, request.moduleId)
			.select("*");
		
		List<EntityRow> rows = q.returnCollection();
		
		return (GetEntityListResponse) new GetEntityListResponse().returnRows(rows);
	}
	
	public GetEntityResponse getEntity(GetEntityRequest request) throws Exception{
		EntityMetadata entity = new EntityMetadata().initByName(request.entityName);
		return (GetEntityResponse) new GetEntityResponse().returnResult(entity);
	}
	
	public GetReferMeEntitiesResponse getReferMeEntities(GetReferMeEntitiesRequest request) throws Exception{
		EntityMetadata entity = new EntityMetadata().initByName(request.entityName);
		return (GetReferMeEntitiesResponse) new GetReferMeEntitiesResponse().returnRows(entity.getReferMeEntities());
	}
	
	public GetMeReferEntitiesResponse getMeReferEntities(GetMeReferEntitiesRequest request) throws Exception{
		EntityMetadata entity = new EntityMetadata().initByName(request.entityName);
		return (GetMeReferEntitiesResponse) new GetMeReferEntitiesResponse().returnRows(entity.getMeReferEntities());
	}
	
	public GetReferMeShipsResponse getReferMeShips(GetReferMeShipsRequest request) throws Exception{
		EntityMetadata entity = new EntityMetadata().initByName(request.entityName);
		return (GetReferMeShipsResponse) new GetReferMeShipsResponse().returnRows(entity.getReferMeShips());
	}
	
	public GetMeReferShipsResponse getMeReferShips(GetMeReferShipsRequest request) throws Exception{
		EntityMetadata entity = new EntityMetadata().initByName(request.entityName);
		return (GetMeReferShipsResponse) new GetMeReferShipsResponse().returnRows(entity.getMeReferShips());
	}
	
	
	public GetAttributeListResponse getAttributeList(GetAttributeListRequest request) throws Exception{
		EntityMetadata entity = new EntityMetadata().initByName(request.entityName);
		return (GetAttributeListResponse) new GetAttributeListResponse().returnRows(entity.getAttributeList());
	}
}
