import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileLoader {
  // An interface to support a  callback function to handle file content
  public interface ContentHandler {
    void handleLine(String line);
  }

  // Read file by lines and expose each line for an external content handler
  public static void loadJSONLinesToMongo(String sourcePath, ContentHandler contentHandler) throws IOException {
    File rawData = null;
    BufferedReader reader = null;

    try {
      rawData = new File(sourcePath);
      reader = new BufferedReader(new FileReader(rawData));

      String st = null;
      while ( (st = reader.readLine()) != null) {
        contentHandler.handleLine(st); 
      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      reader.close();
    }
  }
}
