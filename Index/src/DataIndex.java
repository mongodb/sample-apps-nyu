import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;

import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;

public class DataIndex {
	
	public List<Document> GetShippingByCityWithoutIndex(String connectionString, String city)
	{

		if(connectionString == null || connectionString.isEmpty() || city.isEmpty())
		{
			throw new IllegalArgumentException();
		}
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
		
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");		
			
			//runs a query without index
			List<Document> noIndexQuery = collection.find(and(eq("shippingAddress.state",city),gt("tax",50))).into(new ArrayList<Document>());
			return noIndexQuery;
		}
		catch (Exception e) {
			//log the exception
			
<<<<<<< HEAD
			System.out.println("An exception occured");
			System.out.println("Details:");
			e.printStackTrace();
			return null;
							
		}		
	}
	
	public List<Document> GetShippingByCityWithIndex(String connectionString, String city)
	{

		if(connectionString == null || connectionString.isEmpty() || city.isEmpty())
		{
			throw new IllegalArgumentException();
		}
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
		
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");		
			
			//create index
			collection.createIndex(Indexes.ascending("shippingAddress.state"));
		
			//runs a query with index
			List<Document> noIndexQuery = collection.find(and(eq("shippingAddress.state",city),gt("tax",50))).into(new ArrayList<Document>());
			return noIndexQuery;
		
=======
			//check the current indexes
			for (Document index : collection.listIndexes()) {
			    System.out.println(index.toJson());
			}
			
			//runs a query without index
			Document noIndexQuery =  collection.find(and(eq("shippingAddress.state",city),gt("tax",50))).modifiers(new Document("$explain",true)).first();
			System.out.println(noIndexQuery);
				
			//create index
			collection.createIndex(Indexes.ascending("shippingAddress.state"));
			//check the current indexes
			for (Document index : collection.listIndexes()) {
				   System.out.println(index.toJson());
			}
			
			
			//runs a query with index	
			Document indexQuery =  collection.find(and(eq("shippingAddress.state",city),gt("tax",50))).modifiers(new Document("$explain", true)).first();
			System.out.println(indexQuery);
			
			
			//drop the index
			collection.dropIndex(new BasicDBObject("shippingAddress.state", 1));
			
			
			//checking what all are indexes
			for (Document index : collection.listIndexes()) {
			    System.out.println(index.toJson());
			}
			
			
			return null;
>>>>>>> b50f0132bd90d4173eb2ee998c3627d8cd5910c1
			
		}
		catch (Exception e) {
			//log the exception
			
			System.out.println("An exception occured");
			System.out.println("Details:");
			e.printStackTrace();
			return null;
							
		}		
	}
	
	public int GetShippingByCityIndexSize(String connectionString, String city)
	{
		int line =0;
		if(connectionString == null || connectionString.isEmpty() || city.isEmpty())
		{
			throw new IllegalArgumentException();
		}
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
		
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");		
		
			String queryResult = null;
			//check the current indexes
			for (Document index : collection.listIndexes()) {
			  // System.out.println(index.toJson());
			   line++;
			   queryResult = index.toJson();
			}
		}
		return line;
	}
	
	public int dropIndex(String connectionString, String city)
	{
		if(connectionString == null || connectionString.isEmpty() || city.isEmpty())
		{
			throw new IllegalArgumentException();
		}
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		
		try(MongoClient client = new MongoClient(clientUri))
		{
		MongoDatabase database = client.getDatabase("stores");
		MongoCollection<Document> collection = database.getCollection("orders");		
		
		//drop the index
		collection.dropIndex(new BasicDBObject("shippingAddress.state", 1));
	
		}
		return 0;
		
	}
}