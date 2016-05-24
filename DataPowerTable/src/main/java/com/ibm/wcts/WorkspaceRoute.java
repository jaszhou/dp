package com.ibm.wcts;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.and;
import static spark.Spark.get;
import static spark.Spark.post;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Part;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;

import freemarker.template.SimpleHash;
import freemarker.template.TemplateException;
import spark.Request;
import spark.Response;

public class WorkspaceRoute extends BlogController {

	public WorkspaceRoute() {
		// super();

		try {
			initializeRoutes();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	void initializeRoutes() throws IOException {

		post("/attachfile", new FreemarkerBasedRoute("") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));

				// SimpleHash root = new SimpleHash();

				Util ut = new Util();

				
				String upload_dir =ut.getPropValues("upload_dir");

				MultipartConfigElement multipartConfigElement = new MultipartConfigElement(upload_dir);
				
				request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);

				try {

					String matchid = null;

					String description = null;

					Part idPart = request.raw().getPart("matchid");

					try (Scanner scanner = new Scanner(idPart.getInputStream())) {
						matchid = scanner.nextLine(); // read from the part
					}

					Part dPart = request.raw().getPart("description");

					if (dPart != null) {
						try (Scanner scanner = new Scanner(dPart.getInputStream())) {

							if (scanner.hasNext()) {
								description = scanner.nextLine(); // read from
																	// the
																	// part
							}
						}
					}

					System.out.println("matchid: " + matchid + " desc: " + description);

					// create a new collection
					MongoCollection<Document> c = clientDatabase.getCollection("match");

					final Part file = request.raw().getPart("upload");

					String fileName = getSubmittedFileName(file);

					System.out.println(fileName);

					String filePath = "/uploads";

					Path path = Paths.get(filePath);

					String dir = path.toFile().getAbsolutePath();

					File matchdir = new File(dir + File.separator + matchid );
					
					if (!matchdir.exists()) {
						if (matchdir.mkdir()) {
							System.out.println("Directory is created!");
						} else {
							System.out.println("Failed to create directory!");
						}
					}

					String newfile = dir + File.separator + matchid + File.separator + fileName;

					path = Paths.get(newfile);

					System.out.println(newfile);
					

					final InputStream in = file.getInputStream();
					Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
					in.close();

					// System.out.println(path.getFileSystem());

					Document attach = new Document("desc", description);
					attach.append("file", newfile).append("creator", username).append("lastUpdate", new Date());

					int mid = Integer.valueOf(matchid).intValue();

					// List<Document> attachs = new ArrayList<Document>();

					Document replace = new Document("$push", new Document("attachment", attach));

					System.out.println(replace.toJson());

					c.updateOne(eq("id", mid), replace);

				} catch (Exception e) {
					response.redirect("/internal_error?msg=" + BlogController.encode(e.toString()));
					e.printStackTrace();

				}

				// response.redirect("/plmlist");

				// template.process(root, writer);

			}
		});

		get("/inbox/:permalink", new FreemarkerBasedRoute("inbox.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String permalink = request.params(":permalink");
				String page = request.queryParams("page");

//				System.out.println("permalink:" + permalink);
				
				Document session = sessionDAO.findSessionById(getSessionCookie(request));

				String clientname = session.getString("clientname");



				// String resultid = request.queryParams("resultid");

				// int id = Integer.valueOf(resultid).intValue();

				// List<Document> matches = null;

				SimpleHash root = new SimpleHash();
				// if (resultid != null) {}

				// paging the recs here
				HashMap<String, Object> p = new HashMap<String, Object>();

				int totalrec = 0;

				if (clientname != null) {
					totalrec = PLMDAO.getMatchByStatusSize(clientname,permalink);

				} else {
					totalrec = PLMDAO.getMatchByStatusSize(permalink);

				}


				int pagesize = 50;
				

				// int totalpage=(int)Math.ceil(totalrec/pagesize);

				int totalpage = (int) (totalrec / pagesize) + ((totalrec % pagesize == 0) ? 0 : 1);

				if (page == null)
					page = "0";

				int pnum = Integer.valueOf(page).intValue();

				// (pnum * pagesize < totalrec ? pnum * pagesize : totalrec)

				List<Document> input = null;
				if (clientname != null) {
					input = PLMDAO.getMatchByStatus(clientname,permalink, pnum * pagesize, pagesize);

				} else {
					input = PLMDAO.getMatchByStatus(permalink, pnum * pagesize, pagesize);

				}


				// List<Document> input = matches.subList(pnum * pagesize,
				// ((pnum + 1) * pagesize < totalrec ? (pnum + 1) * pagesize :
				// totalrec));

				root.put("matches", input);

				p.put("pageNumber", pnum);
				p.put("pagesAvailable", totalpage);
				p.put("pageSize", pagesize);
				p.put("sortDirection", "Asc");
				p.put("sortField", "id");
				p.put("param", "");

				root.put("paginationData", p);
				root.put("permalink", permalink);

				// root.put("resultid", resultid);
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				root.put("username", username);
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);

			}
		});

		get("/download", new FreemarkerBasedRoute("") {

			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String file = request.queryParams("file");
				String filename = request.queryParams("filename");
				
				SimpleHash root = new SimpleHash();
				
				System.out.println("download file: "+file);

				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				root.put("username", username);
				
				ServletOutputStream sos = response.raw().getOutputStream();
				

				response.raw().setContentType("application/octet-stream");
				
//				response.header(header, value);
				
				//Content-Disposition: inline; filename="myfile.txt"
				
				
				
			    response.header("Content-Disposition","inline; filename="+filename);

				FileInputStream in = new FileInputStream(file);

				int bufferSize = 8192;
				
				byte[] buffer = new byte[bufferSize];
		        int read;

		        while ((read = in.read(buffer, 0, bufferSize)) != -1) {
		            sos.write(buffer, 0, read);
		        }
		        
				sos.flush();
				sos.close();
			}
		
		});
		
		get("/inboxexport", new FreemarkerBasedRoute("inboxexport.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String status = request.queryParams("status");

				SimpleHash root = new SimpleHash();
				
				System.out.println("got status: "+status);

				List<Document> matches = PLMDAO.getMatchByStatus(status);


				root.put("matches", matches);



				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				root.put("username", username);

				
				
				ServletOutputStream sos = response.raw().getOutputStream();
				
				
				response.raw().setContentType("application/octet-stream");
			    response.header("Content-Disposition","inline; filename="+"export.json");

			    
				for(Document match:matches){

					sos.write(match.toJson().getBytes());
					sos.write("\n".getBytes());
					
//					System.out.println("write doc to file ");
					
					
				}
				sos.flush();
				sos.close();
				
				
				
//				template.process(root, writer);

			}
		});

		get("/inboxexportcsv", new FreemarkerBasedRoute("inboxexport.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String status = request.queryParams("status");

				SimpleHash root = new SimpleHash();
				
				System.out.println("got status: "+status);

				List<Document> matches = PLMDAO.getMatchByStatus(status);


				root.put("matches", matches);



				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				root.put("username", username);
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				
				
				template.process(root, writer);

			}
		});

		get("/actionlist", new FreemarkerBasedRoute("actionlist.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				List<Document> actions = null;

				Document session = sessionDAO.findSessionById(getSessionCookie(request));

				String clientname = session.getString("clientname");

				SimpleHash root = new SimpleHash();
				actions = PLMDAO.getActionList(clientname,100);

				root.put("actions", actions);
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				root.put("username", username);
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);

			}
		});

		get("/searchmatchaction", new FreemarkerBasedRoute("searchmatchlist.ftl") {

			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String page = request.queryParams("page");
				String filter = request.queryParams("filter");

				String batchid = request.queryParams("batchid");

				System.out.println("search for batchid: " + batchid);

				MongoCollection<Document> alist = clientDatabase.getCollection("match");

				MongoCollection<Document> blist = clientDatabase.getCollection("batch");

				Document d = null;
				Document field = new Document();

				if (batchid != null) {

					d = (Document) blist.find(eq("batchid", Integer.valueOf(batchid).intValue())).first().get("fields");

					System.out.println("batch: " + d.toJson());
				}

				if (filter == null && d != null) {

					List<String> names = PLMDAO.getDocFieldName(d);

					// get check box for all cols

					for (int i = 0; i < names.size(); i++) {

						String key = names.get(i);

						String c = request.queryParams(key);

						// System.out.println(key + ": " + c );

						if (c != null && c != "") {

							field.append("input." + key, new Document("$regex", c).append("$options", "i"));
						}
					}

					String status = request.queryParams("status");
					field.append("status", status);

					field.append("batchid", batchid);

					filter = field.toJson();

				} else { // this is paging request

					field = Document.parse(filter);

				}

				// search here

				System.out.println("search: " + field.toJson());

				List<Document> recs = alist.find(field).into(new ArrayList<Document>());

				SimpleHash root = new SimpleHash();

				// paging the recs here
				HashMap<String, Object> p = new HashMap<String, Object>();

				int pagesize = 20;
				int totalrec = (int) recs.size();

				// int totalpage=(int)Math.ceil(totalrec/pagesize);

				int totalpage = (int) (totalrec / pagesize) + ((totalrec % pagesize == 0) ? 0 : 1);

				if (page == null)
					page = "0";

				int pnum = Integer.valueOf(page).intValue();

				// (pnum * pagesize < totalrec ? pnum * pagesize : totalrec)

				// List<Document> input = recs.subList(pnum * pagesize,
				// ((pnum + 1) * pagesize < totalrec ? (pnum + 1) * pagesize :
				// totalrec));

				// int skip_num = pnum * pagesize;
				// int limit_num = pagesize;

				List<Document> input = recs.subList(pnum * pagesize,
						((pnum + 1) * pagesize < totalrec ? (pnum + 1) * pagesize : totalrec));

				// List<Document> recs =
				// alist.find().skip(skip_num).limit(limit_num).into(new
				// ArrayList<Document>());

				root.put("matches", input);

				p.put("pageNumber", pnum);
				p.put("pagesAvailable", totalpage);
				p.put("pageSize", pagesize);
				p.put("sortDirection", "Asc");
				p.put("sortField", "id");

				filter = URLEncoder.encode(filter, "UTF-8");

				p.put("param", "batchid=" + batchid + "&filter=" + filter);

				root.put("paginationData", p);

				// root.put("listid", listid);
				root.put("batchid", batchid);
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				root.put("username", username);
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);

			}

		});

		post("/searchmatchaction", new FreemarkerBasedRoute("searchmatchlist.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String page = request.queryParams("page");
				String filter = request.queryParams("filter");

				String batchid = request.queryParams("batchid");

				System.out.println("search for batchid: " + batchid);

				MongoCollection<Document> alist = clientDatabase.getCollection("match");

				MongoCollection<Document> blist = clientDatabase.getCollection("batch");

				Document d = null;

				Document field = new Document();

				System.out.println("blist: " + blist.toString());

				SimpleHash root = new SimpleHash();

				if (batchid != null && blist != null) {

					Document e = (Document) blist.find(eq("batchid", Integer.valueOf(batchid).intValue())).first();

					if (e != null) {

						d = (Document) e.get("fields");

						System.out.println("batch: " + d.toJson());
					}

					if (filter == null && d != null) {

						List<String> names = PLMDAO.getDocFieldName(d);

						// get check box for all cols

						for (int i = 0; i < names.size(); i++) {

							String key = names.get(i);

							String c = request.queryParams(key);

							// System.out.println(key + ": " + c );

							if (c != null && c != "") {

								field.append("input." + key, new Document("$regex", c).append("$options", "i"));
							}
						}

						String status = request.queryParams("status");
						field.append("status", status);

						field.append("batchid", batchid);

						filter = field.toJson();

					} else { // this is paging request

						if (filter != null) {
							field = Document.parse(filter);
						}
					}

					// search here

					System.out.println("search: " + field.toJson());

					List<Document> recs = alist.find(field).into(new ArrayList<Document>());

					// paging the recs here
					HashMap<String, Object> p = new HashMap<String, Object>();

					int pagesize = 20;
					int totalrec = (int) recs.size();

					// int totalpage=(int)Math.ceil(totalrec/pagesize);

					int totalpage = (int) (totalrec / pagesize) + ((totalrec % pagesize == 0) ? 0 : 1);

					if (page == null)
						page = "0";

					int pnum = Integer.valueOf(page).intValue();

					// (pnum * pagesize < totalrec ? pnum * pagesize : totalrec)

					// List<Document> input = recs.subList(pnum * pagesize,
					// ((pnum + 1) * pagesize < totalrec ? (pnum + 1) * pagesize
					// :
					// totalrec));

					// int skip_num = pnum * pagesize;
					// int limit_num = pagesize;

					List<Document> input = recs.subList(pnum * pagesize,
							((pnum + 1) * pagesize < totalrec ? (pnum + 1) * pagesize : totalrec));

					// List<Document> recs =
					// alist.find().skip(skip_num).limit(limit_num).into(new
					// ArrayList<Document>());

					root.put("matches", input);

					p.put("pageNumber", pnum);
					p.put("pagesAvailable", totalpage);
					p.put("pageSize", pagesize);
					p.put("sortDirection", "Asc");
					p.put("sortField", "id");

					if (filter != null) {
						filter = URLEncoder.encode(filter, "UTF-8");
					}

					p.put("param", "batchid=" + batchid + "&filter=" + filter);

					root.put("paginationData", p);

					// root.put("listid", listid);
					root.put("batchid", batchid);

				}

				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				root.put("username", username);
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);

			}
		});

		post("/deletematch", new FreemarkerBasedRoute("") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String[] matchids = request.raw().getParameterValues("sel");

				MongoCollection<Document> alist = clientDatabase.getCollection("match");

				for (int i = 0; i < matchids.length; i++) {
					System.out.println("delete: " + matchids[i]);

					alist.findOneAndDelete(eq("id", Integer.valueOf(matchids[i]).intValue()));
				}

				// SimpleHash root = new SimpleHash();
				//
				// String username =
				// sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				// root.put("username", username);
				//
				// template.process(root, writer);

			}
		});

		post("/bulkupdate", new FreemarkerBasedRoute("") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String[] matchids = request.raw().getParameterValues("sel");

				String status = request.queryParams("status");

				MongoCollection<Document> alist = clientDatabase.getCollection("match");
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));

				for (int i = 0; i < matchids.length; i++) {
					System.out.println("update: " + matchids[i] + " status: " + status);

					// String matchid = request.queryParams("matchid");
					// String status = request.queryParams("status");
					// String comment = request.queryParams("comment");

					// SimpleHash root = new SimpleHash();

					PLMDAO.updateMatchByID(matchids[i], status, "Bulk Update", username);

					// root.put("match", match);
					//
					System.out.println("update match :" + matchids[i]);

					// alist.findOneAndUpdate(eq("id",
					// Integer.valueOf(matchids[i]).intValue()));
				}

				// SimpleHash root = new SimpleHash();
				//
				// String username =
				// sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				// root.put("username", username);
				//
				// template.process(root, writer);

			}
		});

		get("/searchmatch", new FreemarkerBasedRoute("searchmatch.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				// String listname = request.queryParams("listname");
				// String entityName = request.queryParams("entityname");

				String batchid = request.queryParams("batchid");

				System.out.println("batchid: " + batchid);

				Document session = sessionDAO.findSessionById(getSessionCookie(request));

				String clientname = session.getString("clientname");

				
				MongoCollection<Document> alist = clientDatabase.getCollection("match");

				Document d = null;

				if (batchid != null) {

					d = alist.find(and(eq("batchid", batchid),eq("clientname", clientname))).first();
				}

				// Document search = new Document("Name", entityName);

				// List<Document> recs =
				// PLMDAO.searchPLMList(listname,search,100);

				// MongoCollection<Document> blist =
				// abnDatabase.getCollection("batch");
				List<Document> batches = PLMDAO.findBatchList(clientname,100);

				SimpleHash root = new SimpleHash();

				// root.put("recs", recs);
				// root.put("listname", listname);
				root.put("listdoc", d);
				root.put("batchid", batchid);
				root.put("recs", batches);
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				root.put("username", username);

				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);

			}
		});

		get("/searchmatchform", new FreemarkerBasedRoute("searchmatchform.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				// String listname = request.queryParams("listname");
				// String listid = request.queryParams("listid");
				// String entityName = request.queryParams("entityname");

				String batchid = request.queryParams("batchid");

				System.out.println("batchid: " + batchid);
				System.out.println("search for batchid: " + batchid);

				MongoCollection<Document> alist = clientDatabase.getCollection("match");

				Document d = null;

				if (batchid != null) {

					d = alist.find(eq("batchid", batchid)).first();
				}

				SimpleHash root = new SimpleHash();

				root.put("listdoc", d);
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				root.put("username", username);

				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);

				// response.redirect("/searchmatch");

			}
		});

		get("/searchruleform", new FreemarkerBasedRoute("searchruleform.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String resultid = request.queryParams("resultid");

				// String username =
				// sessionDAO.findUserNameBySessionId(getSessionCookie(request));

				System.out.println("result id: "+resultid);
				
				
				Document session = sessionDAO.findSessionById(getSessionCookie(request));

				String clientname = session.getString("clientname");

				List<Document> rules = null;

				rules = PLMDAO.findRuleList(clientname,100);


				SimpleHash root = new SimpleHash();

				root.put("recs", rules);
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				root.put("username", username);
				root.put("resultid", resultid);
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);
		
				template.process(root, writer);

			
			}
		});

		post("/applyrule", new FreemarkerBasedRoute("") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String resultid = request.queryParams("resultid");

				// String username =
				// sessionDAO.findUserNameBySessionId(getSessionCookie(request));

				System.out.println("result id: "+resultid);
				
				String[] ruleids = request.raw().getParameterValues("sel");

				String status = request.queryParams("status");

				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));

				for (int i = 0; i < ruleids.length; i++) {
					System.out.println("apply: " + ruleids[i]);

					// String matchid = request.queryParams("matchid");
					// String status = request.queryParams("status");
					// String comment = request.queryParams("comment");

					// SimpleHash root = new SimpleHash();

					 
//					PLMDAO.updateMatchByID(ruleids[i], status, "Bulk Update", username);

					PLMDAO.applyRules(resultid, ruleids[i], username);
					
					// root.put("match", match);
					//
					System.out.println("applying rule :" + ruleids[i]);

					// alist.findOneAndUpdate(eq("id",
					// Integer.valueOf(ruleids[i]).intValue()));
				}


				
				Document session = sessionDAO.findSessionById(getSessionCookie(request));

				String clientname = session.getString("clientname");

				SimpleHash root = new SimpleHash();

				root.put("username", username);
				root.put("resultid", resultid);
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);
				
				template.process(root, writer);

			
			}
		});

		get("/matchview", new FreemarkerBasedRoute("matchview.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String matchid = request.queryParams("matchid");

				// System.out.println("review matchid: "+matchid);

				SimpleHash root = new SimpleHash();

				Document match = PLMDAO.getMatchByID(matchid);

				Document result = PLMDAO.getResultByMatchID(matchid);

				ArrayList<Document> screen = (ArrayList<Document>) result.get("screening");

				String fields = null;

				for (int i = 0; i < screen.size(); i++) {
					fields += " " + PLMDAO.getDocFieldName(screen.get(i)).get(0);

				}

				// System.out.println("result to string: "+fields);

				root.put("match", match);
				root.put("keys", fields);

				// System.out.println(match);
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				root.put("username", username);

				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);

			}
		});

		get("/attachment", new FreemarkerBasedRoute("attachmentajax.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String matchid = request.queryParams("matchid");

				System.out.println("matchid: " + matchid);

				SimpleHash root = new SimpleHash();

				Document match = PLMDAO.getMatchByID(matchid);

				root.put("match", match);

				System.out.println(match);
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				root.put("username", username);
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);

			}
		});

		get("/myModal", new FreemarkerBasedRoute("matchmodal.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String matchid = request.queryParams("matchid");

				// System.out.println("matchid: "+matchid);

				String entityid = request.queryParams("entityid");

				// System.out.println("entityid: "+entityid);

				SimpleHash root = new SimpleHash();

				Document match = PLMDAO.getMatchByID(matchid);
				List<Document> entities = (ArrayList<Document>) match.get("entity");

				Document entity = null;

				for (Document en : entities) {

					if (en.get("_id").equals(new ObjectId(entityid))) {
						entity = en;
						break;
					}
				}

				Document result = PLMDAO.getResultByMatchID(matchid);

				ArrayList<Document> screen = (ArrayList<Document>) result.get("screening");

				String fields = null;

				for (int i = 0; i < screen.size(); i++) {
					fields += " " + PLMDAO.getDocFieldName(screen.get(i)).get(0);

				}

				// System.out.println("get modal data here: "+entity.toJson());

				root.put("keys", fields);
				root.put("match", match);
				root.put("hit", entity);

//				System.out.println(match);
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				root.put("username", username);
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);

			}
		});

		post("/updatematch", new FreemarkerBasedRoute("") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String user = sessionDAO.findUserNameBySessionId(getSessionCookie(request));

				String matchid = request.queryParams("matchid");
				String status = request.queryParams("status");
				String comment = request.queryParams("comment");

				Document session = sessionDAO.findSessionById(getSessionCookie(request));

				String clientname = session.getString("clientname");

				// SimpleHash root = new SimpleHash();

				PLMDAO.updateMatchByID(matchid, status, comment, user,clientname);

				// root.put("match", match);
				//
				System.out.println("update match :" + matchid);

				// template.process(root, writer);

				response.redirect("/inbox/open");

			}
		});

		get("/caseview", new FreemarkerBasedRoute("caseview.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String resultid = request.queryParams("resultid");

				int id = Integer.valueOf(resultid).intValue();

				String recid = request.queryParams("recid");

				List<Document> matches = null;

				SimpleHash root = new SimpleHash();

				matches = PLMDAO.getMatchByResultRecID(id, recid);

				root.put("matches", matches);

				// System.out.println(matches);
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				root.put("username", username);
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);

			}
		});

	}

}
