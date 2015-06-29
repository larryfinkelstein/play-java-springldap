package models;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Iterator;

import models.Person;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import static org.springframework.ldap.query.LdapQueryBuilder.query;
import play.Logger;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldif.LDIFReader;

import configs.AppConfig;
import configs.TestLdapConfig;

@ContextConfiguration(classes={AppConfig.class, TestLdapConfig.class})
public class PersonRepositoryTest extends AbstractJUnit4SpringContextTests {

	static int ldapPort = 389;
	static InMemoryDirectoryServer ds;

    @Autowired
    private PersonRepository personRepository;

	@BeforeClass
	public static void createInMemoryLdapServer() throws Exception {
		InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig(
				"dc=example, dc=com");
		config.addAdditionalBindCredentials("cn=Directory Manager", "password");
		config.setListenerConfigs(InMemoryListenerConfig.createLDAPConfig(
				"default", ldapPort));
		config.setSchema(null);

		ds = new InMemoryDirectoryServer(config);
		ds.importFromLDIF(
				true,
				new LDIFReader(PersonRepositoryTest.class
						.getResourceAsStream("/example.ldif")));
		ds.startListening();
		Logger.info("Started LDAP server");
	}
	

	@Test
	public void testServer() throws LDAPException {
		// Get a client connection to the server and use it to perform various
		// operations.
		LDAPConnection conn = ds.getConnection();
		SearchResultEntry entry = conn.getEntry("dc=example,dc=com");
		//Logger.debug(entry.toLDIFString());
		//entry = conn.getEntry("CN=UserOne,ou=People,dc=example, dc=com");
		entry = conn.getEntry("uid=userone,ou=People,dc=example, dc=com");
		//Logger.debug(entry.toLDIFString());
		assertNotNull(entry);
	}
	
	@Test
	public void testNotConfigured() {
		assertNotNull(personRepository);
	}
	
    @Test
    public void findAll() {
        Iterable<Person> persons = personRepository.findAll();
        assertNotNull(persons);
        if (persons instanceof Collection) {
	        assertTrue(((Collection<Person>)persons).size() > 1);
	        assertTrue(((Collection<Person>)persons).size() >= 3);
        }
        for (Iterator<Person> iterator = persons.iterator(); iterator.hasNext();) {
			Person person = (Person) iterator.next();
			Logger.info(person.toString());
		}
    }

    @Test
    public void find() {
    	final String UID = "userone";
    	Person person = personRepository.findOne(query().where("uid").is(UID));
        assertNotNull(person);
        //Logger.debug(person.uid);
        assertEquals(UID, person.uid);
        assertEquals("User", person.givenName);
        assertEquals("One", person.sn);
        //Logger.debug(person.mail);
        assertEquals("userone@example.com", person.mail);
    }

    @Test
    public void findByUid() {
    	final String UID = "userone";
    	Person person = personRepository.findByUid(UID);
        assertNotNull(person);
        assertEquals(UID, person.uid);
        assertEquals("User", person.givenName);
        assertEquals("One", person.sn);
        assertEquals("userone@example.com", person.mail);
    }
    
    @Test
    public void searchPerson() {
    	Iterable<Person> persons = personRepository.findAll(
       		query().where("objectclass").is("person").and("uid").like("*usert*"));
        assertNotNull(persons);
        if (persons instanceof Collection) {
	        assertTrue(((Collection<Person>)persons).size() > 1);
	        assertTrue(((Collection<Person>)persons).size() == 2);
        }
    }
    
    @Test
    public void savePerson() {
    	Person person = personRepository.findByUid("newuser");
    	assertNull(person);
    	person = new Person();
    	person.cn = "New User";
    	person.givenName = "New";
    	person.mail = "newuser@example.com";
    	person.sn = "User";
    	person.uid = "newuser";
    	//person.dn = person.buildDn(person);
    	personRepository.save(person);
    	Person newPerson = personRepository.findByUid("newuser");
    	assertNotNull(newPerson);
    	assertEquals(person.cn, newPerson.cn);
    	assertEquals(person.mail, newPerson.mail);
    	assertEquals(person.uid, newPerson.uid);
    }

    @Test
    public void deletePerson() {
    	Person person = personRepository.findByUid("deleteuser");
    	assertNotNull(person);
    	personRepository.delete(person);
    	person = personRepository.findByUid("deleteuser");
    	assertNull(person);
    }

    @Test
    public void updatePerson() {
    	Person person = personRepository.findByUid("userthree");
    	assertNotNull(person);
    	person.cn="Newuser Three";
    	personRepository.save(person);
    	Person modPerson = personRepository.findByUid("userthree");
    	assertNotNull(modPerson);
    	assertEquals(person.cn, modPerson.cn);
    }
    

    @AfterClass
	public static void destroyInMemoryLdapServer() {
		ds.shutDown(true);
	}
}
