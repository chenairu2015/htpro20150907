package com.framework.pro.dbm.bean;

import com.framework.pro.commons.HTValidate;

/**
 * 类描述：
 * 
 * @since 1.6
 * @version 1.0
 * @author chenairu
 */
public class FieldBean {
	private String column_name; // 列名
	private String data_type; // 字段类型
	private String data_length; // 字段长度
	private String data_default; // 默认值
	private String comments; // 字段注释
	private boolean isPrimary; // 是否是主键
	private String nullAble; // 是否可为空
	private String constraint_name; // 约束名称
	private String columnTagType; // 列标签类型

	public String getColumn_name() {
		return column_name;
	}

	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}

	public String getData_type() {
		return data_type;
	}

	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

	public String getData_length() {
		return data_length;
	}

	public void setData_length(String data_length) {
		this.data_length = data_length;
	}

	public String getData_default() {
		return data_default;
	}

	public void setData_default(String data_default) {
		this.data_default = data_default;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * 获取是否是主键
	 * 
	 * @return 是否主键
	 */
	public boolean isPrimary() {
		if (isPrimary) {
			return true;
		}
		if (HTValidate.noNull(getConstraint_name()))
			isPrimary = true;
		return isPrimary;
	}

	public void setPrimary(boolean isPrimary) {
		this.isPrimary = isPrimary;
	}

	public String getNullAble() {
		return nullAble;
	}

	public void setNullAble(String nullAble) {
		this.nullAble = nullAble;
	}

	public String getConstraint_name() {
		return constraint_name;
	}

	public void setConstraint_name(String constraint_name) {
		this.constraint_name = constraint_name;
	}

	public String getColumnTagType() {
		return columnTagType;
	}

	public void setColumnTagType(String columnTagType) {
		this.columnTagType = columnTagType;
	}

}
