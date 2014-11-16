package net.longersoft.data.metadata.services;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sun.org.apache.regexp.internal.RE;

import net.longersoft.data.EntityRow;
import net.longersoft.data.context.ExecutionContext;
import net.longersoft.data.context.ExecutionContextFun;
import net.longersoft.data.context.ExecutionContextHelper;
import net.longersoft.data.database.annotations.RequiredLevel;
import net.longersoft.data.linq.expressions.Q;
import net.longersoft.data.metadata.bll.AttributeMetadata;
import net.longersoft.data.metadata.bll.EntityMetadata;
import net.longersoft.data.metadata.bll.MetadataErrorCodes;
import net.longersoft.data.metadata.helpers.ActionType;
import net.longersoft.data.metadata.helpers.MetadataHelper;
import net.longersoft.exceptions.ErrorCodes;
import net.longersoft.exceptions.LongerException;
import net.longersoft.framework.LongerWebService;
import net.longersoft.framework.ServiceRequest;
import net.longersoft.framework.ServiceResponse;

public class EntityService extends LongerWebService {
	private Logger log = Logger.getLogger(EntityService.class);
	
	private int normalColumnNumber = 10;
	
	public EntityService() {
	}
	
	public String create(EntityCreateInfo info, ExecutionContext context) throws Exception{
		String result = createInternal(info, context);
		return result;
	}
	
	/// 创建实体
	public String create(final EntityCreateInfo info) throws Exception{
		final String[] returnValue = new String[1];
		ExecutionContextHelper.tran(session, new ExecutionContextFun() {
			@Override
			public void call(ExecutionContext context) throws Exception {
				returnValue[0] = EntityService.this.create(info, context);
			}
		});
		
		return returnValue[0];
	}

	private String createInternal(EntityCreateInfo info, ExecutionContext context) throws Exception {
		// check
		checkEntityNotExists(info);
		
		MetadataHelper helper = new MetadataHelper(context);
		EntityMetadata bag = info.getBag();
		helper.addToQueue(bag, ActionType.Create);
		this.addSystemFields(info, helper, context);
		this.createPrimaryField(info, helper, context);
		this.createNormalFields(info, helper, context);
		this.createAuditFields(info, helper, context);
		helper.processQueue();
		return bag.get_EntityId();
	}

	private void checkEntityNotExists(EntityCreateInfo info) throws Exception {
		EntityMetadata entity = new EntityMetadata();
		String entityName = info.getBag().get_EntityName();
		entity.set_EntityName(entityName);
		try{
			entity.fillIt(session, "entityid");
			throw new LongerException(MetadataErrorCodes.Entity_AlreadyExists, String.format("entity '%s' already exists.", entityName));
		}catch(LongerException le){
			if(MetadataErrorCodes.Entity_NotFound.equals(le.getErrorCode())) return;
			throw le;
		}catch(Exception e){
			throw e;
		}
	}

	private void createNormalFields(EntityCreateInfo info,
			MetadataHelper helper, ExecutionContext context) throws Exception {
		if(info.getNormalFields().size() == 0) return;
		
		for(AttributeInfo a : info.getNormalFields()){
			AttributeMetadata bag = a.getBag();
			bag.set_Entity(info.getBag().get_EntityId(), info.getBag().get_EntityName());
			bag.set_ColumnNumber(this.normalColumnNumber++);
			
			AttributeService.createInternal(a, helper, context);
		}
	}

	private void createPrimaryField(EntityCreateInfo info,
			MetadataHelper helper, ExecutionContext context) throws Exception {
		if(info.getPrimaryField() == null)return;
		
		EntityMetadata entityBag = info.getBag();
		AttributeMetadata bag = info.getPrimaryField().getBag();
		bag.set_Entity(entityBag.get_EntityId(), entityBag.get_EntityName());
		bag.set_IsPrimaryAttribute(true);
		bag.set_ColumnNumber(1);
		bag.set_RequiredLevel(RequiredLevel.Recommend);
				
		AttributeService.createInternal(info.getPrimaryField(), helper, context);
	}
	
	private void createAuditFields(EntityCreateInfo info,
			MetadataHelper helper, ExecutionContext context) throws Exception {
		EntityMetadata entityBag = info.getBag();
		String entityId = entityBag.get_EntityId();
		String entityName = entityBag.get_EntityName();
		{
			AttributeInfo attr = new AttributeInfo(AttributeType.DateTime);
			AttributeMetadata bag = attr.getBag();
			bag.set_Entity(entityId, entityName);
			bag.set_AttributeName("createOn");
			bag.set_DisplayName("创建时间");
			bag.set_ColumnNumber(901);
			bag.set_RequiredLevel(RequiredLevel.Required);
			AttributeService.createInternal(attr, helper, context);
		}
		{
			AttributeInfo attr = new AttributeInfo(AttributeType.String);
			AttributeMetadata bag = attr.getBag();
			bag.set_Entity(entityId, entityName);
			bag.set_AttributeName("createBy");
			bag.set_DisplayName("创建人");
			bag.set_ColumnNumber(902);
			bag.set_Length(36);
			bag.set_RequiredLevel(RequiredLevel.Required);
			AttributeService.createInternal(attr, helper, context);
		}
		{
			AttributeInfo attr = new AttributeInfo(AttributeType.DateTime);
			AttributeMetadata bag = attr.getBag();
			bag.set_Entity(entityId, entityName);
			bag.set_AttributeName("modifyOn");
			bag.set_DisplayName("修改时间");
			bag.set_ColumnNumber(903);
			bag.set_RequiredLevel(RequiredLevel.Optional);
			AttributeService.createInternal(attr, helper, context);
		}
		{
			AttributeInfo attr = new AttributeInfo(AttributeType.String);
			AttributeMetadata bag = attr.getBag();
			bag.set_Entity(entityId, entityName);
			bag.set_AttributeName("modifyBy");
			bag.set_DisplayName("修改人");
			bag.set_ColumnNumber(904);
			bag.set_Length(36);
			bag.set_RequiredLevel(RequiredLevel.Optional);
			AttributeService.createInternal(attr, helper, context);
		}
	}

	private void addSystemFields(EntityCreateInfo info, MetadataHelper helper,
			ExecutionContext context) throws Exception {
		this.createPrimaryKey(info, helper, context);
	}

	private void createPrimaryKey(EntityCreateInfo info, MetadataHelper helper,
			ExecutionContext context) throws Exception {
		EntityMetadata entityBag = info.getBag();
		
		AttributeInfo pkInfo = null;
		if(info.getPrimaryKey() != null){
			pkInfo = info.getPrimaryKey();
		}else{
			pkInfo = new AttributeInfo(AttributeType.PrimaryKey);
			AttributeMetadata bag = pkInfo.getBag();
			bag.set_AttributeName(entityBag.get_EntityName() + "id");
			bag.set_RequiredLevel(RequiredLevel.Required);
		}
		
		pkInfo.getBag().set_Entity(entityBag.get_EntityId(), entityBag.get_EntityName());
		pkInfo.getBag().set_ColumnNumber(0);
		AttributeService.createInternal(pkInfo, helper, context);
	}

	/// 删除实体
	public void delete(final String entityId) throws Exception {
		ExecutionContextHelper.tran(session, new ExecutionContextFun() {
			@Override
			public void call(ExecutionContext context) throws Exception {
				delete(entityId, context);
			}
		});
	}

	public void delete(String entityId, ExecutionContext context) throws Exception {
		deleteInternal(entityId, context);
	}

	private void deleteInternal(String entityId, ExecutionContext context) throws Exception {
		if(entityId == null) return;
		
		MetadataHelper helper = new MetadataHelper(context);
		EntityMetadata entityBag = new EntityMetadata();
		entityBag.set_EntityId(entityId);
		helper.addToQueue(entityBag, ActionType.Delete);
		helper.processQueue();
	}
	
	public void update(final String entityId, final EntityUpdateInfo updateInfo) throws Exception{
		ExecutionContextHelper.tran(session, new ExecutionContextFun() {
			@Override
			public void call(ExecutionContext context) throws Exception {
				update(entityId, updateInfo, context);
			}
		});
	}
	
	protected void update(String entityId, EntityUpdateInfo updateInfo, ExecutionContext context) throws Exception {
		if(entityId == null) return;
		
		MetadataHelper helper = new MetadataHelper(context);
		EntityMetadata entityBag = updateInfo.getInnerData();
		entityBag.set_EntityId(entityId);
		helper.addToQueue(entityBag, ActionType.Update);
		helper.processQueue();
	}

	@Override
	public ServiceResponse handle(ServiceRequest request)
			throws Throwable {
		return super.handle(request);
	}
}
