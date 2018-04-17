import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class InsertExampleDataset {
	
	public static void main(String[] args) throws IOException{
		if(args.length != 2){
			throw new IllegalArgumentException("Usage: InsertExampleDataset <Connection String> <json file path>");
		}
		
		String connectionString = args[0];
		String filePath = args[1];
		
		System.out.println("Reading file: "+ filePath);
		BufferedReader reader = null;
		List<Document> orders = new ArrayList<Document>();

		try {
		    File file = new File(filePath);
		    reader = new BufferedReader(new FileReader(file));

		    String jsonString;
		    while ((jsonString = reader.readLine()) != null) {
		    	Document doc = Document.parse(jsonString);
		    	orders.add(doc);
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    try {
		        reader.close();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		
		System.out.println("Connecting to MongoDB cluster at: "+ connectionString);
		MongoClientURI clientUri = new MongoClientURI(connectionString);
		try(MongoClient client = new MongoClient(clientUri)){
			MongoDatabase database = client.getDatabase("stores");	
			MongoCollection<Document> collection = database.getCollection("orders");
			System.out.println("Deleting existing records");
			collection.deleteMany(new Document());
			System.out.println("Adding new records");
			collection.insertMany(orders);
			System.out.println("Done!");
		}
		catch (Exception e) {
			System.out.println("Exception occured");
			System.out.println("Details:");
			System.out.println(e.getStackTrace());
		}	
	}
}
