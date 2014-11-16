package net.longersoft.data.metadata.services;

import net.longersoft.data.context.ExecutionContext;
import net.longersoft.data.context.ExecutionContextFun;
import net.longersoft.data.context.ExecutionContextHelper;
import net.longersoft.data.metadata.bll.AttributeMetadata;
import net.longersoft.data.metadata.bll.EntityMetadata;
import net.longersoft.data.metadata.helpers.ActionType;
import net.longersoft.data.metadata.helpers.MetadataHelper;
import net.longersoft.framework.LongerWebService;
import net.longersoft.framework.ServiceSession;

public class AttributeService extends LongerWebService {
	public static String createInternal(AttributeInfo attributeInfo, MetadataHelper helper, ExecutionContext context) throws Exception{
		AttributeMetadata attributeBag = attributeInfo.getBag();
		helper.addToQueue(attributeBag, ActionType.Create);
		
		return attributeBag.get_AttributeId();
	}

	/// 创建属性
	public String create(final EntityMetadata entity, final AttributeInfo attributeInfo) throws Exception {
		final String[] returnValue = new String[1];
		ExecutionContextHelper.tran(session, new ExecutionContextFun() {
			@Override
			public void call(ExecutionContext context) throws Exception {
				returnValue[0] = create(entity, attributeInfo, context);
			}
		});
		return returnValue[0];
	}

	public String create(EntityMetadata entity, AttributeInfo attributeInfo, ExecutionContext context) throws Exception {
		MetadataHelper helper = new MetadataHelper(context);
		AttributeMetadata bag = attributeInfo.getBag();
		bag.set_Entity(entity.get_EntityId(), entity.get_EntityName());
		String attributeId = createInternal(attributeInfo, helper, context);
		helper.processQueue();
		return attributeId;
	}

	/// 删除属性
	public void delete(final String attributeId) throws Exception {
		ExecutionContextHelper.tran(session, new ExecutionContextFun() {
			@Override
			public void call(ExecutionContext context) throws Exception {
				delete(attributeId, context);
			}
		});
	}

	protected void delete(String attributeId, ExecutionContext context) throws Exception {
		if( attributeId == null) return;
		
		MetadataHelper helper = new MetadataHelper(context);
		AttributeMetadata attributeInfo = new AttributeMetadata();
		attributeInfo.set_AttributeId(attributeId);
		helper.addToQueue(attributeInfo, ActionType.Delete);
		helper.processQueue();
	}
	
	/// 修改属性
	public void update(final String attributeId, final AttributeInfo attributeInfo) throws Exception{
		ExecutionContextHelper.tran(session, new ExecutionContextFun() {
			@Override
			public void call(ExecutionContext context) throws Exception {
				update(attributeId, attributeInfo, context);
			}
		});
	}

	protected void update(String attributeId, AttributeInfo attributeInfo, ExecutionContext context) throws Exception {
		if( attributeId == null ) return;
		
		MetadataHelper helper = new MetadataHelper(context);
		AttributeMetadata updateAttribute = new AttributeMetadata();
		updateAttribute.set_AttributeId(attributeId);
		attributeInfo.setForUpdate(updateAttribute);
		helper.addToQueue(updateAttribute, ActionType.Update);
		helper.processQueue();
	}
}






