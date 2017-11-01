package com.zhsw.ssm_tool;

 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.ui.ModelMap;
import java.util.List;


@Controller
public class UserController{
	@Autowired
	UserServiceImpl userServiceImpl;

	/**
	*查询：（user_t）信息
	*@throws Exception
	**/
	@RequestMapping("/ssm_tool/select")
	public void select(UserBean userBean,ModelMap model,HttpServletRequest request) throws Exception{ 
		UserBean userBean=new UserBean();
		List<UserBean> listMsg = userServiceImpl.select(userBean);
	}

	/**
	*新增：（user_t）信息
	*@throws Exception
	**/
	@RequestMapping("/ssm_tool/insert")
	public void insert(UserBean userBean,ModelMap model,HttpServletResponse response, HttpServletRequest request) throws Exception{
		userServiceImpl.insert(userBean);
	}

	/**
	*更新：（user_t）信息
	*@throws Exception
	**/
	@RequestMapping("/ssm_tool/update")
	public void update(UserBean userBean,ModelMap model,HttpServletResponse response, HttpServletRequest request) throws Exception{
		userServiceImpl.update(userBean);
	}

	/**
	*删除：（user_t）信息
	*@throws Exception
	**/
	@RequestMapping("/ssm_tool/delete")
	public void delete(UserBean userBean,ModelMap model,HttpServletResponse response, HttpServletRequest request) throws Exception{
		userServiceImpl.delete(userBean);
	}

}