import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


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

	public static void main12(String[] args) {
		List list = new ArrayList();
		list.add("AAA1");
		list.add("AAA2");
		list.add("AAA3");
		JSONArray json = JSONArray.fromObject(list);
		System.out.println(json.toString());
	}

	public static void main22(String[] args) {
		String ss = new String("\u4e2d\u56fd");
		System.out.println(ss);
	}

	public static void main23(String[] args) {
		// int di = 2;
		// int me = 31;
		JSONObject jsonobj = new JSONObject();
		int di = 3;
		int me = 21;
		long re = 0;
		re = (long) Math.pow(di, me);
		System.out.println(re);
	}

	public static void main(String[] args) {
//		for (int j = 0; j < 100; j++) {
			System.out.println((int) ((Math.random() * 9 + 1) * 100000));
//		}
	}
}
