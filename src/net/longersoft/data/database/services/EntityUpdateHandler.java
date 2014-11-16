package net.longersoft.data.database.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import net.longersoft.data.metadata.bll.AttributeMetadata;
import net.longersoft.data.metadata.bll.EntityMetadata;
import net.longersoft.data.metadata.services.AttributeInfo;
import net.longersoft.data.metadata.services.AttributeService;
import net.longersoft.framework.LongerModule;

public class EntityUpdateHandler extends AbstractEntityHandler {	
	private Logger log = Logger.getLogger(EntityUpdateHandler.class);
	EntityMetadata entity = null;
	AttributeService service = null;

	public EntityUpdateHandler(LongerModule module, Class<?> clazz) {
		super(module, clazz);
	}
	
	@Override
	public void initialize() throws Exception {
		super.initialize();		
		this.entity = new EntityMetadata().initByName(this.entityName);
		this.service = new AttributeService();
	}

	private List<AttributeMetadata> makeAttributesFromDb() throws Exception {
		List<AttributeMetadata> list = this.entity.getAttributeList();
		return list;
	}
	
	public void updateEntity() throws Exception{
		List<AttributeMetadata> newList = new ArrayList<AttributeMetadata>();
		for(AttributeInfo info : this.attributes){
			newList.add(info.getBag());
		}
		
		List<AttributeMetadata> oldList = makeAttributesFromDb();
		
		// attr to add
		List<AttributeMetadata> list2add = getNew(newList, oldList);
		for(AttributeMetadata attr : list2add){
			createAttribute(attr);
		}
		
		// attr to del
		List<AttributeMetadata> list2del = getNew(oldList, newList);
		for(AttributeMetadata attr : list2del){
			deleteAttribute(attr);
		}
		
		// attr to update
		List<AttributeMetadata> list2mod = getDiff(oldList, newList);
		for(AttributeMetadata attr : list2mod){
			updateAttribute(attr);
		}
	}

	private void updateAttribute(AttributeMetadata attr) throws Exception {
		log.info(String.format("updateAttribute() called. attr=%s", attr.get_AttributeName()));
		log.warn("!! not implement !!");
		AttributeInfo info = new AttributeInfo(attr);
		this.service.update(attr.get_AttributeId(), info);
	}

	private void deleteAttribute(AttributeMetadata attr) throws Exception {
		log.info(String.format("deleteAttribute() called. attr=%s", attr.get_AttributeName()));
		this.service.delete(attr.get_AttributeId());
	}

	private void createAttribute(AttributeMetadata attr) throws Exception {
		log.info(String.format("createAttribute() called. attr=%s", attr.get_AttributeName()));
		AttributeInfo info = new AttributeInfo(attr);
		this.service.create(this.entity, info);
	}

	private List<AttributeMetadata> getNew(List<AttributeMetadata> list1,
			List<AttributeMetadata> list2) {
		List<AttributeMetadata> list = new ArrayList<AttributeMetadata>();
		for(AttributeMetadata attr : list1){
			if(!has(list2, attr.get_AttributeName())){
				list.add(attr);
			}
		}
		return list;
	}
	
	private List<AttributeMetadata> getDiff(List<AttributeMetadata> list1,
			List<AttributeMetadata> list2) {
		List<AttributeMetadata> list = new ArrayList<AttributeMetadata>();
		return list;
	}
	
	private boolean has(List<AttributeMetadata> list, String attributeName){
		String[] names = new String[]{"createOn", "createBy", "modifyOn", "modifyBy"}; 
		for(String name : names){
			if(attributeName.equalsIgnoreCase(name)){
				return true;
			}
		}
		
		for(AttributeMetadata attr : list){
			if(attributeName.equalsIgnoreCase(attr.get_AttributeName())){
				return true;
			}
		}
		return false;
	}
}
