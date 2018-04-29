import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.Block;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Filters;

public class AggregateData {
	
	 public static void main(String[] args)
	{
	System.out.println("hello");
		//List<Document> res;
		String CONNECTION = "mongodb+srv://m001-student:m001-mongodb-basics@sandbox-rjrpn.mongodb.net/test";
		String state = "Texas";
		
		AggregateData aggregateData = new AggregateData();
		aggregateData.AggregateByState(CONNECTION,state);
		aggregateData.AggregateByStateAverageShipping(CONNECTION, state);
		aggregateData.AggregateByCityCheckStatus(CONNECTION);
	
	}
	
	 Block<Document> printBlock = new Block<Document>() {
	        @Override
	        public void apply(final Document document) {
	            System.out.println(document.toJson());
	        }
	    };
	
	public void AggregateByState(String connectionString, String state){
		if(connectionString == null || connectionString.isEmpty() || state == null || state.isEmpty())
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
				
				collection.aggregate(
					      Arrays.asList(
					              Aggregates.match(Filters.eq("shippingAddress.state", state)),
					              Aggregates.group("$shippingAddress.city", Accumulators.sum("count", 1))
					      )
					).forEach(printBlock);
			}
			
			catch (Exception e) 
			{
				System.out.println("Exception occured");
				System.out.println("Details:");
				e.printStackTrace();
			}	
		}			
	}
	
	public void AggregateByStateAverageShipping(String connectionString, String state){
		if(connectionString == null || connectionString.isEmpty() || state == null || state.isEmpty())
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
				
				collection.aggregate(
					      Arrays.asList(
					              Aggregates.match(Filters.eq("shippingAddress.state", state)),
					              Aggregates.group("$shippingAddress.state", Accumulators.avg("Average Shipping", "$shipping"))
					      )
					).forEach(printBlock);
			}
			
			catch (Exception e) 
			{
				System.out.println("Exception occured");
				System.out.println("Details:");
				e.printStackTrace();
			}	
		}			
	}
	
	
	public void AggregateByCityCheckStatus(String connectionString){
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
	
				collection.aggregate(
						Arrays.asList(
								Aggregates.project(
										Projections.fields(
												Projections.excludeId(),
												Projections.include("shippingAddress.city"),
												Projections.computed(
														"firstCategory",
														new Document("$arrayElemAt", Arrays.asList("$status", 0))
		                    )
		              )
		          )
				)
				).forEach(printBlock);
			}
		}
	}
}
				