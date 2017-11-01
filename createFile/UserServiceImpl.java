package com.zhsw.ssm_tool;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl{
	@Autowired
	UserMapper userMapper;
	/**
	*查询：（user_t）信息
	*@throws Exception
	**/
	public List<UserBean> select(UserBean userBean) throws Exception{
		return userMapper.select(userBean);
	}
	/**
	*新增：（user_t）信息
	*@throws Exception
	**/
	public int insert(UserBean userBean) throws Exception{
		return userMapper.insert(userBean);
	}
	/**
	*更新：（user_t）信息
	*@throws Exception
	**/
	public int update(UserBean userBean) throws Exception{
		return userMapper.update(userBean);
	}
	/**
	*删除：（user_t）信息
	*@throws Exception
	**/
	public void delete(UserBean userBean) throws Exception{
		userMapper.delete(userBean);
	}
}