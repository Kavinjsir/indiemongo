import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import static com.mongodb.client.model.Sorts.*;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

public class MongoOperator {
  private MongoCollection<Document> collection;

  public MongoOperator(MongoCollection<Document> collection) {
    this.collection = collection;
  }

  // Verify if db connection to the collection is successful
  public void validateCollection() {
    System.out.println("Estimated number of documents: " + this.collection.estimatedDocumentCount());
    System.out.println("Count number of documents: " + this.collection.countDocuments());
  }

  // Get document from json string
  private Document formatDocument(String json) {
    // Get nested "data" json obj from raw string
    BsonDocument data = BsonDocument.parse(json).getDocument("data");
    Document dataDoc = Document.parse(data.toJson());

    // Add Date fields based on origin date string fields
    Date closeDate = DateUtils.getDateFromISOStr(dataDoc.getString("close_date"));
    dataDoc.put("local_close_date", closeDate);

    Date openDate = DateUtils.getDateFromISOStr(dataDoc.getString("open_date"));
    dataDoc.put("local_open_date", openDate);

    return dataDoc;
  }

  // Load json string to mongo collection
  public void insertJSONString(String json) {
    Document data =  formatDocument(json);
    this.collection.insertOne(data);
  }

  public void sortByOpenDate() {
    List<Document> results = new ArrayList<Document>();
    this.collection.find().sort(descending("local_open_date")).into(results);
    displayResults(results);
  }

  public void getItemsByDateRange(Date startDate, Date endDate) {
    List<Document> results = new ArrayList<Document>();
    Bson filter = Filters.and(
      Filters.gte("local_close_date", startDate),
      Filters.lte("local_open_date", endDate)
    );
    this.collection.find(filter).into(results);

    displayResults(results);
  }

  public void clearUp() {
    this.collection.drop();
  }

  private void displayResults(List<Document> results) {
    String title, openDate, closeDate;
    for (Document dc: results) {
      title = dc.getString("title");
      openDate = DateUtils.retreiveDate(dc.getString("open_date"));
      closeDate = DateUtils.retreiveDate(dc.getString("close_date"));

      System.out.println("======");
      System.out.println("Title:" + title);
      System.out.println("Open date:" + openDate);
      System.out.println("Close date:" + closeDate);
    }
    System.out.println("======");
  }
}
