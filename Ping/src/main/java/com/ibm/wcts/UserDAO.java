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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bson.Document;

import com.mongodb.ErrorCategory;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import sun.misc.BASE64Encoder;

public class UserDAO {
	private final MongoCollection<Document> usersCollection;
	private final MongoCollection<Document> clientCollection;
	private final MongoCollection<Document> auditCollection;
	
	private Random random = new SecureRandom();

	public UserDAO(final MongoDatabase blogDatabase) {
		usersCollection = blogDatabase.getCollection("users");
		clientCollection = blogDatabase.getCollection("client");
		auditCollection = blogDatabase.getCollection("audit");
	}

	// validates that username is unique and insert into db

	public Document findUser(String username) {
		return usersCollection.find(eq("_id", username)).first();
	}

	public String findClientNameByUserID(String userId) {

		// posts = postsCollection.find().sort(new
		// Document("date",-1)).limit(limit).into(new ArrayList<Document>());
		// post = postsCollection.find(eq("permalink",permalink)).first();

		Document user;

		user = usersCollection.find(eq("_id", userId)).first();

		return user.getString("clientname");

	}

	public boolean activateAccount(String token) {

		Document user;

		user = usersCollection.find(eq("token", token)).first();

		if (user == null) {
			System.out.println("User not in database");
			return false;
		}

		user.put("status", "Activate");

		// metaCollection.findOneAndUpdate(eq("_id", meta.get("_id")), update);
		// Document init = new Document("$set", new Document("batchhwm", 1000));
		// metaCollection.findOneAndUpdate(eq("_id", meta.get("_id")), init);

		Document init = new Document("$set", new Document("status", "Activate"));
		usersCollection.findOneAndUpdate(eq("token", token), init);

		System.out.println("User activated");

		return true;
	}

	public boolean activateAccountAdmin(String username) {

		Document user;

		user = usersCollection.find(eq("_id", username)).first();

		if (user == null) {
			System.out.println("User not in database");
			return false;
		}

		user.put("status", "Activate");

		// metaCollection.findOneAndUpdate(eq("_id", meta.get("_id")), update);
		// Document init = new Document("$set", new Document("batchhwm", 1000));
		// metaCollection.findOneAndUpdate(eq("_id", meta.get("_id")), init);

		Document init = new Document("$set", new Document("status", "Activate"));
		usersCollection.findOneAndUpdate(eq("_id", username), init);

		System.out.println("User activated");

		return true;
	}

	public boolean deactivateAccountAdmin(String username) {

		Document user;

		user = usersCollection.find(eq("_id", username)).first();

		if (user == null) {
			System.out.println("User not in database");
			return false;
		}

		user.put("status", "Inactivate");

		// metaCollection.findOneAndUpdate(eq("_id", meta.get("_id")), update);
		// Document init = new Document("$set", new Document("batchhwm", 1000));
		// metaCollection.findOneAndUpdate(eq("_id", meta.get("_id")), init);

		Document init = new Document("$set", new Document("status", "Inactive"));
		usersCollection.findOneAndUpdate(eq("_id", username), init);

		System.out.println("User deactivated");

		return true;
	}

	// addUser(username, password,
	// email,status,firstname,lastname,workphone,mobilephone,department,role)
	public boolean addUser(String username, String password, String email, String status, String firstname,
			String lastname, String workphone, String mobilephone, String department, List<String> role,
			String clientname) {

		String passwordHash = makePasswordHash(password, Integer.toString(random.nextInt()));

		Document user = new Document();

		user.append("_id", username).append("password", passwordHash).append("status", status)
				.append("firstname", firstname).append("lastname", lastname).append("department", department)
				.append("workphone", workphone).append("mobilephone", mobilephone).append("role", role)
				.append("clientname", clientname).append("email", email);

		// add uuid for account activation
		String pid = UUID.randomUUID().toString();
		System.out.println("UUID: " + pid);

		user.append("token", pid);

		if (email != null && !email.equals("")) {
			// the provided email address
			user.append("email", email);
		}

		try {
			usersCollection.insertOne(user);

			addAudit("username","New account created for "+username,"user account");
			
		

			return true;
		} catch (MongoWriteException e) {
			if (e.getError().getCategory().equals(ErrorCategory.DUPLICATE_KEY)) {
				System.out.println("Username already in use: " + username);
				return false;
			}
			throw e;
		}
	}

	public boolean updateUserPassword(String username, String password) {
		String passwordHash = makePasswordHash(password, Integer.toString(random.nextInt()));

		Document user = new Document("password", passwordHash);

		try {
			usersCollection.findOneAndUpdate(new Document("_id", username), new Document("$set", user));

			return true;
		} catch (MongoWriteException e) {
			if (e.getError().getCategory().equals(ErrorCategory.DUPLICATE_KEY)) {
				return false;
			}
			throw e;
		}

	}

	public void addAudit(String username,String event,String type){
		
		Document eventDoc = new Document();
		
		eventDoc.append("username", username).append("event", event).append("type", type).append("time", new Date());
		
		auditCollection.insertOne(eventDoc);
		
	}
	public boolean updateUser(String username, String email, String status, String firstname, String lastname,
			String workphone, String mobilephone, String department, List<String> role, String clientname) {

		Document user = new Document();

		user.append("_id", username).append("status", status).append("firstname", firstname)
				.append("lastname", lastname).append("department", department).append("workphone", workphone)
				.append("mobilephone", mobilephone).append("role", role).append("clientname", clientname);

		if (email != null && !email.equals("")) {
			// the provided email address
			user.append("email", email);
		}

		try {
			usersCollection.findOneAndUpdate(new Document("_id", username), new Document("$set", user));

			return true;
		} catch (MongoWriteException e) {
			if (e.getError().getCategory().equals(ErrorCategory.DUPLICATE_KEY)) {
				System.out.println("Username already in use: " + username);
				return false;
			}
			throw e;
		}
	}

	public Document validateLogin(String username, String password) {
		Document user;

		user = usersCollection.find(eq("_id", username)).first();

		if (user == null) {
			System.out.println("User not in database");

			user = new Document();

			user.put("message", "User not in database");
		}

		String hashedAndSalted = user.get("password").toString();

		String salt = hashedAndSalted.split(",")[1];

		if (!hashedAndSalted.equals(makePasswordHash(password, salt))) {
			System.out.println("Submitted password is not a match");
			user.put("message", "Submitted password is not a match");
		}

		String status = user.getString("status");

		if (status.equals("Inactive")) {
			System.out.println("User is inactive");
			user.put("message", "User is inactive");

		}
		return user;
	}

	public Document validateLoginwithInfo(String username, String password, HashMap<String, String> errors) {
		Document user;

		user = usersCollection.find(eq("_id", username)).first();

		if (user == null) {
			System.out.println("User not in database");
			errors.put("result", "User not in database");

			return null;
		}

		String hashedAndSalted = user.get("password").toString();

		String salt = hashedAndSalted.split(",")[1];

		if (!hashedAndSalted.equals(makePasswordHash(password, salt))) {
			System.out.println("Submitted password is not a match");
			errors.put("result", "Submitted password is not a match");
			return null;
		}

		return user;
	}

	// public Iterator<Document> findABN(String abnNum, String busName) {
	//
	// Iterator abns;
	// Document user;
	//
	// user = usersCollection.find(eq("_id", username)).first();
	//
	// if (user == null) {
	// System.out.println("User not in database");
	// return null;
	// }
	//
	// String hashedAndSalted = user.get("password").toString();
	//
	// String salt = hashedAndSalted.split(",")[1];
	//
	// if (!hashedAndSalted.equals(makePasswordHash(password, salt))) {
	// System.out.println("Submitted password is not a match");
	// return null;
	// }
	//
	// return user;
	// }

	private String makePasswordHash(String password, String salt) {
		try {
			String saltedAndHashed = password + "," + salt;
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(saltedAndHashed.getBytes());
			BASE64Encoder encoder = new BASE64Encoder();
			byte hashedBytes[] = (new String(digest.digest(), "UTF-8")).getBytes();
			return encoder.encode(hashedBytes) + "," + salt;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("MD5 is not available", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UTF-8 unavailable?  Not a chance", e);
		}
	}
}
