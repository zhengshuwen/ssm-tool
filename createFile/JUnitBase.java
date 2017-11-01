package com.zhsw.ss_tool.junit;

import org.junit.runner.RunWith; 
import org.springframework.transaction.annotation.Transactional; 
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner; 

/**
*配置文件载入类 。
*所有的在spring环境的测试类都需要继承此类。
**/

@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"",""})

/** 添加注释@Transactional 回滚对数据库操作*/
@Transactional
public class JUnitBaseCase { 
	
}