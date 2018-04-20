import java.util.Date;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.lt;

public class RemoveData {
	
	/**
	 * Removes all the cancelled orders older than the given date.
	 * @param connectionString: To MongoDB instance/MongoDB Cluster.
	 * @param thresholdDate: to filter documents for deletion.
	 * @return long: Count of deleted documents..
	 */
	public long RemoveAllCancelledOrders(String connectionString, Date thresholdDate){
		if(connectionString == null || connectionString.isEmpty() || thresholdDate == null)
		{
			throw new IllegalArgumentException();
		}
		else
		{
			MongoClientURI clientUri = new MongoClientURI(connectionString);
			try(MongoClient client = new MongoClient(clientUri))
			{
				MongoDatabase database = client.getDatabase("stores");			
				MongoCollection<Document> collection = database.getCollection("orders");
				
				DeleteResult deletedOrders = collection.deleteMany(and(eq("status","cancelled"),lt("orderPlaced", thresholdDate)));
				System.out.println("Number of Orders removed: "+deletedOrders.getDeletedCount());
				return deletedOrders.getDeletedCount();								
			}
			catch (Exception e) {
				System.out.println("Exception occured");
				System.out.println("Details:");
				System.out.println(e.getStackTrace());
				return -1;
			}	
		}			
	}
	
	/**
	 * Removes one cancelled order older than the given date.
	 * @param connectionString: To MongoDB instance/MongoDB Cluster.
	 * @param thresholdDate: to filter documents for deletion.
	 * @return long: Count of deleted documents.
	 */
	public long RemoveOneCancelledOrder(String connectionString, Date thresholdDate){
		if(connectionString == null || connectionString.isEmpty() || thresholdDate == null )
		{
			throw new IllegalArgumentException();
		}
		else
		{
			MongoClientURI clientUri = new MongoClientURI(connectionString);
			try(MongoClient client = new MongoClient(clientUri))
			{
				MongoDatabase database = client.getDatabase("stores");			
				MongoCollection<Document> collection = database.getCollection("orders");
				
				DeleteResult deletedOrders = collection.deleteOne(and(eq("status","cancelled"),lt("orderPlaced", thresholdDate)));
				System.out.println("Number of Orders removed: "+deletedOrders.getDeletedCount());
				return deletedOrders.getDeletedCount();								
			}
			catch (Exception e) {
				System.out.println("Exception occured");
				System.out.println("Details:");
				System.out.println(e.getStackTrace());
				return -1;
			}	
		}			
	}
	
	/**
	 * Removes all the orders. Persists the collection and the indexes.
	 * @param connectionString: To MongoDB instance/MongoDB Cluster.
	 * @return long: Count of deleted documents.
	 */
	public long RemoveAllOrders(String connectionString){
		if(connectionString == null || connectionString.isEmpty())
		{
			throw new IllegalArgumentException();
		}
		else
		{
			MongoClientURI clientUri = new MongoClientURI(connectionString);
			try(MongoClient client = new MongoClient(clientUri))
			{
				MongoDatabase database = client.getDatabase("stores");			
				MongoCollection<Document> collection = database.getCollection("orders");
				
				DeleteResult deletedOrders = collection.deleteMany(new Document());
				System.out.println("Number of Orders removed: "+deletedOrders.getDeletedCount());
				return deletedOrders.getDeletedCount();								
			}
			catch (Exception e) {
				System.out.println("Exception occured");
				System.out.println("Details:");
				System.out.println(e.getStackTrace());
				return -1;
			}	
		}			
	}
	
	/**
	 * Drop the orders collection along with indexes.
	 * @param connectionString: To MongoDB instance/MongoDB Cluster.
	 */
	public void DropOrdersCollection(String connectionString){
		if(connectionString == null || connectionString.isEmpty())
		{
			throw new IllegalArgumentException();
		}
		else
		{
			MongoClientURI clientUri = new MongoClientURI(connectionString);
			try(MongoClient client = new MongoClient(clientUri))
			{
				MongoDatabase database = client.getDatabase("stores");			
				MongoCollection<Document> collection = database.getCollection("orders");
				collection.drop();							
			}
			catch (Exception e) {
				System.out.println("Exception occured");
				System.out.println("Details:");
				System.out.println(e.getStackTrace());
			}	
		}			
	}
}
