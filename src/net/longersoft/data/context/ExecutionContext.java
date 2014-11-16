package net.longersoft.data.context;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;

import net.longersoft.data.linq.strategy.SqlProvider;
import net.longersoft.framework.ServiceSession;
import net.longersoft.helpers.PropertiesHelper;

import org.apache.log4j.Logger;

public class ExecutionContext  extends OrganizationContext {
	static SqlProvider provider = (SqlProvider)PropertiesHelper.getObject("longer.data.sqlprovider");
	private static Logger log = Logger.getLogger(ExecutionContext.class);
	private Connection connection = null;
	private static int seq = 0;
	
	private ServiceSession session = null;
	
	private int seqNo;
	public ExecutionContext(ServiceSession session) {
		this.setConnection(provider.getConnection());
		this.seqNo = ++seq;
		this.session = session;
	}

	public void commitTransation() {
		try {
			this.connection.commit();
			log.trace("TRAN: commit " + seqNo);
		} catch (SQLException e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public void rollbackTransation() {
		try {
			this.connection.rollback();
			log.trace("TRAN: rollback " + seqNo);
		} catch (SQLException e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	public void startTransation() {
		try {
			this.connection.setAutoCommit(false);
			log.trace("TRAN: begin " + seqNo);
		} catch (SQLException e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}
	
	public ServiceSession getSession(){
		return this.session;
	}

	private void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Connection getConnection() {
		return connection;
	}

	public static SqlProvider getSqlProvider(){
		return provider;
	}
}
