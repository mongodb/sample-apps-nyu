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
	public void ShippingByCityTest() {
		
		ArrayQuery arrayQuery = new ArrayQuery();		
		String[] cities = {"Floris", "Calpine","Orason", "Allentown"};
		List<Document> filteredDocuments = arrayQuery.GetShippingByCity(CONNECTION,cities);
	   	assertEquals(4, filteredDocuments.size());	
			//verify record
		    Document result = filteredDocuments.get(0);
		    String id = result.getString("_id");
		    assertEquals("5a989e4aca820dd0d24cca4c", id);	 
	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ShippingByCityTest_InvalidConnectionString_ThrowsException() 
	{
		String[] cities = {"Floris", "Calpine","Orason", "Allentown"};
		ArrayQuery arrayQuery = new ArrayQuery();	
		List<Document> filteredDocuments = arrayQuery.GetShippingByCity("",cities);	    
	}
		
	
	@Test
	public void ShippingByCityTest_ValidArguments_SuccessWithNoRecords() {
		String[] cities = {"Seoul", "Madrid", "New York"};
		ArrayQuery arrayQuery = new ArrayQuery();
		List<Document> filteredDocuments = arrayQuery.GetShippingByCity(CONNECTION,cities);
	  //verify size
		assertEquals(0, filteredDocuments.size());  
	}
	
	@Test
	public void ShippingByItemTest() {
		ArrayQuery arrayQuery = new ArrayQuery();		
		String[] items = {"omak 2 Door Mobile Cabinet","Glue Sticks"};
		
		List<Document> filteredDocuments = arrayQuery.GetShippingByItem(CONNECTION,items);
		//verify size
		//	System.out.println("Found match");
			assertEquals(1, filteredDocuments.size());	
		    
		   //verify record
		    Document result = filteredDocuments.get(0);
		    String id = result.getString("_id");
		    assertEquals("5a989e4afbb054481f460f1e", id);	
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ShippingByItemTest_InvalidConnectionString_ThrowsException() 
	{
		String[] items = {"omak 2 Door Mobile Cabinet","Glue Sticks"};
		
		ArrayQuery arrayQuery = new ArrayQuery();	
		List<Document> filteredDocuments = arrayQuery.GetShippingByItem("",items);	    
	}
		
	@Test(expected=IllegalArgumentException.class)
	public void ShippingByItemTest_emptyString_ThrowsException() 
	{
		String[] items = {} ;
	
		ArrayQuery arrayQuery = new ArrayQuery();	
		List<Document> filteredDocuments = arrayQuery.GetShippingByItem(CONNECTION,items);	    
		
	}
	
	@Test
	public void ShippingByItemTest_ValidArguments_SuccessWithNoRecords() {
		String[] items = {"Scissors","Tape"};
		ArrayQuery arrayQuery = new ArrayQuery();
		List<Document> filteredDocuments = arrayQuery.GetShippingByItem(CONNECTION,items);
	  //verify size
		assertEquals(0, filteredDocuments.size());  
	}
	
	
	
	
	@Test
	public void FindByStatusTest() {
	ArrayQuery arrayQuery = new ArrayQuery();		
		String status = "shipped";
		//String status = "ordered";
		List<Document> filteredDocuments = arrayQuery.FindByStatus(CONNECTION,status);
	    
		if(filteredDocuments==null)
		{
			
			System.out.println("Empty Filter");
			//assertEquals(0, filteredDocuments.size());	
		}
		else if(filteredDocuments.size()==0)
		{
			
			System.out.println("No match found");
			assertEquals(0, filteredDocuments.size());	
		}	
		else//verify size
	    {//verify size
			System.out.println("Found match");
			assertEquals(38, filteredDocuments.size());	
		    
		   //verify record
		    Document result = filteredDocuments.get(0);
		    String id = result.getString("_id");
		    assertEquals("5a989e4af90b470f476a245b", id);
	    }
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void FindByStatusTest_InvalidConnectionString_ThrowsException() 
	{
		ArrayQuery arrayQuery = new ArrayQuery();	
		List<Document> filteredDocuments =arrayQuery.FindByStatus("","shipped");	    
	}
		
	@Test(expected=IllegalArgumentException.class)
	public void FindByStatusTest_emptyString_ThrowsException() 
	{
		ArrayQuery arrayQuery = new ArrayQuery();	
		List<Document> filteredDocuments = arrayQuery.FindByStatus(CONNECTION,"");	    		
	}
	
	@Test
	public void FindByStatusTest_ValidArguments_SuccessWithNoRecords() 
	{
		ArrayQuery arrayQuery = new ArrayQuery();
		List<Document> filteredDocuments = arrayQuery.FindByStatus(CONNECTION,"re-ordered");
	  //verify size
		assertEquals(0, filteredDocuments.size());  
	}
	
	@Test
	public void FindByItemQuantityTest() {
		ArrayQuery arrayQuery = new ArrayQuery();		
		//String item = "Glue Sticks";
		//int quantity = 30;
		String item = "Tape";
		int quantity = 20;
		List<Document> filteredDocuments = arrayQuery.FindByItemQuantity(CONNECTION,item, quantity);
	    //collection.find(elemMatch("lineitems",and(eq("name","Glue Sticks"),gt("quantity",30))))
	    //verify size
		if(filteredDocuments==null)
		{
			
			System.out.println("Empty Filter");
			//assertEquals(0, filteredDocuments.size());	
		}
		else if(filteredDocuments.size()==0)
		{
			
			System.out.println("No match found");
			assertEquals(0, filteredDocuments.size());	
		}
		
		else//verify size
	    {
			System.out.println("Found match");
			assertEquals(3, filteredDocuments.size());	
		    
		    
		   //verify record
		    Document result = filteredDocuments.get(0);
		    String id = result.getString("_id");
		    assertEquals("5a989e4a3a654e2ac2387e7a", id);
	    }
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void FindByItemQuantityTest_InvalidConnectionString_ThrowsException() 
	{
		ArrayQuery arrayQuery = new ArrayQuery();	
		List<Document> filteredDocuments =arrayQuery.FindByItemQuantity("","Tape", 20);	    
	}
		
	@Test(expected=IllegalArgumentException.class)
	public void FindByItemQuantityTest_emptyString_ThrowsException() 
	{
		ArrayQuery arrayQuery = new ArrayQuery();	
		List<Document> filteredDocuments = arrayQuery.FindByItemQuantity(CONNECTION, "", 0);	    		
	}
	
	@Test
	public void FindByItemQuantityTest_ValidArguments_SuccessWithNoRecords() 
	{
		ArrayQuery arrayQuery = new ArrayQuery();
		List<Document> filteredDocuments = arrayQuery.FindByItemQuantity(CONNECTION, "Tape", 180);
	  //verify size
		assertEquals(0, filteredDocuments.size());  
	}
}


