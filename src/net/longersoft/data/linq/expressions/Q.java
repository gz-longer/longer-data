package net.longersoft.data.linq.expressions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document; 
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.omg.CORBA.StringValueHelper;

import net.longersoft.data.EntityRow;
import net.longersoft.data.SqlHelper;
import net.longersoft.data.context.ExecutionContext;
import net.longersoft.data.context.ExecutionContextFun;
import net.longersoft.data.context.ExecutionContextHelper;
import net.longersoft.data.linq.DeletePlan;
import net.longersoft.data.linq.QueryPlan;
import net.longersoft.data.linq.SelectPlan;
import net.longersoft.data.linq.strategy.SqlProvider;
import net.longersoft.data.linq.visitors.ExpressionVisitor;
import net.longersoft.framework.ServiceSession;
import net.longersoft.helpers.PropertiesHelper;

public class Q extends EntityExpressionBase {
	private FilterExpression filter = null;
	private ColumnSetExpression columnSet = new ColumnSetExpression();
	private OrderByExpression orderby = null;
	private GroupByExpression groupby = null;
	private Q parentQuery = null;
	private String viewName;
	private String tableName = null;
    private String tableAlias;
    private Hashtable<String, String> tableAliases = new Hashtable<String, String>();
    private ColumnSetExpression groupAttributes = null;
    private PagingExpression paging;
    private Boolean todoDelete = false;
    private List<LinkEntityExpression> linkEntities = new ArrayList<LinkEntityExpression>();
    private Aggregate aggregate = null;
    private ServiceSession session = null;
    
    private static Logger log = Logger.getLogger(Q.class);
    
    /*package*/ Q(){
    	
    }
    
    private static Q from0(String tableName, String tableAlias, String viewName){
    	Q q = new Q();
    	if(tableAlias == null || tableAlias.length() == 0) 
    		tableAlias = tableName;
    	q.setTableName(tableName);
    	q.setViewName(viewName);
    	q.setTableAlias(tableAlias);
    	q.setFilter(new FilterExpression());
    	return q;
    }
    
    public static Q from(String tableName){
    	return from0(tableName, null, tableName);
    }
    
    public static Q fromEntity(String entityName_as){
    	String[] arr = parseAs(entityName_as);
    	String entityName = arr[0];
    	String alias = arr[1]; 
    	return from0(entityName + EntityRow.TABLE_TAIL, alias, entityName);
    }
    
    static String[] parseAs(String as){
    	String[] arr = as.split("\\s+as\\s+");
    	if(arr.length == 1){
    		return new String[]{as, as};
    	}else{
    		return arr;
    	}
    }
    
    public static Q from(String tableName, String viewName){
    	return from0(tableName, null, viewName);
    }
    
    public Q join_on(String joinEntityName_as, String attributeFrom, ConditionOperator conditionOperator, String attributeTo){
    	this.addLinkEntity(joinEntityName_as, attributeFrom, conditionOperator, attributeTo);
    	return this;
    }
    
    private LinkEntityExpression addLinkEntity(String joinEntityName_as, String attributeFrom, ConditionOperator conditionOperator, String attributeTo){
    	String[] arr = parseAs(joinEntityName_as);
    	String linkEntityName = arr[0];
    	String linkEntityAlias = arr[1];
    	
    	LinkEntityExpression link = new LinkEntityExpression();
    	link.setEntityName(linkEntityName);
    	link.setTableAlias(linkEntityAlias);
    	link.setAttributeFrom(attributeFrom);
    	link.setAttributeTo(attributeTo);
    	link.setConditionOperator(conditionOperator);
    	link.setJoinOperator(JoinOperator.Inner);
    	
    	link.setParentEntity(this);
    	
    	this.getLinkEntities().add(link); 
    	return link;
    }
    
    public Q select(String...columns){
    	this.aggregate = null;
    	
    	for(String col : columns){
    		String[] arr = parseAs(col.toLowerCase());
    		this.columnSet.addColumn(arr[0], arr[1], null);
    	}
    	return this;
    }
    
    public Q select_count(){
    	if(this.aggregate == null) this.aggregate = new Aggregate();
    	this.aggregate.setType(AggregateType.Count);
    	this.aggregate.setAttribute("1");
    	this.aggregate.setAlias("cnt");
    	return this;
    }
    
    public Q delete(){
    	this.todoDelete = true;
    	return this;
    }
    
    private Q where(String attributeName, ConditionOperator op, ArrayList<Object> values){
    	ConditionExpression condition = new ConditionExpression(attributeName, op, values);
    	return where(condition);
    }
    
    public Q where(String attributeName, ConditionOperator op, Object...vals){
    	ArrayList<Object> values = new ArrayList<Object>();
    	for(Object o : vals) values.add(o);
    	return where(attributeName, op, values);
    }
    
    public Q where(final ConditionExpression condition){
    	return where(new FilterExpressionFun() {
			@Override
			public void call(FilterExpression filter) {
				filter.addCondition(condition);
			}
		});
    }
    
    public Q where(FilterExpressionFun fun){
    	FilterExpression filter = this.getFilter();
    	fun.call(filter);
    	return this;
    }
    
    @SuppressWarnings("unchecked")
	public Q where_filterxml(String filterXml){
    	FilterExpression filter = this.getFilter();
    	
    	try{
	    	SAXReader reader = new SAXReader();
			InputStream stream = new ByteArrayInputStream(filterXml.getBytes());
			Document doc = reader.read(stream);
			
			List<Node> list = doc.selectNodes("//condition");
			for(Node node : list){
				String attribute = node.selectSingleNode("@attribute").getStringValue();
				String oper = node.selectSingleNode("@oper").getStringValue();
				String value = node.selectSingleNode("@value").getStringValue();
				
				ArrayList values = new ArrayList<Object>();
				values.add(value);
				ConditionExpression cond = new ConditionExpression(attribute, ConditionOperator.Equal, values);
				filter.addCondition(cond);
			}    	
	    }catch(Exception e){ }
	    
    	return this;
    }
    
    // order by
    public Q order_by(String col){
    	if(orderby == null) orderby = new OrderByExpression();
    	orderby.setOrderBy(col, false);
    	return this;
    }
    
    public Q order_by_desc(String col){
    	if(orderby == null) orderby = new OrderByExpression();
    	orderby.setOrderBy(col, true);
    	return this;
    }
    
    public Q group_by(String... cols){
    	if( groupby == null) groupby = new GroupByExpression();
    	groupby.setCols(cols);
    	return this;
    }
    
    public Q skip_and_take(int skip, int take){
    	if(paging == null) paging  = new PagingExpression();
    	paging.setSkip(skip);
    	paging.setTake(take);
    	return this;
    }
    
    public Q with_session(ServiceSession session){
    	this.session = session;
    	return this;
    }

	public List<EntityRow> returnCollection() throws Exception {
		final String viewName = this.viewName;
		final List<EntityRow> list = new ArrayList<EntityRow>();
		ExecutionContextHelper.notran(session, new ExecutionContextFun() {
			@Override
			public void call(ExecutionContext context) throws Exception {
				ResultSet rs = returnResultSet(context);
				ResultSetMetaData md = rs.getMetaData();
				int n = md.getColumnCount();
				while(rs.next()){
					EntityRow row = new EntityRow(viewName);
					for(int i = 1; i<=n; i++){
						Object value = rs.getObject(i);
						String colName = md.getColumnName(i);
						row.setAttributeValue(colName, value);
					}
					list.add(row);
				}
			}
		});
		return list;
	}

	private PreparedStatement returnCommand(ExecutionContext context) throws Exception {
		QueryPlan plan = null;
		if(this.todoDelete){
			plan = new DeletePlan(Q.this, context);
		}else{
			plan = new SelectPlan(Q.this, context);
		}
		PreparedStatement command = plan.getCommand();
		return command;
	}
	
	public String returnSql() throws Exception {
		final String[] sql = new String[1];
		ExecutionContextHelper.notran(session, new ExecutionContextFun() {
			@Override
			public void call(ExecutionContext context) throws Exception {
				sql[0] = returnSql(context);
			}
		});
		return sql[0];
	}

	public String returnSql(ExecutionContext context) throws Exception {
		QueryPlan plan = null;
		if(this.todoDelete){
			plan = new DeletePlan(Q.this, context);
		}else{
			plan = new SelectPlan(Q.this, context);
		}
		return plan.getCommandString();
	}

	private ResultSet returnResultSet(ExecutionContext context) throws Exception {
		PreparedStatement command = this.returnCommand(context); 
		return SqlHelper.executeQuery(command);
	}
	
	public void execute() throws Exception{
		ExecutionContextHelper.tran(session, new ExecutionContextFun() {
			@Override
			public void call(ExecutionContext context) throws Exception {
				PreparedStatement command = Q.this.returnCommand(context); 
				SqlHelper.execute(command);
			}
		});
	}
	
	public int returnCount() throws Exception{
		final int[] rets = new int[]{0};
		ExecutionContextHelper.notran(session, new ExecutionContextFun() {
			@Override
			public void call(ExecutionContext context) throws Exception {
				PreparedStatement command = Q.this.returnCommand(context); 
				ResultSet rs = SqlHelper.executeQuery(command);
				if(rs.next()){
					rets[0] = rs.getInt(1);
				}
			}
		});
		return rets[0];
	}

	@Override
	public void acceptVisitor(ExpressionVisitor visitor) {
		visitor.visit(this);
	}
	
	public Aggregate getAggregate(){
		return aggregate;
	}

	private void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public String getViewName() {
		return viewName;
	}
	



	private void setTableAlias(String tableAlias) {
		this.tableAlias = tableAlias;
	}



	public String getTableAlias() {
		return tableAlias;
	}



	public void setColumnSet(ColumnSetExpression columnSet) {
		this.columnSet = columnSet;
	}

	public ColumnSetExpression getColumnSet() {
		return columnSet;
	}

	public void setFilter(FilterExpression filter) {
		this.filter = filter;
	}

	public FilterExpression getFilter() {
		return filter;
	}
	
	public OrderByExpression getOrderBy(){
		return this.orderby;
	}


	private void setTableName(String tableName){
		this.tableName = tableName;
	}
	
	public String getTableName() {
		return this.tableName;
	}

	public List<LinkEntityExpression> getLinkEntities() {
		return linkEntities;
	}

	public GroupByExpression getGroupby() {
		return groupby;
	}
	
	public PagingExpression getPaging(){
		return paging;
	}
}


