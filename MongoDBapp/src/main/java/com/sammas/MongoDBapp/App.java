package com.sammas.MongoDBapp;
import com.mongodb.MongoClient;  
import com.mongodb.client.MongoCollection;  
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters; 
import com.mongodb.client.model.Updates; 
import java.util.Iterator; 
import org.bson.Document;  



public class App 
{
    public static void main( String[] args )
    {
         
        	//---------- Connecting DataBase -------------------------//
        	//authentication for database connection we can make use of method db.authenticate(username, password);//
        	MongoClient mongoClient = new MongoClient( "localhost" , 27017 );  
        	
        	
        	//---------- Creating DataBase ---------------------------//  
        	MongoDatabase db = mongoClient.getDatabase("Resume");  
        	
        	
        	//---------- Creating Collection -------------------------//  
        	MongoCollection<Document> collection = db.getCollection("employee");
        	
        	
        	//---------- Creating Document ---------------------------//    
        	Document doc = new Document("name", "Sammas Colkesen");  
        	doc.append("id",68);  
        	
        	
        	//----------- Insert Data ------------------------------//  
        	collection.insertOne(doc);  
        	
        	
        	//----------- Update Data ------------------------------//
        	collection.updateOne(Filters.eq("id", 68), Updates.set("id", 150)); 
        
        	
        	//----------- Delete Data ------------------------------//
           	collection.deleteOne(Filters.eq("id", 1)); 
        
        	 
       
        	//----------- Retrive all documents ------------------------------//
           	// Getting the iterable object 
	        FindIterable<Document> iterDoc = collection.find(); 
	        int i = 1; 
	
	        // Getting the iterator 
	        Iterator it = iterDoc.iterator(); 
	      
	        while (it.hasNext()) {  
	           System.out.println(it.next());  
	           i++; }
        	
       
	      //----------- Get Collection's names ------------------------------//
	        for (String name : db.listCollectionNames()) { 
	            System.out.println(name); 
	         }
	        
	      //----------- Drop Collection ------------------------------//
	        
	        collection.drop(); 
	        System.out.println(db.listCollectionNames());
	       
	      //----------- Drop Database ------------------------------//
	        db.drop();
    
    
    }
}

/* Before running this code make sure that mongodb service must be running (In linux #service mongodb start)
 * After running this code 
 * In command line type these;
 * #mongo
 * #show dbs (Resume DB must be show)
 * #use Resume
 * #show collections (employee collection must be show)
 * #db.employee.find().pretty(); (you must see Our data as JSON )
 * 
 * */
