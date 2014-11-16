package net.longersoft.data.context;

public abstract class ExecutionContextFun {
	public ExecutionContextFun(){	}	
	public abstract void call(ExecutionContext context) throws Exception;
}
