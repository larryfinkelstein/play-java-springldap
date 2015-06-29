/**
 * 
 */
package models;

import java.io.Serializable;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.support.LdapNameBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;

/**
 * @author Larry Finkelstein
 *
 */
@Entry(objectClasses={"inetOrgPerson","organizationalPerson","person","top"}, 
	base="ou=People,dc=example,dc=com")
public class Person implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2239911212619253274L;

	@Id
	@JsonSerialize(using = JsonNameSerializer.class)
	public Name dn;
	
	@Required(message="Common name is required")
	public String cn;
	@Required(message="Given name is required")
	public String givenName;
	@Email(message="Email not formatted correctly")
	public String mail;
	public String manager;
	public String mobile;
	@Required(message="Last name is required")
	@MinLength(value=2, message="Last name is too short")
	public String sn;
	public String title;
	public String telephonenumber;

	@Required(message="User id is required and must be unique")
	@DnAttribute(value="uid", index=3)
	public String uid;

	@Override
	public String toString() {
		return cn+" [" + uid + "]";
	}
}
