# MongoDB Java Driver Insert Functionality
This is a sample application that demonstrates MongoDB Java Driver's insert functionality. It has four methods:

- one that illustrates the use of insertOne() by passing order values
- one that illustrates the use of insertOne() by passing a JSON file
- one that illustrates the use of insertMany() by passing multiple order values
- one that illustrates the use of insertMany() by passing multiple JSON files


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

### AddOneOrderWithData - insertOne() functionality

```javascript
public static List<Document> AddOneOrderWithData(String connectionString, String sku, String item, Double  unit_price, int quantity)
	{
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		
		if(connectionString == null || connectionString.isEmpty() || sku.isEmpty() || item.isEmpty()|| unit_price==0.0 ||quantity ==0 )
		{
			throw new IllegalArgumentException();
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String today = formatter.format(date);
		Double total = unit_price*quantity;
		Double subtotal = total/4;
		
		Document newOrder = new Document("orderPlaced", today)
        .append("total", total)
        .append("subtotal",subtotal)
		.append("shipping", 50)
		.append("tax", 12.50);

     	List status = Arrays.asList("shipped","2018-03-10");
		newOrder.put("status", status);

		Document shipping = new Document ("number",100)
		.append("street", "Sunnyside")
		.append("city","Yonker")
		.append("state","New York")
		.append("country","USA")
		.append("postalCode",10001);
		newOrder.put("shippingAddress", shipping);	
		
		Document itemDetail = new Document("sku",sku)
		.append("name",item)
		.append("quantity",quantity)
		.append("unit_price",unit_price);
		List items = Arrays.asList(itemDetail);
		newOrder.put("lineitems", items);
		
		try(MongoClient client = new MongoClient(clientUri))
		{
			MongoDatabase database = client.getDatabase("stores");
			MongoCollection<Document> collection = database.getCollection("orders");		
	    	collection.insertOne(newOrder);
			List<Document> queryResult = collection.find().into(new ArrayList<Document>());
			return queryResult;
		}
		catch (Exception e) {
			//log the exception	
			System.out.println("An exception occured");
			System.out.println("Details:");
			e.printStackTrace();
			return null;
		}		
	}
``` 

This method illustrates the use of insertOne operator. 

The following example inserts a new document into the orders collection. If the document does not specify an _id field like above, the driver adds the _id field with an ObjectId value to the new document. 
If the document contains an _id field, the _id value must be unique within the collection to avoid duplicate key error.


### AddOneOrderWithFile 

```javascript
      FileReader file=new FileReader(fileName);
		  Object obj = new JSONParser().parse(file);
      JSONObject jo = (JSONObject) obj;
        .
        .
        .
      String jsonText = jo.toJSONString();
	    Document doc = Document.parse(jsonText);
		  collection.insertOne(doc);
```

The method illustrates the use of insertOne operator also. In this method, the JSON file is passed to this method and read in using the FileReader function. We are passing "simpledata.json" that contains the following 

```javascript
 {
    "_id":"9a999e1a1a11cc111fb1f99c",
    "orderPlaced": "2018-03-14",
    "total": 1483.102,
    "subtotal": 253.5469,
    "shipping": 381,
    "tax": 83.4925,
    "status": [
      "shipped",
      "2018-01-23"
    ],
    "shippingAddress": {
      "number": 133,
      "street": "Rockaway Parkway",
      "city": "Deercroft",
      "state": "Washington",
      "country": "USA",
      "postalCode": 57850
    },
    "lineitems": [
      {
        "sku": "MDBTS285",
        "name": "24-tooth Carbide Tipped Saw Blade",
        "quantity": 32,
        "unit_price": 9.3808
      },
      {
        "sku": "MDBTS468",
        "name": "24-tooth Carbide Tipped Saw Blade",
        "quantity": 17,
        "unit_price": 28.9123
      }
    ]
  }
```

This file is read and parsed into a JSON object. Then it is further converted into a Document type and the data is inserted into the stores.orders collection. As this document specifies an *_id* field like above, this *_id* number is given preference and the driver does not add a new *_id* field.  




### AddMultipleOrderWithData
```javascript
 collection.insertMany(Arrays.asList(newOrder[0],newOrder[1], newOrder[2]));
			
```
The following example inserts 3 new documents into the orders collection. 


### AddMultipleOrdersWithFiles
```javascript
 for(int i =0; i<orders.length; i++)
 {
	Object obj = new JSONParser().parse(new FileReader(orders[i]));
	JSONObject jo = (JSONObject) obj;
	String jsonText = jo.toJSONString();
	Document doc = Document.parse(jsonText);
	multipleOrders.add(doc);
}
collection.insertMany(multipleOrders);
```

The insertMany() function adds multiple documents into a collection. The above example uses db.collection.insertMany() to insert 3 documents. It reads 3 files named order1.json, order2.json and order3.json, parses them through a JSON Parser into a JSON object. Then the multiple order data is aggregated into the *multipleOrders* document and using insertMany function, it is inserted into the stores.orders collection.   



## Additional data files for running the program 
The JSON files used in this program resides in the folder of compiled codes. You can find them here:

    InsertData				# Source folder of the codes
    ├── ...
    ├── src                    	
    ├── testsrc                    	
    ├── order1.json			# Source file
    ├── order2.json
    ├── order3.json
    ├── simpledata.json
  


## Running the test
The unit test cases are written using JUnit 4 framework. You can find them here:

    .
    ├── ...
    ├── testsrc                    	# Source folder for all unit tests
    │   ├── InsertDataTest.java        # Source file for all unit tests
