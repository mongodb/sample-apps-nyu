import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Projections.*;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Filters.all;
import static com.mongodb.client.model.Filters.or;
import static com.mongodb.client.model.Filters.elemMatch;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.lt;

import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Projections.excludeId;



public class arrayQuery {
	public static void main(String[] args)
	{
		System.out.println("hello");

		String CONNECTION = "mongodb+srv://m001-student:student123#@sandbox-trhqa.mongodb.net/test";
		List<Document> res =InOperator(CONNECTION);
		for(int i =0; i<res.size(); i++)
		System.out.println(res.get(i));
		
		res =AllOperator(CONNECTION);
		for(int i =0; i<res.size(); i++)
		System.out.println(res.get(i));
		
		res =ArrayEqualityOperator(CONNECTION);
		for(int i =0; i<res.size(); i++)
		System.out.println(res.get(i));
		
		res = ElemMatchOperator(CONNECTION);
		for(int i =0; i<=res.size(); i++)
		System.out.println(res.get(i));
	}
	
	
	
	public static List<Document> InOperator(String connectionString)
	{
		System.out.println("entered");
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");		
			
			//List<Document> ordersToWI = collection.find(eq("shippingAddress.city","{$in: [ 'Floris', 'Calpine', 'Orason', 'Allentown']}}")).into(new ArrayList<Document>());
			List<Document> queryResult = collection.find(in("shippingAddress.city","Floris", "Calpine", "Orason", "Allentown")).projection(fields(include("subtotal","shipping","shippingAddress.city"),excludeId())).into(new ArrayList<Document>());
			
			System.out.println("-------");
			return queryResult;
		}
		catch (Exception e) {
			//log the exception
			return null;
		}
		
		
	}
	
	public static List<Document> AllOperator(String connectionString)
	{
		
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");		
			
			List<Document> queryResult = collection.find(all("lineitems.name","omak 2 Door Mobile Cabinet","Glue Sticks")).projection(fields(include("subtotal","lineitems.name","shippingAddress.city"),excludeId())).into(new ArrayList<Document>());
			return queryResult;
		}
		catch (Exception e) {
			//log the exception
			return null;
		}
		
		
	}
	
	public static List<Document> ArrayEqualityOperator(String connectionString)
	{
		
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");		
			
			List<Document> queryResult = collection.find(eq("status","shipped")).into(new ArrayList<Document>());
		  // ordersToWI = collection.find(eq("lineitems.name","Table Saw")).into(new ArrayList<Document>());
		
		return queryResult;
		}
		catch (Exception e) {
			//log the exception
			return null;
		}
		
		
	}
	
	public static List<Document> ElemMatchOperator(String connectionString)
	{
		//option 1
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
			MongoDatabase database = client.getDatabase("stores");
			
			MongoCollection<Document> collection = database.getCollection("orders");		
			
			List<Document> queryResult = collection.find(elemMatch("lineitems",and(eq("name","Glue Sticks"),gt("quantity",30)))).into(new ArrayList<Document>());
			
			return queryResult;
		}
		catch (Exception e) {
			//log the exception
			return null;
		}
		
		
	}
}

