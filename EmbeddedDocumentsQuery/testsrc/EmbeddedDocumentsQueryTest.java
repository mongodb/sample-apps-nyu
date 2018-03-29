import static org.junit.Assert.*;

import java.util.List;
import java.text.ParseException;
import org.bson.Document;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;


public class EmbeddedDocumentsQueryTest 
{
    
	static String CONNECTION_STRING = "mongodb+srv://Test:Test987#@cluster0-jxlpi.mongodb.net/test";
	
	@Test
	public void TestConnection() 
	{		
		MongoClientURI clientUri = new MongoClientURI(CONNECTION_STRING);
		try(MongoClient client = new MongoClient(clientUri))
		{
			assertNotNull(client);
		}    
	}

	@Test
	public void FindOrdersByCityTest() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByCity(CONNECTION_STRING,"Woodburn");
	    
	    //verify size
	    assertEquals(1, filteredDocuments.size());	
	    
	    //verify record
	    Document result = filteredDocuments.get(0);
	    String id = result.getString("_id");
	    assertEquals("5a989e4abf545f22ed33d590", id);	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void FindOrdersByCity_InvalidConnectionString_ThrowsException() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByCity("","Woodburn");	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void FindOrdersByCity_EmptyCity_ThrowsException() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByCity(CONNECTION_STRING,"");	    
	}
	
	@Test
	public void FindOrdersByCity_ValidArguments_SuccessWithNoRecords() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByCity(CONNECTION_STRING,"Ithica");
	    
	    //verify size
	    assertEquals(0, filteredDocuments.size());  
	}

	@Test
	public void FindOrdersByStateAndCityTest() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByStateAndCity(CONNECTION_STRING,"Texas","Convent");
	    
	    //verify size
	    assertEquals(1, filteredDocuments.size());	
	    
	    //verify record
	    Document result = filteredDocuments.get(0);
	    String id = result.getString("_id");
	    assertEquals("5a989e4a151dd8d28832eb2f", id);
	    
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void FindOrdersByStateAndCity_InvalidConnectionString_ThrowsException() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByStateAndCity("","Texas","Convent");	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void FindOrdersByStateAndCityTest_EmptyState_ThrowsException() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByStateAndCity(CONNECTION_STRING,"","Convent");	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void FindOrdersByStateAndCityTest_EmptyCity_ThrowsException() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByStateAndCity(CONNECTION_STRING,"Texas","");	    
	}
	
	@Test
	public void FindOrdersByStateAndCity_ValidArguments_SuccessWithNoRecords() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByStateAndCity(CONNECTION_STRING,"Alabama","Mobile");
	    
	    //verify size
	    assertEquals(0, filteredDocuments.size());  
	}
	
	@Test
	public void FindOrdersByUnitPriceTest() {
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByUnitPrice(CONNECTION_STRING,99.9);
	    
	    //verify size
	    assertEquals(1, filteredDocuments.size());	
	    
	    //verify record
	    Document result = filteredDocuments.get(0);
	    String id = result.getString("_id");
	    assertEquals("5a989e4a86d089e9b9c6ea65", id);
	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void FindOrdersByUnitPrice_InvalidConnectionString_ThrowsException() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByUnitPrice("",99.9);	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void FindOrdersByUnitPrice_InvalidUnitPrice_ThrowsException() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByUnitPrice(CONNECTION_STRING,-99.9);	    
	}	
	
	@Test
	public void FindOrdersByUnitPrice_ValidArguments_SuccessWithNoRecords() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByUnitPrice(CONNECTION_STRING,105.0);
	    
	    //verify size
	    assertEquals(0, filteredDocuments.size());  
	}
	
	@Test
	public void FindOrdersByPostalCodeAndSkuTest() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByPostalCodeAndSku(CONNECTION_STRING,10190,"MDBTS192");
	    
	    //verify size
	    assertEquals(1, filteredDocuments.size());	
	    
	    //verify record
	    Document result = filteredDocuments.get(0);
	    String id = result.getString("_id");
	    assertEquals("5a989e4ac5fb0ab8c29ac345", id);
	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void FindOrdersByPostalCodeAndSku_InvalidConnectionString_ThrowsException() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByPostalCodeAndSku("",10190,"MDBTS192");	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void FindOrdersByPostalCodeAndSku_InvalidPostalCode_ThrowsException() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByPostalCodeAndSku(CONNECTION_STRING,-10190,"MDBTS192");	    
	}	
	
	@Test(expected=IllegalArgumentException.class)
	public void FindOrdersByPostalCodeAndSku_EmptySku_ThrowsException() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByPostalCodeAndSku(CONNECTION_STRING,10190,"");	    
	}	
	
	@Test
	public void FindOrdersByPostalCodeAndSku_ValidArguments_SuccessWithNoRecords() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByPostalCodeAndSku(CONNECTION_STRING,10190,"FDXGD403");
	    
	    //verify size
	    assertEquals(0, filteredDocuments.size());  
	}
	
}