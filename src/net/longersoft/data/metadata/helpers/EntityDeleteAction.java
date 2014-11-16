package net.longersoft.data.metadata.helpers;

import java.sql.PreparedStatement;
import org.apache.log4j.Logger;

import net.longersoft.data.SqlHelper;
import net.longersoft.data.linq.expressions.ConditionOperator;
import net.longersoft.data.linq.expressions.Q;
import net.longersoft.data.metadata.bll.AttributeMetadata;
import net.longersoft.data.metadata.bll.EntityMetadata;

public class EntityDeleteAction extends ActionBase {
	private static Logger log = Logger.getLogger(EntityDeleteAction.class);
	
	private EntityMetadata bag;

	public EntityDeleteAction(EntityMetadata bag, MetadataHelper helper) {
		super(ActionType.Delete, MetadataObjectType.Entity, bag.get_EntityId(), helper);
		
		this.bag = bag;
	}
	
	@Override
	public void retrieveDataForOperations() throws Exception {
		super.retrieveDataForOperations();
		
		this.bag.fillIt(this.context, "entityName", "tableName");
	}
	
	@Override
	public void doMetadataOperation() throws Exception {
		super.doMetadataOperation();
		try{ deleteEntityAttributes(); } catch(Exception e){}
		try{ deleteEntity(); } catch(Exception e){}
	}

	private void deleteEntity() throws Exception {
		this.bag.deleteIt(this.session);
	}

	private void deleteEntityAttributes() throws Exception {
		Q.from(AttributeMetadata.EntityName + AttributeMetadata.TABLE_TAIL)
			.where("entityId", ConditionOperator.Equal, this.bag.get_EntityId())
			.delete()
			.execute();
	}
	
	@Override
	public void doDatabaseOperation() throws Exception {
		try{
			super.doDatabaseOperation();
			StringBuffer builder = new StringBuffer();
			builder.append(String.format("drop table %s", this.bag.get_TableName()));
			String sql = super.modifySql(builder.toString());
			PreparedStatement statement = this.context.getConnection().prepareStatement(sql);
			SqlHelper.execute(statement);
		}catch(Exception e){}	
	}
	
	@Override
	public void doViewOperation() throws Exception {
		// TODO Auto-generated method stub
		super.doViewOperation();
	}

}
