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

/**
 * @author Larry Finkelstein
 *
 */
@Entry(objectClasses={"inetOrgPerson","organizationalPerson","person","top"}, base="ou=People")
// To avoid  No serializer found for class javax.naming.ldap.LdapName$1
@JsonIgnoreProperties("dn")
public class Person implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2239911212619253274L;
	public static final String BASE_DN = "ou=People,dc=example,dc=com";
	//public static final String BASE_DN = "";

	@Id
	public Name dn;
	
	public String cn;
	public String givenName;
	public String mail;
	public String manager;
	public String mobile;
	public String sn;
	public String title;
	public String telephonenumber;
	@DnAttribute(value="uid", index=1)
	public String uid;

	@Override
	public String toString() {
		return cn+" [" + uid + "]";
	}
	
	protected Name buildDn() {
		return LdapNameBuilder.newInstance(BASE_DN).add("uid", uid).build();
	}
}
