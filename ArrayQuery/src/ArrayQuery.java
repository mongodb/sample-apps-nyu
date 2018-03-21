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
import static com.mongodb.client.model.Filters.elemMatch;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Projections.excludeId;


public class ArrayQuery {
	/*  public static void main(String[] args)
	{
	System.out.println("hello");
		//List<Document> res;
		String CONNECTION = "mongodb+srv://m001-student:student123#@sandbox-trhqa.mongodb.net/test";
		//String[] cities = {"Floris", "Calpine","Orason", "Allentown"};
		//String[] cities = {};
	//	List<Document> res =FindByItemQuantity(CONNECTION, "", 30);		   
		//List<Document> res =GetShippingByCity(CONNECTION,cities);
		//System.out.println(res.size());
		
		if(res.size()==0)
		{System.out.println("error");
		
		}
		else
			{
			for(int i =0; i<res.size(); i++)
			
		{
			
			System.out.println(res.get(i));
		}
			}
		
	}
	*/
	
	/**
	 * Retrieves documents filtered by shipping address city. use of $in
	 * @param connectionString: To MongoDB instance/MongoDB Cluster.
	 * @param cities: array of Shipping address cities to filter documents.
	 * @return List<Document>: ArrayList of matching documents.
	 */
	public static List<Document> GetShippingByCity(String connectionString, String[] cities)
	{

		if(connectionString == null || connectionString.isEmpty() || cities.length<=0)
		{
			throw new IllegalArgumentException();
		}
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
		
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");		
				
			List<Document> queryResult = collection.find(in("shippingAddress.city",cities))
					.projection(fields(include("subtotal","shipping","shippingAddress.city")))
					.into(new ArrayList<Document>());
			
			return queryResult;
			
		}
		catch (Exception e) {
			//log the exception
			
			System.out.println("An exception occured");
			System.out.println("Details:");
			System.out.println(e.getStackTrace());
			return null;
							
		}		
	}
	/**
	 * Retrieves documents filtered by shipping items. use of $all
	 * @param connectionString: To MongoDB instance/MongoDB Cluster.
	 * @param items: array of items to filter documents.
	 * @return List<Document>: ArrayList of matching documents.
	 */
	public List<Document> GetShippingByItem(String connectionString,String[] items)
	{
		if(connectionString == null || connectionString.isEmpty() || items.equals(null) ||items.length<=0)
		{
			throw new IllegalArgumentException();
		}
		
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");		
		
			List<Document> queryResult = collection.find(all("lineitems.name",items))
					.projection(fields(include("subtotal","lineitems.name","shippingAddress.city")))
					.into(new ArrayList<Document>());
			
			return queryResult;
			
		}
		catch (Exception e) {
			//log the exception
			
			System.out.println("An exception occured");
			System.out.println("Details:");
			System.out.println(e.getStackTrace());
			return null;
							
		}		
	}
	
	/**
	 * Retrieves documents filtered by status to retrieve data from arrays
	 * @param connectionString: To MongoDB instance/MongoDB Cluster.
	 * @param status: string value of status to filter documents.
	 * @return List<Document>: ArrayList of matching documents.
	 */

	
	public static List<Document> FindByStatus(String connectionString, String status)
	{
		if(connectionString == null || connectionString.isEmpty() || status==null|| status.isEmpty())
		{
			throw new IllegalArgumentException();
		}
	    
		MongoClientURI clientUri = new MongoClientURI(connectionString);
	    try(MongoClient client = new MongoClient(clientUri))
	    {
	    	MongoDatabase database = client.getDatabase("stores");
	    	MongoCollection<Document> collection = database.getCollection("orders");        
	        
	    	List<Document> queryResult = collection.find(eq("status",status))
	    			.into(new ArrayList<Document>());
          
	        return queryResult;
	     }
	     
	    catch (Exception e) 
	    {
	            //log the exception
	    	System.out.println("An exception occured");
	        System.out.println("Details:");
	        System.out.println(e.getStackTrace());
	        return null;                            
	     }    
	 }
	
	/**
	 * Retrieves documents filtered by items and their quantity. use of $elemMatch
	 * @param connectionString: To MongoDB instance/MongoDB Cluster.
	 * @param item_name: item name to filter documents.
	 * @param quantity: integer value to filter documents that greater than this value.
	 * @return List<Document>: ArrayList of matching documents.
	 */

	
	public List<Document> FindByItemQuantity(String connectionString, String item_name, int quantity)
    {
        if(connectionString == null || connectionString.isEmpty() || item_name.isEmpty() || item_name ==null || quantity <=0)
        {
        	throw new IllegalArgumentException();
        }
        MongoClientURI clientUri = new MongoClientURI(connectionString);
        
        try(MongoClient client = new MongoClient(clientUri))
        {
                MongoDatabase database = client.getDatabase("stores");
                MongoCollection<Document> collection = database.getCollection("orders");        
            
                List<Document> queryResult = collection.find(elemMatch("lineitems",and
                    (eq("name",item_name),gt("quantity",quantity))))
                    .into(new ArrayList<Document>());
            
                System.out.println(queryResult);
                return queryResult;
        }
        catch (Exception e) 
        {
            //log the exception
           System.out.println("An exception occured");
           System.out.println("Details:");
           System.out.println(e.getStackTrace());
           return null;
                            
        }            
    }
}

