package com.demo.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import com.demo.entity.User;

public interface UserRepository extends GraphRepository<User> {
	
	@Query("MATCH (user:USERS {name:{name}}) RETURN user")
	User getUserByName(@Param("name") String name);
	
}
