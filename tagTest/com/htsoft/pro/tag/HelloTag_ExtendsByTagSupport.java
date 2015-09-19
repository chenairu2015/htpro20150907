package com.htsoft.pro.tag;

import java.io.IOException;
import java.util.Date;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;


// 从TagSupport继承.只需要覆盖TagSupport类的doStartTag和doEndTag两个方法即可，开发比较容易。
public class HelloTag_ExtendsByTagSupport extends TagSupport {

	@Override
	public int doStartTag() throws JspException {
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		String dateString = new Date().toString();
		try {
			pageContext.getOut().write(
					"<br>Hello World hellking.<br>现在的时间是：" + dateString);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}
}
