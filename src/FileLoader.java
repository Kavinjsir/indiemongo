import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileLoader {
  public interface ContentHandler {
    void handleLine(String line);
  }

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
