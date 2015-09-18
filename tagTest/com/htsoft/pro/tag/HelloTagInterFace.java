package com.htsoft.pro.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

public class HelloTagInterFace implements Tag {

	private PageContext pageContext;
	private Tag parent;

	public HelloTagInterFace() {
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
