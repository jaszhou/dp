package com.ibm.wcts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static com.mongodb.client.model.Filters.eq;
import org.bson.Document;

import com.mongodb.client.MongoCollection;

/**
 * This is class can be used for data load
 * 
 * @author jason.zhou
 *
 */

public class ETL extends BlogController {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// ETL.addList();
		// ETL.convertList();
//		ETL.convertAUPEPList();
		// ETL.convertOFAC();
//		ETL.addAUPEPList();
		
		ETL.addDomesticPEPList();
//		ETL.convertDomesticPEPList();
		
//		ETL.convertUN();
//		ETL.convertEU();
//		ETL.addEUList();
		// ETL.addUNList();
		// ETL.addOFACList();

	}

	static void convertEU() {
		


		// TODO Auto-generated method stub

		try {
			Util ut = new Util();

			mongoURL = ut.getPropValues("mongoURL");

			int chunk = 10000;

			setupConnection();

			MongoCollection<Document> PEPCollection = clientDatabase.getCollection("EU1");
			MongoCollection<Document> PEPListCollection = clientDatabase.getCollection("EU");

			long size = PEPCollection.count();

			int l = 0;

			for (; l < size; l += chunk) {

				List<Document> entities = PEPCollection.find().skip(l).limit(chunk).into(new ArrayList<Document>());

				int count = 0;
				for (Document entity : entities) {

					Document en = (Document) entity.get("ENTITY");

					String name = "";
					String country = en.getString("@programme");

					Object names = en.get("NAME");
			
					if (names instanceof java.util.ArrayList) {
						
						// only use first one
						
						List lName = (List<Document>)names;
						
						Document first = (Document)lName.get(0);
						
						name = first.getString("WHOLENAME");
					}else{
						
						Document dName = (Document)names;
						name = dName.getString("WHOLENAME");
					}

			
					// PEPListCollection.insertMany(entities);

					en.append("name", name).append("country", country);

					PEPListCollection.insertOne(en);
					count++;

					if (count % 1000 == 0) {
						System.out.println("number processed: " + (l + count));
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	
		
	}
	static void convertUN() {

		// TODO Auto-generated method stub

		try {
			Util ut = new Util();

			mongoURL = ut.getPropValues("mongoURL");

			int chunk = 10000;

			setupConnection();

			MongoCollection<Document> PEPCollection = clientDatabase.getCollection("UN1");
			MongoCollection<Document> PEPListCollection = clientDatabase.getCollection("UN");

			long size = PEPCollection.count();

			int l = 0;

			for (; l < size; l += chunk) {

				List<Document> entities = PEPCollection.find().skip(l).limit(chunk).into(new ArrayList<Document>());

				int count = 0;
				for (Document entity : entities) {

					Document en = (Document) entity.get("INDIVIDUAL");

					String name = "";
					String country = "";

					if (en == null) {
						en = (Document) entity.get("ENTITY");
						name = en.getString("FIRST_NAME");

						Object oadr = en.get("ENTITY_ADDRESS");

						if (oadr instanceof java.util.ArrayList) {

							List<Document> adrs = (List<Document>) oadr;

							for (Document adr : adrs) {

								if (country == "") {
									country = adr.getString("COUNTRY");
								} else {
									country = country + "," + adr.getString("COUNTRY");
								}

							}
						} else {
							Document adr = (Document) oadr;

							if (adr != null)
								country = adr.getString("COUNTRY");

						}

					} else {

						name = en.getString("SECOND_NAME") + "," + en.getString("FIRST_NAME");
					}

					// PEPListCollection.insertMany(entities);

					Document nationals = null;

					Object nation = en.get("NATIONALITY");

					if (nation instanceof java.util.ArrayList) {

						List<Document> nats = (List<Document>) nation;
						for (Document nat : nats) {
							Object natv = nat.get("VALUE");

							if (natv instanceof java.util.ArrayList) {

								List<String> natm = (List<String>) natv;

								for (String n : natm) {
									if (country == "") {
										country = n;
									} else {
										country = country + "," + n;
									}

								}
							} else {

								country = (String) natv;

							}

						}

					} else {
						nationals = (Document) en.get("NATIONALITY");
					}

					if (nationals != null) {

						Object nat = nationals.get("VALUE");

						if (nat instanceof java.util.ArrayList) {

							List<String> nats = (List<String>) nat;

							for (String n : nats) {
								if (country == "") {
									country = n;
								} else {
									country = country + "," + n;
								}

							}
						} else {

							country = (String) nat;

						}
					}

					en.append("name", name).append("country", country);

					PEPListCollection.insertOne(en);
					count++;

					if (count % 1000 == 0) {
						System.out.println("number processed: " + (l + count));
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	static void convertOFAC() {

		// TODO Auto-generated method stub

		try {
			Util ut = new Util();

			mongoURL = ut.getPropValues("mongoURL");

			int chunk = 10000;

			setupConnection();

			MongoCollection<Document> PEPCollection = clientDatabase.getCollection("OFAC1");
			MongoCollection<Document> PEPListCollection = clientDatabase.getCollection("OFAC");

			long size = PEPCollection.count();

			int l = 0;

			for (; l < size; l += chunk) {

				List<Document> entities = PEPCollection.find().skip(l).limit(chunk).into(new ArrayList<Document>());

				int count = 0;
				for (Document entity : entities) {

					Document en = (Document) entity.get("sdnEntry");

					// PEPListCollection.insertMany(entities);

					String name = "";

					String sdnType = en.getString("sdnType");

					if (sdnType.equalsIgnoreCase("Entity")) {
						name = en.getString("lastName");
					} else if (sdnType.equalsIgnoreCase("Individual")) {
						name = en.getString("lastName") + "," + en.getString("firstName");
					}

					// String
					// country=en.getString("addressList.address.country");
					String country = "";

					Document adl = (Document) en.get("addressList");

					if (adl != null) {

						Object adr = adl.get("address");

						// System.out.println("type:" + adr.getClass());

						if (adr instanceof java.util.ArrayList) {

							List<Document> ads = (List<Document>) adr;

							for (Document ad : ads) {

								if (country == "") {
									country = ad.getString("country");
								} else {
									country = country + "," + ad.getString("country");
								}

							}
						} else {

							Document address = (Document) adr;

							country = address.getString("country");
						}

					}
					en.append("name", name).append("country", country);

					PEPListCollection.insertOne(en);
					count++;

					if (count % 1000 == 0) {
						System.out.println("number processed: " + (l + count));
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	static void convertDomesticPEPList() {

		

		// TODO Auto-generated method stub

		try {
			Util ut = new Util();

			mongoURL = ut.getPropValues("mongoURL");

			int chunk = 1000;

			setupConnection();

			MongoCollection<Document> PEPCollection = clientDatabase.getCollection("AUPEP");
			MongoCollection<Document> PEPListCollection = clientDatabase.getCollection("DomesticPEP");

			long size = PEPCollection.count();

			int l = 0;

			for (; l < size; l += chunk) {

				List<Document> entities = PEPCollection.find().skip(l).limit(chunk).into(new ArrayList<Document>());

				int count = 0;
				for (Document entity : entities) {

//					Document en = (Document) entity.get("entity");

					String name = entity.getString("name");
					String country = "AU";
					
					
					Object t = entity.get("titles");
					List<String> ts = new ArrayList();
					
					if(t instanceof java.util.ArrayList){
						List<Document> titles = (List<Document>)t;
						
						
						for(Document title: titles){
							
							ts.add(title.getString("title"));
							
						}
						
					}else{
						Document tt = (Document)t;
						
						Object t1 = tt.get("title");
						
						if(t1 instanceof java.util.ArrayList){
							List<String> titles = (List<String>)t1;
							
							
							for(String title: titles){
								
								ts.add(title);
								
							}
							
						}else{
							ts.add((String)t1);
						}
					}
					
					
					
					Document p = new Document();
					
					p.append("name", name).append("titles", ts).append("country", country);
					
					Document sdfs = (Document)entity.get("sdfs");
					
					List<Document> sdflist = (List<Document>)sdfs.get("sdf");
					
					for(Document sdf : sdflist){
						String aname = sdf.getString("@name");
						
						if(aname.equals("Relationship")){
							p.append("relationship", sdf.getString("#text"));
						}
						
						if(aname.equals("SubCategory")){
							p.append("subcategory", sdf.getString("#text"));
						}
						
						if(aname.equals("OtherInformation")){
							p.append("comment", sdf.getString("#text"));
						}

					}
					
					// PEPListCollection.insertMany(entities);

					PEPListCollection.insertOne(p);
					count++;

					if (count % 1000 == 0) {
						System.out.println("number processed: " + (l + count));
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	
		
	
	}
	static void convertAUPEPList() {
		

		// TODO Auto-generated method stub

		try {
			Util ut = new Util();

			mongoURL = ut.getPropValues("mongoURL");

			int chunk = 1000;

			setupConnection();

			MongoCollection<Document> PEPCollection = clientDatabase.getCollection("PEP");
			MongoCollection<Document> PEPListCollection = clientDatabase.getCollection("AUPEP");

			long size = PEPCollection.count();

			int l = 0;

			for (; l < size; l += chunk) {

				List<Document> entities = PEPCollection.find(eq("addresses.address.country","AU")).skip(l).limit(chunk).into(new ArrayList<Document>());

				int count = 0;
				for (Document entity : entities) {

//					Document en = (Document) entity.get("entity");

					// PEPListCollection.insertMany(entities);

					PEPListCollection.insertOne(entity);
					count++;

					if (count % 1000 == 0) {
						System.out.println("number processed: " + (l + count));
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	
		
	}
	static void convertList() {
		// TODO Auto-generated method stub

		try {
			Util ut = new Util();

			mongoURL = ut.getPropValues("mongoURL");

			int chunk = 10000;

			setupConnection();

			MongoCollection<Document> PEPCollection = clientDatabase.getCollection("PEP1");
			MongoCollection<Document> PEPListCollection = clientDatabase.getCollection("PEP");

			long size = PEPCollection.count();

			int l = 0;

			for (; l < size; l += chunk) {

				List<Document> entities = PEPCollection.find().skip(l).limit(chunk).into(new ArrayList<Document>());

				int count = 0;
				for (Document entity : entities) {

					Document en = (Document) entity.get("entity");

					// PEPListCollection.insertMany(entities);

					PEPListCollection.insertOne(en);
					count++;

					if (count % 1000 == 0) {
						System.out.println("number processed: " + (l + count));
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	static void addEUList() {


		// TODO Auto-generated method stub

		try {
			Util ut = new Util();

			mongoURL = ut.getPropValues("mongoURL");

			setupConnection();

			Document list = new Document().append("id", 101).append("name", "EU").append("filename", "EU.json")
					.append("date", new Date()).append("creator", "tester").append("description", "EU List")
					.append("size", 2241);

			MongoCollection<Document> listCollection = clientDatabase.getCollection("list");

			listCollection.insertOne(list);

		} catch (Exception e) {
			e.printStackTrace();
		}

	
	}
	static void addUNList() {

		// TODO Auto-generated method stub

		try {
			Util ut = new Util();

			mongoURL = ut.getPropValues("mongoURL");

			setupConnection();

			Document list = new Document().append("id", 100).append("name", "UN").append("filename", "UN.json")
					.append("date", new Date()).append("creator", "tester").append("description", "UN List")
					.append("size", 1021);

			MongoCollection<Document> listCollection = clientDatabase.getCollection("list");

			listCollection.insertOne(list);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	static void addOFACList() {

		// TODO Auto-generated method stub

		try {
			Util ut = new Util();

			mongoURL = ut.getPropValues("mongoURL");

			setupConnection();

			Document list = new Document().append("id", 0).append("name", "OFAC").append("filename", "OFAC.json")
					.append("date", new Date()).append("creator", "tester").append("description", "OFAC List")
					.append("size", 6166);

			MongoCollection<Document> listCollection = clientDatabase.getCollection("list");

			listCollection.insertOne(list);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	static void addAUPEPList() {

		// TODO Auto-generated method stub

		try {
			Util ut = new Util();

			mongoURL = ut.getPropValues("mongoURL");

			setupConnection();

			Document list = new Document().append("id", 1050).append("name", "AUPEP").append("filename", "AUPEP.json")
					.append("date", new Date()).append("creator", "tester").append("description", "AU PEP List")
					.append("size", 19405);

			MongoCollection<Document> listCollection = clientDatabase.getCollection("list");

			listCollection.insertOne(list);

		} catch (Exception e) {
			e.printStackTrace();
		}

	
	}

	static void addDomesticPEPList() {

		// TODO Auto-generated method stub

		try {
			Util ut = new Util();

			mongoURL = ut.getPropValues("mongoURL");

			setupConnection();

			Document list = new Document().append("id", 1051).append("name", "DomesticPEP").append("filename", "DomesticPEP.json")
					.append("date", new Date()).append("creator", "tester").append("description", "AU PEP List")
					.append("size", 19405);

			MongoCollection<Document> listCollection = clientDatabase.getCollection("list");

			listCollection.insertOne(list);

		} catch (Exception e) {
			e.printStackTrace();
		}

	
	}
static void addList() {
		// TODO Auto-generated method stub

		try {
			Util ut = new Util();

			mongoURL = ut.getPropValues("mongoURL");

			setupConnection();

			Document list = new Document().append("id", 1020).append("name", "PEP").append("filename", "PEP.json")
					.append("date", new Date()).append("creator", "tester").append("description", "Full PEP List")
					.append("size", 1300080);

			MongoCollection<Document> listCollection = clientDatabase.getCollection("list");

			listCollection.insertOne(list);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
