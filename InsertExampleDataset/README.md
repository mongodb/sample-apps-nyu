# Import example dataset for Java-based sample apps

## Author
[Raghavendra Harish](https://github.com/Raghav2018)

## Type
Getting started.

## Level
Begineer.

## Product Version
v3.4 and up.

## What You'll Build
Java app to import example dataset on to MongoDB database.

## Time Required 
2 minutes.

## What You'll Need
1. Java 8.
2. [MongoDB Java Driver v3.6](https://mongodb.github.io/mongo-java-driver/).
3. MongoDB instance. Either local or set up one for free on Mongo cloud [Atlas](https://www.mongodb.com/cloud/atlas).
4. Download exampleDataset.json.

## Check Your Environment
1. Check for Java installation and Java driver dependency set up.

## Consideration
1. Get the correct connection string to your MongoDB instance.
2. Get the correct file path on your local machine to ExampleDataset.json.

## Procedure
1. Build the project.
2. Use the following command to import the example dataset on to your MondoDB instance.
```
java -jar InsertExampleDataset.jar <Connection String> exampleDataset.json
```
## Summary
This guide sets you up for rest of the sample applications in this guide.

## What's Next
* See how to [**Create/Insert**](https://github.com/mongodb/sample-apps-nyu/tree/master/InsertData) data.

## See Also
* [Read](https://github.com/mongodb/sample-apps-nyu/tree/master/FindWithScalarFields)
* [Update]()
* [Delete](https://github.com/mongodb/sample-apps-nyu/tree/master/RemoveData)

## Having Trouble
