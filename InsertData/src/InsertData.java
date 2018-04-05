import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.json.simple.JSONArray;
//import org.json.JSONObject;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.Arrays;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSONParseException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InsertData {
		/*  public static void main(String[] args)
	{
	System.out.println("hello");
		String CONNECTION = "mongodb+srv://m001-student:student123#@sandbox-trhqa.mongodb.net/test";
		//List<Document> check = AddOneOrder(CONNECTION);//, String[] cities)
		//List<Document> checkAll = AddMultipleOrders(CONNECTION);//, String[] cities)
	//	AddOneOrderJsonFile
		//System.out.println(check);
		//System.out.println(checkAll);
	
	List<Document> checkFile = AddMultipleOrderWithData(CONNECTION, sku,item, unit,quant);
	List<Document> checkFile2 = AddOneOrderWithData(CONNECTION, "a","b", 5.5,5);
	
	}
	*/
	
	/**
	 * Inserts documents into the collection using insertOne()
	 * @param connectionString: To MongoDB instance/MongoDB Cluster.
	 * @param sku: SKU number of the item ordered, item: name of the ide, unit_price: unit price of the item, quantity: number of items ordered
	 * @return List<Document>: ArrayList of matching documents.
	 */
	public static List<Document> AddOneOrderWithData(String connectionString, String sku, String item, Double  unit_price, int quantity)
	{
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		
		if(connectionString == null || connectionString.isEmpty() || sku.isEmpty() || item.isEmpty()|| unit_price==0.0 ||quantity ==0 )
		{
			System.out.println("ENTERS HERE");
			throw new IllegalArgumentException();
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String today = formatter.format(date);
		Double total = unit_price*quantity;
		Double subtotal = total/4;
		
		Document newOrder = new Document("orderPlaced", today)
        .append("total", total)
        .append("subtotal",subtotal)
		.append("shipping", 50)
		.append("tax", 12.50);

     	List status = Arrays.asList("shipped","2018-03-10");
		newOrder.put("status", status);

		Document shipping = new Document ("number",100)
		.append("street", "Sunnyside")
		.append("city","Yonker")
		.append("state","New York")
		.append("country","USA")
		.append("postalCode",10001);
		newOrder.put("shippingAddress", shipping);	
		
		Document itemDetail = new Document("sku",sku)
		.append("name",item)
		.append("quantity",quantity)
		.append("unit_price",unit_price);
		List items = Arrays.asList(itemDetail);
		newOrder.put("lineitems", items);
		
		try(MongoClient client = new MongoClient(clientUri))
		{
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");		
	    	collection.insertOne(newOrder);
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
	 * Inserts documents into the collection using insertOne()
	 * @param connectionString: To MongoDB instance/MongoDB Cluster.
	 * @param fileName: JSON file that contains the order details 
	 * @return List<Document>: ArrayList of matching documents.
	 */
	public static List<Document> AddOneOrderWithFile(String connectionString, String fileName)
	{
		if(connectionString == null || connectionString.isEmpty())
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
	        JSONObject jo = (JSONObject) obj;
	        String jsonText = jo.toJSONString();
	        Document doc = Document.parse(jsonText);
		    collection.insertOne(doc);
		    
	        List<Document> queryResult = collection.find().into(new ArrayList<Document>());
		    return queryResult;		   
        }	
		catch (FileNotFoundException e) {
		    e.printStackTrace();
		    return null;
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
	 * Inserts documents into the collection using insertOne()
	 * @param connectionString: To MongoDB instance/MongoDB Cluster.
	 * @param orders: Array of JSON file names that contain order details
	 * @return List<Document>: ArrayList of matching documents.
	 */
	public static List<Document> AddMultipleOrdersWithFiles(String connectionString, String[] orders)
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
			
			List<Document> multipleOrders = new ArrayList<>();
			
			//read the json file   
	        for(int i =0; i<orders.length; i++)
	        {
			Object obj = new JSONParser().parse(new FileReader(orders[i]));
	        JSONObject jo = (JSONObject) obj;
	        String jsonText = jo.toJSONString();
	        Document doc = Document.parse(jsonText);
	        multipleOrders.add(doc);
	        }
	        collection.insertMany(multipleOrders);
	        List<Document> queryResult = collection.find().into(new ArrayList<Document>());
			return queryResult;
		}
		catch (JSONParseException e) {
			  System.out.println("An exception occured");
				System.out.println("Details:");
				e.printStackTrace();
				return null;		
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
	 * Inserts documents into the collection using insertOne()
	 * @param connectionString: To MongoDB instance/MongoDB Cluster.
	 * @param sku: Array of SKU numbers of the item ordered, item: Arrays of name of the item, unit_price: Array of unit prices of the items, quantity: Array of number of items ordered
	 * @return List<Document>: ArrayList of matching documents.
	 */
	public static List<Document> AddMultipleOrderWithData(String connectionString, String[] sku, String[] item, Double[]  unit_price, int[] quantity)
	{
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		
		if(connectionString == null || connectionString.isEmpty() || sku.length ==0|| item.length ==0|| unit_price.length ==0 ||quantity.length ==0)
		{		
			throw new IllegalArgumentException();
		}
		
		try(MongoClient client = new MongoClient(clientUri))
		{
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");		
	    	Document[] newOrder = new Document[3];
			for(int i=0; i<sku.length; i++)
			{
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
				String today = formatter.format(date);
				Double total = unit_price[i]*quantity[i];
				Double subtotal = total/4;
				
				newOrder[i] = new Document("orderPlaced", today)
		        .append("total", total)
		        .append("subtotal",subtotal)
				.append("shipping", 50)
				.append("tax", 12.50);
	
		     	List status = Arrays.asList("shipped","2018-03-10");
				newOrder[i].put("status", status);
	
				Document shipping = new Document ("number",100)
				.append("street", "Sunnyside")
				.append("city","Yonker")
				.append("state","New York")
				.append("country","USA")
				.append("postalCode",10001);
				newOrder[i].put("shippingAddress", shipping);	
				
				Document itemDetail = new Document("sku",sku[i])
				.append("name",item[i])
				.append("quantity",quantity[i])
				.append("unit_price",unit_price[i]);
				List items = Arrays.asList(itemDetail);
				newOrder[i].put("lineitems", items);
			}
			
			collection.insertMany(Arrays.asList(newOrder[0],newOrder[1], newOrder[2]));
			
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
	 * Inserts documents into the collection using insertOne()
	 * @param connectionString: To MongoDB instance/MongoDB Cluster.
	 * @return List<Document>: ArrayList of matching documents.
	 */
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

