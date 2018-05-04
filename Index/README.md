# MongoDB Java Driver Indexes
This is a sample application that demonstrates MongoDB Java Driver's index. 
It illustrates the creation, usage and deletion of indexes to efficiently execute the queries in the MongoDB database.


## Getting Started
The sample application queries data from MongoDB Atlas free-tier cluster. The cluster contains stores.orders collection which mimics an e-commerce database. The general schema is as follows:
 

```javascript
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

### GetShippingByCityWithoutIndex

This method illustrates a query without using of the indexes.

The following query selects documents and returns a list of orders from the stores.orders collection where the city matches the paramenter and the tax is greater than 50. 


```javascript
List<Document> noIndexQuery = collection.find(and(eq("shippingAddress.state",city),gt("tax",50)))
				.into(new ArrayList<Document>());
}
```

### GetShippingByCityWithIndex

THis method illustrates the creation of an idex and a query with the use of the index.
The following creates an index for 'state'
```javascript
collection.createIndex(Indexes.ascending("shippingAddress.state"));
```

After the creation of the index, the following query selects documents and returns a list of orders from the stores.orders collection where the city matches the paramenter and the tax is greater than 50. This returns the same list of orders but will use the index to query the database.


```javascript
List<Document> IndexQuery = collection.find(and(eq("shippingAddress.state",city),gt("tax",50)))
				.into(new ArrayList<Document>());
}
```

## GetShippingByCityIndexSize

The following method counts the number of indexes in the stores/orders collection and returns the total number of indexes. 
```javascript
try(MongoClient client = new MongoClient(clientUri))
{
	MongoDatabase database = client.getDatabase("stores");
	MongoCollection<Document> collection = database.getCollection("orders");		
		
	String queryResult = null;
	//check the current indexes
	for (Document index : collection.listIndexes()) {
	   line++;
	   queryResult = index.toJson();
	}
}
return line;
```

## DropIndex			
The following method drops the index for 'state' 

```javascript
try(MongoClient client = new MongoClient(clientUri))
{
	MongoDatabase database = client.getDatabase("stores");
	MongoCollection<Document> collection = database.getCollection("orders");		
		
	//drop the index
	collection.dropIndex(new BasicDBObject("shippingAddress.state", 1));
}
```			


## Running the test
The unit test cases are written using JUnit 4 framework. You can find them here:

    .
    ├── ...
    ├── testsrc                    	# Source folder for all unit tests
    │   ├── DataIndexTest.java        # Source file for all unit tests

