package com.framework.pro.commons;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 类描述：时间公用类
 * 
 * @since 1.6
 * @version 1.0
 * @author chenairu
 * @Company hoteamsoft
 * @date 2015年8月19日 上午9:40:50
 * 
 */
public class HTDateUtil {
	/**
	 * 构造函数私有化
	 */
	private HTDateUtil() {
	}

	/**
	 * 将当前日期转换成yyyy-MM-dd的字符串格式
	 * 
	 * @param date
	 * @param templet
	 * @return
	 */
	public static String getDefalutNowDate() {
		return getDate(null, null);
	}

	/**
	 * 将日期转换成yyyy-MM-dd的字符串格式
	 * 
	 * @param date
	 * @param templet
	 * @return
	 */
	public static String getDate(Date date) {
		return getDate(date, null);
	}

	/**
	 * 将当前日期转换成的字符串格式
	 * 
	 * @param date
	 * @param templet
	 * @return
	 */
	public static String getDate(String templet) {
		return getDate(null, templet);
	}

	/**
	 * 将日期转换成相应的字符串格式
	 * 
	 * @param date
	 * @param templet
	 *            例如 yyyyMMddHHmmssSSS
	 * @return
	 */
	public static String getDate(Date date, String templet) {
		if (templet == null || templet.isEmpty()) {
			templet = "yyyy-MM-dd";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(templet);
		if (date != null) {
			return sdf.format(date);
		} else {
			return sdf.format(new Date());
		}
	}

	/**
	 * 将字符串的stringDate 转换成 templet 格式的日期
	 * 
	 * @param stringDate
	 * @param templet
	 *            （例如 yyyyMMddHHmmssSSS）
	 * @return
	 * @throws Exception
	 */
	public static Date convertToDate(String stringDate, String templet)
			throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(templet);
		Date date = null;
		try {
			date = sdf.parse(stringDate);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new Exception("日期转换错误");
		}
		return date;
	}
}
