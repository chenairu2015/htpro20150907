package com.htsoft.pro.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

// 开发带Body的标签库

// 带有Body的Tag必须实现javax.servlet.jsp.tagext.BodyTag接口，BodyTag接口中定义了一些处理标签体的方法。
// 通过实现BodyTag接口，就可以方便的操作标签体，比如可以让标签体迭代多次等。BodyTag的处理过程：

// 1)当容器创建一个新的标签实例后，通过setPageContext来设置标签的页面上下文。
// 2)使用setParent方法设置这个标签的上一级标签。 
// 3)设置标签的属性。 
// 4)调用doStartTag方法，这个方法可以返回EVAL_BODY_INCLUDE和SKIP_BODY，
//	当返回EVAL_BODY_INCLUDE时，就计算标签的Body，如果返回SKIP_BODY，就不计算标签的Body。
// 5)调用setBodyContent设置当前的BodyContent。 
// 6)调用doInitBody，如果在计算BodyContent时需要进行一些初始化，就在这个方法中进行。
// 7)每次计算完BodyTag后调用doAfterBody，如果返回EVAL_BODY_TAG，
//	表示继续计算一次BodyTag，直接返回SKIP_BODY才继续执行doEndTag方法。 
// 8)调用doEndTag方法，这个方法可以返回EVAL_PAGE或者SKIP_PAGE，
//  当返回EVAL_PAGE时，容器将在标签结束时继续计算JSP页面其他的部分；
//	如果返回SKIP_PAGE，容器将在标签结束时停止计算JSP页面其他的部分。 
// 9)调用release()方法释放标签程序占用的任何资源。

public class Hello_ExtendsByBodyTag extends BodyTagSupport {

	int counts;// counts为迭代的次数。

	public Hello_ExtendsByBodyTag() {
		super();
	}

	/**
	 * 设置counts属性。这个方法由容器自动调用。
	 */
	public void setCounts(int c) {
		this.counts = c;
	}

	/**
	 * 覆盖doStartTag方法
	 */
	@Override
	public int doStartTag() throws JspException {
		System.out.println("开始。。。");
		if (counts > 0) {
			return EVAL_BODY_TAG;
		} else {
			return SKIP_BODY;
		}
	}

	/**
	 * 覆盖doAfterBody方法
	 */
	@Override
	public int doAfterBody() {
		System.out.println("doAfterBody" + counts);
		if (counts > 1) {
			counts--;
			return EVAL_BODY_TAG;
		} else {
			return SKIP_BODY;
		}
	}

	/**
	 * 覆盖doEndTag方法
	 */
	@Override
	public int doEndTag() throws JspException {
		System.out.println("doEndTag");
		try {
			if (bodyContent != null) {
				bodyContent.writeOut(bodyContent.getEnclosingWriter());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	public void doInitBody() {
		System.out.println("doInitBody");
	}

	public void setBodyContent(BodyContent bodyContent) {
		System.out.println("setBodyContent");
		this.bodyContent = bodyContent;
	}
}
