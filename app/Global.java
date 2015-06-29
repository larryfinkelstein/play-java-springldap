import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.support.LdapNameBuilder;

import java.util.Locale;

import javax.naming.Name;

import org.springframework.context.ApplicationContext;
import play.GlobalSettings;
import play.Application;

import configs.AppConfig;
import configs.LdapConfig;
import play.Logger;
import play.data.format.Formatters;

public class Global extends GlobalSettings {

    private ApplicationContext ctx;

    @Override
    public void onStart(Application app) {
    	ctx = new AnnotationConfigApplicationContext(AppConfig.class, LdapConfig.class);
    	Logger.info("Starting application context "+ctx);
    	
    	Formatters.register(Name.class, new Formatters.SimpleFormatter<Name>() {
            @Override
            public Name parse(String input, Locale l)  {
                return LdapNameBuilder.newInstance(input).build();
            }   

            @Override
            public String print(Name name, Locale l) {
                return name.toString();
            }
        });
    	Logger.info("Registered Name Formatter");
    }

    //@Override
    public <A> A getControllerInstance(Class<A> clazz) {
        return ctx.getBean(clazz);
    }

}