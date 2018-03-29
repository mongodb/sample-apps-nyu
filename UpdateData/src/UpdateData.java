import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.and;
//import static com.mongodb.client.model.Filters.lt;
//import static com.mongodb.client.model.Filters.gt;

public class UpdateData {

	/**
	 * Updates documents in the collection using updateOne()
	 * @param connectionString: To MongoDB instance/MongoDB Cluster.
	 * @param total: Total of the document that is to be updated.
	 * @param subtotal: The subtotal of the document whose total is passed is updated with this value.
	 * @return List<Document>: ArrayList of matching documents.
	 */
	
	public List<Document> UpdateOneOrder(String connectionString, Double total, Double subtotal)
	{

		if(connectionString == null || connectionString.isEmpty() || total <= 0 || subtotal <= 0)
		{
			throw new IllegalArgumentException();
		}
		
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
		
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");	
	
			Document newDocument = new Document();
			newDocument.append("$set", new Document().append("subtotal", subtotal));

			Document updateQuery = new Document().append("total", total);

			collection.updateOne(updateQuery, newDocument);
			
	        List<Document> queryResult = collection.find(and(eq("total",total),eq("subtotal",subtotal))).into(new ArrayList<Document>());
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
	 * Updates documents in the collection using updateOne()
	 * @param connectionString: To MongoDB instance/MongoDB Cluster.
	 * @param sku: lineitems - sku of the document that is to be updated.
	 * @param name: The lineitems - name of the document whose sku is passed is updated with this value.
	 * @return List<Document>: ArrayList of matching documents.
	 */
	
	public List<Document> UpdateOneOrderForEmbeddedField(String connectionString, String sku, String name)
	{

		if(connectionString == null || connectionString.isEmpty() || sku == null || sku.isEmpty() || name == null || name.isEmpty())
		{
			throw new IllegalArgumentException();
		}
		
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
		
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");	
	
			Document newDocument = new Document();
			newDocument.append("$set", new Document().append("lineitems.name", name));

			Document updateQuery = new Document().append("lineitems.sku", sku);

			collection.updateOne(updateQuery, newDocument);
			
	        List<Document> queryResult = collection.find(and(eq("lineitems.sku", sku),eq("lineitems.name", name))).into(new ArrayList<Document>());
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
	 * Updates documents in the collection using updateOne()
	 * @param connectionString: To MongoDB instance/MongoDB Cluster.
	 * @param total: Total of the document that is to be updated.
	 * @param tax: The tax of the document whose total is greater than the total passed is updated with this value.
	 * @return List<Document>: ArrayList of matching documents.
	 */
	
	public List<Document> UpdateManyOrdersWithOperator(String connectionString, Double total, Double tax)
	{

		if(connectionString == null || connectionString.isEmpty() || total <= 0 || tax <= 0)
		{
			throw new IllegalArgumentException();
		}
		
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
		
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");	
	
			Document newDocument = new Document();
			newDocument.append("$set", new Document().append("tax", tax));
			
			Document updateQuery = new Document();
			updateQuery.append("total", new Document("$gt", total));

			collection.updateMany(updateQuery, newDocument);
			
	        List<Document> queryResult = collection.find(and(eq("total",total),eq("tax",tax))).into(new ArrayList<Document>());
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
	 * Updates documents in the collection using updateOne()
	 * @param connectionString: To MongoDB instance/MongoDB Cluster.
	 * @param sku: lineitems - sku of the document that is to be updated.
	 * @param unit_price: The lineitems - unit_price of the document whose sku is passed is updated with this value.
	 * @return List<Document>: ArrayList of matching documents.
	 */
	
	public List<Document> UpdateManyOrdersForEmbeddedField(String connectionString, String sku, Double unit_price)
	{

		if(connectionString == null || connectionString.isEmpty() || sku == null || sku.isEmpty() || unit_price <= 0)
		{
			throw new IllegalArgumentException();
		}
		
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
		
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");	
	
			Document newDocument = new Document();
			newDocument.append("$set", new Document().append("lineitems.unit_price", unit_price));
			
			Document updateQuery = new Document();
			updateQuery.append("lineitems.sku", sku);

			collection.updateMany(updateQuery, newDocument);
			
	        List<Document> queryResult = collection.find(and(eq("lineitems.sku", sku),eq("lineitems.unit_price", unit_price))).into(new ArrayList<Document>());
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