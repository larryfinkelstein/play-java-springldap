package models;
import configs.AppConfig;
import configs.TestLdapConfig;
import models.Person;

import org.junit.Test;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import play.Logger;
import static org.junit.Assert.*;

@ContextConfiguration(classes={AppConfig.class, TestLdapConfig.class})
public class PersonTest extends AbstractJUnit4SpringContextTests {
 
    @Test
    public void setPersonName() {
        Person person = new Person();
        person.uid="userone";
        person.cn = "User One";
        person.givenName = "User";
        person.sn = "One";
        person.mail = "userone@example.com";
        assertEquals(person.cn, "User One");
        assertEquals(person.mail, "userone@example.com");
    }
    
    @Test
    public void testJson() {
        Person person = new Person();
        final String BASE_DN = "ou=People,dc=example,dc=com";
        person.dn = LdapNameBuilder.newInstance(BASE_DN)
        		.add("uid", "userone").build();
        //Logger.debug(person.dn.toString());
        person.uid="userone";
        person.cn = "User One";
        person.givenName = "User";
        person.sn = "One";
        person.mail = "userone@example.com";
        
        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
        try {
			String json = mapper.writeValueAsString(person);
			assertNotNull(json);
			assertTrue(json.contains("\"cn\":\"User One\""));
			assertTrue(json.contains("\"dn\":\"uid=userone,"+BASE_DN));
		} catch (JsonProcessingException e) {
			fail(e.getMessage());
		}
    }
}
