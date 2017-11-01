package com.zhsw.tool;

public class DatabaseBean {
	//数据库用户（userName）、数据库密码（password）、数据库类型（driverClass）、数据库地址（dbUrl）
	/**数据库用户*/
	private String userName;
	/**数据库密码*/
	private String password;
	/**数据库类型*/
	private String driverClass;
	/**数据库地址*/
	private String dbUri;
	
	
	public DatabaseBean(String userName, String password, String driverClass, String dbUri) {
		super();
		this.userName = userName;
		this.password = password;
		this.driverClass = driverClass;
		this.dbUri = dbUri;
	}
	
	public DatabaseBean() {
		super();
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDriverClass() {
		return driverClass;
	}
	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}
	public String getDbUri() {
		return dbUri;
	}
	public void setDbUri(String dbUri) {
		this.dbUri = dbUri;
	}
	
	
}
