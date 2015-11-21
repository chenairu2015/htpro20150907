package inspur.tax.service.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;

/**
 * 用于将Map和List转换成xml，或者将xml转成Map和List，无论由谁向谁转换，xml格式必须固定如上面才行。
 */
public class XmlUtil
{
	private static final Logger logger = Logger.getLogger(XmlUtil.class);

	/**以时间起止,同步数据
	 * @param strId
	 * @param xgrqq
	 * @param xgrqz
	 * @return
	 */
	public static String getrqxml(String strId,String kssj,String jssj)
	{
		StringBuffer xml = new StringBuffer();
		char br = '\n';
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(br);
		xml.append("<BUSINESS ID='").append(strId).append("'>").append(br);
		xml.append("<REQUEST>").append(br);
		xml.append("<REQ_KSSJ>").append(kssj).append("</REQ_KSSJ>").append(br);
		xml.append("<REQ_JSSJ>").append(jssj).append("</REQ_JSSJ>").append(br);
		xml.append("</REQUEST>").append(br);
		xml.append("</BUSINESS>").append(br);
		return xml.toString();
	}

	public static String getrqxml(String strId,String kssj)
	{
		StringBuffer xml = new StringBuffer();
		char br = '\n';
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(br);
		xml.append("<BUSINESS ID='").append(strId).append("'>").append(br);
		xml.append("<REQUEST>").append(br);
		xml.append("<REQ_KSSJ>").append(kssj).append("</REQ_KSSJ>").append(br);
		xml.append("</REQUEST>").append(br);
		xml.append("</BUSINESS>").append(br);
		return xml.toString();
	}
	/**
	 *两个list生成xml报文
	 * @param businessId
	 * @param stringArray
	 * @param pzhdlist
	 * @param list
	 * @param kjxx
	 * @param info
	 * @return
	 */
	public static String twoListToResultXml(String businessId,List zblist,List cblist, String kjxx,String info)
	{
		StringBuffer xml = new StringBuffer();
		kjxx = kjxx.toUpperCase();
		info = info.toUpperCase();
		char br = '\n';
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(br);
		xml.append("<BUSINESS ID='").append(businessId).append("'>").append(br);
		xml.append(" <NSRXX>").append(br);
		xml.append(" <").append(info).append(" COUNT='").append(zblist.size()).append("'>").append(br);
		for (int i = 0; i < zblist.size(); i++)
		{
			String[] strArray = (String[]) zblist.get(i);
			xml.append(" <").append(kjxx).append(">").append(br);
			for (int j = 0; j < strArray.length; j++)
			{
				String key = strArray[j].split("&")[0];
				String value = "";
				if (strArray[j].split("&").length == 1)
				{
					value = "";
				} else
				{
					value = strArray[j].split("&")[1].trim();
				}
				xml.append("   <").append(key.toUpperCase()).append(">");
				xml.append(value);
				xml.append("</").append(key.toUpperCase()).append(">");
				xml.append(br);
			}
			xml.append(" </").append(kjxx).append(">").append(br);
		}
		xml.append(" </").append(info).append(">").append(br);
		xml.append(" </NSRXX>").append(br);
		
		
		
		xml.append(" <").append("KJMXINFO").append(" COUNT='").append(cblist.size()).append("'>").append(br);
		for (int i = 0; i < cblist.size(); i++)
		{
			String[] strArray = (String[]) cblist.get(i);
			xml.append(" <").append("KJXXMX").append(">").append(br);
			for (int j = 0; j < strArray.length; j++)
			{
				String key = strArray[j].split("&")[0];
				String value = "";
				if (strArray[j].split("&").length == 1)
				{
					value = "";
				} else
				{
					value = strArray[j].split("&")[1].trim();
				}
				xml.append("<").append(key.toUpperCase()).append(">");
				xml.append(value);
				xml.append("</").append(key.toUpperCase()).append(">");
				xml.append(br);
			}
			xml.append(" </").append("KJXXMX").append(">").append(br);
		}
		xml.append(" </").append("KJMXINFO").append(">").append(br);
		
		
		
		xml.append("</BUSINESS>").append(br);
//		logger.info(xml.toString());
		return xml.toString();
	}
	
	/**
	 * map转换为xml,带有RESULT信息
	 * 
	 * @param businessId
	 * @param nsrMap
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public static String strArrayToResultXml(String businessId, Map nsrMap,String[] strArray, String[] infoArray, String strTagName) throws Exception
	{
		StringBuffer xml = new StringBuffer();
		strTagName = strTagName.toUpperCase();
		char br = '\n';
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(br);
		xml.append("<BUSINESS ID='").append(businessId).append("'>").append(br);
		xml.append(" <NSRXX>").append(br);
		xml.append("  <USER>").append(nsrMap.get("USER")).append("</USER>").append(br);
		xml.append("  <DLZH>").append(nsrMap.get("DLZH")).append("</DLZH>").append(br);
		xml.append("  <NSRSBH>").append(nsrMap.get("NSRSBH")).append("</NSRSBH>").append(br);
		xml.append("  <DLZH>").append(nsrMap.get("DLZH")).append("</DLZH>").append(br);
		xml.append("  <JQBH>").append(nsrMap.get("JQBH")).append("</JQBH>").append(br);
		xml.append("  <XTSJ>").append(new Date()).append("</XTSJ>").append(br);
//		xml.append("  <FH>").append(GgUtil.EncryCfca("tanxianglu")).append("</FH>").append(br);
		xml.append(" </NSRXX>").append(br);
		xml.append(" <RESULT>").append(br);
		for (int i = 0; i < strArray.length; i++)
		{
			String key = strArray[i].split("&")[0];
			String value = strArray[i].split("&")[1];
			xml.append("   <").append(key.toUpperCase()).append(">");
			xml.append(value);
			xml.append("</").append(key.toUpperCase()).append(">");
			xml.append(br);
		}
		xml.append(" </RESULT>").append(br);
		xml.append(" <").append(strTagName).append(">").append(br);
		for (int i = 0; i < infoArray.length; i++)
		{
			String key = infoArray[i].split("&")[0];
			String value = "";
			if (infoArray[i].split("&").length == 1)
			{
				value = "";
			} else
			{
				value = infoArray[i].split("&")[1].trim();
			}
			xml.append("   <").append(key.toUpperCase()).append(">");
			xml.append(value);
			xml.append("</").append(key.toUpperCase()).append(">");
			xml.append(br);
		}
		xml.append(" </").append(strTagName).append(">").append(br);
		xml.append("</BUSINESS>").append(br);
		logger.info(xml.toString());
		return xml.toString();
	}
	
	
	/**
	 * list和map转换为xml,带有RESULT信息
	 * 
	 * @param businessId
	 * @param nsrMap
	 * @param list
	 * @return
	 */
	public static String listAndArrayToResultXml(String businessId, Map nsrMap,List list, String listTagName, String rowTagName,
			String[] strArray, String arrTagName, String[] stringArray)
	{
		StringBuffer xml = new StringBuffer();
		listTagName = listTagName.toUpperCase();
		rowTagName = rowTagName.toUpperCase();
		char br = '\n';
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(br);
		xml.append("<BUSINESS ID='").append(businessId).append("'>").append(br);
		xml.append(" <NSRXX>").append(br);
		xml.append("  <NSRSBH>").append(nsrMap.get("NSRSBH")).append("</NSRSBH>").append(br);
		xml.append("  <DLZH>").append(nsrMap.get("DLZH")).append("</DLZH>").append(br);
		xml.append("  <JQBH>").append(nsrMap.get("JQBH")).append("</JQBH>").append(br);
		xml.append("  <XTSJ>").append(GgUtil.getCurrentTime()).append("</XTSJ>").append(br);
		xml.append(" </NSRXX>").append(br);
		xml.append(" <RESULT>").append(br);
		for (int i = 0; i < stringArray.length; i++)
		{
			String key = stringArray[i].split("&")[0];
			String value = stringArray[i].split("&")[1];
			xml.append("   <").append(key.toUpperCase()).append(">");
			xml.append(value);
			xml.append("</").append(key.toUpperCase()).append(">");
			xml.append(br);
		}
		xml.append(" </RESULT>").append(br);
		xml.append(" <INFO>").append(br);
		xml.append(" <").append(arrTagName).append(">").append(br);
		for (int i = 0; i < strArray.length; i++)
		{
			String key = strArray[i].split("&")[0];
			String value = "";
			if (strArray[i].split("&").length == 1)
			{
				value = "";
			} else
			{
				value = strArray[i].split("&")[1].trim();
			}
			xml.append("   <").append(key.toUpperCase()).append(">");
			xml.append(value);
			xml.append("</").append(key.toUpperCase()).append(">");
			xml.append(br);
		}
		xml.append(" </").append(arrTagName).append(">").append(br);
		xml.append(" <").append(listTagName).append(" COUNT='").append(list.size()).append("'>").append(br);
		for (int i = 0; i < list.size(); i++)
		{
			String[] rowArray = (String[]) list.get(i);
			xml.append(" <").append(rowTagName).append(">").append(br);
			for (int j = 0; j < rowArray.length; j++)
			{
				String key = rowArray[j].split("&")[0];
				String value = "";
				if (rowArray[j].split("&").length == 1)
				{
					value = "";
				} else
				{
					value = rowArray[j].split("&")[1].trim();
				}
				xml.append("   <").append(key.toUpperCase()).append(">");
				xml.append(value);
				xml.append("</").append(key.toUpperCase()).append(">");
				xml.append(br);
			}
			xml.append(" </").append(rowTagName).append(">").append(br);
		}
		xml.append(" </").append(listTagName).append(">").append(br);
		xml.append(" </INFO>").append(br);
		xml.append("</BUSINESS>").append(br);
		logger.info(xml.toString());
		return xml.toString();
	}

	
	/**
	 *一个list生成xml 
	 * @param businessId
	 * @param zbList
	 * @param zbKey
	 * @param stringArray
	 * @return
	 */
	public static String onelistToXml(String businessId, List zbList, String zbKey,String[] stringArray)
	{
		StringBuffer xml = new StringBuffer();
		char br = '\n';
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(br);
		xml.append("<BUSINESS ID='").append(businessId).append("'>").append(br);
		xml.append("<").append(zbKey+"INFO").append(" COUNT='").append(zbList.size()).append("'>").append(br);
		
		for (int i = 0; i < zbList.size(); i++)
		{
			xml.append(" <").append(zbKey).append(">").append(br);
			String[] strArray = (String[]) zbList.get(i);
			for (int j = 0; j < strArray.length; j++)
			{
				String key = strArray[j].split("&")[0];
				String value = "";
				if (strArray[j].split("&").length == 1)
				{
					value = "";
				} else
				{
					value = strArray[j].split("&")[1].trim();
				}
				xml.append("<").append(key.toUpperCase()).append(">");
				xml.append(value);
				xml.append("</").append(key.toUpperCase()).append(">");
				xml.append(br);
			}
			xml.append(" </").append(zbKey).append(">").append(br);
		}
		xml.append("</").append(zbKey+"INFO").append(">").append(br);
 
		xml.append(" <RESULT>").append(br);
		for (int i = 0; i < stringArray.length; i++)
		{
			String key = stringArray[i].split("&")[0];
			String value = stringArray[i].split("&")[1];
			xml.append("   <").append(key.toUpperCase()).append(">");
			xml.append(value);
			xml.append("</").append(key.toUpperCase()).append(">");
			xml.append(br);
		}
		xml.append(" </RESULT>").append(br);
		xml.append("</BUSINESS>").append(br);
		return xml.toString();
	}
	
	 
	public static String listToXmlDZ(String businessId, List zbList, String zbKey,String num,Map map)
	{
		StringBuffer xml = new StringBuffer();
		char br = '\n';
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(br);
		xml.append("<BUSINESS ID='").append(businessId).append("'>").append(br);
		
		xml.append("<REQUEST>").append(br); 
		xml.append("<KSSJ>").append(map.get("KSSJ")).append("</KSSJ>").append(br);
		xml.append("<JSSJ>").append(map.get("JSSJ")).append("</JSSJ>").append(br);
		xml.append("<NSRSBH>").append(map.get("NSRSBH")).append("</NSRSBH>").append(br);
		xml.append("<CYZTS>").append(num).append("</CYZTS>").append(br);
		xml.append("</REQUEST>").append(br);
		
		xml.append(" <").append("NSRXX").append(">").append(br);
		xml.append("<").append(zbKey+"INFO").append(" COUNT='").append(zbList.size()).append("'>").append(br);
		
		for (int i = 0; i < zbList.size(); i++)
		{
			xml.append(" <").append(zbKey).append(">").append(br);
//			String[] strArray = (String[]) zbList.get(i);
			Map map3 = new HashMap();
			map3.putAll((Map) zbList.get(i));
			String fphm = map3.get("fphm").toString();
			String fpdm = map3.get("fpdm").toString();
			String[] strArray = new String[2];
			strArray[0] = "FPHM&"+fphm;
			strArray[1] = "FP_DM&"+fpdm;
			
			for (int j = 0; j < strArray.length; j++)
			{
				String key = strArray[j].split("&")[0];
				String value = "";
				if (strArray[j].split("&").length == 1)
				{
					value = "";
				} else
				{
					value = strArray[j].split("&")[1].trim();
				}
				xml.append("<").append(key.toUpperCase()).append(">");
				xml.append(value);
				xml.append("</").append(key.toUpperCase()).append(">");
				xml.append(br);
			}
			xml.append(" </").append(zbKey).append(">").append(br);
		}
		xml.append("</").append(zbKey+"INFO").append(">").append(br);
		xml.append(" <").append("/NSRXX").append(">").append(br);
		xml.append("</BUSINESS>").append(br);
		return xml.toString();
	}
	
	/**一个list生成报文
	 * @param businessId
	 * @param zbList
	 * @param zbKey
	 * @return
	 */
	public static String onelistToXml(String businessId, List zbList, String zbKey)
	{
		StringBuffer xml = new StringBuffer();
		char br = '\n';
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(br);
		xml.append("<BUSINESS ID='").append(businessId).append("'>").append(br);
		xml.append("<").append(zbKey+"INFO").append(" COUNT='").append(zbList.size()).append("'>").append(br);
		
		for (int i = 0; i < zbList.size(); i++)
		{
			xml.append(" <").append(zbKey).append(">").append(br);
			String[] strArray = (String[]) zbList.get(i);
			for (int j = 0; j < strArray.length; j++)
			{
				String key = strArray[j].split("&")[0];
				String value = "";
				if (strArray[j].split("&").length == 1)
				{
					value = "";
				} else
				{
					value = strArray[j].split("&")[1].trim();
				}
				xml.append("<").append(key.toUpperCase()).append(">");
				xml.append(value);
				xml.append("</").append(key.toUpperCase()).append(">");
				xml.append(br);
			}
			xml.append(" </").append(zbKey).append(">").append(br);
		}
		xml.append("</").append(zbKey+"INFO").append(">").append(br);
		xml.append("</BUSINESS>").append(br);
		return xml.toString();
	}
	
 
	/**
	 * SpringTimer定时器报文生成----基本数据
	 */
	public static String getYwxml(String strId,String kssj)
	{
		StringBuffer xml = new StringBuffer();
		char br = '\n';
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(br);
		xml.append("<BUSINESS ID='").append(strId).append("'>").append(br);
		xml.append("<REQUEST>").append(br);
		xml.append("<KSSJ>").append(kssj).append("</KSSJ>").append(br);
		xml.append("<NSRSBH>").append(GgUtil.getProperties("info.properties", "NSRSBH")).append("</NSRSBH>").append(br);
		xml.append("</REQUEST>").append(br);
		xml.append("</BUSINESS>").append(br);
		return xml.toString();
	}
	
	//
	public static String listToXmlBlock(List pzhdlist,List list, String kpdxxinfo,String kpdxx)
	{
		StringBuffer xml = new StringBuffer();
		kpdxxinfo = kpdxxinfo.toUpperCase();
		kpdxx = kpdxx.toUpperCase();
		char br = '\n';
		xml.append(" <").append("BLOCK").append(">").append(br);
		xml.append(" <").append("KJXXZB").append(">").append(br);
		
		for (int i = 0; i < pzhdlist.size(); i++)
		{
			String[] strArray = (String[]) pzhdlist.get(i);
			for (int j = 0; j < strArray.length; j++)
			{
				String key = strArray[j].split("&")[0];
				String value = "";
				if (strArray[j].split("&").length == 1)
				{
					value = "";
				} else
				{
					value = strArray[j].split("&")[1].trim();
				}
				xml.append("   <").append(key.toUpperCase()).append(">");
				xml.append(value);
				xml.append("</").append(key.toUpperCase()).append(">");
				xml.append(br);
			}
		}
		xml.append(" </").append("KJXXZB").append(">").append(br);


		
		xml.append(" <").append(kpdxxinfo).append(" COUNT='").append(list.size()).append("'>").append(br);
		for (int i = 0; i < list.size(); i++)
		{
			String[] strArray = (String[]) list.get(i);
			xml.append(" <").append(kpdxx).append(">").append(br);
			for (int j = 0; j < strArray.length; j++)
			{
				String key = strArray[j].split("&")[0];
				String value = "";
				if (strArray[j].split("&").length == 1)
				{
					value = "";
				} else
				{
					value = strArray[j].split("&")[1].trim();
				}
				xml.append("   <").append(key.toUpperCase()).append(">");
				xml.append(value);
				xml.append("</").append(key.toUpperCase()).append(">");
				xml.append(br);
			}
			xml.append(" </").append(kpdxx).append(">").append(br);
		}
		xml.append(" </").append(kpdxxinfo).append(">").append(br);
		xml.append(" </").append("BLOCK").append(">").append(br);
		//logger.info(xml.toString());
		return xml.toString();
	}
	
	
	
	

	
	
	/**
	 * SpringTimer定时器报文生成----基本数据
	 */
	public static String getYwxml(String strId,String kssj,String jssj)
	{
		StringBuffer xml = new StringBuffer();
		char br = '\n';
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(br);
		xml.append("<BUSINESS ID='").append(strId).append("'>").append(br);
		xml.append("<REQUEST>").append(br);
		xml.append("<KSSJ>").append(kssj).append("</KSSJ>").append(br);
		xml.append("<JSSJ>").append(jssj).append("</JSSJ>").append(br);
		xml.append("<NSRSBH>").append(GgUtil.getProperties("info.properties", "NSRSBH")).append("</NSRSBH>").append(br);
		xml.append("</REQUEST>").append(br);
		xml.append("</BUSINESS>").append(br);
		return xml.toString();
	}
	
	public static String getDqsjxml(String strId)
	{
		StringBuffer xml = new StringBuffer();
		char br = '\n';
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(br);
		xml.append("<BUSINESS ID='").append(strId).append("'>").append(br);
		xml.append("<PARAMETER>").append(br);
		xml.append("</PARAMETER>").append(br);
		xml.append("</BUSINESS>").append(br);
		return xml.toString();
	}
 
	
	public static String strToXml(String strId,String str)
	{
		StringBuffer xml = new StringBuffer();
		char br = '\n';
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(br);
		xml.append("<BUSINESS ID='").append(strId).append("'>").append(br);
		xml.append("<RESULT>").append(br);
		xml.append("<EWM>").append(str).append("</EWM>");
		xml.append("</RESULT>").append(br);
		xml.append("</BUSINESS>").append(br);
		return xml.toString();
	}
	
	 
	public static String strXml(String businessId,String[] strArray, String strTagName)
	{
		StringBuffer xml = new StringBuffer();
		strTagName = strTagName.toUpperCase();
		char br = '\n';
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(br);
		xml.append("<BUSINESS ID='").append(businessId).append("'>").append(br);
		xml.append(" <").append(strTagName).append(">").append(br);
		for (int i = 0; i < strArray.length; i++)
		{
			String key = strArray[i].split("&")[0];
			String value = "";
			if (strArray[i].split("&").length == 1)
			{
				value = "";
			} else
			{
				value = strArray[i].split("&")[1].trim();
			}
			xml.append("   <").append(key.toUpperCase()).append(">");
			xml.append(value);
			xml.append("</").append(key.toUpperCase()).append(">");
			xml.append(br);
		}
		xml.append(" </").append(strTagName).append(">").append(br);
		xml.append("</BUSINESS>").append(br);
		logger.info(xml.toString());
		return xml.toString();
	}
  
 
 
	
	public static String listToResultXml_6(String businessId,List pzhdlist,List list, String kjxx,String info)
	{
		StringBuffer xml = new StringBuffer();
		kjxx = kjxx.toUpperCase();
		info = info.toUpperCase();
		char br = '\n';
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(br);
		xml.append("<BUSINESS ID='").append(businessId).append("'>").append(br);
		xml.append(" <NSRXX>").append(br);
		xml.append(" <").append(info).append(" COUNT='").append(pzhdlist.size()).append("'>").append(br);
		for (int i = 0; i < pzhdlist.size(); i++)
		{
			String[] strArray = (String[]) pzhdlist.get(i);
			xml.append(" <").append(kjxx).append(">").append(br);
			for (int j = 0; j < strArray.length; j++)
			{
				String key = strArray[j].split("&")[0];
				String value = "";
				if (strArray[j].split("&").length == 1)
				{
					value = "";
				} else
				{
					value = strArray[j].split("&")[1].trim();
				}
				xml.append("   <").append(key.toUpperCase()).append(">");
				xml.append(value);
				xml.append("</").append(key.toUpperCase()).append(">");
				xml.append(br);
			}
			xml.append(" </").append(kjxx).append(">").append(br);
		}
		xml.append(" </").append(info).append(">").append(br);
		xml.append(" </NSRXX>").append(br);
		
		xml.append(" <").append("KJMXINFO").append(" COUNT='").append(list.size()).append("'>").append(br);
		for (int i = 0; i < list.size(); i++)
		{
			String[] strArray = (String[]) list.get(i);
			xml.append(" <").append("KJXXMX").append(">").append(br);
			for (int j = 0; j < strArray.length; j++)
			{
				String key = strArray[j].split("&")[0];
				String value = "";
				if (strArray[j].split("&").length == 1)
				{
					value = "";
				} else
				{
					value = strArray[j].split("&")[1].trim();
				}
				xml.append("   <").append(key.toUpperCase()).append(">");
				xml.append(value);
				xml.append("</").append(key.toUpperCase()).append(">");
				xml.append(br);
			}
			xml.append(" </").append("KJXXMX").append(">").append(br);
		}
		xml.append(" </").append("KJMXINFO").append(">").append(br);
		
		xml.append("</BUSINESS>").append(br);
//		logger.info(xml.toString());
		return xml.toString();
	}
 
	/**
	 * map转换为xml
	 * 
	 * @param businessId
	 * @param nsrMap
	 * @param strArray
	 * @param strTagName
	 * @return
	 */
	public static String strArrayToXml(String businessId, Map nsrMap,String[] strArray, String strTagName)
	{
		StringBuffer xml = new StringBuffer();
		strTagName = strTagName.toUpperCase();
		char br = '\n';
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(br);
		xml.append("<BUSINESS ID='").append(businessId).append("'>").append(br);
		xml.append(" <").append(strTagName).append(">").append(br);
		for (int i = 0; i < strArray.length; i++)
		{
			String key = strArray[i].split("&")[0];
			String value = "";
			if (strArray[i].split("&").length == 1)
			{
				value = "";
			} else
			{
				value = strArray[i].split("&")[1].trim();
			}
			xml.append("   <").append(key.toUpperCase()).append(">");
			xml.append(value);
			xml.append("</").append(key.toUpperCase()).append(">");
			xml.append(br);
		}
		xml.append(" </").append(strTagName).append(">").append(br);
		xml.append("</BUSINESS>").append(br);
		logger.info(xml.toString());
		return xml.toString();
	}
	
	
	
	
	/**
	 * 返回xml
	 *
	 */
	public static String returnXml(String businessId,String[] strArray, String strTagName)
	{
		StringBuffer xml = new StringBuffer();
		strTagName = strTagName.toUpperCase();
		char br = '\n';
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(br);
		xml.append("<BUSINESS ID='").append(businessId).append("'>").append(br);
		xml.append(" <").append(strTagName).append(">").append(br);
		for (int i = 0; i < strArray.length; i++)
		{
			String key = strArray[i].split("&")[0];
			String value = "";
			if (strArray[i].split("&").length == 1)
			{
				value = "";
			} else
			{
				value = strArray[i].split("&")[1].trim();
			}
			xml.append("   <").append(key.toUpperCase()).append(">");
			xml.append(value);
			xml.append("</").append(key.toUpperCase()).append(">");
			xml.append(br);
		}
		xml.append(" </").append(strTagName).append(">").append(br);
		xml.append("</BUSINESS>").append(br);
		logger.info(xml.toString());
		return xml.toString();
	}
	
	
	
	public static String listToXmlBlocks(String businessId,String headinfo,String[] stringArray,List nsrkjxxLsh,String xmlstr,String totalNum)
	{
		StringBuffer xml = new StringBuffer();
		char br = '\n';
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(br);
		xml.append("<BUSINESS ID='").append(businessId).append("'>").append(br);
		xml.append("<TOTALNUM>").append(totalNum).append("</TOTALNUM>").append(br);
		xml.append("<").append(headinfo).append(" COUNT='").append(nsrkjxxLsh.size()).append("'>").append(br);
		xml.append(xmlstr);

		xml.append(" </").append(headinfo).append(">").append(br);
		xml.append(" <RESULT>").append(br);
		for (int i = 0; i < stringArray.length; i++)
		{
			String key = stringArray[i].split("&")[0];
			String value = stringArray[i].split("&")[1];
			xml.append("   <").append(key.toUpperCase()).append(">");
			xml.append(value);
			xml.append("</").append(key.toUpperCase()).append(">");
			xml.append(br);
		}
		xml.append(" </RESULT>").append(br);
		xml.append("</BUSINESS>").append(br);
		return xml.toString();
	}
	

	
	
	
	

	public static String richListToXml(String businessId, Map nsrMap,List list, String tagName, String listTagName, String childTag,
			String rtnFlag, String rtnMsg)
	{
		StringBuffer xml = new StringBuffer();
		tagName = tagName.toUpperCase();
		char br = '\n';
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(br);
		xml.append("<BUSINESS ID='").append(businessId).append("'>").append(br);
		xml.append(" <NSRXX>").append(br);
		xml.append("  <NSRBM>").append(nsrMap.get("NSRBM")).append("</NSRBM>").append(br);
		xml.append("  <NSRSBH>").append(nsrMap.get("NSRSBH")).append("</NSRSBH>").append(br);
		xml.append("  <DLZH>").append(nsrMap.get("DLZH")).append("</DLZH>").append(br);
		xml.append("  <JQBH>").append(nsrMap.get("JQBH")).append("</JQBH>").append(br);
		xml.append("  <XTSJ>").append(GgUtil.getCurrentTime()).append("</XTSJ>").append(br);
		xml.append(" </NSRXX>").append(br);
		xml.append(" <RESULT>").append(br);
		xml.append("   <CODE>").append(rtnFlag).append("</CODE>").append(br);
		if ("0".equals(rtnFlag))
		{
			xml.append("   <DESC>上传成功!</DESC>").append(br);
		} else
		{
			xml.append("   <DESC>上传失败!").append(rtnMsg).append("</DESC>").append(br);
		}
		xml.append(" </RESULT>").append(br);
		xml.append(" <").append(tagName).append(">").append(br);
		xml.append("   <").append(listTagName).append(" COUNT=\"").append(list.size()).append("\">").append(br);
		for (int j = 0; j < list.size(); j++)
		{
			xml.append("     <").append(childTag).append(">").append(br);
			String[] strArray = (String[]) list.get(j);
			for (int i = 0; i < strArray.length; i++)
			{
				String key = strArray[i].split("&")[0];
				String value = "";
				if (strArray[i].split("&").length == 1)
				{
					value = "";
				} else
				{
					value = strArray[i].split("&")[1].trim();
				}
				xml.append("   <").append(key.toUpperCase()).append(">");
				xml.append(value);
				xml.append("</").append(key.toUpperCase()).append(">");
				xml.append(br);
			}
			xml.append("     </").append(childTag).append(">").append(br);
		}
		xml.append("   </").append(listTagName).append(">").append(br);
		xml.append(" </").append(tagName).append(">").append(br);
		xml.append("</BUSINESS>").append(br);
		logger.info(xml.toString());
		return xml.toString();
	}

	/**
	 * list和map转换为xml
	 * 
	 * @param businessId
	 * @param nsrMap
	 * @param list
	 * @return
	 */
	public static String listAndArrayToXml(String businessId, Map nsrMap,List list, String listTagName, String rowTagName,
			String[] strArray, String arrTagName)
	{
		StringBuffer xml = new StringBuffer();
		listTagName = listTagName.toUpperCase();
		rowTagName = rowTagName.toUpperCase();
		char br = '\n';
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(br);
		xml.append("<BUSINESS ID='").append(businessId).append("'>").append(br);
		xml.append(" <NSRXX>").append(br);
		xml.append("  <NSRBM>").append(nsrMap.get("NSRBM")).append("</NSRBM>").append(br);
		xml.append("  <NSRSBH>").append(nsrMap.get("NSRSBH")).append("</NSRSBH>").append(br);
		xml.append("  <DLZH>").append(nsrMap.get("DLZH")).append("</DLZH>").append(br);
		xml.append("  <JQBH>").append(nsrMap.get("JQBH")).append("</JQBH>").append(br);
		xml.append("  <XTSJ>").append(GgUtil.getCurrentTime()).append("</XTSJ>").append(br);
		xml.append(" </NSRXX>").append(br);
		xml.append(" <").append(arrTagName).append(">").append(br);
		for (int i = 0; i < strArray.length; i++)
		{
			String key = strArray[i].split("&")[0];
			String value = "";
			if (strArray[i].split("&").length == 1)
			{
				value = "";
			} else
			{
				value = strArray[i].split("&")[1].trim();
			}
			xml.append("   <").append(key.toUpperCase()).append(">");
			xml.append(value);
			xml.append("</").append(key.toUpperCase()).append(">");
			xml.append(br);
		}
		xml.append(" </").append(arrTagName).append(">").append(br);
		xml.append(" <").append(listTagName).append(" COUNT='").append(list.size()).append("'>").append(br);
		for (int i = 0; i < list.size(); i++)
		{
			String[] rowArray = (String[]) list.get(i);
			xml.append(" <").append(rowTagName).append(">").append(br);
			for (int j = 0; j < rowArray.length; j++)
			{
				String key = rowArray[j].split("&")[0];
				String value = "";
				if (rowArray[j].split("&").length == 1)
				{
					value = "";
				} else
				{
					value = rowArray[j].split("&")[1].trim();
				}
				xml.append("   <").append(key.toUpperCase()).append(">");
				xml.append(value);
				xml.append("</").append(key.toUpperCase()).append(">");
				xml.append(br);
			}
			xml.append(" </").append(rowTagName).append(">").append(br);
		}
		xml.append(" </").append(listTagName).append(">").append(br);
		xml.append("</BUSINESS>").append(br);
		logger.info(xml.toString());
		return xml.toString();
	}

	/**
	 * 从xml中获取Map(重命名以后的)
	 * 
	 * @param xml
	 * @return
	 * @throws ParseXmlException
	 */
	public static Map getMapFromXml(String xml, String mapTag)throws ParseXmlException
	{
		Map map = new HashMap();
		try
		{
			logger.info("88888888888");
			
			mapTag = mapTag.toUpperCase();
			XMLTool tool = new XMLTool();
			Document document = tool.getDocument(xml);
			Element rootElement = tool.getRootElement(document);
			Element mapElement = rootElement.getChild(mapTag);
			if (mapElement == null)
			{
				mapElement = rootElement.getChild("PARAMETER");
				mapElement = mapElement.getChild(mapTag);
			}
			List childList = mapElement.getChildren();
			for (int i = 0; i < childList.size(); i++)
			{
				Element child = (Element) childList.get(i);
				String childName = child.getName();
				String value = child.getText();
				value = value == null ? "" : value;
				map.put(renameLabel(childName.trim()), value.trim());
			}
		} catch (RuntimeException e)
		{
			throw new ParseXmlException();
		}
		return map;
	}

	/**
	 * 从xml中获取List(重命名以后的)
	 * 
	 * @param xml
	 * @return
	 * @throws ParseXmlException
	 */
	public static List getListFromXml(String xml, String listTag)throws ParseXmlException
	{
		List list = new ArrayList();
		try
		{
			listTag = listTag.toUpperCase();
			XMLTool tool = new XMLTool();
			Document document = tool.getDocument(xml);
			Element rootElement = tool.getRootElement(document);
			Element listElement = rootElement.getChild(listTag);
			if (listElement == null)
			{
				listElement = rootElement.getChild("PICTURE");
				listElement = listElement.getChild(listTag);
			}
			List mapList = listElement.getChildren();
			for (int i = 0; i < mapList.size(); i++)
			{
				Map map = new HashMap();
				Element mapElement = (Element) mapList.get(i);
				List childList = mapElement.getChildren();
				for (int j = 0; j < childList.size(); j++)
				{
					Element child = (Element) childList.get(j);
					String childName = child.getName();
					String value = child.getText();
					value = value == null ? "" : value;
					map.put(renameLabel(childName.trim()), value.trim());
				}
				list.add(map);
			}
		} catch (RuntimeException e)
		{
			throw new Exception();
		}
		return list;
	}

 
	/**
	 * 获取子节点的List
	 * 
	 * @param element
	 * @return
	 * @throws ParseXmlException
	 */
	private static List getChildListFromXml(Element element)throws ParseXmlException
	{
		List list = new ArrayList();
		try
		{
			List mapList = element.getChildren();
			for (int i = 0; i < mapList.size(); i++)
			{
				Map map = new HashMap();
				Element mapElement = (Element) mapList.get(i);
				List childList = mapElement.getChildren();
				for (int j = 0; j < childList.size(); j++)
				{
					Element child = (Element) childList.get(j);
					String childName = child.getName();
					String value = child.getText();
					value = value == null ? "" : value;
					map.put(renameLabel(childName.trim()), value.trim());
				}
				list.add(map);
			}
		} catch (RuntimeException e)
		{
			throw new Exception();
		}
		return list;
	}

	/**
	 * 处理掉label的下划线并转化大小写,如CJXM_DM->cjxmDm;AA_BB_CC->aaBbCc
	 */
	private static String renameLabel(String label)
	{
		String[] ca = label.toLowerCase().split("_");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ca.length; i++)
		{
			if (sb.length() == 0)
				sb.append(ca[i]);
			else
			{
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
