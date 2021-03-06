package com.framework.pro.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;


/**
 * @Copyright (c) 2015, 
 * All rights reserved.
 * @文件名称：PinyinUtil.java
 * @版本：1.0
 * @作者：chenairu
 * @完成日期：2015年9月10日 上午11:14:06
 * @当前版本：1.0
 * @修改描述：
 * @修改者：chenairu
 * @完成日期：2015年9月10日 上午11:14:06
 * @需要的jar：pinyin4j2.5.0.jar
 */
public class PinyinUtil {

	private static PinyinUtil instance;

	public static PinyinUtil getInstance() {
		if (instance == null) {
			instance = new PinyinUtil();
		}
		return instance;
	}

	private HanyuPinyinOutputFormat outputFormat = null;

	private HanyuPinyinOutputFormat getOutputFormat() {
		if (outputFormat == null) {
			outputFormat = new HanyuPinyinOutputFormat();
			outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			outputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		}
		return outputFormat;
	}

	/**
	 * 获取字符串中所有字符首字母
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public String getInitial(String str) {
		StringBuffer sbf = new StringBuffer();
		if (str != null) {
			for (int i = 0; i < str.length(); i++) {
				if (str.charAt(i)> 128) {
					sbf.append(getCharInitial(str.charAt(i)));
				}else{
					sbf.append(str.charAt(i));
				}
			}
		}
		return sbf.toString();
	}

	/**
	 * 获取中文字符首字母
	 * 
	 * @param c
	 * @return
	 * @throws Exception
	 */
	public String getCharInitial(char c) {
		try {
			String[] strs = PinyinHelper.toHanyuPinyinStringArray(c,
					getOutputFormat());
			String initial = "";
			if (strs != null && strs.length > 0) {
				String str = strs[0];
				if (str != null && str.length() > 0) {
					initial += str.charAt(0);
				}
			}
			return initial;
		} catch (Exception ex) {
			ex.printStackTrace();
			return c + "";
		}
	}

	public String getPinyin(String chines) {

		char[] nameChar = chines.toCharArray();
		String pinyinStr = "";
		for (int i = 0; i < nameChar.length; i++) {
			try {
				char cha = nameChar[i];
				if (nameChar[i] > 128) {
					pinyinStr += PinyinHelper.toHanyuPinyinStringArray(cha,
							getOutputFormat())[0];
				}else{
					pinyinStr += cha;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				pinyinStr += nameChar[i];
			}
		}
		return pinyinStr;
	}

	/**
	 * 获取字符全拼
	 * 
	 * @param c
	 * @return
	 */
	public String getCharPinyin(char c) {

		try {
			String[] strs = PinyinHelper.toHanyuPinyinStringArray(c,
					getOutputFormat());
			String str = "";
			if (strs != null && strs.length > 0) {
				str = strs[0];
			}
			return str;
		} catch (Exception ex) {
			ex.printStackTrace();
			return c + "";
		}
	}

	public static void main(String[] args) {
		String stringInitial = PinyinUtil.getInstance().getInitial(
				"AAAA 玥是个生僻字一般的拼音码解析不了");
		System.out.println(stringInitial);

		String stringPinyin = PinyinUtil.getInstance().getPinyin(
				"VVVV 玥是个生僻字一般的拼音码解析不了");
		System.out.println(stringPinyin);

	}
}