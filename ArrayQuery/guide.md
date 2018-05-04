
# Sample App for querying database within Arrays

## Title
Querying database within Arrays

## Author
Shinyou Hwang

## Type
Getting Started

## Level
Beginner

## Product Version
v3.4 and up 

## What You’ll Build
A Java application to illustrate different find queries to retrieve data from the mongoDB database. The database and the application that you will be working on mimics queries to an e-commerce database. The four query types you will be exploring are:
Use of the $in operator
Use of the $all operator
Equality match on an array field
Use of the $elemMatch operator

## Time Required
7 - 10 minutes to install and run the sample app

## What You’ll Need
1. [Java 8](http://www.oracle.com/technetwork/java/javase/overview/java8-2100321.html)
2. [MongoDB Java Driver v3.6](https://mongodb.github.io/mongo-java-driver/)
3. MongoDB instance. Either local or set up one for free on Mongo cloud [Atlas](https://www.mongodb.com/cloud/atlas).
	[Deploy a cluster](https://cloud.mongodb.com/user#/atlas/register/accountProfile) - An Atlas Free Tier Cluster is sufficient for this exercise. 
	[Atlas Tutorial](https://docs.mongodb.com/manual/tutorial/atlas-free-tier-setup/#create-free-tier-manual) - Step by step guide for deploying a cluster
4. Create mongoDB collection “stores.order”
5. Download [exampleDataset.json](https://github.com/mongodb/sample-apps-nyu/blob/RH-DOCS-11626/InsertExampleDataset/exampleDataset.json) and [Import data](https://github.com/mongodb/sample-apps-nyu/tree/RH-DOCS-11626/InsertExampleDataset) 


## Check Your Environment
[provided by docs]

## Considerations (optional)
- Make sure the connection string to your MongoDB instance is correct. This can be found in the ArrayQueryTest.java file
```java
String CONNECTION = "mongodb+srv://<clustername>:<password>#@sandbox-trhqa.mongodb.net/test";
```
- Make sure the file path on your local machine to ExampleDataset.json is correct.
- If you are giving a different name to your collection or database, make sure the change is reflected in the code. These are defined in the following code 
```java
MongoClientURI clientUri = new MongoClientURI(connectionString);
	
try(MongoClient client = new MongoClient(clientUri)){
	MongoDatabase database = client.getDatabase("stores");
	MongoCollection<Document> collection = database.getCollection("orders");		
```
Additional information on connecting to MongoDB can be found [here](http://mongodb.github.io/mongo-java-driver/3.6/driver-async/tutorials/connect-to-mongodb/)	



## Procedure
[a list of steps required to complete the task, with code example references indicated. Please note that all code examples need to follow our include paradigm and reside in github.com]

1. Download the files ArrayQuery.java and the test file ArrayQueryTest.java 
2. Build the project.
3. Refer [README documentation](https://github.com/mongodb/sample-apps-nyu/blob/master/ArrayQuery/README.md) for the code explanation


## Summary
This guide helps you understand how to retrieve data from array-ed database using various operators.

## What’s Next
* See how to [**Query embedded documents**](https://github.com/mongodb/sample-apps-nyu/tree/master/EmbeddedDocumentsQuery).


## See Also
* [Read](https://github.com/mongodb/sample-apps-nyu/tree/master/EmbeddedDocumentsQuery)
* [Update]()
* [Delete](https://github.com/mongodb/sample-apps-nyu/tree/master/RemoveData)

## Having Trouble? Reach Out
[list any product or guide-specific support channels here. This section has standard content that will appear under any guide-specific suggestions]






Import
Insert
Find - types
Update
Remove
Aggregate
Index


