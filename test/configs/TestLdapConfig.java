package configs;
import configs.LdapConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.repository.config.EnableLdapRepositories;

import static org.junit.Assert.*;

@Configuration
@EnableLdapRepositories(basePackages={"models"})
public class TestLdapConfig extends LdapConfig {

    @Bean
	public LdapContextSource contextSource() {
    	/*
    	 * We need to statically set these values, since we cannot use the
    	 * real contextSource in LdapConfig because is retrieves values
    	 * from the Play! application.conf.  Play! is not running in a test
    	 * environment, so we set these here for the tests.
    	 */
		LdapContextSource contextSource = new LdapContextSource();
		contextSource.setUrl("ldap://localhost:389");
		//contextSource.setBase("dc=example,dc=com");
		contextSource.setUserDn("cn=Directory Manager");
		contextSource.setPassword("password");
		return contextSource;
    }
	
	@Bean
	public LdapTemplate ldapTemplate() {
		LdapTemplate ldapTemplate = new LdapTemplate(contextSource());
		
		assertNotNull(ldapTemplate);
		return ldapTemplate;
	}
}
