package com.ibm.wcts;

import static com.mongodb.client.model.Filters.eq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import freemarker.template.Configuration;

public class FilterWorker extends PLMDAO {

	static Configuration cfg;
	static BlogPostDAO blogPostDAO;
	static UserDAO userDAO;
	static SessionDAO sessionDAO;
	static ABNDAO ABNDAO;
	static PLMDAO PLMDAO;
	static PEDAO PEDAO;
	static MongoDatabase clientDatabase;
	static String mongoURL = "mongodb://192.168.56.101";

	public FilterWorker(MongoDatabase abnDatabase) {

		super(abnDatabase);

	}

	public static void setupConnection() {

		MongoClient mongoClient = null;

		try {

			Util ut = new Util();

			mongoURL = ut.getPropValues("mongoURL");

			mongoClient = new MongoClient(new MongoClientURI(mongoURL));

			// setPort(8082); // update

			String clientDB = ut.getPropValues("clientDB");

			clientDatabase = mongoClient.getDatabase(clientDB);

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			// mongoClient.close();
		}

	}

	public static void main(String[] args) throws IOException {
	}

	class ScreeningTask extends Thread {

		String batchid;
		List<Document> recs;
		String profileid;
		String user;
		String listid;
		String resultid;

		public ScreeningTask(String resultid, String batchid, List<Document> recs, String profileid, String user,
				String listid) {
			this.batchid = batchid;
			this.recs = recs;
			this.profileid = profileid;
			this.user = user;
			this.listid = listid;
			this.resultid = resultid;
		}

		public void run() {

			// int resultId = 0;
			Date d1 = new Date();

			long id = this.getId();

			System.out.println("New ScreeningTask thread " + id + " launched at: " + new Date());

			// resultId = getResultId();

			Document batch = findBatchByID(Integer.valueOf(batchid).intValue());
			Document profile = findProfileByID(profileid);

			// String listid = profile.getString("listid");
			String fuzzyThreshold = profile.getString("fuzzy");

			String listName = getListNameByID(Integer.valueOf(listid).intValue());

			// get screening fields as array
			List<String> names = getFieldsByID(profileid);

			List<Document> f = (ArrayList<Document>) profile.get("fields");

			String threshold = profile.getString("threshold");

			List<HashMap<String, String>> screeningFields = new ArrayList<HashMap<String, String>>();

			for (int i = 0; i < f.size(); i++) {
				Document item = f.get(i);
				String fname = names.get(i);
				Document d = (Document) item.get(fname);
				String s = (String) d.get("Screening");
				String u = (String) d.get("Unique");
				String w = (String) d.get("Weight");
				String c = (String) d.get("Category");

				// System.out.println("field: "+fname+" s:"+s+" u:"+u+"
				// "+item.toJson());

				if (s != null) {

					HashMap<String, String> hm = new HashMap<String, String>();
					hm.put(fname, w);

					screeningFields.add(hm);

					// System.out.println("field: " + fname);
				}
			}

			// Document result = new Document("resultid",
			// resultId).append("batchid", batchid).append("date", new Date())
			// .append("profileid", profileid).append("screening",
			// screeningFields).append("user", user);
			//
			String clientname = findClientNameByUserID(user);
			//
			// if (clientname != null) {
			//
			// result.append("clientname", clientname);
			// }

			// System.out.println(batch.get("input"));

			// List<Document> recs =
			// findRecsByBatchID(Integer.valueOf(batchid).intValue());

			List<Document> rs = new ArrayList<Document>();

			for (int i = 0; i < recs.size(); i++) {
				// go through each input record

				// kick off screening process
				// try to screen name only

				Document rec = (Document) recs.get(i);
				String recid = String.valueOf(rec.get("_id")); // changed from
																// id to
																// _id to avoid
																// duplication

				// List<Document> r = findByFieldonCustomList(listName,
				// screeningFields, rec, threshold);
				List<Document> r = findByFieldonCustomListWithFuzzy(profileid, listName, screeningFields, rec,
						threshold, fuzzyThreshold);

				if (r != null && r.size() > 0) { // found something

					// System.out.println("found number of matches: "+r.size());

					// add to the match collection
					Document match = new Document("id", getMatchId()).append("recid", recid).append("batchid", batchid)
							.append("status", "open").append("entity", r).append("input", rec)
							.append("lastUpdate", new Date()).append("resultid", resultid);

					if (clientname != null) {

						match.append("clientname", clientname);
					}

					addMatch(match);

					// db.result.update({"resultid":1},{$push:{match:{matchid:6}}})

					Document update = new Document("$push",
							new Document("match", new Document("matchid", match.get("id"))));

					resultCollection.updateOne(eq("resultid", Integer.valueOf(resultid).intValue()), update);

					rs.add(new Document("matchid", match.get("id")));

				}

				if (id % 100 == 0)
					System.out.println("Number of records processed by this thread " + id + ":" + i);
			}

			// result.append("match", rs);
			//
			// resultCollection.insertOne(result);
			// resultCollection.insertOne(result);

			System.out.println("ScreeningTask thread " + id + " completed at: " + new Date());

			Date d2 = new Date();

			// in milliseconds
			long diff = d2.getTime() - d1.getTime();

			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			// long diffDays = diff / (24 * 60 * 60 * 1000);

			System.out.print("Total time spent thread " + id + " : " + diffHours + " hours " + diffMinutes + " minutes "
					+ diffSeconds + " seconds.");

			// notify();

		}
	}

	public int batchScreeningSingle(int resultId, String batchid, String profileid, String user, String listid) {

		List<Document> recs = findRecsByBatchID(Integer.valueOf(batchid).intValue());

		int threads = 2;

		// concurrency = 2

		Util ut = new Util();

		try {
			threads = Integer.valueOf(ut.getPropValues("concurrency")).intValue();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int chunk = 0;
		int total = recs.size();

		// force to use single thread if the total records number < 100

		if (total < 100) {
			threads = 1;
		}

		chunk = total / threads;

		System.out.println("threads: " + threads + " chunk: " + chunk + " total: " + total);

		chunk = total / threads;

		if (chunk < 1)
			chunk = total;

		int i = 0;

		for (i = 0; i <= total - chunk; i = i + chunk) {
			// go through each input record

			// kick off screening process
			// try to screen name only

			System.out.println("Process from " + i + " to " + (i + chunk));
			List<Document> subBatch = recs.subList(i, i + chunk);

			// run multi-thread
			ScreeningTask st = new ScreeningTask(String.valueOf(resultId), batchid, subBatch, profileid, user, listid);
			st.start();

			// synchronized(st){
			// try{
			// System.out.println("Waiting for st to complete...");
			// st.wait();
			// }catch(InterruptedException e){
			// e.printStackTrace();
			// }
			//
			//// System.out.println("Total time is: " + b.total);
			// }
		}

		// the remaining records

		if (i < total) {
			System.out.println("Process from " + i + " to " + total);
			List<Document> remainingBatch = recs.subList(i, total);
			new ScreeningTask(String.valueOf(resultId), batchid, remainingBatch, profileid, user, listid).start();
		}
		return resultId;

	}

	public List<Document> findByFieldonCustomListWithFuzzy(String profileid, String listname,
			List<HashMap<String, String>> screeningFields, Document rec, String threshold, String fuzzy) {

		// set hard limit of returned matches to 1000

		boolean is_public_list = false;
		int limit = 1000;
		int numMatch = 0;

		List<Document> plms = new ArrayList<Document>();
		Iterator<Document> it = null;

		Levenshtein l = new Levenshtein();

		// System.out.println(l.compare("jasonzhou", "jasonzhou"));

		double thres = 90; // 90 by default
		if (threshold != null) {

			thres = Double.valueOf(threshold).doubleValue();

		}

		 System.out.println("search list: " + listname);

		double totalScore = 0.0;
		double fuzzyThreshold = 0.9;

		// get the custom list via name
		MongoCollection<Document> alist = abnDatabase.getCollection(listname);

		// get total score
		for (HashMap<String, String> f : screeningFields) {

			String fn = f.keySet().iterator().next();
			String we = f.get(fn);
			double v = Double.valueOf(we).doubleValue();

			totalScore += v;

		}

		String countryField = "";
		String nameField = "";

		// System.out.println("total score: " + totalScore);

		String country = "UNKNOWN";
		String countryCode = "UNKNOWN";
		String name = "UNKNOWN";
		String name_trans = "UNKNOWN";
		String name_org = "UNKNOWN";

		List<String> altNames = new<String> ArrayList();

		String[] nameFields = null;

		for (HashMap<String, String> f : screeningFields) {

			String fn = f.keySet().iterator().next();

			String category = getFieldCategoryByName(profileid, fn);

			if (category.equals("Country")) {

				countryField = fn;

				country = rec.getString(fn);

				country = country.replaceAll("\"", "");

				System.out.println("search for country: " + country);

			}

			if (category.equals("CountryCode")) {

				countryField = fn;

				countryCode = rec.getString(fn);

				country = country.replaceAll("\"", "");

				// System.out.println("search for country code: " +
				// countryCode);

			}

			if (category.equals("Name")) {

				nameField = fn;

				name = rec.getString(fn);

				name_org = name;

				name = name.replaceAll("\"", "");
				if (name.indexOf(",") != -1) { // input format
												// lastname,firstname

					// no change is required

				} else {
					// get last name, suppost input format is firstname
					// lastname
					// or only one token

					// name.split("\b")

					nameFields = name.split("\\s+");

					if (nameFields.length > 1) {

						// get last one as surname

						String surname = nameFields[nameFields.length - 1];

						String firstname = nameFields[0];

						String othername = "";

						if (nameFields.length > 2) { // got other names

							for (int n = 1; n < nameFields.length - 1; n++) {

								othername += " " + nameFields[n];

							}

						}

						if (othername == "") {
							name = surname + ", " + firstname;
							name_trans = firstname + ", " + surname;
						} else {

							if (listname.contains("PEP")) {
								// ignore middle name
								name = surname + ", " + firstname;
								name_trans = firstname + ", " + surname;
							} else {
								name = surname + ", " + firstname + " " + othername;
								name_trans = firstname + ", " + surname + " " + othername;

							}
						}

						nameFields = new String[2];
						nameFields[0] = surname;
						nameFields[1] = firstname;
					}

				}

				 System.out.println("Full name: " + name);
				 System.out.println("Full transposition name: " + name_trans);
				 System.out.println("Full orig name: " + name_org);

				altNames.add(name);
				altNames.add(name_org);
				altNames.add(name_trans);

				// handle transposition

				// System.out.println("search for country: " + country);

			}

		}
		// take different action if it's PEP list due to its size
		is_public_list = true;
		if (listname.contains("PEP")) {

			// loop all records
			// default to AU entities
			if (country.equals("UNKNOWN") && countryCode.equals("UNKNOWN")) {
				it = alist.find().iterator();

			} else if (countryCode.equals("UNKNOWN")) { // if

				long count = alist.count(eq("addresses.address.countryName", country.toUpperCase()));

				System.out.println("search on number of country name entity: " + count);

				it = alist.find(eq("addresses.address.countryName", country.toUpperCase())).iterator();

			} else { // if use country code

				long count = alist.count(eq("addresses.address.country", country.toUpperCase()));

				System.out.println("search on number of country code entity: " + count);

				it = alist.find(eq("addresses.address.country", countryCode.toUpperCase())).iterator();

			}

		} else {
			// loop all records
			it = alist.find().iterator();

		}

		while (it.hasNext()) { // loop through entities
			Document d = (Document) it.next();

			// search on all screening field here
			double score = 0.0;

			Set<String> matchFields = new HashSet<String>();

			for (HashMap<String, String> field : screeningFields) {
				// System.out.println(field);

				String fieldname = field.keySet().iterator().next();
				String weight = field.get(fieldname);

				// System.out.println("fieldname: " + fieldname + " weight: " +
				// weight);

				double w1 = Double.valueOf(weight).doubleValue();

				if (fieldname.equals(countryField)) {
					// skip compare

					score += w1 * 1 / totalScore; // increase
					// the score
					matchFields.add(fieldname);

					continue;
				}

				String searchString = rec.getString(fieldname);

				// System.out.println("search string: "+searchString);

				if (fieldname.equals(nameField)) {

					double namescore = 0.0;

					String entityField = "";

					if (is_public_list) {
						entityField = d.getString("name");
					} else { // use the name field in private list
						entityField = d.getString(fieldname);
					}

					if(entityField!=null)
						entityField=entityField.toUpperCase();
					
//					System.out.println("entity field:"+entityField);

					// try fuzzy
					for (String n : altNames) {

						searchString = n; // compare name

						if (searchString != null) { // search string can't be
													// null

							if (searchString.trim().length() >= 1) { // handle
																		// spaces
								// System.out.println("list name: "+listname);

								// perform fuzzy search here

								double fuzzyScore = 0.0;

								if (entityField == null) {

									fuzzyScore = 0.0;

								} else {
									if (entityField.trim().length() == 0) {
										fuzzyScore = 0.0;

									} else {
										fuzzyScore = l.compare(searchString.toUpperCase(), entityField.trim());
									}
								}

								// System.out.println(
								// "search: " + searchString + " entity: " +
								// entityField
								// + " fuzzyScore: " + fuzzyScore);

								if (fuzzy != null) {
									// default to 0.8
									fuzzyThreshold = Double.valueOf(fuzzy).doubleValue();
								}

								if (fuzzyScore >= fuzzyThreshold) {

									// check sore of each alt name

									if (fuzzyScore > namescore) {
										namescore = fuzzyScore;
									}

								}

							} // end of if (searchString.trim().length() >= 1)
						} // end of if (searchString.length() > 1)

					} // end of for loop

					// if (namescore > 0)
					// System.out.println("name score:" + namescore);

					double tokenScore = 0;
					// try token
					if (nameFields != null) {

						// System.out.println(nameFields[0]+" "+nameFields[1]);

						for (String token : nameFields) {

							token = token.toUpperCase();

							if (entityField.equals(token)) {
								tokenScore += 0.5; // if both token matches,
													// then
													// 100 score!
								// System.out.println("entity
								// field:"+entityField+" token: "+token+" total
								// tokenScore:"+tokenScore);

								if (tokenScore > 1)
									tokenScore = 1;
							}
						}

					}

					if (tokenScore > namescore) {
						// score += w1/totalScore * fuzzyScore; //
						// increase
						// the score

						// 8 * 0.5 / 10
						score += (w1 * tokenScore) / totalScore; // increase
																	// the
																	// score

						// System.out.println("token score: " + tokenScore + "
						// namescore: " + namescore+ " use token score
						// instead!");
					} else {
						// score += w1/totalScore * fuzzyScore; //
						// increase
						// the score
						score += (w1 * namescore) / totalScore; // increase
																// the
																// score

					}

					matchFields.add(fieldname);

				} // end of if name field

			} // end of for (HashMap<String, String> field : screeningFields)

			// add search detail

			// add search detail round(200.3456, 2);
			double overall = round(score * 100, 2);

			// String oscore = String.valueOf(overall) + "%";

			// System.out.println("threshold: " + thres + " match score: " +
			// overall + " match field: " + matchFields);

			// filter out the weak matches via threshold
			if (overall >= thres) {
				d.append("score", overall).append("match fields", matchFields.toString()).append("fuzzy",
						fuzzyThreshold);

				System.out.println("threshold: " + thres + " match score: " + overall + " match field: " + matchFields);

				plms.add(d);
				numMatch++;

				if (numMatch > limit)
					break; // enforce hard limit, break the while loop
			}

		} // end of while (it.hasNext())

		// System.out.println("match size: "+plms.size());

		// remove duplication
		Set<Document> s = new LinkedHashSet<Document>(plms);

		List<Document> result = new ArrayList<Document>();
		result.addAll(s);

		return result;

	}

	public List<Document> InteractiveScreeningWS(String profileid, Document searchDoc) {

		Document profile = findProfileByID(profileid);

		List<Document> matches = new ArrayList<Document>();

		List<String> lists = (List<String>) profile.get("mlist");

		if (lists != null) {
			for (String listid : lists) {

				String fuzzyThreshold = profile.getString("fuzzy");

				System.out.println("listid: " + listid);

				String listName = getListNameByID(Integer.valueOf(listid).intValue());

				// get screening fields as array
				List<String> names = getFieldsByID(profileid);

				List<Document> f = (ArrayList<Document>) profile.get("fields");

				String threshold = profile.getString("threshold");

				List<HashMap<String, String>> screeningFields = new ArrayList<HashMap<String, String>>();

				for (int i = 0; i < f.size(); i++) {
					Document item = f.get(i);
					String fname = names.get(i);
					Document d = (Document) item.get(fname);
					String s = (String) d.get("Screening");
					String u = (String) d.get("Unique");
					String w = (String) d.get("Weight");

					// System.out.println("field: "+fname+" s:"+s+" u:"+u+"
					// "+item.toJson());

					if (s != null) {

						HashMap<String, String> hm = new HashMap<String, String>();
						hm.put(fname, w);

						screeningFields.add(hm);

						System.out.println("field: " + fname);
					}
				}

				String batchid = String.valueOf(getBatchId());
				String resultid = String.valueOf(getResultId());

				Document result = new Document("resultid", resultid).append("batchid", batchid)
						.append("date", new Date()).append("profileid", profileid).append("screening", screeningFields)
						.append("user", "API");

				// System.out.println(batch.get("input"));

				List<Document> rs = new ArrayList<Document>();

				String recid = String.valueOf(searchDoc.get("_id")); // changed
																		// from
																		// id
																		// to

				List<Document> r = findByFieldonCustomListWithFuzzy(profileid, listName, screeningFields, searchDoc,
						threshold, fuzzyThreshold);

				if (r != null && r.size() > 0) { // found something

					// System.out.println("found number of matches: "+r.size());

					// add to the match collection
					Document match = new Document("id", getMatchId()).append("recid", 1).append("batchid", batchid)
							.append("status", "open").append("entity", r).append("input", searchDoc)
							.append("resultid", resultid).append("lastUpdate", new Date());
					;

					matches.add(match);
					addMatch(match);

					rs.add(new Document("matchid", match.get("id")));

				}

				result.append("match", rs);

				resultCollection.insertOne(result);

			}
		}

		return matches;

	}

	public int InteractiveScreening(String profileid, String batchid, Document searchDoc, String user) {

		int resultId = 0;

		Document profile = findProfileByID(profileid);

		resultId = getResultId();

		// get screening fields as array
		List<String> names = getFieldsByID(profileid);

		List<Document> f = (ArrayList<Document>) profile.get("fields");

		List<String> lists = (List<String>) profile.get("mlist");

		if (lists != null) {
			for (String listid : lists) {

				System.out.println("search list:" + listid);

				resultId = batchScreeningSingle(resultId, batchid, profileid, user, listid);
			}
		}

		List<HashMap<String, String>> screeningFields = new ArrayList<HashMap<String, String>>();

		for (int i = 0; i < f.size(); i++) {
			Document item = f.get(i);
			String fname = names.get(i);
			Document d = (Document) item.get(fname);
			String s = (String) d.get("Screening");
			String u = (String) d.get("Unique");
			String w = (String) d.get("Weight");
			String c = (String) d.get("Category");

			// System.out.println("field: "+fname+" s:"+s+" u:"+u+"
			// "+item.toJson());

			if (s != null) {

				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put(fname, w);

				screeningFields.add(hm);

				// System.out.println("field: " + fname);
			}
		}

		Document result = new Document("resultid", resultId).append("batchid", batchid).append("date", new Date())
				.append("profileid", profileid).append("screening", screeningFields).append("user", user);

		String clientname = findClientNameByUserID(user);

		if (clientname != null) {

			result.append("clientname", clientname);
		}

		// get total match count for result

		// int matchNum=
		// (int)PLMDAO.getMatchCountByResultID(String.valueOf(resultId));
		// result.append("matchCount", matchNum);

		resultCollection.insertOne(result);

		return resultId;

	}

	public int batchScreening(String batchid, String profileid, String user) {
		int resultId = 0;

		resultId = getResultId();

		Document profile = findProfileByID(profileid);

		// get screening fields as array
		List<String> names = getFieldsByID(profileid);

		List<Document> f = (ArrayList<Document>) profile.get("fields");

		List<String> lists = (List<String>) profile.get("mlist");

		if (lists != null) {
			for (String listid : lists) {

				batchScreeningSingle(resultId, batchid, profileid, user, listid);
			}
		}

		List<HashMap<String, String>> screeningFields = new ArrayList<HashMap<String, String>>();

		for (int i = 0; i < f.size(); i++) {
			Document item = f.get(i);
			String fname = names.get(i);
			Document d = (Document) item.get(fname);
			String s = (String) d.get("Screening");
			String u = (String) d.get("Unique");
			String w = (String) d.get("Weight");
			String c = (String) d.get("Category");

			// System.out.println("field: "+fname+" s:"+s+" u:"+u+"
			// "+item.toJson());

			if (s != null) {

				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put(fname, w);

				screeningFields.add(hm);

				// System.out.println("field: " + fname);
			}
		}

		Document result = new Document("resultid", resultId).append("batchid", batchid).append("date", new Date())
				.append("profileid", profileid).append("screening", screeningFields).append("user", user)
				.append("status", "processing");

		String clientname = findClientNameByUserID(user);

		if (clientname != null) {

			result.append("clientname", clientname);
		}

		// get total match count for result

		// int matchNum=
		// (int)PLMDAO.getMatchCountByResultID(String.valueOf(resultId));
		// result.append("matchCount", matchNum);

		resultCollection.insertOne(result);

		return resultId;

	}

}
