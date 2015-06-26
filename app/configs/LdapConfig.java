package configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.pool.factory.PoolingContextSource;
import org.springframework.ldap.pool.validation.DefaultDirContextValidator;
import org.springframework.ldap.repository.config.EnableLdapRepositories;
import org.springframework.ldap.transaction.compensating.manager.TransactionAwareContextSourceProxy;

import play.Logger;
import play.Play;

@Configuration
@EnableLdapRepositories(basePackages = {"models"})
public class LdapConfig {

	@Bean
	public ContextSource contextSource() {
		Logger.debug("Loading spring ldap application context");
		LdapContextSource contextSource = new LdapContextSource();
		play.Configuration playConfig = Play.application().configuration();
		contextSource.setUrl(playConfig.getString("ldap.url"));
		contextSource.setBase(playConfig.getString("ldap.base"));
		contextSource.setUserDn(playConfig.getString("ldap.username"));
		contextSource.setPassword(playConfig.getString("ldap.password"));
		contextSource.afterPropertiesSet(); // *** need this ***

		PoolingContextSource poolingContextSource = new PoolingContextSource();
		poolingContextSource.setDirContextValidator(new DefaultDirContextValidator());
	    poolingContextSource.setContextSource(contextSource);
	    poolingContextSource.setTestOnBorrow(true);
	    poolingContextSource.setTestWhileIdle(true);
	    
	    TransactionAwareContextSourceProxy proxy = new TransactionAwareContextSourceProxy(poolingContextSource);
		
	    return proxy;
	}

	@Bean
	public LdapTemplate ldapTemplate() {
		return new LdapTemplate(contextSource());
	}
}
