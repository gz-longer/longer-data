package net.longersoft.data.database.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RequiredLevel {
	int value();
	
	public static final int Optional = 0;
	public static final int Recommend = 1;
	public static final int Required = 2;
}


