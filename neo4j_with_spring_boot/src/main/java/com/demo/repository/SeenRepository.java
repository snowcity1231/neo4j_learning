package com.demo.repository;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.demo.entity.Seen;

public interface SeenRepository extends GraphRepository<Seen> {

}
