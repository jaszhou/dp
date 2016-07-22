package com.ibm.wcts;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class BlogPostDAO {
	MongoCollection<Document> postsCollection;

	public BlogPostDAO(final MongoDatabase blogDatabase) {
		postsCollection = blogDatabase.getCollection("posts");
	}

	// Return a single post corresponding to a permalink
	public Document findByPermalink(String permalink) {

		// XXX HW 3.2, Work Here
		Document post = null;

		post =  postsCollection.find(eq("permalink",permalink)).first();

		return post;
	}

	public Document findAlertByID(String id) {

		// long id = Long.valueOf(profileid).longValue();
		// posts = postsCollection.find().sort(new
		// Document("date",-1)).limit(limit).into(new ArrayList<Document>());
		// post = postsCollection.find(eq("permalink",permalink)).first();
		
//		System.out.println("find: "+id);
		
		
		
		return postsCollection.find(eq("_id", new ObjectId(id))).first();
		
//		return usersCollection.find(eq("_id", username)).first();

	}
	
	public String addPost(String title, String body, List tags, String username,String action,String project,String production,String status) {

		System.out.println("inserting blog entry " + title + " " + body);

		String permalink = title.replaceAll("\\s", "_"); // whitespace becomes _
		permalink = permalink.replaceAll("\\W", ""); // get rid of non
														// alphanumeric
		permalink = permalink.toLowerCase();

		// XXX HW 3.2, Work Here
		// Remember that a valid post has the following keys:
		// author, body, permalink, tags, comments, date
		//
		// A few hints:
		// - Don't forget to create an empty list of comments
		// - for the value of the date key, today's datetime is fine.
		// - tags are already in list form that implements suitable interface.
		// - we created the permalink for you above.

		// Build the post object and insert it
		Document post = new Document();

		// DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		// Date date = new Date();
		// System.out.println(dateFormat.format(date));

		ArrayList<String> authors = new ArrayList();
		authors.add(username);
		
		post.append("title", title).append("author", username).append("body", body).append("permalink", permalink)
				.append("tags", tags).append("comments", new ArrayList()).append("date", new Date())
				.append("action", action).append("project", project).append("production", production).append("status", status);

		try {
			postsCollection.insertOne(post);

		} catch (MongoWriteException e) {
			e.printStackTrace();
		}

		return permalink;
	}
	
	public boolean updatePost(String id,Document postUpdate) {
		
		postsCollection.findOneAndUpdate(new Document("_id", new ObjectId(id)), new Document("$set", postUpdate));

		return true;
	
		
	}
	
	public List<Document> findProd(int limit) {

		// XXX HW 3.2, Work Here
		// Return a list of DBObjects, each one a post from the posts collection
		
//		Date start = new java.util.Date();
//		
//		Calendar now = Calendar.getInstance();
//	    System.out.println("Current date : " + (now.get(Calendar.MONTH) + 1) + "-"
//	        + now.get(Calendar.DATE) + "-" + now.get(Calendar.YEAR));
//
//	    // add days to current date using Calendar.add method
//	    now.add(Calendar.DATE, 1);
//	    
	    GregorianCalendar gc=new GregorianCalendar();
	    gc.add(GregorianCalendar.DAY_OF_MONTH, 0);
	    
	    gc.set(GregorianCalendar.HOUR_OF_DAY, 0);
	    gc.set(GregorianCalendar.MINUTE, 0);
	    gc.set(GregorianCalendar.SECOND, 0);
	    gc.set(GregorianCalendar.MILLISECOND,0);
	    
	    Date start = gc.getTime();
	    
	    
		            
		List<Document> posts = null;
		    
		posts = postsCollection.find(and(gte("date",start),eq("production","Yes"))).sort(new Document("date",-1).append("production",-1)).limit(limit).into(new ArrayList<Document>());
		
		return posts;
	}
	
	public List<Document> findNonProd(int limit) {

		// XXX HW 3.2, Work Here
		// Return a list of DBObjects, each one a post from the posts collection
		
//		Date start = new java.util.Date();
//		
//		Calendar now = Calendar.getInstance();
//	    System.out.println("Current date : " + (now.get(Calendar.MONTH) + 1) + "-"
//	        + now.get(Calendar.DATE) + "-" + now.get(Calendar.YEAR));
//
//	    // add days to current date using Calendar.add method
//	    now.add(Calendar.DATE, 1);
//	    
	    GregorianCalendar gc=new GregorianCalendar();
	    gc.add(GregorianCalendar.DAY_OF_MONTH, 0);
	    
	    gc.set(GregorianCalendar.HOUR_OF_DAY, 0);
	    gc.set(GregorianCalendar.MINUTE, 0);
	    gc.set(GregorianCalendar.SECOND, 0);
	    gc.set(GregorianCalendar.MILLISECOND,0);
	    
	    Date start = gc.getTime();
	    
	    
		            
		List<Document> posts = null;
		    
		posts = postsCollection.find(and(gte("date",start),eq("production","No"))).sort(new Document("date",-1).append("production",-1)).limit(limit).into(new ArrayList<Document>());
		
		return posts;
	}
	
	// Return a list of posts in descending order. Limit determines
	// how many posts are returned.
	public List<Document> findByDateDescending(int limit) {

		// XXX HW 3.2, Work Here
		// Return a list of DBObjects, each one a post from the posts collection
		List<Document> posts = null;
		    
		posts = postsCollection.find().sort(new Document("date",-1)).limit(limit).into(new ArrayList<Document>());
		
		return posts;
	}

	public String addPost(String title, String body, List tags, String username) {

		System.out.println("inserting blog entry " + title + " " + body);

		String permalink = title.replaceAll("\\s", "_"); // whitespace becomes _
		permalink = permalink.replaceAll("\\W", ""); // get rid of non
														// alphanumeric
		permalink = permalink.toLowerCase();

		// XXX HW 3.2, Work Here
		// Remember that a valid post has the following keys:
		// author, body, permalink, tags, comments, date
		//
		// A few hints:
		// - Don't forget to create an empty list of comments
		// - for the value of the date key, today's datetime is fine.
		// - tags are already in list form that implements suitable interface.
		// - we created the permalink for you above.

		// Build the post object and insert it
		Document post = new Document();

		// DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		// Date date = new Date();
		// System.out.println(dateFormat.format(date));

		ArrayList<String> authors = new ArrayList();
		authors.add(username);
		
		post.append("title", title).append("author", username).append("body", body).append("permalink", permalink)
				.append("tags", tags).append("comments", new ArrayList()).append("date", new Date());

		try {
			postsCollection.insertOne(post);

		} catch (MongoWriteException e) {
			e.printStackTrace();
		}

		return permalink;
	}

	// White space to protect the innocent

	// Append a comment to a blog post
	public void addPostComment(final String name, final String email, final String body, final String permalink) {

		// XXX HW 3.3, Work Here
		// Hints:
		// - email is optional and may come in NULL. Check for that.
		// - best solution uses an update command to the database and a suitable
		// operator to append the comment on to any existing list of comments
		Document post = findByPermalink(permalink);
		
		DBObject update=new BasicDBObject();
		DBObject comment=new BasicDBObject("author",name).append("body", body);
		
		if(email!=null) 
			comment=new BasicDBObject("author",name).append("body", body).append("email", email);
		
		update.put("$push", new Document("comments",comment));
		
		Object id = post.get("_id");
		System.out.println(id);
		
		postsCollection.updateOne(new Document("_id",id), (Bson)update);
		//postsCollection.update(post, update);
	}
}
