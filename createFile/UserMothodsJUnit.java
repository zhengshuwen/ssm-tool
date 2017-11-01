package com.zhsw.ss_tool.junit;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
* 主要用于测试controller里的非controller类的方法 
* */
public class UserMothodsJUnit extends BaseCase{

	@Autowired
	 ServiceImpl service;

	// 执行测试方法之前的初始化工作
	@Before
	public void setUp(){ 

	}
	//测试方法
	@Test
	public void methodJUnit() throws Exception{
		//value:class.method方法的期望值，class.method:需要测试的方法。
		//assertEquals(value,class.method);
	}
}