package com.zhsw.ssm_tool;

import org.junit.BeforeClass;
import org.junit.Test;

import com.zhsw.tool.DatabaseBean;
import com.zhsw.tool.SsmTool;

public class SsmToolTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void test() throws Exception{
		DatabaseBean databaseBean = new DatabaseBean();
		databaseBean.setDbUri("jdbc:mysql://localhost:3306/test");
		databaseBean.setDriverClass("com.mysql.jdbc.Driver");
		databaseBean.setPassword("root");
		databaseBean.setUserName("root");
		String fileDir="E:\\workspace2\\ssm-tool\\createFile";
		String packageDir="com.zhsw.ssm_tool";
		String dbTableName="user_t";
		String junitPackageDir="com.zhsw.ss_tool.junit";
		String springXml="";
		String springMVCXml="";
		SsmTool tool=new SsmTool(databaseBean, fileDir,packageDir,dbTableName);;
//		try {
//			tool.createPOJOClass("UserBean");
//			tool.createMapperXml("UserMapper");
//			tool.createServiceImplClass("UserServiceImpl");
//			tool.createControllerClass("UserController");
//			tool.createJUnitBaseClassOfSpring(junitPackageDir, springXml, springMVCXml);
//			tool.createJUnitControllerClass("UserControllerJUnit");
//			tool.createJUnitMethodsClass("UserMothodsJUnit");
//		} finally {
//			tool.colse();
//		}
		
		
	}

}
