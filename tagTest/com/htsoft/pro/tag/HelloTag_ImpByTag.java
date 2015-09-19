package com.htsoft.pro.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

//实现Tag接口
//
//按照下面的步骤进行：
//
//1)开发标签实现类
//
//2)编写标签描述文件，tld为扩展名。
//
//3)在Web.xml中映射标签库的使用。
//
//4)在JSP网页中调用标签。
// 
// doStartTag的返回值
//在doStartTag返回的值决定的body部分的数据如何显示。
//两个返回值：
//0 – SKIP_BODY – 常量。不显示body。
//1－EVAN_BODY_INCLUDE ;包含body部分的数据，正常显示。
//
//3：在doEndTag也有两个返回值
//决定后面的页面部分是否显示：
//SKIP_PAGE : 不再显示后面的页面部分。
//EVAL_PAGE : 显示后面的page部分。
public class HelloTag_ImpByTag implements Tag {

	private PageContext pageContext;
	private Tag parent;

	public HelloTag_ImpByTag() {
		super();
	}

	// 设置标签的页面的上下文
	@Override
	public void setPageContext(PageContext pageContext) {
		this.pageContext = pageContext;
	}

	// 设置上一级标签
	@Override
	public void setParent(Tag parent) {
		this.parent = parent;
	}

	// 开始标签时的操作
	@Override
	public int doStartTag() throws JspException {
		return SKIP_BODY; // 返回SKIP_BODY，表示不计算标签体
	}

	// 结束标签时的操作
	@Override
	public int doEndTag() throws JspException {
		try {
			pageContext.getOut().write("hello world!");
		} catch (IOException e) {
			e.printStackTrace();
			throw new JspTagException("IO Exception :" + e.getMessage());
		}
		return EVAL_PAGE;
	}

	// 用于释放标签程序占用的资源，如果使用了数据库，那么应关闭连接
	@Override
	public void release() {
	}

	@Override
	public Tag getParent() {
		return parent;
	}
}
