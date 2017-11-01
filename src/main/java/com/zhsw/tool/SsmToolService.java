package com.zhsw.tool;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class SsmToolService {
	
	private DatabaseService dbService;
	
	/**
	 * 初始化工具类
	 * @param databaseBean:数据库实体类，fileDir:生成的文件存放的目录
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * */
	protected SsmToolService(DatabaseBean databaseBean) throws ClassNotFoundException, SQLException{
		this.dbService=new DatabaseService(databaseBean);
		dbService.init();
	}
	
	protected void colse() throws SQLException{
		dbService.closeConnectionAndResultSet();
	}
	
	/**
	 * 生成简单的实体类
	 * @param className:实体类的名称，dbTableName:数据库的表名称，packageDir:文件的包目录
	 * @throws Exception 
	 * */
	protected String createPOJOClass(String className,String dbTableName,String packageDir) throws Exception{
		ResultSetMetaData metaData =dbService.getMetaDataFromTable(dbTableName);
		StringBuffer sb = new StringBuffer();  
		Date now = new Date(); 
		Map<String ,String> tableComment=dbService.getTableComment(dbTableName);//获取数据库表注释
		Map<String ,String> columnComment=dbService.getColumnComment(dbTableName);//获取数据库字段注释
		String tableMsg=null;
		//取出table注释信息
		Iterator<Entry<String, String>> itr=null;
		if(tableComment!=null){
			itr=tableComment.entrySet().iterator();
			while(itr.hasNext()){
				Map.Entry<String, String> entry=(Map.Entry<String, String>)itr.next();
				tableMsg="表："+entry.getValue()+"（"+entry.getKey()+"）实体类信息";
			}
		}
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		sb.append("package "+packageDir+";");
		sb.append("\r\n");
		sb.append("\r\n");
		sb.append("/**\r\n * "+tableMsg+"\r\n * 作者：郑书文\r\n * 生成日期："+sdf.format(now)+"\r\n * */");
		sb.append("\r\n");
		sb.append("public class "+className+"{");
		sb.append("\r\n");
		//设置定义
		for(int i=0; i<metaData.getColumnCount(); i++){
			//获取字段注释
			String value=null;
			if(columnComment!=null){
				value=columnComment.get(metaData.getColumnName(i + 1));
			}
			if(value==null||value.equals("")){
				value=metaData.getColumnName(i + 1);
			}
			sb.append("\t/**\r\n\t * "+value+"\r\n\t * */\r\n");
			sb.append("\t");
			//private String userName;
			sb.append("private " +dbService.columnTypeToString(metaData.getColumnTypeName(i+1))+" "+metaData.getColumnName(i + 1).toLowerCase()+";");
			sb.append("\r\n");
		}
		//设置get、set
		for(int i=0; i<metaData.getColumnCount(); i++){
			//获取字段注释
			String value=null;
			if(columnComment!=null){
				value=columnComment.get(metaData.getColumnName(i + 1));
			}
			if(value==null||value.equals("")){
				value=metaData.getColumnName(i + 1);
			}
			sb.append("\r\n");
			sb.append("\r\n");
			sb.append("\r\n\t/**\r\n\t * 设置："+value+"\r\n\t * */\r\n");
			sb.append("\t");
			sb.append("public void set"+
				metaData.getColumnName(i+1).substring(0, 1).toUpperCase()+
				metaData.getColumnName(i+1).substring(1).toLowerCase()+
				"("+dbService.columnTypeToString(metaData.getColumnTypeName(i+1))+" "+
				metaData.getColumnName(i + 1).toLowerCase()+"){");
			sb.append("\r\n");
			sb.append("\t");
			sb.append("\t");
			sb.append("this."+metaData.getColumnName(i + 1).toLowerCase()+"="+metaData.getColumnName(i + 1).toLowerCase()+";");
			sb.append("\r\n");
			sb.append("\t");
			sb.append("};");
			sb.append("\r\n\r\n");
			sb.append("\r\n\t/**\r\n\t * 获取："+value+"\r\n\t * */\r\n");
			sb.append("\t");
			sb.append("public "+dbService.columnTypeToString(metaData.getColumnTypeName(i+1))+" get"+
				metaData.getColumnName(i+1).substring(0, 1).toUpperCase()+
				metaData.getColumnName(i+1).substring(1).toLowerCase()+"(){");
			sb.append("\r\n");
			sb.append("\t");
			sb.append("\t");
			sb.append("return "+metaData.getColumnName(i + 1).toLowerCase()+";");
			sb.append("\r\n");
			sb.append("\t");
			sb.append("};");
		}
		sb.append("\r\n");
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * 生成mybatis的xml文件
	 * @param xmlName:xml文件的名称，dbTableName:数据库的名称，namespace:与xml关联的java接口类，beanType:数据库对于的实体
	 * @throws Exception 
	 * */
	protected String createMapperXml(String xmlName,String dbTableName,String namespace,String beanType) throws Exception{
		ResultSetMetaData metaData =dbService.getMetaDataFromTable(dbTableName);
		StringBuffer sb = new StringBuffer();  
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("\r\n");
		sb.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
		sb.append("\r\n");
		sb.append("<!--interface mapper-->\r\n<mapper namespace=\""+namespace+"\">");
		sb.append("\r\n");
		sb.append("<!--map is a id / model is a entity(has geter/seter)-->\r\n");
		sb.append("\t<resultMap id=\"map\" type=\""+beanType+"\">");
		// <result column="title" property="title" jdbcType="VARCHAR" />
		for(int i=0; i<metaData.getColumnCount(); i++){
			sb.append("\r\n");
			sb.append("\t\t<result column=\""+metaData.getColumnName(i+1)+
					"\" property=\""+metaData.getColumnName(i + 1).toLowerCase()+
					"\" />");
		}
		sb.append("\r\n");
		sb.append("\t</resultMap>");
		sb.append("\r\n");
		//base_column_list
		sb.append("\r\n");
		sb.append("\t<sql id=\"Base_Column_List\" >");
		sb.append("\r\n");
		sb.append("\t\t");
		for(int i=0; i<metaData.getColumnCount(); i++){
			if(i==0){
				sb.append(metaData.getColumnName(i+1));
			}else{
				sb.append(","+metaData.getColumnName(i+1));
			}
		}
		sb.append("\r\n");
		sb.append("\t</sql>");
		//select
		sb.append("\r\n");
		sb.append("\r\n");
		sb.append("\t<select id=\"select\"  parameterType=\""+beanType+"\" resultMap=\"map\">");
		sb.append("\r\n");
		sb.append("\t\tselect");
		sb.append("\r\n");
		sb.append("\t\t  <include refid=\"Base_Column_List\" />");
		sb.append("\r\n");
		sb.append("\t\tfrom "+metaData.getTableName(1));
		sb.append("\r\n");
		sb.append("\t\t<trim prefix=\"WHERE\" prefixOverrides=\"AND |OR\">");
		for(int i=0; i<metaData.getColumnCount(); i++){
			sb.append("\r\n");
			sb.append("\t\t\t<if test=\""+metaData.getColumnName(i + 1).toLowerCase()+" != null and "+metaData.getColumnName(i + 1).toLowerCase()+"!=''\">\r\n \t\t\t\tAND "+
				metaData.getColumnName(i+1)+"=#{"+metaData.getColumnName(i + 1).toLowerCase()+"}\r\n\t\t\t</if>");
		}
		sb.append("\r\n");
		sb.append("\t\t</trim>");
		sb.append("\r\n");
		sb.append("\t</select>");
		sb.append("\r\n");
		//update
		sb.append("\t<update id=\"update\" parameterType=\""+beanType+"\">\r\n");
		sb.append("\t\tupdate "+metaData.getTableName(1)+" \r\n");
		sb.append("\t\t<trim prefix=\"set\" suffixOverrides=\",\">\r\n" );
		for(int i=0; i<metaData.getColumnCount(); i++){
			sb.append("\t\t\t<if test=\""+metaData.getColumnName(i + 1).toLowerCase()+"!= null and "+metaData.getColumnName(i + 1).toLowerCase()+"!=''\">\r\n \t\t\t\t "+
				metaData.getColumnName(i+1)+"=#{"+metaData.getColumnName(i + 1).toLowerCase()+"}, \r\n\t\t\t</if>\r\n");
		}
		sb.append("\t\t</trim>");
		sb.append("\r\n");
		sb.append("\t\t<trim prefix=\"WHERE\" prefixOverrides=\"AND |OR\">\r\n");
		for(int i=0; i<metaData.getColumnCount(); i++){
			sb.append("\t\t\t<if test=\""+metaData.getColumnName(i + 1).toLowerCase()+" != null and "+metaData.getColumnName(i + 1).toLowerCase()+"!=''\">\r\n \t\t\t\tAND "+
				metaData.getColumnName(i+1)+"=#{"+metaData.getColumnName(i + 1).toLowerCase()+"}\r\n\t\t\t</if>\r\n");
		}
		sb.append("\t\t</trim>\r\n");
		sb.append("\t</update>\r\n");
		//insert insert into() value();
		sb.append("\t<insert id=\"insert\" parameterType=\""+beanType+"\">\r\n");
		sb.append("\t\tinsert into "+metaData.getTableName(1)+"\r\n");
		sb.append("\t\t<trim prefix=\"(\" suffix=\")\" prefixOverrides=\"AND |OR\" suffixOverrides=\",\">\r\n");
		for(int i=0; i<metaData.getColumnCount(); i++){
			sb.append("\t\t\t<if test=\""+metaData.getColumnName(i + 1).toLowerCase()+" != null and "+
					metaData.getColumnName(i + 1).toLowerCase()+"!=''\">\r\n\t\t\t\t"+
					""+metaData.getColumnName(i + 1)+",\r\n\t\t\t</if>\r\n");
		}
		sb.append("\t\t</trim>\r\n");
		sb.append("\t\tvalues\r\n");
		sb.append("\t\t<trim prefix=\"(\" suffix=\")\" prefixOverrides=\"AND |OR\" suffixOverrides=\",\">\r\n");
		for(int i=0; i<metaData.getColumnCount(); i++){
			sb.append("\t\t\t<if test=\""+metaData.getColumnName(i + 1).toLowerCase()+" != null and "+
					metaData.getColumnName(i + 1).toLowerCase()+"!=''\">\r\n\t\t\t\t"+
					"#{"+metaData.getColumnName(i + 1).toLowerCase()+"},\r\n\t\t\t</if>\r\n");
		}
		sb.append("\t\t</trim>\r\n");
		sb.append("\t</insert>\r\n");
		//delete delete from tableName where id=#{id}
		sb.append("\t<delete  id=\"delete\" parameterType=\""+beanType+"\">\r\n");
		sb.append("\t\tdelete from "+metaData.getTableName(1)+"\r\n");
		sb.append("\t\t<trim prefix=\"WHERE\" prefixOverrides=\"AND |OR\">\r\n");
		for(int i=0; i<metaData.getColumnCount(); i++){
			sb.append("\t\t\t<if test=\""+metaData.getColumnName(i + 1).toLowerCase()+" != null and "+metaData.getColumnName(i + 1).toLowerCase()+"!=''\">\r\n \t\t\t\tAND "+
				metaData.getColumnName(i+1)+"=#{"+metaData.getColumnName(i + 1).toLowerCase()+"}\r\n\t\t\t</if>\r\n");
		}
		sb.append("\t\t</trim>\r\n");
		sb.append("\t</delete>");
		sb.append("\r\n");
		sb.append("\r\n");
		sb.append("</mapper>");
		return sb.toString();
	}
	
	/**
	 * 生成controller类
	 * @param ControllerClassName:控制类的名称，dbTableName:数据库的表名称，packageDir:package目录，serviceImplName:impl的名称，entityName:pojo类的名称
	 * @throws SQLException 
	 * */
	protected String createControllerClass(String controllerClassName,String dbTableName,String packageDir,String serviceImplName,String entityName) throws SQLException{
		StringBuffer sb=new StringBuffer();
		Map<String ,String> tableComment = dbService.getTableComment(dbTableName);
	     
		String tableMsg=null;
		//取出table注释信息
		Iterator<Entry<String, String>> itr=null;
		if(tableComment!=null){
			itr=tableComment.entrySet().iterator();
			while(itr.hasNext()){
				Map.Entry<String, String> entry=(Map.Entry<String, String>)itr.next();
				tableMsg=entry.getValue()+"（"+entry.getKey()+"）";
			}
		}
		//获取包名（com.user to user）
		String str=packageDir.substring(packageDir.lastIndexOf(".")+1);
		
		sb.append("package "+packageDir+";\r\n\r\n");
		sb.append("import org.springframework.beans.factory.annotation.Autowired;\r\n");
		sb.append("import org.springframework.stereotype.Controller;\r\n");
		sb.append("import org.springframework.web.bind.annotation.RequestMapping;\r\n");
		sb.append("import javax.servlet.http.HttpServletRequest;\r\n");
		sb.append("import javax.servlet.http.HttpServletResponse;\r\n");
		sb.append("import org.springframework.ui.ModelMap;\r\n");
		sb.append("import java.util.List;\r\n\r\n\r\n");
		sb.append("@Controller\r\n");
		sb.append("public class "+controllerClassName+"{\r\n");
		sb.append("\t@Autowired\r\n");
		sb.append("\t"+serviceImplName+" "+serviceImplName.substring(0,1).toLowerCase()+serviceImplName.substring(1)+";\r\n\r\n");
		sb.append("\t/**\r\n\t*查询："+tableMsg+"信息\r\n\t*@throws Exception\r\n\t**/\r\n");
		sb.append("\t@RequestMapping(\"/"+str+"/select\")\r\n");
		sb.append("\tpublic void select("+entityName+" "+entityName.substring(0,1).toLowerCase()+entityName.substring(1)+",ModelMap model,HttpServletRequest request) throws Exception{ \r\n");
		sb.append("\t\t"+entityName+" "+entityName.substring(0,1).toLowerCase()+entityName.substring(1)+"=new "+entityName+"();\r\n");
		sb.append("\t\tList<"+entityName+"> listMsg = "+serviceImplName.substring(0,1).toLowerCase()+serviceImplName.substring(1)+".select("+entityName.substring(0,1).toLowerCase()+entityName.substring(1)+");\r\n");
		sb.append("\t}\r\n\r\n");
		
		sb.append("\t/**\r\n\t*新增："+tableMsg+"信息\r\n\t*@throws Exception\r\n\t**/\r\n");
		sb.append("\t@RequestMapping(\"/"+str+"/insert\")\r\n");
		sb.append("\tpublic void insert("+entityName+" "+entityName.substring(0,1).toLowerCase()+entityName.substring(1)+",ModelMap model,HttpServletResponse response, HttpServletRequest request) throws Exception{\r\n");
		sb.append("\t\t"+serviceImplName.substring(0,1).toLowerCase()+serviceImplName.substring(1)+".insert("+entityName.substring(0,1).toLowerCase()+entityName.substring(1)+");\r\n");
		sb.append("\t}\r\n\r\n");
		
		
		sb.append("\t/**\r\n\t*更新："+tableMsg+"信息\r\n\t*@throws Exception\r\n\t**/\r\n");
		sb.append("\t@RequestMapping(\"/"+str+"/update\")\r\n");
		sb.append("\tpublic void update("+entityName+" "+entityName.substring(0,1).toLowerCase()+entityName.substring(1)+",ModelMap model,HttpServletResponse response, HttpServletRequest request) throws Exception{\r\n");
		sb.append("\t\t"+serviceImplName.substring(0,1).toLowerCase()+serviceImplName.substring(1)+".update("+entityName.substring(0,1).toLowerCase()+entityName.substring(1)+");\r\n");
		sb.append("\t}\r\n\r\n");
		
		
		sb.append("\t/**\r\n\t*删除："+tableMsg+"信息\r\n\t*@throws Exception\r\n\t**/\r\n");
		sb.append("\t@RequestMapping(\"/"+str+"/delete\")\r\n");
		sb.append("\tpublic void delete("+entityName+" "+entityName.substring(0,1).toLowerCase()+entityName.substring(1)+",ModelMap model,HttpServletResponse response, HttpServletRequest request) throws Exception{\r\n");
		sb.append("\t\t"+serviceImplName.substring(0,1).toLowerCase()+serviceImplName.substring(1)+".delete("+entityName.substring(0,1).toLowerCase()+entityName.substring(1)+");\r\n");
		sb.append("\t}\r\n\r\n");
		
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * 生成serviceImpl类
	 * @throws SQLException 
	 * 
	 * */
	protected String createServiceImplClass(String serviceImplClassName,String dbTableName,String packageDir,String mapperName,String entityName) throws SQLException{
		StringBuffer sb=new StringBuffer();
		Map<String ,String> tableComment=dbService.getTableComment(dbTableName);
		String tableMsg=null;
		//取出table注释信息
		Iterator<Entry<String, String>> itr=null;
		if(tableComment!=null){
			itr=tableComment.entrySet().iterator();
			while(itr.hasNext()){
				Map.Entry<String, String> entry=(Map.Entry<String, String>)itr.next();
				tableMsg=entry.getValue()+"（"+entry.getKey()+"）";
			}
		}
		sb.append("package "+packageDir+";\r\n\r\n");
		sb.append("import java.util.List;\r\nimport org.springframework.beans.factory.annotation.Autowired;\r\nimport org.springframework.stereotype.Service;\r\n\r\n");
		sb.append("@Service\r\n");
		sb.append("public class "+serviceImplClassName+"{\r\n");
		sb.append("\t@Autowired\r\n");
		sb.append("\t"+mapperName+" "+mapperName.substring(0,1).toLowerCase()+mapperName.substring(1)+";\r\n");
//		public List<Sys_userModel> selectMsg(Sys_userModel entity) throws Exception{
//			return sys_userMapper.select(entity);	  
//		}
		sb.append("\t/**\r\n\t*查询："+tableMsg+"信息\r\n\t*@throws Exception\r\n\t**/\r\n");
		sb.append("\tpublic List<"+entityName+"> select("+entityName+" "+entityName.substring(0,1).toLowerCase()+entityName.substring(1)+") throws Exception{\r\n");
		sb.append("\t\treturn "+mapperName.substring(0,1).toLowerCase()+mapperName.substring(1)+".select("+entityName.substring(0,1).toLowerCase()+entityName.substring(1)+");\r\n");
		sb.append("\t}\r\n");
		
		sb.append("\t/**\r\n\t*新增："+tableMsg+"信息\r\n\t*@throws Exception\r\n\t**/\r\n");
		sb.append("\tpublic int insert("+entityName+" "+entityName.substring(0,1).toLowerCase()+entityName.substring(1)+") throws Exception{\r\n");
		sb.append("\t\treturn "+mapperName.substring(0,1).toLowerCase()+mapperName.substring(1)+".insert("+entityName.substring(0,1).toLowerCase()+entityName.substring(1)+");\r\n");
		sb.append("\t}\r\n");
		
		sb.append("\t/**\r\n\t*更新："+tableMsg+"信息\r\n\t*@throws Exception\r\n\t**/\r\n");
		sb.append("\tpublic int update("+entityName+" "+entityName.substring(0,1).toLowerCase()+entityName.substring(1)+") throws Exception{\r\n");
		sb.append("\t\treturn "+mapperName.substring(0,1).toLowerCase()+mapperName.substring(1)+".update("+entityName.substring(0,1).toLowerCase()+entityName.substring(1)+");\r\n");
		sb.append("\t}\r\n");
		
		sb.append("\t/**\r\n\t*删除："+tableMsg+"信息\r\n\t*@throws Exception\r\n\t**/\r\n");
		sb.append("\tpublic void delete("+entityName+" "+entityName.substring(0,1).toLowerCase()+entityName.substring(1)+") throws Exception{\r\n");
		sb.append("\t\t"+mapperName.substring(0,1).toLowerCase()+mapperName.substring(1)+".delete("+entityName.substring(0,1).toLowerCase()+entityName.substring(1)+");\r\n");
		sb.append("\t}\r\n");
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * 生成测试类的基类，即所有测试类都需要继承该类
	 * @param packageDir:package目录，springXml:spring的xml配置文件，springMVCXml:springmvc的配置文件
	 * */
	protected String createJUnitBaseClassOfSpring(String packageDir,String springXML,String springmvcXML){
		StringBuffer sb=new StringBuffer();
		sb.append("package "+packageDir+";\r\n\r\n");
		sb.append("import org.junit.runner.RunWith; \r\n"+
				"import org.springframework.transaction.annotation.Transactional; \r\n"+
				"import org.springframework.transaction.annotation.Transactional;\r\n"+
				"import org.springframework.test.context.junit4.SpringJUnit4ClassRunner; \r\n\r\n" +
				"/**\r\n*配置文件载入类 。\r\n*所有的在spring环境的测试类都需要继承此类。\r\n**/\r\n\r\n");
		sb.append("@RunWith(value = SpringJUnit4ClassRunner.class)\r\n");
		sb.append("@ContextConfiguration(locations={\""+springXML+"\",\""+springmvcXML+"\"})\r\n\r\n");
		sb.append("/** 添加注释@Transactional 回滚对数据库操作*/\r\n"+
				"@Transactional\r\n"+"public class JUnitBaseCase { \r\n\t\n}");
		return sb.toString();
	}
	/**
	 * 生成能对controller类进行测试的junit类
	 * */
	protected String createJUnitControllerClass(String packageDir,String junitName){
		StringBuffer sb=new StringBuffer();
		sb.append("package "+packageDir+";\r\n\r\n");
		sb.append("import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;\r\n" +
				"import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;\r\n" +
				"import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;\r\n" +
				"import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;\r\n\r\n" +
				"import org.junit.Before;\r\n" +
				"import org.junit.Test;\r\n" +
				"import org.springframework.beans.factory.annotation.Autowired;\r\n" +
				"import org.springframework.test.context.web.WebAppConfiguration;\r\n" +
				"import org.springframework.test.web.servlet.MockMvc;\r\n" +
				"import org.springframework.web.context.WebApplicationContext;\r\n\r\n");
		sb.append("/**\r\n * 模拟web中的request请求，测试Controller类的controller方法\r\n**/\r\n\r\n");
		sb.append("@WebAppConfiguration\r\n");
		sb.append("public class "+junitName+" extends JUnitBase{\r\n");
		sb.append("\t@Autowired\r\n" +
				"\tWebApplicationContext wac;\r\n\r\n" +
				"\tprivate MockMvc mockMvc;\r\n\r\n" +
				"\t@Before\r\n" +
				"\tpublic void setup(){\r\n" +
				"\t\tthis.mockMvc = webAppContextSetup(this.wac).build(); \r\n" +
				"\t}\r\n\r\n");
		sb.append("\t@Test\r\n" +
				"\tpublic void controllerJUnit() throws Exception {\r\n" +
				"\t\tmockMvc.perform((post(\"/select\").param(\"user_name\", \"zhssw\"))).andExpect(status().isOk()).andDo(print()); \r\n" +
				"\t}\r\n");
		sb.append("}");
		return sb.toString();	
	}
	/**
	 * 生成能对普通方法进行测试的junit类
	 * */
	protected String createJUnitMethodsClass(String packageDir,String junitName){
		StringBuffer sb=new StringBuffer();
		sb.append("package "+packageDir+";\r\n\r\n");
		sb.append("import static org.junit.Assert.*;\r\n\r\n" +
				"import org.junit.Before;\r\n" +
				"import org.junit.Test;\r\n" +
				"import org.springframework.beans.factory.annotation.Autowired;\r\n\r\n");
		sb.append("/**\r\n* 主要用于测试controller里的非controller类的方法 \r\n* */\r\n" +
				"public class "+junitName+" extends BaseCase{\r\n\r\n");
//		for(int i=0;i<serviceImplName.length;i++){
//			sb.append("\t@Autowired\r\n\t" +
//					serviceImplName[i]+" "+serviceImplName[i].substring(0,1).toLowerCase()+serviceImplName[i].substring(1)+";\r\n\r\n");
//		}
		sb.append("//\t@Autowired\r\n//\tServiceImpl service;\r\n\r\n");
		
		sb.append("\t// 执行测试方法之前的初始化工作\r\n" +
				"\t@Before\r\n" +
				"\tpublic void setUp(){ \r\n\r\n\t}\r\n");
//		@Test
//		public void TestSelect() throws Exception{
		sb.append("\t//测试方法\r\n" +
				"\t@Test\r\n" +
				"\tpublic void methodJUnit() throws Exception{\r\n" +
				"\t\t//value:class.method方法的期望值，class.method:需要测试的方法。\r\n"+
				"\t\t//assertEquals(value,class.method);\r\n" +
				"\t}\r\n");
		sb.append("}");
		return sb.toString();
	}
}
