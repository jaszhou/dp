package com.ibm.wcts;

import static spark.Spark.get;
import static spark.Spark.post;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.bson.Document;

import com.mongodb.client.MongoCollection;

import freemarker.template.SimpleHash;
import freemarker.template.TemplateException;
import spark.Request;
import spark.Response;

public class PLMSearchRoute extends BlogController {

	public PLMSearchRoute() {

		try {
			initializeRoutes();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	void initializeRoutes() throws IOException {

		get("/batchlist", new FreemarkerBasedRoute("batchlist.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				Document session = sessionDAO.findSessionById(getSessionCookie(request));

				String clientname = session.getString("clientname");
				
				String msg = request.queryParams("msg");
				
				List<Document> batches = null;

				if (clientname != null) {
					batches = PLMDAO.findBatchList(clientname, 100);

					System.out.println("search on client: " + clientname);

				} else {
					batches = PLMDAO.findBatchList(100);
					System.out.println("search without a client ");

				}

				SimpleHash root = new SimpleHash();

				root.put("recs", batches);
				root.put("username", username);
				root.put("session", session);
				root.put("msg", msg);
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);

			}
		});

		get("/wizard", new FreemarkerBasedRoute("wizard.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));

				Document session = sessionDAO.findSessionById(getSessionCookie(request));

				String clientname = session.getString("clientname");

				List<Document> batches = null;

				if (clientname != null) {
					batches = PLMDAO.findBatchList(clientname, 100);

					System.out.println("search on client: " + clientname);

				} else {
					batches = PLMDAO.findBatchList(100);
					System.out.println("search without a client ");

				}

				SimpleHash root = new SimpleHash();

				root.put("recs", batches);
				root.put("username", username);

				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);

			}
		});

		get("/deletebatch", new FreemarkerBasedRoute("") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String batchid = request.queryParams("batchid");

				int intBatchId = Integer.valueOf(batchid).intValue();

				PLMDAO.deleteBatch(intBatchId);

				response.redirect("/batchlist");
			}
		});

		get("/deleteresult", new FreemarkerBasedRoute("") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String resultid = request.queryParams("resultid");

				int intResultId = Integer.valueOf(resultid).intValue();

				PLMDAO.deleteResult(intResultId);

				response.redirect("/resultlist");
			}
		});

		get("/profilelist", new FreemarkerBasedRoute("profilelist.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				// String username =
				// sessionDAO.findUserNameBySessionId(getSessionCookie(request));

				
				Document session = sessionDAO.findSessionById(getSessionCookie(request));

				String clientname = session.getString("clientname");

				List<Document> profiles = null;

				if (clientname != null) {

					profiles = PLMDAO.findProfileList(clientname,100);
				} else {
					profiles = PLMDAO.findProfileList(100);
				}



				SimpleHash root = new SimpleHash();

				root.put("recs", profiles);
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				root.put("username", username);
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);

			}
		});

		get("/rulelist", new FreemarkerBasedRoute("rulelist.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				// String username =
				// sessionDAO.findUserNameBySessionId(getSessionCookie(request));

				
				Document session = sessionDAO.findSessionById(getSessionCookie(request));

				String clientname = session.getString("clientname");

				List<Document> rules = null;

				rules = PLMDAO.findRuleList(clientname,100);


				SimpleHash root = new SimpleHash();

				root.put("recs", rules);
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				root.put("username", username);
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);

			}
		});

		get("/batchdetail", new FreemarkerBasedRoute("batchdetail.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String batchid = request.queryParams("batchid");
				String page = request.queryParams("page");

				System.out.println("batch id: " + batchid);

				List<Document> recs = null;
				Document batch = null;

				int id = Integer.valueOf(batchid).intValue();

				SimpleHash root = new SimpleHash();
				if (batchid != null) {
					batch = PLMDAO.findBatchByID(id);
				}

				Document cols = (Document) batch.get("fields");
				ArrayList<String> names = new ArrayList<String>();

				Set<Map.Entry<String, Object>> entrySet = cols.entrySet();
				for (Map.Entry<String, Object> entry : entrySet) {

					// System.out.println(entry.getKey());
					names.add(entry.getKey());

				}

				String fileName = batch.getString("filename");

				// retrieve profile list
				List<Document> profiles = PLMDAO.findProfileList(100);

				HashMap<String, Object> p = new HashMap<String, Object>();

				String batchname = batch.getString("recordCollection");
				MongoCollection<Document> batchRecordCollection = clientDatabase.getCollection(batchname);

				// paging the recs here

				int pagesize = 20;
				int totalrec = (int) batchRecordCollection.count();
				// int totalpage=(int)Math.ceil(totalrec/pagesize);

				int totalpage = (int) (totalrec / pagesize) + ((totalrec % pagesize == 0) ? 0 : 1);

				if (page == null)
					page = "0";

				int pnum = Integer.valueOf(page).intValue();

				recs = batchRecordCollection.find().skip(pnum * pagesize).limit(pagesize)
						.into(new ArrayList<Document>());

				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				root.put("username", username);

				root.put("recs", recs);

				p.put("pageNumber", pnum);
				p.put("pagesAvailable", totalpage);
				p.put("pageSize", pagesize);
				p.put("sortDirection", "Asc");
				p.put("sortField", "id");
				p.put("param", "batchid=" + batchid);

				root.put("profiles", profiles);
				root.put("batchid", batchid);
				root.put("filename", fileName);
				root.put("cols", names);
				root.put("paginationData", p);
				root.put("username", username);

				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);

			}
		});

		post("/batchdetailjson", "application/json", (request, response) -> {

			String batchid = request.queryParams("batchid");
			String jtStartIndex = request.queryParams("jtStartIndex");
			String jtPageSize = request.queryParams("jtPageSize");
			String jtSorting = request.queryParams("jtSorting");

			if (batchid == null)
				batchid = "3";

			String page = request.queryParams("page");

			System.out.println("json batch id: " + batchid + " index " + jtStartIndex + " size: " + jtPageSize);

			List<Document> recs = null;
			Document batch = null;

			int id = Integer.valueOf(batchid).intValue();

			SimpleHash root = new SimpleHash();
			if (batchid != null) {
				batch = PLMDAO.findBatchByID(id);
			}

			Document cols = (Document) batch.get("fields");
			ArrayList<String> names = new ArrayList<String>();

			Set<Map.Entry<String, Object>> entrySet = cols.entrySet();
			for (Map.Entry<String, Object> entry : entrySet) {

				// System.out.println(entry.getKey());
				names.add(entry.getKey());

			}

			String fileName = (String) batch.get("filename");

			// retrieve profile list
			List<Document> profiles = PLMDAO.findProfileList(100);

			HashMap<String, Object> p = new HashMap<String, Object>();

			String batchname = batch.getString("recordCollection");
			MongoCollection<Document> batchRecordCollection = clientDatabase.getCollection(batchname);

			// paging the recs here

			int pagesize = 20;
			int totalrec = (int) batchRecordCollection.count();
			// int totalpage=(int)Math.ceil(totalrec/pagesize);

			int totalpage = (int) (totalrec / pagesize) + ((totalrec % pagesize == 0) ? 0 : 1);

			if (page == null)
				page = "0";

			int pnum = Integer.valueOf(page).intValue();

			recs = batchRecordCollection.find().skip(pnum * pagesize).limit(pagesize).into(new ArrayList<Document>());

			root.put("recs", recs);

			p.put("pageNumber", pnum);
			p.put("pagesAvailable", totalpage);
			p.put("pageSize", pagesize);
			p.put("sortDirection", "Asc");
			p.put("sortField", "id");
			p.put("param", "batchid=" + batchid);

			root.put("profiles", profiles);
			root.put("batchid", batchid);
			root.put("filename", fileName);
			root.put("cols", names);
			root.put("paginationData", p);

			Document result = new Document("Result", "OK").append("Records", recs).append("TotalRecordCount", totalrec);

			System.out.println(result.toJson());

			return result;

		} , new JsonTransformer());

		post("/batchdetailjson", "application/json", (request, response) -> {

			String batchid = request.queryParams("batchid");
			String jtStartIndex = request.queryParams("jtStartIndex");
			String jtPageSize = request.queryParams("jtPageSize");
			String jtSorting = request.queryParams("jtSorting");

			if (batchid == null)
				batchid = "3";

			String page = request.queryParams("page");

			System.out.println("json batch id: " + batchid + " index " + jtStartIndex + " size: " + jtPageSize);

			List<Document> recs = null;
			Document batch = null;

			int id = Integer.valueOf(batchid).intValue();

			SimpleHash root = new SimpleHash();
			if (batchid != null) {
				batch = PLMDAO.findBatchByID(id);
			}

			Document cols = (Document) batch.get("fields");
			ArrayList<String> names = new ArrayList<String>();

			Set<Map.Entry<String, Object>> entrySet = cols.entrySet();
			for (Map.Entry<String, Object> entry : entrySet) {

				// System.out.println(entry.getKey());
				names.add(entry.getKey());

			}

			String fileName = (String) batch.get("filename");

			// retrieve profile list
			List<Document> profiles = PLMDAO.findProfileList(100);

			HashMap<String, Object> p = new HashMap<String, Object>();

			String batchname = batch.getString("recordCollection");
			MongoCollection<Document> batchRecordCollection = clientDatabase.getCollection(batchname);

			// paging the recs here

			int pagesize = 20;
			int totalrec = (int) batchRecordCollection.count();
			// int totalpage=(int)Math.ceil(totalrec/pagesize);

			int totalpage = (int) (totalrec / pagesize) + ((totalrec % pagesize == 0) ? 0 : 1);

			if (page == null)
				page = "0";

			int pnum = Integer.valueOf(page).intValue();

			recs = batchRecordCollection.find().skip(pnum * pagesize).limit(pagesize).into(new ArrayList<Document>());

			root.put("recs", recs);

			p.put("pageNumber", pnum);
			p.put("pagesAvailable", totalpage);
			p.put("pageSize", pagesize);
			p.put("sortDirection", "Asc");
			p.put("sortField", "id");
			p.put("param", "batchid=" + batchid);

			root.put("profiles", profiles);
			root.put("batchid", batchid);
			root.put("filename", fileName);
			root.put("cols", names);
			root.put("paginationData", p);

			Document result = new Document("Result", "OK").append("Records", recs).append("TotalRecordCount", totalrec);

			System.out.println(result.toJson());

			return result;

		} , new JsonTransformer());

		get("/getprofilelist", new FreemarkerBasedRoute("getprofilelist.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String batchid = request.queryParams("batchid");

				System.out.println("calling getprofilelist batch id: " + batchid);

				Document session = sessionDAO.findSessionById(getSessionCookie(request));

				String clientname = session.getString("clientname");

				List<Document> profiles = null;

				if (clientname != null) {
					profiles = PLMDAO.findProfileList(clientname, 100);
				} else {
					profiles = PLMDAO.findProfileList(100);

				}

				SimpleHash root = new SimpleHash();

				root.put("profiles", profiles);
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				root.put("username", username);
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);

			}
		});

		get("/result", new FreemarkerBasedRoute("result.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String batchid = request.queryParams("batchid");
				String resultid = request.queryParams("resultid");

				String page = request.queryParams("page");

				// System.out.println("get batchid: " + batchid);
				//
				// int id = Integer.valueOf(batchid).intValue();

				// paging the recs here
				HashMap<String, Object> p = new HashMap<String, Object>();

				List<Document> matches = null;

				int pagesize = 50;
				// int totalrec = PLMDAO.findResultListByBatchSize(batchid);
				int totalrec = PLMDAO.findResultListBySize(resultid);

				int totalpage = (int) (totalrec / pagesize) + ((totalrec % pagesize == 0) ? 0 : 1);

				if (page == null)
					page = "0";

				int pnum = Integer.valueOf(page).intValue();

				SimpleHash root = new SimpleHash();
				if (resultid != null) {

					matches = PLMDAO.findResultListByResult(resultid, pnum * pagesize, pagesize);

					root.put("matches", matches);

				}

				p.put("pageNumber", pnum);
				p.put("pagesAvailable", totalpage);
				p.put("pageSize", pagesize);
				p.put("sortDirection", "Asc");
				p.put("sortField", "id");
				p.put("param", "resultid=" + resultid);

				root.put("paginationData", p);
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				root.put("username", username);
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);

			}
		});

		get("/resultlist", new FreemarkerBasedRoute("resultlist.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String page = request.queryParams("page");
				Document session = sessionDAO.findSessionById(getSessionCookie(request));

				String clientname = session.getString("clientname");

				int pagesize = 50;
				
				int totalrec = 0;
				if (clientname != null) {
					totalrec = PLMDAO.findResultListSize(clientname);

				} else {
					totalrec = PLMDAO.findResultListSize();
				}


				// paging the recs here
				HashMap<String, Object> p = new HashMap<String, Object>();

				int totalpage = (int) (totalrec / pagesize) + ((totalrec % pagesize == 0) ? 0 : 1);

				if (page == null)
					page = "0";

				int pnum = Integer.valueOf(page).intValue();

				List<Document> recs = null;

				if (clientname != null) {

					recs = PLMDAO.findResultList(clientname,pnum * pagesize, pagesize);
				} else {
					recs = PLMDAO.findResultList(pnum * pagesize, pagesize);
				}

				
				SimpleHash root = new SimpleHash();

				p.put("pageNumber", pnum);
				p.put("pagesAvailable", totalpage);
				p.put("pageSize", pagesize);
				p.put("sortDirection", "Asc");
				p.put("sortField", "id");
				p.put("param", "");

				root.put("paginationData", p);

				root.put("recs", recs);
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				root.put("username", username);
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);
			}
		});

		post("/screen", new FreemarkerBasedRoute("") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				List<Document> batches = PLMDAO.findBatchList(100);
				SimpleHash root = new SimpleHash();

				String batchid = request.queryParams("batchid");
				String profileid = request.queryParams("profileid");
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));

				int id = Integer.valueOf(batchid).intValue();

				System.out.println("profileid: " + profileid);

//				int result = PLMDAO.batchScreening(batchid, profileid, username);
				
				FilterWorker worker = new FilterWorker(clientDatabase);
				
				int result = worker.batchScreening(batchid, profileid, username);

				// root.put("recs", batches);

				// template.process(root, writer);

				// response.redirect("/result?resultid=" + result);

			}
		});

		post("/interactiveact", new FreemarkerBasedRoute("") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String profileid = request.queryParams("profileid");
				String profilename = request.queryParams("profile");
				String comment = request.queryParams("comment");
				String listid = request.queryParams("list");
				String threshold = request.queryParams("thres");
				String fuzzy = request.queryParams("fuzzy");

				int batchId = 0;

				batchId = PLMDAO.getBatchId();

				String batchid = String.valueOf(batchId);

				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));

				Document profile = null;

				SimpleHash root = new SimpleHash();

				List<String> names = PLMDAO.getFieldsByID(profileid);

				// get check box for all cols

				// int pid = Integer.valueOf(profileid).intValue();
				Document searchprofile = new Document("id", profileid);

				Document searchDoc = new Document("_id", "1");

				ArrayList<Document> fields = new ArrayList<Document>();

				Document cols = new Document();

				for (int i = 0; i < names.size(); i++) {

					Document field = new Document();

					String key = names.get(i);

					String checkid = names.get(i) + "_screen";

					String c = request.queryParams(checkid);

					String checkid2 = names.get(i) + "_unique";

					String u = request.queryParams(checkid2);

					String checkid3 = names.get(i) + "_weight";

					String w = request.queryParams(checkid3);

					String checkid4 = names.get(i) + "_value";

					String v = request.queryParams(checkid4);

					// System.out.println(checkid + ": " + c + " " + u);

					field.append(key, new Document("Screening", c).append("Unique", u).append("Weight", w));

					fields.add(field);

					String fieldName = names.get(i);
					fieldName = fieldName.replace('.', ' ');

					cols.append(fieldName, "String");

					searchDoc.append(key, v);

				}

				System.out.println("profileid: " + profileid);
				System.out.println("search doc: " + searchDoc.toJson());

				// PLMDAO.batchScreening(id, profileid, username);

				// add batch

				Document batch = new Document().append("batchid", batchId).append("filename", "Interactive")
						.append("date", new Date()).append("creator", username).append("rec", searchDoc);

				batch.append("fields", cols).append("size", 1);

				// batchRecordCollection.insertOne(batch);

				PLMDAO.addBatch(batch);

				System.out.println(batch.toJson());


				FilterWorker worker = new FilterWorker(clientDatabase);

				int result = worker.InteractiveScreening(profileid, batchid, searchDoc, username);

				// root.put("recs", batches);

				// template.process(root, writer);

				response.redirect("/result?resultid=" + result);

			}
		});

		get("/batch", new FreemarkerBasedRoute("batch.ftl") {
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

		get("/deploy", new FreemarkerBasedRoute("deploy2.ftl") {
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

		get("/profile", new FreemarkerBasedRoute("profiledetail.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String profileid = request.queryParams("profileid");

				Document profile = null;

				SimpleHash root = new SimpleHash();

				profile = PLMDAO.findProfileByID(profileid);

				List<Document> lists = PLMDAO.getPLMList(100);

				List<Document> cats = PLMDAO.getCategoryList();

				List<String> names = PLMDAO.getFieldsByID(profileid);

				// Document fields = PLMDAO.getFieldsDocByID(profileid);
				//

				List<Document> f = (ArrayList<Document>) profile.get("fields");

				List<String> svalue = new ArrayList<String>();
				List<String> uvalue = new ArrayList<String>();
				List<String> wvalue = new ArrayList<String>();
				List<String> cvalue = new ArrayList<String>();

				List<Document> fld = new ArrayList<Document>();

				// for (Document item : fld) {item.toJson()}
				// fld = asList(new Document("$group",new
				// Document("_id","$state").append("totalPop",new
				// Document("$sum","$pop")))
				// ,new Document("$match",new Document("totalPop",new
				// Document("$gte",100000))))
				// List<Document> results =
				// collection.aggregate(pipeline).into(new
				// ArrayList<Document>())

				// pipeline =
				// asList(Document.parse("{$group:{_id:\"$state\",totalPop:{$sum:\"$pop\"}}}")),
				// Document.parse("{$match:{_id:\"$state\",totalPop:{$sum:\"$pop\"}}}"))

				for (int i = 0; i < f.size(); i++) {
					Document item = f.get(i);
					Document d = (Document) item.get(names.get(i));
					String s = d.getString("Screening");
					String u = d.getString("Unique");
					String w = d.getString("Weight");
					String c = d.getString("Category");

					svalue.add(s);
					uvalue.add(u);
					wvalue.add(w);
					cvalue.add(c);

					Document m = new Document();
					m.append("name", names.get(i)).append("svalue", s).append("uvalue", u).append("wvalue", w)
							.append("cvalue", c);

					fld.add(m);
					// System.out.println("s:"+s+" u:"+u);
				}

				// System.out.println(fld);

				root.put("profileid", profileid);
				root.put("profile", profile);
				root.put("cols", names);
				root.put("lists", lists);
				root.put("cats", cats);
				root.put("fld", fld);
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				root.put("username", username);
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);

			}
		});

		get("/rule", new FreemarkerBasedRoute("ruledetail.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String ruleid = request.queryParams("ruleid");

				Document rule = null;

				SimpleHash root = new SimpleHash();

				rule = PLMDAO.findRuleByID(ruleid);


				root.put("ruleid", ruleid);
				root.put("rule", rule);

				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				root.put("username", username);
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);

			}
		});

		get("/searchprofile", new FreemarkerBasedRoute("interactive.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String profileid = request.queryParams("profileid");

				Document profile = null;

				SimpleHash root = new SimpleHash();

				profile = PLMDAO.findProfileByID(profileid);

				List<Document> lists = PLMDAO.getPLMList(100);

				List<String> names = PLMDAO.getFieldsByID(profileid);

				// Document fields = PLMDAO.getFieldsDocByID(profileid);
				//

				List<Document> f = (ArrayList<Document>) profile.get("fields");

				List<String> svalue = new ArrayList<String>();
				List<String> uvalue = new ArrayList<String>();
				List<String> wvalue = new ArrayList<String>();

				List<Document> fld = new ArrayList<Document>();

				for (int i = 0; i < f.size(); i++) {
					Document item = f.get(i);
					Document d = (Document) item.get(names.get(i));
					String s = d.getString("Screening");
					String u = d.getString("Unique");
					String w = d.getString("Weight");
					svalue.add(s);
					uvalue.add(u);
					wvalue.add(w);

					Document m = new Document();
					m.append("name", names.get(i)).append("svalue", s).append("uvalue", u).append("wvalue", w);

					fld.add(m);
					// System.out.println("s:"+s+" u:"+u);
				}

				// System.out.println(fld);

				root.put("profileid", profileid);
				root.put("profile", profile);
				root.put("cols", names);
				root.put("lists", lists);
				root.put("fld", fld);
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				root.put("username", username);

				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);

			}
		});

		get("/deleteprofile", new FreemarkerBasedRoute("") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String profileid = request.queryParams("profileid");

				System.out.println("profile id to delete: " + profileid);

				SimpleHash root = new SimpleHash();

				PLMDAO.deleteProfileByID(profileid);

				// List<Document> profiles = PLMDAO.findProfileList(100);
				//
				// root.put("recs", profiles);
				// String username =
				// sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				// root.put("username", username);
				//
				// template.process(root, writer);

				response.redirect("/profilelist");

			}
		});

		get("/deleterule", new FreemarkerBasedRoute("") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String ruleid = request.queryParams("ruleid");

				System.out.println("rule id to delete: " + ruleid);

				SimpleHash root = new SimpleHash();

				PLMDAO.deleteRuleByID(ruleid);

				// List<Document> rules = PLMDAO.findruleList(100);
				//
				// root.put("recs", rules);
				// String username =
				// sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				// root.put("username", username);
				//
				// template.process(root, writer);

				response.redirect("/rulelist");

			}
		});

		get("/getprofilelist", new FreemarkerBasedRoute("/profilelistajax.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				SimpleHash root = new SimpleHash();

				List<Document> profiles = PLMDAO.findProfileList(100);

				root.put("recs", profiles);
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				root.put("username", username);
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);

				// response.redirect("/profilelist");

			}
		});

		post("/saveprofile", new FreemarkerBasedRoute("profiledetail.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String batchid = request.queryParams("batchid");
				String profilename = request.queryParams("profile");
				String comment = request.queryParams("comment");
				String threshold = request.queryParams("thres");
				String fuzzy = request.queryParams("fuzzy");
				String mlist = request.queryParams("mlist");

				System.out.println("mlist: " + mlist);

				Document session = sessionDAO.findSessionById(getSessionCookie(request));

				String clientname = session.getString("clientname");

				List<String> mlists = null;

				if (mlist != null)
					mlists = Arrays.asList(mlist.split(","));

				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				List<Document> recs = null;
				Document batch = null;

				int id = Integer.valueOf(batchid).intValue();

				SimpleHash root = new SimpleHash();
				if (batchid != null) {
					recs = PLMDAO.findRecsByBatchID(id);
					batch = PLMDAO.findBatchByID(id);
					root.put("recs", recs);
				}

				Document cols = (Document) batch.get("fields");
				ArrayList<String> names = new ArrayList<String>();

				Set<Map.Entry<String, Object>> entrySet = cols.entrySet();
				for (Map.Entry<String, Object> entry : entrySet) {

					// System.out.println(entry.getKey());
					names.add(entry.getKey());

				}

				// get check box for all cols

				String pid = UUID.randomUUID().toString();
				System.out.println("UUID: " + pid);

				Document profile = new Document("id", pid);
				profile.append("name", profilename).append("date", new Date()).append("creator", username)
						.append("fieldnames", names).append("comment", comment).append("batchid", batchid)
						.append("threshold", threshold).append("fuzzy", fuzzy).append("mlist", mlists);

				if (clientname != null) {

					profile.append("clientname", clientname);

				} else {
					profile.append("clientname", "CloudScreening");
				}

				ArrayList<Document> fields = new ArrayList<Document>();

				for (int i = 0; i < names.size(); i++) {

					Document field = new Document();

					String key = names.get(i);

					String checkid = names.get(i) + "_screen";

					String c = request.queryParams(checkid);

					String checkid2 = names.get(i) + "_unique";

					String u = request.queryParams(checkid2);

					// System.out.println(checkid + ": " + c + " " + u);
					String checkid3 = names.get(i) + "_weight";

					String w = request.queryParams(checkid3);

					// System.out.println(checkid + ": " + c + " " + u);
					String category = names.get(i) + "_category";

					String cat_val = request.queryParams(category);

					// System.out.println(checkid + ": " + c + " " + u);

					field.append(key, new Document("Screening", c).append("Unique", u).append("Weight", w)
							.append("Category", cat_val));

					fields.add(field);

				}

				profile.append("fields", fields).append("mlist", mlists);

				PLMDAO.addProfile(profile);

				// String fileName = (String) batch.get("filename");
				// root.put("batchid", batchid);
				// root.put("recs", recs);
				// root.put("filename", fileName);
				// root.put("cols", names);
				// template.process(root, writer);

				response.redirect("/profilelist");

			}
		});


		post("/saverule", new FreemarkerBasedRoute("ruledetail.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String rulename = request.queryParams("rule");
				String comment = request.queryParams("comment");
				String condition = request.queryParams("condition");

				Document session = sessionDAO.findSessionById(getSessionCookie(request));

				String clientname = session.getString("clientname");


				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				SimpleHash root = new SimpleHash();

				// get check box for all cols

				String pid = UUID.randomUUID().toString();
				System.out.println("UUID: " + pid);

				Document rule = new Document("id", pid);
				rule.append("name", rulename).append("date", new Date()).append("creator", username)
						.append("comment", comment).append("condition", condition);

				if (clientname != null) {

					rule.append("clientname", clientname);

				} else {
					rule.append("clientname", "CloudScreening");
				}

				PLMDAO.addRule(rule);


				response.redirect("/rulelist");

			}
		});

		post("/updaterule", new FreemarkerBasedRoute("ruledetail.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String ruleid = request.queryParams("ruleid");
				String rulename = request.queryParams("rule");
				String comment = request.queryParams("comment");
				String condition = request.queryParams("condition");

				Document session = sessionDAO.findSessionById(getSessionCookie(request));

				String clientname = session.getString("clientname");


				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				SimpleHash root = new SimpleHash();

				// get check box for all cols

				Document rule = new Document("id", ruleid);
				rule.append("name", rulename).append("date", new Date()).append("creator", username)
						.append("comment", comment).append("condition", condition);

				if (clientname != null) {

					rule.append("clientname", clientname);

				} else {
					rule.append("clientname", "CloudScreening");
				}
				
				Document updaterule = new Document("$set", rule);
				
				Document searchrule = new Document("id", ruleid);

				PLMDAO.updateRule(searchrule, updaterule);


				response.redirect("/rulelist");

			}
		});

		post("/updateprofile", new FreemarkerBasedRoute("profiledetail.ftl") {

			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String profileid = request.queryParams("profileid");
				String profilename = request.queryParams("profile");
				String comment = request.queryParams("comment");
				String batchid = request.queryParams("batchid");
				String threshold = request.queryParams("thres");
				String fuzzy = request.queryParams("fuzzy");

				String mlist = request.queryParams("mylist");

				System.out.println("mlist: " + mlist);

				List<String> mlists = null;

				if (mlist != null)
					mlists = Arrays.asList(mlist.split(","));

				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));

				Document profile = null;

				SimpleHash root = new SimpleHash();

				List<String> names = PLMDAO.getFieldsByID(profileid);

				// get check box for all cols

				// int pid = Integer.valueOf(profileid).intValue();
				Document searchprofile = new Document("id", profileid);

				Document newprofile = new Document("id", profileid);
				newprofile.append("name", profilename).append("date", new Date()).append("creator", username)
						.append("fieldnames", names).append("comment", comment).append("threshold", threshold)
						.append("fuzzy", fuzzy).append("batchid", batchid).append("mlist", mlists);

				ArrayList<Document> fields = new ArrayList<Document>();

				for (int i = 0; i < names.size(); i++) {

					Document field = new Document();

					String key = names.get(i);

					String checkid = names.get(i) + "_screen";

					String c = request.queryParams(checkid);

					String checkid2 = names.get(i) + "_unique";

					String u = request.queryParams(checkid2);

					String checkid3 = names.get(i) + "_weight";

					String w = request.queryParams(checkid3);

					String category = names.get(i) + "_category";

					String cat_val = request.queryParams(category);

					// System.out.println(checkid + ": " + c + " " + u);

					field.append(key, new Document("Screening", c).append("Unique", u).append("Weight", w)
							.append("Category", cat_val));

					fields.add(field);

				}

				newprofile.append("fields", fields);

				// System.out.println(newprofile);
				Document updateprofile = new Document("$set", newprofile);
				PLMDAO.updateProfile(searchprofile, updateprofile);

				response.redirect("/profile?profileid=" + profileid);

				// template.process(root, writer);

			}

		});

		post("/upload", new FreemarkerBasedRoute("batchlist.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				SimpleHash root = new SimpleHash();
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));

				Document session = sessionDAO.findSessionById(getSessionCookie(request));

				String clientname = session.getString("clientname");
				
				String msg="";

				MultipartConfigElement multipartConfigElement = new MultipartConfigElement("/uploads");
				request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);

				try {

					final Part file = request.raw().getPart("upload");
					String fileName = getSubmittedFileName(file);

					System.out.println(fileName);

					String filePath = "/uploads";

					Path path = Paths.get(filePath);

					String dir = path.toFile().getAbsolutePath();
					String newfile = dir + File.separator + fileName;

					path = Paths.get(newfile);

					System.out.println(newfile);

					final InputStream in = file.getInputStream();
					Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
					in.close();

					// System.out.println(path.getFileSystem());

					File csvData = new File(newfile);

					int batchId = PLMDAO.getBatchId();

					Document batch = new Document().append("batchid", batchId).append("filename", fileName)
							.append("date", new Date()).append("creator", username);

					if (clientname != null) {
						batch.append("clientname", clientname);
					} else {
						// set default to cloudscreening
						batch.append("clientname", "CloudScreening");
					}
					// put batch records into a stand alone collection

					String batchname = "batch" + batchId;

					// create new collection
					MongoCollection<Document> batchRecordCollection = clientDatabase.getCollection(batchname);

					List<Document> recs = new ArrayList<Document>();

					CSVParser parser = CSVParser.parse(csvData, Charset.defaultCharset(), CSVFormat.RFC4180);

					CSVRecord header = null;
					List<String> cols = new ArrayList<String>();

					// save the size of batch
					int size = 0;

					for (CSVRecord csvRecord : parser) {

						
						if(csvRecord==null) continue;
						
						if(csvRecord.size()==1) continue;
						
						
//						System.out.println(csvRecord+" size: "+csvRecord.size());

						if (parser.getCurrentLineNumber() == 1) {
							// get column names from first line

							header = csvRecord;

							// add the header as profile

							Document p = new Document();
							for (int i = 0; i < header.size(); i++) {

								String fieldName = header.get(i);
								fieldName = fieldName.replace('.', ' ');

								p.append(fieldName, "String");
								cols.add(fieldName);
							}

							batch.append("fields", p);
							continue;
						}

						size++;

						// manually generate a unique id for input record
						Document d = new Document();
						// Document d = new Document();

						for (int i = 0; i < header.size(); i++) {

							// remove the '.' character since it's illegal for
							// Mongo

							try {
							String fieldName = header.get(i);

							fieldName = fieldName.replace('.', ' ');

							String fieldValue;
								fieldValue = csvRecord.get(i);
								fieldValue = fieldValue.replace('.', ' ');

								d.append(fieldName, fieldValue);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								
								msg+="line "+size+": "+csvRecord.toString()+" Error:"+BlogController.encode(e.toString())+"\n";
								
							}

						}

						// d.append("id", csvRecord.get(0)).append("NAME",
						// csvRecord.get(1))
						// .append("addr", csvRecord.get(3));

						recs.add(d);

						// add record into batch record collection
						batchRecordCollection.insertOne(d);
					}

					parser.close();

					// add input records

					batch.append("size", size).append("recordCollection", batchname);

					// batch.append("input", recs);

					PLMDAO.addBatch(batch);
					System.out.println("add new batch");

					response.redirect("/batchlist?msg="+ msg);

				} catch (Exception e) {
					response.redirect("/internal_error?msg=" + BlogController.encode(e.toString()));
					e.printStackTrace();

				}
				root.put("username", username);

				template.process(root, writer);

			}
		});

		post("/deployjar", new FreemarkerBasedRoute("") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				SimpleHash root = new SimpleHash();
				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));

				MultipartConfigElement multipartConfigElement = new MultipartConfigElement("/uploads");
				request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);

				try {

					final Part file = request.raw().getPart("upload");
					String fileName = getSubmittedFileName(file);

					System.out.println(fileName);

					String filePath = "/uploads";

					Path path = Paths.get(filePath);

					String dir = path.toFile().getAbsolutePath();
					String newfile = dir + File.separator + fileName;

					path = Paths.get(newfile);

					System.out.println(newfile);

					final InputStream in = file.getInputStream();
					Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
					in.close();

					System.out.println(path.getFileSystem());

					response.redirect("/deploy");

				} catch (Exception e) {
					response.redirect("/internal_error?msg=" + BlogController.encode(e.toString()));
					e.printStackTrace();

					response.redirect("/internal_error?msg=" + BlogController.encode(e.toString()));

				}
				root.put("username", username);
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);

			}
		});

		get("/addprofile", new FreemarkerBasedRoute("addprofile.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				String batchid = request.queryParams("batchid");

				List<Document> recs = null;
				Document batch = null;

				Document session = sessionDAO.findSessionById(getSessionCookie(request));

				String clientname = session.getString("clientname");

				int id = Integer.valueOf(batchid).intValue();

				SimpleHash root = new SimpleHash();
				if (batchid != null) {
					recs = PLMDAO.findRecsByBatchID(id);
					batch = PLMDAO.findBatchByID(id);
					root.put("recs", recs);
				}

				Document cols = (Document) batch.get("fields");
				ArrayList<String> names = new ArrayList<String>();

				Set<Map.Entry<String, Object>> entrySet = cols.entrySet();
				for (Map.Entry<String, Object> entry : entrySet) {

					// System.out.println(entry.getKey());
					names.add(entry.getKey());

				}

				List<Document> lists = null;

				// if (clientname != null) {
				// lists = PLMDAO.getPLMList(clientname,100);
				//
				// } else {
				// lists = PLMDAO.getPLMList(100);
				// }

				lists = PLMDAO.getPLMList(100);

				List<Document> cats = PLMDAO.getCategoryList();

				String fileName = (String) batch.get("filename");

				root.put("batchid", batchid);

				root.put("recs", recs);
				root.put("filename", fileName);
				root.put("cols", names);
				root.put("lists", lists);
				root.put("cats", cats);

				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				root.put("username", username);
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);

			}
		});

		get("/addrule", new FreemarkerBasedRoute("addrule.ftl") {
			@Override
			protected void doHandle(Request request, Response response, Writer writer)
					throws IOException, TemplateException {

				Document session = sessionDAO.findSessionById(getSessionCookie(request));

				String clientname = session.getString("clientname");

				SimpleHash root = new SimpleHash();

				String username = sessionDAO.findUserNameBySessionId(getSessionCookie(request));
				root.put("username", username);
				root.put("clientname", clientname);
				List<String> roles = sessionDAO.findUserRoleBySessionId(getSessionCookie(request));
				root.put("roles", roles);

				template.process(root, writer);

			}
		});


	}

}
