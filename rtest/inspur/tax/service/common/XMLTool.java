package inspur.tax.service.common;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 * xml处理公用类
 */

public class XMLTool
{
	Logger logger = Logger.getLogger(XMLTool.class);

	public String ISOToGbk(String s) throws UnsupportedEncodingException
	{
		if (null == s || s.trim().equals("")) {
			return null;
		}
		return new String(s.getBytes("ISO-8859-1"), "gb2312");
	}

	public String GbkToISO(String s) throws UnsupportedEncodingException
	{
		if (null == s || s.trim().equals("")) {
			return "";
		}
		return new String(s.getBytes("gb2312"), "ISO-8859-1");
	}

	/**
	 * 根据给定的Document对象，获得根元素
	 * 
	 * @param document
	 * @return
	 * @author qibo 2011-6-21 下午04:36:52
	 */
	public Element getRootElement(Document document)
	{
		if (null != document) {
			return document.getRootElement();
		}
		return null;
	}

	/**
	 * 根据根元素，获得业务ID
	 * 
	 * @param root
	 * @return
	 * @author qibo 2011-6-21 下午04:37:15
	 */
	public String getBussineId(Element root)
	{
		if (null != root) {
			return root.getAttributeValue("ID");
		}
		return null;
	}

	/**
	 * @param root
	 * @param mapFpmx
	 * @param xtsj
	 * @return 功能说明：生成密码修改明细XML文件
	 */
	public String CreateModifyPassXml(Element root, Map mapModifyPass,
			String xtsj, Map nsrxxMap)
	{
		try {

			Element _root = new Element("BUSINESS");
			Attribute idAttribute = new Attribute("ID", "MODIFYPASS");
			_root.setAttribute(idAttribute);
			Element nsrxxElement = this.createReturnNsrxxElement(root, xtsj);
			_root.addContent(nsrxxElement);

			// 生成业务信息XML
			Element infoElement = new Element("RESULT");

			// 密码修改操作结果
			Element codeElement = new Element("CODE");
			if (null == mapModifyPass.get("CODE")) {
				codeElement.setText("");
			} else {
				codeElement.setText(String.valueOf(mapModifyPass.get("CODE"))
						.trim());
			}
			infoElement.addContent(codeElement);

			// 密码修改操作结果描述
			Element descElement = new Element("DESC");
			if (null == mapModifyPass.get("DESC")) {
				descElement.setText("");
			} else {
				descElement.setText(String.valueOf(mapModifyPass.get("DESC"))
						.trim());
			}
			infoElement.addContent(descElement);

			_root.addContent(infoElement);

			Document document = new Document(_root);
			XMLOutputter outputter = new XMLOutputter();
			return outputter.outputString(document);

		} catch (Exception e) {
			String[] strArray = new String[2];
			strArray[0] = "CODE&1";
			strArray[1] = "DESC&生成XML异常";
			return XmlUtil.strArrayToXml("MODIFYPASS", nsrxxMap, strArray,
					"result");
		}
	}

	/**
	 * 生成返回客户端数据纳税人信息xml部分
	 * 
	 * @param root
	 *            接受参数根节点
	 * @param xtsj
	 *            系统时间
	 * @return
	 */
	public Element createReturnNsrxxElement(Element root, String xtsj)
	{
		Element nsrxxElement = root.getChild("NSRXX");
		nsrxxElement.removeChild("DLMM");
		Element xtsjElement = new Element("XTSJ");
		xtsjElement.setText(xtsj);
		nsrxxElement.addContent(xtsjElement);
		Element _nsrxxElement = (Element) nsrxxElement.clone();

		return _nsrxxElement;
	}

	/**
	 * 根节点
	 * 
	 * @return 标签和值对应的Map
	 */
	public Map xmlParseXml(Element root)
	{
		Map nsrxxMap = new HashMap();
		if (null != root) {
			Element nsrxxElement = root.getChild("PARAMETER");
			if (nsrxxElement == null) 
			{
				nsrxxElement = root.getChild("PICTURE");
			}

			List childList = nsrxxElement.getChildren();
			// 解析每个XML段内容，
			if (null != childList && childList.size() > 0) {
				for (int i = 0; i < childList.size(); i++) {
					Element child = (Element) childList.get(i);
					String childName = child.getName();
					String value = child.getText();
					nsrxxMap.put(childName, value);
				}
			}
		}
		System.out.println("0000000000000000"+nsrxxMap);
		return nsrxxMap;
	}

	/**
	 * 根节点
	 * 
	 * @return 标签和值对应的Map
	 */
	public Map xmlParsePara(Element root)
	{
		Map nsrxxMap = new HashMap();
		if (null != root) {
			Element nsrxxElement = root.getChild("PARAMETER");

			List childList = nsrxxElement.getChildren();
			// 解析每个XML段内容，
			if (null != childList && childList.size() > 0) {
				for (int i = 0; i < childList.size(); i++) {
					Element child = (Element) childList.get(i);
					String childName = child.getName();
					String value = child.getText();
					nsrxxMap.put(childName, value);
				}
			}
		}
		return nsrxxMap;
	}

	/**
	 * 解析公返回xml（NSRXX）
	 * 
	 * @param root
	 *            根节点
	 * @return 标签和值对应的Map
	 */
	public Map xmlParserenturn(Element root)
	{
		Map nsrxxMap = new HashMap();
		if (null != root) {
			Element element = root.getChild("RESULT");
			List childList = element.getChildren();
			// 解析每个XML段内容，
			if (null != childList && childList.size() > 0) {
				for (int i = 0; i < childList.size(); i++) {
					Element child = (Element) childList.get(i);
					String childName = child.getName();
					String value = child.getText();
					nsrxxMap.put(childName, value);
				}
			}
		}
		return nsrxxMap;
	}

	// 解析xml成list
	public List zxmlList1(Element root)
	{
		// Map map = new HashMap();
		List listValue = new ArrayList();
		long startTime = System.currentTimeMillis();
		Element nsrxxElement = root.getChild("NSRXX");
		Element countlElement = nsrxxElement.getChild("KJXXINFO");
		String countstrint = countlElement.getAttributeValue("COUNT");
		int countnumber = Integer.valueOf(countstrint).intValue();
		System.out.println("一共有的数据条数为------" + String.valueOf(countnumber));

		List list = countlElement.getChildren();
		List spxxList = new ArrayList();
		Map map1 = new HashMap();
		for (int i = 0; i < list.size(); i++) {
			Map map = new HashMap();
			Element spxx = (Element) list.get(i);
			spxxList = spxx.getChildren();
			// List spxxList = spxx.getChildren();
			// 解析每个XML段内容
			if (null != spxxList && spxxList.size() > 0) {
				for (int i1 = 0; i1 < spxxList.size(); i1++) {
					Element child = (Element) spxxList.get(i1);
					String childName = child.getName();
					String value = null;
					try {
						value = new String(child.getText().getBytes(), "gbk");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					map.put(childName, value);
				}
			}
			// System.out.println("输出--" + map);
			listValue.add(map);

		}
		long endTime = System.currentTimeMillis();
		System.out.println("解析主表用时：" + (endTime - startTime));
		return listValue;
	}

	// 从表解析
	public List cxmlList1(Element root)
	{
		long startTime = System.currentTimeMillis();
		List listValue = new ArrayList();

		Element count = root.getChild("KJMXINFO");
		String countstr = count.getAttributeValue("COUNT");
		int countnum = Integer.valueOf(countstr).intValue();
		System.out.println("一共有的数据条数为------" + String.valueOf(countnum));

		List mapList = count.getChildren();
		for (int i = 0; i < mapList.size(); i++) {
			Map map = new HashMap();
			// Element spxx = count.getChild("KPDXX");
			Element spxx = (Element) mapList.get(i);
			List spxxList = spxx.getChildren();
			// 解析每个XML段内容
			if (null != spxxList && spxxList.size() > 0) {
				for (int i1 = 0; i1 < spxxList.size(); i1++) {
					Element child = (Element) spxxList.get(i1);
					String childName = child.getName();
					String value = null;
					try {
						value = new String(child.getText().getBytes(), "gbk");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					map.put(childName, value);
				}
			}
			// System.out.println("输出--" + map);
			listValue.add(map);
		}
		long endTime = System.currentTimeMillis();
		System.out.println("解析从表用时：" + (endTime - startTime));

		return listValue;
	}

	// 解析xml成list
	public List xmlToListYd(Element root, String info)
	{
		// Map map = new HashMap();
		List listValue = new ArrayList();
		long startTime = System.currentTimeMillis();
		// Element nsrxxElement = root.getChild("NSRXX");
		// Element countlElement = nsrxxElement.getChild("NSRXXINFO");

		Element countlElement = root.getChild(info);

		String countstrint = countlElement.getAttributeValue("COUNT");
		int countnumber = Integer.valueOf(countstrint).intValue();
		System.out.println("一共有的数据条数为------" + String.valueOf(countnumber));

		List list = countlElement.getChildren();
		List spxxList = new ArrayList();
		Map map1 = new HashMap();
		for (int i = 0; i < list.size(); i++) {
			Map map = new HashMap();
			Element spxx = (Element) list.get(i);
			spxxList = spxx.getChildren();
			// List spxxList = spxx.getChildren();
			// 解析每个XML段内容
			if (null != spxxList && spxxList.size() > 0) {
				for (int i1 = 0; i1 < spxxList.size(); i1++) {
					Element child = (Element) spxxList.get(i1);
					String childName = child.getName();
					String value = null;
					try {
						value = new String(child.getText().getBytes(), "gbk");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					map.put(childName, value);
				}
			}
			// System.out.println("输出--"+map);
			listValue.add(map);

		}
		long endTime = System.currentTimeMillis();
		System.out.println("解析主表用时：" + (endTime - startTime));
		return listValue;
	}

	/**
	 * xml字符串转化为Jdom的Document对象
	 * 
	 * @param xml
	 * @return
	 * @author qibo 2011-6-21 下午05:13:58
	 */
	public Document getDocument(String xml)
	{
		SAXBuilder builder = new SAXBuilder();
		// 字符串转化为XML对象
		Document document;
		try {
			document = builder.build(new StringReader(xml));
			return document;
		} catch (JDOMException e) {
			e.printStackTrace();
		}

		return null;
	}

}
