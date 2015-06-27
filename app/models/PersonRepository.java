/**
 * 
 */
package models;

import org.springframework.ldap.repository.LdapRepository;
import org.springframework.stereotype.Service;

/**
 * @author larryf
 *
 */
@Service
public interface PersonRepository extends LdapRepository<Person> {
	
	//@Query("uid={0}")
	Person findByUid(String uid);

}
