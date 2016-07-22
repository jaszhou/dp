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

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.and;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class PLMDAO {
	MongoCollection<Document> plmCollection;
	MongoCollection<Document> batchCollection;
	MongoCollection<Document> resultCollection;
	MongoCollection<Document> matchCollection;
	MongoCollection<Document> profileCollection;
	MongoCollection<Document> ruleCollection;
	MongoCollection<Document> actionCollection;
	MongoCollection<Document> listCollection;
	MongoCollection<Document> userCollection;
	MongoCollection<Document> metaCollection;
	MongoCollection<Document> categoryCollection;
	MongoCollection<Document> clientCollection;
	MongoCollection<Document> auditCollection;

	// private final MongoCollection<Document> batchRecordCollection;
	public static MongoDatabase abnDatabase;

	// test commit

	// make some more test here

	// new update

	// another test

	// new

	// test test

	public PLMDAO(MongoDatabase abnDatabase) {
		plmCollection = abnDatabase.getCollection("plm");
		batchCollection = abnDatabase.getCollection("batch");
		resultCollection = abnDatabase.getCollection("result");
		matchCollection = abnDatabase.getCollection("match");
		profileCollection = abnDatabase.getCollection("profile");
		ruleCollection = abnDatabase.getCollection("rule");
		actionCollection = abnDatabase.getCollection("action");
		listCollection = abnDatabase.getCollection("list");
		userCollection = abnDatabase.getCollection("users");
		metaCollection = abnDatabase.getCollection("meta");
		clientCollection = abnDatabase.getCollection("client");
		auditCollection = abnDatabase.getCollection("audit");

		this.abnDatabase = abnDatabase;

		this.init();
	}

	// set up inital values including the category etc
	public void init() {
		categoryCollection = abnDatabase.getCollection("category");

		if (categoryCollection.count() == 0) {
			categoryCollection.insertOne(new Document("category", "Name"));
			categoryCollection.insertOne(new Document("category", "Country"));
			categoryCollection.insertOne(new Document("category", "CountryCode"));
			categoryCollection.insertOne(new Document("category", "DOB"));
			categoryCollection.insertOne(new Document("category", "Default"));
		}
	}

	public int getBatchId() {

		// get count from the batch collection
		// use high water mark to avoid duplication
		int hwm = 0;

		Document meta = metaCollection.find().first();

		if (meta == null) { // if empty batch, then initialize

			Document metaDoc = new Document();

			// add batch id high water mark
			metaDoc.append("batchhwm", 1000);

			metaCollection.insertOne(metaDoc);

			hwm = 1000;

		} else { // if meta collection does not exist

			Integer m = meta.getInteger("batchhwm");
			if (m != null) { // check if batchhwm has been set
				hwm = m.intValue();

				Document update = new Document("$inc", new Document("batchhwm", 1));
				metaCollection.findOneAndUpdate(eq("_id", meta.get("_id")), update);

			} else {

				// increase high water mark

				hwm = 1000;
				Document init = new Document("$set", new Document("batchhwm", 1000));
				metaCollection.findOneAndUpdate(eq("_id", meta.get("_id")), init);
			}

		}

		return hwm;

	}

	public int getListId() {

		// get count from the batch collection

		return (int) listCollection.count() + 1;

	}

	public int getClientId() {

		// get count from the batch collection

		return (int) clientCollection.count() + 1;

	}

	public int getResultId() {

		// get count from the batch collection

		// return (int) resultCollection.count() + 1;

		Document d = resultCollection.find().sort(new Document("resultid", -1)).limit(1).first();

		if (d != null)
			return (Integer) d.getInteger("resultid").intValue()+1;
		else
			return 1;

	}

	public int getMatchId() {

		// get count from the batch collection
		// use high water mark to avoid duplication
		int hwm = 0;

		Document meta = metaCollection.find().first();

		if (meta == null) { // if empty batch, then initialize

			Document metaDoc = new Document();

			// add batch id high water mark
			metaDoc.append("matchhwm", 1000);

			metaCollection.insertOne(metaDoc);

			hwm = 1000;

		} else {

			Integer m = meta.getInteger("matchhwm");
			if (m != null) {
				hwm = m.intValue();

				// increase high water mark

				Document update = new Document("$inc", new Document("matchhwm", 1));
				metaCollection.findOneAndUpdate(eq("_id", meta.get("_id")), update);

			} else {
				hwm = 1000;
				Document init = new Document("$set", new Document("matchhwm", 1000));
				metaCollection.findOneAndUpdate(eq("_id", meta.get("_id")), init);

			}

		}

		return hwm;

	}

	public int getProfileId() {

		// get count from the batch collection

		return (int) profileCollection.count() + 1;

	}

	public void addBatch(Document batch) {
		try {
			batchCollection.insertOne(batch);

		} catch (MongoWriteException e) {
			e.printStackTrace();
		}
	}

	public void addClient(Document client) {
		try {
			clientCollection.insertOne(client);

		} catch (MongoWriteException e) {
			e.printStackTrace();
		}
	}

	public void addList(Document list) {
		try {
			listCollection.insertOne(list);

		} catch (MongoWriteException e) {
			e.printStackTrace();
		}
	}

	public void addAction(Document action) {
		try {
			actionCollection.insertOne(action);

		} catch (MongoWriteException e) {
			e.printStackTrace();
		}
	}

	public void addMatch(Document match) {
		try {
			matchCollection.insertOne(match);

		} catch (MongoWriteException e) {
			e.printStackTrace();
		}
	}

	public void addProfile(Document profile) {
		try {
			profileCollection.insertOne(profile);

		} catch (MongoWriteException e) {
			e.printStackTrace();
		}
	}

	public void addRule(Document rule) {
		try {
			ruleCollection.insertOne(rule);

		} catch (MongoWriteException e) {
			e.printStackTrace();
		}
	}

	public void updateRule(Document p1, Document p2) {

		try {
			ruleCollection.findOneAndUpdate(p1, p2);

		} catch (MongoWriteException e) {
			e.printStackTrace();
		}

	}

	public int addBatchByFileName(String fileName) {
		int batchId = getBatchId();

		Document batch = new Document().append("batchid", batchId).append("filename", fileName)
				.append("input", new Document()).append("profile", new Document());

		try {
			batchCollection.insertOne(batch);

		} catch (MongoWriteException e) {
			e.printStackTrace();
		}

		return batchId;

	}

	/**
	 * This function do generate string match for all screening fields
	 * 
	 * @param name
	 * @return
	 */
	public List<Document> findByField(String field, String name) {

		List<Document> plms = new ArrayList<Document>();
		Iterator<Document> it = null;

		if (name != null) {

			if (name.length() > 1) {
				BasicDBObject regexQuery = new BasicDBObject();
				regexQuery.put(field, new BasicDBObject("$regex", name).append("$options", "i"));

				System.out.println(regexQuery.toString());

				it = plmCollection.find(regexQuery).iterator();

			}
		}

		while (it.hasNext()) {
			Document d = (Document) it.next();

			plms.add(d);
		}

		// System.out.println(abns);

		return plms;

	}

	public List<Document> findByFieldonCustomList(String listname, String field, String weight, String name) {

		List<Document> plms = new ArrayList<Document>();
		Iterator<Document> it = null;

		// get the custom list via name
		MongoCollection<Document> alist = abnDatabase.getCollection(listname);

		if (name != null) {

			if (name.length() > 1) {
				BasicDBObject regexQuery = new BasicDBObject();
				regexQuery.put(field, new BasicDBObject("$regex", name).append("$options", "i"));

				// System.out.println(regexQuery.toString());

				it = alist.find(regexQuery).iterator();

			}
		}

		while (it.hasNext()) {
			Document d = (Document) it.next();

			// add search detail
			d.append("searchfield", field).append("weight", weight);

			plms.add(d);
		}

		// System.out.println("match size: "+plms.size());

		return plms;

	}

	public List<Document> findByFieldonCustomList(String listname, List<HashMap<String, String>> screeningFields,
			Document rec, String threshold) {

		List<Document> plms = new ArrayList<Document>();
		Iterator<Document> it = null;

		double thres = 50; // 50 by default
		if (threshold != null) {

			thres = Double.valueOf(threshold).doubleValue();

		}

		System.out.println("threshold: " + thres);

		double totalScore = 0.0;

		// get the custom list via name
		MongoCollection<Document> alist = abnDatabase.getCollection(listname);

		// get total score
		for (HashMap<String, String> f : screeningFields) {

			String fn = f.keySet().iterator().next();
			String we = f.get(fn);
			double v = Double.valueOf(we).doubleValue();

			totalScore += v;

		}

		System.out.println("total score: " + totalScore);

		for (HashMap<String, String> field : screeningFields) {
			System.out.println(field);

			String fieldname = field.keySet().iterator().next();
			String weight = field.get(fieldname);

			System.out.println("fieldname: " + fieldname + " weight: " + weight);

			String searchString = rec.getString(fieldname);

			if (searchString != null) {

				if (searchString.length() > 1) {
					BasicDBObject regexQuery = new BasicDBObject();
					regexQuery.put(fieldname, new BasicDBObject("$regex", searchString).append("$options", "i"));

					// System.out.println(regexQuery.toString());

					it = alist.find(regexQuery).iterator();

					// it = alist.find().iterator();

				}

				while (it.hasNext()) {
					Document d = (Document) it.next();

					// search on all screening field here
					double score = 0.0;

					double dW = Double.valueOf(weight).doubleValue();

					score += dW;

					Set<String> matchFields = new HashSet<String>();

					// add match fields
					matchFields.add(fieldname);

					for (HashMap<String, String> otherField : screeningFields) {

						String fname = otherField.keySet().iterator().next();

						if (fname.equals(fieldname))
							continue; // skip the duplicate field

						String w = otherField.get(fname);
						double w1 = Double.valueOf(w).doubleValue();

						// System.out.println("search on other field: " +
						// otherField);

						// System.out.println("fieldname: " + fname + " weight:
						// " + w);

						// get enity field value

						// System.out.println(" search enityField: "+d);

						String entityField = d.getString(fname);
						// if
						// (entityField.matches("^(\\d{3}-?\\d{2}-?\\d{4})$")) {

						// System.out.println(" search rec: "+rec);
						// System.out.println(" search entity: "+entityField);

						searchString = rec.getString(fname);

						// System.out.println(" search: "+searchString);

						if (entityField.matches(searchString)) {
							// System.out.println("Found : " + entityField + "
							// search: " + searchString);

							score += w1; // increase the score

							// add match field

							matchFields.add(fname);
						}

					}

					// add search detail round(200.3456, 2);
					double overall = round(score / totalScore * 100, 2);

					String oscore = String.valueOf(overall) + "%";

					// System.out.println("match score: " + overall);

					// System.out.println("match field: " + matchFields);

					// filter out the weak matches via threshold
					if (overall >= thres) {
						d.append("score", overall).append("match fields", matchFields.toString());

						plms.add(d);
					}

				}

			}

		}

		// System.out.println("match size: "+plms.size());

		// remove duplication
		// Set<Document> s = new LinkedHashSet<>(plms);
		Set<Document> s = new LinkedHashSet<Document>(plms);

		List<Document> result = new ArrayList<Document>();
		result.addAll(s);

		return result;

	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);

		// System.out.println("round to: " + bd.doubleValue());

		return bd.doubleValue();
	}

	public String getFieldCategoryByName(String profileid, String fieldname) {

		String category = "Default";

		Document profile = profileCollection.find(eq("id", profileid)).first();

		// get screening fields as array
		List<String> names = getFieldsByID(profileid);

		List<Document> f = (ArrayList<Document>) profile.get("fields");

		for (int i = 0; i < f.size(); i++) {
			Document item = f.get(i);

			String fname = names.get(i);

			Document d = (Document) item.get(fname);
			String s = (String) d.get("Screening");
			String u = (String) d.get("Unique");
			String w = (String) d.get("Weight");
			String c = (String) d.get("Category");

			if (fname.equals(fieldname)) {

				// System.out.println("field: " + fname + " s:" + s + " u:" + u
				// + " c:" + c + " " + item.toJson());

				category = c;
			}

		}

		return category;

	}

	// public List<Document> findByFieldonCustomListWithFuzzy(String profileid,
	// String listname,
	// List<HashMap<String, String>> screeningFields, Document rec, String
	// threshold, String fuzzy) {
	// return null;
	// }

	public List<Document> findByName(String name) {

		List<Document> plms = new ArrayList<Document>();
		Iterator<Document> it = null;

		if (name != null) {

			// db.plm.find({"NAME":"Aeleen Oraham"}).pretty()

			if (name.length() > 1) {
				BasicDBObject regexQuery = new BasicDBObject();
				regexQuery.put("NAME", new BasicDBObject("$regex", name).append("$options", "i"));

				// System.out.println(regexQuery.toString());

				it = plmCollection.find(regexQuery).iterator();

				// it =
				// abnCollection.f("ABR.MainEntity.NonIndividualName.NonIndividualNameText",
				// busName)).iterator();
			}
		}

		while (it.hasNext()) {
			Document d = (Document) it.next();

			plms.add(d);
		}

		// System.out.println(abns);

		return plms;
	}

	public List<Document> findBatchList(int limit) {

		// posts = postsCollection.find().sort(new
		// Document("date",-1)).limit(limit).into(new ArrayList<Document>());

		return batchCollection.find().sort(new Document("date", -1)).limit(limit).into(new ArrayList<Document>());

	}

	public List<Document> findBatchList(String clientname, int limit) {

		// posts = postsCollection.find().sort(new
		// Document("date",-1)).limit(limit).into(new ArrayList<Document>());

		return batchCollection.find(eq("clientname", clientname)).sort(new Document("date", -1)).limit(limit)
				.into(new ArrayList<Document>());

	}

	public void deleteBatch(int batchid) {

		// Document batch = batchCollection.find(eq("batchid",batchid)).first();

		// construct batch collection name
		String name = "batch" + batchid;

		// drop collection
		abnDatabase.getCollection(name).drop();

		System.out.println("delete batch: " + batchid);

		batchCollection.deleteOne(eq("batchid", batchid));

		// for data integrity, the result and match dataset also needs to be
		// deleted

		String batch = String.valueOf(batchid);

		resultCollection.deleteMany(eq("batchid", batch));

		matchCollection.deleteMany(eq("batchid", batch));

		profileCollection.deleteMany(eq("batchid", batch));

	}

	public void deleteResult(int resultid) {

		// Document batch = batchCollection.find(eq("batchid",batchid)).first();

		System.out.println("delete result: " + resultid);

		resultCollection.deleteOne(eq("resultid", resultid));

		// also remove the corresponding matches

		matchCollection.deleteMany(eq("resultid", String.valueOf(resultid)));

	}

	public List<Document> findProfileList(int limit) {

		// posts = postsCollection.find().sort(new
		// Document("date",-1)).limit(limit).into(new ArrayList<Document>());

		return profileCollection.find().sort(new Document("date", -1)).limit(limit).into(new ArrayList<Document>());

	}

	public List<Document> findProfileList(String clientname, int limit) {

		// posts = postsCollection.find().sort(new
		// Document("date",-1)).limit(limit).into(new ArrayList<Document>());

		return profileCollection.find(eq("clientname", clientname)).sort(new Document("date", -1)).limit(limit)
				.into(new ArrayList<Document>());

	}

	public List<Document> findRuleList(String clientname, int limit) {

		// posts = postsCollection.find().sort(new
		// Document("date",-1)).limit(limit).into(new ArrayList<Document>());

		return ruleCollection.find(eq("clientname", clientname)).sort(new Document("date", -1)).limit(limit)
				.into(new ArrayList<Document>());

	}

	public List<Document> findProfileListByBatch(String batchid) {

		// posts = postsCollection.find().sort(new
		// Document("date",-1)).limit(limit).into(new ArrayList<Document>());

		return profileCollection.find(eq("batchid", batchid)).into(new ArrayList<Document>());

	}

	public List<Document> findResultList(int limit) {

		// posts = postsCollection.find().sort(new
		// Document("date",-1)).limit(limit).into(new ArrayList<Document>());

		return resultCollection.find().sort(new Document("date", -1)).limit(limit).into(new ArrayList<Document>());

	}

	public int findResultListSize() {

		// posts = postsCollection.find().sort(new
		// Document("date",-1)).limit(limit).into(new ArrayList<Document>());

		return resultCollection.find().into(new ArrayList<Document>()).size();

	}

	public int findResultListSize(String clientname) {

		// posts = postsCollection.find().sort(new
		// Document("date",-1)).limit(limit).into(new ArrayList<Document>());

		return resultCollection.find(eq("clientname", clientname)).into(new ArrayList<Document>()).size();

	}

	public List<Document> findResultList(int skip, int limit) {

		// posts = postsCollection.find().sort(new
		// Document("date",-1)).limit(limit).into(new ArrayList<Document>());

		return resultCollection.find().sort(new Document("date", -1)).skip(skip).limit(limit)
				.into(new ArrayList<Document>());

	}

	public List<Document> findResultList(String clientname, int skip, int limit) {

		// posts = postsCollection.find().sort(new
		// Document("date",-1)).limit(limit).into(new ArrayList<Document>());

		return resultCollection.find(eq("clientname", clientname)).sort(new Document("date", -1)).skip(skip)
				.limit(limit).into(new ArrayList<Document>());

	}

	public List<Document> findResultListByBatch(int batchid) {

		// posts = postsCollection.find().sort(new
		// Document("date",-1)).limit(limit).into(new ArrayList<Document>());

		return matchCollection.find(eq("batchid", batchid)).sort(new Document("date", -1))
				.into(new ArrayList<Document>());

	}

	public List<Document> findResultListByBatch(String batchid, int skip, int size) {

		// posts = postsCollection.find().sort(new
		// Document("date",-1)).limit(limit).into(new ArrayList<Document>());

		return matchCollection.find(eq("batchid", batchid)).skip(skip).limit(size).sort(new Document("date", -1))
				.into(new ArrayList<Document>());

	}

	public List<Document> findResultListByResult(String resultid, int skip, int size) {

		// posts = postsCollection.find().sort(new
		// Document("date",-1)).limit(limit).into(new ArrayList<Document>());

		return matchCollection.find(eq("resultid", resultid)).skip(skip).limit(size)
				.sort(new Document("lastUpdate", -1)).into(new ArrayList<Document>());

	}

	public int findResultListByBatchSize(String batchid) {

		// posts = postsCollection.find().sort(new
		// Document("date",-1)).limit(limit).into(new ArrayList<Document>());

		return matchCollection.find(eq("batchid", batchid)).into(new ArrayList<Document>()).size();

	}

	public int findResultListBySize(String resultid) {

		// posts = postsCollection.find().sort(new
		// Document("date",-1)).limit(limit).into(new ArrayList<Document>());

		return matchCollection.find(eq("resultid", resultid)).into(new ArrayList<Document>()).size();

	}

	public Document findBatchByID(int batchid) {

		// posts = postsCollection.find().sort(new
		// Document("date",-1)).limit(limit).into(new ArrayList<Document>());
		// post = postsCollection.find(eq("permalink",permalink)).first();
		return batchCollection.find(eq("batchid", batchid)).first();

	}

	public Document findClientByID(int clientid) {

		// posts = postsCollection.find().sort(new
		// Document("date",-1)).limit(limit).into(new ArrayList<Document>());
		// post = postsCollection.find(eq("permalink",permalink)).first();
		return clientCollection.find(eq("id", clientid)).first();

	}

	public Document findMatchByID(int matchid) {

		// posts = postsCollection.find().sort(new
		// Document("date",-1)).limit(limit).into(new ArrayList<Document>());
		// post = postsCollection.find(eq("permalink",permalink)).first();
		return matchCollection.find(eq("id", matchid)).first();

	}

	public Document findProfileByID(String profileid) {

		// long id = Long.valueOf(profileid).longValue();
		// posts = postsCollection.find().sort(new
		// Document("date",-1)).limit(limit).into(new ArrayList<Document>());
		// post = postsCollection.find(eq("permalink",permalink)).first();
		return profileCollection.find(eq("id", profileid)).first();

	}

	public Document findRuleByID(String ruleid) {

		return ruleCollection.find(eq("id", ruleid)).first();

	}

	public void deleteProfileByID(String profileid) {

		// int id = Integer.valueOf(profileid).intValue();

		profileCollection.deleteOne(eq("id", profileid));

	}

	public void deleteRuleByID(String ruleid) {

		// int id = Integer.valueOf(profileid).intValue();

		ruleCollection.deleteOne(eq("id", ruleid));

	}

	public List<String> getFieldsByID(String profileid) {

		Document profile = findProfileByID(profileid);

		List<String> names = (ArrayList<String>) profile.get("fieldnames");

		return names;

	}

	public ArrayList<String> getDocFieldName(Document d) {

		ArrayList<String> names = new ArrayList<String>();

		Set<Map.Entry<String, Object>> entrySet = d.entrySet();
		for (Map.Entry<String, Object> entry : entrySet) {

			// System.out.println(entry.getKey());
			names.add((String) entry.getKey());

		}

		return names;

	}

	public Document getFieldsDocByID(String profileid) {

		Document profile = findProfileByID(profileid);

		return (Document) profile.get("fields");

	}

	public void updateProfile(Document p1, Document p2) {

		try {
			profileCollection.findOneAndUpdate(p1, p2);

		} catch (MongoWriteException e) {
			e.printStackTrace();
		}

	}

	public List<Document> findRecsByBatchID(int batchid) {

		List<Document> recs = new ArrayList<Document>();

		Document batch = findBatchByID(batchid);

		// check if this is an interactive lookup
		String filename = batch.getString("filename");

		if (filename.equals("Interactive")) {
			// this is interactive lookup

			recs.add((Document) batch.get("rec"));
		} else {

			String batchname = batch.getString("recordCollection");

			System.out.println("batchid: " + batchid);
			System.out.println("batchname: " + batchname);

			MongoCollection<Document> batchRecordCollection = abnDatabase.getCollection(batchname);

			recs = batchRecordCollection.find().into(new ArrayList<Document>());

		}
		return recs;

	}

	public List<Document> findRecsByBatchID(int batchid, int page, int pageSize) {

		// posts = postsCollection.find().sort(new
		// Document("date",-1)).limit(limit).into(new ArrayList<Document>());
		// post = postsCollection.find(eq("permalink",permalink)).first();
		// return batchCollection.find(eq("batchid",batchid)).first();

		List<Document> recs = null;

		Document batch = findBatchByID(batchid);

		// System.out.println(batch.get("input"));

		recs = (ArrayList<Document>) batch.get("input");

		return recs;

	}

	public List<Document> getMatchByResultID(int resultid) {

		// posts = postsCollection.find().sort(new
		// Document("date",-1)).limit(limit).into(new ArrayList<Document>());
		// post = postsCollection.find(eq("permalink",permalink)).first();
		// return batchCollection.find(eq("batchid",batchid)).first();

		Document result = resultCollection.find(eq("resultid", resultid)).first();

		List<Document> matches = (ArrayList<Document>) result.get("match");

		return matches;

	}

	public List<Document> getMatchByStatus(String status, int limit) {

		String st = "";

		if (status.equals("open")) {
			st = "open";

		} else if (status.equals("fp")) {
			st = "False Positive";

		} else if (status.equals("fi")) {

			st = "Further Investigation";
		} else if (status.equals("tm")) {
			st = "True Match";

		} else {
			st = "open";

		}
		// System.out.println("st: " + st);

		return matchCollection.find(eq("status", st)).limit(limit).into(new ArrayList<Document>());

	}

	public List<Document> getMatchByStatus(String status) {

		String st = "";

		if (status.equals("open")) {
			st = "open";

		} else if (status.equals("fp")) {
			st = "False Positive";

		} else if (status.equals("fi")) {

			st = "Further Investigation";
		} else if (status.equals("tm")) {
			st = "True Match";

		} else {
			st = "open";

		}

		// System.out.println("st: " + st);

		return matchCollection.find(eq("status", st)).into(new ArrayList<Document>());

	}

	public List<Document> getMatchByStatus(String clientname, String status, int skip, int limit) {

		String st = "";

		if (status.equals("open")) {
			st = "open";

		} else if (status.equals("fp")) {
			st = "False Positive";

		} else if (status.equals("fi")) {

			st = "Further Investigation";
		} else if (status.equals("tm")) {
			st = "True Match";

		} else if (status.equals("pbr")) {
			st = "Pass by Rule";

		} else {
			st = "open";

		}
		// System.out.println("st: " + st);

		return matchCollection.find(and(eq("status", st), eq("clientname", clientname))).sort(new Document("id", -1))
				.skip(skip).limit(limit).into(new ArrayList<Document>());

	}

	public List<Document> getMatchByStatus(String status, int skip, int limit) {

		String st = "";

		if (status.equals("open")) {
			st = "open";

		} else if (status.equals("fp")) {
			st = "False Positive";

		} else if (status.equals("fi")) {

			st = "Further Investigation";
		} else if (status.equals("tm")) {
			st = "True Match";

		} else if (status.equals("pbr")) {
			st = "Pass by Rule";
		} else {
			st = "open";

		}
		// System.out.println("st: " + st);

		return matchCollection.find(eq("status", st)).sort(new Document("id", -1)).skip(skip).limit(limit)
				.into(new ArrayList<Document>());

	}

	public int getMatchByStatusSize(String clientname, String status) {

		String st = "";

		if (status.equals("open")) {
			st = "open";

		} else if (status.equals("fp")) {
			st = "False Positive";

		} else if (status.equals("fi")) {

			st = "Further Investigation";
		} else if (status.equals("tm")) {
			st = "True Match";

		} else if (status.equals("pbr")) {
			st = "Pass by Rule";

		} else {
			st = "open";

		}
		// System.out.println("st: " + st);

		return matchCollection.find(and(eq("status", st), eq("clientname", clientname))).into(new ArrayList<Document>())
				.size();

	}

	public int getMatchByStatusSize(String status) {

		String st = "";

		if (status.equals("open")) {
			st = "open";

		} else if (status.equals("fp")) {
			st = "False Positive";

		} else if (status.equals("fi")) {

			st = "Further Investigation";
		} else if (status.equals("tm")) {
			st = "True Match";

		} else if (status.equals("pbr")) {
			st = "Pass by Rule";

		} else {
			st = "open";

		}
		System.out.println("st: " + st);

		return matchCollection.find(eq("status", st)).into(new ArrayList<Document>()).size();

	}

	public List<Document> getMatchByResultRecID(int resultid, String recid) {

		Document result = resultCollection.find(eq("resultid", resultid)).first();

		List<Document> matches = (ArrayList<Document>) result.get("match");

		List<Document> f = new ArrayList<Document>();

		for (int i = 0; i < matches.size(); i++) {

			Document m = matches.get(i);

			String rec = (String) m.get("recid");

			if (rec.equals(recid)) {
				f.add(m);
			}

		}

		return f;

	}

	public Document getMatchByID(String matchid) {

		return matchCollection.find(eq("id", Integer.valueOf(matchid).intValue()))
				.sort(new Document("entity.score", -1)).first();

	}

	public long getMatchCountByResultID(String resultid) {

		return matchCollection.count(eq("resultid", resultid));

	}

	public Document getRuleByID(String ruleid) {

		return ruleCollection.find(eq("id", ruleid)).first();

	}

	public Document getResultByMatchID(String matchid) {

		return resultCollection.find(eq("match.matchid", Integer.valueOf(matchid).intValue())).first();

	}

	public void updateMatchByID(String matchid, String status, String comment, String user) {

		Document replace = new Document("$set",
				new Document("status", status).append("comment", comment).append("lastUpdate", new Date()));

		// System.out.println("replace: " + replace.toJson());

		matchCollection.updateMany((eq("id", Integer.valueOf(matchid).intValue())), replace);

		// if (result == null) {
		// System.out.println("Update failed!");
		// } else {
		// System.out.println("Updated:" + result.toJson());
		// }

		// record the action
		Document action = new Document("matchid", matchid).append("newstatus", status).append("comment", comment)
				.append("user", user).append("updateTime", new Date());

		addAction(action);
	}

	public void applyRules(String resultid, String ruleid, String user) {

		Document replace = new Document("$set",
				new Document("status", "Pass by Rule").append("lastUpdate", new Date()));

		// System.out.println("replace: " + replace.toJson());

		Document rule = getRuleByID(ruleid);

		String condition = rule.getString("condition");

		// SubCategory=Former PEP

		// nameFields = name.split("\\s+");

		String[] token = condition.split("=");

		System.out.println("type:" + token[0] + " value:" + token[1]);

		// sample
		// "entity.sdfs.sdf.@name":"SubCategory" "entity.sdfs.sdf.#text":"Former
		// PEP"

		// String filter = "eq()"

		matchCollection.updateMany(and(eq("resultid", resultid), eq("entity.sdfs.sdf.@name", token[0]),
				eq("entity.sdfs.sdf.#text", token[1])), replace);

	}

	public void updateMatchByID(String matchid, String status, String comment, String user, String clientname) {

		Document replace = new Document("$set",
				new Document("status", status).append("comment", comment).append("lastUpdate", new Date()));

		// System.out.println("replace: " + replace.toJson());

		matchCollection.updateMany((eq("id", Integer.valueOf(matchid).intValue())), replace);

		// if (result == null) {
		// System.out.println("Update failed!");
		// } else {
		// System.out.println("Updated:" + result.toJson());
		// }

		// record the action
		Document action = new Document("matchid", matchid).append("newstatus", status).append("comment", comment)
				.append("user", user).append("clientname", clientname).append("updateTime", new Date());

		addAction(action);
	}

	public List<Document> getActionList(int limit) {

		return actionCollection.find().sort(new Document("updateTime", -1)).limit(limit)
				.into(new ArrayList<Document>());

	}

	public List<Document> getActionList(String clientname, int limit) {

		return actionCollection.find(eq("clientname", clientname)).sort(new Document("updateTime", -1)).limit(limit)
				.into(new ArrayList<Document>());

	}

	public List<Document> getPLMList(int limit) {

		return listCollection.find().sort(new Document("date", -1)).limit(limit).into(new ArrayList<Document>());

	}

	public List<Document> getPLMList(String clientname, int limit) {

		return listCollection.find(eq("clientname", clientname)).sort(new Document("date", -1)).limit(limit)
				.into(new ArrayList<Document>());

	}

	public List<Document> getCategoryList() {

		return categoryCollection.find().into(new ArrayList<Document>());

	}

	public List<Document> getUserList(int limit) {

		return userCollection.find().sort(new Document("date", -1)).limit(limit).into(new ArrayList<Document>());

	}

	public List<Document> getClientList(int limit) {

		return clientCollection.find().sort(new Document("date", -1)).limit(limit).into(new ArrayList<Document>());

	}

	public List<Document> getEventList(int limit) {

		return auditCollection.find().sort(new Document("date", -1)).limit(limit).into(new ArrayList<Document>());

	}

	public List<Document> searchPLMList(String listid, Document searchDoc, int limit) {

		// findByFieldonCustomList
		return listCollection.find().sort(new Document("date", -1)).limit(limit).into(new ArrayList<Document>());

	}

	public List<Document> getMatchByRecID(String recid) {

		System.out.println("recid:" + recid);
		return resultCollection.find(eq("match.recid", recid)).into(new ArrayList<Document>());

	}

	public String getListNameByID(int id) {

		Document list = listCollection.find(eq("id", id)).first();
		return list.getString("name");
	}

	// public List<Document> InteractiveScreeningWS(String profileid, Document
	// searchDoc) {}

	public int batchScreeningMulti(String batchid, String profileid, String user) {

		int resultId = 0;

		return resultId;
	}

	public String findClientNameByUserID(String userId) {

		// posts = postsCollection.find().sort(new
		// Document("date",-1)).limit(limit).into(new ArrayList<Document>());
		// post = postsCollection.find(eq("permalink",permalink)).first();

		Document user;

		user = userCollection.find(eq("_id", userId)).first();

		return user.getString("clientname");

	}

	// public int batchScreeningSingle(String batchid, String profileid, String
	// user, String listid) {}

	public List<Document> findByHomePhone(String phone) {

		List<Document> plms = new ArrayList<Document>();
		Iterator<Document> it = null;

		if (phone != null) {

			// db.plm.find({"NAME":"Aeleen Oraham"}).pretty()

			if (phone.length() > 1) {
				BasicDBObject regexQuery = new BasicDBObject();
				regexQuery.put("Home Phone", new BasicDBObject("$regex", phone).append("$options", "i"));

				System.out.println(regexQuery.toString());

				it = plmCollection.find(regexQuery).iterator();

			}
		}

		while (it.hasNext()) {
			Document d = (Document) it.next();

			plms.add(d);
		}

		// System.out.println(abns);

		return plms;
	}

	public List<Document> findByMobilePhone(String phone) {

		List<Document> plms = new ArrayList<Document>();
		Iterator<Document> it = null;

		if (phone != null) {

			// db.plm.find({"NAME":"Aeleen Oraham"}).pretty()

			if (phone.length() > 1) {
				BasicDBObject regexQuery = new BasicDBObject();
				regexQuery.put("Mobile Phone", new BasicDBObject("$regex", phone).append("$options", "i"));

				System.out.println(regexQuery.toString());

				it = plmCollection.find(regexQuery).iterator();

			}
		}

		while (it.hasNext()) {
			Document d = (Document) it.next();

			plms.add(d);
		}

		// System.out.println(abns);

		return plms;
	}

	public List<Document> findByAddress(String address) {

		List<Document> plms = new ArrayList<Document>();
		Iterator<Document> it = null;

		if (address != null) {

			// db.plm.find({"NAME":"Aeleen Oraham"}).pretty()

			if (address.length() > 1) {
				BasicDBObject regexQuery = new BasicDBObject();
				regexQuery.put("ADDRESS1", new BasicDBObject("$regex", address).append("$options", "i"));

				System.out.println(regexQuery.toString());

				it = plmCollection.find(regexQuery).iterator();

			}
		}

		while (it.hasNext()) {
			Document d = (Document) it.next();

			plms.add(d);
		}

		return plms;
	}

}
