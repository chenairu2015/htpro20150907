package frame.t;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

public class VNewTest {
	public void getEnumeration(HttpServletRequest request) {
		 Iterator iterator = request.getParameterMap().entrySet().iterator(); 
		   StringBuffer param = new StringBuffer();
		   int i1 = 0;
		   while (iterator.hasNext()) {  
		    i1++;
		       Entry entry = (Entry) iterator.next(); 
		       if(i1 == 1)
		            param.append("?").append(entry.getKey()).append("="); 
		       else
		         param.append("&").append(entry.getKey()).append("=");
		       if (entry.getValue() instanceof String[]) {  
		           param.append(((String[]) entry.getValue())[0]);  
		       } else {  
		           param.append(entry.getValue());  
		       }  
		   }
		   
		System.out.println(">>>>>>>>>>..."+ param);
		// 获取页面上的所有name值
		Enumeration pNames = request.getParameterNames();
//		Enumeration pNames = request.getAttributeNames();
		// 存放符合条件的name-value映射值
		Map initMap = new HashMap();
		String name = "";
		String value = "";
		String[] values;
		StringBuffer sb = new StringBuffer();
		while (pNames.hasMoreElements()) {
			// 获取当前要验证的name值
			name = (String) pNames.nextElement();// name
			value = request.getParameter(name);// name对应的值
			
			sb = new StringBuffer();
			
			values = value.split("','");
			
			for (int i = 0; i < values.length; i++) {
				value = values[i];
				if (i != 0) {
					sb.append("','");
				}
				sb.append(value.replace("'", "''"));
			}
			
//			System.out.println(" .... " + name.toUpperCase() + " ********* :"+sb.toString() );
		}
	}
}
