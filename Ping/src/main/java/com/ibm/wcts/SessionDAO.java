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

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import sun.misc.BASE64Encoder;

public class SessionDAO {
    private  MongoCollection<Document> sessionsCollection;

    public SessionDAO(MongoDatabase blogDatabase) {
        sessionsCollection = blogDatabase.getCollection("sessions");
    }


    public String findUserNameBySessionId(String sessionId) {
        Document session = getSession(sessionId);

        if (session == null) {
            return null;
        } else {
            return session.get("username").toString();
        }
    }

    public List<String> findUserRoleBySessionId(String sessionId) {
        Document session = getSession(sessionId);

        if (session == null) {
            return null;
        } else {
            return (ArrayList<String>)session.get("roles");
        }
    }

    public Document findSessionById(String sessionId) {
        return getSession(sessionId);

    }

    // starts a new session in the sessions table
    public String startSession(String username) {

        // get 32 byte random number. that's a lot of bits.
        SecureRandom generator = new SecureRandom();
        byte randomBytes[] = new byte[32];
        generator.nextBytes(randomBytes);

        BASE64Encoder encoder = new BASE64Encoder();

        String sessionID = encoder.encode(randomBytes);

        // build the BSON object
        Document session = new Document("username", username)
                           .append("_id", sessionID);

        sessionsCollection.insertOne(session);

        return session.getString("_id");
    }

    // starts a new session in the sessions table
    public String startSession(Document user) {

        // get 32 byte random number. that's a lot of bits.
        SecureRandom generator = new SecureRandom();
        byte randomBytes[] = new byte[32];
        generator.nextBytes(randomBytes);

        BASE64Encoder encoder = new BASE64Encoder();

        String sessionID = encoder.encode(randomBytes);

        String username = user.getString("_id");
        String clientname = user.getString("clientname");
        
        List<String> roles = (ArrayList<String>)user.get("role");
        
        // build the BSON object
        Document session = new Document("username", username)
                           .append("_id", sessionID).append("roles",roles).append("clientname", clientname);

        sessionsCollection.insertOne(session);

        return session.getString("_id");
    }


    // ends the session by deleting it from the sesisons table
    public void endSession(String sessionID) {
        sessionsCollection.deleteOne(eq("_id", sessionID));
    }

    // retrieves the session from the sessions table
    public Document getSession(String sessionID) {
        return sessionsCollection.find(eq("_id", sessionID)).first();
    }
}
