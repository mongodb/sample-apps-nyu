import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.elemMatch;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static org.junit.Assert.*;

import org.bson.Document;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.junit.Test;


public class InsertDataTest {

	String CONNECTION = "mongodb+srv://m001-student:student123#@sandbox-trhqa.mongodb.net/test";
	String json = " 'orderPlaced': '2018-03-01', 'total': 13.00, 'subtotal': 191.00,"
			+ "'shipping':11.00,'tax':17.00,"
			+ "'status': ['shipped', '2018-03-04'],"
			+ "'shippingAddress': {'number': 345, 'street': 'Broadway', 'city': 'Sunnyside',"
			+"'state':'Yonkers','country':'USA','postalCode':10010},"
			+"'lineitems':[{'sku':'MDBTS115','name':'color pencil','quantity':31,'unit_price':1.35},"
			+"{'sku':'MDBTS438','name':'Highlighter','quantity':27,'unit_price':2.4}]";
	
	//Check the connection
	@Test
	public void TestConnection() {		
		MongoClientURI clientUri = new MongoClientURI(CONNECTION);
		try(MongoClient client = new MongoClient(clientUri))
		{
			assertNotNull(client);
		}    
	}
	
	//Test for adding one data set
	@Test
	public void AddOneOrderWithDataTest() {
		
        Document newOrder = new Document("orderPlaced", "2018-03-01")
        .append("total", 1633.2)
        .append("subtotal",421.42)
		.append("shipping", 91)
		.append("tax", 11.41);

     	List status = Arrays.asList("shipped","2018-03-10");
		newOrder.put("status", status);

		Document shipping = new Document ("number",113)
		.append("street", "Sunnyside")
		.append("city","Yonker")
		.append("state","New York")
		.append("country","USA")
		.append("postalCode",57850);
		newOrder.put("shippingAddress", shipping);	
		
		Document itemDetail = new Document("sku","MDBTS285")
		.append("name","color pencil")
		.append("quantity",32)
		.append("unit_price",9.3808);
		List items = Arrays.asList(itemDetail);
		newOrder.put("lineitems", items);
			 
		InsertData insertData = new InsertData();		
		List<Document> filteredDocumentsBefore = insertData.CountOrderSize(CONNECTION);
		InsertData.AddOneOrderWithData(CONNECTION, newOrder);
		List<Document> filteredDocumentsAfter = insertData.CountOrderSize(CONNECTION);
			
		assertEquals(filteredDocumentsBefore.size()+1, filteredDocumentsAfter.size());	
	}

	
	@Test(expected=IllegalArgumentException.class)
	public void AddOneOrderWithDataTest_InvalidConnectionString_ThrowsException() 
	{
		Document newOrder = new Document("orderPlaced", "2018-03-01")
        .append("total", 1633.2)
        .append("subtotal",421.42)
		.append("shipping", 91)
		.append("tax", 11.41);

     	List status = Arrays.asList("shipped","2018-03-10");
		newOrder.put("status", status);

		Document shipping = new Document ("number",113)
		.append("street", "Sunnyside")
		.append("city","Yonker")
		.append("state","New York")
		.append("country","USA")
		.append("postalCode",57850);
		newOrder.put("shippingAddress", shipping);	
		
		Document itemDetail = new Document("sku","MDBTS285")
		.append("name","color pencil")
		.append("quantity",32)
		.append("unit_price",9.3808);
		List items = Arrays.asList(itemDetail);
		newOrder.put("lineitems", items);
	
		InsertData insertData = new InsertData();	
		List<Document> AddDocuments = InsertData.AddOneOrderWithData("", newOrder );
	}
	
	
	@Test
	public void AddOneOrderWithExpDataTest_withoutID() {
	
		String orderData ="{"+json+"}";		
		InsertData insertData = new InsertData();		
		List<Document> filteredDocumentsBefore = insertData.CountOrderSize(CONNECTION);
		List<Document> AddDocuments = InsertData.AddOneOrderWithExpData(CONNECTION, orderData);
		List<Document> filteredDocumentsAfter = insertData.CountOrderSize(CONNECTION);
		// check if the data is added and the overall size has incremented by 1
		assertEquals(filteredDocumentsBefore.size()+1, filteredDocumentsAfter.size());	
		
	}
	
	@Test
	public void AddOneOrderWithExpDataTest_withID() {
	
		String orderData = "{ '_id': '7a999e1a1a11cc111fb1f99c'," +json+"}";
					
		InsertData insertData = new InsertData();		
		List<Document> filteredDocumentsBefore = insertData.CountOrderSize(CONNECTION);
		List<Document> AddDocuments = InsertData.AddOneOrderWithExpData(CONNECTION, orderData);
		List<Document> filteredDocumentsAfter = insertData.CountOrderSize(CONNECTION);
		// check if the data is added and the overall size has incremented by 1
		assertEquals(filteredDocumentsBefore.size()+1, filteredDocumentsAfter.size());	
		// check if the above data can be found in the collection. Meaning it is inserted
		Document result = AddDocuments.get(filteredDocumentsAfter.size()-1);
	    String id = result.getString("_id");
	    assertEquals("7a999e1a1a11cc111fb1f99c",id);	
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void AddOneOrderWithExpDataTest_InvalidConnectionString_ThrowsException() 
	{
		String orderData ="{"+json+"}";		
		InsertData insertData = new InsertData();		
		List<Document> AddDocuments = InsertData.AddOneOrderWithExpData("", orderData);
	}
	/*
	@Test
	public void AddOneOrderJsonFileTest() {
		
		InsertData insertData = new InsertData();		
		List<Document> filteredDocumentsBefore = insertData.CountOrderSize(CONNECTION);
		List<Document> AddDocuments = insertData.AddOneOrderJsonFile(CONNECTION,"simpledata.json");
		List<Document> filteredDocumentsAfter = insertData.CountOrderSize(CONNECTION);
		// check if the data is added and the overall size has incremented by 1
	//	assertEquals(filteredDocumentsBefore.size()+1, filteredDocumentsAfter.size());	
		Document result = AddDocuments.get(filteredDocumentsAfter.size()-1);
	    String id = result.getString("_id");
	    assertEquals("8a999e1a1a11cc111fb1f99c",id);	
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void AddOneOrderJsonFileTest_ExistingID_ThrowsException() 
	{
		InsertData insertData = new InsertData();		
		List<Document> AddDocuments = InsertData.AddOneOrderJsonFile(CONNECTION,"simpledata.json");
	    
	}*/

}	
