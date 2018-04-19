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

public class DataIndex {
	 public static void main(String[] args)
	{
	System.out.println("hello");
		//List<Document> res;
		String CONNECTION = "mongodb+srv://m001-student:student123#@sandbox-trhqa.mongodb.net/test";
		String city = "Texas";
		List<Document> filteredDocuments = GetShippingByCity(CONNECTION,city);
	
	}
	
	public static List<Document> GetShippingByCity(String connectionString, String city)
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
			
			
			//checking what all are indexes
			for (Document index : collection.listIndexes()) {
			    System.out.println(index.toJson());
			}
			
			//runs a query without index
			Document query =  collection.find(and(eq("shippingAddress.state",city),gt("tax",50))).modifiers(new Document("$explain",true)).first();
				System.out.println(query);
				
			//create index
			collection.createIndex(Indexes.ascending("shippingAddress.state"));
			//checking what all are indexes
			for (Document index : collection.listIndexes()) {
				   System.out.println(index.toJson());
			}
			
			
			//runs a query with index	
			Document query2 =  collection.find(and(eq("shippingAddress.state",city),gt("tax",50))).modifiers(new Document("$explain", true)).first();
			
			System.out.println(query2);
			
			//drop the index
			collection.dropIndex(new BasicDBObject("shippingAddress.state", 1));
			
			
			//checking what all are indexes
			for (Document index : collection.listIndexes()) {
			    System.out.println(index.toJson());
			}
			
			
			return null;
			
		}
		catch (Exception e) {
			//log the exception
			
			System.out.println("An exception occured");
			System.out.println("Details:");
			System.out.println(e.getStackTrace());
			return null;
							
		}		
	}
}

