import static org.junit.Assert.*;

import java.util.List;
import org.bson.Document;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;


public class EmbeddedDocumentsQueryTest {
    
	static String CONNECTION_STRING = "mongodb+srv://Test:Test987#@cluster0-jxlpi.mongodb.net/test";
	
	@Test
	public void TestConnection() {		
		MongoClientURI clientUri = new MongoClientURI(CONNECTION_STRING);
		try(MongoClient client = new MongoClient(clientUri))
		{
			assertNotNull(client);
		}    
	}

	@Test
	public void SingleNestedFieldEqualityMatchTest() {
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.SingleNestedFieldEqualityMatch(CONNECTION_STRING,"Woodburn");
	    
	    //verify size
	    assertEquals(1, filteredDocuments.size());	
	    
	    //verify record
	    Document result = filteredDocuments.get(0);
	    String id = result.getString("_id");
	    assertEquals("5a989e4abf545f22ed33d590", id);	    
	}
	
	
	@Test
	public void TwoNestedFieldsEqualityMatchTest() {
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.TwoNestedFieldsEqualityMatch(CONNECTION_STRING,"Texas","Convent");
	    
	    //verify size
	    assertEquals(1, filteredDocuments.size());	
	    
	    //verify record
	    Document result = filteredDocuments.get(0);
	    String id = result.getString("_id");
	    assertEquals("5a989e4a151dd8d28832eb2f", id);
	    
	}
	
	@Test
	public void OperatorNestedFieldTest() {
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.OperatorNestedField(CONNECTION_STRING,95.00);
	    
	    //verify size
	    assertEquals(1, filteredDocuments.size());	
	    
	    //verify record
	    Document result = filteredDocuments.get(0);
	    String id = result.getString("_id");
	    assertEquals("5a989e4a86d089e9b9c6ea65", id);
	    
	}
	
	@Test
	public void NestedNonNestedEqualityMatchTest() {
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.NestedNonNestedEqualityMatch(CONNECTION_STRING,"Ellerslie",1148.929);
	    
	    //verify size
	    assertEquals(1, filteredDocuments.size());	
	    
	    //verify record
	    Document result = filteredDocuments.get(0);
	    String id = result.getString("_id");
	    assertEquals("5a989e4a4ce1884736136621", id);
	    
	}
	
}
