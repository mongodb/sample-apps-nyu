import static com.mongodb.client.model.Filters.eq;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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
		
		if(InsertDataset(connectionString,filePath)){
			System.out.println("Done!");	
		}
		else{
			System.out.println("Failed to insert dataset. Please check for error details");
		}
	}
	
	/**
	 * Inserts documents to MongoDB.
	 * @param connectionString: To MongoDB instance/MongoDB Cluster.
	 * @param filePath: Path to Json file with records.
	 * @return List<Document>: ArrayList of matching documents.
	 * @throws FileNotFoundException 
	 */
	public static Boolean InsertDataset(String connectionString, String filePath) throws FileNotFoundException {
		if(connectionString.isEmpty() || connectionString == null || filePath.isEmpty() || filePath == null){
			throw new IllegalArgumentException();
		}
				
		System.out.println("Reading file: "+ filePath);
		BufferedReader reader = null;
		List<Document> orders = new ArrayList<Document>();
		
		File file = new File(filePath);
		if(!file.exists() || !file.isFile()){
			throw new FileNotFoundException();
		}
			
		try{
			reader = new BufferedReader(new FileReader(file));

		    String jsonString;
		    while ((jsonString = reader.readLine()) != null) {
		    	Document doc = Document.parse(jsonString);
		    	orders.add(doc);
		    }
		}
		catch(IOException e){
			System.out.println("Exception occured");
			System.out.println("Details:");
			e.printStackTrace();
			return false;
		} finally {
		    try {
		        reader.close();
		    } catch (IOException e) {
		    	System.out.println("Exception occured");
				System.out.println("Details:");
				e.printStackTrace();
				return false;
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
			return true;			
		}
		catch (Exception e) {
			System.out.println("Exception occured");
			System.out.println("Details:");
			e.printStackTrace();
			return false;
		}				
	}
}
