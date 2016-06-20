package com.ibm.wcts;

import static spark.Spark.get;
import static spark.Spark.post;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.Cookie;

import org.apache.commons.lang3.StringEscapeUtils;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public class MySpark implements spark.servlet.SparkApplication{
	
	static BlogPostDAO blogPostDAO;
	static UserDAO userDAO;
	static SessionDAO sessionDAO;
	static ABNDAO ABNDAO;
	static PLMDAO PLMDAO;
	static PEDAO PEDAO;
	static MongoDatabase clientDatabase;
	static String mongoURL = "mongodb://localhost";
	
	
	public void init(){
		
		MongoClient mongoClient = null;
		
		System.out.println("testing");
		
//		Spark.staticFileLocation("/public");
		
		try {
			
			mongoClient = new MongoClient(new MongoClientURI(mongoURL));

			// setPort(8082); // update
			

			
			String clientDB ="ABN";


//			clientDatabase = mongoClient.getDatabase(clientDB);
//
//			blogPostDAO = new BlogPostDAO(clientDatabase);
//			userDAO = new UserDAO(clientDatabase);
//			sessionDAO = new SessionDAO(clientDatabase);
//
//			ABNDAO = new ABNDAO(clientDatabase);
//			PEDAO = new PEDAO(clientDatabase);
//			PLMDAO = new PLMDAO(clientDatabase);
			
			
			// present the login page
			get("/", new FreemarkerBasedRoute("login.ftl") {
				@Override
				protected void doHandle(Request request, Response response, Writer writer)
						throws IOException, TemplateException {
//					SimpleHash root = new SimpleHash();
//
//					root.put("username", "");
//					root.put("login_error", "");
//
//					template.process(root, writer);
					
					response.redirect("http://169.44.9.236:8082/pingreport");
				}
			});
			
			// process output coming from login form. On success redirect folks to
			// the welcome page
			// on failure, just return an error and let them try again.
			post("/login", new FreemarkerBasedRoute("login.ftl") {
				@Override
				protected void doHandle(Request request, Response response, Writer writer)
						throws IOException, TemplateException {

					String username = request.queryParams("username");
					String password = request.queryParams("password");

					System.out.println("Login: User submitted: '" + username + "'  '" + password+"'");

					Document user = userDAO.validateLogin(username, password);

					if (user.getString("message") == null) {

						// valid user, let's log them in
//						String sessionID = sessionDAO.startSession(user.get("_id").toString());
						String sessionID = sessionDAO.startSession(user);
						
						
						if (sessionID == null) {
							response.redirect("/internal_error");
						} else {
							// set the cookie for the user's browser
							response.raw().addCookie(new Cookie("session", sessionID));

							response.redirect("/pinglist");
						}
					} else {
						SimpleHash root = new SimpleHash();

						root.put("username", StringEscapeUtils.escapeHtml4(username));
						root.put("password", "");
						root.put("login_error", user.getString("message"));
						template.process(root, writer);
					}
				}
			});
	
		
			
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	// tags the tags string and put it into an array
	private ArrayList<String> extractTags(String tags) {

		// probably more efficent ways to do this.
		//
		// whitespace = re.compile('\s')

		tags = tags.replaceAll("\\s", "");
		String tagArray[] = tags.split(",");

		// let's clean it up, removing the empty string and removing dups
		ArrayList<String> cleaned = new ArrayList<String>();
		for (String tag : tagArray) {
			if (!tag.equals("") && !cleaned.contains(tag)) {
				cleaned.add(tag);
			}
		}

		return cleaned;
	}
	

	// helper function to get session cookie as string
	String getSessionCookie(final Request request) {
		if (request.raw().getCookies() == null) {
			return null;
		}
		for (Cookie cookie : request.raw().getCookies()) {
			if (cookie.getName().equals("session")) {
				return cookie.getValue();
			}
		}
		return null;
	}

	// helper function to get session cookie as string
	Cookie getSessionCookieActual(final Request request) {
		if (request.raw().getCookies() == null) {
			return null;
		}
		for (Cookie cookie : request.raw().getCookies()) {
			if (cookie.getName().equals("session")) {
				return cookie;
			}
		}
		return null;
	}
	
}

abstract class FreemarkerBasedRoute implements Route {
	final Template template;

	/**
	 * Constructor
	 *
	 * @param path
	 *            The route path which is used for matching. (e.g. /hello,
	 *            users/:name)
	 * 
	 *            Spark.get(this.appBasePath + "/users", (request, response)
	 *            -> { return this.getUsersView(request); }, new
	 *            FreeMarkerEngine());
	 * 
	 * 
	 */
	protected FreemarkerBasedRoute(final String templateName) throws IOException {
		// super(path);
		
		Configuration cfg;
		
		cfg = createFreemarkerConfiguration();
		
		template = cfg.getTemplate(templateName);
	}
	
	static Configuration createFreemarkerConfiguration() {
		Configuration retVal = new Configuration();
		retVal.setClassForTemplateLoading(MySpark.class, "/freemarker");
		return retVal;
	}

	@Override
	public Object handle(Request request, Response response) {
		StringWriter writer = new StringWriter();
		try {
			doHandle(request, response, writer);
		} catch (Exception e) {
			response.redirect("/internal_error?msg=" + encode(e.toString()));
			e.printStackTrace();
			response.redirect("/internal_error");
		}
		return writer;
	}

	static String encode(String input) {
		return "\"" + input + "\"";
	}
	
	protected abstract void doHandle(final Request request, final Response response, final Writer writer)
			throws IOException, TemplateException;

}

