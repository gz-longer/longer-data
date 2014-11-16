package net.longersoft.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONString;

import net.longersoft.data.context.ExecutionContext;
import net.longersoft.data.context.ExecutionContextFun;
import net.longersoft.data.context.ExecutionContextHelper;
import net.longersoft.data.linq.expressions.Q;
import net.longersoft.data.metadata.helpers.MetadataHelper;
import net.longersoft.framework.ServiceSession;

// 原始的数据库行
public class EntityRow
implements JSONString, Cloneable {
	public static final String TABLE_TAIL = "_t";
	private static Logger log = Logger.getLogger(EntityRow.class);
	protected String initName = null;
	private Hashtable<String, Object> _attributes = new Hashtable<String, Object>();
	
	public EntityRow(String entityName) {
		this.initName = entityName;
		// this.init(); // can not init here! otherwise, 
		// it will be failed when using Q.from..select(...) 
		// 因为初始值会当作查询条件去加载数据 
	}
	
	// 简单的引用，处理不当会有一些负作用，但它提高了效率
	public void copyFrom(EntityRow row){
		this.initName = row.initName;
		this._attributes = row._attributes;
	}

	
	public void setAttributeValue(String attributeName, Object value){
		attributeName = attributeName.toLowerCase();
		if(this.getAttributes().keySet().contains(attributeName)){
			this.getAttributes().remove(attributeName);
		}
		
		if( value != null){
			this.getAttributes().put(attributeName, value);
		}
	}
	
	public Object getAttributeValue(String attributeName){
		attributeName = attributeName.toLowerCase();
		if(this.getAttributes().keySet().contains(attributeName)){
			return this.getAttributes().get(attributeName);
		}
		log.trace(String.format("attribute '%s' not found in table '%s'.", attributeName, this.getDatabaseTableName()));
		return null;
	}
	
	public Object get(String attributeName){
		return this.getAttributeValue(attributeName);
	}
	
	public void set(String attributeName, Object value){
		this.setAttributeValue(attributeName, value);
	}
	
	public void putAttributesByMap(Map<String, Object> map){
		for(Map.Entry<String, Object> entry : map.entrySet()){
			this.setAttributeValue(entry.getKey(), entry.getValue());
		}
	}
	
	// Primary Key
	public String getPrimaryKeyName(){
		return MetadataHelper.getEntityPrimaryKeyName(this.initName);
	}
	
	// Primary Field
	public String getPrimaryFieldName(){
		return MetadataHelper.getEntityPrimaryFieldName(this.initName);
	}

	public String getDatabaseTableName() {
		return initName + TABLE_TAIL;
	}
	
	public void setPrimaryId(String id){
		this.setAttributeValue(this.getPrimaryKeyName(), id);
	}
	
	public String getPrimaryId(){
		return (String)this.getAttributeValue(this.getPrimaryKeyName());
	}
	
	public void setPrimaryName(String id){
		this.setAttributeValue(this.getPrimaryFieldName(), id);
	}
	
	public String getPrimaryName(){
		return (String)this.getAttributeValue(this.getPrimaryFieldName());
	}
	
	public void set_CreateOn(Date createOn){
		this.setAttributeValue("createOn", createOn);
	}
	
	public Date get_CreateOn(){
		return (Date)this.getAttributeValue("createOn");
	}
	
	public void set_ModifyOn(Date modifyOn){
		this.setAttributeValue("modifyOn", modifyOn);
	}
	
	public Date get_ModifyOn(){
		return (Date)this.getAttributeValue("modifyOn");
	}	
	
	public void set_CreateBy(String createBy){
		this.setAttributeValue("createBy", createBy);
	}
	
	public String get_CreateBy(){
		return (String)this.getAttributeValue("createBy");
	}
	
	public void set_ModifyBy(String modifyBy){
		this.setAttributeValue("modifyBy", modifyBy);
	}
	
	public String get_ModifyBy(){
		return (String)this.getAttributeValue("modifyBy");
	}
	
	public int createIt(final ServiceSession session) throws Exception {
		final int[] returnValue = new int[1];
		ExecutionContextHelper.notran(session, new ExecutionContextFun() {
			@Override
			public void call(ExecutionContext context) throws Exception {
				returnValue[0] = createIt(context);
			}
		});
		return returnValue[0];
	}
	
	public int createIt(ExecutionContext context) throws Exception{
		EntityRowProcesser service = new EntityRowProcesser(this.getDatabaseTableName());
		return service.doCreate(this, context);
	}

	public Boolean isNull(String attributeName){
		return this._attributes.get(attributeName) == null;
	}

	public Hashtable<String, Object> getAttributes() {
		return _attributes;
	}
	
	public int deleteIt(final ServiceSession session) throws Exception {
		final int[] returnValue = new int[1];
		ExecutionContextHelper.notran(session, new ExecutionContextFun() {
			@Override
			public void call(ExecutionContext context) throws Exception {
				returnValue[0] = deleteIt(context);
			}
		});
		return returnValue[0];
	}

	public int deleteIt(ExecutionContext context) throws Exception {
		EntityRowProcesser service = new EntityRowProcesser(this.getDatabaseTableName());
		return service.doDelete(this, context);
	}

	public int updateIt(final ServiceSession session) throws Exception {
		final int[] returnValue = new int[1];
		ExecutionContextHelper.notran(session, new ExecutionContextFun() {
			@Override
			public void call(ExecutionContext context) throws Exception {
				returnValue[0] = updateIt(context);
			}
		});
		return returnValue[0];
	}

	public int updateIt(ExecutionContext context) throws Exception {
		EntityRowProcesser service = new EntityRowProcesser(this.getDatabaseTableName());
		return service.doUpdate(this, context);
	}
	
	public int saveIt(final ServiceSession session) throws Exception{
		final int[] returnValue = new int[1];
		ExecutionContextHelper.notran(session, new ExecutionContextFun() {
			@Override
			public void call(ExecutionContext context) throws Exception {
				returnValue[0] = saveIt(context);
			}
		});
		return returnValue[0];
	}
	
	public int saveIt(ExecutionContext context) throws Exception{
		if(existIt(context)){
			return updateIt(context);
		}else{
			return createIt(context);
		}
	}
	
	public void fillIt(final ServiceSession session, final String...attributes) throws Exception{
		ExecutionContextHelper.notran(session, new ExecutionContextFun() {
			@Override
			public void call(ExecutionContext context) throws Exception {
				fillIt(context, attributes);
			}
		});
	}
	
	public void fillIt(ExecutionContext context, String...attributes) throws Exception {
		EntityRowProcesser service = new EntityRowProcesser(this.getDatabaseTableName());
		
		List<String> list = new ArrayList<String>();
		for(String s : attributes)list.add(s);
		
		service.doLoad(this, list, context);
	}
	
	public boolean existIt(final ServiceSession session) throws Exception{
		final boolean[] returnValue = new boolean[1];
		ExecutionContextHelper.notran(session, new ExecutionContextFun() {
			@Override
			public void call(ExecutionContext context) throws Exception {
				returnValue[0] = existIt(context);
			}
		});		
		return returnValue[0];
	}
	
	public boolean existIt(ExecutionContext context) throws Exception{
		EntityRowProcesser service = new EntityRowProcesser(this.getDatabaseTableName());
		List<String> list = new ArrayList<String>();
		list.add(this.getPrimaryKeyName());
		return service.checkExist(this, list, context);		
	}
	
	public void init(){
		this.setAttributeValue(this.getPrimaryKeyName(), UUID.randomUUID().toString());
		this.setAttributeValue(this.getPrimaryFieldName(), ""); 
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public String toJSONString() {
		JSONObject json = new JSONObject(this._attributes);
		return json.toString();
	}
	
	@Override
	public String toString() {
		return this.toJSONString();
	}
}
