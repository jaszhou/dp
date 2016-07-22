/*
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Copyright 2015 MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibm.wcts;


import static spark.Spark.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.Cookie;
import javax.servlet.http.Part;

import org.apache.commons.lang3.StringEscapeUtils;
import org.bson.Document;

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

// test
/**
 * This class encapsulates the controllers for the blog web application. It
 * delegates all interaction with MongoDB to three Data Access Objects (DAOs).
 * <p/>
 * It is also the entry point into the web application.
 */
public class BlogController {
	static Configuration cfg;
	static BlogPostDAO blogPostDAO;
	static UserDAO userDAO;
	static SessionDAO sessionDAO;
	static ABNDAO ABNDAO;
	static PLMDAO PLMDAO;
	static PEDAO PEDAO;
	static MongoDatabase clientDatabase;
	static String mongoURL = "mongodb://192.168.56.101";


	
	public static void main(String[] args) throws IOException {
		if (args.length == 0) {

			Util ut = new Util();

			String port = "443";
			
			mongoURL=ut.getPropValues("mongoURL");
			

			System.out.println("server port: " + port);
			
			System.out.println("Default encoding:"+System.getProperty("file.encoding"));
			Spark.port(Integer.valueOf(port).intValue());
			
			
			Spark.staticFileLocation("/public"); // Static files

//			set404("C:\\spark\\400.html");
			
			// secure(keystoreFile, keystorePassword, truststoreFile,
			// truststorePassword);

			String current = new java.io.File(".").getCanonicalPath();
			System.out.println("Current dir:" + current);
			String currentDir = System.getProperty("user.dir");
			System.out.println("Current dir using System:" + currentDir);

//			Spark.externalStaticFileLocation("/var/www/public");

//			String keystoreFile = "comodo/keystore";
//			String truststoreFile = "comodo/keystore";
//			String keystorePassword = "mongodb";

			String keystoreFile = "ssl/keystore";
			String truststoreFile = "ssl/keystore";
			String keystorePassword = "jasonzhou";

//			Spark.secure(keystoreFile, keystorePassword, null, null);
	
			
//			Spark.secure(keystoreFile, keystorePassword, truststoreFile, keystorePassword);

//			get("/secureHello", (req, res) -> "Hello Secure World");
			
			// Spark.externalStaticFileLocation("D:\\download\\MongoDB\\blog\\src\\main\\resources\\public");

			setupConnection();

			new BlogController();
			new WorkspaceRoute();
			new PLMSearchRoute();
			new ListManagementRoute();
			new UserManagementRoute();
			new Restful();

			// new BlogController("mongodb://localhost");
		}
	}

	public static void setupConnection() {
		MongoClient mongoClient = null;

		try {

			Util ut = new Util();

			mongoURL=ut.getPropValues("mongoURL");
			
			mongoClient = new MongoClient(new MongoClientURI(mongoURL));

			// setPort(8082); // update
			

			
			String clientDB =ut.getPropValues("clientDB");


			clientDatabase = mongoClient.getDatabase(clientDB);

			blogPostDAO = new BlogPostDAO(clientDatabase);
			userDAO = new UserDAO(clientDatabase);
			sessionDAO = new SessionDAO(clientDatabase);

			ABNDAO = new ABNDAO(clientDatabase);
			PEDAO = new PEDAO(clientDatabase);
			PLMDAO = new PLMDAO(clientDatabase);

			cfg = createFreemarkerConfiguration();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			// mongoClient.close();
		}

	}

	public BlogController() {
		try {

			initializeRoutes();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
			template = cfg.getTemplate(templateName);
		}

		@Override
		public Object handle(Request request, Response response) {
			StringWriter writer = new StringWriter();
			try {
				doHandle(request, response, writer);
			} catch (Exception e) {
				response.redirect("/internal_error?msg=" + BlogController.encode(e.toString()));
				e.printStackTrace();
				response.redirect("/internal_error");
			}
			return writer;
		}

		protected abstract void doHandle(final Request request, final Response response, final Writer writer)
				throws IOException, TemplateException;

	}

	void initializeRoutes() throws IOException {
		// this is the blog home page
		get("/blog", new FreemarkerBasedRoute("blog_template.ftl") {
			@Override
			public void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));

				List<Document> posts = blogPostDAO.findByDateDescending(10);
				SimpleHash root = new SimpleHash();

				root.put("myposts", posts);
				if (username != null) {
					root.put("username", username);
				}

				template.process(root, writer);
			}
		});

		get("/", new FreemarkerBasedRoute("") {
			@Override
			public void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				response.redirect("/plmlist");

			}
		});

		// used to display actual blog post detail page
		get("/post/:permalink", new FreemarkerBasedRoute("entry_template.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {
				String permalink = request.params(":permalink");

				System.out.println("/post: get " + permalink);

				Document post = blogPostDAO.findByPermalink(permalink);
				if (post == null) {
					response.redirect("/post_not_found");
				} else {
					// empty comment to hold new comment in form at bottom of
					// blog entry detail page
					SimpleHash newComment = new SimpleHash();
					newComment.put("name", "");
					newComment.put("email", "");
					newComment.put("body", "");

					SimpleHash root = new SimpleHash();

					root.put("post", post);
					root.put("comment", newComment);
					String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
					root.put("username", username);

					template.process(root, writer);
				}
			}
		});

	
		// will present the form used to process new blog posts
		get("/newpost", new FreemarkerBasedRoute("newpost_template.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				// get cookie
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));

				if (username == null) {
					// looks like a bad request. user is not logged in
					response.redirect("/login");
				} else {
					SimpleHash root = new SimpleHash();
					root.put("username", username);

					template.process(root, writer);
				}
			}
		});

		// handle the new post submission
		post("/newpost", new FreemarkerBasedRoute("newpost_template.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String title = StringEscapeUtils.escapeHtml4(request.queryParams("subject"));
				String post = StringEscapeUtils.escapeHtml4(request.queryParams("body"));
				String tags = StringEscapeUtils.escapeHtml4(request.queryParams("tags"));

				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));

				if (username == null) {
					response.redirect("/login"); // only logged in users can
													// post to blog
				} else if (title.equals("") || post.equals("")) {
					// redisplay page with errors
					HashMap<String, String> root = new HashMap<String, String>();
					root.put("errors", "post must contain a title and blog entry.");
					root.put("subject", title);
					root.put("username", username);
					root.put("tags", tags);
					root.put("body", post);
					template.process(root, writer);
				} else {
					// extract tags
					ArrayList<String> tagsArray = extractTags(tags);

					// substitute some <p> for the paragraph breaks
					post = post.replaceAll("\\r?\\n", "<p>");

					String permalink = blogPostDAO.addPost(title, post, tagsArray, username);

					// now redirect to the blog permalink
					response.redirect("/post/" + permalink);
				}
			}
		});

			// process a new comment
		post("/newcomment", new FreemarkerBasedRoute("entry_template.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {
				String name = StringEscapeUtils.escapeHtml4(request.queryParams("commentName"));
				String email = StringEscapeUtils.escapeHtml4(request.queryParams("commentEmail"));
				String body = StringEscapeUtils.escapeHtml4(request.queryParams("commentBody"));
				String permalink = request.queryParams("permalink");

				Document post = blogPostDAO.findByPermalink(permalink);
				if (post == null) {
					response.redirect("/post_not_found");
				}
				// check that comment is good
				else if (name.equals("") || body.equals("")) {
					// bounce this back to the user for correction
					SimpleHash root = new SimpleHash();
					SimpleHash comment = new SimpleHash();

					comment.put("name", name);
					comment.put("email", email);
					comment.put("body", body);
					root.put("comment", comment);
					root.put("post", post);
					root.put("errors", "Post must contain your name and an actual comment");

					template.process(root, writer);
				} else {
					blogPostDAO.addPostComment(name, email, body, permalink);
					response.redirect("/post/" + permalink);
				}
			}
		});

			// present the login page
		get("/lookup", new FreemarkerBasedRoute("lookup.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));

				String abnNum = request.queryParams("abn_num");
				String acnNum = request.queryParams("acn_num");
				String busName = request.queryParams("bus_name");
				List<Document> abns = null;
				List<Document> acns = null;

				System.out.println("ABN: ABN submitted: " + abnNum + "  " + busName);

				SimpleHash root = new SimpleHash();

				if (abnNum != null) {
					abns = ABNDAO.findABN(abnNum, busName);

					if (username != null) {
						// save previous enquery
						PEDAO.addPE(username, "ABN", abnNum);
					}

					root.put("abns", abns);
				}

				if (acnNum != null) {
					acns = ABNDAO.findACN(acnNum);

					if (username != null) {
						// save previous enquery
						PEDAO.addPE(username, "ACN", acnNum);
					}

					root.put("acns", acns);
				}

				root.put("abn_num", "");
				root.put("username", username);

				template.process(root, writer);
			}
		});

		// process output coming from login form. On success redirect folks to
		// the welcome page
		// on failure, just return an error and let them try again.
		post("/lookup", new FreemarkerBasedRoute("lookup.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String abnNum = request.queryParams("abn_num");
				String busName = request.queryParams("bus_name");

				System.out.println("ABN: ABN submitted: " + abnNum + "  " + busName);

				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));

				List<Document> abns = ABNDAO.findABN(abnNum, busName);

				if (username != null) {
					// save previous enquery
					PEDAO.addPE(username, "ABN", abnNum + " " + busName);
				}

				SimpleHash root = new SimpleHash();

				root.put("abns", abns);
				root.put("username", username);

				template.process(root, writer);

				// Document user = userDAO.validateLogin(username, password);

				// if (user != null) {
				//
				// // valid user, let's log them in
				// String sessionID =
				// sessionDAO.startSession(user.get("_id").toString());
				//
				// if (sessionID == null) {
				// response.redirect("/internal_error");
				// }
				// else {
				// // set the cookie for the user's browser
				// response.raw().addCookie(new Cookie("session", sessionID));
				//
				// response.redirect("/welcome");
				// }
				// }
				// else {
				// SimpleHash root = new SimpleHash();
				//
				//
				// root.put("username",
				// StringEscapeUtils.escapeHtml4(username));
				// root.put("password", "");
				// root.put("login_error", "Invalid Login");
				// template.process(root, writer);
				// }
			}
		});

		// tells the user that the URL is dead
		get("/post_not_found", new FreemarkerBasedRoute("post_not_found.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {
				SimpleHash root = new SimpleHash();
				template.process(root, writer);
			}
		});

		// allows the user to logout of the blog
		get("/logout", new FreemarkerBasedRoute("signup.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String sessionID = getSessionCookie(request);

				if (sessionID == null) {
					// no session to end
					response.redirect("/login");
				} else {
					// deletes from session table
					sessionDAO.endSession(sessionID);

					// this should delete the cookie
					Cookie c = getSessionCookieActual(request);
					c.setMaxAge(0);

					response.raw().addCookie(c);

					response.redirect("/login");
				}
			}
		});

		// used to process internal errors
		get("/internal_error", new FreemarkerBasedRoute("error_template.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String msg = request.queryParams("msg");

				SimpleHash root = new SimpleHash();

				root.put("error", "System has encountered an error.");
				root.put("msg", msg);

				template.process(root, writer);
			}
		});
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

	// validates that the registration form has been filled out right and
	// username conforms
	public boolean validateSignup(String username, String password, String verify, String email,
			HashMap<String, String> errors) {
		String USER_RE = "^[a-zA-Z0-9_.@-]{3,40}$";
		String PASS_RE = "^.{3,20}$";
		String EMAIL_RE = "^[\\S]+@[\\S]+\\.[\\S]+$";

		errors.put("username_error", "");
		errors.put("password_error", "");
		errors.put("verify_error", "");
		errors.put("email_error", "");

		if (!username.matches(USER_RE)) {
			errors.put("username_error", "invalid username. try just letters and numbers");
			return false;
		}

		if (!password.matches(PASS_RE)) {
			errors.put("password_error", "invalid password.");
			return false;
		}

		if (!password.equals(verify)) {
			errors.put("verify_error", "password must match");
			return false;
		}

		if (!email.equals("")) {
			if (!email.matches(EMAIL_RE)) {
				errors.put("email_error", "Invalid Email Address");
				return false;
			}
		}

		return true;
	}


	public boolean validateChangePassword(String username, String password, String verify,
			HashMap<String, String> errors) {

		String PASS_RE = "^.{3,20}$";

		errors.put("password_error", "");
		errors.put("verify_error", "");

		if (!password.matches(PASS_RE)) {
			errors.put("result", "invalid password.");
			return false;
		}

		if (!password.equals(verify)) {
			errors.put("result", "password must match");
			return false;
		}

		return true;
	}

	static Configuration createFreemarkerConfiguration() {
		Configuration retVal = new Configuration();
		retVal.setClassForTemplateLoading(BlogController.class, "/freemarker");
		return retVal;
	}

	static String encode(String input) {
		return "\"" + input + "\"";
	}

	static String getSubmittedFileName(Part part) {
		for (String cd : part.getHeader("content-disposition").split(";")) {
			if (cd.trim().startsWith("filename")) {
				String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
				return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE
																													// fix.
			}
		}
		return null;
	}
}
