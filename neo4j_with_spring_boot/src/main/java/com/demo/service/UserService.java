package com.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.entity.Movie;
import com.demo.entity.Seen;
import com.demo.entity.User;
import com.demo.repository.MovieRepository;
import com.demo.repository.SeenRepository;
import com.demo.repository.UserRepository;
import com.google.common.collect.Lists;
@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private SeenRepository seenRepository;

	public void initData() {
		
		//初始化时，先删除旧数据
		userRepository.deleteAll();
		movieRepository.deleteAll();
		seenRepository.deleteAll();
		
		//初始化用户
		User user1 = new User("John Johnson");
		User user2 = new User("Kate Smith");
		User user3 = new User("Jack jeffris");
		
		//为John添加朋友关系
		user1.setFriends(Lists.newArrayList(user2, user3));
		
		//初始化电影
		Movie movie1 = new Movie("Fargo");
		Movie movie2 = new Movie("Alien");
		Movie movie3 = new Movie("Heat");
		
		//初始化HAS_SEEN关系
		Seen hasSeen1 = new Seen(5, user1, movie1);
		Seen hasSeen2 = new Seen(3, user2, movie3);
		Seen hasSeen3 = new Seen(6, user2, movie2);
		Seen hasSeen4 = new Seen(4, user3, movie1);
		Seen hasSeen5 = new Seen(5, user3, movie2);
		
		userRepository.save(Lists.newArrayList(user1, user2, user3));
		movieRepository.save(Lists.newArrayList(movie1, movie2, movie3));
		seenRepository.save(Lists.newArrayList(hasSeen1, hasSeen2, hasSeen3, hasSeen4, hasSeen5));
	}
	
	public User getUserByName(String name) {
		return userRepository.getUserByName(name);
	}
}
