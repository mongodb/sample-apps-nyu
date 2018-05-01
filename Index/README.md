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

### GetShippingByCity


This method illustrates the use of indexes

The following example first lists out all the indexes of the e- commerce database. 

```javascript
for (Document index : collection.listIndexes()) {
    System.out.println(index.toJson());
}
```

The following query first runs the query without being indexed
```javascript		
Document noIndexQuery =  collection.find(and(eq("shippingAddress.state",city),gt("tax",50))).modifiers(new Document("$explain",true)).first();
```

The following creates an index for 'state'
```javascript
collection.createIndex(Indexes.ascending("shippingAddress.state"));
```

The following lists out all the indexes of the e-commerce database. 
```javascript
for (Document index : collection.listIndexes()) {
	   System.out.println(index.toJson());
}
```			
			
The following drops the index for 'state'		
```javascript
collection.dropIndex(new BasicDBObject("shippingAddress.state", 1));
```			
	
