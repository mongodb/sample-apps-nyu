import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;


public class FindWithScalarFieldsTest {
    
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
	public void TestSimpleEqualityMatch() {
		FindWithScalarFields findWithScalarFields = new FindWithScalarFields();		
	    List<Document> filteredDocuments = findWithScalarFields.SimpleEqualityMatch(CONNECTION_STRING,51173);
	    
	    //verify size
	    assertEquals(1, filteredDocuments.size());	
	    
	    //verify record
	    Document result = filteredDocuments.get(0);
	    String id = result.getString("_id");
	    assertEquals("5a989e4ab3039ef2f252d078", id);	    
	}
	
	@Test
	public void TestMultipleFieldsEqualityMatch() {
		FindWithScalarFields findWithScalarFields = new FindWithScalarFields();		
	    List<Document> filteredDocuments = findWithScalarFields.MultipleFieldsEqualityMatch(CONNECTION_STRING,10190,1297.9081);
	    
	    //verify size
	    assertEquals(1, filteredDocuments.size());	
	    
	    //verify record
	    Document result = filteredDocuments.get(0);
	    String id = result.getString("_id");
	    assertEquals("5a989e4ac5fb0ab8c29ac345", id);
	    
	}
	
	@Test
	public void TestRangeQueryOnNumber() {
		FindWithScalarFields findWithScalarFields = new FindWithScalarFields();		
	    List<Document> filteredDocuments = findWithScalarFields.RangeQueryOnNumber(CONNECTION_STRING,100.00,150.00);
	    
	    //verify size
	    assertEquals(1, filteredDocuments.size());	
	    
	    //verify record
	    Document result = filteredDocuments.get(0);
	    	    
	    Double subtotalActual = result.getDouble("subtotal");
	    Double subtotalExpected = 213.4011;
	    assertEquals(subtotalExpected, subtotalActual);
	    
	    Double shippingActual = result.getDouble("shipping");
	    Double shippingExpected = 304.00;
	    assertEquals(shippingExpected, shippingActual);
	    
	    Double taxActual = result.getDouble("tax");
	    Double taxExpected = 71.9626;
	    assertEquals(taxExpected, taxActual);
	     
	}
	
	@Test
	public void TestRangeQueryByDate() throws ParseException {
		Date startDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse("2018-01-16T00:00:00.000");
		Date endDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse("2018-01-19T00:00:00.000");
		
		FindWithScalarFields findWithScalarFields = new FindWithScalarFields();		
	    List<Document> filteredDocuments = findWithScalarFields.RangeQueryByDate(CONNECTION_STRING,startDate,endDate);
	    
	    //verify size
	    assertEquals(1, filteredDocuments.size());
	    
	    //verify result
	    Document result = filteredDocuments.get(0);
	    Double totalExpected = 787.1119;
	    Double totalActual = result.getDouble("total");
	    assertEquals(totalExpected, totalActual);
	}
}
