import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import java.util.Arrays;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.eq;


public class InsertData {
	/*  public static void main(String[] args)
	{
	System.out.println("hello");
		String CONNECTION = "mongodb+srv://m001-student:student123#@sandbox-trhqa.mongodb.net/test";
		List<Document> check = AddOneOrder(CONNECTION);//, String[] cities)
		List<Document> checkAll = AddMultipleOrders(CONNECTION);//, String[] cities)
		
		System.out.println(check);
		System.out.println(checkAll);
	}
	*/
	
	/**
	 * Inserts documents into the collection using insertOne()
	 * @param connectionString: To MongoDB instance/MongoDB Cluster.
	 * @param cities: array of Shipping address cities to filter documents.
	 * @return List<Document>: ArrayList of matching documents.
	 */
	public static List<Document> AddOneOrder(String connectionString)
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
        
	        Document newOrder = new Document("orderPlaced", "2018-03-01")
	        .append("total", 1633.2)
	        .append("subtotal",421.42)
			.append("shipping", 91)
			.append("tax", 11.41);
	
	     	List status = Arrays.asList("shipped","2018-03-10");
			newOrder.put("status", status);
	
			Document shipping = new Document ("number",113)
			.append("street", "Sunnyside")
			.append("city","Yonker")
			.append("state","New York")
			.append("country","USA")
			.append("postalCode",57850);
			newOrder.put("shippingAddress", shipping);
			
			
			Document itemDetail = new Document("sku","MDBTS285")
			.append("name","color pencil")
			.append("quantity",32)
			.append("unit_price",9.3808);
			List items = Arrays.asList(itemDetail);
			newOrder.put("lineitems", items);
			
			 collection.insertOne(newOrder);
			
			 
	        List<Document> queryResult = collection.find(eq("orderPlaced", "2018-03-01")).into(new ArrayList<Document>());
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
	
	
	public static List<Document> AddOneOrderJson(String connectionString)
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
			
			
			String json = "{ 'orderPlaced': '2018-03-01', 'total': 13.00, 'subtotal': 191.00,"
					+ "'shipping':11.00,'tax':17.00,"
					+ "'status': ['shipped', '2018-03-04'],"
					+ "'shippingAddress': {'number': 345, 'street': 'Broadway', 'city': 'Sunnyside',"
					+"'state':'Yonkers','country':'USA','postalCode':10010},"
					+"'lineitems':[{'sku':'MDBTS115','name':'color pencil','quantity':31,'unit_price':1.35},"
					+"{'sku':'MDBTS438','name':'Highlighter','quantity':27,'unit_price':2.4}]}";
					
					Document document = Document.parse(json);
					collection.insertOne(document);			

			         List<Document> queryResult = collection.find(eq("orderPlaced", "2018-03-02")).into(new ArrayList<Document>());
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
	
	public static List<Document> AddMultipleOrders(String connectionString)
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
			
       Document newOrders = new Document();
        
        Document newOrder = new Document("orderPlaced", "2018-03-01")
        .append("total", 1633.2)
        .append("subtotal",421.42)
		.append("shipping", 91)
		.append("tax", 11.41);

     	List status = Arrays.asList("shipped","2018-03-10");
		newOrder.put("status", status);

		Document shipping = new Document ("number",113)
		.append("street", "Sunnyside")
		.append("city","Yonker")
		.append("state","New York")
		.append("country","USA")
		.append("postalCode",57850);
		newOrder.put("shippingAddress", shipping);
		
		
		Document itemDetail = new Document("sku","MDBTS285")
		.append("name","color pencil")
		.append("quantity",32)
		.append("unit_price",9.3808);
		List items = Arrays.asList(itemDetail);
		newOrder.put("lineitems", items);
		
		
		  Document newOrder2 = new Document("orderPlaced", "2018-03-01")
	        .append("total", 1633.2)
	        .append("subtotal",421.42)
			.append("shipping", 91)
			.append("tax", 11.41);

	     	List status2 = Arrays.asList("shipped","2018-03-10");
			newOrder.put("status", status2);

			Document shipping2 = new Document ("number",113)
			.append("street", "Sunnyside")
			.append("city","Yonker")
			.append("state","New York")
			.append("country","USA")
			.append("postalCode",57850);
			newOrder.put("shippingAddress", shipping2);
			
			
			Document itemDetail2 = new Document("sku","MDBTS285")
			.append("name","color pencil")
			.append("quantity",32)
			.append("unit_price",9.3808);
			List items2 = Arrays.asList(itemDetail);
			newOrder.put("lineitems", items2);
		
		 collection.insertMany(Arrays.asList(newOrder,newOrder2));
		
		 
        List<Document> queryResult = collection.find(eq("orderPlaced", "2018-03-01")).into(new ArrayList<Document>());;
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
	
	public static List<Document> CountOrderSize(String connectionString)
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
			System.out.println(e.getStackTrace());
			return null;
							
		}		
	}

}

