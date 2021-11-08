import java.io.Console;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

public class App {


  public static void loadJSONIntoMongoDB(MongoOperator mongoOperator, String SOURCE_PATH) throws IOException {
    // Convert each line in source file to Mongo recognizable Document.
    FileLoader.ContentHandler jsonConverter = new FileLoader.ContentHandler() {
      @Override
      public void handleLine(String line) {
        mongoOperator.insertJSONString(line);
      }     
    };

    // Read file of JSON lines
    // and load each line as json dc to mongodb.
    FileLoader.loadJSONLinesToMongo(SOURCE_PATH, jsonConverter);

    // Verify data is loaded into mongodb
    mongoOperator.validateCollection();
  }

  public static void queryDBWithUserInput(MongoOperator mongoOperator) {
    Console console = System.console();
    String selection = "0";

    while (!selection.equals("q")) {
      System.out.println("Please Input selection:");
      System.out.println("- 1 : Sort items by open date");
      System.out.println("- 2 : Search items by date range");
      System.out.println("- q : Quit");
  
      selection = console.readLine().trim();
  
      if (selection.equals("1")) {
        mongoOperator.sortByOpenDate();
      }
      else if (selection.equals("2")) {
        System.out.println("Please input key word: ");
        String keyword = console.readLine().trim();
  
        System.out.println("Please input start date(yyyy-mm-dd): ");
        String startDateStr = console.readLine().trim();
        Date startDate = DateUtils.getDateFromISOStr(startDateStr);
  
        System.out.println("Please input end date(yyyy-mm-dd): ");
        String endDateStr = console.readLine().trim();
        Date endDate = DateUtils.getDateFromISOStr(endDateStr);

        mongoOperator.getItemsByDateRange(keyword, startDate, endDate);
      }
    }
  
    System.out.println("See you again!");
  }

  public static void main(String[] args) throws Exception {

    // Fetch DB credentail from config file
    Config config = new Config();

    // Get path for the data source
    String SOURCE_PATH = config.getKey("SOURCE_FILE");

    // Get DB credentials
    int DB_PORT = Integer.parseInt(config.getKey("MONGODB_PORT"));
    String DB_HOST = config.getKey("MONGODB_HOST");
    String DB_TABLE = config.getKey("MONGODB_TABLE");
    String DB_COLLECTION = config.getKey("MONGODB_COLLECTION");

    // Initialize MongoDB connection
    MongoClient mongoClient = new MongoClient(DB_HOST, DB_PORT);
    MongoDatabase dbObj = mongoClient.getDatabase(DB_TABLE);
    MongoCollection<Document> col = dbObj.getCollection(DB_COLLECTION);

    // Client instance to operate mongodb.
    MongoOperator mongoOperator = new MongoOperator(col);

    // Clear up collection
    mongoOperator.clearUp();

    // Task 1
    // Load data into MongoDB
    loadJSONIntoMongoDB(mongoOperator, SOURCE_PATH);

    // Create field indexes for full text search
    // FIXME: MongoDB only allows one field to be text search
    String[] fields =  { "title" };
    mongoOperator.createTextIndex(Arrays.asList(fields));

    // Task 2
    // Interact with User to query data by input date
    queryDBWithUserInput(mongoOperator);

    // Close MongoDB connection
    mongoClient.close();
  }
}
