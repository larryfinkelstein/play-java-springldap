package models;
import configs.AppConfig;
import configs.TestLdapConfig;
import models.Person;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
}
