import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.elemMatch;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static org.junit.Assert.*;

import org.bson.Document;

import java.text.ParseException;
import java.util.List;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.junit.Test;


public class InsertDataTest {

	String CONNECTION = "mongodb+srv://m001-student:student123#@sandbox-trhqa.mongodb.net/test";
	
	@Test
	public void TestConnection() {		
		MongoClientURI clientUri = new MongoClientURI(CONNECTION);
		try(MongoClient client = new MongoClient(clientUri))
		{
			assertNotNull(client);
		}    
	}
	@Test
	public void AddOneOrderTest() {
		
		InsertData insertData = new InsertData();		
		List<Document> AddDocuments = InsertData.AddOneOrder(CONNECTION);
		List<Document> filteredDocuments = InsertData.CountOrderSize(CONNECTION);
			
		assertEquals(102, filteredDocuments.size());	
	}

	
	@Test(expected=IllegalArgumentException.class)
	public void AddOneOrderTest_InvalidConnectionString_ThrowsException() 
	{
		InsertData insertData = new InsertData();		
		List<Document> AddDocuments = InsertData.AddOneOrder("");
	    
	}
	
	
	@Test
	public void AddOneOrderJsonTest() {
		
		InsertData insertData = new InsertData();		
		List<Document> AddDocuments = InsertData.AddOneOrderJson(CONNECTION);
		List<Document> filteredDocuments = InsertData.CountOrderSize(CONNECTION);
		
	
		assertEquals(101, filteredDocuments.size());	
	}

	
	@Test(expected=IllegalArgumentException.class)
	public void AddOneOrderJsonTest_InvalidConnectionString_ThrowsException() 
	{
		InsertData insertData = new InsertData();		
		List<Document> AddDocuments = InsertData.AddOneOrder("");
	    
	}
		
}	
