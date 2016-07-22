package com.ibm.wcts;

import static spark.Spark.get;
import static spark.Spark.post;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.Cookie;

import org.apache.commons.lang3.StringEscapeUtils;
import org.bson.Document;

import com.ibm.wcts.BlogController.FreemarkerBasedRoute;

import freemarker.template.SimpleHash;
import freemarker.template.TemplateException;
import spark.Request;
import spark.Response;

public class UserManagementRoute extends BlogController {

	public UserManagementRoute() {

		try {
			
			initializeRoutes();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	void initializeRoutes() throws IOException {

		post("/newclient", new FreemarkerBasedRoute("newclient.ftl") {

			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {
				String clientname = request.queryParams("clientname");
				String status = request.queryParams("status");
				String email = request.queryParams("email");
				String workphone = request.queryParams("workphone");
				String mobilephone = request.queryParams("mobilephone");
				String address = request.queryParams("address");

				Document client = new Document("id",PLMDAO.getClientId());
				
				client.append("clientname", clientname).append("status", status).append("email", email)
						.append("workphone", workphone).append("mobilephone", mobilephone).append("email", email)
						.append("address", address);

				PLMDAO.addClient(client);
				
				SimpleHash root = new SimpleHash();

				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);
				
				root.put("username", username);


				template.process(root, writer);

			}

		});

		get("/newclient", new FreemarkerBasedRoute("newclient.ftl") {

			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {
				String clientname = request.queryParams("clientname");
				String status = request.queryParams("status");
				String email = request.queryParams("email");
				String workphone = request.queryParams("workphone");
				String mobilephone = request.queryParams("mobilephone");
				String address = request.queryParams("address");

				Document client = new Document();
				client.append("clientname", clientname).append("status", status).append("email", email)
						.append("workphone", workphone).append("mobilephone", mobilephone).append("email", email)
						.append("address", address);

				SimpleHash root = new SimpleHash();

				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);
				
				root.put("username", username);

				template.process(root, writer);

			}

		});

		get("/userlist", new FreemarkerBasedRoute("userlist.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				List<Document> recs = PLMDAO.getUserList(100);

				SimpleHash root = new SimpleHash();
				root.put("recs", recs);
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);
				
				root.put("username", username);

				template.process(root, writer);

			}
		});

		get("/clientlist", new FreemarkerBasedRoute("clientlist.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				List<Document> recs = PLMDAO.getClientList(100);

				SimpleHash root = new SimpleHash();
				root.put("recs", recs);
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);
				
				root.put("username", username);

				template.process(root, writer);

			}
		});

		get("/eventlist", new FreemarkerBasedRoute("eventlist.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				List<Document> recs = PLMDAO.getEventList(100);

				SimpleHash root = new SimpleHash();
				root.put("recs", recs);
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);
				
				root.put("username", username);

				template.process(root, writer);

			}
		});

		// handle the signup post
		post("/signup", new FreemarkerBasedRoute("signup.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {
				String email = request.queryParams("email");
				String username = request.queryParams("username");
				String clientname = request.queryParams("client");
				String password = request.queryParams("password");
				String status = request.queryParams("status");
				String firstname = request.queryParams("firstname");
				String lastname = request.queryParams("lastname");
				String workphone = request.queryParams("workphone");
				String mobilephone = request.queryParams("mobilephone");
				String department = request.queryParams("department");
				String role1 = request.queryParams("role1");
				String role2 = request.queryParams("role2");

				String verify = request.queryParams("verify");

				System.out.println("role1: " + role1);
				System.out.println("role2: " + role2);

				// set default status to inactivate and wait for approval
				status = "Inactive";
				
				
				if(clientname==null){
					
					clientname="Demo";
					
				}
				
				
				List<String> roles = new ArrayList<String>();

				if (role1 != null)
					roles.add(role1);
				if (role2 != null)
					roles.add(role2);

				HashMap<String, String> root = new HashMap<String, String>();
				root.put("username", StringEscapeUtils.escapeHtml4(username));
				root.put("email", StringEscapeUtils.escapeHtml4(email));

				if (validateSignup(username, password, verify, email, root)) {
					// good user
					System.out.println("Signup: Creating user with: " + username + " " + password);
					if (!userDAO.addUser(username, password, email, "Active", firstname, lastname, workphone, mobilephone,
							department, roles,clientname)) {
						
						// duplicate user
						root.put("username_error", "Username already in use, Please choose another");
						template.process(root, writer);
					} else {
						
						root.put("username_error", "User account has been created, please wait for administrator confirmation!");
						template.process(root, writer);

						// good user, let's start a session
//						String sessionID = sessionDAO.startSession(username);
//						System.out.println("Session ID is" + sessionID);
//
//						response.raw().addCookie(new Cookie("session", sessionID));
//						response.redirect("/welcome");
					}
				} else {
					// bad signup
					System.out.println("User Registration did not validate");
					template.process(root, writer);
				}
			}
		});

		post("/saveuser", new FreemarkerBasedRoute("") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {
				String email = request.queryParams("email");
				String clientname = request.queryParams("client");
				String username = request.queryParams("username");
				String status = request.queryParams("status");
				String firstname = request.queryParams("firstname");
				String lastname = request.queryParams("lastname");
				String workphone = request.queryParams("workphone");
				String mobilephone = request.queryParams("mobilephone");
				String department = request.queryParams("department");
				String role1 = request.queryParams("role1");
				String role2 = request.queryParams("role2");

				String verify = request.queryParams("verify");

				System.out.println("role1: " + role1);
				System.out.println("role2: " + role2);

				List<String> roles = new ArrayList<String>();

				if (role1 != null)
					roles.add(role1);
				if (role2 != null)
					roles.add(role2);

				HashMap<String, String> root = new HashMap<String, String>();
				root.put("username", StringEscapeUtils.escapeHtml4(username));
				root.put("email", StringEscapeUtils.escapeHtml4(email));

				userDAO.updateUser(username, email, status, firstname, lastname, workphone, mobilephone, department,
						roles,clientname);

			}
		});

		// present signup form for blog
		get("/signup", new FreemarkerBasedRoute("signup.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String clientID = request.queryParams("clientid");
				
				Document client = null;
				
				if(clientID!=null){
				
					client=PLMDAO.findClientByID(Integer.valueOf(clientID).intValue());
				}

				SimpleHash root = new SimpleHash();

				// initialize values for the form.
				root.put("username", "");
				root.put("client", client);
				root.put("password", "");
				root.put("email", "");
				root.put("password_error", "");
				root.put("username_error", "");
				root.put("email_error", "");
				root.put("verify_error", "");

				template.process(root, writer);
			}
		});

		get("/activate", new FreemarkerBasedRoute("activation.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String token = request.queryParams("token");
				String username = request.queryParams("username");

				SimpleHash root = new SimpleHash();

				if(userDAO.activateAccount(token)){
					
					// good user, let's start a session
					String sessionID = sessionDAO.startSession(username);
					System.out.println("Session ID is" + sessionID);

					response.raw().addCookie(new Cookie("session", sessionID));
					response.redirect("/plmlist");
				}else{
					
					root.put("username_error", "Failed to activate user!");
					template.process(root, writer);
				}
				
			}
		});

		post("/adminactivate", new FreemarkerBasedRoute("activation.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				
				String username = request.queryParams("username");

				System.out.println("activate user");
				
				SimpleHash root = new SimpleHash();

				if(userDAO.activateAccountAdmin(username)){
					
					// good user, let's start a session
//					String sessionID = sessionDAO.startSession(username);
//					System.out.println("Session ID is" + sessionID);

//					response.raw().addCookie(new Cookie("session", sessionID));
//					response.redirect("/welcome");
				}else{
					
					root.put("username_error", "Failed to activate user!");
					template.process(root, writer);
				}
				
			}
		});

		post("/admindeactivate", new FreemarkerBasedRoute("activation.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				
				String username = request.queryParams("username");

				System.out.println("deactivate user");
				
				SimpleHash root = new SimpleHash();

				if(userDAO.deactivateAccountAdmin(username)){
					
					// good user, let's start a session
					String sessionID = sessionDAO.startSession(username);
					System.out.println("Session ID is" + sessionID);

//					response.raw().addCookie(new Cookie("session", sessionID));
//					response.redirect("/welcome");
				}else{
					
					root.put("username_error", "Failed to activate user!");
					template.process(root, writer);
				}
				
			}
		});

		get("/about", new FreemarkerBasedRoute("about.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				SimpleHash root = new SimpleHash();

				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));

				root.put("username", username);

				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);
			}
		});


		get("/resetpwd", new FreemarkerBasedRoute("resetpwd.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				SimpleHash root = new SimpleHash();

				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));

				root.put("username", username);

				template.process(root, writer);
			}
		});

		get("/pwdoverride", new FreemarkerBasedRoute("pwdoverride.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				SimpleHash root = new SimpleHash();

				String userid = request.queryParams("userid");
				
				Document user = userDAO.findUser(userid);

				System.out.println(user.toJson());
				
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				
				root.put("username", username);
				root.put("user", user);
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);
			}
		});

		post("/resetpwdforce", new FreemarkerBasedRoute("resetstatus.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String newpassword = request.queryParams("newpassword");
				String verify = request.queryParams("verify");

				String userid = request.queryParams("userid");
				

				HashMap<String, String> root = new HashMap<String, String>();

				userDAO.updateUserPassword(userid, newpassword);
				

				root.put("username", userid);

				
				template.process(root, writer);
			}
		});

		post("/resetpwd", new FreemarkerBasedRoute("resetstatus.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String oldpassword = request.queryParams("oldpassword");
				String newpassword = request.queryParams("newpassword");
				String verify = request.queryParams("verify");

				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));

				HashMap<String, String> root = new HashMap<String, String>();

				Document user = userDAO.validateLoginwithInfo(username, oldpassword, root);


				root.put("username", username);

				if (user != null) {
					if (validateChangePassword(username, newpassword, verify, root)) {
						// update password

						if (userDAO.updateUserPassword(username, newpassword)) {
							String error = "Password updated successfully!";
							root.put("result", error);

						}

					} 

				}
				
				template.process(root, writer);
			}
		});

		get("/edituser", new FreemarkerBasedRoute("edituser.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				SimpleHash root = new SimpleHash();

				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));

				System.out.println(username);

				Document user = userDAO.findUser(username);

				System.out.println(user.toJson());

				root.put("username", username);
				root.put("user", user);

				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);
			}
		});

		get("/updateuser", new FreemarkerBasedRoute("updateuser.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String userid = request.queryParams("userid");
				
				SimpleHash root = new SimpleHash();

				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				System.out.println(username);

				Document user = userDAO.findUser(userid);

				System.out.println(user.toJson());

				root.put("username", username);
				root.put("user", user);

				template.process(root, writer);
			}
		});

		// present the login page
		get("/login", new FreemarkerBasedRoute("login.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {
				SimpleHash root = new SimpleHash();

				root.put("username", "");
				root.put("login_error", "");

				template.process(root, writer);
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
//					String sessionID = sessionDAO.startSession(user.get("_id").toString());
					String sessionID = sessionDAO.startSession(user);
					
					userDAO.addAudit(username, "User logged in", "User Activity");
					
					if (sessionID == null) {
						response.redirect("/internal_error");
					} else {
						// set the cookie for the user's browser
						response.raw().addCookie(new Cookie("session", sessionID));

						response.redirect("/plmlist");
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

		// create 404 page
		get("/page_not_found", new FreemarkerBasedRoute("page_not_found.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				SimpleHash root = new SimpleHash();

				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));

				System.out.println(username);

				Document user = userDAO.findUser(username);

				System.out.println(user.toJson());

				root.put("username", username);
				root.put("user", user);

				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);
				template.process(root, writer);

				
			}
		});

	}
}
