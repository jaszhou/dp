package com.ibm.wcts;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Crunchify.com
 * 
 */

public class RestClient {
	public static void main(String[] args) {
		String answer = "";
		try {

			// Step1: Let's 1st read file from fileSystem
			// Change CrunchifyJSON.txt path here
			// InputStream crunchifyInputStream = new
			// FileInputStream("/Users/<username>/Documents/CrunchifyJSON.txt");
			// InputStreamReader crunchifyReader = new
			// InputStreamReader(crunchifyInputStream);
			// BufferedReader br = new BufferedReader(crunchifyReader);
			// String line;
			// while ((line = br.readLine()) != null) {
			// string += line + "\n";
			// }
			//
			// JSONObject jsonObject = new JSONObject(string);
			// System.out.println(jsonObject);

			String wikiURL = "https://simple.wikipedia.org/w/api.php?action=query&format=json&prop=extracts&titles=Sydney&exintro=1";

			// Step2: Now pass JSON File Data to REST Service
			try {
				URL url = new URL(wikiURL);
				URLConnection connection = url.openConnection();
				// connection.setDoOutput(true);
				// connection.setRequestProperty("Content-Type",
				// "application/json");
				// connection.setConnectTimeout(5000);
				// connection.setReadTimeout(5000);
				// OutputStreamWriter out = new
				// OutputStreamWriter(connection.getOutputStream());
				//// out.write(jsonObject.toString());
				// out.close();

				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

				String content = "";

				while ((content = in.readLine()) != null) {

					// System.out.println(content);

					JSONObject obj = new JSONObject(content);
					JSONObject pages = obj.getJSONObject("query").getJSONObject("pages");

					// System.out.println(pages.toString());

					// JSONObject object = new JSONObject(myJSONString);
					String[] keys = JSONObject.getNames(pages);

					for (String key : keys) {
						JSONObject value = (JSONObject) pages.get(key);

						// System.out.println("key: "+ value);

						String extract = value.getString("extract");

						System.out.println(extract);

						// Determine type of value and do something with it...
					}

					// JSONArray arr = obj.getJSONArray("posts");
					// for (int i = 0; i < arr.length(); i++)
					// {
					// String post_id =
					// arr.getJSONObject(i).getString("post_id");
					// ......
					// }

				}
				System.out.println("\nCrunchify REST Service Invoked Successfully..");
				in.close();
			} catch (Exception e) {
				System.out.println("\nError while calling Crunchify REST Service");
				System.out.println(e);
			}

			// br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String searchAnswer(String keyword) {

		String answer = "";
		try {

			// String wikiURL =
			// "https://simple.wikipedia.org/w/api.php?action=query&format=json&prop=extracts&titles="+keyword+"&exintro=1";
			String wikiURL = "https://en.wikipedia.org/w/api.php?action=opensearch&search="+keyword+"&limit=1&namespace=0&format=json&prop=extracts";

			// Step2: Now pass JSON File Data to REST Service
			try {
				URL url = new URL(wikiURL);
				URLConnection connection = url.openConnection();
				// connection.setDoOutput(true);
				// connection.setRequestProperty("Content-Type",
				// "application/json");
				// connection.setConnectTimeout(5000);
				// connection.setReadTimeout(5000);
				// OutputStreamWriter out = new
				// OutputStreamWriter(connection.getOutputStream());
				//// out.write(jsonObject.toString());
				// out.close();

				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

				String content = "";

				while ((content = in.readLine()) != null) {

//					System.out.println(content);

					JSONArray jsonArray = new JSONArray(content);

					for (int i = 0; i < jsonArray.length(); i++) {

//						System.out.println(jsonArray.get(i));
						
						if(i==2){
							JSONArray answerArray = new JSONArray(jsonArray.get(i).toString());
							
							answer = answerArray.get(0).toString();
							System.out.println(answerArray.get(0).toString());
		
						}
					}

				}
				System.out.println("\nCrunchify REST Service Invoked Successfully..");
				in.close();
			} catch (Exception e) {
				System.out.println("\nError while calling Crunchify REST Service");
				System.out.println(e);
			}

			// br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return answer;

	}

	public static String getAnswer(String keyword) {

		String answer = "";
		try {

			
			String encoded = URLEncoder.encode(keyword,"UTF-8");
	

			// String wikiURL =
			// "https://simple.wikipedia.org/w/api.php?action=query&format=json&prop=extracts&titles="+keyword+"&exintro=1";
			String wikiURL = "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=extracts&titles="
					+ encoded + "&exintro=1";

			// Step2: Now pass JSON File Data to REST Service
			try {
				URL url = new URL(wikiURL);
				URLConnection connection = url.openConnection();
				// connection.setDoOutput(true);
				// connection.setRequestProperty("Content-Type",
				// "application/json");
				// connection.setConnectTimeout(5000);
				// connection.setReadTimeout(5000);
				// OutputStreamWriter out = new
				// OutputStreamWriter(connection.getOutputStream());
				//// out.write(jsonObject.toString());
				// out.close();

				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

				String content = "";

				while ((content = in.readLine()) != null) {

					// System.out.println(content);

					JSONObject obj = new JSONObject(content);
					JSONObject pages = obj.getJSONObject("query").getJSONObject("pages");

					// System.out.println(pages.toString());

					// JSONObject object = new JSONObject(myJSONString);
					String[] keys = JSONObject.getNames(pages);

					for (String key : keys) {
						JSONObject value = (JSONObject) pages.get(key);

						// System.out.println("key: "+ value);

						String extract = value.getString("extract");

						System.out.println(extract);

						answer = extract;

						// Determine type of value and do something with it...
					}

					// JSONArray arr = obj.getJSONArray("posts");
					// for (int i = 0; i < arr.length(); i++)
					// {
					// String post_id =
					// arr.getJSONObject(i).getString("post_id");
					// ......
					// }

				}
				System.out.println("\nCrunchify REST Service Invoked Successfully..");
				in.close();
			} catch (Exception e) {
				System.out.println("\nError while calling Crunchify REST Service");
				System.out.println(e);
			}

			// br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return answer;
	}
}
