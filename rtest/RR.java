import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.jnyt.exception.FileException;
import com.jnyt.zapi.FileHelper;
import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;

import net.sf.json.JSONArray;

public class RR {
	public static void main00(String[] args) {
		String values[] = new String[3];
		String value = "";
		value = "aa','bb','cc";
		values = value.split("','");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < values.length; i++) {
			value = values[i];
			if (i != 0) {
				sb.append("','");
			}
			// aa','
			sb.append(value.replace("'", "''"));
		}
	}

	public static void main(String[] args) {
		List list = new ArrayList();
		list.add("AAA1");
		list.add("AAA2");
		list.add("AAA3");
		JSONArray json = JSONArray.fromObject(list);
		System.out.println(json.toString());
	}
	
}
