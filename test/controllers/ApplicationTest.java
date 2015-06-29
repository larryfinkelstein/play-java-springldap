package controllers;

import static org.junit.Assert.*;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;
import static play.test.Helpers.callAction;
import static play.test.Helpers.charset;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;
import static play.test.Helpers.status;

import java.util.Collections;
import java.util.Map;

import controllers.Application;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import play.mvc.Http;
import play.mvc.Http.Request;
import play.mvc.Result;
import play.api.mvc.RequestHeader;
import play.test.FakeApplication;
import play.test.FakeRequest;
import play.test.Helpers;
import play.twirl.api.Content;

public class ApplicationTest {
	
	public static FakeApplication app;
	
	@BeforeClass
	public static void startApp() {
		app = Helpers.fakeApplication();
		Helpers.start(app);
	}
	
	@Before
	public void setUp() throws Exception {
		Map<String, String> flashData = Collections.emptyMap();
		Map<String, Object> argData = Collections.emptyMap();
		Long id = 2L;
		play.api.mvc.RequestHeader header = mock(RequestHeader.class);
		Request request = null;
		Http.Context context = new Http.Context(id, header, request, flashData, flashData, argData);
		Http.Context.current.set(context);
	}
	
  @Test
  public void testIndex() {
    Result result = new Application().index();
    assertEquals(OK, status(result));
    assertEquals("text/html", contentType(result));
    assertEquals("utf-8", charset(result));
    assertTrue(contentAsString(result).contains("Spring LDAP"));
  }
	
	@Test
	public void testCallIndex() {
	  Result result = callAction(
	    controllers.routes.ref.Application.index(),
	    new FakeRequest(GET, "/")
	  );
	  assertEquals(OK, status(result));
	}

	private RequestHeader mock(Class<RequestHeader> class1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Test
	public void renderTemplate() {
		Content html = views.html.index.render("filter", null);
		assertEquals("text/html", contentType(html));
		assertTrue(contentAsString(html).contains("Spring LDAP Example"));
	}
  
	@AfterClass
    public static void stopApp() {
        Helpers.stop(app);
    }

}