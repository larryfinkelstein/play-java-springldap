package controllers;

import static org.springframework.ldap.query.LdapQueryBuilder.query;
import static play.data.Form.form;
import static play.libs.Json.toJson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.LikeFilter;
import org.springframework.ldap.filter.OrFilter;
import org.springframework.ldap.query.SearchScope;

import models.Person;
import models.PersonRepository;
import play.*;
import play.data.Form;
import play.mvc.*;
import views.html.*;

@org.springframework.stereotype.Controller
public class Application extends Controller {

    @Autowired 
    private PersonRepository personRepository;

    /**
     * Display the index page
     */
    public Result index() {
    	return ok(index.render("", null));
    }

    /**
     * Display the index form with the currently filtered people
     * 
     * @param String filter 
     */
    public Result list(String filter) {
    	Iterable<Person> persons = filterPerson(filter);
    	if (persons == null) {
			return redirect(routes.Application.index());
		} else {
			return ok(index.render(filter, persons));
		}
    }

    /**
     * Search person repository by id
     * 
     * @param String id - user id (uid)
     * @return Person Json
     */
    public Result findPerson(String id) {
    	if (personRepository == null) {
    		// personService does not get autowired in Play 2.4.x
        	Logger.error("personRepo is null");
        	return internalServerError();
		} else {
	    	Person person = personRepository.findByUid(id);
	    	return ok(toJson(person));
		}
    }

    /**
     * Search PersonRepository for uid or cn like filter
     * 
     * @param String likeFilter
     * @return Iterable<Person>
     */
    private Iterable<Person> filterPerson(String likeFilter) {
    	String searchId = "*"+likeFilter+"*";
    	LikeFilter uidFilter = new LikeFilter("uid", searchId);
    	LikeFilter cnFilter = new LikeFilter("cn", searchId);
    	OrFilter orFilter = new OrFilter();
    	orFilter.append(uidFilter).append(cnFilter);
    	EqualsFilter ocFilter = new EqualsFilter("objectclass", "person");
    	AndFilter filter = new AndFilter();
    	filter.append(ocFilter).append(orFilter);
    	//Logger.debug(filter.toString());
    	Iterable<Person> persons = null;
    	try {
        	persons = personRepository.findAll(
            		query()
            		.searchScope(SearchScope.SUBTREE)
            		.countLimit(10)
            		.filter(filter));
		} catch (Exception e) {
			Logger.error(e.getMessage());
			flash("failure", "Unable to retrieve list of people - " + e.getMessage());
		}
    	return persons;
    }
    
    /**
     * Search uid or cn for id
     * 
     * @param String id
     * @return Person collection in Json 
     */
    public Result searchPerson(String id) {
    	Iterable<Person> persons = filterPerson(id);
    	if (persons == null) {
			return redirect(routes.Application.index());
		} else {
			return ok(toJson(persons));
		}
    }
    
    /**
     * Display the 'new Person form'.
     */
    public Result create() {
        Form<Person> personForm = form(Person.class);
        return ok(
            createPerson.render(personForm)
        );
    }
    
    /**
     * Handle the 'new Person form' submission 
     */
    public Result save() {
        Form<Person> personForm = form(Person.class).bindFromRequest();
        if(personForm.hasErrors()) {
            return badRequest(createPerson.render(personForm));
        }
        Person person = personForm.get();
        try {
            personRepository.save(person);
            flash("success", "Person " + personForm.get().cn + " has been created");
		} catch (Exception e) {
            return badRequest(createPerson.render(personForm));
		}
        return redirect(routes.Application.index());
    }
    
    /**
     * Handle person deletion 
     * 
     * @param String uid 
     */
    public Result delete(String uid) {
    	Person person = personRepository.findByUid(uid);
    	personRepository.delete(person);
        flash("success", "Person has been deleted");
        return redirect(routes.Application.index());
    }
    
    /**
     * Display the 'edit form' of a existing Person.
     *
     * @param id Id of the person to edit
     */
    public Result edit(String id) {
    	//Logger.debug(id);
        Form<Person> personForm = form(Person.class).fill(
            personRepository.findByUid(id)
        );
        return ok(
        	editPerson.render(id, personForm)
        );
    }
    
    /**
     * Handle the 'edit form' submission 
     *
     * @param id Id of the computer to edit
     */
    public Result update(String id) {
        Form<Person> personForm = form(Person.class).bindFromRequest();
        if(personForm.hasErrors()) {
        	Logger.warn("Errors on edit form "+personForm.errors());
            return badRequest(editPerson.render(id, personForm));
        }
        Person person = personForm.get();
        Person personWithDn = personRepository.findByUid(id);
        personRepository. save(person);
        flash("success", "Person " + personForm.get().uid + " has been updated");
        return redirect(routes.Application.index());
    }
}
