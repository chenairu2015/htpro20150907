package com.htsoft.pro.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * @Copyright (c) 2015, 山东山大华天软件有限公司 All rights reserved.
 * @文件名称：开发带Body的标签库
 * @版本：1.0
 * @作者：chenairu
 * @完成日期：2015年9月18日 下午5:44:55
 * @当前版本：1.0
 * @修改描述：
 * @修改者：chenairu
 * @完成日期：2015年9月18日 下午5:44:55
 */
public class BodyTagExample extends BodyTagSupport {
	
	int counts;// counts为迭代的次数。

	public BodyTagExample() {
		super();
	}
	@Override
	public int doStartTag() throws JspException {
		return super.doStartTag();
	}

	@Override
	public int doEndTag() throws JspException {
		return super.doEndTag();
	}
}
