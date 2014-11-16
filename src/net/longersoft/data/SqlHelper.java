package net.longersoft.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import net.longersoft.data.context.ExecutionContext;

public class SqlHelper {
	private static Logger log = Logger.getLogger(SqlHelper.class);
	
	public static Boolean execute(PreparedStatement statement) throws SQLException{
		return modify(statement).execute();
	}
	
	public static int executeUpdate(PreparedStatement statement) throws SQLException{
		return modify(statement).executeUpdate();
	}
	
	public static ResultSet executeQuery(PreparedStatement statement) throws SQLException{
		return modify(statement).executeQuery();
	}

	private static void trace(Statement statement) {
		log.trace(ExecutionContext.getSqlProvider().toSqlString(statement));
	}
	
	private static PreparedStatement modify(PreparedStatement statement) throws SQLException{
		// a chance to modify all the statement
		trace(statement);
		return statement;
	}
}
