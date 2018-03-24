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

Retrieves documents filtered by shipping address postal code.

### Equality match on two or more fields

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

Retrieves documents filtered by order placed date and shipping address postal code.

### Range query on a numeric field

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

Retrieves documents where the order total is greater than the lower bound and, less than or equal to the upper bound.

### Range query by date

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

Retrieves documents where the order placed date is between the start date and the end date.

## Running the tests

The unit test cases are written using JUnit 4 framework. You can find them here: 

    .
    ├── ...
    ├── testsrc                    		       # Source folder for all unit tests
    │   ├── FindWithScalarFieldsTest.java              # Source file for all unit tests
