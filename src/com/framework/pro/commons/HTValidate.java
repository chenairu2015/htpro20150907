package com.framework.pro.commons;

/**
 * 验证类
 * @author chenairu
 *
 */
public class HTValidate {

	/**
	 * 判断object是否是空
	 * 
	 * @param object
	 * @return 为空返回true,不为空返回false
	 */
	public static boolean isNull(Object object) {
		return (object == null) || ("null".equals(object.toString().trim()))
				|| ("".equals(object.toString().trim()));
	}

	/**
	 * 判断object不为空
	 * 
	 * @param object
	 * @return 不为空返回true,为空返回false
	 */
	public static boolean noNull(Object object) {
		return !isNull(object);
	}

	/**
	 * 如果为空则设为默认值
	 * 
	 * @param object
	 *            判断该对象是否为空
	 * @param defaultObject
	 *            如果object是空则默认的对象
	 * @return 不为空的一个对象
	 */
	public static Object nullToDefault(Object object, Object defaultObject) {
		if (noNull(object)) {
			return object;
		}
		return defaultObject;
	}
}
