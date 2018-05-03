import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


public class InsertExampleDatasetTest {
    
	static String CONNECTION_STRING = "mongodb+srv://Test:Test987#@cluster0-jxlpi.mongodb.net/test";
	static String FILE_PATH = "exampleDataset.json";
	static String NO_FILE_PATH = "notExampleDataset.json";
	
	@Test
	public void TestConnection() {		
		MongoClientURI clientUri = new MongoClientURI(CONNECTION_STRING);
		try(MongoClient client = new MongoClient(clientUri))
		{
			assertNotNull(client);
		}    
	}
	
	@Test
	public void InsertDataset_ValidArguments_SuccessWithRecords() throws FileNotFoundException {
		Boolean success = InsertExampleDataset.InsertDataset(CONNECTION_STRING,FILE_PATH);
		assertEquals(true,success);
		
		
		MongoClientURI clientUri = new MongoClientURI(CONNECTION_STRING);
		try(MongoClient client = new MongoClient(clientUri)){
			MongoDatabase database = client.getDatabase("stores");	
			MongoCollection<Document> collection = database.getCollection("orders");
			List<Document> orders = collection.find().into(new ArrayList<Document>());
			assertEquals(101, orders.size());
		}		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void InsertDataset_InvalidConnectionString_ThrowsException() throws FileNotFoundException {
		Boolean success = InsertExampleDataset.InsertDataset("",FILE_PATH);	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void InsertDataset_InvalidFilePath_ThrowsException() throws FileNotFoundException {
		Boolean success = InsertExampleDataset.InsertDataset(CONNECTION_STRING,"");	    
	}
	
	@Test(expected=FileNotFoundException.class)
	public void InsertDataset_FileNotPresent_ThrowsException() throws FileNotFoundException {
		Boolean success = InsertExampleDataset.InsertDataset(CONNECTION_STRING,NO_FILE_PATH);	    
	}
}
