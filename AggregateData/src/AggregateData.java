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
	
		/**
		 * Aggregates data in the collection and calculates count using aggregate()
		 * @param connectionString: To MongoDB instance/MongoDB Cluster.
		 * @param state: shippingAddress - state of the document that is to be aggregated.
		 * @return List<Document>: ArrayList of documents with *shippingAddress - city* and its count.
		 */
		
		public List<Document> AggregateByState(String connectionString, String state){
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
					
					List<Document> queryResult = collection.aggregate(
						      		Arrays.asList(
						      				Aggregates.match(Filters.eq("shippingAddress.state", state)),
						      				Aggregates.group("$shippingAddress.city", Accumulators.sum("count", 1))
						      				)
							).into(new ArrayList<Document>());
					return queryResult;
				}
				
				catch (Exception e) 
				{
					System.out.println("Exception occured");
					System.out.println("Details:");
					e.printStackTrace();
					return null;
				}	
			}			
		}
		
		/**
		 * Aggregates data in the collection and calculates average using aggregate()
		 * @param connectionString: To MongoDB instance/MongoDB Cluster.
		 * @param state: shippingAddress - state of the document that is to be aggregated.
		 * @return List<Document>: ArrayList of document with *shippingAddress - state* and its average *shipping*.
		 */
	
		public List<Document> AggregateByStateAverageShipping(String connectionString, String state){
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
					
					List<Document> queryResult = collection.aggregate(
						      Arrays.asList(
						              Aggregates.match(Filters.eq("shippingAddress.state", state)),
						              Aggregates.group("$shippingAddress.state", Accumulators.avg("Average Shipping", "$shipping"))
						      )
						).into(new ArrayList<Document>());
					return queryResult;
					
				}
				
				catch (Exception e) 
				{
					System.out.println("Exception occured");
					System.out.println("Details:");
					e.printStackTrace();
					return null;
				}	
			}			
		}
	
		/**
		 * Aggregates data in the collection and uses various Projections methods to build the $project stage
		 * @param connectionString: To MongoDB instance/MongoDB Cluster.
		 * @return List<Document>: ArrayList of documents with Projected fields, shippingAddress - city and first element of the array, status 
		 */
	
		public List<Document> AggregateByCityCheckStatus(String connectionString){
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
		
					List<Document> queryResult = collection.aggregate(
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
					).into(new ArrayList<Document>());
					return queryResult;
				}
					
					catch (Exception e) 
					{
						System.out.println("Exception occured");
						System.out.println("Details:");
						e.printStackTrace();
						return null;
					}

			}
		}
}
				