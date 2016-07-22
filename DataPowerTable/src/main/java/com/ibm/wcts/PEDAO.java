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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bson.Document;

import com.mongodb.ErrorCategory;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import sun.misc.BASE64Encoder;

public class PEDAO {
    private final MongoCollection<Document> pesCollection;
    private Random random = new SecureRandom();

    public PEDAO(final MongoDatabase peDatabase) {
        pesCollection = peDatabase.getCollection("pes");
    }

    // validates that username is unique and insert into db
    public boolean addPE(String username, String searchType, String keyword) {


        Document pe = new Document();

        pe.append("username", username)
        .append("search type", searchType)
        .append("keyword", keyword)
        .append("timestamp", new Date());

        try {
            pesCollection.insertOne(pe);
            return true;
        } catch (MongoWriteException e) {
        	e.printStackTrace();
            throw e;
        }
    }
    
    // validates that username is unique and insert into db
    public boolean auditChange(String username,String listname, Document before, Document after) {


        Document pe = new Document();

        pe.append("username", username).append("listname", listname)
        .append("timestamp", new Date()).append("before", before).append("after", after);

        try {
            pesCollection.insertOne(pe);
            return true;
        } catch (MongoWriteException e) {
        	e.printStackTrace();
            throw e;
        }
    }


	public List<Document> findPE(String username, String limit) {

		List<Document> pes = new ArrayList<Document>();
		Iterator<Document> it = null;

		int l=100; //use 100 records as default
		
		if(limit !=null){
			l=Integer.valueOf(limit).intValue();
		}
		
		if (username != null) {
			// > db.abn.find({"ABR.ABN.#text":"29080662568"}).pretty()
			it = pesCollection.find(eq("username", username))
					.sort(new Document("timestamp",-1))
					.limit(l)
					.iterator();
		}

		while (it.hasNext()) {
			Document d = (Document) it.next();

			pes.add(d);
		}

		// System.out.println(abns);

		return pes;
	}

	public List<Document> findPE(String limit) {

		List<Document> pes = new ArrayList<Document>();
		Iterator<Document> it = null;

		int l=100; //use 100 records as default
		
		if(limit !=null){
			l=Integer.valueOf(limit).intValue();
		}
		it = pesCollection.find()
				.sort(new Document("timestamp",-1))
				.limit(l)
				.iterator();
		
		

		while (it.hasNext()) {
			Document d = (Document) it.next();

			pes.add(d);
		}

		// System.out.println(abns);

		return pes;
	}
	
//    public Iterator<Document> findABN(String abnNum, String busName) {
//    	
//    	Iterator abns;
//        Document user;
//
//        user = usersCollection.find(eq("_id", username)).first();
//
//        if (user == null) {
//            System.out.println("User not in database");
//            return null;
//        }
//
//        String hashedAndSalted = user.get("password").toString();
//
//        String salt = hashedAndSalted.split(",")[1];
//
//        if (!hashedAndSalted.equals(makePasswordHash(password, salt))) {
//            System.out.println("Submitted password is not a match");
//            return null;
//        }
//
//        return user;
//    }


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
