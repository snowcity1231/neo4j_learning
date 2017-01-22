package com.demo.entity;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

import lombok.Data;

@Data
@NodeEntity(label = "MOVIES")
public class Movie {

	public Movie(String name) {
		this.name = name;
	}
	
	@GraphId
	private Long nodeId;
	@Property(name = "name")
	private String name;
	/**
	 * @return the nodeId
	 */
	public Long getNodeId() {
		return nodeId;
	}
	/**
	 * @param nodeId the nodeId to set
	 */
	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
