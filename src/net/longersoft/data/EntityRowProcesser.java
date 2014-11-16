package net.longersoft.data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;

import net.longersoft.data.context.ExecutionContext;
import net.longersoft.data.linq.CreatePlan;
import net.longersoft.data.linq.DeletePlan;
import net.longersoft.data.linq.SelectPlan;
import net.longersoft.data.linq.UpdatePlan;
import net.longersoft.data.linq.expressions.ColumnSetExpression;
import net.longersoft.data.linq.expressions.ConditionExpression;
import net.longersoft.data.linq.expressions.ConditionOperator;
import net.longersoft.data.linq.expressions.Q;
import net.longersoft.data.linq.expressions.FilterExpression;
import net.longersoft.data.linq.visitors.CreateVisitor;
import net.longersoft.data.linq.visitors.ExpressionVisitor;
import net.longersoft.data.metadata.bll.MetadataErrorCodes;
import net.longersoft.exceptions.ErrorCodes;
import net.longersoft.exceptions.LongerException;

// EntityRow 的处理类
class EntityRowProcesser {
	private static Logger log = Logger.getLogger(EntityRowProcesser.class);
	
	private String entityName = null;
	public EntityRowProcesser(String entityName) {
		this.entityName = entityName;
	}
	
	public int doCreate(EntityRow entityRow, ExecutionContext context) throws SQLException{
		// TODO: check auth: create @ acl://ent/<ent>/attr/<attr>
		log.trace(String.format("doCreate: %s, %s", this.entityName, entityRow.getDatabaseTableName()));
		
		ColumnSetExpression columnSet = new ColumnSetExpression();
		this.fillColumnSetForCreate(entityRow, columnSet);
		
		Q query = Q.from(entityRow.getDatabaseTableName(), this.entityName);
		query.setColumnSet(columnSet);
		
		
		CreatePlan plan = new CreatePlan(query, context);
		PreparedStatement statement = plan.getCommand();
		return SqlHelper.executeUpdate(statement);
	}
	

	public int doUpdate(EntityRow entityRow, ExecutionContext context) throws Exception {
		// TODO: check auth
		
		ColumnSetExpression columnSet = new ColumnSetExpression();
		this.fillColumnSetForUpdate(entityRow, columnSet);
		FilterExpression filter = new FilterExpression();
		this.fillFilterForUpdate(entityRow, filter);
		Q query = Q.from(entityRow.getDatabaseTableName(), this.entityName);
		query.setColumnSet(columnSet);
		query.setFilter(filter);
		UpdatePlan plan = new UpdatePlan(query, context);
		PreparedStatement statement = plan.getCommand();
		return SqlHelper.executeUpdate(statement);
	}

	public int doDelete(EntityRow entityRow, ExecutionContext context) throws SQLException {
		// TODO: check auth
		
		FilterExpression filter = new FilterExpression();
		this.fillFilterForDelete(entityRow, filter);
		Q query = Q.from(entityRow.getDatabaseTableName(), this.entityName);
		query.setFilter(filter);
		DeletePlan plan = new DeletePlan(query, context);
		PreparedStatement statement = plan.getCommand();
		return SqlHelper.executeUpdate(statement);
	}	

	public void doLoad(EntityRow entityRow, List<String> attributes, ExecutionContext context) throws Exception {
		// TODO: check auth
		
		ResultSet rs = getResultSet(entityRow, attributes, context);
		if( !fillEntityRow(entityRow, rs, attributes) ){
			throw new LongerException(MetadataErrorCodes.Entity_NotFound, 
					String.format("entity '%s' was not found with specified condition.", entityRow.getDatabaseTableName()));
		}
	}	
	
	public boolean checkExist(EntityRow entityRow, List<String> attributes, ExecutionContext context) throws Exception{
		ResultSet rs = getResultSet(entityRow, attributes, context);
		return rs.next();
	}

	private ResultSet getResultSet(EntityRow entityRow, List<String> attributes,
			ExecutionContext context) throws Exception, SQLException {
		if(attributes.size() == 0) throw new Exception(String.format("load data from table '%s' missing attributes.", entityRow.getDatabaseTableName()));
		
		Q query = Q.from(entityRow.getDatabaseTableName(), this.entityName);
		this.fillColumnSetForLoad(entityRow, query.getColumnSet(), attributes);
		this.fillFilterForLoad(entityRow, query.getFilter());
		SelectPlan plan = new SelectPlan(query, context);
		PreparedStatement statement = plan.getCommand();
		ResultSet rs = SqlHelper.executeQuery(statement);
		return rs;
	}

	private void fillColumnSetForCreate(EntityRow entityRow,
			ColumnSetExpression columnSet) {
		Hashtable<String, Object> attributes = entityRow.getAttributes();

		for(String key : attributes.keySet()){
			Object value = attributes.get(key);
			columnSet.addColumn(key, null, value);
		}
	}
	
	private void fillColumnSetForUpdate(EntityRow entityRow,
			ColumnSetExpression columnSet) {
		String pk = this.getPrimaryKey(entityRow);
		Hashtable<String, Object> attributes = entityRow.getAttributes();

		for(String key : attributes.keySet()){
			if(key.equals(pk)) continue;
			Object value = attributes.get(key);
			columnSet.addColumn(key, null, value);
		}
	}	

	private void fillFilterForUpdate(EntityRow entityRow,
			FilterExpression filter) {
		String pk = this.getPrimaryKey(entityRow);
		log.info("pk:" + pk);
		Hashtable<String, Object> attributes = entityRow.getAttributes();

		for(String key : attributes.keySet()){
			if( !key.equals(pk) ) continue;
			
			Object value = attributes.get(key);
			ArrayList<Object> values = new ArrayList<Object>();
			values.add(value);
			ConditionExpression condition = new ConditionExpression(key, ConditionOperator.Equal, values);
			filter.addCondition(condition);
			break;
		}
	}		

	private String getPrimaryKey(EntityRow entityRow) {
		return entityRow.getPrimaryKeyName();
	}

	private boolean fillEntityRow(EntityRow entityRow, ResultSet rs, List<String> attributes) throws SQLException {
		ResultSetMetaData resultSetMD = rs.getMetaData();
		
		if(rs.next()){
			for(int i = 1; i <= resultSetMD.getColumnCount(); i++){
				Object value = rs.getObject(i);
				String colName = resultSetMD.getColumnName(i);
				entityRow.setAttributeValue(colName, value);
			}
			
			return true;
		}else{
			return false;
		}
	}

	private void fillColumnSetForLoad(EntityRow entityRow, ColumnSetExpression columnSet, List<String> attributes) {
		for(String a : attributes){
			columnSet.addColumn(a, null, null);
		}
	}
	

	private void fillFilterForLoad(EntityRow entityRow, FilterExpression filter) {
		Hashtable<String, Object> attributes = entityRow.getAttributes();

		for(String key : attributes.keySet()){
			Object value = attributes.get(key);
			ArrayList<Object> values = new ArrayList<Object>();
			values.add(value);
			ConditionExpression condition = new ConditionExpression(key, ConditionOperator.Equal, values);
			filter.addCondition(condition);
		}
	}	

	private void fillFilterForDelete(EntityRow entityRow, FilterExpression filter) {
		fillFilterForLoad(entityRow, filter);
	}
}
