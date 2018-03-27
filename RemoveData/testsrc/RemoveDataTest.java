import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.bson.Document;
import org.junit.Test;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoNamespace;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Aggregates.*;


public class RemoveDataTest {
    
	static String CONNECTION_STRING = "mongodb+srv://Test:Test987#@cluster0-jxlpi.mongodb.net/test";
	
	@Test
	public void TestConnection() {		
		MongoClientURI clientUri = new MongoClientURI(CONNECTION_STRING);
		try(MongoClient client = new MongoClient(clientUri))
		{
			assertNotNull(client);
		}    
	}
	
	// Remove All method with multiple conditions
	@Test(expected=IllegalArgumentException.class)
	public void RemoveAllCancelledOrders_InvalidConnectionString_ThrowsException() throws ParseException {
		RemoveData removeData = new RemoveData();		
		Date thresholdDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse("2018-01-16T19:00:00.000");
	    removeData.RemoveAllCancelledOrders("",thresholdDate);    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void RemoveAllCancelledOrders_InvalidThresholdDate_ThrowsException() throws ParseException {
		RemoveData removeData = new RemoveData();		
		removeData.RemoveAllCancelledOrders(CONNECTION_STRING,null);
	}
		
	@Test
	public void RemoveAllCancelledOrders_ValidArguments_Success() throws ParseException {
		// Add 5 dummy documents to test the deletion method
		Date thresholdDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse("1900-01-16T19:00:00.000");
		List<Document> documents = new ArrayList<Document>();
		for(int i=1;i<=5;i++)
		{
			documents.add(new Document("orderPlaced", DateUtils.addDays(thresholdDate, -1*i))
	         .append("status", Arrays.asList("cancelled", DateUtils.addDays(thresholdDate, -2*i))));
		}
		
		MongoClientURI clientUri = new MongoClientURI(CONNECTION_STRING);
		try(MongoClient client = new MongoClient(clientUri))
		{
			MongoDatabase database = client.getDatabase("stores");			
			MongoCollection<Document> collection = database.getCollection("orders");
			collection.insertMany(documents);	
		}	 
		
		RemoveData removeData = new RemoveData();		
	    long removedDocumentsCount = removeData.RemoveAllCancelledOrders(CONNECTION_STRING,thresholdDate);
	    
	    //verify if 5 documents were deleted
	    assertEquals(5, removedDocumentsCount);	    
	}
	
	// Remove one method with multiple conditions	
	@Test(expected=IllegalArgumentException.class)
	public void RemoveOneCancelledOrder_InvalidConnectionString_ThrowsException() throws ParseException {
		RemoveData removeData = new RemoveData();		
		Date thresholdDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse("2018-01-16T19:00:00.000");
	    removeData.RemoveOneCancelledOrder("",thresholdDate);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void RemoveOneCancelledOrder_InvalidThresholdDate_ThrowsException() throws ParseException {
		RemoveData removeData = new RemoveData();
	    removeData.RemoveOneCancelledOrder(CONNECTION_STRING,null);	    
	}
	
	@Test
	public void RemoveOneCancelledOrder_ValidArguments_Success() throws ParseException {	
		Date thresholdDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse("1905-01-16T19:00:00.000");
		//Add one dummy document to test deletion
		MongoClientURI clientUri = new MongoClientURI(CONNECTION_STRING);
		try(MongoClient client = new MongoClient(clientUri))
		{
			MongoDatabase database = client.getDatabase("stores");			
			MongoCollection<Document> collection = database.getCollection("orders");
			collection.insertOne(new Document("orderPlaced", DateUtils.addDays(thresholdDate, -1))
	         							.append("status", Arrays.asList("cancelled", DateUtils.addDays(thresholdDate, -2))));
		}	
		RemoveData removeData = new RemoveData();
	    long removedDocumentsCount = removeData.RemoveOneCancelledOrder(CONNECTION_STRING,thresholdDate);
	    
	    //verify if one record was deleted
	    assertEquals(1, removedDocumentsCount);
	}
	
	//Remove All No condition
	@Test(expected=IllegalArgumentException.class)
	public void RemoveAllOrders_InvalidConnectionString_ThrowsException() throws ParseException {
		RemoveData removeData = new RemoveData();
	    removeData.RemoveAllOrders("");    
	}
	
	@Test
	public void RemoveAllOrders_ValidArguments_Success() throws ParseException {
		MongoClientURI clientUri = new MongoClientURI(CONNECTION_STRING);
		try(MongoClient client = new MongoClient(clientUri))
		{
			MongoDatabase database = client.getDatabase("stores");			
			MongoCollection<Document> orginalCollection = database.getCollection("orders");
			
			//create copy of documents as backup
			List<Document> allOrders = orginalCollection
					.find(new Document())
					.into(new ArrayList<Document>());
			
			//remove all the orders
			RemoveData removeData = new RemoveData();		
			long removedDocumentsCount = removeData.RemoveAllOrders(CONNECTION_STRING);
		    
		    //Assert deletion
		    assertEquals(101, removedDocumentsCount);	 
			
			//Restore orders to original collection			
			orginalCollection.insertMany(allOrders);
			client.close();
		}	       
	}
	
	//Drop Collection
	@Test(expected=IllegalArgumentException.class)
	public void DropOrdersCollection_InvalidConnectionString_ThrowsException() throws ParseException {
		RemoveData removeData = new RemoveData();
	    removeData.DropOrdersCollection("");    
	}
	
	@Test
	public void DropOrdersCollection_ValidArguments_Success() throws ParseException {
	    MongoClientURI clientUri = new MongoClientURI(CONNECTION_STRING);
		try(MongoClient client = new MongoClient(clientUri))
		{
			MongoDatabase database = client.getDatabase("stores");			
			MongoCollection<Document> orginalCollection = database.getCollection("orders");
			
			//Create replica of original collection as backup
			orginalCollection.aggregate(Arrays.asList(out("ordersTemp"))).forEach(printBlock);
			
			//drop the collection
			RemoveData removeData = new RemoveData();		
		    removeData.DropOrdersCollection(CONNECTION_STRING);
		    
		    //Assert deletion
		    boolean collectionExists = client.getDatabase("stores").listCollectionNames()
		    	    .into(new ArrayList<String>()).contains("orders");
		    assertEquals(false, collectionExists);
			
			//rename replica to restore original collection			
			MongoCollection<Document> tempCollection = database.getCollection("ordersTemp");
			tempCollection.renameCollection(new MongoNamespace("stores","orders"));
			client.close();
		}
	}
	
	Block<Document> printBlock = new Block<Document>() {
        @Override
        public void apply(final Document document) {
            System.out.println(document.toJson());
        }
    };
}
