package net.longersoft.data.context;

import java.sql.Connection;

import net.longersoft.framework.ServiceSession;

import org.apache.log4j.Logger;

public class ExecutionContextHelper {
	private static Logger log = Logger.getLogger(ExecutionContextHelper.class);
	
	public static void tran(ServiceSession session, ExecutionContextFun fun) throws Exception{
		execute(session, true, fun);
	}
	
	public static void notran(ServiceSession session, ExecutionContextFun fun) throws Exception{
		execute(session, false, fun);
	}
	
	private static void execute(ServiceSession session, boolean useTransaction, ExecutionContextFun fun) throws Exception{
		ExecutionContext context = new ExecutionContext(session);
		if(useTransaction)context.startTransation();
		try{
			fun.call(context);
			if(useTransaction)context.commitTransation();
			destroy(context);
		}catch (Exception e) {
			if(useTransaction)context.rollbackTransation();
			destroy(context);
			log.error(e);
			throw e;
		}
	}
	
	private static void destroy(ExecutionContext context){
		Connection conn = context.getConnection();
		if(conn == null) return;
		try{
			if(conn.isClosed()) return;
			conn.close();
		}catch(Exception exp){
			exp.printStackTrace();
		}
	}
}