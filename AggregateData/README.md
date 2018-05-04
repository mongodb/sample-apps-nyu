# MongoDB Java Driver Aggregate Data

## Introduction

This is a sample application that demonstrates MongoDB Java Driver's Aggregation Framework. It has three methods:

* one that illustrates count
* one that illustrates average
* one that illustrates projection

## Getting Started

The sample application demonstrates aggregation of data from a MongoDB Atlas free-tier cluster. The cluster contains stores.orders collection which mimics an e-commerce database. The general schema is as follows: 

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

To perform aggregation, a list of aggregation stages are passed to the MongoCollection.aggregate() method.  The Aggregates helper class  contains builders for aggregation stages.

### Count - AggregateByState

```java
					List<Document> queryResult = collection.aggregate(
						      		Arrays.asList(
						      				Aggregates.match(Filters.eq("shippingAddress.state", state)),
						      				Aggregates.group("$shippingAddress.city", Accumulators.sum("count", 1))
						      				)
							).into(new ArrayList<Document>());
					return queryResult;
```

This method illustrates Count. The example uses Aggregates.group to build the $group stage and Accumulators.sum to build the accumulator expression. 
* A $match stage to filter for documents whose *shippingAdress - state* contains the parameter passed. 
* A $group stage to group the matching documents by the *shippingAdress - city* field, accumulating a count of documents for each distinct value of *shippingAdress - city*. 

```java
		public List<Document> AggregateByState(String connectionString, String state){
			if(connectionString == null || connectionString.isEmpty() || state == null || state.isEmpty())
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
					
					List<Document> queryResult = collection.aggregate(
						      		Arrays.asList(
						      				Aggregates.match(Filters.eq("shippingAddress.state", state)),
						      				Aggregates.group("$shippingAddress.city", Accumulators.sum("count", 1))
						      				)
							).into(new ArrayList<Document>());
					return queryResult;
				}
				
				catch (Exception e) 
				{
					System.out.println("Exception occured");
					System.out.println("Details:");
					e.printStackTrace();
					return null;
				}	
			}			
		}
```
    
### Average - AggregateByStateAverageShipping

```java
					List<Document> queryResult = collection.aggregate(
						      Arrays.asList(
						              Aggregates.match(Filters.eq("shippingAddress.state", state)),
						              Aggregates.group("$shippingAddress.state", Accumulators.avg("Average Shipping", "$shipping"))
						      )
						).into(new ArrayList<Document>());
					return queryResult;
```

This method illustrates Average. The example uses Aggregates.group to build the $group stage and Accumulators.avg to build the accumulator expression. 
* A $match stage to filter for documents whose *shippingAdress - state* contains the parameter passed. 
* A $group stage to group the matching documents by the *shippingAdress - state* field, calculating the average of documents for the value of *shippingAdress - state*. 

```java
		public List<Document> AggregateByStateAverageShipping(String connectionString, String state){
			if(connectionString == null || connectionString.isEmpty() || state == null || state.isEmpty())
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
					
					List<Document> queryResult = collection.aggregate(
						      Arrays.asList(
						              Aggregates.match(Filters.eq("shippingAddress.state", state)),
						              Aggregates.group("$shippingAddress.state", Accumulators.avg("Average Shipping", "$shipping"))
						      )
						).into(new ArrayList<Document>());
					return queryResult;
					
				}
				
				catch (Exception e) 
				{
					System.out.println("Exception occured");
					System.out.println("Details:");
					e.printStackTrace();
					return null;
				}	
			}			
		}
```

### Using Projections - AggregateByCityCheckStatus

```java
					List<Document> queryResult = collection.aggregate(
							Arrays.asList(
									Aggregates.project(
											Projections.fields(
													Projections.excludeId(),
													Projections.include("shippingAddress.city"),
													Projections.computed(
															"firstCategory",
															new Document("$arrayElemAt", Arrays.asList("$status", 0))
			                    )
			              )
			          )
					)
					).into(new ArrayList<Document>());
					return queryResult;
```

This method illustrates Project. The example uses Aggregates.project and various Projections methods to build the $project stage.
The aggregation pipeline uses a $project stage to return only the *shippingAddress - city* field and the calculated field firstCategory whose value is the first element in the *status* array. 

```java
		public List<Document> AggregateByCityCheckStatus(String connectionString){
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
		
					List<Document> queryResult = collection.aggregate(
							Arrays.asList(
									Aggregates.project(
											Projections.fields(
													Projections.excludeId(),
													Projections.include("shippingAddress.city"),
													Projections.computed(
															"firstCategory",
															new Document("$arrayElemAt", Arrays.asList("$status", 0))
			                    )
			              )
			          )
					)
					).into(new ArrayList<Document>());
					return queryResult;
				}
					
					catch (Exception e) 
					{
						System.out.println("Exception occured");
						System.out.println("Details:");
						e.printStackTrace();
						return null;
					}

			}
		}
```

## Running the tests

The unit test cases are written using JUnit 4 framework. You can find them here: 

    .
    ├── ...
    ├── testsrc                    		             # Source folder for all unit tests
    │   ├── AggregateDataTest.java                 # Source file for all unit tests
   
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

These unit tests verify the AggregateByState method. Valid scenarios along with edge cases like invalid connection string and invalid/empty state are verified. 

```java
		@Test
		public void AggregateByState_ValidArguments_SuccessWithRecords() {
			
			MongoClientURI clientUri = new MongoClientURI(CONNECTION_STRING);
			MongoClient client = new MongoClient(clientUri);
			
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");
			
			AggregateData aggregateData = new AggregateData();
			List<Document> queryResult = aggregateData.AggregateByState(CONNECTION_STRING, "Texas");
			assertEquals(2, queryResult.size());	
	                
	        //verify first record with count
		    Document result1 = queryResult.get(0);
		    String id1 = result1.getString("_id");
		    Integer count1 = result1.getInteger("count");
		    assertEquals("Convent", id1);
		    assertEquals(1, count1, 0.00);
		    
	        //verify second record with count
		    Document result2 = queryResult.get(1);
		    String id2 = result2.getString("_id");
		    Integer count2 = result2.getInteger("count");
		    assertEquals("Cade", id2);
		    assertEquals(1, count2, 0.00);
		}
		 
		@Test(expected=IllegalArgumentException.class)
		public void AggregateByState_InvalidConnectionString_ThrowsException() {
			
				AggregateData aggregateData = new AggregateData();	
				List<Document> queryResult = aggregateData.AggregateByState("", "Texas");	    
			}
		
		@Test(expected=IllegalArgumentException.class)
		public void AggregateByState_InvalidState_ThrowsException() {
			
			AggregateData aggregateData = new AggregateData();	
			List<Document> queryResult = aggregateData.AggregateByState(CONNECTION_STRING, "");    
		}
		
		@Test
		public void AggregateByState_ValidArguments_SuccessWithNoRecords_ThrowsException() {
			
			AggregateData aggregateData = new AggregateData();	
			List<Document> queryResult = aggregateData.AggregateByState(CONNECTION_STRING, "Quebec");
			
			
			//verify size
		    assertEquals(0, queryResult.size());  
		}
```

These unit tests verify the AggregateByStateAverageShipping method. Valid scenarios along with edge cases like invalid connection string and invalid/empty state are verified.

```java
		@Test
		public void AggregateByStateAverageShipping_ValidArguments_SuccessWithRecords() {
			
			MongoClientURI clientUri = new MongoClientURI(CONNECTION_STRING);
			MongoClient client = new MongoClient(clientUri);
			
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");
			
			//Calculate average
	        List<Document> queryResult1 = collection
	        		.find((eq("shippingAddress.state", "Texas")))
	        		.into(new ArrayList<Document>());
	        
		    Document result1a = queryResult1.get(0);
		    Double shipping1a = result1a.getDouble("shipping");
		    
		    Document result1b = queryResult1.get(1);
		    Double shipping1b = result1b.getDouble("shipping");
		    
		    Double average1 = (shipping1a + shipping1b)/2;
			
			AggregateData aggregateData = new AggregateData();
			List<Document> queryResult2 = aggregateData.AggregateByStateAverageShipping(CONNECTION_STRING, "Texas");
			assertEquals(1, queryResult2.size());	
	                
	        //verify record and average
		    Document result2 = queryResult2.get(0);
		    String state2 = result2.getString("_id");
		    Double average2 = result2.getDouble("Average Shipping");
		    assertEquals("Texas", state2);
		    assertEquals(average1, average2, 0.00);
		    
		}
		 
		@Test(expected=IllegalArgumentException.class)
		public void AggregateByStateAverageShipping_InvalidConnectionString_ThrowsException() {
			
				AggregateData aggregateData = new AggregateData();	
				List<Document> queryResult = aggregateData.AggregateByState("", "Texas");	    
			}
		
		@Test(expected=IllegalArgumentException.class)
		public void AggregateByStateAverageShipping_InvalidState_ThrowsException() {
			
			AggregateData aggregateData = new AggregateData();	
			List<Document> queryResult = aggregateData.AggregateByState(CONNECTION_STRING, "");    
		}
		
		@Test
		public void AggregateByStateAverageShipping_ValidArguments_SuccessWithNoRecords_ThrowsException() {
			
			AggregateData aggregateData = new AggregateData();	
			List<Document> queryResult = aggregateData.AggregateByState(CONNECTION_STRING, "Quebec"); 
			
			
			//verify size
		    assertEquals(0, queryResult.size());  
		}
```

These unit tests verify the AggregateByCityCheckStatus method. Valid scenarios along with edge case invalid connection string are verified. 

```java
		@Test
		public void AggregateByCityCheckStatus_ValidArguments_SuccessWithRecords() {
			
			MongoClientURI clientUri = new MongoClientURI(CONNECTION_STRING);
			MongoClient client = new MongoClient(clientUri);
			
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");
			
			AggregateData aggregateData = new AggregateData();
			List<Document> queryResult = aggregateData.AggregateByCityCheckStatus(CONNECTION_STRING);
			assertEquals(100, queryResult.size());	
	                
	        //verify first record with count
		    Document result = queryResult.get(0);
		    //String city = result.getString("shippingAddress$city");
		    String firstCategory = result.getString("firstCategory");
		    //assertEquals("Deercroft", city);
		    assertEquals("shipped", firstCategory);
		    
		}
		 
		@Test(expected=IllegalArgumentException.class)
		public void AggregateByCityCheckStatus_InvalidConnectionString_ThrowsException() {
			
				AggregateData aggregateData = new AggregateData();	
				List<Document> queryResult = aggregateData.AggregateByCityCheckStatus("");	    
			}
```
