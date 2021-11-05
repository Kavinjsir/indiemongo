import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

// Get credential from config file
// Avoid hardcoding of passwords and config values
public class Config {
  private static final String CONFIG_PATH="app.config";
  private Properties prop;

  Config() {
    prop = new Properties();
    try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
      prop.load(fis);
    } catch (FileNotFoundException ex) {
      System.err.println("Missing config");
      System.exit(1);
    } catch (IOException ex) {
      System.err.println("Failed to load config");
      ex.printStackTrace();
      System.exit(1);
    }
  }

  public String getKey(String key) {
    return prop.getProperty(key);
  }
}
