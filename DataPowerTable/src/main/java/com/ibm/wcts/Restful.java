/**
 *  This is the class to expose the web services API
 */

package com.ibm.wcts;

import static spark.Spark.get;
import static spark.Spark.post;

import java.io.IOException;
import java.net.URLEncoder;

import org.bson.Document;
import org.jsoup.Jsoup;

public class Restful extends BlogController {

	public Restful() {

		try {
			initializeRoutes();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static String html2text(String html) {
	    return Jsoup.parse(html).text();
	}
	

	void initializeRoutes() throws IOException {

		// GET http://localhost:8082/batchbyid/1005
		get("/search/:name", "application/json", (request, response) -> {
			String name = request.params(":name");
			System.out.println("name: " + name);
			response.raw().setContentType("application/json");

			return PLMDAO.findMemo(name);

		}, new JsonTransformer());
		
		get("/story/:name", "application/json", (request, response) -> {
			String name = request.params(":name");
			System.out.println("name: " + name);
			response.raw().setContentType("application/json");

			return PLMDAO.findStoryByName(name);

		}, new JsonTransformer());
		
		
		get("/storylist", "application/json", (request, response) -> {
//			String name = request.params(":name");
//			System.out.println("name: " + name);
			response.raw().setContentType("application/json");

			return PLMDAO.findStoryList();

		}, new JsonTransformer());

		get("/wiki2/:keyword", "application/json", (request, response) -> {
//			String name = request.params(":name");
//			System.out.println("name: " + name);
			response.raw().setContentType("application/json");

			String keyword = request.params(":keyword");
			System.out.println("keyword: " + keyword);
			
			return PLMDAO.findWiki(keyword);

		}, new JsonTransformer());
		
		get("/wiki/:keyword", "application/json", (request, response) -> {
//			String name = request.params(":name");
//			System.out.println("name: " + name);
			response.raw().setContentType("application/json");

			String keyword = request.params(":keyword");
			
			String encoded = URLEncoder.encode(keyword,"UTF-8");
					
			System.out.println("keyword: " + encoded);
			
			
			
			return PLMDAO.searchWiki(encoded);

		}, new JsonTransformer());
		
		get("/save/:keyword", "application/json", (request, response) -> {
//			String name = request.params(":name");
//			System.out.println("name: " + name);
			response.raw().setContentType("application/json");

			String keyword = request.params(":keyword");
			
			String encoded = URLEncoder.encode(keyword,"UTF-8");
					
			System.out.println("keyword: " + encoded);
			
			
			
			return PLMDAO.saveMemo(keyword);
			
			

		}, new JsonTransformer());
		
		
		get("/batchlistjs", "application/json", (request, response) -> {

			response.raw().setContentType("application/json");

			return PLMDAO.findBatchList(100);
		}, new JsonTransformer());

		// GET http://localhost:8082/batchbyid/1005
		get("/batchbyid/:id", "application/json", (request, response) -> {
			String id = request.params(":id");
			System.out.println("id: " + id);
			response.raw().setContentType("application/json");

			return PLMDAO.findBatchByID(Integer.valueOf(id).intValue());
		}, new JsonTransformer());

		// http://localhost:8082/matchbyid/1038
		get("/matchbyid/:id", "application/json", (request, response) -> {
			String id = request.params(":id");
			System.out.println("id: " + id);

			response.type("application/json");

			// response.raw().setContentType("application/json");
			// response.type("text/xml");

			return PLMDAO.findMatchByID(Integer.valueOf(id).intValue());
		}, new JsonTransformer());

		/*
		 * use Rest Web Service Client for Chrome
		 * 
		 * http://localhost:8082/lookupjs
		 * 
		 * body:
		 * 
		 * { "profileid":"4d381fb2-bb72-447f-b5ae-9b024f02e3d8", "search": {
		 * "full_name":"Therron Jardiing", "email":"tjarding@hotmail.com" } }
		 * 
		 * 
		 */
		post("/lookupjs", "application/json", (request, response) -> {

			Document doc = Document.parse(request.body());

			String id = doc.getString("profileid");
			Document search = (Document) doc.get("search");

			System.out.println("profileid: " + id);
			System.out.println("body: " + doc.toJson());

			response.raw().setContentType("application/json");

			FilterWorker worker = new FilterWorker(clientDatabase);

			return worker.InteractiveScreeningWS(id, search);

		}, new JsonTransformer());

	}

}
