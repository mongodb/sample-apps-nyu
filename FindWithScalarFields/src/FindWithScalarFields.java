import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lte;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Projections.excludeId;

public class FindWithScalarFields {
	
	/**
	 * Retrieves documents filtered by shipping address postal.
	 * @param connectionString: To MongoDB instance/MongoDB Cluster.
	 * @param postalcode: Shipping address postal code to filter documents.
	 * @return List<Document>: ArrayList of matching documents.
	 */
	public List<Document> SimpleEqualityMatch(String connectionString, int postalcode){
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
			MongoDatabase database = client.getDatabase("stores");			
			MongoCollection<Document> collection = database.getCollection("orders");		
			
			//option 1 - Static import the required filters
			List<Document> ordersFiltered = collection
					.find(eq("shippingAddress.postalCode",postalcode))
					.into(new ArrayList<Document>());
			
			//option 2  - create bson filter
			/*Bson filter = new Document("shippingAddress.postalcode",postalcode);
			List<Document> ordersFiltered = collection
					.find(filter)
					.into(new ArrayList<Document>());*/
			
			return ordersFiltered;
		}
		catch (Exception e) {
			//log the exception
			return null;
		}		
	}
	
	/**
	 * Retrieves documents filtered by shipping address postal code and total.
	 * @param connectionString: To MongoDB instance/MongoDB Cluster.
	 * @param postalcode: Shipping address postal code to filter documents.
	 * @param total: Order total to filter documents.
	 * @return List<Document>: ArrayList of matching documents.
	 */
	public List<Document> MultipleFieldsEqualityMatch(String connectionString, int postalcode, Double total){
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
			MongoDatabase database = client.getDatabase("stores");		
			MongoCollection<Document> collection = database.getCollection("orders");		
			
			//option 1 - Static import the required filters
			/*List<Document> ordersFiltered = collection
					.find(and(eq("shippingAddress.postalcode",postalcode),eq("total",total)))				
					.into(new ArrayList<Document>());*/
			
			//option 2  - create basicDBObject and append filters			
			BasicDBObject filters = new BasicDBObject();
			filters.append("shippingAddress.postalCode", postalcode);
			filters.append("total", total);			
			List<Document> ordersFiltered = collection
					.find(filters)
					.into(new ArrayList<Document>());
			
			return ordersFiltered;
		}
		catch (Exception e) {
			//log the exception
			return null;
		}	
	}
	
	/**
	 * Retrieves documents where total is greater than lowerBoundTotal and, less than or equal to upperBoundTotal.
	 * @param connectionString: To MongoDB instance/MongoDB Cluster.
	 * @param lowerBoundTotal: Lower bound for Order total to filter documents.
	 * @param upperBoundTotal: Upper bound for Order total to filter documents.
	 * @return List<Document>: ArrayList of matching documents
	 */
	public List<Document> RangeQueryOnNumber(String connectionString, Double lowerBoundTotal, Double upperBoundTotal ){
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
			MongoDatabase database = client.getDatabase("stores");	
			MongoCollection<Document> collection = database.getCollection("orders");		
			
			//option 1 - Static import the required filters
			List<Document> ordersFiltered = collection
					.find(and(gt("total",lowerBoundTotal),lte("total",upperBoundTotal)))
					.projection(fields(include("subtotal","shipping","tax"),excludeId()))
					.into(new ArrayList<Document>());
			
			//option 2  - create basicDBObject and append filters			
			/*BasicDBObject filters = new BasicDBObject();
			filters.append("total", new BasicDBObject("$gt", lowerBoundTotal));
			filters.append("total", new BasicDBObject("$lte", upperBoundTotal));						
			List<Document> ordersFiltered = collection
					.find(filters)
					.projection(new BasicDBObject("subtotal",true)
									.append("shipping", true)
									.append("tax", true)
									.append("_id", false))
					.into(new ArrayList<Document>());*/
			
			return ordersFiltered;
		}
		catch (Exception e) {
			//log the exception
			return null;
		}		
	}
	
	/**
	 * Retrieves documents where orderPlaced is between startDate and endDate.
	 * @param connectionString: To MongoDB instance/MongoDB Cluster.
	 * @param startDate: Lower bound for order placed date to filter documents.
	 * @param endDate: Upper bound for order placed date to filter documents.
	 * @return List<Document>: ArrayList of matching documents
	 */
	public List<Document> RangeQueryByDate(String connectionString, Date startDate, Date endDate){
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
			MongoDatabase database = client.getDatabase("stores");	
			MongoCollection<Document> collection = database.getCollection("orders");
			
			//option 1 - Static import the required filters
			/*List<Document> ordersFiltered = collection
					.find(and(gte("orderPlaced",startDate),lt("orderPlaced",endDate)))
					.projection(fields(include("total"),excludeId()))
					.into(new ArrayList<Document>());*/
			
			//option 2  - create basicDBObject and append filters			
			BasicDBObject filters = new BasicDBObject();
			filters.append("orderPlaced", new BasicDBObject("$gte", startDate));
			filters.append("orderPlaced", new BasicDBObject("$lt", endDate));						
			List<Document> ordersFiltered = collection
					.find(filters)
					.projection(new BasicDBObject("total",true)
									.append("_id", false))
					.into(new ArrayList<Document>());
			
			return ordersFiltered;
		}
		catch (Exception e) {
			//log the exception
			return null;
		}		
	}		
	
}
