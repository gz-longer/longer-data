package net.longersoft.data.metadata.module;

import java.util.Date;
import java.util.List;

import net.longersoft.data.context.ExecutionContext;
import net.longersoft.data.context.ExecutionContextFun;
import net.longersoft.data.context.ExecutionContextHelper;
import net.longersoft.data.database.annotations.DisplayName;
import net.longersoft.data.database.services.DatabaseService;
import net.longersoft.data.linq.expressions.ConditionOperator;
import net.longersoft.data.linq.expressions.Q;
import net.longersoft.data.metadata.bll.MetadataModule;
import net.longersoft.data.metadata.dal.LongerMetadataModule;
import net.longersoft.framework.LongerModule;
import net.longersoft.helpers.ReflectionHelper;

public class LongerDataModule extends LongerModule {
	@Override
	protected void install() throws Exception {
		List<Class<?>> classes = this.getModuleClasses();
		DatabaseService service = new DatabaseService();
		service.create(this, classes); 		
	}
	
	@Override
	protected void uninstall() throws Exception {
		List<Class<?>> classes = ReflectionHelper.getClasses(this);
		DatabaseService service = new DatabaseService();
		try{ service.delete(this, classes); } catch(Exception e){}			
	}

	@Override
	protected void update() throws Exception {
		List<Class<?>> classes = ReflectionHelper.getClasses(this);
		DatabaseService service = new DatabaseService();
		try{ service.update(this, classes); } catch(Exception e){}		
	}
	
	@Override
	protected boolean isInstalled() throws Exception {
		Q q = Q.fromEntity(MetadataModule.EntityName)
				.where("moduleId", ConditionOperator.Equal, this.getModuleId())
				.where("installStatus", ConditionOperator.NotLike, "uninstall%")
				.select_count();
		
		return q.returnCount() > 0;
	}
	
	@Override
	protected void init() throws Exception {
		
	}

	@Override
	protected void setInstallStatus(final String status) throws Exception {
		super.setInstallStatus(status);
		
		ExecutionContextHelper.tran(session, new ExecutionContextFun() {
			@Override
			public void call(ExecutionContext context) throws Exception {
				MetadataModule mod = new MetadataModule();
				mod.set_ModuleId(LongerDataModule.this.getModuleId());
				
				if(mod.existIt(context)){
					mod.set_InstallStatus(status);
					mod.updateIt(context);
				}else{
					String moduleName = LongerDataModule.this.getModuleName();
					String displayName = LongerDataModule.this.getDisplayName(moduleName);
					mod.set_DisplayName(displayName);
					mod.set_ModuleName(moduleName);
					mod.set_InstallTime(new Date());
					mod.set_InstallStatus(status);
					mod.createIt(context);
				}
			}
		});
	}
	
	private String getDisplayName(String defaultValue){
		DisplayName aDisplayName = (DisplayName) this.getClass().getAnnotation(DisplayName.class);
		if(aDisplayName != null) return aDisplayName.value();
		
		return defaultValue;
	}
}
