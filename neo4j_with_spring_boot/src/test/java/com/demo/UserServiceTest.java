package com.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.demo.entity.User;
import com.demo.service.UserService;

/** 
* @ClassName: UserServiceTest 
* @Description: 单元测试
* @author xuechen
* @date 2017年1月22日 下午5:26:50
*  
*/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringbootNeo4jApplication.class)
public class UserServiceTest {

	@Autowired
	private UserService userService;
	/**
	 * 因为是通过http连接到Neo4j数据库的，所以要预先启动Neo4j：neo4j console
	 */
	@Test
	public void testInitData(){
	    userService.initData();
	}
	@Test
	public void testGetUserByName(){
	    User user = userService.getUserByName("John Johnson");
	    System.out.println(user.toString());
	}
}
