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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class ABNDAO {
	private final MongoCollection<Document> abnCollection;
	private final MongoCollection<Document> acnCollection;

	public ABNDAO(final MongoDatabase abnDatabase) {
		abnCollection = abnDatabase.getCollection("abn");
		acnCollection = abnDatabase.getCollection("acn");
	}

	public List<Document> findABN(String abnNum, String busName) {

		List<Document> abns = new ArrayList<Document>();
		Iterator<Document> it = null;

		if (abnNum != null) {
			// > db.abn.find({"ABR.ABN.#text":"29080662568"}).pretty()
			it = abnCollection.find(eq("ABR.ABN.#text", abnNum)).iterator();
		}

		if (busName != null) {
			// >
			// db.abn.find({"ABR.MainEntity.NonIndividualName.NonIndividualNameText":/Veda
			// Advantage/}).pretty()

			if (busName.length() > 1) {
				BasicDBObject regexQuery = new BasicDBObject();
				regexQuery.put("ABR.MainEntity.NonIndividualName.NonIndividualNameText",
						new BasicDBObject("$regex", "^" + busName).append("$options", "i"));

				System.out.println(regexQuery.toString());

				it = abnCollection.find(regexQuery).iterator();

				// it =
				// abnCollection.find(eq("ABR.MainEntity.NonIndividualName.NonIndividualNameText",
				// busName)).iterator();
			}
		}

		while (it.hasNext()) {
			Document d = (Document) it.next();

			// get ABN values and simplify the document returned since SPARK
			// can't do nested loop

			// ABN Name -
			// ${abn["ABR.MainEntity.NonIndividualName.NonIndividualNameText"]}
			// <br>

			Document abr = (Document) d.get("ABR");
			Document ABN = (Document) abr.get("ABN");
			String number = ABN.getString("#text");
			// System.out.println("got number: " + number);
			d.append("number", number);

			Document main = (Document) abr.get("MainEntity");
			Document NonIndividualName = (Document) main.get("NonIndividualName");
			String name = NonIndividualName.getString("NonIndividualNameText");

			// System.out.println("got name: " + name);

			d.append("name", name);

			Document BusinessAddress = (Document) main.get("BusinessAddress");
			Document AddressDetails = (Document) BusinessAddress.get("AddressDetails");
			String addr = AddressDetails.getString("Postcode") + " " + AddressDetails.getString("State");
			d.append("addr", addr);

			Document ASICNumber = (Document) abr.get("ASICNumber");
			if (ASICNumber != null) {
				String acn = ASICNumber.getString("#text");

				System.out.println(acn);

				// get ACN info
//				List<Document> acns = findACN(acn);
//
//				if (acns != null) {
//					Document a = (Document) acns.toArray()[0];
//					String company = a.getString("Company Name");
//					d.append("companyName", company);
//				}

				d.append("acn", acn);
			} else {
				d.append("acn", "");

			}

			abns.add(d);
		}

		// System.out.println(abns);

		return abns;
	}

	public List<Document> findACN(String acnNum) {

		List<Document> acns = new ArrayList<Document>();
		Iterator<Document> it = null;

		if (acnNum != null) {
			// > db.abn.find({"ABR.ABN.#text":"29080662568"}).pretty()
			int a = Integer.valueOf(acnNum).intValue();
			it = acnCollection.find(eq("ACN", a)).iterator();
		}

		while (it.hasNext()) {
			Document d = (Document) it.next();

			acns.add(d);
		}

		// System.out.println(abns);

		return acns;
	}

}
