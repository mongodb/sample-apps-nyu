# MongoDB Java Driver Delete Data

## Introduction

This is a sample application that demonstrates MongoDB Java Driver's delete functionality. It has four methods:

* one that illustrates removal of multiple records based on a condition.
* one that illustrates removal of one record based on a condition.
* one that illustrates removal of all records. 
* one that illustrates dropping a collection.

## Getting Started

The sample application demonstrates deletion of data from a MongoDB Atlas free-tier cluster. The cluster contains stores.orders collection which mimics an e-commerce database. The general schema is as follows: 

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

### Delete Many

```java
DeleteResult deletedOrders = collection.deleteMany(and(eq("status","cancelled"),lt("orderPlaced", thresholdDate)));
```
This method illustrates delete many functionality based on a condition. It takes the **threshold date** as the parameter, deletes all cancelled orders older than the that and returns the count of deleted orders.

```java
public long RemoveAllCancelledOrders(String connectionString, Date thresholdDate){
	if(connectionString == null || connectionString.isEmpty() || thresholdDate == null)
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
			
			DeleteResult deletedOrders = collection.deleteMany(and(eq("status","cancelled"),lt("orderPlaced", thresholdDate)));
			System.out.println("Number of Orders removed: "+deletedOrders.getDeletedCount());
			return deletedOrders.getDeletedCount();								
		}
		catch (Exception e) {
			System.out.println("Exception occured");
			System.out.println("Details:");
			System.out.println(e.getStackTrace());
			return -1;
		}	
	}			
}
```

 

### Delete One

```java
DeleteResult deletedOrders = collection.deleteOne(and(eq("status","cancelled"),lt("orderPlaced", thresholdDate)));
```
This method illustrates delete one functionality based on a condition. It takes the **threshold date** as the parameter, deletes one cancelled order older than the that and returns the count of deleted orders. 

```java
public long RemoveOneCancelledOrder(String connectionString, Date thresholdDate){
	if(connectionString == null || connectionString.isEmpty() || thresholdDate == null )
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
			
			DeleteResult deletedOrders = collection.deleteOne(and(eq("status","cancelled"),lt("orderPlaced", thresholdDate)));
			System.out.println("Number of Orders removed: "+deletedOrders.getDeletedCount());
			return deletedOrders.getDeletedCount();								
		}
		catch (Exception e) {
			System.out.println("Exception occured");
			System.out.println("Details:");
			System.out.println(e.getStackTrace());
			return -1;
		}	
	}			
}
```



### Delete Many - No conditions (Remove all)

```java
DeleteResult deletedOrders = collection.deleteMany(new Document());
```
This method illustrates how to remove all records in a collection. It returns the count of deleted records. Even though all records all deleted, the collection and the indexes remain untouched. If you wish to delete them as well, use drop collection which is demonstrated in the next method.

```java
public long RemoveAllOrders(String connectionString){
	if(connectionString == null || connectionString.isEmpty())
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
			
			DeleteResult deletedOrders = collection.deleteMany(new Document());
			System.out.println("Number of Orders removed: "+deletedOrders.getDeletedCount());
			return deletedOrders.getDeletedCount();								
		}
		catch (Exception e) {
			System.out.println("Exception occured");
			System.out.println("Details:");
			System.out.println(e.getStackTrace());
			return -1;
		}	
	}			
}
```



### Range query by date

```java
collection.drop();
```
This method illustrates dropping a collection. The drop method called on a collection deletes the collection, its records, and its indexes. 

```java
public void DropOrdersCollection(String connectionString){
	if(connectionString == null || connectionString.isEmpty())
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
			collection.drop();							
		}
		catch (Exception e) {
			System.out.println("Exception occured");
			System.out.println("Details:");
			System.out.println(e.getStackTrace());
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

This test verifies connection to MongoDB Atlas Cluster
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

These tests verify RemoveAllCancelledOrders. Valid scenarios along with edge cases like invalid connection string and invalid threshold date are verified.  
```java
// Remove All method with multiple conditions
@Test(expected=IllegalArgumentException.class)
public void RemoveAllCancelledOrders_InvalidConnectionString_ThrowsException() throws ParseException {
	RemoveData removeData = new RemoveData();		
	Date thresholdDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse("2018-01-16T19:00:00.000");
	removeData.RemoveAllCancelledOrders("",thresholdDate);    
}

@Test(expected=IllegalArgumentException.class)
public void RemoveAllCancelledOrders_InvalidThresholdDate_ThrowsException() throws ParseException {
	RemoveData removeData = new RemoveData();		
	removeData.RemoveAllCancelledOrders(CONNECTION_STRING,null);
}
	
@Test
public void RemoveAllCancelledOrders_ValidArguments_Success() throws ParseException {
	// Add 5 dummy documents to test the deletion method
	Date thresholdDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse("1900-01-16T19:00:00.000");
	List<Document> documents = new ArrayList<Document>();
	for(int i=1;i<=5;i++)
	{
		documents.add(new Document("orderPlaced", DateUtils.addDays(thresholdDate, -1*i))
		 .append("status", Arrays.asList("cancelled", DateUtils.addDays(thresholdDate, -2*i))));
	}
	
	MongoClientURI clientUri = new MongoClientURI(CONNECTION_STRING);
	try(MongoClient client = new MongoClient(clientUri))
	{
		MongoDatabase database = client.getDatabase("stores");			
		MongoCollection<Document> collection = database.getCollection("orders");
		collection.insertMany(documents);	
	}	 
	
	RemoveData removeData = new RemoveData();		
	long removedDocumentsCount = removeData.RemoveAllCancelledOrders(CONNECTION_STRING,thresholdDate);
	
	//verify if 5 documents were deleted
	assertEquals(5, removedDocumentsCount);	    
}
```
These tests verify RemoveOneCancelledOrder. Valid scenarios along with edge cases like invalid connection string and invalid threshold date are verified. 
```java
// Remove one method with multiple conditions	
@Test(expected=IllegalArgumentException.class)
public void RemoveOneCancelledOrder_InvalidConnectionString_ThrowsException() throws ParseException {
	RemoveData removeData = new RemoveData();		
	Date thresholdDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse("2018-01-16T19:00:00.000");
	removeData.RemoveOneCancelledOrder("",thresholdDate);
}

@Test(expected=IllegalArgumentException.class)
public void RemoveOneCancelledOrder_InvalidThresholdDate_ThrowsException() throws ParseException {
	RemoveData removeData = new RemoveData();
	removeData.RemoveOneCancelledOrder(CONNECTION_STRING,null);	    
}

@Test
public void RemoveOneCancelledOrder_ValidArguments_Success() throws ParseException {	
	Date thresholdDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse("1905-01-16T19:00:00.000");
	//Add one dummy document to test deletion
	MongoClientURI clientUri = new MongoClientURI(CONNECTION_STRING);
	try(MongoClient client = new MongoClient(clientUri))
	{
		MongoDatabase database = client.getDatabase("stores");			
		MongoCollection<Document> collection = database.getCollection("orders");
		collection.insertOne(new Document("orderPlaced", DateUtils.addDays(thresholdDate, -1))
									.append("status", Arrays.asList("cancelled", DateUtils.addDays(thresholdDate, -2))));
	}	
	RemoveData removeData = new RemoveData();
	long removedDocumentsCount = removeData.RemoveOneCancelledOrder(CONNECTION_STRING,thresholdDate);
	
	//verify if one record was deleted
	assertEquals(1, removedDocumentsCount);
}

```
These tests verify RemoveAllOrders. Valid scenarios along with edge case like invalid connection string is verified. 
```java
//Remove All No condition
@Test(expected=IllegalArgumentException.class)
public void RemoveAllOrders_InvalidConnectionString_ThrowsException() throws ParseException {
	RemoveData removeData = new RemoveData();
	removeData.RemoveAllOrders("");    
}

@Test
public void RemoveAllOrders_ValidArguments_Success() throws ParseException {
	MongoClientURI clientUri = new MongoClientURI(CONNECTION_STRING);
	try(MongoClient client = new MongoClient(clientUri))
	{
		MongoDatabase database = client.getDatabase("stores");			
		MongoCollection<Document> orginalCollection = database.getCollection("orders");
		
		//create copy of documents as backup
		List<Document> allOrders = orginalCollection
				.find(new Document())
				.into(new ArrayList<Document>());
		
		//remove all the orders
		RemoveData removeData = new RemoveData();		
		long removedDocumentsCount = removeData.RemoveAllOrders(CONNECTION_STRING);
		
		//Assert deletion
		assertEquals(101, removedDocumentsCount);	 
		
		//Restore orders to original collection			
		orginalCollection.insertMany(allOrders);
		client.close();
	}	       
}
```
These tests verify DropOrdersCollection. Valid scenarios along with edge case like invalid connection string is verified. 
```java
//Drop Collection
@Test(expected=IllegalArgumentException.class)
public void DropOrdersCollection_InvalidConnectionString_ThrowsException() throws ParseException {
	RemoveData removeData = new RemoveData();
	removeData.DropOrdersCollection("");    
}

@Test
public void DropOrdersCollection_ValidArguments_Success() throws ParseException {
	MongoClientURI clientUri = new MongoClientURI(CONNECTION_STRING);
	try(MongoClient client = new MongoClient(clientUri))
	{
		MongoDatabase database = client.getDatabase("stores");			
		MongoCollection<Document> orginalCollection = database.getCollection("orders");
		
		//Create replica of original collection as backup
		orginalCollection.aggregate(Arrays.asList(out("ordersTemp"))).forEach(printBlock);
		
		//drop the collection
		RemoveData removeData = new RemoveData();		
		removeData.DropOrdersCollection(CONNECTION_STRING);
		
		//Assert deletion
		boolean collectionExists = client.getDatabase("stores").listCollectionNames()
				.into(new ArrayList<String>()).contains("orders");
		assertEquals(false, collectionExists);
		
		//rename replica to restore original collection			
		MongoCollection<Document> tempCollection = database.getCollection("ordersTemp");
		tempCollection.renameCollection(new MongoNamespace("stores","orders"));
		client.close();
	}
}
