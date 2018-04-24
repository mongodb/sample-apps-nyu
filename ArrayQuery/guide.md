
Guides Contribution Template

Please fill in the following fields to complete your guide.


Title
Query within Arrays

Author
Shinyou Hwang

Type
Getting Started

Level
Beginner

Product Version
v3.4 and up 

What You’ll Build
A Java application to illustrate different find queries to retrieve data from the mongoDB database. The database and the application that you will be working on mimics queries to an e-commerce database. The four query types you will be exploring are:
Use of the $in operator
Use of the $all operator
Equality match on an array field
Use of the $elemMatch operator

Time Required
7 - 10 minutes to install and run the sample app

What You’ll Need
[a list of steps (possibly with links to other guides or tutorials, or reference information) that serves as prerequisite to this task. For steps that are optional or that pertain to a particular technology, please indicate (i.e., OS or driver)]

Java 8
MongoDB Java Driver v3.6.
MongoDB instance. Either local or set up one for free on Mongo cloud Atlas.
Deploy a cluster - An Atlas Free Tier Cluster is sufficient for this exercise. 
Atlas Tutorial - Step by step guide for deploying a cluster
Create mongoDB collection “stores.order”
Download exampleDataset.json and Import data 



Check Your Environment
[provided by docs]

Considerations (optional)
Make sure the connection string to your MongoDB instance is correct. This can be found in the ArrayQueryTest.java file
String CONNECTION = "mongodb+srv://<clustername>:<password>#@sandbox-trhqa.mongodb.net/test";




	
Make sure the file path on your local machine to ExampleDataset.json is correct.
If you are giving a different name to your collection or database, make sure the change is reflected in the code. These are defined in the following code 
MongoClientURI clientUri = new MongoClientURI(connectionString);
	
	try(MongoClient client = new MongoClient(clientUri)){
		MongoDatabase database = client.getDatabase("stores");
		MongoCollection<Document> collection = database.getCollection("orders");		
			Additional information on connecting to MongoDB can be found here	




Procedure
[a list of steps required to complete the task, with code example references indicated. Please note that all code examples need to follow our include paradigm and reside in github.com]
ENTIRE MARKDOWN FILE
Download the files ArrayQuery.java and the test file ArrayQueryTest.java 
Build the project.
Use the following command to import the example dataset on to your MondoDB instance.




Summary
[summarize for the user what they have accomplished]

What’s Next
[a list of specific content or content topics that would be appropriate for the reader to look at next]

See Also
[a list of relevant reference documentation, university content, or manual content]

Having Trouble? Reach Out
[list any product or guide-specific support channels here. This section has standard content that will appear under any guide-specific suggestions]






Import
Insert
Find - types
Update
Remove
Aggregate
Index


