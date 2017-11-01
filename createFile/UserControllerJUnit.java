package com.zhsw.ss_tool.junit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

/**
 * 模拟web中的request请求，测试Controller类的controller方法
**/

@WebAppConfiguration
public class UserControllerJUnitextends BaseCase{
	@Autowired
	WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup(){
		this.mockMvc = webAppContextSetup(this.wac).build(); 
	}

	@Test
	public void controllerJUnit() throws Exception {
		mockMvc.perform((post("/select").param("user_name", "zhssw"))).andExpect(status().isOk()).andDo(print()); 
	}
}