import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/*******************************************************************************
 * 
 * 
 * @author arix04
 * 
 */

public class SS {

	public static final InputStream InputStream = null;
	private static final String PROPERTY_FILE = "/config/tsbca.properties";
	private static SS loadProp = new SS();

	public static void main(String[] args) throws Exception {

		String key = readData("key");

		writeData("key1", "kkkkkkkkk");
	}

	public static String readData(String key) {
		Properties props = new Properties();

		try {
			InputStream in = loadProp.getClass().getResourceAsStream(
					PROPERTY_FILE);
			props.load(in);
			in.close();
			String value = props.getProperty(key);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void writeData(String key, String value) {
		Properties prop = new Properties();
		try {
			File file = new File(PROPERTY_FILE);
			if (!file.exists())
				file.createNewFile();
			InputStream fis = new FileInputStream(file);
			prop.load(fis);
			fis.close();
			OutputStream fos = new FileOutputStream(PROPERTY_FILE);
			prop.setProperty(key, value);
			prop.store(fos, "Update '" + key + "' value");
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();

		}
	}

}