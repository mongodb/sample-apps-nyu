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
	public void UpdateSubtotalShippingTaxBasedOnTotalTest() {
		
		MongoClientURI clientUri = new MongoClientURI(CONNECTION_STRING);
		MongoClient client = new MongoClient(clientUri);
		
		MongoDatabase database = client.getDatabase("stores");
		MongoCollection<Document> collection = database.getCollection("orders");
		
        List<Document> queryResult1 = collection
        		.find(and(eq("total", 406.0701),eq("subtotal", 70.87),eq("shipping",226),eq("tax",67.9352)))
        		.into(new ArrayList<Document>());
                
        //verify original record
	    Document result1 = queryResult1.get(0);
	    String id1 = result1.getString("_id");
	    Double subtotal1 = result1.getDouble("subtotal");
	    Double shipping1 = result1.getDouble("shipping");
	    Double tax1 = result1.getDouble("tax");
	    assertEquals("5a989e4ae0d1351062640fdc", id1);
	    assertEquals(70.87, subtotal1, 0.00);
	    assertEquals(226, shipping1, 0.00);
	    assertEquals(67.9352, tax1, 0.00);
	    
	    //update data
		UpdateData updateData = new UpdateData();		
		List<Document> queryResult2 = updateData.UpdateSubtotalShippingTaxBasedOnTotal(CONNECTION_STRING, 406.0701, 100.23, 200.23, 56.12);
		assertEquals(1, queryResult2.size());	
		
		//verify updated record
	    Document result2 = queryResult2.get(0);
	    String id2 = result2.getString("_id");
	    Double subtotal2 = result2.getDouble("subtotal");
	    Double shipping2 = result2.getDouble("shipping");
	    Double tax2 = result2.getDouble("tax");
	    assertEquals("5a989e4ae0d1351062640fdc", id2);
	    assertEquals(100.23, subtotal2, 0.00);
	    assertEquals(200.23, shipping2, 0.00);
	    assertEquals(56.12, tax2, 0.00);
	    
	    //revert to original value
	    List<Document> queryResult3 = updateData.UpdateSubtotalShippingTaxBasedOnTotal(CONNECTION_STRING, 406.0701, 70.87, 226.0, 67.9352);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateOneOrderTest_InvalidConnectionString_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateSubtotalShippingTaxBasedOnTotal("", 406.0701, 100.23, 200.23, 56.12);	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateOneOrderTest_InvalidTotal_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateSubtotalShippingTaxBasedOnTotal(CONNECTION_STRING, -406.0701, 100.23, 200.23, 56.12);	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateOneOrderTest_InvalidSubtotal_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateSubtotalShippingTaxBasedOnTotal(CONNECTION_STRING, 406.0701, -100.23, 200.23, 56.12);	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateOneOrderTest_InvalidShipping_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateSubtotalShippingTaxBasedOnTotal(CONNECTION_STRING, 406.0701, 100.23, -200.23, 56.12);	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateOneOrderTest_InvalidTax_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateSubtotalShippingTaxBasedOnTotal(CONNECTION_STRING, 406.0701, 100.23, 200.23, -56.12);	    
	}
	
	@Test
	public void UpdateOneOrderTest_ValidArguments_SuccessWithNoRecords_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateSubtotalShippingTaxBasedOnTotal(CONNECTION_STRING, 555.555, 100.00, 200.20, 56.50);
		
		 //verify size
	    assertEquals(0, queryResult.size());  
	}
	
	@Test
	public void UpdateNameBasedOnSKUTest() {
		
		MongoClientURI clientUri = new MongoClientURI(CONNECTION_STRING);
		MongoClient client = new MongoClient(clientUri);
		
		MongoDatabase database = client.getDatabase("stores");
		MongoCollection<Document> collection = database.getCollection("orders");
		
        List<Document> queryResult1 = collection
        		.find(and(eq("lineitems.sku", "MDBTS134"),eq("lineitems.name", "1.6 oz. Vitamin C Serum")))
        		.into(new ArrayList<Document>());
        
        //verify original record
        Document document1 = (Document) collection
        		.find(and(eq("lineitems.sku", "MDBTS134"),eq("lineitems.name", "1.6 oz. Vitamin C Serum")))
        		.first()
        	    .get("lineitems", ArrayList.class)
        	    .get(0);
	    
        Document result1 = queryResult1.get(0);
	    String id1 = result1.getString("_id");
	    String name1 = document1.getString("name");
	    assertEquals("5a989e4af8a55a0ce18a4134", id1);
	    assertEquals("1.6 oz. Vitamin C Serum", name1);
		
	    //update data
		UpdateData updateData = new UpdateData();		
		List<Document> queryResult2 = updateData.UpdateNameBasedOnSKU(CONNECTION_STRING, "MDBTS134", "Medium Duty Blender");
		assertEquals(1, queryResult2.size());	
		
		//verify updated record		
        Document document2 = (Document) collection
        		.find(and(eq("lineitems.sku", "MDBTS134"),eq("lineitems.name", "Medium Duty Blender")))
        		.first()
        	    .get("lineitems", ArrayList.class)
        	    .get(0);
        
	    Document result2 = queryResult2.get(0);
	    String id2 = result2.getString("_id");
	    String name2 = document2.getString("name");
	    assertEquals("5a989e4af8a55a0ce18a4134", id2);
	    assertEquals("Medium Duty Blender", name2);
	    
	    //revert to original value
	    List<Document> queryResult3 = updateData.UpdateNameBasedOnSKU(CONNECTION_STRING, "MDBTS134", "1.6 oz. Vitamin C Serum");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateOneOrderForEmbeddedFieldTest_InvalidConnectionString_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateNameBasedOnSKU("", "MDBTS134", "Medium Duty Blender");	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateOneOrderForEmbeddedFieldTest_EmptySku_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateNameBasedOnSKU(CONNECTION_STRING, "", "Medium Duty Blender");	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateOneOrderForEmbeddedFieldTest_EmptyName_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateNameBasedOnSKU(CONNECTION_STRING, "MDBTS134", "");	    
	}
	
	@Test
	public void UpdateOneOrderForEmbeddedFieldTest_ValidArguments_SuccessWithNoRecords_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateNameBasedOnSKU(CONNECTION_STRING, "GBPX8634", "Medium Duty Blender");
		
		 //verify size
	    assertEquals(0, queryResult.size());  
	}
	
	@Test
	public void UpdateTaxBasedOnTotalForManyOrdersTest() {
		
		MongoClientURI clientUri = new MongoClientURI(CONNECTION_STRING);
		MongoClient client = new MongoClient(clientUri);
		
		MongoDatabase database = client.getDatabase("stores");
		MongoCollection<Document> collection = database.getCollection("orders");
	
		
        List<Document> queryResult1 = collection
        		.find(and(gt("total",2300.00)))
        		.into(new ArrayList<Document>());
        
        //verify original record
        assertEquals(4, queryResult1.size());
        	
		Document result1 = queryResult1.get(0);
		String id1 = result1.getString("_id");
		Double tax1 = result1.getDouble("tax");
		assertEquals("5a989e4a1509ec12d817f03e", id1);
		assertEquals(121.30, tax1, 0.00);
		
		//update data
		UpdateData updateData = new UpdateData();		
		List<Document> queryResult2 = updateData.UpdateTaxBasedOnTotalForManyOrders(CONNECTION_STRING, 2300.00, 230.00);
		assertEquals(4, queryResult2.size());	
		
		//verify updated record
			
		Document result2 = queryResult2.get(0);
		String id2 = result2.getString("_id");
		Double tax2 = result2.getDouble("tax");
		assertEquals("5a989e4a1509ec12d817f03e", id2);
		assertEquals(230.00, tax2, 0.00);

		
	    //revert to original value
		
		Document newDocument1 = new Document();
		newDocument1.append("$set", new Document().append("tax", 121.30));
		
		Document newDocument2 = new Document();
		newDocument2.append("$set", new Document().append("tax", 145.23));
		
		Document newDocument3 = new Document();
		newDocument3.append("$set", new Document().append("tax", 112.96));
		
		Document newDocument4 = new Document();
		newDocument4.append("$set", new Document().append("tax", 156.21));
		

		Document updateQuery1 = new Document().append("_id", "5a989e4a1509ec12d817f03e");
		Document updateQuery2 = new Document().append("_id", "5a989e4a60d71cc34a54260b");
		Document updateQuery3 = new Document().append("_id", "5a989e4a24a0ed39cf3e92a0");
		Document updateQuery4 = new Document().append("_id", "5a989e4a6ff8212799790854");
		
		collection.updateOne(updateQuery1, newDocument1);
		collection.updateOne(updateQuery2, newDocument2);
		collection.updateOne(updateQuery3, newDocument3);
		collection.updateOne(updateQuery4, newDocument4);

	}
		
	@Test(expected=IllegalArgumentException.class)
	public void UpdateManyOrdersWithOperatorTest_InvalidConnectionString_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateTaxBasedOnTotalForManyOrders("", 2300.00, 230.00);	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateManyOrdersWithOperatorTest_InvalidTotal_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateTaxBasedOnTotalForManyOrders(CONNECTION_STRING, -2300.00, 230.00);	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateManyOrdersWithOperatorTest_InvalidTax_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateTaxBasedOnTotalForManyOrders(CONNECTION_STRING, 2300.00, -230.00);	    
	}
	
	@Test
	public void UpdateManyOrdersWithOperatorTest_ValidArguments_SuccessWithNoRecords_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateTaxBasedOnTotalForManyOrders(CONNECTION_STRING, 5000.00, 500.00);
		
		 //verify size
	    assertEquals(0, queryResult.size());  
	}
	
	@Test
	public void UpdateUnitPriceBasedOnSKUForManyOrdersTest() {
		
		MongoClientURI clientUri = new MongoClientURI(CONNECTION_STRING);
		MongoClient client = new MongoClient(clientUri);
		
		MongoDatabase database = client.getDatabase("stores");
		MongoCollection<Document> collection = database.getCollection("orders");
		
        List<Document> queryResult1 = collection
        		.find(and(eq("lineitems.sku", "MDBTS244")))
        		.into(new ArrayList<Document>());
        
        //verify original record
        assertEquals(2, queryResult1.size());

        Document document1 = (Document) collection
        		.find(and(eq("lineitems.sku", "MDBTS244"),eq("lineitems.unit_price", 45.23)))
        		.first()
        	    .get("lineitems", ArrayList.class)
        	    .get(1);

		Document result1 = queryResult1.get(0);
		String id1 = result1.getString("_id");
		Double unitPrice1 = document1.getDouble("unit_price");
		assertEquals("5a989e4a317af0f885a7e8f6", id1);
		assertEquals(45.23, unitPrice1, 0.00);
		
        //update data
		UpdateData updateData = new UpdateData();		
		List<Document> queryResult2 = updateData.UpdateUnitPriceBasedOnSKUForManyOrders(CONNECTION_STRING, "MDBTS244", 70.23);
		assertEquals(2, queryResult2.size());	
		
		//verify updated record
			
        Document document2 = (Document) collection
        		.find(and(eq("lineitems.sku", "MDBTS244"),eq("lineitems.unit_price", 70.23)))
        		.first()
        	    .get("lineitems", ArrayList.class)
        	    .get(1);

		Document result2 = queryResult2.get(0);
		String id2 = result2.getString("_id");
		Double unitPrice2 = document2.getDouble("unit_price");
		assertEquals("5a989e4a317af0f885a7e8f6", id2);
		assertEquals(70.23, unitPrice2, 0.00);	
	    
		//revert to original value
		
		Document newDocument1 = new Document();
		newDocument1.append("$set", new Document().append("lineitems.$.unit_price", 45.23));
		
		Document newDocument2 = new Document();
		newDocument2.append("$set", new Document().append("lineitems.$.unit_price", 56.12));
		

		Document updateQuery1 = new Document().append("lineitems.sku", "MDBTS244").append("_id", "5a989e4a317af0f885a7e8f6");
		Document updateQuery2 = new Document().append("lineitems.sku", "MDBTS244").append("_id", "5a989e4a6629619e0aecc566");
		
		collection.updateOne(updateQuery1, newDocument1);
		collection.updateOne(updateQuery2, newDocument2);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateManyOrdersForEmbeddedFieldTest_InvalidConnectionString_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateUnitPriceBasedOnSKUForManyOrders("", "MDBTS244", 70.23);	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateManyOrdersForEmbeddedFieldTest_EmptySku_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateUnitPriceBasedOnSKUForManyOrders(CONNECTION_STRING, "", 70.23);	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateManyOrdersForEmbeddedFieldTest_InvalidUnitPrice_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateUnitPriceBasedOnSKUForManyOrders(CONNECTION_STRING, "MDBTS244", -70.23);	    
	}
	
	@Test
	public void UpdateManyOrdersForEmbeddedFieldTest_ValidArguments_SuccessWithNoRecords_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateUnitPriceBasedOnSKUForManyOrders(CONNECTION_STRING, "GBPX8634", 70.23);
		
		 //verify size
	    assertEquals(0, queryResult.size());  
	}
}