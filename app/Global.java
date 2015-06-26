import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.ApplicationContext;
import play.GlobalSettings;
import play.Application;

import configs.AppConfig;
import configs.LdapConfig;
import play.Logger;

public class Global extends GlobalSettings {

    private ApplicationContext ctx;

    @Override
    public void onStart(Application app) {
    	ctx = new AnnotationConfigApplicationContext(AppConfig.class, LdapConfig.class);
    	Logger.info("Starting application context "+ctx);
    }

    //@Override
    public <A> A getControllerInstance(Class<A> clazz) {
        return ctx.getBean(clazz);
    }

}