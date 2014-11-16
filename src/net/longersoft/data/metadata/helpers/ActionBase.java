package net.longersoft.data.metadata.helpers;

import net.longersoft.data.context.ExecutionContext;
import net.longersoft.framework.ServiceSession;

import org.apache.log4j.Logger;


public abstract class ActionBase {
	protected MetadataHelper helper;
	protected ExecutionContext context;
	protected ServiceSession session;
	private Logger log = Logger.getLogger(ActionBase.class);
	
	public ActionBase(ActionType actionType, MetadataObjectType objectType, String objectId, MetadataHelper helper){
		this.helper = helper;
		this.helper.allActions.put(new ActionKey(objectType, actionType, objectId), this);
		this.context = this.helper.getContext();
		this.session = this.context.getSession();
		if(this.session == null){
			this.session = new ServiceSession().initByUserId("system");
		}
	}

	public void validate() throws Exception{
		log.trace(this.getClass().getSimpleName() + " validate...");
	}

	public void injectDefaults(){
		log.trace(this.getClass().getSimpleName() + " inject defaults...");
	}

	public void retrieveDataForOperations() throws Exception{
		log.trace(this.getClass().getSimpleName() + " retriving data from operations...");
	}

	public void doMetadataOperation() throws Exception{
		log.trace(this.getClass().getSimpleName() + " doing metadata operation...");
	}

	public void doDatabaseOperation() throws Exception{
		log.trace(this.getClass().getSimpleName() + " doing database operation...");
	}
	
	public void doViewOperation() throws Exception{
		log.trace(this.getClass().getSimpleName() + " doing view operation...");
	}
	
	protected String modifySql(String sql){
		return ExecutionContext.getSqlProvider().modifyActionSql(this, sql);
	}
}