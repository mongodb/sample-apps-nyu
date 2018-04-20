# MongoDB Java Driver Find Method with Scalar Fields

## Introduction

This is a sample application that demonstrates MongoDB Java Driver's find functionality with scalar fields. It has four methods:

* one that illustrates a simple equality match 
* one that illustrates an equality match on two or more fields
* one that illustrates a range query on a numeric field 
* one that illustrates a range query by date.

The last two methods also make use of 'project'.

## Getting Started

The sample application queries data from MongoDB Atlas free-tier cluster. The cluster contains stores.orders collection which mimics an e-commerce database. The general schema is as follows: 

```js
{
  _id: ObjectId(),
  orderPlaced: datetime(),
  total: NumberDecimal("153.00"),
  subtotal: NumberDecimal("141.00"),
  shipping: NumberDecimal("5.00"),
  tax: NumberDecimal("7.00"),
  status: ["shipped", datetime()],
  shippingAddress: {
    number: 345,
    street: Alvin St.,
    city: Madison
    state: WI,
    country: USA
    postalCode: 53558
  },
  lineitems: [
    { sku: "MDBTS001",
      name: "Flannel T-shirt",
      quantity: 10,
      unit_price: NumberDecimal("9.00") },
    { sku: "MDBTS002",
      quantity: 5,
      unit_price: NumberDecimal("10.00")}] 
}
```

## Description 

### Simple equality match 

```java
//option 1 - Static import the required filters
List<Document> ordersFiltered = collection
		.find(eq("shippingAddress.postalCode",postalcode))
		.into(new ArrayList<Document>());
				
//option 2  - create bson filter
Bson filter = new Document("shippingAddress.postalcode",postalcode);
List<Document> ordersFiltered = collection
			.find(filter)
			.into(new ArrayList<Document>());
```

This method illustrates a simple equality match. It takes the **shipping address postal code** as the parameter and returns a list of orders where shipping address postal code matches the parameter.

```java
public List<Document> GetOrdersByPostalCode(String connectionString, int postalcode){
	if(connectionString == null || connectionString.isEmpty() || postalcode<0)
	{
		throw new IllegalArgumentException();
	}
	else
	{
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
			MongoDatabase database = client.getDatabase("stores");			
			MongoCollection<Document> collection = database.getCollection("orders");		
				
			//option 1 - Static import the required filters
			List<Document> ordersFiltered = collection
					.find(eq("shippingAddress.postalCode",postalcode))
					.into(new ArrayList<Document>());
				
			//option 2  - create bson filter
			/*Bson filter = new Document("shippingAddress.postalcode",postalcode);
			List<Document> ordersFiltered = collection
					.find(filter)
					.into(new ArrayList<Document>());*/
				
			return ordersFiltered;
		}
		catch (Exception e) {
			System.out.println("Exception occured");
			System.out.println("Details:");
			System.out.println(e.getStackTrace());
			return null;
		}	
	}			
}
```



### Equality match on two or more fields

```java
//option 1 - Static import the required filters
List<Document> ordersFiltered = collection
		.find(and(eq("shippingAddress.postalcode",postalcode),eq("total",total)))				
		.into(new ArrayList<Document>());*/
				
//option 2  - create basicDBObject and append filters			
BasicDBObject filters = new BasicDBObject();
filters.append("shippingAddress.postalCode", postalcode);
filters.append("orderPlaced", orderPlacedDate);			
List<Document> ordersFiltered = collection
		.find(filters)
		.into(new ArrayList<Document>());
```
This method illustrates an equality match on two or more fields. It takes **order placed date** and **shipping address postal code** as parameters and returns a list of only those orders that match both the parameters.

```java
public List<Document> GetOrdersByOrderPlacedDateAndPostalCode(String connectionString, Date orderPlacedDate,  int postalcode){
	if(connectionString == null || connectionString.isEmpty() || postalcode<0 || orderPlacedDate == null)
	{
		throw new IllegalArgumentException();
	}
	else
	{
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
			MongoDatabase database = client.getDatabase("stores");		
			MongoCollection<Document> collection = database.getCollection("orders");		
				
			//option 1 - Static import the required filters
			/*List<Document> ordersFiltered = collection
					.find(and(eq("shippingAddress.postalcode",postalcode),eq("total",total)))				
					.into(new ArrayList<Document>());*/
				
			//option 2  - create basicDBObject and append filters			
			BasicDBObject filters = new BasicDBObject();
			filters.append("shippingAddress.postalCode", postalcode);
			filters.append("orderPlaced", orderPlacedDate);			
			List<Document> ordersFiltered = collection
					.find(filters)
					.into(new ArrayList<Document>());
			
			return ordersFiltered;
		}
		catch (Exception e) {
			System.out.println("Exception occured");
			System.out.println("Details:");
			System.out.println(e.getStackTrace());
			return null;
		}	
	}		
}
```

### Range query on a numeric field

```java
//option 1 - Static import the required filters
List<Document> ordersFiltered = collection
		.find(and(gt("total",lowerBoundTotal),lte("total",upperBoundTotal)))
		.projection(fields(include("subtotal","shipping","tax"),excludeId()))
		.into(new ArrayList<Document>());
			
//option 2  - create basicDBObject and append filters			
BasicDBObject filters = new BasicDBObject();
filters.append("total", new BasicDBObject("$gt", lowerBoundTotal));
filters.append("total", new BasicDBObject("$lte", upperBoundTotal));						
List<Document> ordersFiltered = collection
			.find(filters)
			.projection(new BasicDBObject("subtotal",true)
							.append("shipping", true)
							.append("tax", true)
							.append("_id", false))
			.into(new ArrayList<Document>());
```
This method illustrates a range query on a numeric field. It takes the lower and upper limits of the order **total** as parameters. It returns a list of only those orders where the order **total** is greater than the lower limit and, less than or equal to the upper limit. Using projection, only select fields of an order such as **subtotal, shipping, tax** are returned. The **'_id'** field is explicitly excluded. 

```java
public List<Document> GetOrdersInRangeOfTotal(String connectionString, Double lowerBoundTotal, Double upperBoundTotal ){
	if(connectionString == null || connectionString.isEmpty() || lowerBoundTotal<0 || upperBoundTotal<0 || lowerBoundTotal>=upperBoundTotal)
	{
		throw new IllegalArgumentException();
	}
	else
	{
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
			MongoDatabase database = client.getDatabase("stores");	
			MongoCollection<Document> collection = database.getCollection("orders");		
				
			//option 1 - Static import the required filters
			List<Document> ordersFiltered = collection
					.find(and(gt("total",lowerBoundTotal),lte("total",upperBoundTotal)))
					.projection(fields(include("subtotal","shipping","tax"),excludeId()))
					.into(new ArrayList<Document>());
			
			//option 2  - create basicDBObject and append filters			
			/*BasicDBObject filters = new BasicDBObject();
			filters.append("total", new BasicDBObject("$gt", lowerBoundTotal));
			filters.append("total", new BasicDBObject("$lte", upperBoundTotal));						
			List<Document> ordersFiltered = collection
					.find(filters)
					.projection(new BasicDBObject("subtotal",true)
									.append("shipping", true)
									.append("tax", true)
									.append("_id", false))
					.into(new ArrayList<Document>());*/
			
			return ordersFiltered;
		}
		catch (Exception e) {
			System.out.println("Exception occured");
			System.out.println("Details:");
			System.out.println(e.getStackTrace());
			return null;
		}	
	}			
}
```

### Range query by date

```java
//option 1 - Static import the required filters
List<Document> ordersFiltered = collection
		.find(and(gte("orderPlaced",startDate),lt("orderPlaced",endDate)))
		.projection(fields(include("total"),excludeId()))
		.into(new ArrayList<Document>());*/
			
//option 2  - create basicDBObject and append filters			
BasicDBObject filters = new BasicDBObject();
filters.append("orderPlaced", new BasicDBObject("$gte", startDate));
filters.append("orderPlaced", new BasicDBObject("$lt", endDate));						
List<Document> ordersFiltered = collection
			.find(filters)
			.projection(new BasicDBObject("total",true)
							.append("_id", false))
			.into(new ArrayList<Document>());
```

This method illustrates a range query by date. It takes the start and end dates of the **order placed date** as parameters. It returns a list of only those orders where the **order placed date** falls between the start and end dates. Using projection, only the order **total** field is returned and the '_id' field is explicitly excluded.

```java
public List<Document> GetOrdersBetweenDates(String connectionString, Date startDate, Date endDate){
	if(connectionString == null || connectionString.isEmpty() || startDate == null || endDate == null || startDate.compareTo(endDate) >0)
	{
		throw new IllegalArgumentException();
	}
	else
	{
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
			MongoDatabase database = client.getDatabase("stores");	
			MongoCollection<Document> collection = database.getCollection("orders");
			
			//option 1 - Static import the required filters
			/*List<Document> ordersFiltered = collection
					.find(and(gte("orderPlaced",startDate),lt("orderPlaced",endDate)))
					.projection(fields(include("total"),excludeId()))
					.into(new ArrayList<Document>());*/
			
			//option 2  - create basicDBObject and append filters			
			BasicDBObject filters = new BasicDBObject();
			filters.append("orderPlaced", new BasicDBObject("$gte", startDate));
			filters.append("orderPlaced", new BasicDBObject("$lt", endDate));						
			List<Document> ordersFiltered = collection
					.find(filters)
					.projection(new BasicDBObject("total",true)
									.append("_id", false))
					.into(new ArrayList<Document>());
			
			return ordersFiltered;
		}
		catch (Exception e) {
			System.out.println("Exception occured");
			System.out.println("Details:");
			System.out.println(e.getStackTrace());
			return null;
		}	
	}	
}

```



## Running the tests

The unit test cases are written using JUnit 4 framework. You can find them here: 

    .
    ├── ...
    ├── testsrc                    		       # Source folder for all unit tests
    │   ├── FindWithScalarFieldsTest.java              # Source file for all unit tests

### Description

This unit test verifies connection to MongoDB Atlas cluster.
```java
@Test
	public void TestConnection() {		
		MongoClientURI clientUri = new MongoClientURI(CONNECTION_STRING);
		try(MongoClient client = new MongoClient(clientUri))
		{
			assertNotNull(client);
		}    
	}
```

These unit tests verify the GetOrdersByPostalCode method. Valid scenarios along with edge cases like invalid connection string and invalid postal code are verified. 

```java
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
```
These unit tests verify the GetOrdersByOrderPlacedDateAndPostalCode method. Valid scenarios along with edge cases like invalid connection string, invalid order placed date and invalid postal code are verified. 

```java
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
```
These unit tests verify the GetOrdersInRangeOfTotal method. Valid scenarios along with edge cases like invalid total and invalid range of totals are verified. 

```java
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
```
These unit tests verify the GetOrdersBetweenDates method. Valid scenarios along with edge cases like invalid start/end date and invalid range of dates are verified.
```java
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
```

