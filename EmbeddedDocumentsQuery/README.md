# MongoDB Java Driver Find Method on Embedded Documents

## Introduction

This is a sample application that demonstrates MongoDB Java Driver's find functionality on Embedded Documents. It has four methods:

* one that illustrates an equality match on a single nested field 
* one that illustrates an equality match on two nested fields
* one that illustrates the use of an operator on a nested field ($gt)
* one that illustrates an equality match that uses a nested field and one or more fields that are not nested

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

### Equality match on a Single Nested field - FindOrdersByCity

```java
	public List<Document> FindOrdersByCity(String connectionString, String city)
	{
		if(connectionString == null || connectionString.isEmpty() || city==null|| city.isEmpty())
			{
				throw new IllegalArgumentException();
			}
		
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
			MongoDatabase database = client.getDatabase("stores");			
			MongoCollection<Document> collection = database.getCollection("orders");		
			
			List<Document> queryOrders = collection
					.find(eq("shippingAddress.city",city))
					.into(new ArrayList<Document>());
			
			return queryOrders;
		}
		catch (Exception e) 
		{
			//log the exception
	    	System.out.println("An exception occured");
	        System.out.println("Details:");
	        System.out.println(e.getStackTrace());
			return null;
		}		
	}
 ```

This method illustrates a simple equality match on a single nested field. It takes the *shipping address - city* as the parameter and returns a list of orders where shipping address - city matches the parameter.

### Equality match on Two Nested Fields - FindOrdersByStateAndCity

```java
	public List<Document> FindOrdersByStateAndCity(String connectionString, String state, String city)
	{
        if(connectionString == null || connectionString.isEmpty() || state.isEmpty() || state ==null || city.isEmpty() || city ==null)
        	{
        		throw new IllegalArgumentException();
        	}
        
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
			MongoDatabase database = client.getDatabase("stores");			
			MongoCollection<Document> collection = database.getCollection("orders");		
			
			List<Document> queryOrders = collection
					.find(and(eq("shippingAddress.state",state),eq("shippingAddress.city",city)))
					.into(new ArrayList<Document>());
			
			return queryOrders;
		}
		catch (Exception e) 
		{
			//log the exception
	    	System.out.println("An exception occured");
	        System.out.println("Details:");
	        System.out.println(e.getStackTrace());
			return null;
		}		
	}
 ```
 
This method illustrates an equality match on two nested fields. It takes *shipping address - state* and *shipping address - city* as parameters and returns a list of only those orders that match both the parameters.

### Use of an Operator on a Nested Field ($gt) - FindOrdersByUnitPrice

```java
	public List<Document> FindOrdersByUnitPrice(String connectionString, Double unitPrice)
	{
		if(connectionString == null || connectionString.isEmpty() || unitPrice < 0)
			{
				throw new IllegalArgumentException();
			}
		
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");		
			
			List<Document> queryOrders = collection
					.find((gt("lineitems.unit_price",unitPrice)))
					.into(new ArrayList<Document>());
			
			return queryOrders;
		}
		catch (Exception e) 
		{
			//log the exception
			System.out.println("Exception occured");
			System.out.println("Details:");
			System.out.println(e.getStackTrace());
			return null;
		}		
	}
  ```
  
This method illustrates the use of opeartor **$gt** on a nested field. It takes *lineitems - unit_price* as the parameter and returns a list of only those orders where the unit_price is greater than the parameter.

### Equality match that uses a Nested Field and one field that is not nested - FindOrdersByPostalCodeAndSku

```java
	public List<Document> FindOrdersByPostalCodeAndSku(String connectionString, int postalCode, String stockKeepingUnit)
	{
		if(connectionString == null || connectionString.isEmpty() || stockKeepingUnit.isEmpty() || stockKeepingUnit ==null|| postalCode < 0)
			{
				throw new IllegalArgumentException();
			}
		
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");		
			
			List<Document> queryOrders = collection
					.find(and(eq("shippingAddress.postalCode",postalCode),eq("lineitems.sku",stockKeepingUnit)))
					.into(new ArrayList<Document>());
			
			return queryOrders;
		}
		catch (Exception e) 
		{
			//log the exception
			System.out.println("Exception occured");
			System.out.println("Details:");
			System.out.println(e.getStackTrace());
			return null;
		}		
	}
 ```
  
This method illustrates an equality match on a nested field and a not nested field. It takes *shipping address - postalCode* and *lineitems - sku* as parameters and returns a list of only those orders that match both the parameters.

## Running the tests

The unit test cases are written using JUnit 4 framework. You can find them here: 

    .
    ├── ...
    ├── testsrc                    		             # Source folder for all unit tests
    │   ├── EmbeddedDocumentsQueryTest.java              # Source file for all unit tests
