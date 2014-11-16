package net.longersoft.data.linq.strategy;

import java.sql.Connection;
import java.sql.Statement;

import net.longersoft.data.metadata.bll.AttributeMetadata;
import net.longersoft.data.metadata.helpers.ActionBase;
import net.longersoft.data.metadata.services.AttributeType;
import net.longersoft.data.metadata.services.AttributeTypeInfo;


public abstract class SqlProvider {
	public abstract Connection getConnection();
	public abstract AttributeTypeInfo getAttributeTypeInfo(AttributeType attributeType);
	public abstract String getFullSqlType(AttributeTypeInfo typeInfo, AttributeMetadata attributeInfo) ;
	public abstract String toSqlString(Statement statement);
	public abstract String modifyActionSql(ActionBase actionBase, String sql);
}
