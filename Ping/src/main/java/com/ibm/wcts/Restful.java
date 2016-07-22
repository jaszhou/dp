/**
 *  This is the class to expose the web services API
 */

package com.ibm.wcts;

import static spark.Spark.get;
import static spark.Spark.post;

import java.io.IOException;

import org.bson.Document;

public class Restful extends BlogController {

	public Restful() {

		try {
			initializeRoutes();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	void initializeRoutes() throws IOException {

	
		/*
		 * use Rest Web Service Client for Chrome
		 * 
		 * http://localhost:8082/lookupjs 
		 * 
		 * body:
		 * 
		 * {
			  "profileid":"4d381fb2-bb72-447f-b5ae-9b024f02e3d8",
			  "search":
			  {
			     "full_name":"Therron Jardiing",
			     "email":"tjarding@hotmail.com"
			  } 
			}
		 * 
		 * 
		 */
	

	}

}
