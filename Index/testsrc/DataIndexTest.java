import static org.junit.Assert.*;

import java.util.List;

import org.bson.Document;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


public class DataIndexTest {

	String connectionString = "mongodb+srv://m001-student:student123#@sandbox-trhqa.mongodb.net/test";
	MongoClientURI clientUri = new MongoClientURI(connectionString);
	MongoClient client = new MongoClient(clientUri);
	MongoDatabase database = client.getDatabase("stores");
	MongoCollection<Document> collection = database.getCollection("orders");
	String city = "Texas";
	//Check the connection
		@Test
		public void TestConnection() {		
			MongoClientURI clientUri = new MongoClientURI(connectionString);
			try(MongoClient client = new MongoClient(clientUri))
			{
				assertNotNull(client);
			}    
		}
		
		@Test
		public void checkIndexBefore()
		{
			DataIndex d_index = new DataIndex();	
			List<Document> filteredDocuments = d_index.GetShippingByCityWithoutIndex(connectionString,city);
			int line = d_index.GetShippingByCityIndexSize(connectionString, city);
			assertEquals(filteredDocuments.size(),5);
			assertEquals(line,1);
	//		d_index.dropIndex(connectionString, city);
			
		}
		
		@Test(expected=IllegalArgumentException.class)
		public void checkIndexBefore_InvalidConnectionString_ThrowsException() 
		{
			DataIndex d_index = new DataIndex();	
			List<Document> filteredDocuments = d_index.GetShippingByCityWithoutIndex("",city);
			
		}
		
		@Test(expected=IllegalArgumentException.class)
		public void checkIndexBefore_emptyValue_ThrowsException() 
		{
			DataIndex d_index = new DataIndex();	
			List<Document> filteredDocuments = d_index.GetShippingByCityWithoutIndex(connectionString,"");
			
		}
		
		
		@Test
		public void checkIndexSizeAfter()
		{
			DataIndex d_index = new DataIndex();	
			List<Document> filteredDocuments = d_index.GetShippingByCityWithIndex(connectionString,city);
			int line = d_index.GetShippingByCityIndexSize(connectionString, city);
			assertEquals(filteredDocuments.size(),5);
			assertEquals(line,2);
			d_index.dropIndex(connectionString, city);
		}
		
		@Test(expected=IllegalArgumentException.class)
		public void checkIndexSizeAfter_InvalidConnectionString_ThrowsException() 
		{
			DataIndex d_index = new DataIndex();	
			List<Document> filteredDocuments = d_index.GetShippingByCityWithoutIndex("",city);
			
		}
		
		@Test(expected=IllegalArgumentException.class)
		public void checkIndexSizeAfter_emptyValue_ThrowsException() 
		{
			DataIndex d_index = new DataIndex();	
			List<Document> filteredDocuments = d_index.GetShippingByCityWithoutIndex(connectionString,"");
			
		}

}
