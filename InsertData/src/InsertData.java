import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import org.bson.Document;

import java.util.Arrays;
import java.util.List;

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
				/*
				 * 
{
  _id: ObjectId(),
  orderPlaced: datetime(),
  total: NumberDecimal("153.00"),
  subtotal: NumberDecimal("141.00"),
  shipping: NumberDecimal("5.00"),
  tax: NumberDecimal("7.00"),
  status: ["shipped", datetime()],
  shippingAddress: {
    number: 345,
    street: Alvin St.,
    city: Madison
    state: WI,
    country: USA
    postalCode: 53558
  },
  lineitems: [
    { sku: "MDBTS001",
      name: "Flannel T-shirt",
      quantity: 10,
      unit_price: NumberDecimal("9.00") },
    { sku: "MDBTS002",
      quantity: 5,
      unit_price: NumberDecimal("10.00")}] 
}

*/
        
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

