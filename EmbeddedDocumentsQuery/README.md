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
List<Document> queryOrders = collection
		.find(eq("shippingAddress.city",city))
		.into(new ArrayList<Document>());
```

This method illustrates a simple equality match on a single nested field. It takes the *shipping address - city* as the parameter and returns a list of orders where shipping address - city matches the parameter.

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

### Equality match on Two Nested Fields - FindOrdersByStateAndCity

```java
List<Document> queryOrders = collection
		.find(and(eq("shippingAddress.state",state),eq("shippingAddress.city",city)))
		.into(new ArrayList<Document>());
 ```
 
This method illustrates an equality match on two nested fields. It takes *shipping address - state* and *shipping address - city* as parameters and returns a list of only those orders that match both the parameters.

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
 
### Use of an Operator on a Nested Field ($gt) - FindOrdersByUnitPrice

```java
List<Document> queryOrders = collection
		.find((gt("lineitems.unit_price",unitPrice)))
		.into(new ArrayList<Document>());
```

This method illustrates the use of opeartor **$gt** on a nested field. It takes *lineitems - unit_price* as the parameter and returns a list of only those orders where the unit_price is greater than the parameter.

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
 
### Equality match that uses a Nested Field and one field that is not nested - FindOrdersByPostalCodeAndSku

```java
List<Document> queryOrders = collection
		.find(and(eq("shippingAddress.postalCode",postalCode),eq("lineitems.sku",stockKeepingUnit)))
		.into(new ArrayList<Document>());
```
 
This method illustrates an equality match on a nested field and a not nested field. It takes *shipping address - postalCode* and *lineitems - sku* as parameters and returns a list of only those orders that match both the parameters
 
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

## Running the tests

The unit test cases are written using JUnit 4 framework. You can find them here: 

    .
    ├── ...
    ├── testsrc                    		             # Source folder for all unit tests
    │   ├── EmbeddedDocumentsQueryTest.java              # Source file for all unit tests
   
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

These unit tests verify the FindOrdersByCity method. Valid scenarios along with edge cases like invalid connection string and invalid/empty city are verified. 

```java
	@Test
	public void FindOrdersByCityTest() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByCity(CONNECTION_STRING,"Woodburn");
	    
	    //verify size
	    assertEquals(1, filteredDocuments.size());	
	    
	    //verify record
	    Document result = filteredDocuments.get(0);
	    String id = result.getString("_id");
	    assertEquals("5a989e4abf545f22ed33d590", id);	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void FindOrdersByCity_InvalidConnectionString_ThrowsException() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByCity("","Woodburn");	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void FindOrdersByCity_EmptyCity_ThrowsException() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByCity(CONNECTION_STRING,"");	    
	}
	
	@Test
	public void FindOrdersByCity_ValidArguments_SuccessWithNoRecords() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByCity(CONNECTION_STRING,"Ithica");
	    
	    //verify size
	    assertEquals(0, filteredDocuments.size());  
	}
```

These unit tests verify the FindOrdersByStateAndCity method. Valid scenarios along with edge cases like invalid connection string, invalid/empty state and invalid/empty city are verified. 

```java
	@Test
	public void FindOrdersByStateAndCityTest() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByStateAndCity(CONNECTION_STRING,"Texas","Convent");
	    
	    //verify size
	    assertEquals(1, filteredDocuments.size());	
	    
	    //verify record
	    Document result = filteredDocuments.get(0);
	    String id = result.getString("_id");
	    assertEquals("5a989e4a151dd8d28832eb2f", id);
	    
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void FindOrdersByStateAndCity_InvalidConnectionString_ThrowsException() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByStateAndCity("","Texas","Convent");	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void FindOrdersByStateAndCityTest_EmptyState_ThrowsException() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByStateAndCity(CONNECTION_STRING,"","Convent");	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void FindOrdersByStateAndCityTest_EmptyCity_ThrowsException() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByStateAndCity(CONNECTION_STRING,"Texas","");	    
	}
	
	@Test
	public void FindOrdersByStateAndCity_ValidArguments_SuccessWithNoRecords() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByStateAndCity(CONNECTION_STRING,"Alabama","Mobile");
	    
	    //verify size
	    assertEquals(0, filteredDocuments.size());  
	}
```

These unit tests verify the FindOrdersByUnitPrice method. Valid scenarios along with edge cases like invalid connection string and invalid unit_price are verified. 

```java
	@Test
	public void FindOrdersByUnitPriceTest() {
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByUnitPrice(CONNECTION_STRING,99.9);
	    
	    //verify size
	    assertEquals(1, filteredDocuments.size());	
	    
	    //verify record
	    Document result = filteredDocuments.get(0);
	    String id = result.getString("_id");
	    assertEquals("5a989e4afbff26cbbee32480", id);
	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void FindOrdersByUnitPrice_InvalidConnectionString_ThrowsException() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByUnitPrice("",99.9);	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void FindOrdersByUnitPrice_InvalidUnitPrice_ThrowsException() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByUnitPrice(CONNECTION_STRING,-99.9);	    
	}	
	
	@Test
	public void FindOrdersByUnitPrice_ValidArguments_SuccessWithNoRecords() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByUnitPrice(CONNECTION_STRING,105.0);
	    
	    //verify size
	    assertEquals(0, filteredDocuments.size());  
	}
```

These unit tests verify the FindOrdersByPostalCodeAndSku method. Valid scenarios along with edge cases like invalid connection string, invalid postal code and invalid/empty sku are verified. 

```java
	@Test
	public void FindOrdersByPostalCodeAndSkuTest() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByPostalCodeAndSku(CONNECTION_STRING,10190,"MDBTS192");
	    
	    //verify size
	    assertEquals(1, filteredDocuments.size());	
	    
	    //verify record
	    Document result = filteredDocuments.get(0);
	    String id = result.getString("_id");
	    assertEquals("5a989e4ac5fb0ab8c29ac345", id);
	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void FindOrdersByPostalCodeAndSku_InvalidConnectionString_ThrowsException() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByPostalCodeAndSku("",10190,"MDBTS192");	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void FindOrdersByPostalCodeAndSku_InvalidPostalCode_ThrowsException() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByPostalCodeAndSku(CONNECTION_STRING,-10190,"MDBTS192");	    
	}	
	
	@Test(expected=IllegalArgumentException.class)
	public void FindOrdersByPostalCodeAndSku_EmptySku_ThrowsException() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByPostalCodeAndSku(CONNECTION_STRING,10190,"");	    
	}	
	
	@Test
	public void FindOrdersByPostalCodeAndSku_ValidArguments_SuccessWithNoRecords() 
	{
		EmbeddedDocumentsQuery embeddedDocumentsQuery = new EmbeddedDocumentsQuery();		
	    List<Document> filteredDocuments = embeddedDocumentsQuery.FindOrdersByPostalCodeAndSku(CONNECTION_STRING,10190,"FDXGD403");
	    
	    //verify size
	    assertEquals(0, filteredDocuments.size());  
	}
	
}
```
