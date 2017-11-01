<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--interface mapper-->
<mapper namespace="null.null">
<!--map is a id / model is a entity(has geter/seter)-->
	<resultMap id="map" type="null.UserBean">
		<result column="id" property="id" />
		<result column="user_name" property="user_name" />
		<result column="password" property="password" />
		<result column="age" property="age" />
	</resultMap>

	<sql id="Base_Column_List" >
		id,user_name,password,age
	</sql>

	<select id="select"  parameterType="null.UserBean" resultMap="map">
		select
		  <include refid="Base_Column_List" />
		from user_t
		<trim prefix="WHERE" prefixOverrides="AND |OR">
			<if test="id != null and id!=''">
 				AND id=#{id}
			</if>
			<if test="user_name != null and user_name!=''">
 				AND user_name=#{user_name}
			</if>
			<if test="password != null and password!=''">
 				AND password=#{password}
			</if>
			<if test="age != null and age!=''">
 				AND age=#{age}
			</if>
		</trim>
	</select>
	<update id="update" parameterType="null.UserBean">
		update user_t 
		<trim prefix="set" suffixOverrides=",">
			<if test="id!= null and id!=''">
 				 id=#{id}, 
			</if>
			<if test="user_name!= null and user_name!=''">
 				 user_name=#{user_name}, 
			</if>
			<if test="password!= null and password!=''">
 				 password=#{password}, 
			</if>
			<if test="age!= null and age!=''">
 				 age=#{age}, 
			</if>
		</trim>
		<trim prefix="WHERE" prefixOverrides="AND |OR">
			<if test="id != null and id!=''">
 				AND id=#{id}
			</if>
			<if test="user_name != null and user_name!=''">
 				AND user_name=#{user_name}
			</if>
			<if test="password != null and password!=''">
 				AND password=#{password}
			</if>
			<if test="age != null and age!=''">
 				AND age=#{age}
			</if>
		</trim>
	</update>
	<insert id="insert" parameterType="null.UserBean">
		insert into user_t
		<trim prefix="(" suffix=")" prefixOverrides="AND |OR" suffixOverrides=",">
			<if test="id != null and id!=''">
				id,
			</if>
			<if test="user_name != null and user_name!=''">
				user_name,
			</if>
			<if test="password != null and password!=''">
				password,
			</if>
			<if test="age != null and age!=''">
				age,
			</if>
		</trim>
		values
		<trim prefix="(" suffix=")" prefixOverrides="AND |OR" suffixOverrides=",">
			<if test="id != null and id!=''">
				#{id},
			</if>
			<if test="user_name != null and user_name!=''">
				#{user_name},
			</if>
			<if test="password != null and password!=''">
				#{password},
			</if>
			<if test="age != null and age!=''">
				#{age},
			</if>
		</trim>
	</insert>
	<delete  id="delete" parameterType="null.UserBean">
		delete from user_t
		<trim prefix="WHERE" prefixOverrides="AND |OR">
			<if test="id != null and id!=''">
 				AND id=#{id}
			</if>
			<if test="user_name != null and user_name!=''">
 				AND user_name=#{user_name}
			</if>
			<if test="password != null and password!=''">
 				AND password=#{password}
			</if>
			<if test="age != null and age!=''">
 				AND age=#{age}
			</if>
		</trim>
	</delete>

</mapper>