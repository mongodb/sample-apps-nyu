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
	public void GetOrdersByPostalCode_ValidArguments_SuccessWithRecords() {
		FindWithScalarFields findWithScalarFields = new FindWithScalarFields();		
	    List<Document> filteredDocuments = findWithScalarFields.GetOrdersByPostalCode(CONNECTION_STRING,51173);
	    
	    //verify size
	    assertEquals(1, filteredDocuments.size());	
	    
	    //verify record
	    Document result = filteredDocuments.get(0);
	    String id = result.getString("_id");
	    assertEquals("5a989e4ab3039ef2f252d078", id);	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void GetOrdersByPostalCode_InvalidConnectionString_ThrowsException() {
		FindWithScalarFields findWithScalarFields = new FindWithScalarFields();		
	    List<Document> filteredDocuments = findWithScalarFields.GetOrdersByPostalCode("",51173);	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void GetOrdersByPostalCode_InvalidPostalCode_ThrowsException() {
		FindWithScalarFields findWithScalarFields = new FindWithScalarFields();		
	    List<Document> filteredDocuments = findWithScalarFields.GetOrdersByPostalCode(CONNECTION_STRING,-51173);	    
	}
	
	@Test
	public void GetOrdersByPostalCode_ValidArguments_SuccessWithNoRecords() {
		FindWithScalarFields findWithScalarFields = new FindWithScalarFields();		
	    List<Document> filteredDocuments = findWithScalarFields.GetOrdersByPostalCode(CONNECTION_STRING,00001);
	    
	    //verify size
	    assertEquals(0, filteredDocuments.size());  
	}
			
	@Test
	public void GetOrdersByOrderPlacedDateAndPostalCode_ValidArguments_SuccessWithRecords() throws ParseException {
		Date orderPlacedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse("2018-01-16T19:00:00.000");
		FindWithScalarFields findWithScalarFields = new FindWithScalarFields();		
	    List<Document> filteredDocuments = findWithScalarFields.GetOrdersByOrderPlacedDateAndPostalCode(CONNECTION_STRING,orderPlacedDate,70839);
	    
	    //verify size
	    assertEquals(1, filteredDocuments.size());	
	    
	    //verify record
	    Document result = filteredDocuments.get(0);
	    String id = result.getString("_id");
	    assertEquals("5a989e4abc7ca02a896847a1", id);
	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void GetOrdersByOrderPlacedDateAndPostalCode_InvalidConnectionString_ThrowsException() throws ParseException {
		Date orderPlacedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse("2018-01-16T19:00:00.000");
		FindWithScalarFields findWithScalarFields = new FindWithScalarFields();		
		List<Document> filteredDocuments = findWithScalarFields.GetOrdersByOrderPlacedDateAndPostalCode("",orderPlacedDate,70839);	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void GetOrdersByOrderPlacedDateAndPostalCode_InvalidPostalCode_ThrowsException() throws ParseException {
		Date orderPlacedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse("2018-01-16T19:00:00.000");
		FindWithScalarFields findWithScalarFields = new FindWithScalarFields();		
		List<Document> filteredDocuments = findWithScalarFields.GetOrdersByOrderPlacedDateAndPostalCode(CONNECTION_STRING,orderPlacedDate,-10190);	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void GetOrdersByOrderPlacedDateAndPostalCode_InvalidOrderPlacedDate_ThrowsException() {
		FindWithScalarFields findWithScalarFields = new FindWithScalarFields();		
		List<Document> filteredDocuments = findWithScalarFields.GetOrdersByOrderPlacedDateAndPostalCode(CONNECTION_STRING,null,70839);	    
	}
	
	@Test
	public void GetOrdersByOrderPlacedDateAndPostalCode_ValidArguments_SuccessWithNoRecords() throws ParseException {
		Date orderPlacedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse("2018-01-16T19:00:00.000");
		FindWithScalarFields findWithScalarFields = new FindWithScalarFields();		
	    List<Document> filteredDocuments = findWithScalarFields.GetOrdersByOrderPlacedDateAndPostalCode(CONNECTION_STRING,orderPlacedDate,70707);
	    
	    //verify size
	    assertEquals(0, filteredDocuments.size());   
	}
	
	@Test
	public void GetOrdersInRangeOfTotal_ValidArguments_SuccessWithRecords() {
		FindWithScalarFields findWithScalarFields = new FindWithScalarFields();		
	    List<Document> filteredDocuments = findWithScalarFields.GetOrdersInRangeOfTotal(CONNECTION_STRING,100.00,150.00);
	    
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
	
	@Test(expected=IllegalArgumentException.class)
	public void GetOrdersInRangeOfTotal_InvalidConnectionString_ThrowsException() {
		FindWithScalarFields findWithScalarFields = new FindWithScalarFields();		
	    List<Document> filteredDocuments = findWithScalarFields.GetOrdersInRangeOfTotal("",100.00,150.00);	     
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void GetOrdersInRangeOfTotal_InvalidLowerboundTotal_ThrowsException() {
		FindWithScalarFields findWithScalarFields = new FindWithScalarFields();		
	    List<Document> filteredDocuments = findWithScalarFields.GetOrdersInRangeOfTotal(CONNECTION_STRING,-100.00,150.00);	     
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void GetOrdersInRangeOfTotal_InvalidUpperboundTotal_ThrowsException() {
		FindWithScalarFields findWithScalarFields = new FindWithScalarFields();		
	    List<Document> filteredDocuments = findWithScalarFields.GetOrdersInRangeOfTotal(CONNECTION_STRING,100.00,-150.00);	     
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void GetOrdersInRangeOfTotal_InvalidRangeofTotal_ThrowsException() {
		FindWithScalarFields findWithScalarFields = new FindWithScalarFields();		
	    List<Document> filteredDocuments = findWithScalarFields.GetOrdersInRangeOfTotal(CONNECTION_STRING,150.00,100.00);	     
	}
	
	@Test
	public void GetOrdersInRangeOfTotal_ValidArguments_SuccessWithNoRecords() {
		FindWithScalarFields findWithScalarFields = new FindWithScalarFields();		
	    List<Document> filteredDocuments = findWithScalarFields.GetOrdersInRangeOfTotal(CONNECTION_STRING,1.00,1.50);
	    
	    //verify size
	    assertEquals(0, filteredDocuments.size());	     
	}
	
	@Test
	public void GetOrdersBetweenDates_ValidArguments_SuccessWithRecords() throws ParseException {
		Date startDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse("2018-01-16T00:00:00.000");
		Date endDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse("2018-01-19T00:00:00.000");
		
		FindWithScalarFields findWithScalarFields = new FindWithScalarFields();		
	    List<Document> filteredDocuments = findWithScalarFields.GetOrdersBetweenDates(CONNECTION_STRING,startDate,endDate);
	    
	    //verify size
	    assertEquals(1, filteredDocuments.size());
	    
	    //verify result
	    Document result = filteredDocuments.get(0);
	    Double totalExpected = 787.1119;
	    Double totalActual = result.getDouble("total");
	    assertEquals(totalExpected, totalActual);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void GetOrdersBetweenDates_InvalidConnectionString_ThrowsException() throws ParseException {
		Date startDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse("2018-01-16T00:00:00.000");
		Date endDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse("2018-01-19T00:00:00.000");
		
		FindWithScalarFields findWithScalarFields = new FindWithScalarFields();		
	    List<Document> filteredDocuments = findWithScalarFields.GetOrdersBetweenDates("",startDate,endDate);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void GetOrdersBetweenDates_InvalidStartDate_ThrowsException() throws ParseException {
		Date startDate = null;
		Date endDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse("2018-01-19T00:00:00.000");
		FindWithScalarFields findWithScalarFields = new FindWithScalarFields();		
	    List<Document> filteredDocuments = findWithScalarFields.GetOrdersBetweenDates(CONNECTION_STRING,startDate,endDate);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void GetOrdersBetweenDates_InvalidEndDate_ThrowsException() throws ParseException {
		Date startDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse("2018-01-16T00:00:00.000");
		Date endDate = null;
		
		FindWithScalarFields findWithScalarFields = new FindWithScalarFields();		
	    List<Document> filteredDocuments = findWithScalarFields.GetOrdersBetweenDates(CONNECTION_STRING,startDate,endDate);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void GetOrdersBetweenDates_InvalidRangeOfDates_ThrowsException() throws ParseException {
		Date startDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse("2018-01-19T00:00:00.000");	
		Date endDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse("2018-01-16T00:00:00.000");
		
		FindWithScalarFields findWithScalarFields = new FindWithScalarFields();		
	    List<Document> filteredDocuments = findWithScalarFields.GetOrdersBetweenDates(CONNECTION_STRING,startDate,endDate);
	}
	
	@Test
	public void GetOrdersBetweenDates_ValidArguments_SuccessWithNoRecords() throws ParseException {
		Date startDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse("1888-01-16T00:00:00.000");
		Date endDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse("1889-01-19T00:00:00.000");
		
		FindWithScalarFields findWithScalarFields = new FindWithScalarFields();		
	    List<Document> filteredDocuments = findWithScalarFields.GetOrdersBetweenDates(CONNECTION_STRING,startDate,endDate);
	    
	    //verify size
	    assertEquals(0, filteredDocuments.size());
	}
}
