package com.zhsw.tool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

public class SsmTool {
//	tool.displayMetaData( tool.getMetaDataFromTable(tableName));
//	tool.metaDataToDaoClass("com.xss.user",fileUrl,"UserEntity", tool.getMetaDataFromTable(tableName));
//	tool.metaDataToXML("com.xss.user.UserMapper","com.xss.user.UserEntity",fileUrl, "UserMapper", tool.getMetaDataFromTable(tableName));
//	tool.metaDataToMapper(fileUrl, "com.xss.user","UserMapper","UserEntity", tool.getMetaDataFromTable(tableName));
//	tool.toSpringMvcController(fileUrl, "com.xss.user", "UserController", "UserServiceImpl", "UserEntity");
//	tool.toServiceImplFile(fileUrl, "com.xss.user", "UserServiceImpl", "UserMapper", "UserEntity");
	
//	tool.getJUnitBaseCaseOnSpring(fileUrl, "com.junit", "classpath:applicationContext.xml", "classpath:springMVC.xml");
//	tool.getControllerJunitCase(fileUrl, "com.test", "userControllerJUnit");
//	tool.getMethodsJUnitCase(fileUrl, "com.junit", "serviceImplJUnit",new String[]{"UserServiceImpl"});
//	tool.closeConnectionAndResultSet();
	/**生成的文件所在目录*/
	private String fileDir;
	/***/
	private String packageDir;
	
	private String junitPackageDir;
	
	private String dbTableName;
	
	private SsmToolService toolService;
	
	private String pojoClassName;
	
	private String mapperClassName;
	
	private String serviceImplClassName;
	/**
	 * 初始化工具类
	 * @param databaseBean:数据库实体类，fileDir:生成的文件存放的目录，packageDir:package目录，dbTableName:数据库的表名称
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * */
	public SsmTool(DatabaseBean databaseBean,String fileDir,String packageDir,String dbTableName) throws ClassNotFoundException, SQLException{
		toolService=new SsmToolService(databaseBean);
		this.fileDir=fileDir;
		this.packageDir=packageDir;
		this.dbTableName=dbTableName;
	}
	
	/**
	 * 关闭工具
	 * @throws SQLException 
	 * */
	public void colse() throws SQLException{
		toolService.colse();
	}
	
	private void createFile(File file) throws Exception{
		if(file.exists()){
			file.delete();
			file.createNewFile();
		}else{
			file.createNewFile();
		}
	}
	
	/**
	 * 生成简单的实体类
	 * @param className:实体类的名称，dbTableName:数据库的表名称
	 * @throws FileNotFoundException 
	 * @throws Exception 
	 * */
	public void createPOJOClass(String className) throws FileNotFoundException ,Exception {
		this.pojoClassName=className;
		File file=new File(this.fileDir+File.separatorChar+className+".java");
		createFile(file);
		FileOutputStream out=new FileOutputStream(file);	
		String fileString=toolService.createPOJOClass(className, this.dbTableName,this.packageDir);
		out.write(fileString.getBytes());
		out.close();
	}
	
	/**
	 * 生成mybatis的xml文件
	 * 环境需求：pojo类(bean)
	 * @param xmlName:xml文件的名称，dbTableName:数据库的名称
	 * @throws FileNotFoundException 
	 * @throws Exception 
	 * */
	public void createMapperXml(String xmlName) throws FileNotFoundException,Exception{
		this.mapperClassName=xmlName;
		File file=new File(this.fileDir+File.separatorChar+xmlName+".xml");
		createFile(file);
		FileOutputStream out=new FileOutputStream(file);	
		String fileString=toolService.createMapperXml(xmlName, this.dbTableName,this.packageDir+"."+this.mapperClassName,this.packageDir+"."+this.pojoClassName);
		out.write(fileString.getBytes());
		out.close();
	}
	
	/**
	 * 生成controller类
	 * 环境需求：需要先生成ServiceImpl类和pojo类(bean)
	 * @param ControllerClassName:控制类的名称，dbTableName:数据库的表名称
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws Exception
	 * */
	public void createControllerClass(String controllerClassName) throws SQLException, IOException,Exception{
		File file=new File(this.fileDir+File.separatorChar+controllerClassName+".java");
		createFile(file);
		FileOutputStream out=new FileOutputStream(file);
		String fileString=toolService.createControllerClass(controllerClassName, this.dbTableName,this.packageDir,this.serviceImplClassName,this.pojoClassName);
		out.write(fileString.getBytes());
		out.close();
	};
	
	/**
	 * 生成serviceImpl类
	 * 环境需求：需要先生成pojo类(bean).和mapper类
	 * @param serviceImplClassName:serviceImpl类的名称
	 * @throws FileNotFoundException 
	 * @throws Exception 
	 * 
	 * */
	public void createServiceImplClass(String serviceImplClassName) throws FileNotFoundException,Exception {
		this.serviceImplClassName=serviceImplClassName;
		File file=new File(this.fileDir+File.separatorChar+serviceImplClassName+".java");
		createFile(file);
		FileOutputStream out=new FileOutputStream(file);
		String fileString = toolService.createServiceImplClass(serviceImplClassName, this.dbTableName,this.packageDir,this.mapperClassName,this.pojoClassName);
		out.write(fileString.getBytes());
		out.close();
	};
	
	/**
	 * 生成测试类的基类，即所有测试类都需要继承该类
	 * 测试环境：spring springmvc
	 * 作用：加载配置文件
	 * @param springXml:spring的xml配置文件，springMVCXml:springmvc的配置文件
	 * @throws FileNotFoundException,Exception 
	 * @throws Exception 
	 * */
	public void createJUnitBaseClassOfSpring(String junitPackageDir,String springXml,String springMVCXml) throws FileNotFoundException,Exception{
		this.junitPackageDir=junitPackageDir;
		File file=new File(this.fileDir+File.separatorChar+"JUnitBase.java");
		createFile(file);
		FileOutputStream out=new FileOutputStream(file);
		String fileString = toolService.createJUnitBaseClassOfSpring(this.junitPackageDir,springXml,springMVCXml);
		out.write(fileString.getBytes());
		out.close();
	}
	/**
	 * 生成能对controller类进行测试的junit类
	 * 测试环境：junit4 spring springmvc
	 * @param junitName:类名
	 * @throws Exception 
	 * */
	public void createJUnitControllerClass(String junitName) throws Exception{
		File file=new File(this.fileDir+File.separatorChar+junitName+".java");
		createFile(file);
		FileOutputStream out=new FileOutputStream(file);
		String fileString = toolService.createJUnitControllerClass(this.junitPackageDir,junitName);
		out.write(fileString.getBytes());
		out.close();
	}
	/**
	 * 生成能对普通方法进行测试的junit类
	 * 测试环境：junit4 spring springmvc
	 * @throws Exception 
	 * */
	public void createJUnitMethodsClass(String junitName) throws Exception{
		File file=new File(this.fileDir+File.separatorChar+junitName+".java");
		createFile(file);
		FileOutputStream out=new FileOutputStream(file);
		String fileString = toolService.createJUnitMethodsClass(this.junitPackageDir,junitName);
		out.write(fileString.getBytes());
		out.close();
	}
}
