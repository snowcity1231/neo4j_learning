package com.demo.repository;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import com.demo.entity.Movie;

@Repository
public interface MovieRepository extends GraphRepository<Movie>{

}
