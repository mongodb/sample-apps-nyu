import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.eq;


public class ArrayRangeQuery {
	
	/**
	 * SimpleEqualityMatch - Retrieves documents where shipping address postal code is 53558
	 * @param connectionString - connectString to MongoDB instance/MongoDB Cluster
	 * @return List<Documents>
	 */
	public List<Document> InOperator(String connectionString)
	{
		
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
			MongoDatabase database = client.getDatabase("stores");
			
			//option 2
			//MongoDatabase database = ConnectToCluster(connectionString);
			
			MongoCollection<Document> collection = database.getCollection("orders");		
			
			List<Document> ordersToWI;
			
		//use of the $in operator; 
			
		    ordersToWI = collection.find(eq("shippingAddress.city","{$in: [ 'Floris', 'Calpine', 'Orason', 'Allentown']}}")).into(new ArrayList<Document>());
			return ordersToWI;
		}
		catch (Exception e) {
			//log the exception
			return null;
		}
				
			
	}
	
	public List<Document> AllOperator(String connectionString)
	{
		
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
			MongoDatabase database = client.getDatabase("stores");
			
			//option 2
			//MongoDatabase database = ConnectToCluster(connectionString);
			
			MongoCollection<Document> collection = database.getCollection("orders");		
			
			List<Document> ordersToWI;
			

			//use of the $all operator; 
		    ordersToWI = collection.find(eq("lineitems.name","{$all: ['omak 2 Door Mobile Cabinet','Glue Sticks']}}")).into(new ArrayList<Document>());
			return ordersToWI;
		}
		catch (Exception e) {
			//log the exception
			return null;
		}
				
	}
	public List<Document> ArrayEquality(String connectionString)
	{
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
			MongoDatabase database = client.getDatabase("stores");
			
			//option 2
			//MongoDatabase database = ConnectToCluster(connectionString);
			
			MongoCollection<Document> collection = database.getCollection("orders");		
			
			List<Document> ordersToWI;
			

			 //equality match on an array field,
			   ordersToWI = collection.find(eq("status","shipped")).into(new ArrayList<Document>());
			   ordersToWI = collection.find(eq("lineitems.name","Table Saw")).into(new ArrayList<Document>());
			
			return ordersToWI;
		}
		catch (Exception e) {
			//log the exception
			return null;
		}
	}
	public List<Document> ElemMatch(String connectionString)
	{
		
		//option 1
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
			MongoDatabase database = client.getDatabase("stores");
			
			//option 2
			//MongoDatabase database = ConnectToCluster(connectionString);
			
			MongoCollection<Document> collection = database.getCollection("orders");		
			
			List<Document> ordersToWI;
			
		//use of the $in operator; 
			
			//collection.find(eq("rated", "{$in: [ 'R', 'PG-13', 'G']}}"))
		    ordersToWI = collection.find(eq("shippingAddress.city","{$in: [ 'Floris', 'Calpine', 'Orason', 'Allentown']}}")).into(new ArrayList<Document>());
			
			
			
			//use of the $all operator; 
		    ordersToWI = collection.find(eq("lineitems.name","{$all: ['omak 2 Door Mobile Cabinet','Glue Sticks']}}")).into(new ArrayList<Document>());
		
		    
		    //equality match on an array field,
		   ordersToWI = collection.find(eq("status","shipped")).into(new ArrayList<Document>());
		   ordersToWI = collection.find(eq("lineitems.name","Table Saw")).into(new ArrayList<Document>());
		
		    
		    //use of the $elemMatch operator.
		    ordersToWI = collection.find(eq("lineitems","{$elemMatch: {'name':'Glue Sticks', 'quantity':{$gte:10}}}}")).into(new ArrayList<Document>());
			//option 2  - create bson filter
			//Bson filter = new Document("shippingAddress.postalcode",53558);
			//List<Document> ordersToWI = collection.find(filter).into(new ArrayList<Document>());
			
			return ordersToWI;
		}
		catch (Exception e) {
			//log the exception
			return null;
		}
	}	
	
}