package com.zhsw.tool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class DatabaseService {
	/**数据库实体类*/
	private DatabaseBean databaseBean;
	/**数据库连接*/
	private Connection connection;
	/**声明，即向数据库发送要执行的sql*/
	private Statement statement;
	/**查询数据库时的结果集*/
	private ResultSet rs;
	
	public DatabaseService(DatabaseBean databaseBean){
		this.databaseBean=databaseBean;
	}
	
	protected void init() throws SQLException, ClassNotFoundException{
		Class.forName(databaseBean.getDriverClass());  
	    this.connection = DriverManager.getConnection(databaseBean.getDbUri(), databaseBean.getUserName(), databaseBean.getPassword());  
	    this.statement = this.connection.createStatement();  
	}
	
	/**
	 * 关闭数据库连接
	 * @throws SQLException 
	 * */
	protected void closeConnectionAndResultSet() throws SQLException{
		if(this.rs!=null){
			this.rs.close();
		}
		if(this.statement!=null){
			this.statement.close();
		}
		if(this.connection!=null){
			this.connection.close();
		}
	}
	
	/**
	 * 获取表机构
	 * 参数 ：数据看表名称（tableName）
	 * 返回 ：表机构
	 * */
	protected ResultSetMetaData getMetaDataFromTable(String tableName) throws Exception {
	    String sql = "SELECT * FROM " + tableName.toUpperCase() + " WHERE 1 != 1";  
	    this.rs = this.statement.executeQuery(sql);  
	    return rs.getMetaData();
	}  
	
	/**
	 * 展示表结构
	 * 参数：表结构（metaData）
	 * */
	protected void displayMetaData(ResultSetMetaData metaData) throws Exception {  
	    StringBuffer sb = new StringBuffer();  
	    for (int i = 0; i < metaData.getColumnCount(); i++) {  
	        sb.append("\n");  
	        sb.append(metaData.getTableName(i + 1));  
	        sb.append(".");  
	        sb.append(metaData.getColumnName(i + 1));  
	        sb.append("|");  
	        sb.append(metaData.getColumnType(i + 1));  
	        sb.append("|");  
	        sb.append(metaData.getColumnTypeName(i + 1));  
	        sb.append("|");  
	        sb.append(metaData.getColumnDisplaySize(i + 1));  
	        sb.append("|");  
	    }   
	}
	
	/**
	 * 把数据库的类型转换到java的类型
	 * 
	 * */
	protected String columnTypeToString(Object object){
		String str=object.toString();
		String type=null;
		if("INT".equals(str)){
			type="int";
		}else if("VARCHAR".equals(str)){
			type="String";
		}else if("CHAR".equals(str)){
			type="String";
		}else if("FLOAT".equals(str)){
			type="Float";
		}else if("DOUBLE".equals(str)){
			type="Double";
		}else if("DECIMAL".equals(str)){
			type="BigDecimal";
		}else if("DATE".equals(str)){
			type="Date";
		}else if("DATETIME".equals(str)){
			type="Timestamp";
		}else{
			type="类型待补充";
			System.out.println("请补充数据库映射类型。数据库类型为："+str);
		}
		return type;
	}
	
	/**
	 * mysql数据库、获取数据的table注释
	 * map<tableName,中文注释>
	 * @throws SQLException 
	 * */
	protected Map<String,String> getTableComment(String tableName) throws SQLException{
		Map<String , String> commentMap=new HashMap<String,String>();
		StringBuffer sql=new StringBuffer();
		//先取表的信息
		sql.append("select table_name,table_comment from information_schema.tables where table_name='"+tableName+"' ");//and table_schema='"+dataName+"'");
		this.statement=this.connection.createStatement();
		this.rs=statement.executeQuery(sql.toString());
		while(rs.next()){
			String tablename=rs.getString("table_name");
			String tablecomment=rs.getString("table_comment");
			commentMap.put(tablename, tablecomment);
		}
		return commentMap;
	}
	
	/**
	 * mysql数据库、获取数据的列表注释
	 * map<tableName,中文注释>、
	 * @throws SQLException 
	 * */
	protected Map<String,String> getColumnComment(String tableName) throws SQLException{
		Map<String , String> commentMap=new HashMap<String,String>();
		StringBuffer sql=new StringBuffer();
		//先取字段的信息
		sql.append("select column_name,column_comment from information_schema.columns where table_name='"+tableName+"'");// and table_schema='"+dataName+"'");
		this.statement=this.connection.createStatement();
		this.rs=statement.executeQuery(sql.toString());
		while(rs.next()){
			String tablename=rs.getString("column_name");
			String tablecomment=rs.getString("column_comment");
			commentMap.put(tablename, tablecomment);
		}
		return commentMap;
	}
	
	
}
