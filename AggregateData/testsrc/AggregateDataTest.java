import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.elemMatch;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static org.junit.Assert.*;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.Block;

import org.junit.Test;

public class AggregateDataTest {
    
	static String CONNECTION_STRING = "mongodb+srv://m001-student:m001-mongodb-basics@sandbox-rjrpn.mongodb.net/test";
	
	@Test
	public void TestConnection() {		
		MongoClientURI clientUri = new MongoClientURI(CONNECTION_STRING);
		try(MongoClient client = new MongoClient(clientUri))
		{
			assertNotNull(client);
		}
	}
		
		@Test
		public void AggregateByStateTest() {
			
			MongoClientURI clientUri = new MongoClientURI(CONNECTION_STRING);
			MongoClient client = new MongoClient(clientUri);
			
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");
			
			AggregateData aggregateData = new AggregateData();
			List<Document> queryResult = aggregateData.AggregateByState(CONNECTION_STRING, "Texas");
			assertEquals(2, queryResult.size());	
	                
	        //verify first record with count
		    Document result1 = queryResult.get(0);
		    String id1 = result1.getString("_id");
		    Integer count1 = result1.getInteger("count");
		    assertEquals("Convent", id1);
		    assertEquals(1, count1, 0.00);
		    
	        //verify second record with count
		    Document result2 = queryResult.get(1);
		    String id2 = result2.getString("_id");
		    Integer count2 = result2.getInteger("count");
		    assertEquals("Cade", id2);
		    assertEquals(1, count2, 0.00);
		}
		 
		@Test(expected=IllegalArgumentException.class)
		public void AggregateByStateTest_InvalidConnectionString_ThrowsException() {
			
				AggregateData aggregateData = new AggregateData();	
				List<Document> queryResult = aggregateData.AggregateByState("", "Texas");	    
			}
		
		@Test(expected=IllegalArgumentException.class)
		public void AggregateByStateTest_InvalidState_ThrowsException() {
			
			AggregateData aggregateData = new AggregateData();	
			List<Document> queryResult = aggregateData.AggregateByState(CONNECTION_STRING, "");    
		}
		
		@Test
		public void AggregateByStateTest_ValidArguments_SuccessWithNoRecords_ThrowsException() {
			
			AggregateData aggregateData = new AggregateData();	
			List<Document> queryResult = aggregateData.AggregateByState(CONNECTION_STRING, "Quebec");
			
			
			//verify size
		    assertEquals(0, queryResult.size());  
		}
		
		@Test
		public void AggregateByStateAverageShippingTest() {
			
			MongoClientURI clientUri = new MongoClientURI(CONNECTION_STRING);
			MongoClient client = new MongoClient(clientUri);
			
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");
			
			//Calculate average
	        List<Document> queryResult1 = collection
	        		.find((eq("shippingAddress.state", "Texas")))
	        		.into(new ArrayList<Document>());
	        
		    Document result1a = queryResult1.get(0);
		    Double shipping1a = result1a.getDouble("shipping");
		    
		    Document result1b = queryResult1.get(1);
		    Double shipping1b = result1b.getDouble("shipping");
		    
		    Double average1 = (shipping1a + shipping1b)/2;
			
			AggregateData aggregateData = new AggregateData();
			List<Document> queryResult2 = aggregateData.AggregateByStateAverageShipping(CONNECTION_STRING, "Texas");
			assertEquals(1, queryResult2.size());	
	                
	        //verify record and average
		    Document result2 = queryResult2.get(0);
		    String state2 = result2.getString("_id");
		    Double average2 = result2.getDouble("Average Shipping");
		    assertEquals("Texas", state2);
		    assertEquals(average1, average2, 0.00);
		    
		}
		 
		@Test(expected=IllegalArgumentException.class)
		public void AggregateByStateAverageShippingTest_InvalidConnectionString_ThrowsException() {
			
				AggregateData aggregateData = new AggregateData();	
				List<Document> queryResult = aggregateData.AggregateByState("", "Texas");	    
			}
		
		@Test(expected=IllegalArgumentException.class)
		public void AggregateByStateAverageShippingTest_InvalidState_ThrowsException() {
			
			AggregateData aggregateData = new AggregateData();	
			List<Document> queryResult = aggregateData.AggregateByState(CONNECTION_STRING, "");    
		}
		
		@Test
		public void AggregateByStateAverageShippingTest_ValidArguments_SuccessWithNoRecords_ThrowsException() {
			
			AggregateData aggregateData = new AggregateData();	
			List<Document> queryResult = aggregateData.AggregateByState(CONNECTION_STRING, "Quebec"); 
			
			
			//verify size
		    assertEquals(0, queryResult.size());  
		}
		
		@Test
		public void AggregateByCityCheckStatusTest() {
			
			MongoClientURI clientUri = new MongoClientURI(CONNECTION_STRING);
			MongoClient client = new MongoClient(clientUri);
			
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");
			
			AggregateData aggregateData = new AggregateData();
			List<Document> queryResult = aggregateData.AggregateByCityCheckStatus(CONNECTION_STRING);
			assertEquals(100, queryResult.size());	
	                
	        //verify first record with count
		    Document result = queryResult.get(0);
		    //String city = result.getString("shippingAddress$city");
		    String firstCategory = result.getString("firstCategory");
		    //assertEquals("Deercroft", city);
		    assertEquals("shipped", firstCategory);
		    
		}
		 
		@Test(expected=IllegalArgumentException.class)
		public void AggregateByCityCheckStatus_InvalidConnectionString_ThrowsException() {
			
				AggregateData aggregateData = new AggregateData();	
				List<Document> queryResult = aggregateData.AggregateByCityCheckStatus("");	    
			}
}


