# MongoDB Java Driver Update Data

## Introduction

This is a sample application that demonstrates MongoDB Java Driver's update functionality. It has four methods:

* one that illustrates updating a single field
* one that illustrates updating a single nested field
* one that illustrates updating multiple fields using operator ($gt)
* one that illustrates updating multiple nested fields

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

### Update One - UpdateOneOrder

```java
			Document newDocument = new Document();
			newDocument.append("$set", new Document().append("subtotal", subtotal));

			Document updateQuery = new Document().append("total", total);

			collection.updateOne(updateQuery, newDocument);
```

This method illustrates updating one order. It takes *total* and *subtotal* as parameters and updates the first document with total equal to parameter, using the $set operator to update the subtotal field with parameter passed.

```java
	public List<Document> UpdateOneOrder(String connectionString, Double total, Double subtotal)
	{

		if(connectionString == null || connectionString.isEmpty() || total <= 0 || subtotal <= 0)
		{
			throw new IllegalArgumentException();
		}
		
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
		
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");	
	
			Document newDocument = new Document();
			newDocument.append("$set", new Document().append("subtotal", subtotal));

			Document updateQuery = new Document().append("total", total);

			collection.updateOne(updateQuery, newDocument);
			
	        List<Document> queryResult = collection
	        		.find(and(eq("total",total),eq("subtotal",subtotal)))
	        		.into(new ArrayList<Document>());
			return queryResult;
		}
		
		catch (Exception e) {
			//log the exception
			
			System.out.println("An exception occured");
			System.out.println("Details:");
			System.out.println(e.getStackTrace());
			return null;
							
		}
	}
```

### Update One for a Nested field - UpdateOneOrderForEmbeddedField

```java
			Document newDocument = new Document();
			newDocument.append("$set", new Document().append("lineitems.$.name", name));

			Document updateQuery = new Document().append("lineitems.sku", stockKeepingUnit);

			collection.updateOne(updateQuery, newDocument);
```

This method illustrates updating one order for a nested field. It takes *lineitems - stockKeepingUnit* and *lineitems - name* as parameters and updates the first document with stockKeepingUnit equal to parameter, using the $set operator to update the name field with parameter passed.

```java
	public List<Document> UpdateOneOrderForEmbeddedField(String connectionString, String stockKeepingUnit, String name)
	{

		if(connectionString == null || connectionString.isEmpty() || stockKeepingUnit == null || stockKeepingUnit.isEmpty() || name == null || name.isEmpty())
		{
			throw new IllegalArgumentException();
		}
		
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
		
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");	
	
			Document newDocument = new Document();
			newDocument.append("$set", new Document().append("lineitems.$.name", name));

			Document updateQuery = new Document().append("lineitems.sku", stockKeepingUnit);

			collection.updateOne(updateQuery, newDocument);
			
	        List<Document> queryResult = collection
	        		.find(and(eq("lineitems.sku", stockKeepingUnit),eq("lineitems.name", name)))
	        		.into(new ArrayList<Document>());
			return queryResult;
		}
		
		catch (Exception e) {
			//log the exception
			
			System.out.println("An exception occured");
			System.out.println("Details:");
			System.out.println(e.getStackTrace());
			return null;
							
		}
	}

```

### Update Many using operator $gt - UpdateManyOrdersWithOperator

```java
			Document newDocument = new Document();
			newDocument.append("$set", new Document().append("tax", tax));
			
			Document updateQuery = new Document();
			updateQuery.append("total", new Document("$gt", total));

			collection.updateMany(updateQuery, newDocument);
```

This method illustrates updating many orders with an operator. It takes *total* and *tax* as parameters and updates all documents with total greater than the parameter, using the $set operator to update the tax field with parameter passed.

```java
	public List<Document> UpdateManyOrdersWithOperator(String connectionString, Double total, Double tax)
	{

		if(connectionString == null || connectionString.isEmpty() || total <= 0 || tax <= 0)
		{
			throw new IllegalArgumentException();
		}
		
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
		
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");	
	
			Document newDocument = new Document();
			newDocument.append("$set", new Document().append("tax", tax));
			
			Document updateQuery = new Document();
			updateQuery.append("total", new Document("$gt", total));

			collection.updateMany(updateQuery, newDocument);
			
	        List<Document> queryResult = collection
	        		.find(and(gt("total",total),eq("tax",tax)))
	        		.into(new ArrayList<Document>());
			return queryResult;
		}
		
		catch (Exception e) {
			//log the exception
			
			System.out.println("An exception occured");
			System.out.println("Details:");
			System.out.println(e.getStackTrace());
			return null;
							
		}
	}
```

### Update Many for Nested Fields - UpdateManyOrdersForEmbeddedField

```java
			Document newDocument = new Document();
			newDocument.append("$set", new Document().append("lineitems.$.unit_price", unitPrice));
			
			Document updateQuery = new Document().append("lineitems.sku", stockKeepingUnit);

			collection.updateMany(updateQuery, newDocument);
```

This method illustrates updating many orders with nested fields. It takes *lineitems - stockKeepingUnit * and *lineitems - unitPrice* as parameters and updates all documents with stockKeepingUnit equal to the parameter, using the $set operator to update the unitPrice field with parameter passed.

```java
	public List<Document> UpdateManyOrdersForEmbeddedField(String connectionString, String stockKeepingUnit, Double unitPrice)
	{

		if(connectionString == null || connectionString.isEmpty() || stockKeepingUnit == null || stockKeepingUnit.isEmpty() || unitPrice <= 0)
		{
			throw new IllegalArgumentException();
		}
		
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
		
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");	
	
			Document newDocument = new Document();
			newDocument.append("$set", new Document().append("lineitems.$.unit_price", unitPrice));
			
			Document updateQuery = new Document().append("lineitems.sku", stockKeepingUnit);

			collection.updateMany(updateQuery, newDocument);
			
	        List<Document> queryResult = collection
	        		.find(and(eq("lineitems.sku", stockKeepingUnit),eq("lineitems.unit_price", unitPrice)))
	        		.into(new ArrayList<Document>());
			return queryResult;
		}
		
		catch (Exception e) {
			//log the exception
			
			System.out.println("An exception occured");
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
    ├── testsrc                    		             # Source folder for all unit tests
    │   ├── UpdateDataTest.java              # Source file for all unit tests
   
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

These unit tests verify the UpdateOneOrder method. Valid scenarios along with edge cases like invalid connection string, invalid total and invalid subtotal are verified. 

```java
	@Test
	public void UpdateOneOrderTest() {
		
		MongoClientURI clientUri = new MongoClientURI(CONNECTION_STRING);
		MongoClient client = new MongoClient(clientUri);
		
		MongoDatabase database = client.getDatabase("stores");
		MongoCollection<Document> collection = database.getCollection("orders");
		
        List<Document> queryResult1 = collection
        		.find(and(eq("total", 406.0701),eq("subtotal", 70.87)))
        		.into(new ArrayList<Document>());
                
        //verify original record
	    Document result1 = queryResult1.get(0);
	    String id1 = result1.getString("_id");
	    Double subtotal1 = result1.getDouble("subtotal");
	    assertEquals("5a989e4ae0d1351062640fdc", id1);
	    assertEquals(70.87, subtotal1, 0.00);
	    
	    //update data
		UpdateData updateData = new UpdateData();		
		List<Document> queryResult2 = updateData.UpdateOneOrder(CONNECTION_STRING, 406.0701, 100.23);
		assertEquals(1, queryResult2.size());	
		
		//verify updated record
	    Document result2 = queryResult2.get(0);
	    String id2 = result2.getString("_id");
	    Double subtotal2 = result2.getDouble("subtotal");
	    assertEquals("5a989e4ae0d1351062640fdc", id2);
	    assertEquals(100.23, subtotal2, 0.00);
	    
	    //revert to original value
	    List<Document> queryResult3 = updateData.UpdateOneOrder(CONNECTION_STRING, 406.0701, 70.87);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateOneOrderTest_InvalidConnectionString_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateOneOrder("", 406.0701, 100.23);	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateOneOrderTest_InvalidTotal_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateOneOrder(CONNECTION_STRING, -406.0701, 100.23);	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateOneOrderTest_InvalidSubtotal_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateOneOrder(CONNECTION_STRING, 406.0701, -100.23);	    
	}
	
	@Test
	public void UpdateOneOrderTest_ValidArguments_SuccessWithNoRecords_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateOneOrder(CONNECTION_STRING, 555.555, 100.00);
		
		 //verify size
	    assertEquals(0, queryResult.size());  
	}
```

These unit tests verify the UpdateOneOrderForEmbeddedField method. Valid scenarios along with edge cases like invalid connection string, invalid/empty stockKeepingUnit and invalid/empty name are verified.

```java
	@Test
	public void UpdateOneOrderForEmbeddedFieldTest() {
		
		MongoClientURI clientUri = new MongoClientURI(CONNECTION_STRING);
		MongoClient client = new MongoClient(clientUri);
		
		MongoDatabase database = client.getDatabase("stores");
		MongoCollection<Document> collection = database.getCollection("orders");
		
        List<Document> queryResult1 = collection
        		.find(and(eq("lineitems.sku", "MDBTS134"),eq("lineitems.name", "1.6 oz. Vitamin C Serum")))
        		.into(new ArrayList<Document>());
        
        //verify original record
        Document document1 = (Document) collection
        		.find(and(eq("lineitems.sku", "MDBTS134"),eq("lineitems.name", "1.6 oz. Vitamin C Serum")))
        		.first()
        	    .get("lineitems", ArrayList.class)
        	    .get(0);
	    
        Document result1 = queryResult1.get(0);
	    String id1 = result1.getString("_id");
	    String name1 = document1.getString("name");
	    assertEquals("5a989e4af8a55a0ce18a4134", id1);
	    assertEquals("1.6 oz. Vitamin C Serum", name1);
		
	    //update data
		UpdateData updateData = new UpdateData();		
		List<Document> queryResult2 = updateData.UpdateOneOrderForEmbeddedField(CONNECTION_STRING, "MDBTS134", "Medium Duty Blender");
		assertEquals(1, queryResult2.size());	
		
		//verify updated record		
        Document document2 = (Document) collection
        		.find(and(eq("lineitems.sku", "MDBTS134"),eq("lineitems.name", "Medium Duty Blender")))
        		.first()
        	    .get("lineitems", ArrayList.class)
        	    .get(0);
        
	    Document result2 = queryResult2.get(0);
	    String id2 = result2.getString("_id");
	    String name2 = document2.getString("name");
	    assertEquals("5a989e4af8a55a0ce18a4134", id2);
	    assertEquals("Medium Duty Blender", name2);
	    
	    //revert to original value
	    List<Document> queryResult3 = updateData.UpdateOneOrderForEmbeddedField(CONNECTION_STRING, "MDBTS134", "1.6 oz. Vitamin C Serum");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateOneOrderForEmbeddedFieldTest_InvalidConnectionString_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateOneOrderForEmbeddedField("", "MDBTS134", "Medium Duty Blender");	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateOneOrderForEmbeddedFieldTest_EmptySku_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateOneOrderForEmbeddedField(CONNECTION_STRING, "", "Medium Duty Blender");	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateOneOrderForEmbeddedFieldTest_EmptyName_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateOneOrderForEmbeddedField(CONNECTION_STRING, "MDBTS134", "");	    
	}
	
	@Test
	public void UpdateOneOrderForEmbeddedFieldTest_ValidArguments_SuccessWithNoRecords_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateOneOrderForEmbeddedField(CONNECTION_STRING, "GBPX8634", "Medium Duty Blender");
		
		 //verify size
	    assertEquals(0, queryResult.size());  
	}
```

These unit tests verify the UpdateManyOrdersWithOperator method. Valid scenarios along with edge cases like invalid connection string, invalid total and invalid tax are verified.

```java
	@Test
	public void UpdateManyOrdersWithOperatorTest() {
		
		MongoClientURI clientUri = new MongoClientURI(CONNECTION_STRING);
		MongoClient client = new MongoClient(clientUri);
		
		MongoDatabase database = client.getDatabase("stores");
		MongoCollection<Document> collection = database.getCollection("orders");
	
		
        List<Document> queryResult1 = collection
        		.find(and(gt("total",2300.00)))
        		.into(new ArrayList<Document>());
        
        //verify original record
        assertEquals(4, queryResult1.size());
        	
		Document result1 = queryResult1.get(0);
		String id1 = result1.getString("_id");
		Double tax1 = result1.getDouble("tax");
		assertEquals("5a989e4a1509ec12d817f03e", id1);
		assertEquals(121.30, tax1, 0.00);
		
		//update data
		UpdateData updateData = new UpdateData();		
		List<Document> queryResult2 = updateData.UpdateManyOrdersWithOperator(CONNECTION_STRING, 2300.00, 230.00);
		assertEquals(4, queryResult2.size());	
		
		//verify updated record
			
		Document result2 = queryResult2.get(0);
		String id2 = result2.getString("_id");
		Double tax2 = result2.getDouble("tax");
		assertEquals("5a989e4a1509ec12d817f03e", id2);
		assertEquals(230.00, tax2, 0.00);

		
	    //revert to original value
		
		Document newDocument1 = new Document();
		newDocument1.append("$set", new Document().append("tax", 121.30));
		
		Document newDocument2 = new Document();
		newDocument2.append("$set", new Document().append("tax", 145.23));
		
		Document newDocument3 = new Document();
		newDocument3.append("$set", new Document().append("tax", 112.96));
		
		Document newDocument4 = new Document();
		newDocument4.append("$set", new Document().append("tax", 156.21));
		

		Document updateQuery1 = new Document().append("_id", "5a989e4a1509ec12d817f03e");
		Document updateQuery2 = new Document().append("_id", "5a989e4a60d71cc34a54260b");
		Document updateQuery3 = new Document().append("_id", "5a989e4a24a0ed39cf3e92a0");
		Document updateQuery4 = new Document().append("_id", "5a989e4a6ff8212799790854");
		
		collection.updateOne(updateQuery1, newDocument1);
		collection.updateOne(updateQuery2, newDocument2);
		collection.updateOne(updateQuery3, newDocument3);
		collection.updateOne(updateQuery4, newDocument4);

	}
```

These unit tests verify the UpdateManyOrdersForEmbeddedField method. Valid scenarios along with edge cases like invalid connection string, invalid/empty stockKeepingUnit and invalid unitPrice are verified.

```java
	@Test(expected=IllegalArgumentException.class)
	public void UpdateManyOrdersWithOperatorTest_InvalidConnectionString_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateManyOrdersWithOperator("", 2300.00, 230.00);	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateManyOrdersWithOperatorTest_InvalidTotal_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateManyOrdersWithOperator(CONNECTION_STRING, -2300.00, 230.00);	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateManyOrdersWithOperatorTest_InvalidTax_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateManyOrdersWithOperator(CONNECTION_STRING, 2300.00, -230.00);	    
	}
	
	@Test
	public void UpdateManyOrdersWithOperatorTest_ValidArguments_SuccessWithNoRecords_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateManyOrdersWithOperator(CONNECTION_STRING, 5000.00, 500.00);
		
		 //verify size
	    assertEquals(0, queryResult.size());  
	}
	
	@Test
	public void UpdateManyOrdersForEmbeddedFieldTest() {
		
		MongoClientURI clientUri = new MongoClientURI(CONNECTION_STRING);
		MongoClient client = new MongoClient(clientUri);
		
		MongoDatabase database = client.getDatabase("stores");
		MongoCollection<Document> collection = database.getCollection("orders");
		
        List<Document> queryResult1 = collection
        		.find(and(eq("lineitems.sku", "MDBTS244")))
        		.into(new ArrayList<Document>());
        
        //verify original record
        assertEquals(2, queryResult1.size());

        Document document1 = (Document) collection
        		.find(and(eq("lineitems.sku", "MDBTS244"),eq("lineitems.unit_price", 45.23)))
        		.first()
        	    .get("lineitems", ArrayList.class)
        	    .get(1);

		Document result1 = queryResult1.get(0);
		String id1 = result1.getString("_id");
		Double unitPrice1 = document1.getDouble("unit_price");
		assertEquals("5a989e4a317af0f885a7e8f6", id1);
		assertEquals(45.23, unitPrice1, 0.00);
		
        //update data
		UpdateData updateData = new UpdateData();		
		List<Document> queryResult2 = updateData.UpdateManyOrdersForEmbeddedField(CONNECTION_STRING, "MDBTS244", 70.23);
		assertEquals(2, queryResult2.size());	
		
		//verify updated record
			
        Document document2 = (Document) collection
        		.find(and(eq("lineitems.sku", "MDBTS244"),eq("lineitems.unit_price", 70.23)))
        		.first()
        	    .get("lineitems", ArrayList.class)
        	    .get(1);

		Document result2 = queryResult2.get(0);
		String id2 = result2.getString("_id");
		Double unitPrice2 = document2.getDouble("unit_price");
		assertEquals("5a989e4a317af0f885a7e8f6", id2);
		assertEquals(70.23, unitPrice2, 0.00);	
	    
		//revert to original value
		
		Document newDocument1 = new Document();
		newDocument1.append("$set", new Document().append("lineitems.$.unit_price", 45.23));
		
		Document newDocument2 = new Document();
		newDocument2.append("$set", new Document().append("lineitems.$.unit_price", 56.12));
		

		Document updateQuery1 = new Document().append("lineitems.sku", "MDBTS244").append("_id", "5a989e4a317af0f885a7e8f6");
		Document updateQuery2 = new Document().append("lineitems.sku", "MDBTS244").append("_id", "5a989e4a6629619e0aecc566");
		
		collection.updateOne(updateQuery1, newDocument1);
		collection.updateOne(updateQuery2, newDocument2);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateManyOrdersForEmbeddedFieldTest_InvalidConnectionString_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateManyOrdersForEmbeddedField("", "MDBTS244", 70.23);	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateManyOrdersForEmbeddedFieldTest_EmptySku_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateManyOrdersForEmbeddedField(CONNECTION_STRING, "", 70.23);	    
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void UpdateManyOrdersForEmbeddedFieldTest_InvalidUnitPrice_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateManyOrdersForEmbeddedField(CONNECTION_STRING, "MDBTS244", -70.23);	    
	}
	
	@Test
	public void UpdateManyOrdersForEmbeddedFieldTest_ValidArguments_SuccessWithNoRecords_ThrowsException() 
	{
		UpdateData updateData = new UpdateData();	
		List<Document> queryResult =updateData.UpdateManyOrdersForEmbeddedField(CONNECTION_STRING, "GBPX8634", 70.23);
		
		 //verify size
	    assertEquals(0, queryResult.size());  
	}
}
```
