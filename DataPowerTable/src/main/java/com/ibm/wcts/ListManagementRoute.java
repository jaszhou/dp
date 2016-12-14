package com.ibm.wcts;

import static com.mongodb.client.model.Filters.eq;
import static spark.Spark.get;
import static spark.Spark.post;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
import javax.servlet.http.Part;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;

import freemarker.template.SimpleHash;
import freemarker.template.TemplateException;
import spark.Request;
import spark.Response;

public class ListManagementRoute extends BlogController {

	public ListManagementRoute() {

		try {
			initializeRoutes();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	void initializeRoutes() throws IOException {

		get("/newplm", new FreemarkerBasedRoute("newplm.ftl") {
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

		get("/plmlist", new FreemarkerBasedRoute("plmlist.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				List<Document> recs = PLMDAO.getPLMList(100);

				SimpleHash root = new SimpleHash();
				root.put("recs", recs);
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				root.put("username", username);
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);

			}
		});

		get("/searchentity", new FreemarkerBasedRoute("searchentity.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String listname = request.queryParams("listname");
				// String listid = request.queryParams("listid");
				String entityName = request.queryParams("entityname");

				MongoCollection<Document> alist = clientDatabase.getCollection(listname);

				Document d = alist.find().first();

				Document search = new Document("Name", entityName);

				// List<Document> recs =
				// PLMDAO.searchPLMList(listname,search,100);

				SimpleHash root = new SimpleHash();

				// root.put("recs", recs);
				root.put("listname", listname);
				root.put("listdoc", d);
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				root.put("username", username);
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);

			}
		});
		// searchmatchaction

		get("/editentity", new FreemarkerBasedRoute("editentity.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String listname = request.queryParams("listname");
				String entityid = request.queryParams("_id");

				MongoCollection<Document> alist = clientDatabase.getCollection(listname);

				// query.put("_id", new ObjectId(id));
				// DBObject dbObj = collection.findOne(query);
				//
				Document id = new Document("_id", new ObjectId(entityid));

				Document d = alist.find(id).first();

				SimpleHash root = new SimpleHash();

				// root.put("recs", recs);
				root.put("listname", listname);
				root.put("listdoc", d);
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				root.put("username", username);
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);

			}
		});

		get("/addentity", new FreemarkerBasedRoute("addentity.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String listname = request.queryParams("listname");

				MongoCollection<Document> alist = clientDatabase.getCollection(listname);

				Document d = alist.find().first();

				SimpleHash root = new SimpleHash();

				// root.put("recs", recs);
				root.put("listname", listname);
				root.put("listdoc", d);
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				root.put("username", username);
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);

			}
		});

		get("/deleteentity", new FreemarkerBasedRoute("editentity.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String listname = request.queryParams("listname");
				String entityid = request.queryParams("_id");

				MongoCollection<Document> alist = clientDatabase.getCollection(listname);

				// query.put("_id", new ObjectId(id));
				// DBObject dbObj = collection.findOne(query);
				//
				// Document id = new Document("_id", new ObjectId(entityid));

				// delete entity here

				alist.findOneAndDelete(eq("_id", new ObjectId(entityid)));

				response.redirect("/list?name=" + listname);
			}
		});

		post("/saveentity", new FreemarkerBasedRoute("editentity.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String listname = request.queryParams("listname");
				String entityid = request.queryParams("entityid");

				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				
				
				Document search = new Document("_id", new ObjectId(entityid));

				Document newDoc = new Document("_id", new ObjectId(entityid));

				System.out.println("_id: " + entityid);
				System.out.println("search: " + search);
				System.out.println("new doc: " + newDoc);

				MongoCollection<Document> alist = clientDatabase.getCollection(listname);

				System.out.println("listname: " + listname);

				Document d = alist.find().first();

				Document before = alist.find(search).first();
				
				List<String> names = PLMDAO.getDocFieldName(d);

				// get check box for all cols

				for (int i = 0; i < names.size(); i++) {

					String key = names.get(i);

					if (!key.contains("_id")) {

						System.out.println("key: "+key);
						
						if(request.queryParams(key)!=null){

						String c = request.queryParams(key).trim();

						newDoc.append(key, c);
						}
					}
				}

				// search here

				
				// record the change
				
				Document updatEntity = new Document("$set", newDoc);
				// PLMDAO.updateProfile(searchprofile, updateprofile);

				System.out.println("new Doc: " + updatEntity);

				alist.updateOne(search, updatEntity);
				
				Document after = alist.find(search).first();
				
				PEDAO.auditChange(username,listname, before, after);
				
				System.out.println("Record the changes");


//				d = alist.find(search).first();

				SimpleHash root = new SimpleHash();

				root.put("listname", listname);
				root.put("listdoc", after);
				root.put("username", username);
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);

			}
		});

		post("/addentityaction", new FreemarkerBasedRoute("listdetail.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String listname = request.queryParams("listname");

				Document newDoc = new Document();

				System.out.println("new doc: " + newDoc);

				MongoCollection<Document> alist = clientDatabase.getCollection(listname);

				System.out.println("listname: " + listname);

				Document d = alist.find().first();

				List<String> names = PLMDAO.getDocFieldName(d);

				// get check box for all cols

				for (int i = 0; i < names.size(); i++) {

					String key = names.get(i);

					if (!key.contains("_id")) {

						// System.out.println("key: "+key);

//						request.raw().setCharacterEncoding("UTF-8");
//						
						String c = request.queryParams(key).trim();
//						
//						
//						StringBuffer text = new StringBuffer(c);
//						
//					    int loc = (new String(text)).indexOf('\n');
//					    while(loc > 0){
//					        text.replace(loc, loc+1, "<BR>");
//					        loc = (new String(text)).indexOf('\n');
//					   }
					    

						newDoc.append(key, c);

					}
				}

				// search here

				alist.insertOne(newDoc);

				// String listid = request.queryParams("id");
				// String name = request.queryParams("name");
				// String page = request.queryParams("page");

				response.redirect("/list?name=" + listname);

			}
		});

		get("/list", new FreemarkerBasedRoute("listdetail.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String listid = request.queryParams("id");
				String name = request.queryParams("name");
				String page = request.queryParams("page");

				// get the custom list via name
				MongoCollection<Document> alist = clientDatabase.getCollection(name);
				
				

				SimpleHash root = new SimpleHash();

				// paging the recs here
				HashMap<String, Object> p = new HashMap<String, Object>();

				int pagesize = 20;
				int totalrec = (int) alist.count();
				// int totalpage=(int)Math.ceil(totalrec/pagesize);

				int totalpage = (int) (totalrec / pagesize) + ((totalrec % pagesize == 0) ? 0 : 1);

				if (page == null)
					page = "0";

				int pnum = Integer.valueOf(page).intValue();

				// (pnum * pagesize < totalrec ? pnum * pagesize : totalrec)

				// List<Document> input = recs.subList(pnum * pagesize,
				// ((pnum + 1) * pagesize < totalrec ? (pnum + 1) * pagesize :
				// totalrec));

				int skip_num = pnum * pagesize;
				int limit_num = pagesize;

				List<Document> recs = alist.find().skip(skip_num).limit(limit_num).sort(new Document("_id",-1)).into(new ArrayList<Document>());
				root.put("recs", recs);

				p.put("pageNumber", pnum);
				p.put("pagesAvailable", totalpage);
				p.put("pageSize", pagesize);
				p.put("sortDirection", "Asc");
				p.put("sortField", "id");
				p.put("param", "name=" + name + "&id=" + listid);

				root.put("paginationData", p);
				root.put("listid", listid);
				root.put("listname", name);
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				root.put("username", username);

				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);
				template.process(root, writer);

			}
		});

		post("/createplm", new FreemarkerBasedRoute("") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));

				// SimpleHash root = new SimpleHash();

				MultipartConfigElement multipartConfigElement = new MultipartConfigElement("/uploads");
				// request.raw().setAttribute("org.eclipse.multipartConfig",
				// multipartConfigElement);

				// ServletHolder mainHolder = new ServletHolder(new
				// MainServlet());
				// mainHolder.getRegistration().setMultipartConfig(multipartConfigElement);

				request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);

				try {

					String listname = request.raw().getParameter("listname");

					String description = request.raw().getParameter("description");

					Part idPart = request.raw().getPart("listname");

					try (Scanner scanner = new Scanner(idPart.getInputStream())) {
						listname = scanner.nextLine(); // read from the part
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

					System.out.println("list name: " + listname + " desc: " + description);

					// create a new collection
					MongoCollection<Document> c = clientDatabase.getCollection(listname);

					final Part file = request.raw().getPart("upload");

					String fileName = getSubmittedFileName(file);

					System.out.println(fileName);

					String filePath = "/uploads";

					Path path = Paths.get(filePath);

					String dir = path.toFile().getAbsolutePath();
					String newfile = dir + File.separator + fileName;

					path = Paths.get(newfile);

					// System.out.println(newfile);

					final InputStream in = file.getInputStream();
					Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
					in.close();

					// System.out.println(path.getFileSystem());

					File csvData = new File(newfile);

					int listId = PLMDAO.getListId();

					Document list = new Document().append("id", listId).append("name", listname)
							.append("filename", fileName).append("date", new Date()).append("creator", username)
							.append("description", description);

					List<Document> recs = new ArrayList<Document>();

					// CSVParser inParser = CSVParser.parse(inputFile,
					// StandardCharsets.UTF_8, CSVFormat.EXCEL.withHeader())

					// CSVParser parser = CSVParser.parse(csvData,
					// Charset.forName("UTF-8"), CSVFormat.RFC4180);
					CSVParser parser = CSVParser.parse(csvData, StandardCharsets.UTF_8, CSVFormat.RFC4180);

					CSVRecord header = null;
					List<String> cols = new ArrayList<String>();

					// save the size of batch
					int size = 0;

					String fieldName = "";

					for (CSVRecord csvRecord : parser) {

						size++;

						if (parser.getCurrentLineNumber() == 1) {
							// get column names from first line

							header = csvRecord;

							// add the header as profile

							Document p = new Document();
							for (int i = 0; i < header.size(); i++) {
								fieldName = header.get(i);

								if(fieldName!=""){
								// remove the '.' character since it's illegal
								// for Mongo

								fieldName = fieldName.replace('.', ' ');

								p.append(fieldName, "String");
								cols.add(fieldName);
								}
							}

							list.append("fields", p);
							continue;
						}

						System.out.println("line:" + size + " " + csvRecord.toString());

						Document d = new Document();

						for (int i = 0; i < header.size(); i++) {

							fieldName = header.get(i);

							if(fieldName!=""){
										// remove the '.' character since it's illegal for
							// Mongo

							fieldName = fieldName.replace('.', ' ');

							d.append(fieldName, csvRecord.get(i));
							}
						}

						// d.append("id", csvRecord.get(0)).append("NAME",
						// csvRecord.get(1))
						// .append("addr", csvRecord.get(3));

						recs.add(d);

						c.insertOne(d);

					}

					// add input records

					list.append("size", size);

					// list.append("input", recs);

					PLMDAO.addList(list);
					System.out.println("add new list");

					parser.close();

					// root.put("recs", recs);
					// root.put("batchid", batchId);
					// root.put("filename", fileName);
					// root.put("cols", cols);

				} catch (Exception e) {
					response.redirect("/internal_error?msg=" + BlogController.encode(e.toString()));
					e.printStackTrace();

				}

				response.redirect("/plmlist");

				// template.process(root, writer);

			}
		});

		get("/plm", new FreemarkerBasedRoute("plm.ftl") {
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

		post("/searchaction", new FreemarkerBasedRoute("listdetail.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String listname = request.queryParams("listname");
				String page = request.queryParams("page");
				String filter = request.queryParams("filter");

				MongoCollection<Document> alist = clientDatabase.getCollection(listname);

				System.out.println("listname: " + listname);

				Document field = new Document();

				if (filter == null) {
					Document d = alist.find().first();

					List<String> names = PLMDAO.getDocFieldName(d);

					// get check box for all cols

					for (int i = 0; i < names.size(); i++) {

						String key = names.get(i);

						String c = request.queryParams(key);

						// System.out.println(checkid + ": " + c + " " + u);

						// BasicDBObject regexQuery = new BasicDBObject();
						// regexQuery.put(field, new BasicDBObject("$regex",
						// name).append("$options", "i"));

						if (c != null && c != "") {

							if (c.trim().length() > 0)

								field.append(key, new Document("$regex", c).append("$options", "i"));
						}
					}

					filter = field.toJson();

				} else { // this is paging request

					field = Document.parse(filter);

				}

				// search here

				System.out.println("search: " + field.toJson());

				List<Document> recs = alist.find(field).sort(new Document("_id",-1)).into(new ArrayList<Document>());

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

				root.put("recs", input);

				p.put("pageNumber", pnum);
				p.put("pagesAvailable", totalpage);
				p.put("pageSize", pagesize);
				p.put("sortDirection", "Asc");
				p.put("sortField", "id");

				filter = URLEncoder.encode(filter, "UTF-8");

				p.put("param", "listname=" + listname + "&filter=" + filter);

				root.put("paginationData", p);

				// root.put("listid", listid);
				root.put("listname", listname);
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				root.put("username", username);
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);
				root.put("filter", filter);

				template.process(root, writer);

			}
		});

		get("/searchaction", new FreemarkerBasedRoute("listdetail.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String listname = request.queryParams("listname");
				String page = request.queryParams("page");
				String filter = request.queryParams("filter");

				System.out.println("listname: " + listname);
				System.out.println("filter: " + filter);

				MongoCollection<Document> alist = clientDatabase.getCollection(listname);

				Document field = new Document();

				if (filter == null) {
					Document d = alist.find().first();

					List<String> names = PLMDAO.getDocFieldName(d);

					// get check box for all cols

					for (int i = 0; i < names.size(); i++) {

						String key = names.get(i);

						String c = request.queryParams(key);

						// System.out.println(checkid + ": " + c + " " + u);

						// BasicDBObject regexQuery = new BasicDBObject();
						// regexQuery.put(field, new BasicDBObject("$regex",
						// name).append("$options", "i"));

						if (c != null && c != "") {

							// Document regexQuery = new Document();
							// regexQuery.put(key, new
							// Document("$regex",c).append("$options", "i"));

							field.append(key, new Document("$regex", c).append("$options", "i"));
						}
					}

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

				root.put("recs", input);

				p.put("pageNumber", pnum);
				p.put("pagesAvailable", totalpage);
				p.put("pageSize", pagesize);
				p.put("sortDirection", "Asc");
				p.put("sortField", "id");

				filter = URLEncoder.encode(filter, "UTF-8");

				p.put("param", "listname=" + listname + "&filter=" + filter);

				root.put("paginationData", p);

				// root.put("listid", listid);
				root.put("listname", listname);
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				root.put("username", username);

				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);

			}
		});

		// process output coming from login form. On success redirect folks to
		// the welcome page
		// on failure, just return an error and let them try again.
		post("/plm", new FreemarkerBasedRoute("plm.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String name = request.queryParams("name");
				String homePhone = request.queryParams("home_phone");
				String mobilePhone = request.queryParams("mobile_phone");
				String address = request.queryParams("address");

				List<Document> plms = null;

				System.out.println("Name submitted: " + name);

				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));

				if (name != null && name.length() >= 1) {
					plms = PLMDAO.findByName(name);
				}

				if (homePhone != null && homePhone.length() >= 1) {
					plms = PLMDAO.findByHomePhone(homePhone);
				}

				if (mobilePhone != null && mobilePhone.length() >= 1) {
					plms = PLMDAO.findByMobilePhone(mobilePhone);
				}

				if (address != null && address.length() >= 1) {
					plms = PLMDAO.findByAddress(address);
				}

				if (username != null) {
					// save previous enquery
					PEDAO.addPE(username, "PLM", name);
				}

				SimpleHash root = new SimpleHash();

				root.put("plms", plms);
				root.put("username", username);
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);

			}
		});

	}

}
