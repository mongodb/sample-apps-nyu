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

public class UpdateDataTest {
    
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
	public void UpdateOneOrderTest() {
		
		UpdateData updateData = new UpdateData();		
		List<Document> filteredDocuments = updateData.UpdateOneOrder(CONNECTION_STRING, 406.0701, 100.23);
		assertEquals(1, filteredDocuments.size());	
		
		//verify record
	    Document result = filteredDocuments.get(0);
	    String id = result.getString("_id");
	    assertEquals("5a989e4ae0d1351062640fdc", id);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateOneOrderTest_InvalidConnectionString_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> filteredDocuments =updateData.UpdateOneOrder("", 406.0701, 100.23);	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateOneOrderTest_InvalidTotal_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> filteredDocuments =updateData.UpdateOneOrder(CONNECTION_STRING, -406.0701, 100.23);	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateOneOrderTest_InvalidSubtotal_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> filteredDocuments =updateData.UpdateOneOrder(CONNECTION_STRING, 406.0701, -100.23);	    
	}
	
	@Test
	public void UpdateOneOrderTest_ValidArguments_SuccessWithNoRecords_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> filteredDocuments =updateData.UpdateOneOrder(CONNECTION_STRING, 555.555, 100.00);
		
		 //verify size
	    assertEquals(0, filteredDocuments.size());  
	}
	
	@Test
	public void UpdateOneOrderForEmbeddedFieldTest() {
		
		UpdateData updateData = new UpdateData();		
		List<Document> filteredDocuments = updateData.UpdateOneOrderForEmbeddedField(CONNECTION_STRING, "MDBTS134", "Medium Duty Blender");
		assertEquals(1, filteredDocuments.size());	
		
		//verify record
	    Document result = filteredDocuments.get(0);
	    String id = result.getString("_id");
	    assertEquals("5a989e4af8a55a0ce18a4134", id);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateOneOrderForEmbeddedFieldTest_InvalidConnectionString_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> filteredDocuments =updateData.UpdateOneOrderForEmbeddedField("", "MDBTS134", "Medium Duty Blender");	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateOneOrderForEmbeddedFieldTest_EmptySku_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> filteredDocuments =updateData.UpdateOneOrderForEmbeddedField(CONNECTION_STRING, "", "Medium Duty Blender");	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateOneOrderForEmbeddedFieldTest_EmptyName_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> filteredDocuments =updateData.UpdateOneOrderForEmbeddedField(CONNECTION_STRING, "MDBTS134", "");	    
	}
	
	@Test
	public void UpdateOneOrderForEmbeddedFieldTest_ValidArguments_SuccessWithNoRecords_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> filteredDocuments =updateData.UpdateOneOrderForEmbeddedField(CONNECTION_STRING, "GBPX8634", "Medium Duty Blender");
		
		 //verify size
	    assertEquals(0, filteredDocuments.size());  
	}
	
	@Test
	public void UpdateManyOrdersWithOperatorTest() {
		
		UpdateData updateData = new UpdateData();		
		List<Document> filteredDocuments = updateData.UpdateManyOrdersWithOperator(CONNECTION_STRING, 2300.00, 230.00);
		assertEquals(4, filteredDocuments.size());	
		
		//verify records
		
		String[] ids = {"5a989e4a1509ec12d817f03e", "5a989e4a60d71cc34a54260b", "5a989e4a24a0ed39cf3e92a0", "5a989e4a6ff8212799790854"};
		
		for(int i=0; i<=3; i++)
		{	
			Document result = filteredDocuments.get(i);
			String id = result.getString("_id");
			assertEquals(ids[i], id);
		}

	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateManyOrdersWithOperatorTest_InvalidConnectionString_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> filteredDocuments =updateData.UpdateManyOrdersWithOperator("", 2300.00, 230.00);	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateManyOrdersWithOperatorTest_InvalidTotal_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> filteredDocuments =updateData.UpdateManyOrdersWithOperator(CONNECTION_STRING, -2300.00, 230.00);	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateManyOrdersWithOperatorTest_InvalidTax_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> filteredDocuments =updateData.UpdateManyOrdersWithOperator(CONNECTION_STRING, 2300.00, -230.00);	    
	}
	
	@Test
	public void UpdateManyOrdersWithOperatorTest_ValidArguments_SuccessWithNoRecords_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> filteredDocuments =updateData.UpdateManyOrdersWithOperator(CONNECTION_STRING, 5000.00, 500.00);
		
		 //verify size
	    assertEquals(0, filteredDocuments.size());  
	}
	
	@Test
	public void UpdateManyOrdersForEmbeddedFieldTest() {
		
		UpdateData updateData = new UpdateData();		
		List<Document> filteredDocuments = updateData.UpdateManyOrdersForEmbeddedField(CONNECTION_STRING, "MDBTS244", 70.23);
		assertEquals(2, filteredDocuments.size());	
		
		//verify records
		
		String[] ids = {"5a989e4a317af0f885a7e8f6", "5a989e4a6629619e0aecc566"};
		
		for(int i=0; i<=1; i++)
		{	
			Document result = filteredDocuments.get(i);
			String id = result.getString("_id");
			assertEquals(ids[i], id);
		}
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateManyOrdersForEmbeddedFieldTest_InvalidConnectionString_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> filteredDocuments =updateData.UpdateManyOrdersForEmbeddedField("", "MDBTS244", 70.23);	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateManyOrdersForEmbeddedFieldTest_EmptySku_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> filteredDocuments =updateData.UpdateManyOrdersForEmbeddedField(CONNECTION_STRING, "", 70.23);	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateManyOrdersForEmbeddedFieldTest_InvalidUnitPrice_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> filteredDocuments =updateData.UpdateManyOrdersForEmbeddedField(CONNECTION_STRING, "MDBTS244", -70.23);	    
	}
	
	@Test
	public void UpdateManyOrdersForEmbeddedFieldTest_ValidArguments_SuccessWithNoRecords_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> filteredDocuments =updateData.UpdateManyOrdersForEmbeddedField(CONNECTION_STRING, "GBPX8634", 70.23);
		
		 //verify size
	    assertEquals(0, filteredDocuments.size());  
	}
}