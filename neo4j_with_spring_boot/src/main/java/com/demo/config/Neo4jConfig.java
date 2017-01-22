package com.demo.config;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
/**
 * 新建类Neo4jConfig，用于初始化Neo4j连接
 * @author Administrator
 *
 */
@Configuration
@EnableNeo4jRepositories("com.demo.repository")
@EnableTransactionManagement
public class Neo4jConfig extends Neo4jConfiguration {
	
	@Bean
	public org.neo4j.ogm.config.Configuration getConfiguration() {
		org.neo4j.ogm.config.Configuration config = new org.neo4j.ogm.config.Configuration();
		config.driverConfiguration()
				.setDriverClassName("org.neo4j.ogm.drivers.http.driver.HttpDriver")
				.setURI("http://neo4j:123456@localhost:7474");	//用户名neo4j，密码123456，连接localhost:7474
		return config;
	}

	@Override
	public SessionFactory getSessionFactory() {
		return new SessionFactory(getConfiguration(), "com.demo.entity");
	}

	
}
