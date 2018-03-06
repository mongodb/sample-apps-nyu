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


public class ArrayQueryTest {

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
	public void InOperatorTest() {
		
		ArrayQuery arrayQuery = new ArrayQuery();		
		String[] cities = {"Floris", "Calpine","Orason", "Allentown"};
	  
		List<Document> filteredDocuments = arrayQuery.InOperator(CONNECTION,cities);
	    
	    //verify size
	    assertEquals(4, filteredDocuments.size());	
	    
	   //verify record
	    Document result = filteredDocuments.get(0);
	    String id = result.getString("_id");
	    assertEquals("5a989e4aca820dd0d24cca4c", id);	 
	    
	}
	@Test
	public void AllOperatorTest() {
		ArrayQuery arrayQuery = new ArrayQuery();		
		String[] items = {"omak 2 Door Mobile Cabinet","Glue Sticks"};
	   
		List<Document> filteredDocuments = arrayQuery.AllOperator(CONNECTION,items);
	    
	    //verify size
	    assertEquals(1, filteredDocuments.size());	
	    
	   //verify record
	    Document result = filteredDocuments.get(0);
	    String id = result.getString("_id");
	    assertEquals("5a989e4afbb054481f460f1e", id);	 
	
	}
	@Test
	public void ArrayEqualityTest() {
	ArrayQuery arrayQuery = new ArrayQuery();		
		String status = "shipped";
	   
		List<Document> filteredDocuments = arrayQuery.ArrayEqualityOperator(CONNECTION,status);
	    
	    //verify size
	    assertEquals(38, filteredDocuments.size());	
	    
	   //verify record
	    Document result = filteredDocuments.get(0);
	    String id = result.getString("_id");
	    assertEquals("5a989e4af90b470f476a245b", id);
	}
	
	@Test
	public void ElemMatchOperatorTest() {
		ArrayQuery arrayQuery = new ArrayQuery();		
		String item = "Glue Sticks";
		int quantity = 30;
	   
		List<Document> filteredDocuments = arrayQuery.ElemMatchOperator(CONNECTION,item, quantity);
	    //collection.find(elemMatch("lineitems",and(eq("name","Glue Sticks"),gt("quantity",30))))
	    //verify size
	    assertEquals(3, filteredDocuments.size());	
	    
	   //verify record
	    Document result = filteredDocuments.get(0);
	    String id = result.getString("_id");
	    assertEquals("5a989e4a3a654e2ac2387e7a", id);
	}
}


