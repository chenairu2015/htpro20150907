package inspur.tax.service.common;

import java.util.List;

public class XTest {
	public static void main(String[] args) {
		String result = "<returnsms> <returnstatus>Success</returnstatus> <message>ok</message> <remainpoint>7</remainpoint> <taskID>440457</taskID> <successCounts>1</successCounts></returnsms>";
		try {
			List list = HTXmlUtil.getListFromXml(result, "returnsms");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
