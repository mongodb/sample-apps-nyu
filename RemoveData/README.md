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

This method illustrates delete many functionality based on a condition. It takes the **threshold date** as the parameter, deletes all cancelled orders older than the that and returns the count of deleted orders. 

### Delete One

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

This method illustrates delete one functionality based on a condition. It takes the **threshold date** as the parameter, deletes one cancelled order older than the that and returns the count of deleted orders. 

### Delete Many - No conditions (Remove all)

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

This method illustrates how to remove all records in a collection. It returns the count of deleted records. Even though all records all deleted, the collection and the indexes remain untouched. If you wish to delete them as well, use drop collection which is demonstrated in the next method.

### Range query by date

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

This method illustrates dropping a collection. The drop method called on a collection deletes the collection, its records, and its indexes. 

## Running the tests

The unit test cases are written using JUnit 4 framework. You can find them here: 

    .
    ├── ...
    ├── testsrc                    		       # Source folder for all unit tests
    │   ├── FindWithScalarFieldsTest.java              # Source file for all unit tests
