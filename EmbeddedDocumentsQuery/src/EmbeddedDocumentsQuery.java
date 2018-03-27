import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
//import com.mongodb.client.model.Projections;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.gt;
//import static com.mongodb.client.model.Filters.lt;
//import static com.mongodb.client.model.Filters.gte;
//import static com.mongodb.client.model.Filters.lte;
//import static com.mongodb.client.model.Projections.fields;
//import static com.mongodb.client.model.Projections.include;
//import static com.mongodb.client.model.Projections.excludeId;

public class EmbeddedDocumentsQuery {
	
	/**
	 * Retrieves documents filtered by shipping address - city.
	 * @param connectionString: To MongoDB instance/MongoDB Cluster.
	 * @param city: Shipping address - city to filter documents.
	 * @return List<Document>: ArrayList of matching documents.
	 */
	
	public List<Document> FindOrdersByCity(String connectionString, String city)
	{
		if(connectionString == null || connectionString.isEmpty() || city==null|| city.isEmpty())
			{
				throw new IllegalArgumentException();
			}
		
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
			MongoDatabase database = client.getDatabase("stores");			
			MongoCollection<Document> collection = database.getCollection("orders");		
			
			List<Document> queryQrders = collection
					.find(eq("shippingAddress.city",city))
					.into(new ArrayList<Document>());
			
			return queryQrders;
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
	 * Retrieves documents filtered by shipping address - state and city.
	 * @param connectionString: To MongoDB instance/MongoDB Cluster.
	 * @param state: Shipping address - state to filter documents.
	 * @param city: Shipping address - city to filter documents.
	 * @return List<Document>: ArrayList of matching documents.
	 */
	
	public List<Document> FindOrdersByStateAndCity(String connectionString, String state, String city)
	{
        if(connectionString == null || connectionString.isEmpty() || state.isEmpty() || state ==null || city.isEmpty() || city ==null)
        	{
        		throw new IllegalArgumentException();
        	}
        
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
			MongoDatabase database = client.getDatabase("stores");			
			MongoCollection<Document> collection = database.getCollection("orders");		
			
			List<Document> queryQrders = collection
					.find(and(eq("shippingAddress.state",state),eq("shippingAddress.city",city)))
					.into(new ArrayList<Document>());
			
			return queryQrders;
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
	 * Retrieves documents filtered by lineitems - unit_price. Use of gt
	 * @param connectionString: To MongoDB instance/MongoDB Cluster.
	 * @param unit_price: lineitems - unit_price to filter documents.
	 * @return List<Document>: ArrayList of matching documents.
	 */
	 
	public List<Document> FindOrdersByUnitPrice(String connectionString, Double unit_price)
	{
		if(connectionString == null || connectionString.isEmpty() || unit_price < 0)
			{
				throw new IllegalArgumentException();
			}
		
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");		
			
			List<Document> queryOrders = collection
					.find((gt("lineitems.unit_price",unit_price)))
					.into(new ArrayList<Document>());
			
			return queryOrders;
		}
		catch (Exception e) 
		{
			//log the exception
			System.out.println("Exception occured");
			System.out.println("Deatils:");
			System.out.println(e.getStackTrace());
			return null;
		}		
	}
	
	/**
	 * Retrieves documents filtered by lineitems - sku and postalcode.
	 * @param connectionString: To MongoDB instance/MongoDB Cluster.
	 * @param postalcode: postalcode to filter documents.
	 * @param sku: lineitems - sku to filter documents.
	 * @return List<Document>: ArrayList of matching documents.
	 */
	
	public List<Document> FindOrdersByPostalCodeAndSku(String connectionString, int postalcode, String sku)
	{
		if(connectionString == null || connectionString.isEmpty() || sku.isEmpty() || sku ==null|| postalcode < 0)
			{
				throw new IllegalArgumentException();
			}
		
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");		
			
			List<Document> queryOrders = collection
					.find(and(eq("shippingAddress.postalCode",postalCode),eq("lineitems.sku",sku)))
					.into(new ArrayList<Document>());
			
			return queryOrders;
		}
		catch (Exception e) 
		{
			//log the exception
			System.out.println("Exception occured");
			System.out.println("Deatils:");
			System.out.println(e.getStackTrace());
			return null;
		}		
	}
}
