import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
//import org.json.JSONObject;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.Arrays;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;

public class InsertData {
	/*	  public static void main(String[] args)
	{
	System.out.println("hello");
		/*String CONNECTION = "mongodb+srv://m001-student:student123#@sandbox-trhqa.mongodb.net/test";
		List<Document> check = AddOneOrder(CONNECTION);//, String[] cities)
		List<Document> checkAll = AddMultipleOrders(CONNECTION);//, String[] cities)
		AddOneOrderJsonFile
		System.out.println(check);
		System.out.println(checkAll);
	
	String CONNECTION = "mongodb+srv://m001-student:student123#@sandbox-trhqa.mongodb.net/test";
	List<Document> checkFile = AddOneOrderJsonFile(CONNECTION);
	//List<Document> checkFile2 = AddMultipleOrder(CONNECTION);
	System.out.println(checkFile);
	}*/
	
	
	/**
	 * Inserts documents into the collection using insertOne()
	 * @param connectionString: To MongoDB instance/MongoDB Cluster.
	 * @param addOrder: Json data in String format
	 * @return List<Document>: ArrayList of matching documents.
	 */
	public static List<Document> AddOneOrderWithData(String connectionString, Document addOrder)
	{
		if(connectionString == null || connectionString.isEmpty() )
		{
			throw new IllegalArgumentException();
		}
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");		
	    			
			collection.insertOne(addOrder);
			List<Document> queryResult = collection.find().into(new ArrayList<Document>());
			
			
			
			return queryResult;
		}
		catch (Exception e) {
			//log the exception	
			System.out.println("An exception occured");
			System.out.println("Details:");
			e.printStackTrace();
			return null;
		}		
	}
	
	/**
	 * Read a JSON file to inserts a document into the collection using insertOne()
	 * @param connectionString: To MongoDB instance/MongoDB Cluster.
	 * @param AddOrder: A Json File name.
	 * @return List<Document>: ArrayList of matching documents.
	 */
	
	public static List<Document> AddOneOrderWithExpData(String connectionString, String AddOrder)
	{
		if(connectionString == null || connectionString.isEmpty() )
		{
			throw new IllegalArgumentException();
		}
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
		
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");		
					
			Document document = Document.parse(AddOrder);
			collection.insertOne(document);			
			
			List<Document> queryResult = collection.find().into(new ArrayList<Document>());
			return queryResult;
		}
		
		catch (Exception e) {
			//log the exception
			
			System.out.println("An exception occured");
			System.out.println("Details:");
			e.printStackTrace();
			return null;
		}	
	}
	
	
	
	public static List<Document> AddOneOrderJsonFile(String connectionString, String fileName)
	{
		if(connectionString == null || connectionString.isEmpty() )
		{
			throw new IllegalArgumentException();
		}
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try (MongoClient client = new MongoClient(clientUri))
		{
		   
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");		
			
			//read the json file   
	        Object obj = new JSONParser().parse(new FileReader(fileName));
	        System.out.println("DONE");
	        System.out.println(obj);
	                
	        JSONObject jo = (JSONObject) obj;
	        String jsonText = jo.toJSONString();
	        Document doc = Document.parse(jsonText);
		    collection.insertOne(doc);
		          
		     List<Document> queryResult = collection.find().into(new ArrayList<Document>());
			 return queryResult;
	        
        }
		catch (Exception e) {
			//log the exception
			
			System.out.println("An exception occured");
			System.out.println("Details:");
			e.printStackTrace();
			return null;
							
		}
			
	}
	
	public List<Document> CountOrderSize(String connectionString)
	{

		if(connectionString == null || connectionString.isEmpty() )
		{
			throw new IllegalArgumentException();
		}
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
		
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");		
				
			List<Document> queryResult = collection.find().into(new ArrayList<Document>());
			
			return queryResult;
			
		}
		catch (Exception e) {
			//log the exception
			
			System.out.println("An exception occured");
			System.out.println("Details:");
			e.printStackTrace();
			return null;
							
		}		
	}

	

}

