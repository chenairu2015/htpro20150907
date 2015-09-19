package com.t1.test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * @Copyright (c) 2015, 山东山大华天软件有限公司 All rights reserved.
 * @文件名称：遍历Map的四种方法
 * @版本：1.0
 * @作者：chenairu
 * @完成日期：2015年9月19日 上午10:47:05
 * @当前版本：1.0
 * @修改描述：
 * @修改者：chenairu
 * @完成日期：2015年9月19日 上午10:47:05
 */
public class MapTest {
	public static void main(String[] args) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("1", "value1");
		map.put("2", "value2");
		map.put("3", "value3");

		// 第一种：普遍使用，二次取值
		System.out.println("通过Map.keySet遍历key和value：");
		for (String key : map.keySet()) {
			System.out.println("key= " + key + " and value= " + map.get(key));
		}
		
		// 第四种
		System.out.println("通过Map.values()遍历所有的value，但不能遍历key");
		for (String v : map.values()) {
			System.out.println("value= " + v);
		}
		
		// 第二种
		System.out.println("通过Map.entrySet使用iterator遍历key和value：");
		Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			System.out.println("key= " + entry.getKey() + " and value= "
					+ entry.getValue());
		}

		// 第三种：推荐，尤其是容量大时
		System.out.println("通过Map.entrySet遍历key和value");
		for (Map.Entry<String, String> entry : map.entrySet()) {
			System.out.println("key= " + entry.getKey() + " and value= "
					+ entry.getValue());
		}

	}
}
