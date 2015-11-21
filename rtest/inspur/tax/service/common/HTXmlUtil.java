package inspur.tax.service.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;

public class HTXmlUtil {
	public static List getListFromXml(String xml, String listTag)
			throws Exception {
		List list = new ArrayList();
		try {
			XMLTool tool = new XMLTool();
			Document document = tool.getDocument(xml);
			Element rootElement = tool.getRootElement(document);
			List mapList = rootElement.getChildren();
			System.out.println(mapList.size());
			for (int i = 0; i < mapList.size(); i++) {
				Map map = new HashMap();
				Element mapElement = (Element) mapList.get(i);
				String childName = mapElement.getName();
				String value = mapElement.getText();
				list.add(map);
			}
		} catch (RuntimeException e) {
			throw new Exception();
		}
		return list;
	}

	/**
	 * 处理掉label的下划线并转化大小写,如CJXM_DM->cjxmDm;AA_BB_CC->aaBbCc
	 */
	private static String renameLabel(String label) {
		String[] ca = label.toLowerCase().split("_");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ca.length; i++) {
			if (sb.length() == 0)
				sb.append(ca[i]);
			else {
				if ("".equals(ca[i]))
					continue;
				sb.append(String.valueOf(ca[i].charAt(0)).toUpperCase());
				if (ca[i].length() > 1)
					sb.append(ca[i].substring(1));
			}
		}
		return sb.toString();
	}
}
