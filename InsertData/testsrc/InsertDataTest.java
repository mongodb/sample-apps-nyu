import static com.mongodb.client.model.Filters.in;
import static org.junit.Assert.*;

import org.bson.Document;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;

import org.json.JSONException;
import org.json.simple.parser.ParseException;
import org.junit.Test;


public class InsertDataTest {

	String CONNECTION = "mongodb+srv://m001-student:student123#@sandbox-trhqa.mongodb.net/test";
	
	 
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
		
		String sku = "MDBTS110";
		String item = "color pencil";
		Double unit_price = 9.50;
		int quantity = 32;
		
		InsertData insertData = new InsertData();		
		List<Document> filteredDocumentsBefore = insertData.CountOrderSize(CONNECTION);
		InsertData.AddOneOrderWithData(CONNECTION, sku, item, unit_price, quantity);
		List<Document> filteredDocumentsAfter = insertData.CountOrderSize(CONNECTION);
			
		assertEquals(filteredDocumentsBefore.size()+1, filteredDocumentsAfter.size());	
	}

	
	@Test(expected=IllegalArgumentException.class)
	public void AddOneOrderWithDataTest_InvalidConnectionString_ThrowsException() 
	{
		InsertData insertData = new InsertData();	
		List<Document> AddDocuments = InsertData.AddOneOrderWithData("", "MDBTS120", "color pencil", 9.50, 32 );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void AddOneOrderWithDataTest_emptyValue_ThrowsException() 
	{
		InsertData insertData = new InsertData();	
		List<Document> AddDocuments = InsertData.AddOneOrderWithData(CONNECTION, "", "color pencil", 0.0, 32 );
	}
	
	
	@Test
	public void AddOneOrderWithFileTest() throws JSONException, IOException, ParseException {
		
		InsertData insertData = new InsertData();		
		List<Document> filteredDocumentsBefore = insertData.CountOrderSize(CONNECTION);
		List<Document> AddDocuments = insertData.AddOneOrderWithFile(CONNECTION,"simpledata.json");
		List<Document> filteredDocumentsAfter = insertData.CountOrderSize(CONNECTION);
		// check if the data is added and the overall size has incremented by 1
		
		assertEquals(filteredDocumentsBefore.size()+1, filteredDocumentsAfter.size());	
		Document result = AddDocuments.get(filteredDocumentsAfter.size()-1);
		String id = result.getString("_id");
		assertEquals("9a999e1a1a11cc111fb1f99c",id);	
	   
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void AddOneOrderWithFileTest_InvalidConnectionString_ThrowsException() throws IOException, ParseException   
	{
		InsertData insertData = new InsertData();	
		List<Document> AddDocuments = insertData.AddOneOrderWithFile("", "simpledata.json" );
	}
	
	@Test(expected=FileNotFoundException.class)
	public void AddOneOrderWithFileTest_InvalidFile_ThrowsException() throws IOException, ParseException   
	{
		InsertData insertData = new InsertData();	
		List<Document> AddDocuments = insertData.AddOneOrderWithFile(CONNECTION, "simple.json" );
		
	}
		
	
	@Test
	public void AddMultipleOrdersWithFilesTest() throws FileNotFoundException {
		
		InsertData insertData = new InsertData();		
		List<Document> multipleOrder = new ArrayList<>();
		  
		String[] orderfiles = new String[3];
		
		for (int i=0; i< orderfiles.length; i++)
		{
			orderfiles[i] = "order"+(i+1)+".json";
		}
		
		List<Document> filteredDocumentsBefore = insertData.CountOrderSize(CONNECTION);
		List<Document> AddDocuments = insertData.AddMultipleOrdersWithFiles(CONNECTION, orderfiles);
		List<Document> filteredDocumentsAfter = insertData.CountOrderSize(CONNECTION);
		
		// check if the data is added and the overall size has incremented by the size of the array
		assertEquals(filteredDocumentsBefore.size()+orderfiles.length, filteredDocumentsAfter.size());	
		Document result = AddDocuments.get(filteredDocumentsAfter.size()-1);
		String id = result.getString("_id");
		assertEquals("3a333e3a3a33cc333fb3f33c",id);	
	
		
	}
	
	@Test
	public void AddMultipleOrderWithDataTest() {
		
		String[] sku = {"MAERW111","MBERQ112","MCERT113"};
		String[] item = {"color pencil","marker","compass"};
		Double[] unit = {5.0, 24.5, 35.20};
		int[] quant= {7,14,23};
		
		InsertData insertData = new InsertData();		
		List<Document> filteredDocumentsBefore = insertData.CountOrderSize(CONNECTION);
		InsertData.AddMultipleOrderWithData(CONNECTION, sku, item, unit, quant);
		List<Document> filteredDocumentsAfter = insertData.CountOrderSize(CONNECTION);
			
		assertEquals(filteredDocumentsBefore.size()+3, filteredDocumentsAfter.size());	
	}

	
	@Test(expected=IllegalArgumentException.class)
	public void AddMultipleOrderWithDataTest_InvalidConnectionString_ThrowsException() 
	{
		String[] sku = {"MAERW111","MBERQ112","MCERT113"};
		String[] item = {"color pencil","marker","compass"};
		Double[] unit = {5.0, 24.5, 35.20};
		int[] quant= {7, 14,23};
		
		InsertData insertData = new InsertData();	
		List<Document> AddDocuments = insertData.AddMultipleOrderWithData("",  sku, item, unit, quant );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void AddMultipleOrderWithDataTest_emptyValue_ThrowsException() 
	{
		String[] sku = {};
		String[] item = {"color pencil","marker","compass"};
		Double[] unit = {};
		int[] quant= {7, 14,23};
		
		InsertData insertData = new InsertData();	
		List<Document> AddDocuments = insertData.AddMultipleOrderWithData("",  sku, item, unit, quant );
	}

	
	@Test
	public void RemoveAddedOrders() 
	{
		
		MongoClientURI clientUri = new MongoClientURI(CONNECTION);
		MongoClient client = new MongoClient(clientUri);
		MongoDatabase database = client.getDatabase("stores");
		MongoCollection<Document> collection = database.getCollection("orders");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String today = formatter.format(date);
		String[] ddatess = {today,"2018-03-14","2018-03-11", "2018-04-11"};
		DeleteResult deletedOrder = collection.deleteMany(in("orderPlaced",ddatess));
		
	
		System.out.println("Number of Orders removed: "+deletedOrder.getDeletedCount());
	}
	
}	
