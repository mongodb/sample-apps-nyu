# MongoDB Java Driver Find Method within Arrays
This is a sample application that demonstrates MongoDB Java Driver's find within Array method. It has four methods:

- one that illustrates the use of the $in operator
- one that illustrates the use of the $all operator
- one that illustrates an equality match on an array field
- one that illustrates the use of the $elemMatch operator


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

### GetShippingByCity - $in Operator

```javascript
public static List<Document> GetShippingByCity(String connectionString, String[] cities)
	{

		if(connectionString == null || connectionString.isEmpty() || cities.length<=0)
		{
			throw new IllegalArgumentException();
		}
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri))
		{
		
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");		
				
			List<Document> queryResult = collection.find(in("shippingAddress.city",cities))
					.projection(fields(include("subtotal","shipping","shippingAddress.city")))
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

This method illustrates the use of $in operator. The $in operator selects the documents where the value of a field equals any value in the specified array. This takes the name of the cities as the parameter and [returns a list of orders](https://github.com/mongodb/sample-apps-nyu/blob/cbbd93a8c37e4aa500ee1c7f43cb3446fe6cd2c0/ArrayQuery/src/ArrayQuery.java#L72) from the *stores.orders* collection where the shipping address cities matches the parameter. 

*Example:* When cities named "Floris", "Calpine","Orason", "Allentown" are [passed](https://github.com/mongodb/sample-apps-nyu/blob/cbbd93a8c37e4aa500ee1c7f43cb3446fe6cd2c0/ArrayQuery/testsrc/ArrayQueryTest.java#L32), this method returns a list of 4 orders that match these shipping cities. 

	
### GetShippingByItem - $all Operator

```javascript
List<Document> queryResult = collection.find(all("lineitems.name",items))
					.projection(fields(include("subtotal","lineitems.name","shippingAddress.city")))
					.into(new ArrayList<Document>());
```

The [method](https://github.com/mongodb/sample-apps-nyu/blob/cbbd93a8c37e4aa500ee1c7f43cb3446fe6cd2c0/ArrayQuery/src/ArrayQuery.java#L95) illustrates the use of $all operator. The $all operator selects the documents where the value of a field is an array that contains all the specified elements. This method takes the names of the line items as the parameter and returns a list of orders from the *stores.orders* collection where the names of the lineitems matches the parameter. 
This method uses projection to only return items with fields such as subtotal, name of the item and city of the shipping address.

*Example:* When item named "omak 2 Door Mobile Cabinet" and "Glue Sticks" are [passed](https://github.com/mongodb/sample-apps-nyu/blob/cbbd93a8c37e4aa500ee1c7f43cb3446fe6cd2c0/ArrayQuery/testsrc/ArrayQueryTest.java#L64), this method returns a list of 1 order that match these item names. 
		
### FindByStatus - $eq Operator
```javascript
List<Document> queryResult = collection.find(eq("status",status))
	    			.into(new ArrayList<Document>());
```

The [method](https://github.com/mongodb/sample-apps-nyu/blob/cbbd93a8c37e4aa500ee1c7f43cb3446fe6cd2c0/ArrayQuery/src/ArrayQuery.java#L134) illustrates the use of $eq operator. The $eq operator matches documents where the value of a field equals the specified value. This method takes the status as the parameter and returns a list of orders from the *stores.orders* collection where the status of the order matches the parameter. 
The field *status* is an array that contains the date of status update as well as the following status: “shipped”, “ordered”, “cancelled”. 

*Example:* When the status "shipped" is [passed](https://github.com/mongodb/sample-apps-nyu/blob/cbbd93a8c37e4aa500ee1c7f43cb3446fe6cd2c0/ArrayQuery/testsrc/ArrayQueryTest.java#L111), this method returns a list of 38 orders that match this status.	


### FindByItemQuantity - $elemMatch Operator
```javascript
 List<Document> queryResult = collection.find(elemMatch("lineitems",and
                    (eq("name",item_name),gt("quantity",quantity))))
                    .into(new ArrayList<Document>());
```

The [method](https://github.com/mongodb/sample-apps-nyu/blob/cbbd93a8c37e4aa500ee1c7f43cb3446fe6cd2c0/ArrayQuery/src/ArrayQuery.java#L172) illustrates the use of $elemMatch operator. The $elemMatch operator matches documents that contain an array field with at least one element that matches all the specified query criteria. This method takes the item name and quantity as the parameter. It returns a list of orders from the *stores.orders* collection where the name of the item matches the parameter as well as the quantity is greater than the paramenter. 

*Example:* When the item named "Glue Sticks" and quanity "30" is [passed](https://github.com/mongodb/sample-apps-nyu/blob/cbbd93a8c37e4aa500ee1c7f43cb3446fe6cd2c0/ArrayQuery/testsrc/ArrayQueryTest.java#L155), this method returns a list of 3 orders that match the item name and the quantity is greater than 30. 

## Running the test
The unit test cases are written using JUnit 4 framework. You can find them here:

    .
    ├── ...
    ├── testsrc                    	# Source folder for all unit tests
    │   ├── ArrayQueryTest.java        # Source file for all unit tests
